// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.InputStream;

public interface RestRequester
{
    void loadFromServer() throws RestException;
    
    void reloadFromServer() throws RestException;
    
    void saveToServer() throws RestException;
    
    void handleResponse(final InputStream p0) throws RestException;
    
    void disconnect() throws RestException;
    
    String getHandlerContext();
    
    String getURI();
}
