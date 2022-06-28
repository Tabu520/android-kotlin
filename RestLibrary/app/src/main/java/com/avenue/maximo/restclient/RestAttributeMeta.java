// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.Serializable;

public class RestAttributeMeta implements Serializable
{
    private String name;
    private Integer datatype;
    private boolean isRequired;
    private boolean isUniqueId;
    
    public RestAttributeMeta() {
        this.datatype = 0;
        this.isRequired = false;
        this.isUniqueId = false;
    }
    
    public RestAttributeMeta(final String name) {
        this.datatype = 0;
        this.isRequired = false;
        this.isUniqueId = false;
        this.setName(name);
    }
    
    public String getName() {
        return this.name;
    }
    
    protected RestAttributeMeta setName(final String name) {
        this.name = name;
        return this;
    }
    
    public Integer getDataType() {
        return this.datatype;
    }
    
    public RestAttributeMeta setDataType(final int value) {
        this.datatype = value;
        return this;
    }
    
    public boolean isRequired() {
        return this.isRequired;
    }
    
    public RestAttributeMeta setRequired(final boolean value) {
        this.isRequired = value;
        return this;
    }
    
    public boolean isUniqueId() {
        return this.isUniqueId;
    }
    
    public RestAttributeMeta setUniqueId(final boolean value) {
        this.isUniqueId = value;
        return this;
    }
    
    public String getDataTypeAsString() {
        switch (this.getDataType()) {
            case 0: {
                return "TEXT";
            }
            case 1: {
                return "INTEGER";
            }
            case 2: {
                return "LONG";
            }
            case 3: {
                return "FLOAT";
            }
            case 4: {
                return "DOUBLE";
            }
            case 5: {
                return "BOOLEAN";
            }
            case 6: {
                return "DATETIME";
            }
            case 7: {
                return "BLOB";
            }
            default: {
                return null;
            }
        }
    }
    
    public static int parseDataTypeFromString(final String datatype) {
        if (datatype != null) {
            if (datatype.equalsIgnoreCase("TEXT")) {
                return 0;
            }
            if (datatype.equalsIgnoreCase("INTEGER")) {
                return 1;
            }
            if (datatype.equalsIgnoreCase("LONG")) {
                return 2;
            }
            if (datatype.equalsIgnoreCase("FLOAT")) {
                return 3;
            }
            if (datatype.equalsIgnoreCase("DOUBLE")) {
                return 4;
            }
            if (datatype.equalsIgnoreCase("BOOLEAN")) {
                return 5;
            }
            if (datatype.equalsIgnoreCase("DATETIME")) {
                return 6;
            }
            if (datatype.equalsIgnoreCase("BLOB")) {
                return 7;
            }
        }
        return -1;
    }
    
    public boolean isText() {
        return this.getDataType() == 0;
    }
    
    public boolean isNumeric() {
        return this.isInteger() || this.isDecimal();
    }
    
    public boolean isDecimal() {
        return this.getDataType() == 3 || this.getDataType() == 4;
    }
    
    public boolean isInteger() {
        return this.getDataType() == 1 || this.getDataType() == 2;
    }
    
    public boolean isDateTime() {
        return this.getDataType() == 6;
    }
    
    public boolean isBoolean() {
        return this.getDataType() == 5;
    }
    
    public boolean isBLOB() {
        return this.getDataType() == 7;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RestAttributeMeta) {
            final RestAttributeMeta compare = (RestAttributeMeta)obj;
            if (compare.getName() != null && this.getName() != null) {
                return compare.getName().equalsIgnoreCase(this.getName());
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.getName() + " - " + this.getDataTypeAsString() + (this.isUniqueId() ? ", resourceid" : "") + (this.isRequired() ? ", required" : "");
    }
    
    public interface DataType
    {
        int TEXT = 0;
        int INTEGER = 1;
        int LONG = 2;
        int FLOAT = 3;
        int DOUBLE = 4;
        int BOOLEAN = 5;
        int DATETIME = 6;
        int BLOB = 7;
    }
}
