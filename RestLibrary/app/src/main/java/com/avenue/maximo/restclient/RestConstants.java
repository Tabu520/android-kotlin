// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;

public interface RestConstants {
    DateFormat REST_DATE_RESPONSE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
    DateFormat REST_DATE_REQUEST_FORMAT = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.US);
    String DEFAULT_DOUBLE_FORMAT = "%.2f";
    String HTTP_METHOD_POST = "POST";
    String HTTP_METHOD_PUT = "PUT";
    String HTTP_METHOD_GET = "GET";
    String HTTP_METHOD_DELETE = "DELETE";
    String AUTH_BASIC = "basic";
    String AUTH_MAXAUTH = "maxauth";
    String AUTH_FORM = "form";
    String MAXREST_MODULE_CONTEXT = "maxrest";
    String API_REST_CONTEXT = "rest";
    String JSON_FORMAT = "json";
    String XML_FORMAT = "xml";
    String OS_HANDLER_CONTEXT = "os";
    String MBO_HANDLER_CONTEXT = "mbo";
    String ROWSTAMP_META = "rowstamp";
    String HIDDEN_META = "hidden";
    String READONLY_META = "readonly";
    String REQUIRED_META = "required";
    String RESOURCE_ID_META = "resourceid";
    String ATTRIBUTES_META = "Attributes";
    String RELATED_MBOS_META = "RelatedMbos";
    String CONTENT_META = "content";
    String LOCALE_META = "localecontent";
    String RS_START = "rsStart";
    String RS_COUNT = "rsCount";
    String RS_TOTAL = "rsTotal";
    String DEFAULT_ENCODING = "utf-8";
}
