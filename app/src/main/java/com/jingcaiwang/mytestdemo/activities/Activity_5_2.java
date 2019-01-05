package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_5_2 extends AppCompatActivity {


    @Bind(R.id.grid)
    GridView mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_2);
        ButterKnife.bind(this);

        mGrid.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 79;
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
                TextView textView = new TextView(Activity_5_2.this);

                textView.setText(position+1+"");
                return  textView;
            }
        });
    }


}

