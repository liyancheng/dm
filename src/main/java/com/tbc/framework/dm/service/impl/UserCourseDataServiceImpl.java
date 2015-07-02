package com.tbc.framework.dm.service.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.tbc.framework.dm.common.DMConstant;
import com.tbc.framework.dm.domain.UserCourseDailyStudy;
import com.tbc.framework.dm.domain.UserCourseDataMoveBean;
import com.tbc.framework.dm.service.UserCourseDataService;
import com.tbc.framework.dm.util.JsonStatus;
import com.tbc.framework.dm.util.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCommands;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据迁移业务处理层
 *
 * @author liyancheng
 * @since 7/1/2015 10:48 AM
 */
@Service("userCourseDataService")
public class UserCourseDataServiceImpl implements UserCourseDataService {

    /**
     * 日志记录器
     */
    private static Logger logger = Logger.getLogger(UserCourseDataServiceImpl.class);

    /**
     * OMS datasource
     */
    @Resource
    private DataSource omsDataSource;
    @Resource
    private JedisCommands jedisMdm;

    @Override
    @PostConstruct
    public void moveCorpDataFromOMS() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = omsDataSource.getConnection();
            List<UserCourseDataMoveBean> userCourseDataMoveBeanList = getCorpList(connection);
            if (CollectionUtils.isEmpty(userCourseDataMoveBeanList)) {
                logger.debug("User course data list is empty");
                return;
            }
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(
                    "INSERT INTO t_dm_user_course_data_move(data_move_id,corp_code,corp_name,status) VALUES (?,?,?,?)");
            for (int i = 0; i < userCourseDataMoveBeanList.size(); i++) {
                UserCourseDataMoveBean userCourseDataMoveBean = userCourseDataMoveBeanList.get(i);
                ps.setString(1, userCourseDataMoveBean.getDataMoveId());
                ps.setString(2, userCourseDataMoveBean.getCorpCode());
                ps.setString(3, userCourseDataMoveBean.getCorpName());
                ps.setString(4, userCourseDataMoveBean.getStatus());
                ps.addBatch();
                if (i != 0 && i % 1000 == 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
            connection.commit();
        } catch (Exception e) {
            logger.error("Error move corp data from oms!", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error("Error move corp data from oms!", e);
            }
        }
    }

    @Override
    public Page<UserCourseDataMoveBean> findPage(Page<UserCourseDataMoveBean> page, String corpCode, String status) throws Exception {
        Connection connection = null;
        try {
            connection = omsDataSource.getConnection();
            List<UserCourseDataMoveBean> userCourseDataMoveBeanList = getCorpListByParam(connection, page, corpCode, status);
            if (CollectionUtils.isEmpty(userCourseDataMoveBeanList)) {
                return page;
            }
            page.setData(userCourseDataMoveBeanList);
            long totalCount = getCorpCount(connection, corpCode, status);
            page.setTotal(totalCount);
        } catch (Exception e) {
            logger.error("Error find page corp data!", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error("Error move corp data from oms!", e);
                throw new RuntimeException(e);
            }
        }
        return page;
    }

