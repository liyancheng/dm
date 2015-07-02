package com.tbc.framework.dm.web.UserCourseData;

import com.tbc.framework.dm.domain.UserCourseDataMoveBean;
import com.tbc.framework.dm.util.Page;
import com.tbc.framework.dm.web.BaseModel;
import org.apache.commons.lang.StringUtils;

/**
 * @Package: com.tbc.framework.dm.web.CourseStudySnap
 * @ClassName: UserCourseDataModel
 * @Description: 用户课程迁移model
 * @Author: liyancheng
 * @CreateDate: 7/1/2015 10:22 AM
 */
public class UserCourseDataModel extends BaseModel<UserCourseDataMoveBean> {

    private Page<UserCourseDataMoveBean> page;

    private String status = "all";

    public Page<UserCourseDataMoveBean> getPage() {
        if (page == null) {
            page = new Page<UserCourseDataMoveBean>();
        }
        return page;
    }

    public void setPage(Page<UserCourseDataMoveBean> page) {
        this.page = page;
    }

    private String corpCode;

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
