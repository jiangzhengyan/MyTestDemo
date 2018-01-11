package com.jingcaiwang.mytestdemo.network;


import okhttp3.Request;

public class CustomResultCallback<T> extends OKHttpManager.ResultCallback<T> {

    @Override
    public void onResponse(T response) {

    }

    @Override
    public void onAfter() {
        super.onAfter();
    }

    @Override
    public void onError(Request request, Exception e, String msg) {
        super.onBefore(request);

    }
}
