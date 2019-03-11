package com.jingcaiwang.mytestdemo.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

public class Activity_7_1 extends AppCompatActivity {


    @Bind(R.id.ll_content)
    LinearLayout mLlContent;
    @Bind(R.id.btn)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_1);
        ButterKnife.bind(this);

    }

    private static final String TAG = "Activity_7_1";
    public void anim(View view) {


        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(mLlContent.getWidth(),  ScreenUtils.getScreenW() );


        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: "+value );
                
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlContent.getLayoutParams();
                layoutParams.width = value;
                mLlContent.setLayoutParams(layoutParams);
            }
        });
        valueAnimator1.setDuration(5000);
        valueAnimator1.start();
//        ObjectAnimator moveIn = ObjectAnimator.ofFloat(mLlContent, "scaleX", 1f, 3f);
//        //    ObjectAnimator moveIn1 = ObjectAnimator.ofFloat(tv_ani, "scaleY", 1f, 3f);
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(mLlContent, "rotation", 0f, 360f);
//        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(mLlContent, "alpha", 1f, 0f, 1f);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.play(rotate).with(fadeInOut).with(moveIn);
//        animSet.setDuration(2000);
//        animSet.start();
//
//        View layout = popup.getContentView();
//        TextView tvpppp = (TextView) layout.findViewById(R.id.pppp);
//        tvpppp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Activity_1_1.class);
//                MainActivity.this.startActivity(intent);
//                startActivityForResult(intent, 11);
//            }
//        });
//        popup.showAtLocation(mRootview, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        anim(null);
    }
}

