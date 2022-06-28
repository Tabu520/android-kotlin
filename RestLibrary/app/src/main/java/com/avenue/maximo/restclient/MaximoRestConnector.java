// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class MaximoRestConnector implements RestConstants, Serializable, Cloneable, RestEntity {
    private RestOptions restOptions;
    private RestParams initialRestParams;
    private RestParams restParams;
    private HttpURLConnection webRequest;
    private boolean valid;
    private List<String> cookies;
    private String httpMethod;
    private int lastResponseCode;
    private int connectTimeout;
    private int readTimeout;

    public MaximoRestConnector() {
        this.valid = true;
        this.cookies = null;
        this.httpMethod = "GET";
        this.lastResponseCode = -1;
        this.connectTimeout = 15000;
        this.readTimeout = 60000;
    }

    public MaximoRestConnector(final RestOptions ops, final RestParams pars) {
        this();
        this.setRestOptions(ops);
        this.setRestParams(pars);
        this.initialRestParams = this.getRestParams().clone();
    }

    public MaximoRestConnector(final RestOptions restOptions) {
        this(restOptions, getDefaultRestParams());
    }

    public static RestParams getDefaultRestParams() {
        return (new RestParams()).put("_format", "json").put("_compact", false).put("_md", true).put("_urs", true).put("_locale", true).put("_tc", true);
    }

    public MaximoRestConnector setRestOptions(final RestOptions ops) {
        this.restOptions = ops;
        return this;
    }

    public MaximoRestConnector setRestParams(final RestParams pars) {
        this.restParams = pars;
        if (this.initialRestParams == null && this.restParams != null) {
            this.initialRestParams = this.restParams.clone();
        }
        return this;
    }

    public MaximoRestConnector setConnectTimeOut(final int timeOut) {
        this.connectTimeout = timeOut;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public MaximoRestConnector setReadTimeOut(final int timeOut) {
        this.readTimeout = timeOut;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public MaximoRestConnector resetToInitialParams() {
        this.setRestParams(this.getInitialRestParams());
        return this;
    }

    public MaximoRestConnector clearInitialParams() {
        this.initialRestParams = null;
        return this;
    }

    public RestParams getInitialRestParams() {
        return this.initialRestParams != null ? this.initialRestParams.clone() : null;
    }

    public RestOptions getRestOptions() {
        return this.restOptions;
    }

    public RestParams getRestParams() {
        return this.restParams;
    }

    public String getConnectorURI(final RestRequester requester) {
        this.getRestOptions().setHandlerContext(requester.getHandlerContext());
        return this.getRestOptions().getHandlerURI();
    }

    boolean isGET() {
        return this.httpMethod.equals("GET");
    }

    boolean isPOST() {
        return this.httpMethod.equals("POST");
    }

    boolean isPUT() {
        return this.httpMethod.equals("PUT");
    }

    boolean isDELETE() {
        return this.httpMethod.equals("DELETE");
    }

    public boolean isValid() {
        return this.valid;
    }

    public List<String> getCookies() {
        return this.cookies;
    }

    public void clearCookies() {
        this.cookies = null;
    }

    private void setLastResponseCode(final int code) {
        this.lastResponseCode = code;
    }

    public int getLastResponseCode() {
        return this.lastResponseCode;
    }

    private void openWebRequest(final String uri, final String method, final String body) throws IOException, RestException {
        this.validateForServer();
        try {
            this.createWebRequest(uri);
            this.setMethod(method);
            this.setCookiesForSession();
            if (body != null && body.length() > 0) {
                this.setRequestStream(body);
            }
            System.out.println(method.toUpperCase() + ": " + uri);
            this.setLastResponseCode(this.webRequest.getResponseCode());
            if (this.getLastResponseCode() >= 400) {
                RestException.throwExceptionFromResponseStream(this.getLastResponseCode(), this.webRequest.getErrorStream());
            }
            System.out.println("Response from connection: " + this.getLastResponseCode());
        } catch (SocketTimeoutException | ConnectException ex1) {
            RestException.throwTimeOutException();
        }
    }

    private InputStream doHttpRequest(final String uri, final String method, final String body) throws IOException, RestException {
        if (this.getCookies() == null) {
            this.connect();
            this.lastResponseCode = -1;
        }
        this.openWebRequest(uri, method, body);
        return this.webRequest.getInputStream();
    }

    public void closeConnection() {
        if (this.webRequest != null) {
            this.webRequest.disconnect();
            this.webRequest = null;
        }
    }

    public void connect() throws IOException, RestException {
        this.validateForServer();
        final String connectURI = this.getRestOptions().getRootApiURI();
        this.clearCookies();
        System.out.println("Authenticating with Maximo server at " + connectURI);
        this.createAuthRequest(connectURI);
        try {
            if (!this.getRestOptions().isFormAuth()) {
                this.setMethod("GET");
            }
            this.setLastResponseCode(this.webRequest.getResponseCode());
            final int responseCode = this.getLastResponseCode();
            if (responseCode == -1) {
                throw new RestException("Invalid Request");
            }
            if (responseCode < 400) {
                this.cookies = this.webRequest.getHeaderFields().get("Set-Cookie");
            }
            if (this.getCookies() == null) {
                InputStream inStream = this.webRequest.getErrorStream();
                if (inStream == null) {
                    inStream = this.webRequest.getInputStream();
                }
                RestException.throwExceptionFromResponseStream(this.getLastResponseCode(), inStream);
            }
        } catch (SocketTimeoutException | ConnectException ex1) {
            RestException.throwTimeOutException();
        } finally {
            this.closeConnection();
        }
    }

    public void disconnect() throws IOException {
        System.out.println("&&&&&&&&&&&&&&&&&&&& LOGOUT &&&&&&&&&&&&&&&&&&&&");
        final String logout = this.getRestOptions().getRootApiURI() + "/logout";
        final URL httpURL = new URL(logout);
        this.webRequest = (HttpURLConnection) httpURL.openConnection();
        try {
            this.webRequest.setRequestMethod("GET");
            this.setLastResponseCode(this.webRequest.getResponseCode());
        } finally {
            this.closeConnection();
        }
    }

    protected void createWebRequest(String uri) throws IOException, RestException {
        String publicHost = this.getRestOptions().getHost();
        final int publicPort = this.getRestOptions().getPort();
        if (publicPort != -1) {
            publicHost = publicHost + ":" + publicPort;
        }
        if (!uri.contains(publicHost)) {
            final URL tempURL = new URL(uri);
            String currentHost = tempURL.getHost();
            if (tempURL.getPort() != -1) {
                currentHost = currentHost + ":" + tempURL.getPort();
            }
            uri = uri.replace(currentHost, publicHost);
        }
        if (this.getRestParams() != null) {
            uri += this.getRestParams().construct();
        }
        (this.webRequest = (HttpURLConnection)new URL(uri).openConnection()).setConnectTimeout(this.getConnectTimeout());
        this.webRequest.setReadTimeout(this.getReadTimeout());
    }

    protected void createAuthRequest(final String uri) throws IOException, RestException {
        final RestOptions ops = this.getRestOptions();
        if (ops.getUser() != null && ops.getPassword() != null) {
            if (this.getRestOptions().isFormAuth()) {
                this.createWebRequest(uri + "/j_security_check");
                this.webRequest.setInstanceFollowRedirects(false);
                this.webRequest.setRequestMethod("POST");
                this.webRequest.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
                this.webRequest.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                this.webRequest.setRequestProperty("Connection", "keep-alive");
                this.webRequest.setRequestProperty("x-public-uri", ops.getHandlerURI());
                this.webRequest.setDoOutput(true);
                final StringBuilder content = new StringBuilder();
                content.append("j_username=").append(ops.getUser());
                content.append("&j_password=").append(ops.getPassword());
                final OutputStream outputStream = this.webRequest.getOutputStream();
                outputStream.write(content.toString().getBytes());
                outputStream.close();
            } else {
                this.createWebRequest(uri + "/login");
                final String encodedUserPwd = RestUtil.encodeBase64(ops.getUser() + ":" + ops.getPassword());
                if (ops.isBasicAuth()) {
                    this.webRequest.setRequestProperty("Authorization", "Basic " + encodedUserPwd);
                } else if (ops.isMaxAuth()) {
                    this.webRequest.setRequestProperty("maxauth", encodedUserPwd);
                }
            }
        }
    }

    protected void setMethod(final String method) throws IOException, RestException {
        this.httpMethod = method;
        final RestOptions opts = this.getRestOptions();
        if (this.isGET()) {
            this.webRequest.setRequestMethod("GET");
            this.webRequest.setRequestProperty("Accept", "application/json");
            this.webRequest.setUseCaches(false);
            this.webRequest.setAllowUserInteraction(false);
            this.webRequest.setRequestProperty("x-public-uri", opts.getHandlerURI());
        } else if (this.isPOST()) {
            this.webRequest.setRequestMethod("POST");
            this.webRequest.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            this.webRequest.setDoOutput(true);
            this.webRequest.setRequestProperty("x-public-uri", opts.getHandlerURI());
        } else if (this.isPUT()) {
            this.webRequest.setRequestMethod("PUT");
            this.webRequest.setRequestProperty("Type", "application/json");
            this.webRequest.setDoOutput(true);
            this.webRequest.setRequestProperty("x-public-uri", opts.getHandlerURI());
        } else if (this.isDELETE()) {
            this.webRequest.setRequestMethod("DELETE");
            this.webRequest.setRequestProperty("Accept", "application/json");
            this.webRequest.setUseCaches(false);
            this.webRequest.setAllowUserInteraction(false);
            this.webRequest.setRequestProperty("x-public-uri", opts.getHandlerURI());
        }
    }

    protected void setRequestStream(final String content) throws IOException {
        final OutputStreamWriter writer = new OutputStreamWriter(this.webRequest.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private void setCookiesForSession() {
        if (this.cookies != null) {
            Iterator var1 = this.cookies.iterator();

            while(var1.hasNext()) {
                String cookie = (String)var1.next();
                this.webRequest.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
    }

    public synchronized byte[] getFile(final String uri) throws IOException, RestException {
        this.openWebRequest(uri, "GET", null);
        return RestUtil.getBytesFromInputStream(this.webRequest.getInputStream());
    }

    public synchronized void get(final String uri, final RestRequester requester) throws IOException, RestException {
        requester.handleResponse(this.doHttpRequest(uri, "GET", null));
    }

    public synchronized void put(final String uri, final RestRequester requester) throws IOException, RestException {
        requester.handleResponse(this.doHttpRequest(uri, "PUT", null));
    }

    public synchronized void post(final String uri, final String body, final RestRequester requester) throws IOException, RestException {
        requester.handleResponse(this.doHttpRequest(uri, "POST", body));
    }

    public synchronized void delete(final String uri) throws IOException, RestException {
        this.doHttpRequest(uri, "DELETE", null);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void close() {
        this.restOptions = null;
        this.initialRestParams = null;
        this.restParams = null;
        this.valid = false;
    }

    @Override
    public boolean canSaveToServer() {
        return true;
    }

    @Override
    public void validateForServer() throws RestException {
        if (!this.isValid()) {
            throw new RestException("The instance of MaximoRestConnector is not valid.");
        }
    }
}
