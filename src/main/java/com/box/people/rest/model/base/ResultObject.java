package com.box.people.rest.model.base;

import java.util.List;

public class ResultObject<T> {
    private String appName;
    private Boolean success;
    private T result;
    private List<ErrorMessage> errorMessages;

    protected ResultObject() {
    }

    protected ResultObject(String appName, Boolean success) {
        this.appName = appName;
        this.success = success;
    }

    public ResultObject(String appName, Boolean success, T result) {
        this(appName, success);
        this.result = result;
    }

    public ResultObject(String appName, Boolean success, T result, List<ErrorMessage> errorMessages) {
        this(appName, success, result);
        this.errorMessages = errorMessages;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
    }
}