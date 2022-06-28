// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.text.ParseException;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

public class RestAttributeImpl implements RestAttribute, RestAttributeMeta.DataType {
    private RestAttributeMeta meta;
    private RestResource restResource;
    private Object initialContent;
    private Object previousContent;
    private Object currentContent;
    private String localeContent;
    private boolean isHidden;
    private boolean isReadOnly;
    private boolean isChanged;

    public RestAttributeImpl(final String name, final RestResource restResource) {
        this.isHidden = false;
        this.isReadOnly = false;
        this.isChanged = false;
        this.setName(name);
        this.setOwner(restResource);
    }

    public RestAttributeImpl(final RestResource restResource) {
        this(null, restResource);
    }

    public RestAttributeImpl(final String name) {
        this(name, null);
    }

    public RestAttributeImpl(final String name, final Object value, final RestResource restResource) {
        this(name, restResource);
        this.currentContent = value;
        this.previousContent = value;
        this.initialContent = value;
        this.resolveDataType();
    }

    protected RestAttribute setOwner(final RestResource res) {
        this.restResource = res;
        return this;
    }

    @Override
    public RestResource getOwner() {
        return this.restResource;
    }

    protected RestAttributeMeta initMeta() {
        return new RestAttributeMeta();
    }

    protected RestAttribute setMeta(final RestAttributeMeta meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public RestAttributeMeta getMeta() {
        if (this.meta == null) {
            this.setMeta(this.initMeta());
        }
        return this.meta;
    }

    protected RestAttribute setName(final String name) {
        this.getMeta().setName(name != null ? name.toUpperCase() : null);
        return this;
    }

    @Override
    public String getName() {
        return this.getMeta().getName();
    }

    protected void onResponseParsed() {
    }

    @Override
    public void parseJsonResponse(final Object parser) throws RestException {
        try {
            JsonParser jsonParser = (JsonParser)parser;

            for(JsonToken token = jsonParser.nextToken(); token != JsonToken.END_OBJECT; token = jsonParser.nextToken()) {
                if (token == JsonToken.FIELD_NAME) {
                    String fieldName = jsonParser.getCurrentName();
                    token = jsonParser.nextToken();
                    if (fieldName.equals("hidden")) {
                        this.setHidden(jsonParser.getBooleanValue());
                    } else if (fieldName.equals("readonly")) {
                        this.setReadOnly(jsonParser.getBooleanValue());
                    } else if (fieldName.equals("required")) {
                        this.setRequired(jsonParser.getBooleanValue());
                    } else if (fieldName.equals("resourceid")) {
                        this.setUniqueId(jsonParser.getBooleanValue());
                    } else if (!fieldName.equals("content")) {
                        if (fieldName.equals("localecontent")) {
                            this.localeContent = jsonParser.getText();
                        } else {
                            this.getMeta().setName(fieldName);
                        }
                    } else {
                        Object contentValue = null;
                        if (token == JsonToken.VALUE_STRING) {
                            contentValue = jsonParser.getText();
                            this.setDataType(0);
                        } else if (token == JsonToken.VALUE_NUMBER_INT) {
                            contentValue = jsonParser.getIntValue();
                            this.setDataType(1);
                        } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                            contentValue = jsonParser.getDoubleValue();
                            this.setDataType(4);
                        } else if (token != JsonToken.VALUE_TRUE && token != JsonToken.VALUE_FALSE) {
                            if (token == JsonToken.VALUE_NULL) {
                                contentValue = null;
                            }
                        } else {
                            contentValue = jsonParser.getBooleanValue();
                            this.setDataType(5);
                        }

                        this.initialContent = contentValue;
                        this.currentContent = contentValue;
                    }
                }
            }

            this.onResponseParsed();
        } catch (IOException var6) {
            throw new RestException(var6);
        }
    }

