package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jingcaiwang.mytestdemo.R;

import butterknife.ButterKnife;

public class Activity_8_1 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_1);
        ButterKnife.bind(this);


    }

}

