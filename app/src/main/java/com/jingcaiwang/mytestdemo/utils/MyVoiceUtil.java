package com.jingcaiwang.mytestdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.application.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyVoiceUtil {

    private TextView tvAllStatus;

    private File myRecAudioFile;
    /**
     * 录音保存路径
     **/
    private File myRecAudioDir;
    private MediaRecorder mMediaRecorder01;
    /**
     * 存放音频文件列表
     **/
    private ArrayList<String> recordFiles;
    /**
     * 是否停止录音
     **/
    private boolean isStopRecord;
    private final String SUFFIX = ".amr";

    /**
     * 记录需要合成的几段amr语音文件
     **/
    private ArrayList<String> list;
    int second = 0;
    int secondLimit = 60;//时间限制
    /**
     * 计时器
     **/
    Timer timer;
    boolean isStartRecord = false;//是否开始过录音
    /**
     * 在暂停状态中
     **/
    private boolean inThePause;
    private ProgressBar pb;
    private ImageView iv_delete;
    private File finnalyVoiceFile;
    private Context context;
    private Activity activity;

    public MyVoiceUtil(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void initVoiceSetting(int secondLimit,File myRecAudioDir) {

        {
            this.secondLimit=secondLimit;
            this.myRecAudioDir=myRecAudioDir;
            //暂停状态标志位
            inThePause = false;
            //初始化list
            list = new ArrayList<String>();
            tvAllStatus = (TextView) activity.findViewById(R.id.tvAllStatus);
            pb = (ProgressBar) activity.findViewById(R.id.pb);

            iv_delete = (ImageView) activity.findViewById(R.id.iv_delete);
            // 取得sd card路径作为录音文件的位置
            if (myRecAudioDir==null){
                String pathStr = Environment.getExternalStorageDirectory().getPath() + File.separator + "cache" + File.separator+"myvoice";
                myRecAudioDir = new File(pathStr);
            }
            if (!myRecAudioDir.exists()) {
                myRecAudioDir.mkdirs();
                Log.e("录音", "创建录音文件夹！" + myRecAudioDir.exists());
            }
            if (!myRecAudioDir.exists()) {
                String pathStr = Environment.getExternalStorageDirectory().getPath() + File.separator + "cache" + File.separator+"myvoice";
                myRecAudioDir = new File(pathStr);
                myRecAudioDir.mkdirs();
            }
//			Environment.getExternalStorageDirectory().getPath() + "/" + PREFIX + "/";

            // 取得sd card 目录里的.arm文件
            getRecordFiles();
            deleteAllFile();
            //停止/提交
//            btn_stop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    iv_delete.setVisibility(View.VISIBLE);
//                    recodeStop();
//                    //这里写暂停处理的 文件！加上list里面 语音合成起来
//                    getInputCollection(list);
//                    //还原标志位
//                    inThePause = false;
//                    isStartRecord = false;
//                    //停止录音了
//                    isStopRecord = true;
//                    tvAllStatus.setText("开始录音");
//                    tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.icon_voice), null, null, null);
//
//                }
//            });
            //清空录音文件
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_delete.setVisibility(View.INVISIBLE);
                    second = 0;
                    pb.setProgress(0);
                    recodeStop();
                    getRecordFiles();
                    deleteAllFile();
                    isStartRecord = false;
                    inThePause = false;
                    list.clear();
                    tvAllStatus.setText("开始录音");
                    tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.icon_voice), null, null, null);
                }
            });

