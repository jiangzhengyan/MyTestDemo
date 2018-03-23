package com.jingcaiwang.mytestdemo.activities;

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

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_10, R.id.btn_11, R.id.btn_12})
    public void allClick(View view) {

        switch (view.getId()) {
            case R.id.btn_1:
                iv_anim.setBackgroundResource(R.drawable.anim_1);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_2:
                iv_anim.setBackgroundResource(R.drawable.anim_2);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_3:
                iv_anim.setBackgroundResource(R.drawable.anim_3);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_4:
                iv_anim.setBackgroundResource(R.drawable.anim_4);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_5:
                iv_anim.setBackgroundResource(R.drawable.anim_5);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_6:
                iv_anim.setBackgroundResource(R.drawable.anim_6);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_7:
                iv_anim.setBackgroundResource(R.drawable.anim_7);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_8:
                iv_anim.setBackgroundResource(R.drawable.anim_8);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_9:
                iv_anim.setBackgroundResource(R.drawable.anim_9);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_10:
                iv_anim.setBackgroundResource(R.drawable.anim_10);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_11:
                iv_anim.setBackgroundResource(R.drawable.anim_11);
                animationDrawable = (AnimationDrawable) iv_anim.getBackground();

                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                break;
            case R.id.btn_12:
                iv_anim.setBackgroundResource(R.drawable.anim_12);
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
