package com.krokozyabr.lastfm.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;

import com.octo.android.robospice.request.SpiceRequest;

public abstract class AbstractRequest<T> extends SpiceRequest<T> {

    protected static final int CONNECTION_TIMEOUT = 15000;
    protected static final String CACHE_HEADER = "Cache-Control";
    protected static final String CACHE_VALUE = "no-cache";
    protected static final String TYPE_GET = "GET";
    protected static final String ENCODING_UTF_8 = "utf-8";
    protected static final String ENCODING_KEY = "charset";
    protected static final String CONTENT_TYPE_KEY = "Content-Type";
    protected static final String CONTENT_TYPE_JSON = "application/json";

    public AbstractRequest(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        preSteps();
        HttpURLConnection connection = null;
        T res = null;
        connection = composeConnection();
        int code = connection.getResponseCode();
        InputStream is;
        if (code < HttpStatus.SC_OK && code >= HttpStatus.SC_MULTIPLE_CHOICES) {
            is = connection.getErrorStream();
        } else { // TODO: check for success codes
            is = connection.getInputStream();
        }
        res = parse(is);

        if (connection != null) {
            connection.disconnect();
        }
        postSteps(res);
        return res;
    }

    abstract HttpURLConnection composeConnection() throws Exception;

    abstract T parse(InputStream is) throws Exception;

    /**
     * Defines steps after successful network request/parsing
     *
     * @param res - parsed message
     */
    protected void postSteps(T res) {
        // Empty by default
    }

    /**
     * Defines steps before network request
     *
     */
    protected void preSteps() throws Exception {
        // Empty by default
    }

    protected String readBody(InputStream is) throws IOException {
        String result = null;
        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                result = sb.toString();
            } finally {
                is.close();
            }
        }
        return result;
    }
    protected String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
