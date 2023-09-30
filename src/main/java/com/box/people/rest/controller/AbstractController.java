package com.box.people.rest.controller;

import com.box.people.rest.model.base.ResultObject;

public abstract class AbstractController {
    protected String appName;

    public AbstractController(final String appName) {
        this.appName = appName;
    }

    public <C> ResultObject<C> mapToResultObject(C channelObject) {
        return new ResultObject<>(appName, true, channelObject);
    }

}