package com.jingcaiwang.mytestdemo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.MyVoiceUtil;

import java.io.File;

public class Activity_3_2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_2);

        MyVoiceUtil myVoiceUtil = new MyVoiceUtil(this, this);
       // String pathStr = "/Android" + File.separator + "data" + File.separator + "com.aaas" + File.separator + "cache" + File.separator;
        String pathStr = Environment.getExternalStorageDirectory().getPath() + File.separator + "cache" + File.separator+"myvoice";
        File file = new File(pathStr);
        myVoiceUtil.initVoiceSetting(4,file);
        File voiceFile = myVoiceUtil.getVoiceFile();



    }


}
