package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.MyRecordVoicePlayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_3_3 extends AppCompatActivity {


    private MyRecordVoicePlayUtil instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_3);
        ButterKnife.bind(this);
        instance = MyRecordVoicePlayUtil.getInstance();
        instance.initVoiceSetting(Activity_3_3.this, Activity_3_3.this, "http://192.168.14.27:8080/2.mp3");

    }

    private static final String TAG = "Activity_3_3";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.onDestroy();
    }


}

