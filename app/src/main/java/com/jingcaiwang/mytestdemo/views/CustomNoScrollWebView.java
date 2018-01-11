package com.jingcaiwang.mytestdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomNoScrollWebView extends WebView {

	private boolean enable = false;

    public CustomNoScrollWebView(Context context) {
        super(context);
    }

    public CustomNoScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomNoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		return !isNoScrollEnable() && super.onInterceptTouchEvent(ev);
	}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
		if(!isNoScrollEnable()){
			return super.onTouchEvent(ev);
		}
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                return super.onTouchEvent(ev);
            case MotionEvent.ACTION_CANCEL:
                return super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

	public boolean isNoScrollEnable() {
		return enable;
	}

	public void setNoScrollEnable(boolean open) {
		enable = open;
	}
}
