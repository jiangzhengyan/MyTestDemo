package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jingcaiwang.mytestdemo.R;

import butterknife.ButterKnife;

public class Activity_7_2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_2);
        ButterKnife.bind(this);


    }

}

