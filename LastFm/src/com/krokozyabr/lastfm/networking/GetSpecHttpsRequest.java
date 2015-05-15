package com.krokozyabr.lastfm.networking;


import java.net.HttpURLConnection;
import java.net.URL;

public abstract class GetSpecHttpsRequest<T extends AbstractResponse> extends AbstractSpecRequest<T> {
    public GetSpecHttpsRequest(Class<T> clazz) {
        super(clazz);
    }
    @Override
    HttpURLConnection composeConnection() throws Exception {
        URL url = new URL(composeUrl());
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
        httpConn.setRequestMethod(TYPE_GET);
        httpConn.addRequestProperty(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        httpConn.addRequestProperty(ENCODING_KEY, ENCODING_UTF_8);
        return httpConn;
    }

    abstract String composeUrl();
}
