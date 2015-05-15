package com.krokozyabr.lastfm.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpStatus;

import com.octo.android.robospice.request.SpiceRequest;

public abstract class AbstractSpecRequest<T extends AbstractResponse> extends SpiceRequest<T> {

    protected static final int CONNECTION_TIMEOUT = 15000;
    protected static final String CACHE_HEADER = "Cache-Control";
    protected static final String CACHE_VALUE = "no-cache";
    protected static final String TYPE_GET = "GET";
    protected static final String ENCODING_UTF_8 = "utf-8";
    protected static final String ENCODING_KEY = "charset";
    protected static final String CONTENT_TYPE_KEY = "Content-Type";
    protected static final String CONTENT_TYPE_JSON = "application/json";
    protected Class<T> tClass;

    public AbstractSpecRequest(Class<T> clazz) {
        super(clazz);
        tClass = clazz;
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        HttpURLConnection connection = null;
        T res = null;
        connection = composeConnection();
        int code = 0;
        try {
        	code = connection.getResponseCode(); 
        } catch (IOException e) {
            // Will return 401, because now connection has the correct internal state.
        	code = connection.getResponseCode(); 
        }
        InputStream is;
        if (code < HttpStatus.SC_OK || code >= HttpStatus.SC_MULTIPLE_CHOICES) {
            is = connection.getErrorStream();
            String body = readBody(is);
            T response = tClass.newInstance();
            response.parseError(body, code);
            res = response;
        } else {
            is = connection.getInputStream();
            res = parse(is);
        }

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

    protected String readBody(InputStream is) throws IOException {
        String result = null;
        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } finally {
                is.close();
            }
        }
        return result;
    }
}

