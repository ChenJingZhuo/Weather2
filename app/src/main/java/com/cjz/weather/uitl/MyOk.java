package com.cjz.weather.uitl;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyOk {

    private static final OkHttpClient client = new OkHttpClient();

    public static void get(String address, Callback callback) {
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
