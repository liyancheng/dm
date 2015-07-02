package com.tbc.framework.dm.domain;

import java.io.Serializable;
import java.sql.Date;

/**
 * 用户课程每日学习情况统计表
 * @author CHEN Yunfei@HF
 * @since 2015/6/30
 */
public class UserCourseDailyStudy implements Serializable {

    private static final long serialVersionUID = 1L;

    //记录主键
    private String recordId;
    //记录日期
    private Date recordDate;
    //公司号
    private String corpCode;
    //用户id
    private String userId;
    //课程id
    private String courseId;
    //当天内的学习次数
    private Long studyCount;
    //通用字段：创建时间
    private Date createTime;
    //通用字段：创建人id
    private String createBy;
    //通用字段：最后修改时间
    private Date lastModifyTime;
    //通用字段：最后修改人id
    private String lastModifyBy;
    //通用字段：修改时间
    private Long optTime;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Long getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Long studyCount) {
        this.studyCount = studyCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Long getOptTime() {
        return optTime;
    }

    public void setOptTime(Long optTime) {
        this.optTime = optTime;
    }
}
