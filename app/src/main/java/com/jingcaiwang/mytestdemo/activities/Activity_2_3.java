package com.jingcaiwang.mytestdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.application.MyApplication;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_2_3 extends AppCompatActivity {

    @Bind(R.id.iv_pic)
    ImageView mIvPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_3);
        ButterKnife.bind(this);
    }

    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 1;

    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;

    private void takePhoto() {
        // 跳转到系统的拍照界面
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量

//        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "aaaaaa" + File.separator + System.currentTimeMillis() + File.separator + "photo.jpeg";
        //mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "aaaaaa" + File.separator + System.currentTimeMillis() +   "photo.jpeg";
        mTempPhotoPath =  getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + System.currentTimeMillis() +   "photo.jpeg";
       // mTempPhotoPath = getExternalCacheDir().getAbsolutePath() + File.separator + "aaaaaa" + File.separator + System.currentTimeMillis() +   "photo.jpg";
        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
        //imageUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.provider", new File(mTempPhotoPath));
        //下面这句指定调用相机拍照后的照片存储的路径
//        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intentToTakePhoto, 999);
        File file = new File(mTempPhotoPath);
        try {

//            if (!file.exists()) {
//                file.createNewFile();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.jingcaiwang.mytestdemo.fileprovider", file);
           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
        }
        startActivityForResult(intent, 111);
    }

    @OnClick(R.id.tv_open)
    public void onViewClicked() {

        takePhoto();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + mTempPhotoPath);
       // Bitmap bitmap = BitmapFactory.decodeFile("file://" + mTempPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(  mTempPhotoPath);
        Toast.makeText(this, "" + mTempPhotoPath + "-----" + bitmap + "  " + new File(mTempPhotoPath).exists(), Toast.LENGTH_LONG).show();


       Glide.with(this).load(mTempPhotoPath).into(mIvPic);
       // Glide.with(this).load(imageUri).into(mIvPic);

    }

    private static final String TAG = "Activity_2_3";
}