    @Override
    public JsonStatus userCourseDataMove(String corpCode) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            if (StringUtils.isEmpty(corpCode)) {
                return new JsonStatus(JsonStatus.ERROR, false, "CorpCode is empty");
            }
            String rfsMdl = jedisMdm.get("rfs;;" + corpCode);
            String elsMdl = jedisMdm.get("els;;" + corpCode);
            DataSource rfsDataSource = getDataSource(rfsMdl);
            DataSource elsDataSource = getDataSource(elsMdl);
            List<UserCourseDailyStudy> userCourseDailyStudyList = getUserCourseByCorp(rfsDataSource, corpCode);
            if (CollectionUtils.isEmpty(userCourseDailyStudyList)) {
                return new JsonStatus(JsonStatus.ERROR, false, "Company corresponding data is empty");
            }
            addUserCourse(userCourseDailyStudyList, elsDataSource, corpCode);
            connection = omsDataSource.getConnection();
            ps = connection.prepareStatement("UPDATE t_dm_user_course_data_move SET status=? WHERE corp_code=?");
            ps.setString(1, DMConstant.StatusConstant.DATA_MOVE_SUCCESS);
            ps.setString(2, corpCode);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Error data move exception!", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error("Error data move exception!", e);
                throw new RuntimeException(e);
            }
        }
        return new JsonStatus(JsonStatus.OK, true, "SUCCESS");
    }


    private void addUserCourse(List<UserCourseDailyStudy> userCourseDailyStudyList,
                               DataSource elsDataSource, String corpCode) throws SQLException {
        Connection connection = elsDataSource.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("DELETE FROM t_els_user_course_daily_study WHERE corp_code=?");
        ps.setString(1, corpCode);
        ps.executeUpdate();
        ps.close();
        PreparedStatement newPs = connection.prepareStatement("INSERT INTO t_els_user_course_daily_study(record_id," +
                "record_date,corp_code,user_id,course_id,study_count,create_time,create_by,last_modify_time,last_modify_by,opt_time)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        for (int i = 0; i < userCourseDailyStudyList.size(); i++) {
            UserCourseDailyStudy userCourseDailyStudy = userCourseDailyStudyList.get(i);
            newPs.setString(1, userCourseDailyStudy.getRecordId());
            newPs.setDate(2, userCourseDailyStudy.getRecordDate());
            newPs.setString(3, corpCode);
            newPs.setString(4, userCourseDailyStudy.getUserId());
            newPs.setString(5, userCourseDailyStudy.getCourseId());
            newPs.setLong(6, userCourseDailyStudy.getStudyCount());
            newPs.setDate(7, userCourseDailyStudy.getCreateTime());
            newPs.setString(8, userCourseDailyStudy.getUserId());
            newPs.setDate(9, userCourseDailyStudy.getCreateTime());
            newPs.setString(10, userCourseDailyStudy.getUserId());
            newPs.setLong(11, userCourseDailyStudy.getOptTime());
            newPs.addBatch();
            if (i != 0 && i % 1000 == 0) {
                newPs.executeBatch();
                connection.commit();
                newPs.clearBatch();
            }
        }
        newPs.executeBatch();
        connection.commit();
        newPs.close();
        connection.close();
    }

    private List<UserCourseDailyStudy> getUserCourseByCorp(DataSource rfsDataSource, String corpCode) throws SQLException {
        Connection connection = rfsDataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT snapshot_date,create_time,corp_code,user_id,course_id,study_count FROM t_rfs_course_study_snap WHERE corp_code=?");
        ps.setString(1, corpCode);
        ResultSet rs = ps.executeQuery();
        List<UserCourseDailyStudy> userCourseDailyStudyList = new ArrayList<UserCourseDailyStudy>();
        while (rs.next()) {
            String userId = rs.getString("user_id");
            String courseId = rs.getString("course_id");
            long studyCount = rs.getLong("study_count");
            if (StringUtils.isNotEmpty(userId) &&
                    StringUtils.isNotEmpty(courseId)) {
                UserCourseDailyStudy userCourseDailyStudy = new UserCourseDailyStudy();
                userCourseDailyStudy.setStudyCount(studyCount);
                userCourseDailyStudy.setCourseId(courseId);
                userCourseDailyStudy.setUserId(userId);
                userCourseDailyStudy.setCorpCode(corpCode);
                userCourseDailyStudy.setCreateBy(userId);
                userCourseDailyStudy.setCreateTime(rs.getDate("create_time"));
                userCourseDailyStudy.setRecordDate(rs.getDate("snapshot_date"));
                userCourseDailyStudy.setRecordId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
                userCourseDailyStudy.setLastModifyBy(userId);
                userCourseDailyStudy.setLastModifyTime(rs.getDate("create_time"));
                userCourseDailyStudy.setOptTime(23927607l);
                userCourseDailyStudyList.add(userCourseDailyStudy);
            }
        }
        rs.close();
        ps.close();
        connection.close();
        return userCourseDailyStudyList;
    }

    private DataSource getDataSource(String mdlString) throws PropertyVetoException {
        Map<String, String> connectionMap = jsonToMap(mdlString);
        ComboPooledDataSource mdmDataSource = new ComboPooledDataSource();
        mdmDataSource.setDriverClass(connectionMap.get("driverClass"));
        mdmDataSource.setUser(connectionMap.get("userName"));
        mdmDataSource.setJdbcUrl(connectionMap.get("jdbcUrl"));
        mdmDataSource.setPassword(connectionMap.get("password"));

        return mdmDataSource;
    }

    private Map<String, String> jsonToMap(String json) {
        json = json.substring(1, json.length() - 1);
        String[] splitArray = json.split(",");
        Map<String, String> map = new HashMap<String, String>(splitArray.length);
        for (String split : splitArray) {
            int splitLine = split.indexOf("\":\"");
            String key = split.substring(1, splitLine);
            String value = split.substring(splitLine + 3, split.length() - 1);
            map.put(key, value);
        }

        return map;
    }

    private long getCorpCount(Connection connection, String corpCode, String status) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT count(1) as totalCount FROM t_dm_user_course_data_move WHERE 1=1");

        if (StringUtils.isNotEmpty(corpCode)) {
            buffer.append(" AND (corp_code='");
            buffer.append(corpCode);
            buffer.append("' or corp_name like '%");
            buffer.append(corpCode);
            buffer.append("%')");
        }
        if (StringUtils.isNotEmpty(status) && !"all".equals(status)) {
            buffer.append(" AND status='");
            buffer.append(status);
            buffer.append("'");
        }
        PreparedStatement ps = connection.prepareStatement(buffer.toString());
        ResultSet rs = ps.executeQuery();
        long count = 0l;
        while (rs.next()) {
            count = rs.getLong("totalCount");
        }
        rs.close();
        ps.close();
        return count;
    }

    private List<UserCourseDataMoveBean> getCorpListByParam(Connection connection, Page<UserCourseDataMoveBean> page,
                                                            String corpCode, String status) throws SQLException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT data_move_id,corp_code,corp_name,status FROM t_dm_user_course_data_move WHERE 1=1");

        if (StringUtils.isNotEmpty(corpCode)) {
            buffer.append(" AND (corp_code='");
            buffer.append(corpCode);
            buffer.append("' or corp_name like '%");
            buffer.append(corpCode);
            buffer.append("%')");
        }
        if (StringUtils.isNotEmpty(status) && !"all".equals(status)) {
            buffer.append(" AND status='");
            buffer.append(status);
            buffer.append("'");
        }
        buffer.append(" OFFSET ");
        buffer.append((page.getIndex() - 1) * page.getSize());
        buffer.append(" LIMIT ");
        buffer.append(page.getSize());
        PreparedStatement ps = connection.prepareStatement(buffer.toString());
        ResultSet rs = ps.executeQuery();
        List<UserCourseDataMoveBean> userCourseDataList = new ArrayList<UserCourseDataMoveBean>();
        while (rs.next()) {
            UserCourseDataMoveBean userCourseDataMoveBean = new UserCourseDataMoveBean();
            userCourseDataMoveBean.setCorpCode(rs.getString("corp_code"));
            userCourseDataMoveBean.setCorpName(rs.getString("corp_name"));
            userCourseDataMoveBean.setStatus(rs.getString("status"));
            userCourseDataMoveBean.setDataMoveId(rs.getString("data_move_id"));
            userCourseDataList.add(userCourseDataMoveBean);
        }

        rs.close();
        ps.close();

        return userCourseDataList;
    }


    private List<UserCourseDataMoveBean> getCorpList(Connection connection) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT corp_code,corp_name FROM t_oms_corp where corp_status=?");
        ps.setString(1, "ENABLED");
        ResultSet rs = ps.executeQuery();
        List<UserCourseDataMoveBean> userCourseDataList = new ArrayList<UserCourseDataMoveBean>();
        while (rs.next()) {
            String corp_code = rs.getString("corp_code");
            String corp_name = rs.getString("corp_name");
            if (StringUtils.isNotEmpty(corp_code) && StringUtils.isNotEmpty(corp_name)) {
                boolean exitCorp = findExitCorp(connection, corp_code);
                if (exitCorp) {
                    continue;
                }
                UserCourseDataMoveBean userCourseDataMoveBean = new UserCourseDataMoveBean();
                userCourseDataMoveBean.setCorpCode(corp_code);
                userCourseDataMoveBean.setCorpName(corp_name);
                userCourseDataMoveBean.setStatus(DMConstant.StatusConstant.DATA_MOVE_NOT_EXECUTING);
                userCourseDataMoveBean.setDataMoveId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
                userCourseDataList.add(userCourseDataMoveBean);
            }
        }
        rs.close();
        ps.close();

        return userCourseDataList;
    }

    private boolean findExitCorp(Connection connection, String corpCode) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "SELECT data_move_id FROM t_dm_user_course_data_move WHERE corp_code=?");
        ps.setString(1, corpCode);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return true;
        }
        rs.close();
        ps.close();
        return false;
    }
}
