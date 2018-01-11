package com.jingcaiwang.mytestdemo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jingcaiwang.mytestdemo.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private EditText et_prop_add_content;
    private ImageView iv;


    private CharSequence mTempText;//投诉建议的内容
    private int mInputNumLimit = 4;//投诉字数限制
    private boolean canClean = false;
    private float x;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dddddd);
        iv = (ImageView) findViewById(R.id.iv);

        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                float pivotX = iv.getPivotX();
                x = iv.getX();
                y = iv.getY();
                Log.e(TAG, "onGlobalLayout: pivotX : "+pivotX+"  x :"+ x);
                iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                move();
                return true;
            }
        });

    }

    private void move() {

        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch: down" );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch: move"+ event.getX() );
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)iv.getLayoutParams();
                        params.leftMargin= (int) event.getX();
                        iv .setLayoutParams(params);

                        iv.requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        iv.setOnTouchListener(null);
                        break;
                }
                return false;
            }
        });
    }

    private void editContent() {
        //设置默认剩余字数
        et_prop_add_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {
                Log.e(TAG, "beforeTextChanged: " + et_prop_add_content.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mTempText = s;
                Log.e(TAG, "onTextChanged: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: " + s.toString());
                int mSelectionStart = et_prop_add_content.getSelectionStart();
                int mSelectionEnd = et_prop_add_content.getSelectionEnd();
                Log.e(TAG, "afterTextChanged mSelectionStart: " + mSelectionStart);
                Log.e(TAG, "afterTextChanged mSelectionEnd: " + mSelectionEnd);
                if (mTempText.length() > mInputNumLimit) {
                    s.delete(mSelectionStart - 1, mSelectionEnd);
                    int tempSelection = mSelectionStart;
                    et_prop_add_content.setText(s);
                    et_prop_add_content.setSelection(mInputNumLimit);// 设置光标在最后

                    Log.e(TAG, "afterTextChanged:  超");
                    Toast.makeText(TestActivity.this, "字数朝鲜", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}
