package com.jingcaiwang.mytestdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jingcaiwang.mytestdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_6_1 extends AppCompatActivity {


    @Bind(R.id.btn)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_1);
        ButterKnife.bind(this);


    }


    @OnClick(R.id.btn)
    public void onViewClicked() {

        Intent intent = new Intent( );

        intent.setAction(Intent.ACTION_WEB_SEARCH);
      //  intent.addCategory(Intent.CATEGORY_APP_MAPS);

        startActivity(intent);

    }
}

