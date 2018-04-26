package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.views.MDrawLineView;

public class Activity_1_3 extends AppCompatActivity {

    private MDrawLineView mDrawLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_3);
        mDrawLine = (MDrawLineView) findViewById(R.id.mDrawLine);
        Button clearBut = (Button) findViewById(R.id.clearBut);
        clearBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawLine.clear();
            }
        });
    }
}
