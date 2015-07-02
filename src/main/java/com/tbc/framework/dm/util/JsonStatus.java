package com.tbc.framework.dm.util;

public class JsonStatus {

    public static final String OK = "200";
    public static final String ERROR = "500";

    private String status;
    private String message;
    private boolean success;
    private Object jsonObj;

    public JsonStatus() {

    }

    public JsonStatus(String status, boolean success) {
        super();
        this.status = status;
        this.success = success;
    }

    public JsonStatus(String status, boolean success, String message) {
        super();
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public JsonStatus(String status, boolean success, Object jsonObj) {
        super();
        this.status = status;
        this.success = success;
        this.jsonObj = jsonObj;
    }

    public JsonStatus(String status, boolean success, String message, Object jsonObj) {
        super();
        this.status = status;
        this.success = success;
        this.message = message;
        this.jsonObj = jsonObj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(Object jsonObj) {
        this.jsonObj = jsonObj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}