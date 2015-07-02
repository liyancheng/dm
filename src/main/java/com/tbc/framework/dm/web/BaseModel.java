package com.tbc.framework.dm.web;

import com.tbc.framework.dm.util.Page;

/**
 * Created with IntelliJ IDEA.
 * User: Ztian
 * Date: 13-4-4
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
public class BaseModel<T> {
    protected String actionNamespace;
    protected String type;
    protected String status;
    protected String search;

    protected T entity;
    protected Page<T> entities;

    public T getEntity() {
        if (entity == null) {
            Class<? extends BaseModel> subClass = this.getClass();
            entity = ReflectUtil.<T>getGenericParamInstance(subClass);
        }
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Page<T> getEntities() {
        if (entities == null) {
            entities = new Page<T>();
        }

        return entities;
    }

    public void setEntities(Page<T> entities) {
        this.entities = entities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getActionNamespace() {
        return actionNamespace;
    }

    public void setActionNamespace(String actionNamespace) {
        this.actionNamespace = actionNamespace;
    }
}
