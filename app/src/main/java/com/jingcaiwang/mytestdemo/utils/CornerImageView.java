package com.jingcaiwang.mytestdemo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.jingcaiwang.mytestdemo.R;

public class CornerImageView extends android.support.v7.widget.AppCompatImageView {
    private int cornerSize = 0;
    private Paint paint;
    private int imageTopLeftRadius = -1;
    private int imageTopRightRadius = -1;
    private int imageBottomLeftRadius = -1;
    private int imageBottomRightRadius = -1;

    public CornerImageView(Context context) {
        super(context);
        initPaint();

    }


    public CornerImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

        initPaint();


    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView, defStyleAttr, 0);

        cornerSize = typedArray.getDimensionPixelSize(R.styleable.CornerImageView_CornerImageRadius, cornerSize);

        imageTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.CornerImageView_CornerImageTopLeftRadius, imageTopLeftRadius);
        imageTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.CornerImageView_CornerImageTopRightRadius, imageTopRightRadius);
        imageBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.CornerImageView_CornerImageBottomLeftRadius, imageBottomLeftRadius);
        imageBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.CornerImageView_CornerImageBottomRightRadius, imageBottomRightRadius);


        typedArray.recycle();

        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);//消除锯齿
    }

    public void setRadius(int radius) {
        this.cornerSize = radius;
        invalidate();
    }

    public void setRadius(int imageTopLeftRadius, int imageTopRightRadius, int imageBottomLeftRadius, int imageBottomRightRadius) {
        this.imageTopLeftRadius = imageTopLeftRadius;
        this.imageTopRightRadius = imageTopRightRadius;
        this.imageBottomLeftRadius = imageBottomLeftRadius;
        this.imageBottomRightRadius = imageBottomRightRadius;
        invalidate();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (imageTopLeftRadius <= 0) {
            if (cornerSize <= 0) {
                drawLeftTop(canvas, 0);
            } else {
                drawLeftTop(canvas, cornerSize);
            }
        } else {
            drawLeftTop(canvas, imageTopLeftRadius);
        }
        if (imageTopRightRadius <= 0) {
            if (cornerSize <= 0) {
                drawRightTop(canvas, 0);
            } else {
                drawRightTop(canvas, cornerSize);
            }
        } else {
            drawRightTop(canvas, imageTopRightRadius);
        }
        if (imageBottomLeftRadius <= 0) {
            if (cornerSize <= 0) {
                drawLeftBottom(canvas, 0);
            } else {
                drawLeftBottom(canvas, cornerSize);
            }
        } else {
            drawLeftBottom(canvas, imageBottomLeftRadius);
        }
        if (imageBottomRightRadius <= 0) {
            if (cornerSize <= 0) {
                drawRightBottom(canvas, 0);
            } else {
                drawRightBottom(canvas, cornerSize);
            }
        } else {
            drawRightBottom(canvas, imageBottomRightRadius);
        }

    }

    private void drawLeftTop(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(0, cornerSize);
        path.lineTo(0, 0);
        path.lineTo(cornerSize, 0);
        path.arcTo(new RectF(0, 0, cornerSize * 2, cornerSize * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLeftBottom(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(0, getHeight() - cornerSize);
        path.lineTo(0, getHeight());
        path.lineTo(cornerSize, getHeight());

        RectF rectF = new RectF(0, getHeight() - cornerSize * 2, cornerSize * 2, getHeight());
        path.arcTo(new RectF(rectF), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightBottom(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(getWidth() - cornerSize, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - cornerSize);
        RectF oval = new RectF(getWidth() - cornerSize * 2, getHeight() - cornerSize * 2, getWidth(), getHeight());
        path.arcTo(oval, 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightTop(Canvas canvas, int cornerSize) {
        Path path = new Path();
        path.moveTo(getWidth(), cornerSize);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - cornerSize, 0);
        path.arcTo(new RectF(getWidth() - cornerSize * 2, 0, getWidth(), 0 + cornerSize * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    public int getCornerSize() {
        return cornerSize;
    }

    public void setCornerSize(int cornerSize) {
        this.cornerSize = cornerSize;
    }

}
