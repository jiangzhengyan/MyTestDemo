package com.jingcaiwang.mytestdemo.activities;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.network.OKHttpManager;
import com.jingcaiwang.mytestdemo.utils.Lambdatest;
import com.jingcaiwang.mytestdemo.utils.MyLifecycleHandler;
import com.jingcaiwang.mytestdemo.utils.ToastUtil;
import com.jingcaiwang.mytestdemo.utils.UserUtil;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsManager;
import com.jingcaiwang.mytestdemo.utils.permission.PermissionsResultAction;
import com.jingcaiwang.mytestdemo.views.CustomNoScrollWebView;

import org.json.JSONException;

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
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Style;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.navigation)
    BottomNavigationView mNavigation;

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
    private TextView tv_ani;
    LinearLayout mRootview;

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
        mRootview = (LinearLayout) findViewById(R.id.lyaout_fenshi);
        tv_ani = (TextView) findViewById(R.id.tv_ani);

        repair1();
        myList();

        jiexi();
        new Lambdatest();
        initPopu();
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ToastUtil.toastLongCenter(MainActivity.this, "订单");

                return true;
            }
        });
    }

    private void initPopu() {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_car_popu, mRootview, false);
        popup = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable((new BitmapDrawable()));
        popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popup.setAnimationStyle(R.style.mypopwindow_anim_style);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    private PopupWindow popup;

    public void anim(View view) {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(tv_ani, "scaleX", 1f, 3f);
        ObjectAnimator moveIn1 = ObjectAnimator.ofFloat(tv_ani, "scaleY", 1f, 3f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(tv_ani, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(tv_ani, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).with(moveIn).with(moveIn1);
        animSet.setDuration(2000);
        animSet.start();
//
//        View layout = popup.getContentView();
//        TextView tvpppp = (TextView) layout.findViewById(R.id.pppp);
//        tvpppp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Activity_1_1.class);
//                MainActivity.this.startActivity(intent);
//                startActivityForResult(intent, 11);
//            }
//        });
//        popup.showAtLocation(mRootview, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    @Override
    protected void onResume() {
        super.onResume();

        boolean applicationInForeground = MyLifecycleHandler.isApplicationInForeground();
        Toast.makeText(this, "onResume前台运行?  " + applicationInForeground, Toast.LENGTH_LONG).show();
        Log.e(TAG, "onResume: " + "onResume前台运行?  " + applicationInForeground);
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean applicationInForeground = MyLifecycleHandler.isApplicationInForeground();

    }

    @Override
    protected void onPause() {
        super.onPause();
        boolean applicationInForeground = MyLifecycleHandler.isApplicationInForeground();

    }

    private void jiexi() {
        String string = "{\"currTime\":1529482696725,\"result\":1,\"message\":\"\",\"cars\":[{\"sn\":\"250019660\",\"idc\":\"19250019660\",\"id\":1529312390,\"receivedTime\":1529312381993,\"electricity\":\"49/100\",\"mileage\":\"106\",\"engineStatus\":0,\"leftFrontDoor\":0,\"rightFrontDoor\":0,\"leftRearDoor\":0,\"rightRearDoor\":0,\"centralLckingStatus\":0,\"lightsStatus\":0,\"chargeStatus\":0,\"totalMileage\":\"20097\",\"gpsTime\":1529312335000,\"longitude\":116.490235,\"latitude\":39.784632,\"speed\":255,\"rpm\":-1,\"on\":0,\"voltage\":\"12.13\",\"boot\":0,\"lastHeartbeatTime\":\"2018/6/20 13:36:24\",\"IsOnline\":1,\"IsSleep\":0,\"KeyPosition\":-1,\"DrivingMethod\":-1,\"MileageRangeReceivedTime\":1529310942076},{\"sn\":\"250019661\",\"idc\":\"19250019661\",\"id\":1529482304,\"receivedTime\":1529482301299,\"electricity\":\"87/100\",\"mileage\":\"142\",\"engineStatus\":0,\"leftFrontDoor\":1,\"rightFrontDoor\":1,\"leftRearDoor\":1,\"rightRearDoor\":1,\"centralLckingStatus\":1,\"lightsStatus\":0,\"chargeStatus\":0,\"totalMileage\":\"73211\",\"gpsTime\":1529480282000,\"longitude\":116.35211,\"latitude\":39.836017,\"speed\":255,\"rpm\":-1,\"on\":0,\"voltage\":\"12.41\",\"boot\":0,\"lastHeartbeatTime\":\"2018/6/20 14:26:08\",\"IsOnline\":1,\"IsSleep\":0,\"KeyPosition\":-1,\"DrivingMethod\":-1,\"MileageRangeReceivedTime\":1529480270020}]}\n";

        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Modes modes = JSONObject.parseObject(string, Modes.class);
        //String resultValue = jsonObject.getString("lastHeartbeatTime");
        // Log.e(TAG, "jiexi: " + modes.getLastHeartbeatTime());
        // List<Modes> modes = JSONArray.parseArray(resultValue, Modes.class);

    }


    /**
     * 金额的监听
     */
    private CharSequence mTempText;

    private void inputNumberListener(final EditText editNumber,
                                     final Button btnConfirm,
                                     final String maxLimitAmount,
                                     final int dotBeforeLimit,
                                     final int dotAfterLimit) {
        mTempText = "";
        editNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTempText = s;
                if (TextUtils.isEmpty(maxLimitAmount)) {
//                    makeText("网络连接失败,请重新扫描");
                    return;
                }
                if (TextUtils.isEmpty(s)) {
                    btnConfirm.setEnabled(false);
                } else if (".".equals(s.toString())) {
                    btnConfirm.setEnabled(false);

                } else {
                    double parseDouble = Double.parseDouble(UserUtil.remain_dot_2(s.toString()));
                    int p = (int) (parseDouble * 100);
                    if (p == 0) {
                        btnConfirm.setEnabled(false);
                    } else if (p > Double.parseDouble(maxLimitAmount) * 100) {
                        btnConfirm.setEnabled(false);
                        Toast.makeText(MainActivity.this, "最大金额不能超过" + maxLimitAmount + "元", Toast.LENGTH_SHORT).show();
                    } else {
                        // amount = Integer.toString(p);
                        btnConfirm.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int mSelectionStart = editNumber.getSelectionStart();
                int mSelectionEnd = editNumber.getSelectionEnd();
                String s1 = mTempText.toString();
                if (s1.contains(".")) {
                    String[] split = s1.split("\\.");
                    int splitsize = split.length;
                    if (splitsize == 0) {
                    } else if (splitsize == 1) {
                        //  情况   121.
                        String ss0 = split[0];//点前面
                        if (ss0.length() > dotBeforeLimit) {
                            s.delete(mSelectionStart - 1, mSelectionEnd);
                        }
                    } else if (splitsize == 2) {
                        //  情况   121.12或者 .12
                        String ss0 = split[0];//点前面
                        String ss1 = split[1];//点后面
                        if (ss0.length() > dotBeforeLimit) {
                            s.delete(mSelectionStart - 1, mSelectionEnd);
                        }
                        if (ss1.length() > dotAfterLimit) {
                            s.delete(mSelectionStart - 1, mSelectionEnd);
                        }
                    }
                } else {
                    if (s1.length() > dotBeforeLimit) {
                        s.delete(mSelectionStart - 1, mSelectionEnd);
                    }
                }
            }
        });
    }


    EditText et_number;

    private void repair1() {

        if (ll_repair_index != null) {
            ll_repair_index.removeAllViews();
        }


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


    public class lladapter extends BaseAdapter {
        @Override
        public int getCount() {
            Log.e(TAG, "getCount===========: ");
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
                    nextPage(btn_1, "开门开门", Activity_2_1.class);
                    nextPage(btn_2, "标题", Activity_2_2.class);
                    nextPage(btn_3, "标题", Activity_2_3.class);
                    break;
                case 3:
                    //第3排
                    nextPage(btn_1, "标题", Activity_3_1.class);
                    nextPage(btn_2, "录音", Activity_3_2.class);
                    nextPage(btn_3, "播放录音", Activity_3_3.class);
                    break;
                case 4:
                    //第4排
                    nextPage(btn_1, "标题", Activity_4_1.class);
                    nextPage(btn_2, "标题", Activity_4_2.class);
                    nextPage(btn_3, "表格", Activity_4_3.class);
                    break;
                case 5:
                    //第5排
                    nextPage(btn_1, "测试下载", Activity_5_1.class);
                    nextPage(btn_2, "标题", Activity_5_2.class);
                    nextPage(btn_3, "练习", Activity_5_3.class);
                    break;
                case 6:
                    //第6排
                    nextPage(btn_1, "pop", Activity_6_1.class);
                    nextPage(btn_2, "OnDraw_myView", Activity_6_2.class);
                    nextPage(btn_3, "标题", Activity_6_3.class);
                    break;
                case 7:
                    //第7排
                    nextPage(btn_1, "Activity_7_1", Activity_7_1.class);
                    nextPage(btn_2, "Activity_7_2", Activity_7_2.class);
                    nextPage(btn_3, "Activity_7_3", Activity_7_3.class);
                    break;
                case 8:
                    //第8排
                    nextPage(btn_1, "Activity_8_1", Activity_8_1.class);
                    nextPage(btn_2, "Activity_8_2", Activity_8_2.class);
                    nextPage(btn_3, "Activity_8_3", Activity_8_3.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ToastUtil.toastLongCenter(this, "00");


        return super.onOptionsItemSelected(item);
    }
}
