package com.jingcaiwang.mytestdemo.pop;

/**
 * Created by jiang_yan on 2017/9/30.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


@SuppressLint("NewApi")
public abstract class CommentPopUtils implements OnDismissListener
{
    private View v;
    protected PopupWindow popupWindow;
    protected OnClickListener onClickListener;
    protected OnItemClickListener itemClickListener;
    protected OnDismissListener onDismissListener;
    private Context context;

    public CommentPopUtils(View v, Context context, int layout)
    {
        super();
        this.v = v;
        View view = LayoutInflater.from(context).inflate(layout, null);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                popupWindow.dismiss();
                if (onDismissListener != null)
                {
                    onDismissListener.onDismiss();
                }
            }
        });
        initLayout(view, context);


    }

    public CommentPopUtils(Context context, int layout)
    {
        super();
        this.context=context;
        View view = LayoutInflater.from(context).inflate(layout, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                popupWindow.dismiss();
                if (onDismissListener != null)
                {
                    onDismissListener.onDismiss();
                }
            }
        });
        initLayout(view, context);


    }

    public void setOnDismissListener(OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
    }

    public abstract void initLayout(View v, Context context);

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     *

     */
    @SuppressWarnings("deprecation")
    public void showAsDropDown(View v)
    {
        // 这个是为了点击“返回Back”也能使其消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 使其聚集
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        //兼容api24
        if (v != null && Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            int h = v.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            popupWindow.setHeight(h);
        }
        // 设置弹出位置
        popupWindow.showAsDropDown(v);
        // 刷新状态
        popupWindow.update();

    }

    @SuppressWarnings("deprecation")
    public void showPopUp(View v)
    {

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.TOP, 15, location[1] - popupWindow.getHeight());
        popupWindow.update();
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     *
     */
    @SuppressWarnings("deprecation")
    public void showAsDropDownInstance()
    {
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 使其聚集
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        // 设置弹出位置
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        // 刷新状态
        popupWindow.update();

    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     *
     */
    @SuppressWarnings("deprecation")
    public void showAtLocation()
    {
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置弹出位置
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        // 刷新状态
        popupWindow.update();

    }

    /**
     * 隐藏菜单
     */
    public void dismiss()
    {
        popupWindow.dismiss();
        // Log.e("wsssssssss");
        // popupWindow.setOnDismissListener(new OnDismissListener()
        // {
        //
        // @Override
        // public void onDismiss()
        // {
        // popupWindow.dismiss();
        // Log.e("wsssssssss");
        //
        // }
        // });
    }

    public void setItemClickListener(OnItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public void onDismiss() {

    }

    /**
     * 适配android7.0 8.0
     * @param anchor
     * @param xoff
     * @param yoff
     */
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popupWindow.setHeight(height);
            popupWindow.showAsDropDown(anchor, xoff, yoff);
        } else {
            popupWindow.showAsDropDown(anchor, xoff, yoff);
        }
    }

    /**
     * 展示在屏幕中间
     */
    public void showAtCenter(){
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 使其聚集
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸

        popupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popupWindow.update();



    }
}
