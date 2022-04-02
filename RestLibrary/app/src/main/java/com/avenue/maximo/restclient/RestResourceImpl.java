// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.util.HashMap;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import com.avenue.maximo.restclient.mbo.RestMboSet;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import org.xmlpull.v1.XmlPullParser;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParserFactory;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public abstract class RestResourceImpl implements RestResource
{
    private int index;
    private List<RestAttribute> attributeList;
    private boolean isHidden;
    private boolean isReadOnly;
    private boolean isLoaded;
    private boolean isModified;
    private boolean toBeAdded;
    private boolean toBeDeleted;
    private RestResourceSet thisSet;
    private String uniqueID;
    private String rowstamp;
    private Map<String, RestResourceSet> relatedSets;
    
    public RestResourceImpl(final int index, final RestResourceSet restResourceSet) {
        this.index = -1;
        this.isHidden = false;
        this.isReadOnly = false;
        this.isLoaded = false;
        this.isModified = false;
        this.toBeAdded = false;
        this.toBeDeleted = false;
        this.rowstamp = null;
        this.setThisSet(restResourceSet);
        this.setIndex(index);
    }
    
    public RestResourceImpl(final RestResourceSet restResourceSet) {
        this(restResourceSet.count(), restResourceSet);
    }
    
    protected RestResource setThisSet(final RestResourceSet restResourceSet) {
        this.thisSet = restResourceSet;
        return this;
    }
    
    @Override
    public RestResourceSet getThisSet() {
        return this.thisSet;
    }
    
    @Override
    public MaximoRestConnector getMaximoRestConnector() {
        return (this.getThisSet() != null) ? this.getThisSet().getMaximoRestConnector() : null;
    }
    
    protected RestResource setRowStamp(final String value) {
        this.rowstamp = value;
        return this;
    }
    
    @Override
    public String getRowStamp() {
        return this.rowstamp;
    }
    
    @Override
    public String getURI() {
        return this.getMaximoRestConnector().getConnectorURI(this) + "/" + this.getName() + "/" + this.getUniqueID();
    }
    
    @Override
    public RestResourceMeta getMeta() {
        if (this.getThisSet() != null) {
            return this.getThisSet().getMeta();
        }
        return null;
    }
    
    @Override
    public String getTableName() {
        return this.getMeta().getTableName();
    }
    
    @Override
    public String getName() {
        return this.getMeta().getName();
    }
    
    @Override
    public RestResource setUniqueID(final String uniqueID) {
        this.uniqueID = uniqueID;
        return this;
    }
    
    @Override
    public String getUniqueID() {
        return this.uniqueID;
    }
    
    @Override
    public String getUniqueColumn() {
        if (this.getThisSet().getUniqueColumn() == null) {
            for (final RestAttribute attr : this.getAttributes()) {
                if (attr.isUniqueId()) {
                    this.getThisSet().setUniqueColumn(attr.getName());
                    break;
                }
            }
        }
        return this.getThisSet().getUniqueColumn();
    }
    
    @Override
    public String getSelectClause() {
        return this.thisSet.getSelectClause();
    }
    
    @Override
    public RestResource setIndex(final int index) {
        this.index = index;
        return this;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public RestAttribute getNewAttributeInstance(final String name) {
        return new RestAttributeImpl(name, this);
    }
    
    protected void onResponseHandled() {
        this.setLoaded(true);
    }
    
    @Override
    public void handleResponse(final InputStream response) throws RestException {
        try {
            final Reader inputReader = new InputStreamReader(response, "utf-8");
            if (this.getMaximoRestConnector().getRestParams().isJSONFormat()) {
                this.parseJsonResponse(new JsonFactory().createParser(inputReader));
            }
            else {
                final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                final XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputReader);
                this.parseXMLResponse(xpp);
            }
        }
        catch (Exception ex) {
            throw new RestException("Errors while parsing the response", ex);
        }
    }
    
    @Override
    public void parseJsonResponse(final Object parser) throws RestException {
        try {
            final JsonParser jsonParser = (JsonParser)parser;
            for (JsonToken token = jsonParser.nextToken(); token != JsonToken.END_OBJECT; token = jsonParser.nextToken()) {
                if (token == JsonToken.FIELD_NAME) {
                    String fieldName = jsonParser.getCurrentName();
                    token = jsonParser.nextToken();
                    if (fieldName.equals("rowstamp")) {
                        this.setRowStamp(jsonParser.getValueAsString());
                    }
                    else if (fieldName.equals("hidden")) {
                        this.setHidden(jsonParser.getBooleanValue());
                    }
                    else if (fieldName.equals("readonly")) {
                        this.setReadOnly(jsonParser.getBooleanValue());
                    }
                    else if (fieldName.equals("Attributes")) {
                        final List<RestAttribute> newAttrList = new ArrayList<RestAttribute>();
                        if (token == JsonToken.START_OBJECT) {
                            while (token != JsonToken.END_OBJECT) {
                                token = jsonParser.nextToken();
                                if (token == JsonToken.FIELD_NAME) {
                                    final String attrName = jsonParser.getCurrentName();
                                    RestAttribute newAttr = this.getAttribute(attrName);
                                    if (newAttr == null) {
                                        newAttr = this.getNewAttributeInstance(attrName);
                                        newAttrList.add(newAttr);
                                    }
                                    newAttr.parseJsonResponse(parser);
                                    this.onAttributeFetched(newAttr);
                                }
                            }
                        }
                        this.setAttributes(newAttrList, false);
                    }
                    else if (fieldName.equals("RelatedMbos")) {
                        for (token = jsonParser.getCurrentToken(); token != JsonToken.END_OBJECT; token = jsonParser.nextToken()) {
                            if (token == JsonToken.FIELD_NAME) {
                                fieldName = jsonParser.getCurrentName();
                            }
                            else if (token == JsonToken.START_ARRAY) {
                                while (token != JsonToken.END_OBJECT && token != JsonToken.FIELD_NAME) {
                                    if (token == JsonToken.START_ARRAY) {
                                        final RestMboSet newSet = new RestMboSet(fieldName, this.getMaximoRestConnector());
                                        newSet.setOwner(this);
                                        newSet.setRelationship(fieldName);
                                        newSet.parseJsonResponse(parser);
                                        this.getRelatedSetList().put(fieldName, newSet);
                                    }
                                    else if (token == JsonToken.END_ARRAY) {
                                        break;
                                    }
                                    token = jsonParser.getCurrentToken();
                                }
                            }
                        }
                    }
                }
            }
            this.onResponseHandled();
        }
        catch (IOException ex) {
            throw new RestException(ex);
        }
    }
    
    @Override
    public void parseXMLResponse(final Object parser) throws RestException {
        try {
            final XmlPullParser xmlParser = (XmlPullParser)parser;
            int event = xmlParser.getEventType();
            while (event != 1) {
                event = xmlParser.getEventType();
                String tagName = xmlParser.getName();
                if (event == 2) {
                    final String requiredAttr = xmlParser.getAttributeValue((String)null, "required");
                    if (requiredAttr != null) {
                        RestAttribute newAttr = this.getAttribute(tagName);
                        if (newAttr == null) {
                            newAttr = this.getNewAttributeInstance(tagName);
                            this.addAttribute(newAttr);
                        }
                        newAttr.parseXMLResponse(xmlParser);
                        this.onAttributeFetched(newAttr);
                    }
                    else {
                        if (!tagName.equals(this.getTableName())) {
                            while (event != 3 || !tagName.equals(this.getTableName())) {
                                if (event == 2) {
                                    RestResourceSet relatedRSSet = this.getRelatedSet(tagName);
                                    if (relatedRSSet == null) {
                                        relatedRSSet = new RestMboSet(tagName, this.getMaximoRestConnector());
                                        ((RestMboSet)relatedRSSet).setOwner(this);
                                        ((RestMboSet)relatedRSSet).setRelationship(tagName);
                                        relatedRSSet.parseXMLResponse(xmlParser);
                                        this.getRelatedSetList().put(tagName, relatedRSSet);
                                    }
                                }
                                event = xmlParser.getEventType();
                                tagName = xmlParser.getName();
                            }
                            break;
                        }
                        final String hiddenAttr = xmlParser.getAttributeValue((String)null, "hidden");
                        final String readonlyAttr = xmlParser.getAttributeValue((String)null, "readonly");
                        if (hiddenAttr != null) {
                            this.setHidden(Integer.valueOf(hiddenAttr) == 1);
                        }
                        if (readonlyAttr != null) {
                            this.setReadOnly(Integer.valueOf(readonlyAttr) == 1);
                        }
                    }
                }
                if (event == 3 && tagName.equals(this.getTableName())) {
                    break;
                }
                xmlParser.next();
            }
            this.onResponseHandled();
        }
        catch (XmlPullParserException xppe) {
            throw new RestException((Throwable)xppe);
        }
        catch (IOException ioe) {
            throw new RestException(ioe);
        }
    }
    
    protected void onAttributeFetched(final RestAttribute attr) {
        if (attr.isUniqueId()) {
            if (this.getUniqueColumn() == null) {
                this.getThisSet().setUniqueColumn(attr.getName());
            }
            this.setUniqueID(attr.getString());
        }
        final RestAttributeMeta newAttrMeta = attr.getMeta();
        if (!this.getMeta().hasAttributeMeta(newAttrMeta)) {
            this.getMeta().getListAttributeMeta().add(newAttrMeta);
        }
    }
    
    @Override
    public void loadFromServer() throws RestException {
        if (!this.isLoaded()) {
            try {
                this.getMaximoRestConnector().getRestOptions().setHandlerContext(this.getHandlerContext());
                final String uri = this.getURI();
                if (uri == null || uri.length() <= 0) {
                    throw new RestException("The resource is invalid");
                }
                this.getMaximoRestConnector().get(uri, this);
            }
            catch (IOException ioe) {
                throw new RestException(ioe.getMessage(), ioe);
            }
        }
    }
    
    @Override
    public void reloadFromServer(final String... attributes) throws RestException {
        this.getMaximoRestConnector().getRestParams().put("_includecols", new RestQuerySelect().select(attributes));
        this.reloadFromServer();
    }
    
    @Override
    public void reloadFromServer() throws RestException {
        if (this.getURI() != null) {
            this.setLoaded(false);
            this.loadFromServer();
        }
    }
    
    @Override
    public RestResource setAttributes(final List<RestAttribute> newList, final boolean overWrite) {
        if ((overWrite && this.attributeList != null) || this.attributeList == null || this.attributeList.isEmpty()) {
            this.attributeList = newList;
        }
        else {
            for (int i = 0; i < newList.size(); ++i) {
                for (int j = 0; j < this.attributeList.size(); ++j) {
                    final RestAttribute newRestAttribute = newList.get(i);
                    if (this.attributeList.get(j).getMeta().equals(newRestAttribute.getMeta())) {
                        this.attributeList.set(j, newRestAttribute);
                        break;
                    }
                }
            }
        }
        return this;
    }
    
    @Override
    public List<RestAttribute> getAttributes() {
        if (this.attributeList == null) {
            this.attributeList = new ArrayList<RestAttribute>();
        }
        return this.attributeList;
    }
    
    @Override
    public RestAttribute getAttribute(final int index) {
        if (this.hasAttributes()) {
            return this.getAttributes().get(index);
        }
        return null;
    }
    
    @Override
    public RestAttribute getAttribute(final String name) {
        if (this.hasAttributes()) {
            for (final RestAttribute restAttribute : this.getAttributes()) {
                if (restAttribute.getName().equalsIgnoreCase(name)) {
                    return restAttribute;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean hasAttributes() {
        return this.getAttributes() != null && !this.getAttributes().isEmpty();
    }
    
    @Override
    public boolean containsAttribute(final String name) {
        return this.containsAttribute(new RestAttributeImpl(name));
    }
    
    @Override
    public boolean containsAttribute(final RestAttribute attr) {
        return this.hasAttributes() && this.getAttributes().contains(attr);
    }
    
    @Override
    public RestAttribute addAttribute(final String attributeName) throws RestException {
        final RestAttribute newAttr = this.getNewAttributeInstance(attributeName);
        this.addAttribute(newAttr);
        return newAttr;
    }
    
    @Override
    public RestResource addAttribute(final String attributeName, final Object value) throws RestException {
        final RestAttribute newAttr = this.getNewAttributeInstance(attributeName);
        newAttr.setValue(value);
        return this.addAttribute(newAttr);
    }
    
    @Override
    public RestResource addAttribute(final RestAttribute attr) throws RestException {
        if (!this.containsAttribute(attr)) {
            this.getAttributes().add(attr);
            return this;
        }
        throw new RestException("Already contains a RestAttribute named '" + attr.getName() + "'");
    }
    
    @Override
    public RestResource setLoaded(final boolean value) {
        this.isLoaded = value;
        return this;
    }
    
    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }
    
    @Override
    public RestResource setModified(final boolean value) {
        this.isModified = value;
        return this;
    }
    
    @Override
    public boolean isModified() {
        return this.isModified;
    }
    
    @Override
    public RestResource setToBeAdded(final boolean value) {
        this.toBeAdded = value;
        return this;
    }
    
    @Override
    public boolean toBeAdded() {
        return this.toBeAdded;
    }
    
    @Override
    public RestResource setToBeDeleted(final boolean value) {
        this.toBeDeleted = value;
        if (this.toBeAdded()) {
            this.setToBeAdded(false);
        }
        if (this.isModified()) {
            this.setModified(false);
        }
        return this;
    }
    
    @Override
    public boolean toBeDeleted() {
        return this.toBeDeleted;
    }
    
    @Override
    public boolean toBeSaved() {
        return (this.toBeAdded() && !this.toBeDeleted()) || this.isModified() || this.toBeDeleted();
    }
    
    @Override
    public boolean isNull(final String attribute) {
        return !this.containsAttribute(attribute) || this.getAttribute(attribute).isNull();
    }
    
    @Override
    public Object getValue(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getValue() : null;
    }
    
    @Override
    public String getString(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getString() : null;
    }
    
    @Override
    public Boolean getBoolean(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getBoolean() : null;
    }
    
    @Override
    public Integer getInt(final String attribute, final Locale locale) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getInt(locale) : null;
    }
    
    @Override
    public Integer getInt(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getInt() : null;
    }
    
    @Override
    public Long getLong(final String attribute, final Locale locale) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getLong(locale) : null;
    }
    
    @Override
    public Long getLong(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getLong() : null;
    }
    
    @Override
    public Float getFloat(final String attribute, final Locale locale) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getFloat(locale) : null;
    }
    
    @Override
    public Float getFloat(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getFloat() : null;
    }
    
    @Override
    public Double getDouble(final String attribute, final Locale locale) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getDouble(locale) : null;
    }
    
    @Override
    public Double getDouble(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getDouble() : null;
    }
    
    @Override
    public Date getDate(final String attribute) {
        return this.containsAttribute(attribute) ? this.getAttribute(attribute).getDate() : null;
    }
    
    @Override
    public byte[] getBytes(final String attribute) {
        return (byte[])(this.containsAttribute(attribute) ? this.getAttribute(attribute).getBytes() : null);
    }
    
    @Override
    public RestResource setValue(final String attributeName, final Object value) throws RestException {
        if (!this.isReadOnly()) {
            if (this.containsAttribute(attributeName)) {
                this.getAttribute(attributeName).setValue(value);
            }
            else {
                try {
                    this.addAttribute(attributeName).setValue(value);
                }
                catch (RestException ex) {}
            }
            this.setModified(true);
            return this;
        }
        throw new RestException("Object " + this.getName() + "(" + this.getUniqueColumn() + ": " + this.getUniqueID() + ") is readonly!");
    }
    
    @Override
    public RestResource setNull(final String atrributeName) throws RestException {
        this.setValue(atrributeName, null);
        return this;
    }
    
    @Override
    public void validateForServer() throws RestException {
        final String uri = this.getURI();
        if (uri == null || uri.isEmpty()) {
            throw new RestException("The resource is invalid");
        }
    }
    
    @Override
    public boolean canSaveToServer() {
        return true;
    }
    
    protected void putToServer() throws RestException, IOException {
        final RestParams params = this.getMaximoRestConnector().getRestParams();
        if (params.containsKey("#restquerywhere")) {
            params.remove("#restquerywhere");
        }
        final RestAttribute[] attributes = this.getAttributes().toArray(new RestAttribute[this.getAttributes().size()]);
        for (int i = 0; i < attributes.length; ++i) {
            final RestAttribute attr = attributes[i];
            if (attr.isChanged() && attr.canSaveToServer()) {
                attr.validateForServer();
                params.put(attr.getName(), attr.isNull() ? null : attr.getString());
            }
        }
        this.getMaximoRestConnector().put(this.getURI(), this);
        this.getMaximoRestConnector().resetToInitialParams();
    }
    
    protected void postToServer() throws IOException, RestException {
        String body = "";
        final RestAttribute[] attributes = this.getAttributes().toArray(new RestAttribute[this.getAttributes().size()]);
        for (int i = 0; i < attributes.length; ++i) {
            final RestAttribute attr = attributes[i];
            if (attr.canSaveToServer()) {
                attr.validateForServer();
                String value;
                if (attr.isDateTime()) {
                    value = RestResourceImpl.REST_DATE_REQUEST_FORMAT.format(attr.getDate());
                }
                else if (attr.isDecimal()) {
                    value = String.valueOf(attr.getDouble());
                }
                else if (attr.isBLOB()) {
                    value = Base64.encodeBase64String(attr.getBytes());
                }
                else {
                    value = attr.getString();
                }
                body = body + attr.getName() + "=" + URLEncoder.encode(value, "utf-8") + "&";
            }
        }
        this.getMaximoRestConnector().post(this.getThisSet().getURI(), body.substring(0, body.length() - 1), this);
    }
    
    protected void deleteOnServer() throws RestException, IOException {
        this.getMaximoRestConnector().resetToInitialParams();
        this.getMaximoRestConnector().delete(this.getURI());
        this.close();
    }
    
    @Override
    public void disconnect() throws RestException {
        try {
            this.getMaximoRestConnector().disconnect();
            this.getMaximoRestConnector().closeConnection();
        }
        catch (IOException ioe) {
            throw new RestException(ioe.getMessage(), ioe);
        }
    }
    
    @Override
    public void saveToServer() throws RestException {
        if (this.canSaveToServer()) {
            this.validateForServer();
            try {
                if (!this.toBeDeleted()) {
                    if (this.toBeAdded()) {
                        this.postToServer();
                    }
                    else if (this.isModified()) {
                        this.putToServer();
                    }
                    this.setToBeAdded(false);
                    this.setModified(false);
                }
                else {
                    this.deleteOnServer();
                }
                this.onSavedToServer();
            }
            catch (IOException ioe) {
                throw new RestException(ioe.getMessage(), ioe);
            }
        }
    }
    
    protected void onSavedToServer() {
    }
    
    protected void onAdded() {
    }
    
    @Override
    public RestResource discardChanges() {
        if (this.isModified()) {
            for (final RestAttribute attr : this.getAttributes()) {
                attr.rollbackToInitialValue();
            }
            this.setModified(false);
        }
        return this;
    }
    
    @Override
    public boolean isHidden() {
        return this.isHidden;
    }
    
    @Override
    public RestResource setHidden(final boolean value) {
        this.isHidden = value;
        return this;
    }
    
    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }
    
    @Override
    public RestResource setReadOnly(final boolean value) {
        this.isReadOnly = value;
        return this;
    }
    
    @Override
    public void close() {
        synchronized (this) {
            if (this.attributeList != null && !this.attributeList.isEmpty()) {
                for (final RestAttribute attr : this.getAttributes()) {
                    attr.close();
                }
                this.attributeList.clear();
            }
            if (this.relatedSets != null && !this.relatedSets.isEmpty()) {
                this.relatedSets.clear();
            }
            this.attributeList = null;
            this.thisSet = null;
            this.uniqueID = null;
            this.rowstamp = null;
        }
        this.onClosed();
    }
    
    protected void onClosed() {
    }
    
    @Override
    public RestResourceSet getRelatedSet(final String name) {
        return this.getRelatedSetList().get(name);
    }
    
    @Override
    public Map<String, RestResourceSet> getRelatedSetList() {
        if (this.relatedSets == null) {
            this.relatedSets = new HashMap<String, RestResourceSet>();
        }
        return this.relatedSets;
    }
    
    protected void clearRelatedSetList() {
        synchronized (this) {
            this.relatedSets.clear();
            this.relatedSets = null;
        }
    }
    
    @Override
    public String toString() {
        return this.getName() + " - " + this.getUniqueColumn() + ": " + this.getUniqueID() + (this.isHidden() ? ", hidden" : "") + (this.isReadOnly() ? ", readonly" : "");
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof RestResource && ((RestResource)o).getMeta().equals(this.getMeta());
    }
}
