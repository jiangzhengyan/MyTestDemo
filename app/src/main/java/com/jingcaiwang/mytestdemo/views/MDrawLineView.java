package com.jingcaiwang.mytestdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MDrawLineView extends View {

    public MDrawLineView(Context context){
        super(context);
    }
    public MDrawLineView(Context context,AttributeSet attrs){
        super(context, attrs);
        paint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
        if(bitmap==null){
            bitmap = Bitmap.createBitmap(900, 1200, Bitmap.Config.ARGB_8888); //设置位图的宽高
        }
        canvas=new Canvas();
        canvas.setBitmap(bitmap);
       // paint.setStyle(Paint.Style.STROKE);//设置非填充
        paint.setStyle(Paint.Style.STROKE);//设置非填充
        paint.setStrokeWidth(10);//笔宽5像素
        paint.setColor(Color.RED);//设置为红笔
        paint.setAntiAlias(true);//锯齿不显示




       // mPaint = new Paint();
       // mPaint.setAntiAlias(true);
        paint.setDither(true);
       // mPaint.setColor(0xFF000000);
        //mPaint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //paint.setStrokeWidth(10);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap==null){
            bitmap = Bitmap.createBitmap(900, 1200, Bitmap.Config.ARGB_8888); //设置位图的宽高
        }
        canvas.drawBitmap(bitmap,0,0,null);
    }
    public void clear(){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE: //用户手指在屏幕上移动画线

               canvas.drawLine(mov_x-1,mov_y-1,event.getX()+1,event.getY()+1,paint);
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN://用户手指按下时画起点
                mov_x=(int) event.getX();
                mov_y=(int) event.getY();
                canvas.drawPoint(mov_x,mov_y,paint);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mov_x=(int) event.getX();
        mov_y=(int) event.getY();
        return true;
        //return super.onTouchEvent(event);
    }
    private int mov_x;//声明起点x坐标
    private int mov_y;//声明起点y坐标
    private Paint paint;//声明画笔
    private Canvas canvas;//画布
    private Bitmap bitmap;//位图
    private int blcolor;

}
