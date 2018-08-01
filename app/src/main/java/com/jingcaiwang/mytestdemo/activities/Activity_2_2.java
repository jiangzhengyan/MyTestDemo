package com.jingcaiwang.mytestdemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.beans.BaseBean;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;

import java.util.HashMap;

import okhttp3.Request;

public class Activity_2_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_2);
    }

    private void getWorkerFunctions(String doorKey) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", "profile");

        params.put("pc","29e39ed274ea8e7a50f8a83abf1239faca843022");
        params.put("cmd","updatedaily");
        params.put("uid","updatedaily");
        OKHttpManager.postAsyn("http://pl.api.ledongli.cn/xq/io.ashx", params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
//                {"success":1,"errorMsg":"开门成功","data":{}}

                Toast.makeText(Activity_2_2.this, baseBean.getErrorMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Request request, Exception e, String msg) {
                Toast.makeText(Activity_2_2.this, msg, Toast.LENGTH_LONG).show();

            }
        }, null);
    }
}
