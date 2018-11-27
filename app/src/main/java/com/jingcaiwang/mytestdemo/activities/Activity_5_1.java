package com.jingcaiwang.mytestdemo.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.application.MyApplication;
import com.jingcaiwang.mytestdemo.beans.UpdateInfoBean;
import com.jingcaiwang.mytestdemo.network.CustomResultCallback;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;
import com.jingcaiwang.mytestdemo.utils.DownloadUtil;
import com.jingcaiwang.mytestdemo.utils.NetworkUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class Activity_5_1 extends AppCompatActivity implements DialogInterface.OnKeyListener {


    @Bind(R.id.btn_down)
    Button mBtnDown;
    private Context mContext=this;
    private static final String TAG = "UpdateUtil";
    private static   String mFileurl = "http://supervision.test.admin.jingcaiwang.cn/triple/erp/getFile?pk_jkbx=1007A81000000003IAGC&fileName=设备卡片 - 副本.xls&type=1";
    private Dialog dialog_update;
    private Dialog dialog_warn;
    private Dialog dialog_updateing;
    //    public static final String ACCOUNT_DIR = MyApplication.getInstance().getApplicationContext().getExternalCacheDir().getAbsolutePath();
//    public static final String APK_PATH = "apk_cache/";
    public static final String ACCOUNT_DIR = MyApplication.getInstance().getPath();
    public static final String APK_PATH = "apk_cache/";

    File f = new File(ACCOUNT_DIR + APK_PATH);
    File downloadFile = new File(f.getAbsoluteFile() + File.separator + "temp.xls");

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK) {
            if (dialog_update == dialogInterface) {
                dialog_update.dismiss();
                // ((Activity) mContext).finish();
            } else if (dialog_updateing == dialogInterface) {
                Toast.makeText(mContext, "正在下载无法退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_1);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_down)
    public void onViewClicked() {

        upDateApp();
        ok();
        downFile(mFileurl);

    }

    private void ok() {
        OKHttpManager.getDownloadDelegate().downloadAsyn(mFileurl,downloadFile.getAbsolutePath(),new CustomResultCallback<Object>(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e(TAG, "onBefore: "  );
            }

            @Override
            public void onError(Request request, Exception e, String msg) {
                super.onError(request, e, msg);
                Log.e(TAG, "onError: "  );
            }

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);
                Log.e(TAG, "onResponse: "  );
            }

            @Override
            public void onAfter() {
                super.onAfter();
                Log.e(TAG, "onAfter: "  );
            }
        });
    }
    /**
     * 文件下载
     *
     * @param url
     */
    public void downFile(String url) {

        ProgressDialog  progressDialog = new ProgressDialog(Activity_5_1.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        DownloadUtil.get().download(toUtf8String(url), f.getAbsolutePath(), "kuoke.xls", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //下载完成进行相关逻辑操作
                Log.e(TAG, "onDownloadSuccess: "+file.getAbsolutePath() );


            }

            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
                Log.e(TAG, "onDownloading: "+progress );

            }

            @Override
            public void onDownloadFailed(Exception e) {
                //下载异常进行相关提示操作
                Log.e(TAG, "onDownloadFailed: " );

            }
        });
    }

    /**
     * 将url进行encode，解决部分手机无法下载含有中文url的文件的问题（如OPPO R9）
     *
     * @param url
     * @return
     * @author xch
     */
    private String toUtf8String(String url) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void upDateApp( ) {
        //先清除缓存目录下的文件
        clearAPPCache();

        if (!f.exists()) {
            f.mkdirs();
        }
        dialog_update = new Dialog(mContext, R.style.update_dialog);
        dialog_update.setOnKeyListener(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.updatedialog, null);
        dialog_update.setContentView(view);
        TextView tv_queding = (TextView) view.findViewById(R.id.tv_queding);
        final TextView tv_quxiao = (TextView) view.findViewById(R.id.tv_quxiao);

        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //是wifi链接
                if (NetworkUtils.isWifiConnected(mContext)) {
                    gotoUpdate(mFileurl);
                    return;
                }
                dialog_warn = new Dialog(mContext, R.style.update_dialog);
                View view_warming = LayoutInflater.from(mContext).inflate(R.layout.netwarmingdlg, null);
                dialog_warn.setContentView(view_warming);
                TextView tv_warn_cancle = (TextView) view_warming.findViewById(R.id.tv_warn_cancle);
                TextView tv_warn_continue = (TextView) view_warming.findViewById(R.id.tv_warn_continue);
                tv_warn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_warn.dismiss();
                    }
                });
                tv_warn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_warn.dismiss();
                        gotoUpdate(mFileurl);
                    }
                });
                dialog_warn.show();

            }
        });

        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_update != null && dialog_update.isShowing()) {
                    dialog_update.dismiss();
                }
            }
        });

        dialog_update.setCancelable(false);
        dialog_update.show();
    }

    public void gotoUpdate(  String  fileurl) {

       // fileurl=  toUtf8String(fileurl);
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
        FileDownloader.getImpl().create(fileurl)
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
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setDataAndType(Uri.fromFile(downloadFile),
//                                    "application/vnd.android.package-archive");
//                            mContext.startActivity(intent);
                              Toast.makeText(Activity_5_1.this,downloadFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        dialog_updateing.dismiss();
                        Toast.makeText(mContext, "更新出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
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

}

