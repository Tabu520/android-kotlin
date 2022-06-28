// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient.mbo;

import com.avenue.maximo.restclient.RestResource;
import com.avenue.maximo.restclient.RestResourceImpl;
import com.avenue.maximo.restclient.RestResourceSet;

public class RestMbo extends RestResourceImpl {
    public RestMbo(final RestMboSet restMboSet) {
        super(restMboSet);
    }

    public RestMbo(final int index, final RestMboSet restMboSet) {
        super(index, restMboSet);
    }

    @Override
    public String getHandlerContext() {
        return "mbo";
    }

    @Override
    public RestResourceSet getRelatedSet(final String name) {
        RestResourceSet retSet = this.getRelatedSetList().get(name);
        if (retSet == null) {
            retSet = new RestMboSet(null, this.getMaximoRestConnector());
            ((RestMboSet) retSet).setOwner(this);
            ((RestMboSet) retSet).setRelationship(name);
            this.getRelatedSetList().put(name, retSet);
        }
        return retSet;
    }

    @Override
    public String getURI() {
        if (this.getOwner() != null) {
            return this.getThisSet().getURI() + "/" + this.getUniqueID();
        }
        return super.getURI();
    }

    public RestResource getOwner() {
        if (this.getThisSet() != null) {
            return ((RestMboSet) this.getThisSet()).getOwner();
        }
        return null;
    }
}
