package com.jingcaiwang.mytestdemo.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 经常使用的工具类 #1 log打印等一系列工具
 *
 * @author jiang_yan
 */
public class UserUtil {
    private static final String TAG = "UserUtil";

    public UserUtil() {

    }

    private static boolean isShowLog = true;// 是否打印log日志

    /**
     * 打印log日志的方法.
     *
     * @param tag
     * @param content
     */
    public static void Log(String tag, String content) {
        if (isShowLog) {
            Log.e(tag, content);
        }
    }

    /**
     * 以友好的方式显示时间 ,几秒前/几分钟前/几小时前/昨天/前天/天前/日期......
     *
     * @param strDate 1423224342字符串毫秒值
     * @return
     */
    public static String friendly_time(String strDate) {
        long oldTime = new Date(Long.parseLong(strDate)).getTime();
        String ftime = "";
        int minite = 0;
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = formatDate(cal.getTime().getTime(), "yyyy-MM-dd");
        String paramDate = formatDate(oldTime, "yyyy-MM-dd");
        ;
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
                    ftime = Math.max(
                            (cal.getTimeInMillis() - oldTime) / 1000,
                            1)
                            + "秒前";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }

            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = oldTime / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
                    ftime = Math.max(
                            (cal.getTimeInMillis() - oldTime) / 1000,
                            1)
                            + "秒前";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = formatDate(oldTime, "yyyy-MM-dd");
        }
        return ftime;
    }

    /**
     * 1413131232 -->一分钟之内,几分钟前,几小时前,日期2016-12-12 13:10
     * 用到的地方 : 1,投诉建议主页面,详情页.
     * 2, 有求必应主页面
     * 3, 随叫随到主页面
     * 4,园圈主页面,园圈详情页右上角
     *
     * @param strDate
     * @return
     */
    public static String friendly_time_2(String strDate) {
        long oldTime = new Date(Long.parseLong(strDate)).getTime();
        String ftime = "";
        int minite;
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = formatDate(cal.getTime().getTime(), "yyyy-MM-dd");
        String paramDate = formatDate(oldTime, "yyyy-MM-dd");

        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
//					ftime = Math.max(
//							(cal.getTimeInMillis() - oldTime) / 1000,
//							1)
//							+ "秒前";
                    ftime = "1分钟内";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }

            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = oldTime / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
//					ftime = Math.max(
//							(cal.getTimeInMillis() - oldTime) / 1000,
//							1)
//							+ "秒前";
                    ftime = "1分钟内";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else {
            ftime = formatDate(oldTime, "yyyy-MM-dd HH:mm");
        }
        return ftime;
    }

    /**
     * 毫秒时间转换成一定格式的日期,14002020304--->2016-12-12 14:12:43
     *
     * @param timeMillis  时间戳毫秒值
     * @param datePattern yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long timeMillis, String datePattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);

        String dateString = simpleDateFormat.format(new Date(timeMillis));
        return dateString;
    }

    /**
     * 获取一个日期的毫秒值
     *
     * @param dateTime    日期时间  2018-09-11 12:12:34
     * @param datePattern yyyy-MM-dd HH:mm:ss
     * @return 日期的毫秒值
     */
    public static long getDateTimeMillis(String dateTime, String datePattern) {
        if (TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(datePattern))
            return 0;
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
        try {
            return simpleDateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param firstTimeMillis 第一个时间的毫秒值
     * @param lastTimeMillis  第二个时间的毫秒值
     * @return 是否是同一天.
     */
    public static boolean isSameDay(long firstTimeMillis, long lastTimeMillis) {
        if (firstTimeMillis <= 0 || lastTimeMillis <= 0)
            return false;
        String first = formatDate(firstTimeMillis, "yyyy-MM-dd");
        String second = formatDate(lastTimeMillis, "yyyy-MM-dd");
        return first.equalsIgnoreCase(second);
    }

    /**
     * 获取 SimpleDateFormat
     *
     * @param datePattern
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat getSimpleDateFormat(String datePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat;
    }

    /**
     * 获取 SimpleDateFormat
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        return simpleDateFormat;
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @return long毫秒时间
     */
    public static long getCurrentTimeMilliseconds() {

        return new Date().getTime();
    }

    /**
     * 获取当前时间  如2017-12-02
     *
     * @param datePattern yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateStr(String datePattern) {

        return formatDate(getCurrentTimeMilliseconds(), datePattern);
    }


    /**
     * 对比时间是否超过某个时间长度 timelong
     *
     * @param historyTimeMilli 历史某个时间    .单位毫秒值
     * @param timeLongMilli    现在时间-历史时间是否超过这个时间.  单位毫秒值
     * @return true 超过规定时间,  false 未超过规定时间
     */
    public static boolean isTimeOverOneTime(String historyTimeMilli, String timeLongMilli) {
        long timeLong_ = Long.parseLong(timeLongMilli);//时长
        long historyTime_ = Long.parseLong(historyTimeMilli);//历史时间
        long currentTimeMilliseconds_ = getCurrentTimeMilliseconds();//现在时间
        long result_ = currentTimeMilliseconds_ - historyTime_;
        return result_ > timeLong_;
    }

    /**
     * 显示对话框
     */
    public static void showDialog(Context context, String title, String message, int theme) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, theme);
        dialog.setTitle(title).setMessage(message);
        dialog.show();

    }

    /**
     * 判断用户是否需要去登录,根据c值去判断用户是否已经登录了
     */
