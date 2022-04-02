// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import org.xmlpull.v1.XmlPullParserException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.Reader;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserFactory;

import com.fasterxml.jackson.core.JsonFactory;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class RestResourceSetImpl implements RestResourceSet {
    private RestResourceMeta meta;
    private Integer maxItems;
    private int pageID;
    private String whereClause;
    private String selectClause;
    private String orderByAscClause;
    private String orderByDescClause;
    private RestQueryWhere restQueryWhere;
    private MaximoRestConnector mc;
    private List<RestResource> memberList;
    private boolean isLoaded;
    private int rsStart;
    private Integer rsCount;
    private Integer rsTotal;
    private RestResource currRes;
    private int currIndex;
    private boolean toBeSaved;

    public RestResourceSetImpl() {
        this.maxItems = null;
        this.pageID = 1;
        this.whereClause = null;
        this.selectClause = "";
        this.orderByAscClause = null;
        this.orderByDescClause = null;
        this.restQueryWhere = null;
        this.isLoaded = false;
        this.rsStart = 0;
        this.rsCount = null;
        this.rsTotal = null;
        this.currRes = null;
        this.currIndex = -1;
        this.toBeSaved = false;
        this.setMemberList(new ArrayList<RestResource>());
    }

    public RestResourceSetImpl(final String name, final MaximoRestConnector connector) {
        this();
        this.setName(name);
        this.setMaximoRestConnector(connector);
    }

    public RestResourceSetImpl(final String name, final String tablename, final MaximoRestConnector connector) {
        this(name, connector);
        this.setTableName(tablename);
    }

    protected abstract RestResource initNewResource();

    @Override
    public RestResourceSet setMaximoRestConnector(final MaximoRestConnector connector) {
        this.mc = connector;
        if (this.mc != null) {
            final RestOptions options = this.mc.getRestOptions();
            if (options.getHandlerContext() == null) {
                options.setHandlerContext(this.getHandlerContext());
            }
        }
        return this;
    }

    @Override
    public MaximoRestConnector getMaximoRestConnector() {
        return this.mc;
    }

    protected RestResourceMeta initMeta() {
        return new RestResourceMeta();
    }

    protected RestResourceSet setMeta(final RestResourceMeta meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public RestResourceMeta getMeta() {
        if (this.meta == null) {
            this.setMeta(this.initMeta());
        }
        return this.meta;
    }

    protected RestResourceSet setLoaded(final boolean value) {
        this.isLoaded = value;
        return this;
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    protected void setToBeSaved(final boolean value) {
        this.toBeSaved = value;
    }

    @Override
    public boolean toBeSaved() {
        if (this.toBeSaved) {
            return true;
        }
        for (RestResource rs = this.moveFirst(); rs != null; rs = this.moveNext()) {
            if (rs.toBeSaved()) {
                this.setToBeSaved(true);
                return this.toBeSaved;
            }
        }
        return false;
    }

    @Override
    public RestResourceSet setTableName(final String name) {
        this.getMeta().setTableName((name != null) ? name.toUpperCase() : null);
        return this;
    }

    @Override
    public String getTableName() {
        return this.getMeta().getTableName();
    }

    @Override
    public RestResourceSet setName(final String name) {
        this.getMeta().setName((name != null) ? name.toUpperCase() : null);
        return this;
    }

    @Override
    public String getName() {
        return this.getMeta().getName();
    }

    @Override
    public String getURI() {
        return this.getMaximoRestConnector().getConnectorURI(this) + "/" + this.getName();
    }

    @Override
    public RestResourceSet setStartPosition(final int pos) {
        this.rsStart = pos;
        return this;
    }

    @Override
    public int getStartPosition() {
        return this.rsStart;
    }

    protected void setRsCount(final int value) {
        this.rsCount = value;
    }

    @Override
    public Integer getRsCount() {
        return this.rsCount;
    }

    protected void setRsTotal(final int value) {
        this.rsTotal = value;
    }

    @Override
    public Integer getRsTotal() {
        return this.rsTotal;
    }

    @Override
    public RestResourceSet setUniqueColumn(final String value) {
        this.getMeta().setUniqueColumn(value);
        return this;
    }

    @Override
    public String getUniqueColumn() {
        return this.getMeta().getUniqueColumn();
    }

    protected void onResponseHandled() {
        this.setLoaded(true);
    }

    protected RestResourceSet setMemberList(final List<RestResource> list) {
        this.memberList = list;
        return this;
    }

    @Override
    public List<RestResource> getMemberList() {
        return this.memberList;
    }

    @Override
    public <T extends RestResource> ArrayList<T> getMemberList(final Class<T> subTypeOfResource) {
        final ArrayList<T> retList = new ArrayList<T>();
        for (final RestResource rs : this.memberList) {
            retList.add(rs.getIndex(), subTypeOfResource.cast(rs));
        }
        return retList;
    }

    @Override
    public RestResourceSet select(final String... attributes) {
        this.selectClause = new RestQuerySelect().select(attributes);
        return this;
    }

    @Override
    public String getSelectClause() {
        return this.selectClause;
    }

    @Override
    public RestResourceSet where(final String clause) {
        this.whereClause = clause;
        return this;
    }

    @Override
    public String getWhereClause() {
        return this.whereClause;
    }

    @Override
    public RestResourceSet where(final RestQueryWhere where) {
        this.restQueryWhere = where;
        return this;
    }

    @Override
    public RestQueryWhere getQueryWhere() {
        return this.restQueryWhere;
    }

    @Override
    public RestResourceSet orderByAsc(final String... attributes) {
        this.orderByAscClause = new RestQuerySelect().select(attributes);
        return this;
    }

    @Override
    public String getOrderByAsc() {
        return this.orderByAscClause;
    }

    @Override
    public RestResourceSet orderByDesc(final String... attributes) {
        this.orderByDescClause = new RestQuerySelect().select(attributes);
        return this;
    }

    @Override
    public String getOrderByDesc() {
        return this.orderByDescClause;
    }

    @Override
    public RestResourceSet setMaxItems(final int size) {
        this.maxItems = size;
        return this;
    }

    @Override
    public Integer getMaxItems() {
        return this.maxItems;
    }

    @Override
    public int getPageID() {
        return this.pageID;
    }

    @Override
    public void handleResponse(final InputStream response) throws RestException {
        try {
            final Reader inputReader = new InputStreamReader(response, "utf-8");
            if (this.getMaximoRestConnector().getRestParams().isJSONFormat()) {
                this.parseJsonResponse(new JsonFactory().createParser(inputReader));
            } else {
                final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                final XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputReader);
                this.parseXMLResponse(xpp);
            }
        } catch (Exception ex) {
            throw new RestException("Errors while parsing the response", ex);
        } finally {
            try {
                response.close();
                this.disconnect();
            } catch (IOException ioex) {
                throw new RestException("Errors while closing the response", ioex);
            }
        }
    }

    @Override
    public void parseJsonResponse(final Object parser) throws RestException {
        try {
            final JsonParser jsonParser = (JsonParser) parser;
            JsonToken token = jsonParser.getCurrentToken();
            String fieldName = "";
            while (token != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    fieldName = jsonParser.getCurrentName();
                } else if (token == JsonToken.VALUE_NUMBER_INT) {
                    if (fieldName.equals("rsStart")) {
                        this.setStartPosition(jsonParser.getIntValue());
                    } else if (fieldName.equals("rsCount")) {
                        this.setRsCount(jsonParser.getIntValue());
                    } else if (fieldName.equals("rsTotal")) {
                        this.setRsTotal(jsonParser.getIntValue());
                    }
                } else if (token == JsonToken.START_ARRAY) {
                    if (this.getName() == null) {
                        this.setName(fieldName);
                    }
                    if (this.getTableName() == null) {
                        this.setTableName(fieldName);
                    }
                    for (JsonToken token2 = jsonParser.getCurrentToken(); token2 != JsonToken.END_ARRAY && token2 != null; token2 = jsonParser.nextToken()) {
                        if (token2 == JsonToken.START_OBJECT) {
                            final RestResource newRes = this.populateNewResource();
                            newRes.parseJsonResponse(parser);
                            this.onResourceFetched(newRes);
                        }
                    }
                    break;
                }
                token = jsonParser.nextToken();
            }
            this.onResponseHandled();
        } catch (IOException ex) {
            throw new RestException(ex);
        }
    }

    @Override
    public void parseXMLResponse(final Object parser) throws RestException {
        try {
            final XmlPullParser xmlParser = (XmlPullParser) parser;
            for (int event = xmlParser.getEventType(); event != 1; event = xmlParser.next()) {
                String requiredMeta = null;
                String hiddenMeta = null;
                String readonlyMeta = null;
                String tagName = xmlParser.getName();
                if (event == XmlPullParser.START_TAG) {
                    final String rsStart = xmlParser.getAttributeValue(null, "rsStart");
                    final String rsCount = xmlParser.getAttributeValue(null, "rsCount");
                    final String rsTotal = xmlParser.getAttributeValue(null, "rsTotal");
                    requiredMeta = xmlParser.getAttributeValue(null, "required");
                    hiddenMeta = xmlParser.getAttributeValue(null, "hidden");
                    readonlyMeta = xmlParser.getAttributeValue(null, "readonly");
                    tagName = xmlParser.getName();
                    if (rsStart != null || rsCount != null || rsTotal != null) {
                        if (rsStart != null) {
                            this.setStartPosition(Integer.parseInt(rsStart));
                        }
                        if (rsCount != null) {
                            this.setRsCount(Integer.parseInt(rsCount));
                        }
                        if (rsTotal != null) {
                            this.setRsTotal(Integer.parseInt(rsTotal));
                        }
                    } else if (requiredMeta == null && hiddenMeta != null && readonlyMeta != null) {
                        if (this.getTableName() != null && !tagName.equals(this.getTableName())) {
                            break;
                        }
                        if (this.getTableName() == null) {
                            this.setTableName(tagName);
                        }
                        final RestResource newRes = this.populateNewResource();
                        newRes.parseXMLResponse(xmlParser);
                        this.onResourceFetched(newRes);
                    }
                }
                if (event == XmlPullParser.END_TAG) {
                    if (tagName.equals(this.getTableName()) ||
                            requiredMeta == null && hiddenMeta == null && readonlyMeta == null) {
                        break;
                    }
                }
            }
            this.onResponseHandled();
        } catch (XmlPullParserException xppe) {
            throw new RestException((Throwable) xppe);
        } catch (IOException ioe) {
            throw new RestException(ioe);
        }
    }

    protected void onResourceFetched(final RestResource resource) {
        if (this.getUniqueColumn() == null) {
            this.setUniqueColumn(resource.getUniqueColumn());
        }
    }

    @Override
    public void loadFromServer() throws RestException {
        if (!this.isLoaded()) {
            try {
                this.cleanOldResourceSetParams();
                this.buildParams();
                this.getMaximoRestConnector().get(this.getURI(), this);
                return;
            } catch (IOException ioe) {
                throw new RestException(ioe.getMessage(), ioe);
            }
//            throw new RestException(this.toString() + " is already loaded. Please use reloadFromServer()");
        }
        throw new RestException(this.toString() + " is already loaded. Please use reloadFromServer()");
    }

    @Override
    public void reloadFromServer() throws RestException {
        this.clear();
        this.setLoaded(false);
        this.loadFromServer();
    }

    @Override
    public boolean isPagingAvailable() {
        return this.getMaxItems() != null && this.getMaxItems() > 0;
    }

    @Override
    public boolean hasNextPage() {
        return this.isPagingAvailable() && this.getStartPosition() + this.getRsCount() < this.getRsTotal();
    }

    @Override
    public RestResourceSet nextPage() throws RestException {
        if (this.isPagingAvailable()) {
            final int tc = (this.getRsTotal() != null) ? this.getRsTotal() : this.totalCount();
            final int ns = this.count() + this.getMaxItems();
            this.setStartPosition((ns < tc) ? ns : this.count());
            ++this.pageID;
            return this;
        }
        throw new RestException("Paging is not available");
    }

    @Override
    public RestResourceSet previousPage() throws RestException {
        if (this.isPagingAvailable()) {
            final int ns = this.getStartPosition() - this.getMaxItems();
            this.setStartPosition((ns > 0) ? ns : 0);
            --this.pageID;
            return this;
        }
        throw new RestException("Paging is not available");
    }

    protected void buildParams() throws RestException {
        try {
            final RestParams params = this.getMaximoRestConnector().getRestParams();
            params.put("_rsStart", this.rsStart);
            if (this.maxItems != null) {
                params.put("_maxItems", this.maxItems);
            }
            if (this.selectClause != null && this.selectClause.length() > 0) {
                params.put("_includecols", this.selectClause);
            }
            if (this.orderByAscClause != null) {
                params.put("_orderbyasc", this.orderByAscClause);
            }
            if (this.orderByDescClause != null) {
                params.put("_orderbydesc", this.orderByDescClause);
            }
            if (this.whereClause != null) {
                params.put("_uw", this.whereClause);
            }
            if (this.restQueryWhere != null) {
                params.put("#restquerywhere", this.restQueryWhere.whereClause());
            }
        } catch (Exception e) {
            throw new RestException("Errors while building connection parameters", e);
        }
    }

    private void cleanOldResourceSetParams() {
        final MaximoRestConnector connector = this.getMaximoRestConnector();
        if (connector != null) {
            final RestParams restParams = connector.getRestParams();
            if (restParams != null) {
                restParams.remove("_maxItems");
                restParams.remove("_includecols");
                restParams.remove("_orderbyasc");
                restParams.remove("_orderbydesc");
                restParams.remove("_uw");
                restParams.remove("#restquerywhere");
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.count() <= 0;
    }

    @Override
    public int count() {
        return (this.getMemberList() != null) ? this.getMemberList().size() : 0;
    }

    @Override
    public int countAfterCommitToServer() {
        int count = this.count();
        for (final RestResource rs : this.getMemberList()) {
            if (rs.toBeDeleted()) {
                --count;
            }
        }
        return count;
    }

    @Override
    public int totalCount() {
        return this.getStartPosition() + this.count();
    }

    @Override
    public RestResource get(final int index) {
        if (index >= 0 && index < this.count()) {
            return this.getMemberList().get(index);
        }
        return null;
    }

    @Override
    public RestResource get(final String uniqueId) {
        if (this.isLoaded() && this.getMemberList() != null && !this.getMemberList().isEmpty()) {
            for (final RestResource rs : this.getMemberList()) {
                if (rs.getUniqueID().equals(uniqueId)) {
                    return rs;
                }
            }
        }
        return null;
    }

    protected RestResource populateNewResource() {
        final RestResource newRes = this.initNewResource().setIndex(this.count());
        this.getMemberList().add(this.count(), newRes);
        return newRes;
    }

    @Override
    public RestResource add() {
        final RestResource newRes = this.populateNewResource();
        newRes.setToBeAdded(true);
        ((RestResourceImpl) newRes).onAdded();
        return newRes;
    }

    @Override
    public RestResource remove(final RestResource res) {
        return this.remove(res.getUniqueID());
    }

    @Override
    public RestResource remove(final int index) {
        if (this.getMemberList() != null && !this.getMemberList().isEmpty()) {
            final RestResource removed = this.getMemberList().remove(index);
            removed.setToBeDeleted(true);
            this.updateIndicesForResourcesFrom(removed.getIndex());
            return removed;
        }
        return null;
    }

    private void updateIndicesForResourcesFrom(final int fromPos) {
        for (int i = fromPos; i < this.getMemberList().size(); ++i) {
            this.get(i).setIndex(i);
        }
    }

    @Override
    public RestResource remove(final String uniqueId) {
        final RestResource toBeRemovedRes = this.get(uniqueId);
        if (toBeRemovedRes != null) {
            this.remove(toBeRemovedRes.getIndex());
        }
        return null;
    }

    protected void reloadToIncludeUniqueColumn() throws RestException, IOException {
        final String uniqueColumn = this.getUniqueColumn();
        final String selectClause = this.getSelectClause();
        if (selectClause != null && !selectClause.contains(uniqueColumn)) {
            this.select(uniqueColumn, selectClause);
            this.reloadFromServer();
        }
    }

    @Override
    public void saveToServer() throws RestException {
        if (this.canSaveToServer()) {
            this.validateForServer();
            this.cleanOldResourceSetParams();
            this.buildParams();
            for (RestResource rs = this.moveFirst(); rs != null; rs = this.moveNext()) {
                rs.saveToServer();
            }
            this.disconnect();
        }
    }

    @Override
    public RestResourceSet discardChanges() {
        for (RestResource rs = this.moveFirst(); rs != null; rs = this.moveNext()) {
            rs.discardChanges();
        }
        return this;
    }

    @Override
    public void clear() {
        synchronized (this) {
            if (this.getMemberList() != null) {
                for (RestResource rs = this.moveLast(); rs != null; rs = this.movePrevious()) {
                    rs.close();
                }
                this.getMemberList().clear();
            }
        }
        this.onClear();
    }

    protected void onClear() {
    }

    @Override
    public void close() {
        this.clear();
        synchronized (this) {
            this.setMeta(null);
            this.maxItems = null;
            this.whereClause = null;
            this.selectClause = null;
            this.orderByAscClause = null;
            this.orderByDescClause = null;
            this.restQueryWhere = null;
            this.mc = null;
        }
        this.onClosed();
    }

    protected void onClosed() {
    }

    @Override
    public void disconnect() throws RestException {
        try {
            this.getMaximoRestConnector().disconnect();
            this.getMaximoRestConnector().closeConnection();
        } catch (IOException ioe) {
            throw new RestException(ioe.getMessage(), ioe);
        }
    }

    protected void setCurrentResource(final RestResource res) {
        this.currRes = res;
    }

    @Override
    public RestResource getCurrentResource() {
        return this.currRes;
    }

    @Override
    public RestResource moveFirst() {
        return this.moveTo(0);
    }

    @Override
    public RestResource moveLast() {
        final int n = this.count() - 1;
        this.currIndex = n;
        return this.moveTo(n);
    }

    @Override
    public RestResource moveNext() {
        return this.moveTo(this.currIndex + 1);
    }

    @Override
    public RestResource movePrevious() {
        return this.moveTo(this.currIndex - 1);
    }

    @Override
    public RestResource moveTo(final int position) {
        final RestResource res = this.get(position);
        if (res != null) {
            this.currIndex = position;
            this.setCurrentResource(res);
        }
        return res;
    }

    @Override
    public boolean canSaveToServer() {
        return true;
    }

    @Override
    public void validateForServer() throws RestException {
        if (this.getMeta() == null || this.getName() == null || this.getName().isEmpty()) {
            throw new RestException("The resource set is not valid");
        }
    }

    @Override
    public String toString() {
        return this.getMeta().toString() + " - RsStart: " + this.getStartPosition() + ", RsTotal: " + this.getRsTotal() + ", RsCount: " + this.getRsCount() + ", Count: " + this.count();
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof RestResourceSet && ((RestResourceSet) o).getMeta().equals(this.getMeta());
    }
}
