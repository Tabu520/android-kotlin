// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RestQuerySelect implements Serializable {
    private final Map map;

    public RestQuerySelect() {
        this.map = new HashMap();
    }

    public String select(final String... selectClause) {
        if (selectClause != null) {
            StringBuffer strb = new StringBuffer();
            String[] var3 = selectClause;
            int var4 = selectClause.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                if (s.startsWith("$")) {
                    strb.append(s.substring(1) + ",");
                } else if (s.indexOf(46) > 0) {
                    String[] tokens = s.split("\\.");
                    this.handleTokens(tokens, 0, this.map);
                } else {
                    strb.append(s + ",");
                }
            }

            this.mapToString(strb, this.map);
            if (strb.toString().endsWith(",")) {
                strb.deleteCharAt(strb.toString().length() - 1);
            }

            return strb.toString();
        } else {
            return null;
        }
    }

    private void mapToString(final StringBuffer strb, final Map map) {
        Set<Map.Entry> set = map.entrySet();
        Iterator var4 = set.iterator();

        while(true) {
            while(var4.hasNext()) {
                Map.Entry entry = (Map.Entry)var4.next();
                String key = (String)entry.getKey();
                Map value = (Map)entry.getValue();
                if (value != null && value.size() != 0) {
                    strb.append(key + "{");
                    this.mapToString(strb, value);
                    if (strb.toString().endsWith(",")) {
                        strb.deleteCharAt(strb.toString().length() - 1);
                    }

                    strb.append("},");
                } else {
                    strb.append(key + ",");
                }
            }

            return;
        }
    }

    private void handleTokens(final String[] tokens, final int index, final Map selectMap) {
        if (tokens.length >= index + 1) {
            String key = tokens[index];
            Map map2 = (Map)selectMap.get(key);
            if (map2 == null) {
                map2 = new HashMap();
                selectMap.put(key, map2);
            }

            this.handleTokens(tokens, index + 1, (Map)map2);
        }
    }
}
