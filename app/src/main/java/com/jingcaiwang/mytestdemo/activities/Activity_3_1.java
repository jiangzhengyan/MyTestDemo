package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jingcaiwang.mytestdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_3_1 extends AppCompatActivity {

    @Bind(R.id.lv)
    ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_1);
        ButterKnife.bind(this);
        mLv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 11;
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

                View inflate = View.inflate(Activity_3_1.this, R.layout.item_la, null);
                return inflate;
            }
        });
    }
}
