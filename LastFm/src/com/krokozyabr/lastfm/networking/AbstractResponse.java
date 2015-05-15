package com.krokozyabr.lastfm.networking;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;

import com.octo.android.robospice.exception.NetworkException;

public abstract class AbstractResponse {
    protected ErrorInfo errorInfo;

    protected AbstractResponse() {
        errorInfo = new ErrorInfo();
    }

    public void parseError(String body, int errorCode) throws NetworkException, JSONException {
        if (body != null)
            errorInfo.parse(body);
        if (errorCode == 401) {
            HttpResponseException httpResponseException = new HttpResponseException(errorCode, body);
            NetworkException networkException = new NetworkException(body, httpResponseException);
            throw networkException;
        }

    }

    abstract void parseSuccess(String body) throws Exception;

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
