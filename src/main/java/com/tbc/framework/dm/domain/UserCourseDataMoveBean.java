package com.tbc.framework.dm.domain;

import java.io.Serializable;

/**
 * @Package: com.tbc.framework.dm.domain
 * @ClassName: UserCourseDataMoveBean
 * @Description: 用户学习课程 实体
 * @Author: ycli
 * @CreateDate: 7/1/2015 10:00 AM
 */
public class UserCourseDataMoveBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String dataMoveId;

    /**
     * 公司编码
     */
    private String corpCode;

    /**
     * 公司名称
     */
    private String corpName;

    /**
     * 状态
     */
    private String status;

    public String getDataMoveId() {
        return dataMoveId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public String getStatus() {
        return status;
    }

    public void setDataMoveId(String dataMoveId) {
        this.dataMoveId = dataMoveId;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
