// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.Iterator;
import java.net.URLEncoder;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class RestParams implements Serializable, Cloneable
{
    public static final String FORMAT = "_format";
    public static final String COMPACT = "_compact";
    public static final String USE_ROWSTAMP = "_urs";
    public static final String RESOURCE_START = "_rsStart";
    public static final String INCLUDE_COLS = "_includecols";
    public static final String EXCLUDE_COLS = "_excludecols";
    public static final String EXACT_MATCH = "_exactmatch";
    public static final String SQL_WHERE = "_uw";
    public static final String MAX_ITEMS = "_maxItems";
    public static final String ORDERBY_ASC = "_orderbyasc";
    public static final String ORDERBY_DESC = "_orderbydesc";
    public static final String OPERATION_MODE_OR = "_opmodeor";
    public static final String METADATA = "_md";
    public static final String GENERAL_LEDGER = "_glc";
    public static final String TABLE_DOMAIN = "_fd";
    public static final String ORGANIZATION_ID = "_fdorg";
    public static final String SITE_ID = "_fdsite";
    public static final String DROP_NULLS = "_dropnulls";
    public static final String KEY_COLUMNS_ONLY = "_keys";
    public static final String LANG_CODE = "_lang";
    public static final String LOCALE = "_locale";
    public static final String PAGE_NUMBER = "_page";
    public static final String ROW_STAMP = "_rowstamp";
    public static final String ROOT_ONLY = "_rootonly";
    public static final String TIME_ZONE = "_tz";
    public static final String RETURN_NEW_RESOURCE_URL = "_ulcr";
    public static final String VERBOSE = "_verbose";
    public static final String VALIDATE_XML = "_vt";
    public static final String QUERY_METHOD = "_qop";
    public static final String TOTAL_COUNT = "_tc";
    public static final String REST_QUERY_WHERE = "#restquerywhere";
    private HashMap<String, Object> parameters;
    
    public RestParams() {
        this.parameters = new HashMap<String, Object>();
    }
    
    public RestParams put(final String key, Object value) {
        if (value instanceof Boolean) {
            if (value) {
                value = 1;
            }
            else {
                value = 0;
            }
        }
        this.parameters.put(key, value);
        return this;
    }
    
    public Object get(final String key) {
        return this.parameters.get(key);
    }
    
    public Boolean getBoolean(final String key) {
        final Object value = this.get(key);
        if (value != null && value instanceof Integer && (int)value == 1) {
            return true;
        }
        return false;
    }
    
    public RestParams remove(final String key) {
        this.parameters.remove(key);
        return this;
    }
    
    public RestParams clear() {
        this.parameters.clear();
        return this;
    }
    
    public boolean containsKey(final String key) {
        return this.parameters.containsKey(key);
    }
    
    public boolean containsValue(final Object value) {
        return this.parameters.containsValue(value);
    }
    
    public RestParams clone() {
        final RestParams result = new RestParams();
        result.parameters = (HashMap<String, Object>)this.parameters.clone();
        return result;
    }
    
    public String construct() throws RestException {
        try {
            final StringBuilder retStr = new StringBuilder("?");
            for (final Map.Entry<String, Object> m : this.parameters.entrySet()) {
                final String key = m.getKey();
                if (!key.equals("#restquerywhere")) {
                    retStr.append(m.getKey()).append("=");
                    retStr.append(URLEncoder.encode(String.valueOf(m.getValue()), "utf-8"));
                }
                else {
                    retStr.append(String.valueOf(m.getValue()));
                }
                retStr.append("&");
            }
            return retStr.substring(0, retStr.length() - 1);
        }
        catch (Exception e) {
            throw new RestException("Errors while constructing REST parameters");
        }
    }
    
    public boolean hasFormatSet() {
        return this.get("_format") != null;
    }
    
    public boolean isJSONFormat() {
        return this.hasFormatSet() && this.get("_format").equals("json");
    }
    
    public boolean isXMLFormat() {
        return this.hasFormatSet() && this.get("_format").equals("xml");
    }
}
