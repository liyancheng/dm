package com.tbc.framework.dm.web;

import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Ztian
 * Date: 13-3-9
 * Time: 下午9:02
 * To change this template use File | Settings | File Templates.
 */
public class BaseAction<T> implements ModelDriven<T> {
    public static final String EMPTY_JSON = "{}";
    public static final String SUCCESS_JSON = "{\"result\":\"success\"}";
    public static final String FAILED_JSON = "{\"result\":\"failed\"}";

    public static final Log LOG = LogFactory.getLog(BaseAction.class);

    private T model;
    private String json;

    @Override
    public T getModel() {
        if (model == null) {
            Class<? extends BaseAction> aClass = this.getClass();
            model = ReflectUtil.<T>getGenericParamInstance(aClass);
        }
        return model;
    }

    /**
     * <code>JsonResult</code>需要调用的方法，任何返回Json的方法应该先设置jsonResult的值
     *
     * @return
     */
    public String getJson() {
        if (json == null) {
            json = EMPTY_JSON;
        }

        return json;
    }

    /**
     * 设置需要以Json形式返回的数据.
     *
     * @param json
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * 成功。
     */
    public void success(String... msg) {
        if (msg != null && msg.length > 0) {
            this.json = "{\"result\":\"success\",\"msg\":\"" + msg[0] + "\"}";
        } else {
            this.json = SUCCESS_JSON;
        }
    }

    /**
     * 失败
     */
    public void failed(String... msg) {
        if (msg != null && msg.length > 0) {
            this.json = "{\"result\":\"failed\",\"msg\":\"" + msg[0] + "\"}";
        } else {
            this.json = FAILED_JSON;
        }
    }
}
