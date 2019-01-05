package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.pop.PopSelectDate;
import com.jingcaiwang.mytestdemo.utils.CornerImageView;
import com.jingcaiwang.mytestdemo.utils.UserUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_4_2 extends AppCompatActivity {


    @Bind(R.id.tv)
    TextView mTv;
    @Bind(R.id.corneriv)
    CornerImageView mCorneriv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_2);
        ButterKnife.bind(this);

    }

    private void pop() {

        final PopSelectDate popSelectItem = new PopSelectDate(mTv, this, R.layout.pop_select_date);


        popSelectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_sure:
                        String yearSelect = popSelectItem.getYearSelect();
                        String mmSelect = popSelectItem.getMMSelect();
                        String ddSelect = popSelectItem.getddSelect();
                        long selectTimeMillis = UserUtil.getDateTimeMillis(yearSelect + mmSelect + ddSelect, "yyyy年MM月dd日");
                        long currentTimeMillis = UserUtil.getDateTimeMillis(UserUtil.getCurrentDateStr("yyyy年MM月dd日"), "yyyy年MM月dd日");

                        if (selectTimeMillis > currentTimeMillis) {
                            Toast.makeText(Activity_4_2.this, "请选择当前或以前的日期", Toast.LENGTH_SHORT).show();

                            break;
                        }
                        String formatDate = UserUtil.formatDate(selectTimeMillis, "yyyy-MM-dd");
                        Toast.makeText(Activity_4_2.this, "formatDate" + formatDate, Toast.LENGTH_SHORT).show();

                        popSelectItem.dismiss();
                        break;

                }
            }
        });
        popSelectItem.showAtLocation();

    }


    @OnClick(R.id.tv)
    public void onViewClicked() {

        pop();
    }
}

