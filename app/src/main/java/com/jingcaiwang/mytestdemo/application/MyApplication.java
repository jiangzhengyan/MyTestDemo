package com.jingcaiwang.mytestdemo.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.jingcaiwang.mytestdemo.utils.MyLifecycleHandler;
import com.jingcaiwang.mytestdemo.utils.ScreenUtils;
import com.lidroid.mutils.MUtils;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.gen.RePluginHostConfig;

import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by jiang_yan on 2017/9/28.
 */

public class MyApplication extends RePluginApplication {


    private static final String TAG = "LiveApplication";
    public static MyApplication instance = null;
    public static OkHttpClient okHttpClient = null;
    private String path;


    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtils.initScreen(this);//初始化ScreenUtils
        instance = this;
        int pid = android.os.Process.myPid();
        path = MUtils.getMUtils().getPath(this);

        String processAppName = getAppName(pid);
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        if (processAppName == null || !processAppName.equalsIgnoreCase("com.jingkai.worker")) {
            //Toast.makeText(this,"--"+processAppName,Toast.LENGTH_LONG).show();
           // return;
        }

    }


    /**
     * 获取设备的 IMEI
     *
     * @return
     */
    public static String getIMEI() {
        try {
            String deviceId = ((TelephonyManager) instance.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            Log.d(TAG, "deviceId:" + deviceId);
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager packageManager = instance.getPackageManager();
        try {
            return packageManager.getPackageInfo(instance.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getVersionCode() {
        PackageManager packageManager = instance.getPackageManager();
        try {
            return packageManager.getPackageInfo(instance.getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static OkHttpClient getOkHttpClient() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient();
//      okHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(instance), CookiePolicy.ACCEPT_ALL));
        }
        return okHttpClient;
    }

    public String getPath() {
        return path;
    }

    /**
     * 获取用户账户的sessionKey
     *
     * @return
     */
    public static String getSessionKey() {
        SharedPreferences sp = getInstance().getSharedPreferences("user", MODE_PRIVATE);
        String session_key = sp.getString("session_key", "");
        if (!TextUtils.isEmpty(session_key)) {
            return session_key;
        }
        return "";
    }



    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List list = am.getRunningAppProcesses();
        if (list == null) return null;
        Iterator i = list.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c =
                            pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }
}
