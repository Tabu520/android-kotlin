// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;

public interface RestConstants
{
    public static final DateFormat REST_DATE_RESPONSE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
    public static final DateFormat REST_DATE_REQUEST_FORMAT = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.US);
    public static final String DEFAULT_DOUBLE_FORMAT = "%.2f";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String AUTH_BASIC = "basic";
    public static final String AUTH_MAXAUTH = "maxauth";
    public static final String AUTH_FORM = "form";
    public static final String MAXREST_MODULE_CONTEXT = "maxrest";
    public static final String API_REST_CONTEXT = "rest";
    public static final String JSON_FORMAT = "json";
    public static final String XML_FORMAT = "xml";
    public static final String OS_HANDLER_CONTEXT = "os";
    public static final String MBO_HANDLER_CONTEXT = "mbo";
    public static final String ROWSTAMP_META = "rowstamp";
    public static final String HIDDEN_META = "hidden";
    public static final String READONLY_META = "readonly";
    public static final String REQUIRED_META = "required";
    public static final String RESOURCE_ID_META = "resourceid";
    public static final String ATTRIBUTES_META = "Attributes";
    public static final String RELATED_MBOS_META = "RelatedMbos";
    public static final String CONTENT_META = "content";
    public static final String LOCALE_META = "localecontent";
    public static final String RS_START = "rsStart";
    public static final String RS_COUNT = "rsCount";
    public static final String RS_TOTAL = "rsTotal";
    public static final String DEFAULT_ENCODING = "utf-8";
}
