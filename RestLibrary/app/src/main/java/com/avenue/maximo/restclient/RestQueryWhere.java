// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.Iterator;
import java.util.Set;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.Serializable;

public class RestQueryWhere implements Serializable
{
    private Map<String, Object> map;
    private String currentKey;
    
    public RestQueryWhere() {
        this.map = new LinkedHashMap<String, Object>();
    }
    
    public RestQueryWhere where(final String name) {
        this.currentKey = name.toUpperCase();
        return this;
    }
    
    public RestQueryWhere and(final String name) {
        if (name.indexOf(46) > 0) {
            final String[] attrPath = name.split("\\.");
            Map<String, String> childMap = this.map.get(attrPath[0]);
            if (childMap == null) {
                childMap = new LinkedHashMap<String, String>();
                this.map.put(attrPath[0], childMap);
            }
        }
        return this.where(name);
    }
    
    private Map getCurrentMap() {
        if (this.currentKey.indexOf(46) > 0) {
            final String[] attrPath = this.currentKey.split("\\.");
            return this.map.get(attrPath[0]);
        }
        return this.map;
    }
    
    private String getCurrentKey() {
        if (this.currentKey.indexOf(46) > 0) {
            final String[] attrPath = this.currentKey.split("\\.");
            return attrPath[1];
        }
        return this.currentKey;
    }
    
    private void setQueryToken(final String s) {
        final Map currMap = this.getCurrentMap();
        String currKey = this.getCurrentKey();
        if (currMap.containsKey(currKey)) {
            currKey = "/" + currKey;
        }
        currMap.put(currKey, s);
    }
    
    private void setQueryTokenValue(final String token, final Object value) throws RestException {
        try {
            this.setQueryToken(RestUtil.stringValue(token + value));
        }
        catch (DatatypeConfigurationException ex1) {
            throw new RestException(ex1.getMessage(), ex1);
        }
        catch (UnsupportedEncodingException ex2) {
            throw new RestException(ex2.getMessage(), ex2);
        }
    }
    
    public RestQueryWhere equalTo(final Object value) throws RestException {
        this.setQueryTokenValue("~eq~", value);
        return this;
    }
    
    public RestQueryWhere notEqualTo(final Object value) throws RestException {
        this.setQueryTokenValue("~neq~", value);
        return this;
    }
    
    public RestQueryWhere startsWith(final String value) throws RestException {
        this.setQueryTokenValue("~sw~", value);
        return this;
    }
    
    public RestQueryWhere endsWith(final String value) throws RestException {
        this.setQueryTokenValue("~ew~", value);
        return this;
    }
    
    public RestQueryWhere like(final String value) throws RestException {
        this.setQueryTokenValue("", value);
        return this;
    }
    
    public RestQueryWhere greaterThan(final Object value) throws RestException {
        this.setQueryTokenValue("~gt~", value);
        return this;
    }
    
    public RestQueryWhere greaterThanEqual(final Object value) throws RestException {
        this.setQueryTokenValue("~gteq~", value);
        return this;
    }
    
    public RestQueryWhere lessThan(final Object value) throws RestException {
        this.setQueryTokenValue("~lt~", value);
        return this;
    }
    
    public RestQueryWhere lessThanEqual(final Object value) throws RestException {
        this.setQueryTokenValue("~lteq~", value);
        return this;
    }
    
    public String whereClause() throws RestException {
        try {
            final StringBuffer strbWhere = new StringBuffer();
            final Set<Map.Entry<String, Object>> set = this.map.entrySet();
            int cnt = 0;
            for (final Map.Entry<String, Object> entry : set) {
                ++cnt;
                String key = entry.getKey();
                if (key.startsWith("/")) {
                    key = key.substring(1);
                }
                final Object value = entry.getValue();
                strbWhere.append(key);
                strbWhere.append("=");
                if (value instanceof String) {
                    strbWhere.append(URLEncoder.encode((String)value, "utf-8"));
                }
                else {
                    final Map<String, String> childMap = (Map<String, String>)value;
                    strbWhere.append("{");
                    final Set<Map.Entry<String, String>> cset = childMap.entrySet();
                    int ccnt = 0;
                    for (final Map.Entry<String, String> centry : cset) {
                        ++ccnt;
                        String cKey = centry.getKey();
                        if (cKey.startsWith("/")) {
                            cKey = cKey.substring(1);
                        }
                        final String cValue = centry.getValue();
                        strbWhere.append(cKey);
                        strbWhere.append("=");
                        strbWhere.append(URLEncoder.encode(cValue, "utf-8"));
                        if (cset.size() > ccnt) {
                            strbWhere.append("&");
                        }
                    }
                    strbWhere.append("}");
                }
                if (set.size() > cnt) {
                    strbWhere.append("&");
                }
            }
            final String where = strbWhere.toString();
            return where;
        }
        catch (Exception e) {
            throw new RestException("Errors while constructing where clause from RestQueryWhere object");
        }
    }
}
