package com.jingcaiwang.mytestdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.UserUtil;
import com.jingcaiwang.mytestdemo.views.passwordinput.OnPasswordInputFinish;
import com.jingcaiwang.mytestdemo.views.passwordinput.PopEnterPassword;

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

showPayKeyBoard();
    }


    public void showPayKeyBoard( ) {

        PopEnterPassword popEnterPassword = new PopEnterPassword(Activity_6_1.this);

        // 显示窗口
      //  popEnterPassword.showAtLocation(this.findViewById(R.id.layoutContent),
        popEnterPassword.showAtLocation(mBtn,



                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

        popEnterPassword.setOnPasswordInputFinish(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String password) {


            }
        });
    }
}

