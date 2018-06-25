package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.beans.BaseBean;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class Activity_2_1 extends AppCompatActivity {
    private static final String TAG = "Activity_2_1";
    @Bind(R.id.left_door)
    Button mLeftDoor;
    @Bind(R.id.right_door)
    Button mRightDoor;
    @Bind(R.id.big_door)
    Button mBigDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_1);
        ButterKnife.bind(this);

    }


    private void getWorkerFunctions(String doorKey) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("c", "293c398e456dc2174667dc3e02f9daf7");
        params.put("parkId", 24 + "");
        params.put("doorKey",doorKey);
        OKHttpManager.postAsyn("http://testjcgj.jingcaiwang.cn:8881/doormanager/getDoorInfoByQRCode.do", params, new OKHttpManager.ResultCallback<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
//                {"success":1,"errorMsg":"开门成功","data":{}}
                Log.e(TAG, "onResponse: " + baseBean);
                Toast.makeText(Activity_2_1.this, baseBean.getErrorMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Request request, Exception e, String msg) {
                Toast.makeText(Activity_2_1.this, msg, Toast.LENGTH_LONG).show();

            }
        }, null);
    }

    @OnClick({R.id.left_door, R.id.right_door, R.id.big_door})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_door:
                getWorkerFunctions("1:1");
                break;
            case R.id.right_door:
                getWorkerFunctions("1:3");
                break;
            case R.id.big_door:
                getWorkerFunctions("1:4");
                break;
        }
    }
}
