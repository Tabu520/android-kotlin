// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.Date;
import java.util.Locale;

public interface RestAttribute extends RestResponseParser, RestEntity {
    RestAttributeMeta getMeta();

    String getName();

    RestResource getOwner();

    void resolveDataType();

    RestAttribute setDataType(final int p0);

    int getDataType();

    String getDataTypeAsString();

    Object getValue();

    String getString();

    Boolean getBoolean();

    Integer getInt(final Locale p0);

    Integer getInt();

    Long getLong(final Locale p0);

    Long getLong();

    Float getFloat(final Locale p0);

    Float getFloat();

    Double getDouble(final Locale p0);

    Double getDouble();

    Date getDate();

    byte[] getBytes();

    Object getInitialValue();

    Object getPreviousValue();

    RestAttribute setValue(final Object p0) throws RestException;

    RestAttribute rollbackToInitialValue();

    RestAttribute undo() throws RestException;

    String getLocaleValue();

    boolean isRequired();

    RestAttribute setRequired(final boolean p0);

    boolean isUniqueId();

    RestAttribute setUniqueId(final boolean p0);

    boolean isChanged();

    RestAttribute setChanged(final boolean p0);

    boolean isNull();

    boolean isText();

    boolean isNumeric();

    boolean isDecimal();

    boolean isInteger();

    boolean isDateTime();

    boolean isBoolean();

    boolean isBLOB();

    boolean isHidden();

    RestAttribute setHidden(final boolean p0);

    boolean isReadOnly();

    RestAttribute setReadOnly(final boolean p0);
}
