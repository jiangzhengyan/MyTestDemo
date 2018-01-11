package com.jingcaiwang.mytestdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by jiang_yan on 2017/11/2.
 */

public class ScrollWebView extends WebView {

    private static final String TAG = "ScrollWebView";
    public ScrollWebView(Context context) {
        this(context, null);
        inits();

    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        inits();

    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits();
    }

    private void inits() {
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e(TAG, "onTouch:setOnTouchListener " );
//                 return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

    }


    double x;
    double y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                double x_move = event.getX();
                double y_move = event.getY();
                if (Math.pow(x_move-x,2)- Math.pow(y_move-y,2)>0){
                    requestDisallowInterceptTouchEvent(true);
                }else {
                    Log.e(TAG, "onTouchEvent: " );
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED,

                Integer.MAX_VALUE >> 2);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    // disable scroll on touch
//  this.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            return (event.getAction() == MotionEvent.ACTION_MOVE);
//        }
//    });



}
