package com.jingcaiwang.mytestdemo.utils;

import android.os.Process;
import android.util.Log;
import android.widget.TextView;

public class Lambdatest {

    public Lambdatest() {

        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();

    }

    private static final String TAG = "Lambdatest";

    private void test1() {

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread(() -> {
            Log.e(TAG, "test1: java8 之后");
        }).start();
    }

    private void test2() {

    }

    private void test3() {

    }

    private void test4() {

    }

    private void test5() {

    }

    private void test6() {

    }

    private void test7() {

    }

    private void test8() {

    }
    private void setOntest(OntestCallBack s){}

    public interface  OntestCallBack{
        void first( );
        void first1();
    }

}
