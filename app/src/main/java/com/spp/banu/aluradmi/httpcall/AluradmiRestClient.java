package com.spp.banu.aluradmi.httpcall;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by banu on 20/02/17.
 */

public class AluradmiRestClient {
    private static final String BASE_URL = "http://aluradmi.pe.hu/data/";
    private static SyncHttpClient client = new SyncHttpClient();

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static AsyncHttpClient client_async = new AsyncHttpClient();

    public static void get_Async(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client_async.get(getAbsoluteUrl(url), params, responseHandler);
    }
}
