// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient.mbo;

import com.avenue.maximo.restclient.MaximoRestConnector;
import com.avenue.maximo.restclient.RestResource;
import com.avenue.maximo.restclient.RestResourceSet;
import com.avenue.maximo.restclient.RestResourceSetImpl;

public class RestMboSet extends RestResourceSetImpl {
    private String relationshipName;
    private RestResource owner;

    public RestMboSet() {
    }

    public RestMboSet(final String resName, final MaximoRestConnector mc) {
        super(resName, mc);
    }

    @Override
    public String getHandlerContext() {
        return "mbo";
    }

    @Override
    protected RestResource initNewResource() {
        return new RestMbo(this);
    }

    public void setRelationship(final String relationship) {
        this.relationshipName = relationship;
    }

    public String getRelationship() {
        return this.relationshipName;
    }

    public RestResource getOwner() {
        return this.owner;
    }

    public void setOwner(final RestResource res) {
        this.owner = res;
    }

    @Override
    public String getURI() {
        final StringBuilder strb = new StringBuilder(this.getMaximoRestConnector().getConnectorURI(this));
        if (this.getOwner() != null && this.getOwner() instanceof RestMbo && !this.getOwner().toBeAdded() && !this.getOwner().toBeDeleted()) {
            strb.append("/").append(this.getOwner().getTableName()).append("/").append(this.getOwner().getUniqueID());
        }
        strb.append("/");
        if (this.getRelationship() != null) {
            strb.append(this.getRelationship());
        } else {
            strb.append(this.getName());
        }
        return strb.toString();
    }

    @Override
    public RestResourceSet setName(final String name) {
        this.setTableName(name);
        return super.setName(name);
    }
}
