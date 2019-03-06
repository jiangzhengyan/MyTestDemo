package com.jingcaiwang.mytestdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       //把整张画布绘制成白色
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        //去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        int viewWidth = this.getWidth();
        //绘制圆形
        canvas.drawCircle(viewWidth/10+10,viewWidth/10+10,viewWidth/10,paint);

        //绘制正方形 边长为 viewWidth/5
        canvas.drawRect(10,viewWidth/5+20,viewWidth/5+10,viewWidth*2/5+20,paint);
        //绘制矩形
        canvas.drawRect(10,viewWidth*2/5+30,viewWidth/5+10,viewWidth/2+30,paint);
        RectF rel = new RectF(10, viewWidth / 2 + 40, 10 + viewWidth / 5, viewWidth * 3 / 5 + 40);
        //绘制圆角矩形
        canvas.drawRoundRect(rel,15,15,paint);
        RectF rell = new RectF(10, viewWidth * 3 / 5 + 50, 10 + viewWidth / 5, viewWidth * 7 / 10 + 50);
        //绘制椭圆
        canvas.drawOval(rell,paint);
        //定义一个path对象,封闭成一个三角形
        Path path1 = new Path();
        path1.moveTo(10,viewWidth*9/10+60);
        path1.lineTo(viewWidth/5+10,viewWidth*9/10+60);
         path1.lineTo(viewWidth/10+10,viewWidth*7/10+60);
        path1.close();
        //根据path进行绘制,绘制三角形
        canvas.drawPath(path1,paint);
        //填充
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        //绘制圆形
        canvas.drawCircle(viewWidth*3/10+20,viewWidth/10+10,viewWidth/10,paint);

        //为画笔设置渐变
        LinearGradient mShader = new LinearGradient(0, 0, 40, 60, new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, null, Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        //设置阴影
        paint.setShadowLayer(25,20,20,Color.RED);
        //绘制圆形
        canvas.drawCircle(viewWidth/2+30,viewWidth/10+10,viewWidth/10,paint);

        //设置字符大小后绘制
        paint.setTextSize(48);
        paint.setShader(null);
        canvas.drawText("圆形",30+viewWidth*3/5,viewWidth/10+10,paint);



    }
}
