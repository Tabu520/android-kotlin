// 
// Decompiled by Procyon v0.5.36
// 

package com.avenue.maximo.restclient;

import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Date;

public class RestUtil
{
    public static String stringValue(final Object o) throws DatatypeConfigurationException, UnsupportedEncodingException {
        if (o instanceof Date) {
            final TimeZone timeZone = TimeZone.getDefault();
            final GregorianCalendar gc = (GregorianCalendar)Calendar.getInstance(timeZone);
            gc.setTime((Date)o);
            return "\"" + DatatypeFactory.newInstance().newXMLGregorianCalendar(gc).toXMLFormat() + "\"";
        }
        if (o instanceof Number) {
            return "" + o;
        }
        if (o instanceof Boolean) {
            return (o == null) ? "null" : o.toString();
        }
        return (String)o;
    }
    
    public static String urlEncode(final String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "utf-8");
    }
    
    public static String getStringFromInputStream(final InputStream is) throws IOException {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();
        br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    
    public static byte[] getBytesFromInputStream(final InputStream is) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final byte[] buffer = new byte[65535];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        final byte[] ret = os.toByteArray();
        os.close();
        return ret;
    }
    
    public static String encodeBase64(final String input) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(input.getBytes("utf-8")));
    }
    
    public static String decodeBase64(final String base64String) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(base64String));
    }
    
    public static String getBooleanAsString(final boolean value) {
        return value ? "true" : "false";
    }
    
    public static Integer getBooleanAsInteger(final boolean value) {
        return value ? 1 : 0;
    }
    
    public static MaximoRestConnector getMaximoRestConnector(final String host, final int port, final String username, final String password) {
        final MaximoRestConnector maximoRestConnector = new MaximoRestConnector(new RestOptions().setAuth("maxauth").setHost(host).setPort(port).setUser(username).setPassword(password));
        return maximoRestConnector;
    }
}
