// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient.os;

import com.avenue.maximo.restclient.MaximoRestConnector;
import com.avenue.maximo.restclient.RestException;
import com.avenue.maximo.restclient.RestResource;
import com.avenue.maximo.restclient.RestResourceSetImpl;
import com.avenue.maximo.restclient.mbo.RestMboSet;

import java.io.IOException;

public class RestObjectStructureSet extends RestResourceSetImpl {
    private RestResource owner;

    public RestObjectStructureSet() {
    }

    public RestObjectStructureSet(final String resName, final MaximoRestConnector mc) {
        super(resName, mc);
    }

    public RestObjectStructureSet(final String resName, final String tableName, final MaximoRestConnector mc) {
        super(resName, tableName, mc);
    }

    @Override
    public String getHandlerContext() {
        return "os";
    }

    @Override
    protected RestResource initNewResource() {
        return new RestObjectStructure(this);
    }

    public RestMboSet getRootMboSet() throws RestException, IOException {
        if (this.getTableName() != null && this.getMaximoRestConnector() != null) {
            return (RestMboSet) new RestMboSet(this.getTableName(), this.getMaximoRestConnector()).setStartPosition(this.getStartPosition()).setMaxItems(this.getMaxItems()).select(this.getSelectClause()).where(this.getWhereClause()).where(this.getQueryWhere());
        }
        return null;
    }

    public RestResource getOwner() {
        return this.owner;
    }

    public void setOwner(final RestResource res) {
        this.owner = res;
    }
}
