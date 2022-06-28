// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.Serializable;

public interface RestEntity extends RestConstants, Serializable {
    boolean canSaveToServer();

    void validateForServer() throws RestException;

    void close();
}
