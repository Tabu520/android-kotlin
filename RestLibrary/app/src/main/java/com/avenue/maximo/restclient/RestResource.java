// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.Map;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public interface RestResource extends RestRequester, RestResponseParser, RestEntity {
    RestResourceMeta getMeta();

    RestResourceSet getThisSet();

    MaximoRestConnector getMaximoRestConnector();

    String getRowStamp();

    String getName();

    String getTableName();

    RestResource setUniqueID(final String p0);

    String getUniqueID();

    String getUniqueColumn();

    String getSelectClause();

    RestResource setIndex(final int p0);

    int getIndex();

    void reloadFromServer(final String... p0) throws RestException;

    RestResource setAttributes(final List<RestAttribute> p0, final boolean p1);

    List<RestAttribute> getAttributes();

    boolean hasAttributes();

    boolean containsAttribute(final String p0);

    boolean containsAttribute(final RestAttribute p0);

    RestAttribute addAttribute(final String p0) throws RestException;

    RestResource addAttribute(final String p0, final Object p1) throws RestException;

    RestResource addAttribute(final RestAttribute p0) throws RestException;

    RestAttribute getAttribute(final int p0);

    RestAttribute getAttribute(final String p0);

    RestResource setLoaded(final boolean p0);

    boolean isLoaded();

    RestResource setModified(final boolean p0);

    boolean isModified();

    RestResource setToBeAdded(final boolean p0);

    boolean toBeAdded();

    RestResource setToBeDeleted(final boolean p0);

    boolean toBeDeleted();

    boolean toBeSaved();

    boolean isNull(final String p0);

    Object getValue(final String p0);

    String getString(final String p0);

    Boolean getBoolean(final String p0);

    Integer getInt(final String p0, final Locale p1);

    Integer getInt(final String p0);

    Long getLong(final String p0, final Locale p1);

    Long getLong(final String p0);

    Float getFloat(final String p0, final Locale p1);

    Float getFloat(final String p0);

    Double getDouble(final String p0, final Locale p1);

    Double getDouble(final String p0);

    Date getDate(final String p0);

    byte[] getBytes(final String p0);

    RestResource setValue(final String p0, final Object p1) throws RestException;

    RestResource setNull(final String p0) throws RestException;

    RestResource discardChanges();

    boolean isHidden();

    RestResource setHidden(final boolean p0);

    boolean isReadOnly();

    RestResource setReadOnly(final boolean p0);

    RestAttribute getNewAttributeInstance(final String p0);

    RestResourceSet getRelatedSet(final String p0);

    Map<String, RestResourceSet> getRelatedSetList();
}
