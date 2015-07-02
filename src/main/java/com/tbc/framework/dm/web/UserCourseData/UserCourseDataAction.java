
package com.tbc.framework.dm.web.UserCourseData;

import com.opensymphony.xwork2.ActionContext;
import com.tbc.framework.dm.domain.UserCourseDataMoveBean;
import com.tbc.framework.dm.service.UserCourseDataService;
import com.tbc.framework.dm.util.JsonStatus;
import com.tbc.framework.dm.util.Page;
import com.tbc.framework.dm.web.BaseAction;
import com.tbc.framework.dm.web.BaseModel;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * @Package: com.tbc.framework.dm.web.CourseStudySnap
 * @ClassName: UserCourseDataAction
 * @Description: 用户课程迁移控制层
 * @Author: liyancheng
 * @CreateDate: 7/1/2015 10:23 AM
 */
public class UserCourseDataAction extends BaseAction<UserCourseDataModel> {

    /**
     * 日志记录器
     */
    private Logger logger = Logger.getLogger(UserCourseDataAction.class);

    @Resource
    private UserCourseDataService userCourseDataService;

    /**
     * 分页显示公司信息
     *
     * @return
     */
    public String listCorp() {
        try {
            UserCourseDataModel userCourseDataModel = getModel();
            Page<UserCourseDataMoveBean> page = userCourseDataService.findPage(userCourseDataModel.getPage(),
                    userCourseDataModel.getCorpCode(), userCourseDataModel.getStatus());
            userCourseDataModel.setPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "listCorp";
    }

    /**
     * 数据迁移
     *
     * @return
     */
    public void userCourseDataMove() {
        Writer writer = null;
        try {
            JsonStatus jsonStatus = userCourseDataService.userCourseDataMove(getModel().getCorpCode());
            if (jsonStatus.isSuccess()) {
                success(jsonStatus.getMessage());
            } else {
                failed(jsonStatus.getMessage());
            }
            HttpServletResponse response = ServletActionContext.getResponse();
            writer = response.getWriter();
            writer.write(getJson());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
