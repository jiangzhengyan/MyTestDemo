package com.jingcaiwang.mytestdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;
import com.jingcaiwang.mytestdemo.utils.UserUtil;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsManager;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsResultAction;
import com.jingcaiwang.mytestdemo.views.CustomNoScrollWebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Style;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.tv)
    TextView tv;
    private CustomNoScrollWebView web_view;
    private RelativeLayout rel;
    private ListView lv;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.50f;
    public static final Style DEFAULT = new Style.Builder().setBackgroundColor(R.color.alphablack).setTextColor(R.color.textwhite).build();
    public static final Style DEFAULT_BLACK = new Style.Builder().setBackgroundColor(R.color.alphablack).setTextColor(R.color.textwhite).build();
    public static final Configuration conf = new Configuration.Builder().setDuration(2000).build();
    public static final Configuration confLong = new Configuration.Builder().setDuration(6000).build();
    private MainActivity.lladapter lladapter;

    LinearLayout ll_repair_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        lv = (ListView) findViewById(R.id.lv);
        ll_repair_index = (LinearLayout) findViewById(R.id.ll_repair_index);

        repair();
        repair1();
    }

    private void repair1() {

        if (ll_repair_index != null) {
            ll_repair_index.removeAllViews();
        }


        for (int i = 0; i < 6; i++) {
            View repairStatusLayout = View.inflate(this, R.layout.repair_status_layout, null);
            TextView  tv_status = (TextView) repairStatusLayout.findViewById(R.id.tv_status_0);
            TextView  tv_time = (TextView) repairStatusLayout.findViewById(R.id.tv_status_0_time);
            tv_status.setText("状态+"+i);
            tv_time.setText("时间+"+i);

            ll_repair_index.addView(repairStatusLayout);
        }

    }

    private void repair() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.GREEN);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        View view = new View(this);
        RelativeLayout.LayoutParams paramsView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        paramsView.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setBackgroundColor(Color.GRAY);
        view.setLayoutParams(paramsView);
        view.invalidate();

        relativeLayout.addView(view);
        TextView textStatus = new TextView(this);
        textStatus.setText("这是文字");
        textStatus.setBackgroundResource(R.drawable.shape_repair_status_bg_2);
        relativeLayout.addView(textStatus);

        linearLayout.addView(relativeLayout);
        TextView textTime = new TextView(this);
        textStatus.setText("20182222");
        linearLayout.addView(textTime);

        ll_repair_index.addView(linearLayout);
    }

    private String getPhoneDeviceInfo() {

        //获取手机型号：OPPO R11
        String MODEL = Build.MODEL;
        // 获取手机厂商：OPPO
        String MANUFACTURER = Build.MANUFACTURER;
        //           arm64-v8a
        String CPU_ABI = Build.CPU_ABI;
        //OPPO/R11/R11:7.1.1/NMF26X/1506793082:user/release-keys
        String FINGERPRINT = Build.FINGERPRINT;
        //d6b6041
        String SERIAL = Build.SERIAL;
        StringBuilder sb = new StringBuilder();
        sb.append("Device infomation :")
                .append("\nMODEL = " + MODEL)
                .append("\nMANUFACTURER = " + MANUFACTURER)
                .append("\nCPU_ABI = " + CPU_ABI)
                .append("\nFINGERPRINT = " + FINGERPRINT)
                .append("\nSERIAL = " + SERIAL)
                .append("\n");
        Log.e(TAG, "getPhoneInfo: " + sb);

        Field[] fields = Build.class.getFields();
        for (Field f : fields) {
            try {
                String name = f.getName();
                Object value = f.get(name);
//       String brand = f.get("BRAND").toString(); //Xiaomi
//       String model = f.get("MODEL").toString(); //Redmi Note 3

                Log.e(TAG, "key:" + name + ":value:" + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, new String[]{Manifest.permission.CALL_PHONE}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "同意", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(MainActivity.this, "拒绝", Toast.LENGTH_LONG).show();

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
            bitmap = BitmapFactory.decodeStream(is);
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
        lv.setAdapter(lladapter);
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
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
            View inflate = View.inflate(MainActivity.this, R.layout.item_menu, null);
            Button btn_1 = (Button) inflate.findViewById(R.id.btn_1);
            Button btn_2 = (Button) inflate.findViewById(R.id.btn_2);
            Button btn_3 = (Button) inflate.findViewById(R.id.btn_3);
            switch (position + 1) {
                case 1:
                    //第1排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 2:
                    //第2排
                    nextPage(btn_1, "标题", Activity_2_1.class);
                    nextPage(btn_2, "标题", Activity_2_2.class);
                    nextPage(btn_3, "标题", Activity_2_3.class);
                    break;
                case 3:
                    //第3排
                    nextPage(btn_1, "标题", Activity_3_1.class);
                    nextPage(btn_2, "标题", Activity_3_2.class);
                    nextPage(btn_3, "标题", Activity_3_3.class);
                    break;
                case 4:
                    //第4排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 5:
                    //第5排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 6:
                    //第6排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 7:
                    //第7排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 8:
                    //第8排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 9:
                    //第9排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;
                case 10:
                    //第10排
                    nextPage(btn_1, "标题", Activity_1_1.class);
                    nextPage(btn_2, "标题", Activity_1_2.class);
                    nextPage(btn_3, "标题", Activity_1_3.class);
                    break;

            }

            return inflate;
        }

        private void nextPage(Button btn, String title, final Class clazz) {
            btn.setText(title);
            int randomColor = UserUtil.getRandomColor();
            btn.setBackgroundColor(randomColor);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, clazz);
                    MainActivity.this.startActivity(intent);
                }
            });

        }
    }

    private void initBeepSound() {
        if (mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = null;
            try {
                file = getAssets().openFd("tts_0.mp3");
            } catch (IOException e) {

            }
//            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.qq);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
        playBeepSoundAndVibrate();
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
                Log.e(TAG, "onPageFinished: " + url);
                Log.e(TAG, "onPageFinished: " + w + "    +" + h);
                Log.e(TAG, "onPageFinished: " + view.getHeight());
                Log.e(TAG, "onPageFinished: " + view.getChildCount());
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
