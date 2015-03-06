package com.wenwen.chatuidemo.utils;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpClientRequest {

    private static final String BASE_URL = Urls.BASIC_URL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, int timeout,
            AsyncHttpResponseHandler responseHandler) {
        if (timeout > 0)
            client.setTimeout(timeout);
        if (url.equals(""))
            url = BASE_URL;
        client.get(url, params, responseHandler);
    }

    // data byte[]
    public static void post(Context con, String url, HttpEntity entity,
            int timeout, AsyncHttpResponseHandler responseHandler) {
        if (timeout > 0)
            client.setTimeout(timeout);
        if (url.equals(""))
            url = BASE_URL;
        client.post(con, url, entity, "application/octet-stream",
                responseHandler);
    }

    // ---------------------------------

    public static void post(String url, RequestParams params, int timeout,
            AsyncHttpResponseHandler responseHandler) {
        if (timeout > 0) client.setTimeout(timeout);
        client.post(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, int timeout,
            JsonHttpResponseHandler responseHandler) {
        if (timeout > 0)
            client.setTimeout(timeout);
        if (url.equals(""))
            url = BASE_URL;
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
