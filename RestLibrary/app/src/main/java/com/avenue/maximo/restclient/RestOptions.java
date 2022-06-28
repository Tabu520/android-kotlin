// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.Serializable;

public class RestOptions implements RestConstants, Serializable, Cloneable {
    private String host;
    private int port;
    private String authMode;
    private String user;
    private String password;
    private boolean ssl;
    private String moduleContext;
    private String apiContext;
    private String handlerContext;

    public RestOptions() {
        this.port = 80;
        this.authMode = null;
        this.ssl = false;
        this.moduleContext = "maxrest";
        this.apiContext = "rest";
    }

    public RestOptions setHost(final String value) {
        this.host = value;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public RestOptions setModuleContext(final String value) {
        this.moduleContext = value;
        return this;
    }

    public String getModuleContext() {
        return this.moduleContext;
    }

    public RestOptions setApiContext(final String value) {
        this.apiContext = value;
        return this;
    }

    public String getApiContext() {
        return this.apiContext;
    }

    public RestOptions setHandlerContext(final String value) {
        this.handlerContext = value.toLowerCase();
        return this;
    }

    public String getHandlerContext() {
        return this.handlerContext;
    }

    public RestOptions setHttps() {
        this.ssl = true;
        return this;
    }

    public RestOptions setHttp() {
        this.ssl = false;
        return this;
    }

    public boolean isHttps() {
        return this.ssl;
    }

    public RestOptions setPort(final int value) {
        this.port = value;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public RestOptions setAuth(final String value) {
        this.authMode = value;
        return this;
    }

    public RestOptions setUser(final String value) {
        this.user = value;
        return this;
    }

    public String getUser() {
        return this.user;
    }

    public RestOptions setPassword(final String value) {
        this.password = value;
        return this;
    }

    protected String getPassword() {
        return this.password;
    }

    public String getHandlerURI() {
        return this.getRootApiURI() + "/" + this.handlerContext;
    }

    public String getRootApiURI() {
        return (this.ssl ? "https://" : "http://") + this.host + ":" + this.port + "/" + this.moduleContext + "/" + this.apiContext;
    }

    public boolean isBasicAuth() {
        return this.authMode.equals("basic");
    }

    public boolean isFormAuth() {
        return this.authMode.equals("form");
    }

    public boolean isMaxAuth() {
        return this.authMode.equals("maxauth");
    }
}
