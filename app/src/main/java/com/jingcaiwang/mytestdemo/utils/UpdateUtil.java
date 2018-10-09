package com.jingcaiwang.mytestdemo.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.application.MyApplication;
import com.jingcaiwang.mytestdemo.beans.UpdateInfoBean;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
/**
 *  本类的主要功能是 :  更新
 *
 * @author  jiang_zheng_yan  2018/10/9 22:42
 *
 */
public class UpdateUtil implements DialogInterface.OnKeyListener {
    private Context mContext;
    private DonotUpdateListener donotUpdateListener;
    private static final String TAG = "UpdateUtil";
    private Dialog dialog_update;
    private Dialog dialog_warm;
    private Dialog dialog_updateing;

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK) {
            if (dialog_update == dialogInterface) {
                dialog_update.dismiss();
                ((Activity) mContext).finish();
            } else if (dialog_updateing == dialogInterface) {
                Toast.makeText(mContext, "正在下载无法退出", Toast.LENGTH_SHORT).show();

            }
            return true;
        } else {
            return false;
        }
    }

    public interface DonotUpdateListener {
          void donotUpdate();
    }

    public UpdateUtil(Context mContext, DonotUpdateListener donotUpdateListener) {
        this.mContext = mContext;
        this.donotUpdateListener = donotUpdateListener;
    }

    //    public static final String ACCOUNT_DIR = MyApplication.getInstance().getApplicationContext().getExternalCacheDir().getAbsolutePath();
//    public static final String APK_PATH = "apk_cache/";
    public static final String ACCOUNT_DIR = MyApplication.getInstance().getPath();
    public static final String APK_PATH = "apk_cache/";

    File f = new File(ACCOUNT_DIR + APK_PATH);
    File downloadFile = new File(f.getAbsoluteFile() + File.separator + "temp.apk");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void upDateApp(final UpdateInfoBean updateInfoBean) {
        //先清除缓存目录下的文件
        clearAPPCache();
        if (updateInfoBean != null) {
            if (!TextUtils.isEmpty(updateInfoBean.getServiceUrl())) {
                if (!f.exists()) {
                    f.mkdirs();
                }
                dialog_update = new Dialog(mContext, R.style.update_dialog);
                dialog_update.setOnKeyListener(this);
                View view = LayoutInflater.from(mContext).inflate(R.layout.updatedialog, null);
                dialog_update.setContentView(view);
                final TextView tv_update_content = (TextView) view.findViewById(R.id.tv_update_content);
                tv_update_content.setText(updateInfoBean.getUpdateNotification());
                Button btn_update = (Button) view.findViewById(R.id.btn_update);
                final ImageView iv_donotupdate = (ImageView) view.findViewById(R.id.iv_donotupdate);
                if (updateInfoBean.getIfUpdate() == false) {
                    iv_donotupdate.setVisibility(View.VISIBLE);
                } else {
                    iv_donotupdate.setVisibility(View.GONE);
                }
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!NetworkUtils.isWifiConnected(mContext)) {
                            dialog_warm = new Dialog(mContext, R.style.update_dialog);
                            View view_warming = LayoutInflater.from(mContext).inflate(R.layout.netwarmingdlg, null);
                            dialog_warm.setContentView(view_warming);
                            TextView tv_warm_cancle = (TextView) view_warming.findViewById(R.id.tv_warm_cancle);
                            TextView tv_warm_continue = (TextView) view_warming.findViewById(R.id.tv_warm_continue);
                            tv_warm_cancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_warm.dismiss();
                                }
                            });
                            tv_warm_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_warm.dismiss();
                                    gotoUpdate(updateInfoBean);
                                }
                            });
                            dialog_warm.show();
                        } else {
                            gotoUpdate(updateInfoBean);
                        }
                    }
                });
                iv_donotupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_update.dismiss();
                        donotUpdateListener.donotUpdate();
                    }
                });
                dialog_update.setCancelable(false);
                dialog_update.show();
            } else {
                donotUpdateListener.donotUpdate();
            }
        }
    }

    public void gotoUpdate(final UpdateInfoBean updateInfoBean) {
        final String url = updateInfoBean.getServiceUrl();
        dialog_update.dismiss();
        dialog_updateing = new Dialog(mContext, R.style.update_dialog);
        View view_update = LayoutInflater.from(mContext).inflate(R.layout.progressdlg, null);
        dialog_updateing.setContentView(view_update);
        final ProgressBar progressBar = (ProgressBar) view_update.findViewById(R.id.pb_update);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        final TextView tv_status = (TextView) view_update.findViewById(R.id.tv_status);
        final TextView tv_nowsize = (TextView) view_update.findViewById(R.id.tv_nowsize);
        final TextView tv_totalsize = (TextView) view_update.findViewById(R.id.tv_totalsize);
        dialog_updateing.setCanceledOnTouchOutside(false);
        dialog_updateing.setOnKeyListener(this);
        dialog_updateing.show();
        FileDownloader.getImpl().create(url)
                .setPath(downloadFile.getAbsolutePath(), false)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        tv_status.setText("连接中...");
                        tv_totalsize.setText(getPrintSize(totalBytes));
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        tv_status.setText("下载中...");
                        progressBar.setProgress((int) (((float) soFarBytes / totalBytes) * 100));
                        tv_nowsize.setText(getPrintSize(soFarBytes));
                        tv_totalsize.setText(getPrintSize(totalBytes));
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        tv_status.setText("下载完成");
                        progressBar.setProgress(100);
                        dialog_updateing.dismiss();
                        if (downloadFile.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(downloadFile),
                                    "application/vnd.android.package-archive");
                            mContext.startActivity(intent);
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        dialog_updateing.dismiss();
                        Toast.makeText(mContext, "更新出错", Toast.LENGTH_SHORT).show();
                        if (!updateInfoBean.getIfUpdate()) {
                            donotUpdateListener.donotUpdate();
                        } else {
                            System.exit(0);
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    public static String getPrintSize(int size) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        double size1 = Double.valueOf(size);
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size1 < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size1 = size1 / 1024;
        }

        if (size1 < 1024) {
            return df.format(size1) + "KB";
        } else {
            size1 = size1 / 1024;
        }


        if (size1 < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            Log.e("==》", size1 + "");
            return df.format(size1) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size1 = size1 * 100 / 1024;
            return String.valueOf((size1 / 100)) + "."
                    + String.valueOf((size1 % 100)) + "GB";
        }
    }

    public void clearAPPCache() {
        try {
            // 1、清除APP cache目录下的内容
            File appCacheDir = mContext.getCacheDir();
            delFolder(appCacheDir.getAbsolutePath());
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                //Environment.isExternalStorageRemovable()，通过此方法我们可以知道手机上的存储卡是手机自带的还是外边可插拔的SD卡
                // 2、清除APP外部cache目录下的内容
                File appExCacheDir = mContext.getExternalCacheDir();
                if (appExCacheDir != null) {
                    delFolder(appExCacheDir.getAbsolutePath());
                }
                // 3、清除APP在外部自定义的缓存目录
                if (f != null) {
                    delFolder(f.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
