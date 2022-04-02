// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient.os;

import com.avenue.maximo.restclient.RestResource;
import com.avenue.maximo.restclient.RestException;
import java.io.IOException;
import com.avenue.maximo.restclient.RestParams;
import com.avenue.maximo.restclient.MaximoRestConnector;
import com.avenue.maximo.restclient.mbo.RestMboSet;
import com.avenue.maximo.restclient.RestResourceSet;
import com.avenue.maximo.restclient.RestResourceImpl;

public class RestObjectStructure extends RestResourceImpl
{
    public static final String ACTION_CHANGE = "Change";
    public static final String ACTION_ADD_CHANGE = "AddChange";
    
    public RestObjectStructure(final RestObjectStructureSet osSet) {
        super(osSet);
    }
    
    public RestObjectStructure(final int index, final RestObjectStructureSet osSet) {
        super(index, osSet);
    }
    
    @Override
    public String getHandlerContext() {
        return "os";
    }
    
    private String lookupChildRelationship(final String childName) throws IOException, RestException {
        String result = null;
        final MaximoRestConnector mc = this.getMaximoRestConnector();
        final RestParams oldRestParams = mc.getRestParams().clone();
        final RestMboSet moidSet = (RestMboSet)new RestMboSet("MAXINTOBJDETAIL", mc).select("RELATION").where("INTOBJECTNAME='" + this.getName() + "' and  OBJECTNAME ='" + childName + "'");
        moidSet.loadFromServer();
        if (moidSet.count() > 0) {
            result = moidSet.get(0).getString("RELATION");
            mc.setRestParams(oldRestParams);
        }
        return result;
    }
    
    public RestResource invokeAction(final String actionName) {
        this.getMaximoRestConnector().getRestParams().put("_action=", actionName);
        return this;
    }
}
