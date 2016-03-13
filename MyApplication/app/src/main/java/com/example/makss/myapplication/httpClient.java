package com.example.makss.myapplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.entity.StringEntity;

public class httpClient {
    private String host, port, url;
    private AsyncHttpClient client;

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
//        this.host = "109.60.147.185";
        this.port = "3000";
        this.url = "http://" + host + ":" + port;
        client.addHeader("Content-Type", "application/json");
        client.addHeader("token", "10A8D41FCA841705D8EFF4669578E512502");

    }

    public void asyncPost(String path, RequestParams params, AsyncHttpResponseHandler handler){
        params.setUseJsonStreamer(true);
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString());
            se.setContentType("application/json");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        client.post(url + path, params, handler);

    }

    public void asyncGet(String path, RequestParams params, AsyncHttpResponseHandler handler){
        if (params!=null) {
            params.setUseJsonStreamer(true);
            StringEntity se = null;
            try {
                se = new StringEntity(params.toString());
                se.setContentType("application/json");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        client.get(url + path, params, handler);
    }
}
