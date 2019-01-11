package com.jingcaiwang.mytestdemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.application.MyApplication;


public class ToastUtil {
    private static Toast toast;
    private static View toastView;

    public static void toastShortCenter(Context context,String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastLongCenter(Context context,String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    public static void toastView(String message){
//        if (TextUtils.isEmpty(message))return;
//        if (toastView==null){
//            toastView = LayoutInflater.from(MyApplication.getInstance().getApplicationContext()).inflate(R.layout.view_warn_toast,null);
//        }
//        TextView textView=toastView.findViewById(R.id.tv_toast_warn_message);
//        textView.setText(message);
//        Toast toast=new Toast(MyApplication.getInstance().getApplicationContext());
//        toast.setView(toastView);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();
//    }
}