    @Override
    public void parseXMLResponse(final Object parser) throws RestException {
        try {
            final XmlPullParser xmlParser = (XmlPullParser)parser;
            for (int event = xmlParser.getEventType(); event != 1; event = xmlParser.next()) {
                if (event == 2) {
                    final String isHidden = xmlParser.getAttributeValue(null, "hidden");
                    if (isHidden != null) {
                        this.setHidden(Integer.parseInt(isHidden) == 1);
                    }
                    final String isReadOnly = xmlParser.getAttributeValue(null, "readonly");
                    if (isReadOnly != null) {
                        this.setReadOnly(Integer.parseInt(isReadOnly) == 1);
                    }
                    final String isRequired = xmlParser.getAttributeValue(null, "required");
                    if (isRequired != null) {
                        this.setRequired(Integer.parseInt(isRequired) == 1);
                    }
                    final String isResourceId = xmlParser.getAttributeValue(null, "resourceid");
                    if (isResourceId != null) {
                        this.setUniqueId(Integer.parseInt(isResourceId) == 1);
                    }
                }
                if (event == 4) {
                    final String parsedContent = xmlParser.getText();
                    this.initialContent = parsedContent;
                    this.currentContent = parsedContent;
                    this.localeContent = parsedContent;
                }
                if (event == 3) {
                    break;
                }
            }
            this.resolveDataType();
            this.onResponseParsed();
        }
        catch (XmlPullParserException | IOException xppe) {
            throw new RestException(xppe);
        }
    }

    @Override
    public void resolveDataType() {
        if (this.getValue() instanceof byte[]) {
            this.setDataType(7);
        } else if (!(this.getValue() instanceof Date) && this.getDate() == null) {
            if (!(this.getValue() instanceof Boolean) && this.getBoolean() == null) {
                if (!(this.getValue() instanceof Integer) && this.getInt() == null) {
                    if (this.getValue() instanceof Float || this.getFloat() != null) {
                        this.setDataType(!(this.getValue() instanceof Double) && this.getDouble() == null ? 3 : 4);
                    }
                } else {
                    this.setDataType(!(this.getValue() instanceof Long) && this.getLong() == null ? 1 : 2);
                }
            } else {
                this.setDataType(5);
            }
        } else {
            this.setDataType(6);
        }
    }

    @Override
    public RestAttribute setHidden(final boolean value) {
        this.isHidden = value;
        return this;
    }

    @Override
    public boolean isHidden() {
        return this.isHidden;
    }

    @Override
    public RestAttribute setReadOnly(final boolean value) {
        this.isReadOnly = value;
        return this;
    }

    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    @Override
    public RestAttribute setRequired(final boolean value) {
        this.getMeta().setRequired(value);
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.getMeta().isRequired();
    }

    @Override
    public RestAttribute setUniqueId(final boolean value) {
        this.getMeta().setUniqueId(value);
        return this;
    }

    @Override
    public boolean isUniqueId() {
        return this.getMeta().isUniqueId();
    }

    @Override
    public RestAttribute setValue(final Object value) throws RestException {
        if (!this.isReadOnly()) {
            this.previousContent = this.getValue();
            this.currentContent = value;
            this.setChanged(true);
            return this;
        }
        throw new RestException("Attribute " + this.getName() + " is readonly!");
    }

    @Override
    public RestAttribute rollbackToInitialValue() {
        if (this.isChanged()) {
            this.currentContent = this.getInitialValue();
            this.previousContent = this.getInitialValue();
            this.setChanged(false);
        }
        return this;
    }

    @Override
    public Object getInitialValue() {
        return this.initialContent;
    }

    @Override
    public Object getPreviousValue() {
        return this.previousContent;
    }

    @Override
    public Object getValue() {
        return this.currentContent;
    }

    @Override
    public RestAttribute undo() throws RestException {
        if (this.isChanged()) {
            this.setValue(this.getPreviousValue());
        }
        return this;
    }

    @Override
    public String getLocaleValue() {
        return this.localeContent;
    }

    @Override
    public RestAttribute setChanged(final boolean value) {
        this.isChanged = value;
        return this;
    }

    @Override
    public boolean isChanged() {
        return this.isChanged;
    }

    @Override
    public String getString() {
        return this.getStringFromValue(true);
    }

