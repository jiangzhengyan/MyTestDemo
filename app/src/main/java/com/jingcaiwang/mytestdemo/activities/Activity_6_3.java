package com.jingcaiwang.mytestdemo.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jingcaiwang.mytestdemo.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_6_3 extends AppCompatActivity {

    GameView gameView;
    //桌面宽度
    private int tableWidth;
    //桌面高度
    private int tableHeight;
    //球拍的垂直位置
    private int racketY;
    //球拍的高宽
    private final int RACKET_HEIGHT = 30;
    private final int RACKET_WIDTH = 90;
    //小球的大小
    private final int BALL_SIZE = 16;
    //小球纵向的运动速度
    private int ySpeed = 15;
    Random rand = new Random();
    //返回一个 -0.5~0.5的比率,控制小球运动方向
    private double xyRate = rand.nextDouble() - 0.5;
    //小球横向运动速度
    private int xSpeed = (int) (ySpeed * xyRate * 2);
    //ballx和bally代表小球坐标
    private int ballX = rand.nextInt(200) + 20;
    private int ballY = rand.nextInt(10) + 20;
    //racketX代表球的水平位置
    private int racketX = rand.nextInt(200);
    //佑熙是否结束的旗标
    private boolean isLose = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

          gameView = new GameView(this);
        setContentView(gameView);
        ButterKnife.bind(this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        //屏幕


        tableWidth = metrics.widthPixels;
        tableHeight = metrics.heightPixels;

        racketY = tableHeight - 80;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    Activity_6_3.this.gameView.invalidate();
                }
            }
        };
        this.gameView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getKeyCode()) {

                    case KeyEvent.KEYCODE_A:
                        if (racketX > 0) {
                            racketX -= 0;
                        }
                        break;
                    case KeyEvent.KEYCODE_D:
                        if (racketX < tableWidth - RACKET_WIDTH) {
                            racketX += 10;
                        }
                        break;

                }
                Activity_6_3.this.gameView.invalidate();
                return true;
            }
        });
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //小球碰到边
                if (ballX<0||ballX>=tableWidth-BALL_SIZE){
                    xSpeed=-xSpeed;
                }
                //失败
                if (ballY>=racketY-BALL_SIZE&&(ballX<racketX||ballX>racketX+RACKET_WIDTH)){
                    timer.cancel();
                    isLose=true;
                }else if (ballY<=0||(ballY>=racketY-BALL_SIZE&&ballX>racketX&&ballX<=racketX+RACKET_WIDTH)){
                    ySpeed=-ySpeed;
                }

                ballY+=ySpeed;
                ballX+=xSpeed;
                handler.sendEmptyMessage(0x123);

            }
        },0,100);


    }


    class GameView extends View {
        Paint paint = new Paint();


        public GameView(Context context) {
            super(context);
            setFocusable(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            if (isLose) {
                paint.setColor(Color.RED);
                paint.setTextSize(40);
                canvas.drawText("游戏结束", tableWidth / 2 - 100, 200, paint);
            } else {
                paint.setColor(Color.rgb(255, 0, 0));
                canvas.drawCircle(ballX, ballY, BALL_SIZE, paint);
                paint.setColor(Color.rgb(80, 80, 200));
                canvas.drawRect(racketX, racketY, racketX + RACKET_WIDTH, racketY + RACKET_HEIGHT, paint);
            }
        }
    }

}

