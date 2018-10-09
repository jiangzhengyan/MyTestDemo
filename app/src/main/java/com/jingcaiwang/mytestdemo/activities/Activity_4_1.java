package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.BuildConfig;
import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.beans.UpdateInfoBean;
import com.jingcaiwang.mytestdemo.utils.UpdateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_4_1 extends AppCompatActivity  implements UpdateUtil.DonotUpdateListener{

    private UpdateUtil updateUtil;

    @Bind(R.id.tv_update)
    TextView mTvUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_1);
        ButterKnife.bind(this);

        updateUtil=new UpdateUtil(this,this);
    }


    @OnClick(R.id.tv_update)
    public void onViewClicked() {
        int versionCodeNow= BuildConfig.VERSION_CODE;
        UpdateInfoBean updateInfoBean = new UpdateInfoBean();
        updateInfoBean.setIfUpdate(true);
        updateInfoBean.setServiceUrl("http://www");
        updateInfoBean.setUpdateNotification("1212");
        updateInfoBean.setVersion(12);
        updateUtil.upDateApp(updateInfoBean);

    }

    /**
     *  不更新的
     */
    @Override
    public void donotUpdate() {


    }
}

