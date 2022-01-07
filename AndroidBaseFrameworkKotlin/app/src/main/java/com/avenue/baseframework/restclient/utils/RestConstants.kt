package com.avenue.baseframework.restclient.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object RestConstants {

    //REQUEST METHOD
    val HTTP_POST = "POST"
    val HTTP_PUT = "PUT"
    val HTTP_GET = "GET"
    val HTTP_DELETE = "DELETE"

    //AUTHENTICATION
    val AUTH_NONE = "none"
    val AUTH_BASIC = "basic"
    val AUTH_MAXAUTH = "maxauth"
    val AUTH_TOKEN = "token"
    val AUTH_FORM = "form"

    //STRING
    val XML_FORMAT = "xml"
    val JSON_FORMAT = "json"
    val DEFAULT_ENCODING = "utf-8"
    val DEFAULT_DOUBLE_FORMAT = "%.2f"

    //DATE FORMAT
    val REST_DATE_RESPONSE_FORMAT: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)
    val REST_DATE_REQUEST_FORMAT: DateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.US)

    val OS_HANDLER_CONTEXT = "os"
    val MBO_HANDLER_CONTEXT = "mbo"
    val ROWSTAMP_META = "rowstamp"
    val HIDDEN_META = "hidden"
    val READONLY_META = "readonly"
    val REQUIRED_META = "required"
    val RESOURCE_ID_META = "resourceid"
    val ATTRIBUTES_META = "Attributes"
    val RELATED_MBOS_META = "RelatedMbos"
    val CONTENT_META = "content"
    val LOCALE_META = "localecontent"
    val RS_START = "rsStart"
    val RS_COUNT = "rsCount"
    val RS_TOTAL = "rsTotal"
    val MAXREST_MODULE_CONTEXT = "maxrest"
    val API_REST_CONTEXT = "rest"
}