// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

public interface RestResponseParser
{
    void parseJsonResponse(final Object p0) throws RestException;
    
    void parseXMLResponse(final Object p0) throws RestException;
}
