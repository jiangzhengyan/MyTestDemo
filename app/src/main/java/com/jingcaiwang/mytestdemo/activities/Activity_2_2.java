package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.views.MyDrawBoard;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_2_2 extends AppCompatActivity {

    @Bind(R.id.my_draw_board)
    MyDrawBoard mMyDrawBoard;
    @Bind(R.id.btn_clean)
    Button mBtnClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_clean)
    public void onViewClicked() {

        mMyDrawBoard.clean();
    }
}
