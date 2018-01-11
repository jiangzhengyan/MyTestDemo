package com.jingcaiwang.mytestdemo.utils;

/**
 * Created by jiang_yan on 2017/9/29.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.jingcaiwang.mytestdemo.application.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class ImageUtils {


    //根据InputStream获取图片实际的宽度和高度

    public static ImageSize getImageSize(InputStream imageStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize {
        int width, height;

        public ImageSize() {

        }

        public ImageSize(int size, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageSize{"
                    + "width=" + width +
                    ", height=" + height + "}";
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
        //源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;
        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;
        if (width > reqWidth && height > reqHeight) {
            //计算出实际的宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    //计算合适的inSampleSize
    public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ImageView imageView) {
        final int srcWidth = srcSize.width;
        final int srcHeight = srcSize.height;
        final int targetWidth = targetSize.width;
        final int targetHeight = targetSize.height;

        int scale = 1;
        if (imageView == null) {
            scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight);
        } else {
            switch (imageView.getScaleType()) {
                case FIT_CENTER:
                case FIT_XY:
                case FIT_START:
                case FIT_END:
                case CENTER_INSIDE:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight);
                    break;
                case CENTER:
                case CENTER_CROP:
                case MATRIX:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight);
                    break;
                default:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight);
                    break;
            }
        }

        if (scale < 1) {
            scale = 1;
        }

        return scale;
    }

    //根据ImageView获取适当的压缩的宽和高
    public static ImageSize getImageViewSize(View view) {
        ImageSize imageSize = new ImageSize();
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    private static int getExpectHeight(View view) {
        int height = 0;
        if (view == null) {
            return 0;
        }
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth()无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = view.getWidth();
        }
        if (height <= 0 && params != null) {
            //获得布局文件中的声明宽度
            height = params.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");
        }

        //如果宽度还没有获取到，使用屏幕的宽度
        if (height <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }
        return height;
    }

    //根据view获得期望的高度
    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null) {
            return 0;
        }
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = view.getWidth();
        }
        if (width <= 0 && params != null) {
            width = params.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(view, "mMaxWidth");
        }
        if (width <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }
        return width;
    }

    //通过反射获取imageview的某个属性值
    private static int getImageViewFieldValue(Object object, String filedName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(filedName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {

        }
        return value;
    }

    //压缩后返回
    public static Bitmap getImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;
        float w = 720f;
        float h = 1080f;
        int size = 1;
        if (width > height && width > w) {
            size = (int) (options.outWidth / w);
        } else {
            size = (int) (options.outHeight / h);
        }
        if (size <= 0) {
            size = 1;
        }
        options.inSampleSize = size;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap != null) {

        }
        return compressImage(bitmap, 80);
    }

    //好像只有Bitmap.Config.RGB_565有效
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 80f;//这里设置高度为800f
        float ww = 48f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    /**
     * @param bitmap    要质量压缩的bitmap
     * @param imageSize 单位kb
     *                  压缩之后内存大小不变,质量发生变化
     * @return
     */
    public static Bitmap compressImage(Bitmap bitmap, long imageSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > imageSize) {
            Log.d("ImageUtils", "compress_one");
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
            if (options <= 0) {
                break;
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap1 = BitmapFactory.decodeStream(bais, null, null);
        return bitmap1;
    }

    private static int mDesiredWidth;
    private static int mDesiredHeight;

    /**
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     * @description 从Resources中加载图片
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置成了true,不占用内存，只获取bitmap宽高
        options.inJustDecodeBounds = true;
        // 初始化options对象
        BitmapFactory.decodeResource(res, resId, options);
        // 得到计算好的options，目标宽、目标高
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight); // 进一步得到目标大小的缩略图
    }

    /**
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     * @description 从SD卡上加载图片, 对文件图片压缩有效果
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight);
    }

//    /**
//     * 对图片文件进行再压缩
//     * @param context
//     * @param pathName
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    public static String compressBitmapFromFile(Context context, String pathName, int reqWidth, int reqHeight) {
//        Bitmap bitmap = decodeSampledBitmapFromFile(pathName, reqWidth, reqHeight);
//        String fileName = StringUtil.getFileName(pathName);
//        return saveImage(context, bitmap, fileName);
//    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return BitmapFactory.Options对象
     * @description 计算目标宽度，目标高度，inSampleSize
     */
    private static BitmapFactory.Options getBestOptions(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 读取图片长宽
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        // Then compute the dimensions we would ideally like to decode to.
        mDesiredWidth = getResizedDimension(reqWidth, reqHeight, actualWidth, actualHeight);
        mDesiredHeight = getResizedDimension(reqHeight, reqWidth, actualHeight, actualWidth);
        // 根据现在得到计算inSampleSize
        options.inSampleSize = calculateBestInSampleSize(actualWidth, actualHeight, mDesiredWidth, mDesiredHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * Scales one side of a rectangle to fit aspect ratio. 最终得到重新测量的尺寸
     *
     * @param maxPrimary      Maximum size of the primary dimension (i.e. width for max
     *                        width), or zero to maintain aspect ratio with secondary
     *                        dimension
     * @param maxSecondary    Maximum size of the secondary dimension, or zero to maintain
     *                        aspect ratio with primary dimension
     * @param actualPrimary   Actual size of the primary dimension
     * @param actualSecondary Actual size of the secondary dimension
     */
    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    // Visible for testing.
    private static int calculateBestInSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float inSampleSize = 1.0f;
        while ((inSampleSize * 2) <= ratio) {
            inSampleSize *= 2;
        }

        return (int) inSampleSize;
    }

    /**
     * @return
     * @description 通过传入的bitmap，进行压缩，得到符合标准的bitmap
     */
    private static Bitmap createScaleBitmap(Bitmap tempBitmap, int desiredWidth, int desiredHeight) {
        // If necessary, scale down to the maximal acceptable size.
        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
            // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
            Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
            tempBitmap.recycle(); // 释放Bitmap的native像素数组
            return bitmap;
        } else {
            return tempBitmap; // 如果没有缩放，那么不回收
        }
    }

    public static String saveImage(byte[] data, String fileName) {

        String dirPath = MyApplication.getInstance().getFilesDir().getAbsolutePath() + File.separator + "uploadimage";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try {
            //存图片大图
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static String saveImage(Context context, Bitmap bitmap, String fileName) {
        File externalFilesDir = MyApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            } else {
                externalFilesDir = MyApplication.getInstance().getFilesDir();
            }
        }
        String dirPath = externalFilesDir.getAbsolutePath() + File.separator + "uploadimage";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try {
            //存图片大图
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void sendSaveSuccessBroadcast(Context context, File file) {

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(intent);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent1 = new Intent();
            // 重新挂载的动作
            intent1.setAction(Intent.ACTION_MEDIA_MOUNTED);
            // 要重新挂载的路径
            intent1.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            context.sendBroadcast(intent1);
        }

    }

    public static Bitmap compressBitmap(String filePath, int smallRate) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, opt);

            // 获取到这个图片的原始宽度和高度
            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;

            //读取图片失败时直接返回
            if (picWidth == 0 || picHeight == 0) {
                return null;
            }

            //初始压缩比例
            opt.inSampleSize = smallRate;
            // 根据屏的大小和图片大小计算出缩放比例
            if (picWidth > picHeight) {
                if (picWidth > ScreenUtils.getScreenW())
                    opt.inSampleSize *= picWidth / ScreenUtils.getScreenW();
            } else {
                if (picHeight > ScreenUtils.getScreenH())
                    opt.inSampleSize *= picHeight / ScreenUtils.getScreenH();
            }

            //这次再真正地生成一个有像素的，经过缩放了的bitmap
            opt.inJustDecodeBounds = false;
            final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

            return bmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ImageRotate {

        private float hasRotateDegree;

        public void rotateImage(final ImageView imageView, final int degree, final boolean isShun, boolean isNeeRotate) {

            if (imageView == null) return;
            hasRotateDegree = 0;
            final Matrix matrix = new Matrix();
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            if (isNeeRotate) {
                matrix.postRotate(180, imageView.getWidth() / 2, imageView.getHeight() / 2);
                imageView.setImageMatrix(matrix);
            }
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float curTimePercent = animation.getCurrentPlayTime() / ((float) animation.getDuration());
                    float curYPercent = ((Float) animation.getAnimatedValue()).floatValue();
                    rotate(curTimePercent, curYPercent, imageView, degree, isShun, matrix);
                }
            });
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }

        private void rotate(float curTimePercent, float curYPercent, ImageView imageView, float degree, boolean isShun, Matrix matrix) {
            if (!isShun) {
                degree = -degree;
            }
            float shouldRotate = curYPercent * degree - hasRotateDegree;
            matrix.postRotate(shouldRotate, imageView.getWidth() / 2, imageView.getHeight() / 2);
            Log.i("rotate:", hasRotateDegree + shouldRotate + "");
            imageView.setImageMatrix(matrix);
            hasRotateDegree = hasRotateDegree + shouldRotate;
        }
    }

    public static void rotate180(ImageView imageView) {
        final Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate(180, imageView.getWidth() / 2, imageView.getHeight() / 2);
        imageView.setImageMatrix(matrix);
    }

    public static void rotateImageAlways(ImageView ivLoading) {

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, ivLoading.getLayoutParams().height / 2, ivLoading.getLayoutParams().width / 2);
        rotateAnimation.setDuration(700);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        ivLoading.startAnimation(rotateAnimation);
    }


    private static final class DisplayNextView implements Animation.AnimationListener {

        TagView tagView;

        private DisplayNextView(TagView tagView) {
            this.tagView = tagView;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
//            Rotate3dAnimation rotation = new Rotate3dAnimation(0, 180, llPhotoInfo.getLeft() + llPhotoInfo.getWidth() / 2, llPhotoInfo.getHeight() / 2, 310.0f, false);
//            rotation.setDuration(0);
//            rotation.setFillAfter(true);
//            llPhotoInfo.startAnimation(rotation);
            tagView.curTag++;
            if (tagView.curTag == tagView.tagViews.size()) tagView.curTag = 0;
            tagView.targetView.removeAllViews();
            tagView.targetView.addView(tagView.tagViews.get(tagView.curTag));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }


    static class TagView {

        private List<View> tagViews;
        private int curTag = 0;
        private ViewGroup targetView;

        public TagView(List<View> tagViews, int curTag, ViewGroup targetView) {
            this.tagViews = tagViews;
            this.curTag = curTag;
            this.targetView = targetView;
        }
    }


}
