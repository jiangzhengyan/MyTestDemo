package com.jingcaiwang.mytestdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.jingcaiwang.mytestdemo.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 本类的主要功能是 :  播放声音     布局  include  layout_my_record_play_voice
 *  使用单例模式   onDestroy   onStop
 * @author jiang_zheng_yan  2018/10/8 14:16
 */
public class MyRecordVoicePlayUtil {

    private TextView tvAllStatus;

    private static final MyRecordVoicePlayUtil myRecordVoicePlayUtil = new MyRecordVoicePlayUtil();

    /**
     * 计时器
     **/
    Timer timer;

    private ProgressBar pb;
    private Context context;
    private Activity activity;
    private MediaPlayer mediaPlayer;
    private int duration;

    private MyRecordVoicePlayUtil() {

    }

    public static MyRecordVoicePlayUtil getInstance() {

        return myRecordVoicePlayUtil;
    }

    private String playUrl;

    public void initVoiceSetting(Context context, Activity activity, String playUrl) {
        this.context = context;
        this.activity = activity;
        this.playUrl = playUrl;

        tvAllStatus = (TextView) activity.findViewById(R.id.tvAllStatus);
        pb = (ProgressBar) activity.findViewById(R.id.pb);
        initPlay();

        //所有状态的按钮
        tvAllStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isStop) {
                    play();
                } else {
                    stopPlay();
                }

            }
        });

    }

    private boolean isStop = true;//是否在停止播放



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                pb.setProgress(currentPosition * 100 / duration);

                tvAllStatus.setCompoundDrawablesWithIntrinsicBounds
                        (activity.getResources().getDrawable(R.mipmap.icon_stop_play), null, null, null);
                String s = UserUtil.traslateMillisToFM(currentPosition + "");
                tvAllStatus.setText("停止播放        "+s);

            }
        }

    };

    private void initPlay() {
        isStop = true;
        tvAllStatus.setEnabled(false);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(playUrl);
            mediaPlayer.prepareAsync();// 准备播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    duration = mediaPlayer.getDuration();
                    pb.setProgress(0);
                    tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.mipmap.icon_play), null, null, null);
                    tvAllStatus.setText("开始播放        " + UserUtil.traslateMillisToFM(duration + ""));
                    tvAllStatus.setEnabled(true);

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
           stopPlay();
        }
    }

    private void play() {
        tvAllStatus.setEnabled(false);

        mediaPlayer = new MediaPlayer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();

        try {
            mediaPlayer.setDataSource(playUrl);
            mediaPlayer.prepareAsync();// 准备播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    tvAllStatus.setCompoundDrawablesWithIntrinsicBounds
                            (activity.getResources().getDrawable(R.mipmap.icon_stop_play), null, null, null);
                    tvAllStatus.setText("停止播放        "+"0'");
                    timer.schedule(timerTask, 1000, 1000);
                    duration = mediaPlayer.getDuration();
                    mediaPlayer.start();// 播放
                    isStop = false;
                    tvAllStatus.setEnabled(true);

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //完成播放
                    stopPlay();
                }
            });


        } catch (IOException e1) {
            e1.printStackTrace();
              Toast.makeText(activity,"录音地址异常",Toast.LENGTH_SHORT).show();

            stopPlay();
        }
    }

    private static final String TAG = "MyRecordVoicePlayUtil";

    private void stopPlay() {
        isStop = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        pb.setProgress(0);
        tvAllStatus.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.mipmap.icon_play), null, null, null);
        tvAllStatus.setText("开始播放        " + UserUtil.traslateMillisToFM(duration+""));
        tvAllStatus.setEnabled(true);
    }

    public void onDestroy() {
        stopPlay();
    }


    public void onStop() {
        stopPlay();
    }
}
