package com.jingcaiwang.mytestdemo.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by jiang_zheng_yan on 2018/2/28.
 */

public class MyscrollView extends ScrollView {

    public MyscrollView(Context context) {
        super(context);
    }

    public MyscrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyscrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt, isAtTop(), isAtBottom());
        }
    }

    /**
     * 是否在顶部
     *
     * @return
     */
    public boolean isAtTop() {
        return getScrollY() <= 0;
    }

    /**
     * 是否在底部
     *
     * @return
     */
    public boolean isAtBottom() {
        return getScrollY() == getChildAt(getChildCount() - 1).getBottom() + getPaddingBottom() - getHeight();
    }

    private static final String TAG = "MyscrollView";


    private OnMyScrollChangedListener onScrollChangedListener;

    public void setOnScrollChangedListener(OnMyScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnMyScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt, boolean isAtTop, boolean isAtBottom);
    }


    public interface OnScrollStateChangeListener {

        void onScrollChanged(ScrollType scrollType);

    }

    private Handler mHandler;
    private OnScrollStateChangeListener scrollViewListener;

    /**
     * 滚动状态   IDLE 滚动停止  TOUCH_SCROLL 手指拖动滚动         FLING滚动
     */
    public enum ScrollType {
        IDLE, TOUCH_SCROLL, FLING
    }

    /**
     * 记录当前滚动的距离
     */
    private int currentY = -9999999;
    /**
     * 当前滚动状态
     */
    private ScrollType scrollType = ScrollType.IDLE;
    /**
     * 滚动监听间隔
     */
    private int scrollDealy = 50;
    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            if (getScrollY() == currentY) {
                //滚动停止  取消监听线程
                scrollType = ScrollType.IDLE;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollChanged(scrollType);
                }
                mHandler.removeCallbacks(this);
                return;
            } else {
                //手指离开屏幕    view还在滚动的时候
                Log.d("", "Fling。。。。。");
                scrollType = ScrollType.FLING;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollChanged(scrollType);
                }
            }
            currentY = getScrollY();
            mHandler.postDelayed(this, scrollDealy);
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.scrollType = ScrollType.TOUCH_SCROLL;
                scrollViewListener.onScrollChanged(scrollType);
                //手指在上面移动的时候   取消滚动监听线程
                mHandler.removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                //手指移动的时候
                mHandler.post(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 必须先调用这个方法设置Handler  不然会出错
     *
     * @param handler
     * @return void
     */
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    /**
     * 设置滚动监听
     * @param listener
     * @return void
     */
    public void setOnScrollStateChangedListener(OnScrollStateChangeListener listener, Handler handler) {
        this.scrollViewListener = listener;
        this.mHandler = handler;
    }

}
