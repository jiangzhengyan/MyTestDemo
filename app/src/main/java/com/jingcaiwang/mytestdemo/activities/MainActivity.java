package com.jingcaiwang.mytestdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsManager;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsResultAction;
import com.jingcaiwang.mytestdemo.views.CustomNoScrollWebView;
import com.jingcaiwang.mytestdemo.views.MyscrollView;
import com.lidroid.mutils.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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
    private ListView lv1;
    private MainActivity.lladapter lladapter;
    private MyscrollView myscrollview;
    private TextView tv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        processSome();
        callP();
        final Button tv_clivk = (Button) findViewById(R.id.btn_clivk);
        myscrollview = (MyscrollView) findViewById(R.id.myscrollview);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        lv1 = (ListView) findViewById(R.id.lv);
        input();
//        lv = (ListView) findViewById(R.id.lv);
        initBeepSound();
        Utils.getUtils().setApplication(null);
        Crouton.makeText(this, "" + Utils.getUtils(), DEFAULT, R.layout.activity_main).setConfiguration(conf).show();
//
//        BaseBean baseBean = JSONObject.parseObject("", BaseBean.class);
//        List<PreBillBean> preFeelist = JSONArray.parseArray(baseBean.getData(), PreBillBean.class);
//

        tv_clivk.setOnClickListener(new
                                            View.OnClickListener() {
                                                int x = 0;

                                                @Override
                                                public void onClick(View v) {
//                                                    startActivity(new Intent(MainActivity.this,TestActivity.class));
//                                                    startActivity(new Intent(MainActivity.this,B_Activity.class));
//                                                    UserUtil.showToastCenter(MainActivity.this, "sdgdg chdh h hfvjnn +" + x++, Color.WHITE, 17, Toast.LENGTH_LONG);

                                                    PopupWindow popupWindow = new
                                                            PopupWindow(MainActivity.this);

                                                    popupWindow.setContentView(View.inflate(MainActivity.this, R.layout.activity_test, null));
//                                                    popupWindow.showAsDropDown(tv_clivk);
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

                                                    lladapter.notifyDataSetChanged();

                                                    lv1.setSelection(10);

                                                    lv1.smoothScrollToPosition(11);
                                                }
                                            });

        myList();


        myscrollview.setOnScrollStateChangedListener(new MyscrollView.OnScrollStateChangeListener() {
            @Override
            public void onScrollChanged(MyscrollView.ScrollType scrollType) {

                Log.e(TAG, "onScrollChanged: " + scrollType);
            }
        }, new Handler());


        tv_1.setText("好啊好啊");
    }

    private void input() {
        EditText et_1 = (EditText) findViewById(R.id.et_1);
        EditText et_2 = (EditText) findViewById(R.id.et_2);
        EditText et_3 = (EditText) findViewById(R.id.et_3);
        EditText et_4 = (EditText) findViewById(R.id.et_4);
        EditText et_5 = (EditText) findViewById(R.id.et_5);
        EditText et_6 = (EditText) findViewById(R.id.et_6);
        et_1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        et_2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        et_3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        et_4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        et_5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        et_6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this,actionId+"",Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    private void processSome() {


        //[1] 获取缓存目录,不需要权限,清理缓存的时候会被清理掉,没有会自动创建
        File externalCacheDir = getExternalCacheDir();
        Log.e(TAG, "processSome: 缓存目录 " + externalCacheDir.getPath());
//        /storage/emulated/0/Android/data/com.jingcaiwang.mytestdemo/cache
        //getExternalCacheDirs();需要minSdkVersion>=19

        //[2]不会删除,不需要权限,会创建个文件夹aFileDir
        final File aFileDir = getExternalFilesDir("aFileDir");
        Log.e(TAG, "processSome: 文件目录 " + aFileDir.getPath());
// /storage/emulated/0/Android/data/com.jingcaiwang.mytestdemo/files/aFileDir

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap netBitmap = getNetBitmap("https://t12.baidu.com/it/u=2494558901,3986153391&fm=173&app=12&f=JPEG?w=500&h=371&s=D988BF55C1517DC60C9150640300E070");

                saveBitmap(MainActivity.this, netBitmap, aFileDir.getPath());
            }
        }).start();
    }

    private void callP() {
        requestPermission();
    }

    /**
     * 请求授权
     */
    private void requestPermission() {

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
//            //进行授权
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//        } else {
//            //调用打电话的方法
//            makeCall();
//        }
        makeCall();
    }

    private void makeCall() {

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult( this, new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
              Toast.makeText(MainActivity.this,"同意",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(MainActivity.this,"拒绝",Toast.LENGTH_LONG).show();

            }
        });





        try {


            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "17600908294"));
            Log.e(TAG, "makeCall 1: " + ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE));
            Log.e(TAG, "makeCall 2:   " + PackageManager.PERMISSION_GRANTED);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//        int permission = getApplicationContext().checkCallingPermission(Manifest.permission.CALL_PHONE);
//        Log.e(TAG, "makeCall: permission   "+permission );
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            Log.e(TAG, "makeCall: 需要授权 " );
//            // TODO: Consider calling
//                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//           return;
//        }
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "makeCall: =========");
        }
    }

    /**
     * 权限申请返回结果
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //同意权限申请
                    Toast.makeText(this, "权限同意了", Toast.LENGTH_SHORT).show();

                } else { //拒绝权限申请
                    Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //2017-2-13 根据网络图片url转换为bitmap
    private Bitmap getNetBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = android.graphics.BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void saveBitmap(Context context, Bitmap bitmap, String dir) {
//        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
//        File appDir = new File(sdCardDir, "ToastImage");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
        String fileName = "ToastImage" + System.currentTimeMillis() + ".jpg";
        File f = new File(dir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通知图库更新  
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private void myList() {

        lladapter = new lladapter();

        lv1.setAdapter(lladapter);

    }

    public class lladapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 66;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new
                    TextView(MainActivity.this);
            textView.setText("这是  " + position);
            return textView;
        }
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

                Log.e(TAG, "onResponse: " + baseBean);
            }

            @Override
            public void onError(Request request, Exception e, String msg) {

                Log.e(TAG, "onError: " + request);
                Log.e(TAG, "onError: " + msg);
            }
        }, null);
    }
}
