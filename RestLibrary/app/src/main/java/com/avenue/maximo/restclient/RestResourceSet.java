// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.List;

public interface RestResourceSet extends RestRequester, RestResponseParser, RestEntity
{
    RestResourceMeta getMeta();
    
    RestResourceSet setMaximoRestConnector(final MaximoRestConnector p0);
    
    MaximoRestConnector getMaximoRestConnector();
    
    boolean isLoaded();
    
    boolean toBeSaved();
    
    RestResourceSet setTableName(final String p0);
    
    String getTableName();
    
    RestResourceSet setName(final String p0);
    
    String getName();
    
    RestResourceSet setStartPosition(final int p0);
    
    int getStartPosition();
    
    Integer getRsCount();
    
    Integer getRsTotal();
    
    RestResourceSet setUniqueColumn(final String p0);
    
    String getUniqueColumn();
    
    List<RestResource> getMemberList();
    
     <T extends RestResource> List<T> getMemberList(final Class<T> p0);
    
    RestResourceSet select(final String... p0);
    
    String getSelectClause();
    
    RestResourceSet where(final String p0);
    
    String getWhereClause();
    
    RestResourceSet where(final RestQueryWhere p0);
    
    RestQueryWhere getQueryWhere();
    
    RestResourceSet orderByAsc(final String... p0);
    
    String getOrderByAsc();
    
    RestResourceSet orderByDesc(final String... p0);
    
    String getOrderByDesc();
    
    RestResourceSet setMaxItems(final int p0);
    
    Integer getMaxItems();
    
    int getPageID();
    
    boolean isPagingAvailable();
    
    boolean hasNextPage();
    
    RestResourceSet nextPage() throws RestException;
    
    RestResourceSet previousPage() throws RestException;
    
    RestResource getCurrentResource();
    
    RestResource moveFirst();
    
    RestResource moveLast();
    
    RestResource moveNext();
    
    RestResource movePrevious();
    
    RestResource moveTo(final int p0);
    
    boolean isEmpty();
    
    int count();
    
    int countAfterCommitToServer();
    
    int totalCount();
    
    RestResource get(final int p0);
    
    RestResource get(final String p0);
    
    RestResource add();
    
    RestResource remove(final RestResource p0);
    
    RestResource remove(final String p0);
    
    RestResource remove(final int p0);
    
    RestResourceSet discardChanges();
    
    void clear();
}
