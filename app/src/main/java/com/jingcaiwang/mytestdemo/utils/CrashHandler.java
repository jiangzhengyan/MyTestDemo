package com.jingcaiwang.mytestdemo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.os.Process;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by jiang_yan on 2017/9/28.
 */


public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private String path = "";
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            Class var0 = CrashHandler.class;
            synchronized (CrashHandler.class) {
                instance = new CrashHandler();
            }
        }

        return instance;
    }

    public void setPath(String path) {
        this.path = path + "err" + File.separator;
        File file = new File(this.path);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    public void uncaughtException(Thread thread, Throwable ex) {
        (new Thread() {
            public void run() {
                Looper.prepare();
                AlertDialog.Builder dialog = new AlertDialog.Builder(CrashHandler.this.mContext, 3);
                dialog.setTitle("提示");
                dialog.setMessage("程序出现异常，需要重新启动");
                dialog.setPositiveButton("重启", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivitiesManager.getInstance().popAllActivity();
                        Intent intent = CrashHandler.this.mContext.getPackageManager().getLaunchIntentForPackage(CrashHandler.this.mContext.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        CrashHandler.this.mContext.startActivity(intent);
                        Process.killProcess(Process.myPid());

                    }
                });
                dialog.setCancelable(false);
                dialog.create().show();
                Looper.loop();
            }
        }).start();
        ex.printStackTrace();
        this.saveException(ex);
    }

    public void saveException(Throwable ex) {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.format("%02d", new Object[]{Integer.valueOf(c.get(Calendar.MONTH) + 1)});
        String date = String.format("%02d", new Object[]{Integer.valueOf(c.get(Calendar.DATE))});
        String hour = String.format("%02d", new Object[]{Integer.valueOf(c.get(Calendar.HOUR_OF_DAY))});
        String minute = String.format("%02d", new Object[]{Integer.valueOf(c.get(Calendar.MINUTE))});
        String second = String.format("%02d", new Object[]{Integer.valueOf(c.get(Calendar.SECOND))});
        this.saveSD(year + month + date + hour + minute + second, this.getStackTraceText(ex));
    }

    private void saveSD(String key, String value) {
        try {
            byte[] byteArray = value.getBytes("GBK");
            FileOutputStream fos = new FileOutputStream(this.path + key, false);
            DataOutputStream out = new DataOutputStream(fos);
            out.write(byteArray);
            out.close();
        } catch (IOException var6) {
            ;
        }

    }

    private String getStackTraceText(Throwable ex) {
        try {
            StringWriter e = new StringWriter();
            PrintWriter pw = new PrintWriter(e);
            ex.printStackTrace(pw);
            pw.close();
            return e.toString();
        } catch (Exception var4) {
            ex.printStackTrace();
            return "";
        }
    }

    public void init(Context context) {

        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);

    }
}
