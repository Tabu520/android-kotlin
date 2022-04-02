// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStream;

public class RestException extends Exception
{
    public static final int INFO = 1000;
    public static final int WARNING = 2000;
    public static final int ERROR = 3000;
    private int errorType;
    private int errorCode;
    private String maxId;
    
    public RestException(final int errorCode, final String message) {
        super(message);
        this.errorType = 3000;
        this.errorCode = 400;
        this.maxId = null;
        this.errorCode = errorCode;
    }
    
    public RestException(final int errorCode, final String message, final Throwable t) {
        super(message, t);
        this.errorType = 3000;
        this.errorCode = 400;
        this.maxId = null;
        this.errorCode = errorCode;
    }
    
    public RestException(final Throwable t) {
        super(t);
        this.errorType = 3000;
        this.errorCode = 400;
        this.maxId = null;
    }
    
    public RestException(final String message, final Throwable t) {
        super(message, t);
        this.errorType = 3000;
        this.errorCode = 400;
        this.maxId = null;
    }
    
    public RestException(final String message) {
        super(message);
        this.errorType = 3000;
        this.errorCode = 400;
        this.maxId = null;
    }
    
    public String getMaxId() {
        return this.maxId;
    }
    
    protected void setMaxId(final String id) {
        this.maxId = id;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    protected void setErrorType(final int value) {
        this.errorType = value;
    }
    
    public int getErrorType() {
        return this.errorType;
    }
    
    public static void throwTimeOutException() throws RestException {
        throw new RestException("Cannot connect to Maximo server");
    }
    
    public static void throwExceptionFromResponseStream(final int error, final InputStream is) throws RestException {
        String inputString;
        try {
            inputString = RestUtil.getStringFromInputStream(is);
        }
        catch (IOException e) {
            inputString = e.getMessage();
        }
        final String fullMsg = inputString.replaceAll(".*:", "").trim();
        final RestException exception = new RestException(error, fullMsg);
        final Pattern p = Pattern.compile("BMX.*-");
        final Matcher m = p.matcher(fullMsg);
        if (m.find()) {
            final String maxId = m.group(0).split("-")[0].trim();
            exception.setMaxId(maxId);
            final String errorType = maxId.substring(maxId.length() - 1, maxId.length());
            if ("I".equals(errorType)) {
                exception.setErrorType(1000);
            }
            else if ("W".equals(errorType)) {
                exception.setErrorType(2000);
            }
            else {
                exception.setErrorType(3000);
            }
        }
        throw exception;
    }
}
