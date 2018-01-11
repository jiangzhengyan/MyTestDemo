package com.jingcaiwang.mytestdemo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jingcaiwang.mytestdemo.MyDaydreamService;
import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.beans.BaseBean;
import com.jingcaiwang.mytestdemo.beans.PreBillBean;
import com.jingcaiwang.mytestdemo.conf.AppConf;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;
import com.jingcaiwang.mytestdemo.utils.UpdateUtils;
import com.jingcaiwang.mytestdemo.utils.UserUtil;
import com.jingcaiwang.mytestdemo.views.CustomNoScrollWebView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CustomNoScrollWebView web_view;
    private RelativeLayout rel;
    private ListView lv;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.50f;
    public static final Style DEFAULT = new Style.Builder().setBackgroundColor(R.color.alphablack).setTextColor(R.color.textwhite).build();
    public static final Style DEFAULT_BLACK = new Style.Builder().setBackgroundColor(R.color.alphablack).setTextColor(R.color.textwhite).build();
    public static final Configuration conf = new Configuration.Builder().setDuration(2000).build();
    public static final Configuration confLong = new Configuration.Builder().setDuration(6000).build();
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Button tv_clivk = (Button) findViewById(R.id.btn_clivk);
//        lv = (ListView) findViewById(R.id.lv);
        initBeepSound();
        Crouton.makeText(this, "哈哈哈哈",  DEFAULT,R.layout.activity_main).setConfiguration(conf).show();
//
//        BaseBean baseBean = JSONObject.parseObject("", BaseBean.class);
//        List<PreBillBean> preFeelist = JSONArray.parseArray(baseBean.getData(), PreBillBean.class);
//

        tv_clivk.setOnClickListener(new
                                            View.OnClickListener() {
                                                int x = 0;

                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(MainActivity.this,TestActivity.class));
//                                                    UserUtil.showToastCenter(MainActivity.this, "sdgdg chdh h hfvjnn +" + x++, Color.WHITE, 17, Toast.LENGTH_LONG);

                                                    PopupWindow popupWindow = new
                                                            PopupWindow(MainActivity.this);

                                                    popupWindow.setContentView(View.inflate(MainActivity.this,R.layout.activity_test,null));
                                                    popupWindow.showAsDropDown(tv_clivk);
//                                                    new Handler().postDelayed(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            UpdateUtils.notifyUpdate(MainActivity.this, "title", "content", 100);
//                                                            UserUtil.showToastCenter(MainActivity.this, "11111111111+" + x++, Color.WHITE, 17, Toast.LENGTH_LONG);
//
//                                                            PopupWindow popupWindow = new
//                                                                    PopupWindow(MainActivity.this);
//                                                            popupWindow.setContentView(View.inflate(MainActivity.this,R.layout.activity_test,null));
//                                                            popupWindow.showAsDropDown(tv_clivk);
//                                                        }
//                                                    },3000);

                                                }
                                            });


    }


    private void initBeepSound() {
        if (mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.qq);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {

        // 扫描声音和振动
        if (mediaPlayer != null) {
            Log.e(TAG, "playBeepSoundAndVibrate: " + "开始播放");
            mediaPlayer.start();
        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 初始化webview
     *
     * @param webview
     */
    public void initWebView(WebView webview) {

        final WebSettings webSetting = webview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        webSetting.setBlockNetworkImage(true);

//        webview.setWebViewClient(new HelloWebViewClient());
        webview.setInitialScale(5);
//        webview.addJavascriptInterface(new Object(){
//            @JavascriptInterface
//            public void back_h5() {
//                WebUI.this.finish();
//            }
//        },"Android");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                web_view.loadUrl("http://testjcgj.jingcaiwang.cn:8886/H5-page/ativities/ativities.html?c=&activityId=157");
                super.onPageFinished(view, url);
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                android.util.Log.e(TAG, "onPageFinished: " + url);
                android.util.Log.e(TAG, "onPageFinished: " + w + "    +" + h);
                android.util.Log.e(TAG, "onPageFinished: " + view.getHeight());
                android.util.Log.e(TAG, "onPageFinished: " + view.getChildCount());
                //重新测量
                web_view.measure(w, h);
                webSetting.setBlockNetworkImage(false);

            }
        });

    }


    private void getWorkerFunctions() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("b", "1");
        OKHttpManager.postAsyn("http://192.168.23.2:8080/doormanager/sendDoorInfoByQRCode.do", map, new OKHttpManager.ResultCallback<String>() {
            @Override
            public void onResponse(String baseBean) {

                Log.e(TAG, "onResponse: "+baseBean );
            }

            @Override
            public void onError(Request request, Exception e, String msg) {

                Log.e(TAG, "onError: "+request );
                Log.e(TAG, "onError: "+msg );
            }
        }, null);
    }
}
