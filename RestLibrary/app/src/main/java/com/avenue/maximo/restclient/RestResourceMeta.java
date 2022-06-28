// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class RestResourceMeta implements Serializable {
    private String tableName;
    private String name;
    private String uniqueColumn;
    private List<RestAttributeMeta> metaAttributes;

    public RestResourceMeta() {
    }

    public RestResourceMeta(final String name, final String tableName) {
        this.name = name;
        this.tableName = tableName;
    }

    protected RestResourceMeta setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    protected RestResourceMeta setTableName(final String name) {
        this.tableName = name;
        return this;
    }

    public String getTableName() {
        return this.tableName;
    }

    protected RestResourceMeta setUniqueColumn(final String uniqueColumn) {
        this.uniqueColumn = uniqueColumn;
        return this;
    }

    public String getUniqueColumn() {
        if (this.uniqueColumn == null) {
            this.lookupUniqueColumn();
        }
        return this.uniqueColumn;
    }

    private void lookupUniqueColumn() {
        for (int i = 0; i < this.getListAttributeMeta().size(); ++i) {
            final RestAttributeMeta metaAttr = this.getListAttributeMeta().get(i);
            if (metaAttr.isUniqueId()) {
                this.setUniqueColumn(metaAttr.getName());
                break;
            }
        }
    }

    protected RestResourceMeta setListAttributeMeta(final List<RestAttributeMeta> metaAttributes) {
        this.metaAttributes = metaAttributes;
        this.lookupUniqueColumn();
        return this;
    }

    public List<RestAttributeMeta> getListAttributeMeta() {
        if (this.metaAttributes == null) {
            this.metaAttributes = new ArrayList<>();
        }
        return this.metaAttributes;
    }

    public boolean hasAttributeMeta(final RestAttributeMeta attrMeta) {
        return this.getListAttributeMeta().contains(attrMeta);
    }

    public RestAttributeMeta getAttributeMeta(final String name) {
        for (int i = 0; i < this.metaAttributes.size(); ++i) {
            if (this.metaAttributes.get(i).getName().equalsIgnoreCase(name)) {
                return this.metaAttributes.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RestResourceMeta) {
            final RestResourceMeta compare = (RestResourceMeta) obj;
            if (compare.getName() != null && this.getName() != null && compare.getTableName() != null && this.getTableName() != null) {
                return compare.getName().equalsIgnoreCase(this.getName()) && compare.getTableName().equalsIgnoreCase(this.getTableName());
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getName() + " Set (Table: " + this.getTableName() + ", UColumn: " + this.getUniqueColumn() + ")";
    }
}
