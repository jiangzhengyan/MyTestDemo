package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_4_3 extends AppCompatActivity {


    @Bind(R.id.lv)
    ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_3);
        ButterKnife.bind(this);


        mLv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 119;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView textView = new TextView(Activity_4_3.this);


                textView.setText("是是是是是是是是是是是是是试试是是是是是是是是是是是是是试试事实上事实上所所所所是是是是是是是是是是是是是试试是是是是是是是是是是是是是试试事实上事实上所所所所");
                return textView;
            }
        });
    }


}

