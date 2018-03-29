package com.jingcaiwang.mytestdemo.activities;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.UserUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_1_2 extends AppCompatActivity {
    @Bind(R.id.et_chongmoney)
    EditText et_chongmoney;
    @Bind(R.id.tv_pay)
    TextView tv_pay;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.50f;
    private ArrayList<AssetFileDescriptor> fileLists;
    /**
     * 金额的监听
     */
    private CharSequence mTempText;
    private String amount;//充值的金额(元)
    private int limitAmount = 99999999;//限制最大金额

    //充值的最小金额
    private int minAmount = 1;//限制最小金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_2);
        ButterKnife.bind(this);

        initSoundData();

        etListener();
    }

    /**
     * 存放声音文件
     */
    private void initSoundData() {
        fileLists = new ArrayList<>();
        Resources am = getResources();
        fileLists.add(am.openRawResourceFd(R.raw.tts_0));//0
        fileLists.add(am.openRawResourceFd(R.raw.tts_1));//1
        fileLists.add(am.openRawResourceFd(R.raw.tts_2));//2
        fileLists.add(am.openRawResourceFd(R.raw.tts_3));//3
        fileLists.add(am.openRawResourceFd(R.raw.tts_4));//4
        fileLists.add(am.openRawResourceFd(R.raw.tts_5));//5
        fileLists.add(am.openRawResourceFd(R.raw.tts_6));//6
        fileLists.add(am.openRawResourceFd(R.raw.tts_7));//7
        fileLists.add(am.openRawResourceFd(R.raw.tts_8));//8
        fileLists.add(am.openRawResourceFd(R.raw.tts_9));//9
        fileLists.add(am.openRawResourceFd(R.raw.tts_cash));//10
        fileLists.add(am.openRawResourceFd(R.raw.tts_coupon));//11
        fileLists.add(am.openRawResourceFd(R.raw.tts_dot));//12
        fileLists.add(am.openRawResourceFd(R.raw.tts_hundred));//13
        fileLists.add(am.openRawResourceFd(R.raw.tts_koubei_daibo));//14
        fileLists.add(am.openRawResourceFd(R.raw.tts_mobiledata));//15
        fileLists.add(am.openRawResourceFd(R.raw.tts_reward));//16
        fileLists.add(am.openRawResourceFd(R.raw.tts_success));//17
        fileLists.add(am.openRawResourceFd(R.raw.tts_ten));//18
        fileLists.add(am.openRawResourceFd(R.raw.tts_ten_million));//19
        fileLists.add(am.openRawResourceFd(R.raw.tts_ten_thousand));//20
        fileLists.add(am.openRawResourceFd(R.raw.tts_thousand));//21
        fileLists.add(am.openRawResourceFd(R.raw.tts_voucher));//22
        fileLists.add(am.openRawResourceFd(R.raw.tts_yuan));//23
    }

    @OnClick({R.id.tv_pay})
    public void moneyOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                //initSoundData();
                mediaPlayer = null;
                tempIndex = 0;
                startMoney();
                break;

        }
    }

    //位数
    private void startMoney() {
        int amountLength = amount.length();
        ArrayList<Integer> fileIndexs = new ArrayList<>();
        fileIndexs.add(17);
        switch (amountLength) {
            case 1:
                //一位数
                money_1(fileIndexs, Integer.parseInt(amount));
                break;
            case 2:
                //两位数
                money_2(fileIndexs, amount);
                break;
            case 3:
                //3位数
                money_3(fileIndexs, amount);
                break;
            case 4:
                //4位数
                money_4(fileIndexs, amount);
                break;
            case 5:
                //5位数
                money_5(fileIndexs, amount);
                break;
            case 6:
                money_6(fileIndexs, amount);
                break;
            case 7:
                money_7(fileIndexs, amount);
                break;
            case 8:
                money_8(fileIndexs, amount);
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
        }
        for (int i = 0; i < fileIndexs.size(); i++) {
            Log.e(TAG, "money_2需要读取的index:: " + fileIndexs.get(i));
        }
        fileIndexs.add(23);
        initBeepSound(fileIndexs);

    }


    /**
     * 8位数
     *
     * @param fileIndexs
     * @param moneyAmount
     */
    private void money_8(ArrayList<Integer> fileIndexs, String moneyAmount) {
        char[] amountChars = moneyAmount.toCharArray();
        money_4(fileIndexs, amountChars[0] + "" + amountChars[1] + "" + amountChars[2] + "" + amountChars[3]);
        fileIndexs.add(20);

        //10000 10001   10011  10111 11101  11111
        // 0123 4567
        // 1000 0000
        // 1000 0001
        // 1000 0011
        // 1000 0111
        // 1000 1111

        if ((amountChars[4])==('0') && (amountChars[5])==('0') && (amountChars[6])==('0')&&(amountChars[7])==('0') ) {
            //1000 0000
        } else if (  (amountChars[4])==('0') && (amountChars[5])==('0')&&(amountChars[6])==('0') ) {
            //1000 0001
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt((amountChars[7] + "")));
        } else if ((amountChars[4])==('0') && (amountChars[5])==('0')) {
            //1000 0011
            fileIndexs.add(0);
            money_2(fileIndexs, amountChars[6] + "" + amountChars[7]);
        } else if ((amountChars[4])==('0')) {
            //1000 0111
            fileIndexs.add(0);
            money_3(fileIndexs, amountChars[5] + "" + amountChars[6] + "" + amountChars[7]);
        } else {
            money_4(fileIndexs, amountChars[4] + "" + amountChars[5] + "" + amountChars[6] + "" + amountChars[7]);
        }

    }
    /**
     * 7位数
     *
     * @param fileIndexs
     * @param moneyAmount
     */
    private void money_7(ArrayList<Integer> fileIndexs, String moneyAmount) {
        char[] amountChars = moneyAmount.toCharArray();
        money_3(fileIndexs, amountChars[0] + "" + amountChars[1] + "" + amountChars[2]);
        fileIndexs.add(20);

        //10000 10001   10011  10111 11101  11111
        // 012 3456
        // 100 0000
        // 100 0001
        // 100 0011
        // 100 0111
        // 100 1111

        if ((amountChars[3])==('0') && (amountChars[4])==('0') && (amountChars[5])==('0') && (amountChars[6])==('0')) {
            //100 0000
        } else if ((amountChars[3])==('0') && (amountChars[4])==('0') && (amountChars[5])==('0')) {
            //100 0001
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt((amountChars[6] + "")));
        } else if ((amountChars[3])==('0') && (amountChars[4])==('0')) {
            //100 0011
            fileIndexs.add(0);
            money_2(fileIndexs, amountChars[5] + "" + amountChars[6]);
        } else if ((amountChars[3])==('0')) {
            //100 0111
            fileIndexs.add(0);
            money_3(fileIndexs, amountChars[4] + "" + amountChars[5] + "" + amountChars[6]);
        } else {
            money_4(fileIndexs, amountChars[3] + "" + amountChars[4] + "" + amountChars[5] + "" + amountChars[6]);
        }

    }

    /**
     * 6位数
     *
     * @param fileIndexs
     * @param moneyAmount
     */
    private void money_6(ArrayList<Integer> fileIndexs, String moneyAmount) {
        char[] amountChars = moneyAmount.toCharArray();
        money_2(fileIndexs, amountChars[0] + "" + amountChars[1]);
        fileIndexs.add(20);

        //10000 10001   10011  10111 11101  11111
        //01 2345

        // 10 0000
        // 10 0001
        // 10 0011
        // 10 0111
        // 10 1111

        if ((amountChars[2])==('0') && (amountChars[3])==('0') && (amountChars[4])==('0') && (amountChars[5])==('0')) {
            //10 0000
        } else if ((amountChars[2])==('0') && (amountChars[3])==('0') && (amountChars[4])==('0')) {
            //10 0001
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt((amountChars[5] + "")));
        } else if ((amountChars[2])==('0') && (amountChars[3])==('0')) {
            //10 0011
            fileIndexs.add(0);
            money_2(fileIndexs, amountChars[4] + "" + amountChars[5]);
        } else if ((amountChars[2])==('0')) {
            //10 0111
            fileIndexs.add(0);
            money_3(fileIndexs, amountChars[3] + "" + amountChars[4] + "" + amountChars[5]);
        } else {
            money_4(fileIndexs, amountChars[2] + "" + amountChars[3] + "" + amountChars[4] + "" + amountChars[5]);
        }

    }

    /**
     * 5位数
     *
     * @param fileIndexs
     * @param moneyAmount
     */
    private void money_5(ArrayList<Integer> fileIndexs, String moneyAmount) {
        char[] amountChars = moneyAmount.toCharArray();
        money_1(fileIndexs, Integer.parseInt((amountChars[0] + "")));
        fileIndexs.add(20);

        //10000 10001   10011  10111 11101  11111

        if ((amountChars[1])==('0') && (amountChars[2])==('0') && (amountChars[3])==('0') && (amountChars[4])==('0')) {
            //10000
        } else if ((amountChars[1])==('0') && (amountChars[2])==('0') && (amountChars[3])==('0')) {
            //10001
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt((amountChars[4] + "")));
//            money_1(fileIndexs, Integer.parseInt(amountChars[2] + ""));
        } else if ((amountChars[1])==('0') && (amountChars[2])==('0')) {
            //10011
            fileIndexs.add(0);
            money_2(fileIndexs, amountChars[3] + "" + amountChars[4]);
        } else if ((amountChars[1])==('0')) {
            //10111
            fileIndexs.add(0);
            money_3(fileIndexs, amountChars[2] + "" + amountChars[3] + "" + amountChars[4]);
        } else {
            money_4(fileIndexs, amountChars[1] + "" + amountChars[2] + "" + amountChars[3] + "" + amountChars[4]);
        }

    }

    /**
     * 4位数
     *
     * @param fileIndexs
     * @param moneyAmount
     */
    private void money_4(ArrayList<Integer> fileIndexs, String moneyAmount) {
        char[] amountChars = moneyAmount.toCharArray();

        money_1(fileIndexs, Integer.parseInt((amountChars[0] + "")));
        fileIndexs.add(21);

        //1000 1001   1011  1101  1111

        if ((amountChars[1])==('0') && (amountChars[2])==('0') && (amountChars[3])==('0')) {
//            money_1(fileIndexs, Integer.parseInt(amountChars[2] + ""));
        } else if ((amountChars[1])==('0') && (amountChars[2])==('0')) {
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt((amountChars[3] + "")));
        } else if ((amountChars[1])==('0')) {
            fileIndexs.add(0);
            money_2(fileIndexs, amountChars[2] + "" + amountChars[3]);
        } else {
            money_3(fileIndexs, amountChars[1] + "" + amountChars[2] + "" + amountChars[3] + "");
        }

    }

    /**
     * 三位数
     *
     * @param fileIndexs
     */
    private void money_3(ArrayList<Integer> fileIndexs, String thirdNum) {
        char[] amountChars = thirdNum.toCharArray();
        money_1(fileIndexs, Integer.parseInt((amountChars[0] + "")));
        fileIndexs.add(13);
        //100
        //101
        if ((amountChars[1])==('0') && (amountChars[2])==('0')) {
            //100
        } else if ((amountChars[1])==('0') && (amountChars[2])==('0')) {
            //101
            fileIndexs.add(0);
            money_1(fileIndexs, Integer.parseInt(amountChars[2] + ""));
        } else {
            money_2(fileIndexs, "" + amountChars[1] + "" + amountChars[2]);
        }

    }

    /**
     * 两位数
     */
    private void money_2(ArrayList<Integer> fileIndexs, String twoNum) {

        char[] amountChars = twoNum.toCharArray();
        int firstNum = Integer.parseInt((amountChars[0] + ""));
//        if (firstNum!=1){
            money_1(fileIndexs, Integer.parseInt((amountChars[0] + "")));
//        }
        fileIndexs.add(18);
        if ((amountChars[1])!=('0')) {
            money_1(fileIndexs, Integer.parseInt((amountChars[1] + "")));
        }


    }

    /**
     * 读单个数字
     */
    private void money_1(ArrayList<Integer> fileIndexs, int singleNum) {
        fileIndexs.add(singleNum);
    }

    int tempIndex = 0;

    private void initBeepSound(final ArrayList<Integer> fileIndex) {
        int fileIndexLength = fileIndex.size();
        Log.e(TAG, "initBeepSound: fileIndexLength :" + fileIndexLength);
        if (tempIndex > fileIndexLength - 1) {
            tv_pay.setEnabled(true);
            return;
        }
        int fileIndexNum = fileIndex.get(tempIndex);
        tempIndex++;

        AssetFileDescriptor file = fileLists.get(fileIndexNum);
//        if (mediaPlayer == null) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                initSoundData();
                mediaPlayer.release();
                mediaPlayer = null;
                initBeepSound(fileIndex);
                Log.e(TAG, "onCompletion:mediaPlayer: " + mediaPlayer);
            }
        });

