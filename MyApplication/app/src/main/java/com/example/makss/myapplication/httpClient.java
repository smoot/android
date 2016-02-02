package com.example.makss.myapplication;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonStreamerEntity;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class httpClient {
    private String host;
    private String port;
    private AsyncHttpClient client;
    private String url;

    public httpClient(String host, int port) {
        this.client = new AsyncHttpClient();
        this.host = host;
        this.port = String.valueOf(port);
        this.url = "http://" + this.host + ":" + this.port;
        client.addHeader("Content-Type","application/json");
    }

    public httpClient() {
        this.client = new AsyncHttpClient();
        this.host = "192.168.56.1";
        this.port = "3000";
        this.url = "http://" + host + ":" + port + "/aaa";
        client.addHeader("Content-Type","application/json");
    }

    public void asyncGet() {
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private static final String LOG_TAG = "JsonStreamSample";

    public void asyncPost(){

        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        params.put("user", "android@tester.com");
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString());
            se.setContentType("application/json");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
