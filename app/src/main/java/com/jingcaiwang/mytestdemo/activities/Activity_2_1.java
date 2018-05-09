package com.jingcaiwang.mytestdemo.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jingcaiwang.mytestdemo.R;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_2_1 extends AppCompatActivity {
    private static final String TAG = "Activity_2_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_1);
        File file = new File("/sdcard/Android/data/com.risenb.jingkai/record1.raw");
        Log.e(TAG, "onCreate: ");
        startBufferedWrite(file);

//        getEnvironmentDirectories();
//        getApplicationDirectories(this);
    }

    /**
     * 写入到raw文件
     * new AudioRecorder2Mp3Util(null, "/sdcard/Android/data/com.risenb.jingkai/record1.raw",
     * "/sdcard/Android/data/com.risenb.jingkai/record1.mp3");
     *
     * @param file
     */
    private void startBufferedWrite(final File file)  {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        DataOutputStream output = null;
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    fileOutputStream);
            output = new DataOutputStream(bufferedOutputStream);
//                    while (true) {
//                        int readSize = 11;
//                        for (int i = 0; i < readSize; i++) {
//                            output.writeShort(12);
//                        }
//
//                    }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // }
//        }
// ).start();
    }

    public static void getEnvironmentDirectories() {
        //:/system
        String rootDir = Environment.getRootDirectory().toString();
        System.out.println("Environment.getRootDirectory()=:" + rootDir);

        //:/data 用户数据目录
        String dataDir = Environment.getDataDirectory().toString();
        System.out.println("Environment.getDataDirectory()=:" + dataDir);

        //:/cache 下载缓存内容目录
        String cacheDir = Environment.getDownloadCacheDirectory().toString();
        System.out.println("Environment.getDownloadCacheDirectory()=:" + cacheDir);

        //:/mnt/sdcard或者/storage/emulated/0或者/storage/sdcard0 主要的外部存储目录
//<span style="color:#ff0000;">这个不一定是外部存储
        String storageDir = Environment.getExternalStorageDirectory().toString();
        System.out.println("Environment.getExternalStorageDirectory()=:" + storageDir);

        //:/mnt/sdcard/Pictures或者/storage/emulated/0/Pictures或者/storage/sdcard0/Pictures
        String publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        System.out.println("Environment.getExternalStoragePublicDirectory()=:" + publicDir);

        //获取SD卡是否存在:mounted
        String storageState = Environment.getExternalStorageState().toLowerCase();
        System.out.println("Environment.getExternalStorageState()=:" + storageState);

        //设备的外存是否是用内存模拟的，是则返回true。(API Level 11)
        boolean isEmulated = Environment.isExternalStorageEmulated();
        System.out.println("Environment.isExternalStorageEmulated()=:" + isEmulated);

        //设备的外存是否是可以拆卸的，比如SD卡，是则返回true。(API Level 9)
        boolean isRemovable = Environment.isExternalStorageRemovable();
        System.out.println("Environment.isExternalStorageRemovable()=</span>:" + isRemovable);
    }

    public static void getApplicationDirectories(Context context) {

        //获取当前程序路径 应用在内存上的目录 :/data/data/com.mufeng.toolproject/files
        String filesDir = context.getFilesDir().toString();
        System.out.println("context.getFilesDir()=:" + filesDir);

        //应用的在内存上的缓存目录 :/data/data/com.mufeng.toolproject/cache
        String cacheDir = context.getCacheDir().toString();
        System.out.println("context.getCacheDir()=:" + cacheDir);

        //应用在外部存储上的目录 :/storage/emulated/0/Android/data/com.mufeng.toolproject/files/Movies
        String externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString();
        System.out.println("context.getExternalFilesDir()=:" + externalFilesDir);

        //应用的在外部存储上的缓存目录 :/storage/emulated/0/Android/data/com.mufeng.toolproject/cache
        String externalCacheDir = context.getExternalCacheDir().toString();
        System.out.println("context.getExternalCacheDir()=:" + externalCacheDir);

        //获取该程序的安装包路径 :/data/app/com.mufeng.toolproject-3.apk
        String packageResourcePath = context.getPackageResourcePath();
        System.out.println("context.getPackageResourcePath()=:" + packageResourcePath);

        //获取程序默认数据库路径 :/data/data/com.mufeng.toolproject/databases/mufeng
        String databasePat = context.getDatabasePath("mufeng").toString();
        System.out.println("context.getDatabasePath(\"mufeng\")=:" + databasePat);


    }
}
