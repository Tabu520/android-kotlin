// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class RestQuerySelect implements Serializable
{
    private Map map;
    
    public RestQuerySelect() {
        this.map = new HashMap();
    }
    
    public String select(final String... selectClause) {
        if (selectClause != null) {
            final StringBuffer strb = new StringBuffer();
            for (final String s : selectClause) {
                if (s.startsWith("$")) {
                    strb.append(s.substring(1) + ",");
                }
                else if (s.indexOf(46) > 0) {
                    final String[] tokens = s.split("\\.");
                    this.handleTokens(tokens, 0, this.map);
                }
                else {
                    strb.append(s + ",");
                }
            }
            this.mapToString(strb, this.map);
            if (strb.toString().endsWith(",")) {
                strb.deleteCharAt(strb.toString().length() - 1);
            }
            return strb.toString();
        }
        return null;
    }
    
    private void mapToString(final StringBuffer strb, final Map map) {
        final Set<Map.Entry> set = (Set<Map.Entry>)map.entrySet();
        for (final Map.Entry entry : set) {
            final String key = entry.getKey();
            final Map value = entry.getValue();
            if (value == null || value.size() == 0) {
                strb.append(key + ",");
            }
            else {
                strb.append(key + "{");
                this.mapToString(strb, value);
                if (strb.toString().endsWith(",")) {
                    strb.deleteCharAt(strb.toString().length() - 1);
                }
                strb.append("},");
            }
        }
    }
    
    private void handleTokens(final String[] tokens, final int index, final Map selectMap) {
        if (tokens.length < index + 1) {
            return;
        }
        final String key = tokens[index];
        Map map2 = selectMap.get(key);
        if (map2 == null) {
            map2 = new HashMap();
            selectMap.put(key, map2);
        }
        this.handleTokens(tokens, index + 1, map2);
    }
}