//    public static boolean isNeedLogin() {
//        String c = MyApplication.getInstance().getC();
//        if (TextUtils.isEmpty(c)) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 前往登录的界面,
     *
     * @param context
     * @param tagBack 进入登录页面按返回  : 是返回本页面 y,返回首页 n
     */
//    public static void goLoginUI(Context context, String tagBack) {
//        Intent intent = new Intent(context, LoginUI.class);
//        if (!TextUtils.isEmpty(tagBack.trim())) {
//            intent.putExtra("tag", tagBack);
//            context.startActivity(intent);
//        }
//    }

    /**
     * 判断用户名是否合法 长度不小于3个字符，不大于16个字符，26个大小写字母、数字或“_-”（不能以数字开头）
     *
     * @param userName
     */
    public static boolean isUserName(String userName) {
        if (TextUtils.isEmpty(userName.trim().toString())) {
            return false;
        }
        String numReg = "[0-9]";
        String reg = "^[a-zA-Z0-9-_]{3,16}$";
        String firstC = String.valueOf(userName.charAt(0));
        if (firstC.matches(numReg)) {

            return false;
        }

        if (userName.matches(reg))
            return true;
        return false;

    }

    /**
     * 判断密码是否合法 密码，长度不小于3个字符，不大于32个字符，非中文 非空格
     *
     * @param passWord
     * @return
     */
    public static boolean isPassWord(String passWord) {
        String regPassWord = "^[\u4e00-\u9fa5]*$";

        if (passWord.length() < 3 || passWord.length() > 32) {
            // 提示
            return false;
        }
        for (int i = 0; i < passWord.length(); i++) {
            char charAt = passWord.charAt(i);

            String valueOf = String.valueOf(charAt);
            if (valueOf.matches(regPassWord)) {
                // 含有中文
                return false;
            }
            if (" ".equals(valueOf)) {
                //含有空格
                return false;
            }

        }
        return true;
    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String remain_dot_2(String num) {
        if (TextUtils.isEmpty(num)) {
            return "";
        }
        double d = Double.parseDouble(num);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String formatNum = df.format(d);
        if (formatNum.startsWith(".")) {
            formatNum = "0".concat(formatNum);
        }
        return formatNum;
    }

    /**
     * 把元转换成分
     *
     * @param money
     * @return
     */
    public static String parseMoneyToCenter(String money) {
        double parseDouble = Double.parseDouble(UserUtil.remain_dot_2(money.trim().toString()));
        return Integer.toString((int) (parseDouble * 100));
    }



    /**
     * 展示中间toast
     *
     * @param content
     */
    private static Toast toast;//在类前面声明吐 司，确保在这个页面只有一个吐 司

    public static void showToastCenter(Context mContext, String msg, int textColor, int textSize, int mDuration) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, mDuration);
        }else {
            toast.cancel();//关闭吐 司显示
            Log.e(TAG, "showToastCenter: "+toast );
            toast = Toast.makeText(mContext, msg, mDuration);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(mDuration);
        LinearLayout toastView = (LinearLayout) toast.getView();
        TextView textView = (TextView) toastView.getChildAt(0);
        toastView.setBackgroundResource(R.drawable.bg_pop_reg_tip_user);
        textView.setTextColor(textColor);
        textView.setText(msg);
        textView.setTextSize(textSize);
        textView.setGravity(Gravity.CENTER);
        toast.show();
    }

    /**
     * 输入框焦点变化的监听方法,是否显示后面的情况按钮
     *
     * @param et
     * @param ivClear
     * @param hasFocus
     */
    public static void isShowClearBtn(EditText et, ImageView ivClear, boolean hasFocus) {
        if (null != et && null != ivClear) {
            String pwd = et.getText().toString();
            if (hasFocus) {
                if (TextUtils.isEmpty(pwd)) {
                    ivClear.setVisibility(View.GONE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            } else {
                ivClear.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 启动到app详情界面,检查版本更新，跳转到腾讯应用宝进行下载或者网页
     * <p>
     * //    本App的包名
     * //   com.tencent.android.qqdownloader
     * 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,
     * 否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetailMarketorWeb(Context context) {

        try {

            Uri uri = Uri.parse("market://details?id=" + "com.risenb.jingkai");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.tencent.android.qqdownloader");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Uri uri = Uri.parse("http://sj.qq.com/myapp/detail.htm?apkName=com.risenb.jingkai");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }


    /**
     * 重新设置版本的信息数据
     */
//    public static void resetLineVersionData(final Context context, final MyApplication application) {
//        if (null == context || null == application) return;
//        RequestParams params = new RequestParams(); // 默认编码UTF-8
//        params.addBodyParameter("deviceType", "Android");
//        String url = context.getResources().getString(R.string.service_host_address).concat(context.getString(R.string.getVersionInfo));
//        NetUtils.getNetUtils().send(false, url, params, new NetUtils.NetBack() {
//
//            @Override
//            public void onSuccess(BaseBean baseBean) {
//                String versionData = baseBean.getData();
//                application.setLineVersionData(versionData);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                application.setLineVersionData("");
//            }
//        });
//    }

    /**
     * 重新设置当前的本地版本的信息
     *
     * @param context
     * @param application
     */
//    public static void resetCurrentVersion(final Context context, final MyApplication application) {
//        if (null == context || null == application) return;
//
//        PackageManager pm = context.getPackageManager();
//        PackageInfo pi = null;//getPackageName()是你当前类的包名，0代表是获取版本信息
//        try {
//            pi = pm.getPackageInfo(context.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String name = pi.versionName;
//        int code = pi.versionCode;
//        application.setCurrentVersionCode(code + "");
//        application.setCurrentVersionName(name);
//    }

    /**
     * 判断是否需要进行版本的更新
     *
     * @return
     */
//    public static boolean isNeedUpDateVersion(Context context, MyApplication application) {
//        if (null == context || null == application) return false;
//        //重新设置版本信息
//        resetLineVersionData(context, application);
//        resetCurrentVersion(context, application);
//        boolean isNeed = false;
//        int currentVersionCode = Integer.parseInt(application.getCurrentVersionCode());
//        String lineVersionData = application.getLineVersionData();
//        Log.e("====", "processVerdionMessage1: " + lineVersionData);
//        if (!TextUtils.isEmpty(lineVersionData)) {
//            int lineVersionCode = com.alibaba.fastjson.JSONObject.parseObject(lineVersionData).getIntValue("code");
//            isNeed = currentVersionCode < lineVersionCode;
//        }
//        return isNeed;
//    }

    /**
     * 跳转到另一个app,如果手机没有安装,就去下载
     *
     * @param toPackageName 跳转到的app的包名
     * @param toUrl         app的应用商店
     */
    public static void goAnotherApp(Context context, String toPackageName, String toUrl) {
        if (null == toPackageName || null == toUrl) {
            return;
        }
        Intent intent = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(toPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent viewIntent = new Intent("android.intent.action.VIEW",
                    Uri.parse(toUrl));
            context.startActivity(viewIntent);
        }
    }

//    public static void resetJpushTagAndAlis(MyApplication application) {
//        // 设置极光推送参数
//        JPushInterface.resumePush(application);
//        if (!TextUtils.isEmpty(application.getC())) {
//            //2017-2-13 极光推送功能改造 device_token
//            JpushSet jpushSet = new JpushSet(application);
//
//            jpushSet.setAlias(application.getDeviceToken());
//            jpushSet.setTag(application.getJpushTag());
//            Log.e("Jpushset    :", "resetJpushTagAndAlis: " + application.getJpushTag());
//        } else {
//            //2017-2-13 极光推送功能改造 device_token
//            new JpushSet(application).setAliasAndTags("www", "www");
//        }
//    }

    /**
     * 弹出键盘
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//显示键盘
        imm.showSoftInput(view, 0);
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 键盘是否显示
     * 无论显示与否都是true()
     *
     * @param context
     * @return
     */
    public static boolean isShowSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取状态信息

        return imm.isActive();//true 打开
    }

    /**
     * 获取drawable数组
     *
     * @param context
     * @return
     */
    public static int[] getDrawableIconArr(Context context, int resId) {

        TypedArray ar = context.getResources().obtainTypedArray(resId);
        int len = ar.length();
        int[] icon = new int[len];
        for (int i = 0; i < len; i++) {
            icon[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        return icon;
    }

    /**
     * 获取资源字符串数组
     *
     * @param context
     * @return
     */
    public static String[] getStringTextArr(Context context, int resId) {
        //文字
        String[] text = context.getResources().getStringArray(resId);
        return text;
    }

    /**
     * 存图片,返回图片路径
     *
     * @param context  上下文
     * @param bitmap   要存的图片
     * @param dirName  存图片的文件夹名字
     * @param fileName 图片的名字 ".jpg"
     * @return 图片存放的路径的全名
     */
    public static String saveBitmap(Context context, Bitmap bitmap, String dirName, String fileName) {
//        选择的图片路径 : /storage/emulated/0/DCIM/Camera/IMG_20170918_135342.jpg
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File f = new File(appDir, fileName);
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
        return f.getPath();
    }

    /**
     * 获取一定范围的随机数. 比如 10-20的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        if (max < min) {
            return 0;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    //    /**
//     * 绘制文字到左下方
//     * @param context
//     * @param bitmap
//     * @param text
//     * @param size
//     * @param color
//     * @param paddingLeft
//     * @param paddingBottom
//     * @return
//     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(ScreenUtils.dp2px(size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                ScreenUtils.dp2px(paddingLeft),
                bitmap.getHeight() - ScreenUtils.dp2px(paddingBottom));
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     *   随机颜色
     * @return
     */
    public static int getRandomColor(){

        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        int rgb = Color.rgb(r, g, b);
        return rgb;
    }
}