//            AssetFileDescriptor file = null;
//            try {
//                file = getAssets().openFd("tts_0.mp3");
//            } catch (IOException e) {
//
//            }
//        AssetFileDescriptor file = getResources().openRawResourceFd(rawId);
        Log.e(TAG, "initBeepSound111: " + file.getFileDescriptor());
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());

            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
        } catch (IOException e) {
            mediaPlayer = null;
        }
//        }
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


    private void etListener() {
        //设置按钮不能点击
        tv_pay.setEnabled(false);
        et_chongmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTempText = s;//输入的数字
                UserUtil.Log(TAG, "onTextChanged: " + s);


                if (TextUtils.isEmpty(s)) {
                    tv_pay.setEnabled(false);
                } else {
                    int money = Integer.parseInt(s.toString());

                    if (money == 0) {
                        tv_pay.setEnabled(false);
                    } else if (money > limitAmount) {
                        tv_pay.setEnabled(false);
                        UserUtil.showToastCenter(Activity_1_2.this, "单笔金额不能超过" + limitAmount + "元");
                    } else if (money < minAmount) {
                        tv_pay.setEnabled(false);
                        UserUtil.showToastCenter(Activity_1_2.this, "单笔金额不能小于" + minAmount + "元");
                    } else {
                        amount = money + "";
                        Log.e(TAG, "onTextChanged:amount : " + amount);
                        tv_pay.setEnabled(true);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                int mSelectionStart = et_chongmoney.getSelectionStart();
                int mSelectionEnd = et_chongmoney.getSelectionEnd();
                String s1 = mTempText.toString();

                if (s1.length() > 10) {
                    s.delete(mSelectionStart - 1, mSelectionEnd);
                }
            }
        });
    }


    private static final String TAG = "Activity_1_2";
}
