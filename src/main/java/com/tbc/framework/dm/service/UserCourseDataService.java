package com.tbc.framework.dm.service;

import com.tbc.framework.dm.domain.UserCourseDataMoveBean;
import com.tbc.framework.dm.util.JsonStatus;
import com.tbc.framework.dm.util.Page;

/**
 * @Package: com.tbc.framework.dm.service
 * @ClassName: UserCourseDataService
 * @Description: 用户每天学习迁移 业务处理层
 * @Author: liyancheng
 * @CreateDate: 7/1/2015 10:28 AM
 */
public interface UserCourseDataService {


    /**
     * 公司信息迁移数据到本地库中
     */
    void moveCorpDataFromOMS();

    /**
     * 分页显示公司信息
     *
     * @param page     分页条件
     * @param corpCode 公司编码 或者公司名称
     * @param status   状态
     * @return
     */
    Page<UserCourseDataMoveBean> findPage(Page<UserCourseDataMoveBean> page, String corpCode, String status)
            throws Exception;

    /**
     * 数据迁移主要方法
     *
     * @param corpCode 公司编码
     * @throws Exception
     */
    JsonStatus userCourseDataMove(String corpCode) throws Exception;


}