//        isPause = false;   是否暂停
//        inThePause = false; 是否在暂停中
            //  boolean isStartRecord=false;//是否开始过录音

            //所有状态的按钮
            tvAllStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (second >= secondLimit) {
                        Toast.makeText(activity, "已到最长录音时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isStartRecord) {
                        //已经暂停过了，再次点击按钮 开始录音，录音状态在录音中
                        if (inThePause) {
                            iv_delete.setVisibility(View.INVISIBLE);

                            tvAllStatus.setText("暂停录音");
                            tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.icon_stop), null, null, null);

                            start();
                            inThePause = false;
                        }
                        //正在录音，点击暂停,现在录音状态为暂停
                        else {
                            //当前正在录音的文件名，全程
                            iv_delete.setVisibility(View.VISIBLE);

                            inThePause = true;
                            recodeStop();
                            //start();
                            tvAllStatus.setText("继续录音   " + second + "'");
                            tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.icon_continue), null, null, null);
                        }

                    } else {
                        iv_delete.setVisibility(View.INVISIBLE);
                        //没有开始过录音
                        //开始
                        second = 0;
                        getRecordFiles();
                        deleteAllFile();
                        list.clear();
                        start();
                        tvAllStatus.setText("暂停录音");
                        tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.icon_stop), null, null, null);
                        isStartRecord = true;
                    }
                }
            });
        }
    }

    protected void recodeStop() {
        if (mMediaRecorder01 != null && !isStopRecord) {
            // 停止录音
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }


    /**
     * activity的生命周期，stop时关闭录音资源
     */

    public void onStop() {
        if (mMediaRecorder01 != null && !isStopRecord) {
            // 停止录音
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
    }


    /**
     * 获取目录下的所有音频文件
     */
    private void getRecordFiles() {
        recordFiles = new ArrayList<String>();
        File files[] = myRecAudioDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                recordFiles.add(files[i].getAbsolutePath());
            }
        }

    }

    private void start() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                second++;
                handler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);

        try {

            String mMinute1 = getTime();
            // 创建音频文件

            myRecAudioFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
            mMediaRecorder01 = new MediaRecorder();
            // 设置录音为麦克风
            mMediaRecorder01
                    .setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder01
                    .setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mMediaRecorder01
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //录音文件保存这里
            mMediaRecorder01.setOutputFile(myRecAudioFile
                    .getAbsolutePath());
            mMediaRecorder01.prepare();
            mMediaRecorder01.start();

            list.add(myRecAudioFile.getAbsolutePath());
            isStopRecord = false;
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        return time;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (second >= secondLimit) {
                recodeStop();
                tvAllStatus.setText(second + "`");
                tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                pb.setProgress(100);
                iv_delete.setVisibility(View.VISIBLE);
            }
            pb.setProgress(second * 100 / secondLimit);

        }

    };


    /**
     * 获取合成后的录音文件
     *
     * @return
     */
    public File getVoiceFile() {
        return finnalyVoiceFile;
    }

    /**
     * @param // 是否需要添加list之外的最新录音，一起合并
     * @return 将合并的流用字符保存
     */
    public void getInputCollection(List list) {
        String mMinute1 = getTime();

        // 创建音频文件,合并的文件放这里
        finnalyVoiceFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
        FileOutputStream fileOutputStream = null;
        if (!finnalyVoiceFile.exists()) {
            try {
                finnalyVoiceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileOutputStream = new FileOutputStream(finnalyVoiceFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
        for (int i = 0; i < list.size(); i++) {
            File file = new File((String) list.get(i));
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] myByte = new byte[fileInputStream.available()];
                //文件长度
                int length = myByte.length;

                //头文件
                if (i == 0) {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 0, length);
                    }
                }

                //之后的文件，去掉头文件就可以了
                else {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 6, length - 6);
                    }
                }

                fileOutputStream.flush();
                fileInputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //结束后关闭流
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //合成一个文件后，删除之前暂停录音所保存的零碎合成文件
        deleteListRecord();

    }

    private void deleteListRecord() {
        for (int i = 0; i < list.size(); i++) {
            File file = new File((String) list.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void deleteAllFile() {
        getRecordFiles();
        for (int i = 0; i < recordFiles.size(); i++) {
            File file = new File(recordFiles.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        if (myRecAudioFile != null && myRecAudioFile.exists()) {
            myRecAudioFile.delete();
        }

        recordFiles.clear();
    }
}
