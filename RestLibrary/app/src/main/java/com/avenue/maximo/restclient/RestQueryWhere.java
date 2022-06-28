// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;

public class RestQueryWhere implements Serializable {
    private final Map<String, Object> map;
    private String currentKey;

    public RestQueryWhere() {
        this.map = new LinkedHashMap<>();
    }

    public RestQueryWhere where(final String name) {
        this.currentKey = name.toUpperCase();
        return this;
    }

    public RestQueryWhere and(final String name) {
        if (name.indexOf(46) > 0) {
            String[] attrPath = name.split("\\.");
            Map<String, String> childMap = (Map)this.map.get(attrPath[0]);
            if (childMap == null) {
                childMap = new LinkedHashMap();
                this.map.put(attrPath[0], childMap);
            }
        }

        return this.where(name);
    }

    private Map getCurrentMap() {
        if (this.currentKey.indexOf(46) > 0) {
            final String[] attrPath = this.currentKey.split("\\.");
            return (Map) this.map.get(attrPath[0]);
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
        } catch (DatatypeConfigurationException | UnsupportedEncodingException ex1) {
            throw new RestException(ex1.getMessage(), ex1);
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
            StringBuffer strbWhere = new StringBuffer();
            Set<Map.Entry<String, Object>> set = this.map.entrySet();
            int cnt = 0;
            Iterator var4 = set.iterator();

            while(var4.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var4.next();
                ++cnt;
                String key = entry.getKey();
                if (key.startsWith("/")) {
                    key = key.substring(1);
                }

                Object value = entry.getValue();
                strbWhere.append(key);
                strbWhere.append("=");
                if (value instanceof String) {
                    strbWhere.append(URLEncoder.encode((String)value, "utf-8"));
                } else {
                    Map<String, String> childMap = (Map)value;
                    strbWhere.append("{");
                    Set<Map.Entry<String, String>> cset = childMap.entrySet();
                    int ccnt = 0;
                    Iterator var11 = cset.iterator();

                    while(var11.hasNext()) {
                        Map.Entry<String, String> centry = (Map.Entry)var11.next();
                        ++ccnt;
                        String cKey = centry.getKey();
                        if (cKey.startsWith("/")) {
                            cKey = cKey.substring(1);
                        }

                        String cValue = centry.getValue();
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

            String where = strbWhere.toString();
            return where;
        } catch (Exception var15) {
            throw new RestException("Errors while constructing where clause from RestQueryWhere object");
        }
    }
}
