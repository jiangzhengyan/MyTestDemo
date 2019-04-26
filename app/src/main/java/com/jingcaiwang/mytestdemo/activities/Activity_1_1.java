package com.jingcaiwang.mytestdemo.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jingcaiwang.mytestdemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Activity_1_1 extends AppCompatActivity {

    private ImageView iv_anim;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_1);
        ButterKnife.bind(this);

        anim();
    }

    private void anim() {


        iv_anim = (ImageView) findViewById(R.id.iv_anim);

    }

    @OnClick({   R.id.btn_12})
    public void allClick(View view) {

        switch (view.getId()) {

            case R.id.btn_12:
                iv_anim.setBackgroundResource(R.drawable.anim_1demo);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;

        }
    }
}