    private String getStringFromValue(final boolean removeApostrophe) {
        String ret = null;
        if (this.currentContent != null) {
            if (this.getDataType() == 7 && this.getValue() instanceof byte[]) {
                ret = Base64.encodeBase64String((byte[]) this.currentContent);
            } else {
                ret = String.valueOf(this.currentContent);
                final String apos = "\"";
                if (removeApostrophe && ret.indexOf(apos) == 0 && ret.endsWith(apos)) {
                    ret = ret.substring(1, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    @Override
    public Boolean getBoolean() {
        if (this.getValue() instanceof Boolean) {
            return (Boolean) this.getValue();
        }
        final String value = this.getString();
        if (value != null) {
            if (value.equalsIgnoreCase("true") || value.equals("1")) {
                return true;
            }
            if (value.equalsIgnoreCase("false") || value.equals("0")) {
                return false;
            }
        }
        return null;
    }

    @Override
    public Integer getInt(final Locale locale) {
        if (this.getValue() instanceof Integer) {
            return (Integer) this.getValue();
        }
        final Number formmattedNum = this.getNumberFromString(locale);
        if (formmattedNum != null) {
            return formmattedNum.intValue();
        }
        return null;
    }

    @Override
    public Integer getInt() {
        return this.getInt(Locale.getDefault());
    }

    @Override
    public Long getLong(final Locale locale) {
        if (this.getValue() instanceof Long) {
            return (Long) this.getValue();
        }
        final Number formmattedNum = this.getNumberFromString(locale);
        if (formmattedNum != null) {
            return formmattedNum.longValue();
        }
        return null;
    }

    @Override
    public Long getLong() {
        return this.getLong(Locale.getDefault());
    }

    @Override
    public Float getFloat(final Locale locale) {
        if (this.getValue() instanceof Float) {
            return (Float) this.getValue();
        }
        final Number formmattedNum = this.getNumberFromString(locale);
        if (formmattedNum != null) {
            return formmattedNum.floatValue();
        }
        return null;
    }

    @Override
    public Float getFloat() {
        return this.getFloat(Locale.getDefault());
    }

    @Override
    public Double getDouble(final Locale locale) {
        if (this.getValue() instanceof Double) {
            return (Double) this.getValue();
        }
        final Number formmattedNum = this.getNumberFromString(locale);
        if (formmattedNum != null) {
            return formmattedNum.doubleValue();
        }
        return null;
    }

    @Override
    public Double getDouble() {
        return this.getDouble(Locale.getDefault());
    }

    private Number getNumberFromString(final Locale locale) {
        final NumberFormat nf = NumberFormat.getNumberInstance(locale);
        try {
            final String value = this.getStringFromValue(false);
            if (value == null) {
                throw new NullPointerException();
            }
            return Double.parseDouble(String.format(locale, "%.2f", nf.parse(value).doubleValue()));
        } catch (ParseException | NumberFormatException | NullPointerException ex1) {
            return null;
        }
    }

    @Override
    public Date getDate() {
        if (this.getValue() instanceof Date) {
            return (Date) this.getValue();
        }
        try {
            return RestAttributeImpl.REST_DATE_RESPONSE_FORMAT.parse(this.getStringFromValue(true));
        } catch (ClassCastException | ParseException | NullPointerException ex1) {
            return null;
        }
    }

    @Override
    public byte[] getBytes() {
        if (this.getValue() instanceof byte[]) {
            return (byte[]) this.getValue();
        }
        return Base64.decodeBase64(this.getString());
    }

    @Override
    public RestAttribute setDataType(final int type) {
        this.getMeta().setDataType(type);
        return this;
    }

    @Override
    public String getDataTypeAsString() {
        return this.getMeta().getDataTypeAsString();
    }

    @Override
    public int getDataType() {
        return this.getMeta().getDataType();
    }

    @Override
    public boolean isNull() {
        return this.getValue() == null;
    }

    @Override
    public boolean isText() {
        return this.getMeta().isText();
    }

    @Override
    public boolean isNumeric() {
        return this.getMeta().isNumeric();
    }

    @Override
    public boolean isDecimal() {
        return this.getMeta().isDecimal();
    }

    @Override
    public boolean isInteger() {
        return this.getMeta().isInteger();
    }

    @Override
    public boolean isDateTime() {
        return this.getMeta().isDateTime();
    }

    @Override
    public boolean isBoolean() {
        return this.getMeta().isBoolean();
    }

    @Override
    public boolean isBLOB() {
        return this.getMeta().isBLOB();
    }

    @Override
    public void close() {
        synchronized (this) {
            this.setMeta(null);
            this.restResource = null;
            this.initialContent = null;
            this.previousContent = null;
            this.currentContent = null;
            this.localeContent = null;
        }
        this.onClosed();
    }

    protected void onClosed() {
    }

    @Override
    public void validateForServer() throws RestException {
    }

    @Override
    public boolean canSaveToServer() {
        return !this.isUniqueId() && !this.isNull();
    }

    @Override
    public String toString() {
        return this.getMeta().toString() + (this.isHidden() ? ", hidden" : "") + (this.isReadOnly() ? ", readonly" : "") + (this.isChanged() ? ", changed" : "") + " = " + this.getString();
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof RestAttribute && ((RestAttribute) o).getMeta().equals(this.getMeta());
    }
}
