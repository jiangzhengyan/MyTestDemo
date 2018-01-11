package com.jingcaiwang.mytestdemo.network;

/**
 * Created by jiang_yan on 2017/9/28.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.commit451.nativestackblur.NativeStackBlur;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;
import com.jingcaiwang.mytestdemo.application.MyApplication;
import com.jingcaiwang.mytestdemo.beans.BaseBean;
import com.jingcaiwang.mytestdemo.conf.AppConf;
import com.jingcaiwang.mytestdemo.utils.ImageUtils;
import com.jingcaiwang.mytestdemo.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

public class OKHttpManager {

    private static final String TAG = "OKHttpManager";

    private static OKHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
//    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("text/plain");

    private HttpsDelegate mHttpsDelegate = new HttpsDelegate();
    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
    private DisplayImageDelegate mDisplayImageDelegate = new DisplayImageDelegate();
    private DisplayBlurImageDelegate mDisplayBlurImageDelegate = new DisplayBlurImageDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();
    private PostDelegate mPostDelegate = new PostDelegate();

    private OKHttpManager() {
        //enable cookie
//    mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient = MyApplication.getOkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OKHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpManager();
                }
            }
        }
        return mInstance;
    }

    public GetDelegate getGetDelegate() {
        return mGetDelegate;
    }

    public PostDelegate getPostDelegate() {
        return mPostDelegate;
    }

    private HttpsDelegate _getHttpsDelegate() {
        return mHttpsDelegate;
    }

    private DownloadDelegate _getDownloadDelegate() {
        return mDownloadDelegate;
    }

    private DisplayImageDelegate _getDisplayImageDelegate() {
        return mDisplayImageDelegate;
    }

    private DisplayBlurImageDelegate _getDisplayBlurImageDelegate() {
        return mDisplayBlurImageDelegate;
    }


    public static DisplayImageDelegate getDisplayImageDelegate() {
        return getInstance()._getDisplayImageDelegate();
    }

    public static DisplayBlurImageDelegate getDisplayBlurImageDelegate() {
        return getInstance()._getDisplayBlurImageDelegate();
    }

    public static DownloadDelegate getDownloadDelegate() {
        return getInstance()._getDownloadDelegate();
    }

    public static HttpsDelegate getHttpsDelegate() {
        return getInstance()._getHttpsDelegate();
    }


    //GET
    public static void getAsyn(String url, ResultCallback callback) {
        getInstance().getGetDelegate().getAsyn(url, callback, null);
    }

    public static void getAsyn(String url, ResultCallback callback, Object tag) {
        getInstance().getGetDelegate().getAsyn(url, callback, tag);
    }


    /**
     * //post 请求网络数据
     *
     * @param url
     * @param map
     * @param callback
     * @param tag
     */
    public static void postAsyn(String url, HashMap map, final ResultCallback callback, Object tag) {
        if (url == null) {
            return;
        }
        getInstance().getPostDelegate().postAsyn(url, map, callback, tag);
    }

    /**
     * //post上传图片
     *
     * @param url
     * @param map
     * @param callback
     */
    public static void postAsyn(String url, HashMap map, String mFilePartKeyName, List<File> imagePathFiles, final ResultCallback callback) {
        if (url == null) {
            return;
        }
        getInstance().getPostDelegate().postAsyn(url, map, mFilePartKeyName, imagePathFiles, callback, null);
    }


//    public static void postAsyn(String url, String bodyStr, final ResultCallback callback) {
//        getInstance().getPostDelegate().postAsyn(url, bodyStr, callback, null);
//    }
//
//    public static void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {
//        getInstance().getPostDelegate().postAsyn(url, params, callback, tag);
//    }

//    public static void postAsyn(String url, HashMap<String, String> params, final ResultCallback callback, Object tag) {
//        getInstance().getPostDelegate().postAsyn(url, params, callback, tag);
//    }
//
//    public static void postAsyn(String url, String bodyStr, final ResultCallback callback, Object tag) {
//        getInstance().getPostDelegate().postAsyn(url, bodyStr, callback, tag);
//    }


    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null) {
            return new Param[0];
        } else {
            return params;
        }
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) {
            return new Param[0];
        }
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }


    private void deliveryResult(ResultCallback callback, final Request request) {
        if (callback == null) {
            callback = DEFAULT_RESULT_CALLBACK;
        }

        final ResultCallback resultCallback = callback;

        callback.onBefore(request);

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //  网络请求失败
                        sendFailedStringCallback(request, e, AppConf.NET_FAIL_MSG, resultCallback);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String result = response.body().string();
                            Log.e(TAG, "onResponse: " + result);
                            if (resultCallback.mType == String.class) {
                                sendSuccessResultCallback(result, resultCallback);
                            } else if (resultCallback.mType == BaseBean.class) {
                                BaseBean baseBean = com.alibaba.fastjson.JSONObject.parseObject(result, BaseBean.class);
                                if ("1".equals(baseBean.getSuccess())) {
                                    sendSuccessResultCallback(baseBean, resultCallback);
                                } else if ("0".equals(baseBean.getSuccess())) {
                                    sendFailedStringCallback(response.request(), new Exception(), baseBean.getErrorMsg(), resultCallback);
                                } else if ("999999".equals(baseBean.getSuccess())) {
                                    sendFailedStringCallback(response.request(), new Exception(), AppConf.NET_SERVICE_ERR_MSG, resultCallback);
                                }
                            } else {
                                Object object = com.alibaba.fastjson.JSONObject.parseObject(result, resultCallback.mType);
                                sendSuccessResultCallback(object, resultCallback);
                            }
                        } catch (IOException e) {
                            sendFailedStringCallback(response.request(), e, AppConf.NET_FAIL_MSG, resultCallback);
                        } catch (JsonParseException e) {
                            sendFailedStringCallback(response.request(), e, AppConf.NET_DAATA_PARSE_ERR_MSG, resultCallback);
                        }
                    }
                });

    }


    private void sendFailedStringCallback(final Request request, final Exception e, final String msg, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e, msg);
                callback.onAfter();
                Log.e(TAG, "onError: " + request.body().toString());
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    private String getFileName(String path) {

        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

//    private BufferedSink bufferedSink;

    private Sink sink(final Sink sink, final RequestBody body) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度,避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获取contentLength,之后不再调用
                    contentLength = body.contentLength();
                }
                bytesWritten += byteCount;

            }
        };
    }


    private Request buildPostFormBodyRequestWithHashMap(String url, HashMap map, Object tag) {
        if (map == null) {
            return null;
        }
        FormBody.Builder builder = new FormBody.Builder();
        Log.e(TAG, "url: " + url);
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            builder.add(entry.getKey(), entry.getValue());
            Log.e(TAG, entry.getKey() + " : " + entry.getValue());

        }
//        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, map + "");
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url).post(builder.build());
        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    /**
     * //上传图片
     *
     * @param url
     * @param map
     * @param mFilePartKeyName
     * @param imagePathFiles
     * @param callback
     * @param tag
     * @return
     */
    private Request buildPostFormBodyRequestWithHashMapAndListFiles(String url, HashMap map, String mFilePartKeyName, List<File> imagePathFiles, final ResultCallback callback, Object tag) {
        if (map == null) {
            return null;
        }

        MultipartBody.Builder builder_ = new MultipartBody.Builder();
        builder_.setType(MultipartBody.FORM);
        Log.e(TAG, "url:" + url);
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            builder_.addFormDataPart(entry.getKey(), entry.getValue());
            Log.e(TAG, entry.getKey() + " : " + entry.getValue());
        }

        Log.e(TAG, "mFilePartKeyName : " + mFilePartKeyName);
        for (File file_ : imagePathFiles) {
            builder_.addFormDataPart(mFilePartKeyName, file_.getName(), RequestBody.create(MediaType.parse("image/png"), file_));
            Log.e(TAG, ":" + file_.getAbsolutePath());
        }
        MultipartBody requestBody = builder_.build();
        Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }


    private Request buildPostJSONRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        JSONObject json = null;
        try {
            json = new JSONObject();
            for (Param param : params) {
                json.put(param.key, param.value);
            }
        } catch (JSONException e) {

        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json.toString());
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url).post(body);
        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }


    public OkHttpClient client() {
        return mOkHttpClient;
    }

    public static OkHttpClient getClient() {
        return getInstance().client();
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter..");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onBefore(Request request) {

        }

        public void onAfter() {

        }

        public abstract void onError(Request request, Exception e, String msg);

        public abstract void onResponse(T response);
    }

    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e, String msg) {

        }

        @Override
        public void onResponse(String response) {

        }
    };

    public static class Param {
        String key, value;

        public Param() {

        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }


    //PostDelegate
    public class PostDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
        private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

        public Response post(String url, Param[] params) throws IOException {
            return post(url, params, null);
        }

        // 同步的post请求
        public Response post(String url, Param[] params, Object tag) throws IOException {
            Request request = buildPostJSONRequest(url, params, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

//        public String postAsString(String url, Param[] params) throws IOException {
//            return postAsString(url, params, null);
//        }

        public String postAsString(String url, Param[] params, Object tag) throws IOException {
            Response response = post(url, params, tag);
            return response.body().string();
        }


//        public void postAsyn(String url, Param[] params, final ResultCallback callback) {
//            postAsyn(url, params, callback, null);
//        }

//        public void postAsyn(String url, Param[] params, ParamLongArray[] paramsLongArray, final ResultCallback callback) {
//            postAsyn(url, params, paramsLongArray, callback, null);
//        }

        //异步post请求
        public void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {
            Request request = buildPostJSONRequest(url, params, tag);
            deliveryResult(callback, request);
        }

        /**
         * //上传图片
         *
         * @param url
         * @param map
         * @param mFilePartKeyName 图片集合的键的名字
         * @param imagePathFiles
         * @param callback
         * @param tag
         */
        public void postAsyn(String url, HashMap map, String mFilePartKeyName, List<File> imagePathFiles, final ResultCallback callback, Object tag) {

            Request request = buildPostFormBodyRequestWithHashMapAndListFiles(url, map, mFilePartKeyName, imagePathFiles, callback, tag);
            deliveryResult(callback, request);
        }

        /**
         * 普通请求网络
         *
         * @param url
         * @param map
         * @param callback
         * @param tag
         */
        public void postAsyn(String url, HashMap map, final ResultCallback callback, Object tag) {
            try {
                Request request = buildPostFormBodyRequestWithHashMap(url, map, tag);
                deliveryResult(callback, request);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

//        public void postAsyn(String url, Param[] params, ParamLongArray[] paramsLongArray, final ResultCallback callback, Object tag) {
//            Request request = buildPostJSONRequest(url, params, paramsLongArray, tag);
//            deliveryResult(callback, request);
//        }

        //同步的post请求,直接将bodyStr写入请求体
        public Response post(String url, String bodyStr) throws IOException {
            return post(url, bodyStr, null);
        }

        public Response post(String url, String bodyStr, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, bodyStr);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        //同步的post请求，直接将bodyFile写入请求体
        public Response post(String url, File bodyFile) throws IOException {
            return post(url, bodyFile, null);
        }

        public Response post(String url, File bodyFile, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        //同步的post请求
        public Response post(String url, byte[] bodyBytes) throws IOException {
            return post(url, bodyBytes, null);
        }

        public Response post(String url, byte[] bodyBytes, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        //直接将bodyStr写入请求体
        public void postAsyn(String url, String bodyStr, final ResultCallback callback) {
            postAsyn(url, bodyStr, callback, null);
        }

        public void postAsyn(String url, String bodyStr, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyStr, MediaType.parse("text/plain;charset=utf-8"), callback, tag);
        }

        //直接将bodyBytes写入请求体
        public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback) {
            postAsyn(url, bodyBytes, callback, null);
        }

        public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyBytes, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
        }

        //直接将bodyFile写入请求体
        public void postAsyn(String url, File bodyFile, final ResultCallback callback) {
            postAsyn(url, bodyFile, callback, null);
        }

        public void postAsyn(String url, File bodyFile, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyFile, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
        }

        //直接将bodyStr写入请求体
        public void postAsynWithMediaType(String url, String bodyStr, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyStr);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }

        //直接将bodyBytes写入请求体
        public void postAsynWithMediaType(String url, byte[] bodyBytes, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyBytes);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }

        //直接将bodyFile写入请求体
        public void postAsynWithMediaType(String url, File bodyFile, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }

        //post构造Request的方法
        private Request buildPostRequest(String url, RequestBody body, Object tag) {
            Request.Builder builder = new Request.Builder().
                    addHeader("Content-Type", "application/json")
                    .url(url).post(body);
            if (tag != null) {
                builder.tag(tag);
            }
            Request request = builder.build();
            return request;
        }

    }

    //GetDelegate
    public class GetDelegate {

        private Request buildGetRequest(String url, Object tag) {
            Request.Builder builder = new Request.Builder().url(url);
            if (tag != null) {
                builder.tag(tag);
            }
            return builder.build();
        }

        //通用方法
        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response excute = call.execute();
            return excute;
        }

        //同步的get请求
        public Response get(String url) throws IOException {
            return get(url, null);
        }

        public Response get(String url, Object tag) throws IOException {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }

        public String getAsString(String url) throws IOException {
            return getAsString(url, null);
        }

        public String getAsString(String url, Object tag) throws IOException {
            Response excute = get(url, tag);
            return excute.body().string();
        }

        //通用方法
        public void getAsyn(Request request, ResultCallback callback) {
            deliveryResult(callback, request);
        }

        //异步的get请求
        public void getAsyn(String url, final ResultCallback callback) {
            getAsyn(url, callback, null);
        }

        public void getAsyn(String url, final ResultCallback callback, Object tag) {
            final Request request = buildGetRequest(url, tag);
            getAsyn(request, callback);
        }
    }


    //DisplayImageDelegate
    public class DisplayImageDelegate {
        //加载图片
        public void displayImage(final ImageView view, final String url, final int errorResId, final Object tag) {
            final Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            if (view == null) {
                return;
            }
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    try {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try {
                            is.reset();
                        } catch (IOException e) {
                            response = mGetDelegate.get(url, tag);
                            is = response.body().byteStream();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = inSampleSize;
                        final Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
//                        setErrorResId(view, errorResId);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        public void displayImage(final ImageView view, String url) {
            displayImage(view, url, -1, null);
        }

        public void displayImage(final ImageView view, String url, Object tag) {
            displayImage(view, url, -1, tag);
        }

    }

    //DisplayBlurImageDelegate
    public class DisplayBlurImageDelegate {

        //加载图片并高斯模糊
        public void displayImage(final RelativeLayout view, final String url, final int errorResId, final Object tag, final int bannerValue, final ImageView imageView) {
            final Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    try {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try {
                            is.reset();
                        } catch (IOException e) {
                            response = mGetDelegate.get(url, tag);
                            is = response.body().byteStream();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = inSampleSize;
                        final Bitmap bitmap = NativeStackBlur.process(BitmapFactory.decodeStream(is, null, options), bannerValue);

                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
//                view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                if (view != null) {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                    } else {
                                        view.setBackground(new BitmapDrawable(bitmap));
                                    }
                                }
                                if (imageView != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
//                view.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
//                        setErrorResId(view, errorResId);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        public void displayImage(final ImageView view, final String url, final int bannerValue, int placeHolderId) {
            Glide.with(MyApplication.getInstance()).load(url).placeholder(placeHolderId).error(placeHolderId).bitmapTransform(new BitmapTransformation(MyApplication.getInstance()) {
                @Override
                protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

                    Bitmap bitmap = null;
                    float screenScale = ScreenUtils.getScreenH() / (float) ScreenUtils.getScreenW();
                    int width = toTransform.getWidth();
                    int height = toTransform.getHeight();
                    float scale = height / (float) width;
                    if (scale >= screenScale) {
                        bitmap = Bitmap.createBitmap(toTransform, 0, 0, width, (int) (width * screenScale));
                    } else {
                        bitmap = Bitmap.createBitmap(toTransform, 0, 0, (int) (height / screenScale), height);
                    }
//                    Log.d(TAG, "toTransform.getWidth():" + toTransform.getWidth());
//                    Log.d(TAG, "toTransform.getHeight():" + toTransform.getHeight());
//                    Log.d(TAG, "bitmap.getWidth():" + bitmap.getWidth());
//                    Log.d(TAG, "bitmap.getHeight():" + bitmap.getHeight());
                    return NativeStackBlur.process(bitmap, bannerValue);
                }

                @Override
                public String getId() {
                    return url + "_banner" + bannerValue;
                }
            }).into(view);
        }


        public void displayImage(final ImageView view, final String url, final int errorResId, final Object tag, final int bannerValue) {
            final Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    try {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try {
                            is.reset();
                        } catch (IOException e) {
                            response = mGetDelegate.get(url, tag);
                            is = response.body().byteStream();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = inSampleSize;
                        final Bitmap bitmap = NativeStackBlur.process(BitmapFactory.decodeStream(is, null, options), bannerValue);

                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
//                view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                view.setImageBitmap(bitmap);
//                view.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
//                        setErrorResId(view, errorResId);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        public void displayBlurImage(final RelativeLayout view, String url) {
            displayImage(view, url, -1, null, 50, null);
        }

        public void displayBlurImage(final ImageView view, String url) {
            displayImage(null, url, -1, null, 50, view);
        }

        public void displayBlurImage(final ImageView view, String url, int bannerValue, int placeHolderResource) {
            displayImage(view, url, bannerValue, placeHolderResource);
        }

        public void displayBlurImage(final RelativeLayout view, String url, int bannerValue, ImageView imageView) {
            displayImage(view, url, -1, null, bannerValue, imageView);
        }

        public void displayBlurImage(final RelativeLayout view, String url, Object tag) {
            displayImage(view, url, -1, tag, 50, null);
        }

        private void setErrorResId(final RelativeLayout view, final int errResId) {
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
//          (view.getBackground()).setImageResource(errResId);
                    try {
                        view.setBackgroundResource(errResId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //DownloadDelegate
    //下载相关的模块
    public class DownloadDelegate {


        //异步下载文件
        public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback, Object tag) {
            if (url == null || TextUtils.isEmpty(url)) {
                return;
            }
            final Request request = new Request.Builder().url(url).tag(tag).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    sendFailedStringCallback(request, e, AppConf.DOWNLOAD_ERR_MSG, callback);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, getFileName(url));
                        HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
                        if (file.exists() && file.length() == huc.getContentLength()) {
                            sendSuccessResultCallback(file.getAbsolutePath(), callback);
                            huc.disconnect();
                            return;
                        }
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessResultCallback(file.getAbsolutePath(), callback);
                    } catch (Exception e) {
//                        sendFailedStringCallback(response.request(), e, callback);
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {

                        }
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {

                        }
                    }
                }
            });
        }


        //异步下载文件
        public void downloadAsynWithProgress(final String url, final String destFileDir, final ProgressCallback callback, Object tag) {
            if (url == null || TextUtils.isEmpty(url)) {
                return;
            }
            final Request request = new Request.Builder().url(url).tag(tag).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onDownloadError(e.toString());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    FileOutputStream fos = null;
                    try {
                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        final File file = new File(dir, getFileName(url));
                        HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
                        long contentLength = huc.getContentLength();
                        long progress = 0;
                        float percent = 0;
                        int len = 0;
                        if (file.exists() && file.length() == contentLength) {
                            if (callback != null)
                                mDelivery.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onDownloadFinish(file.getAbsolutePath());
                                    }
                                });
                            huc.disconnect();
                            return;
                        }
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            progress += len;
                            percent = (float) progress / contentLength;
                            if (callback != null) {
                                final float finalPercent = percent;
                                final long finalProgress = progress;
                                mDelivery.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onProgressChanged(finalPercent, finalProgress);
                                    }
                                });
                            }

                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        if (callback != null)
                            mDelivery.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onDownloadFinish(file.getAbsolutePath());
                                }
                            });
                    } catch (Exception e) {
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onDownloadError("errorException");
                                }
                            }
                        });
//                        sendFailedStringCallback(response.request(), e, callback);
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {

                        }
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {

                        }
                    }
                }
            });
        }

        //下载图片返回bitmap
        public void downLoadImage(final String url, final int bannerValue, final DownLoadImageCallBack downLoadImageCallBack) {
            final Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (downLoadImageCallBack != null) {
                            mDelivery.post(new Runnable() {
                                @Override
                                public void run() {
                                    downLoadImageCallBack.getBitMap(NativeStackBlur.process(BitmapFactory.decodeStream(response.body().byteStream()), bannerValue));
                                }
                            });
                        }
                    } catch (Exception e) {
//                        setErrorResId(view, errorResId);
                    }
                }
            });
        }

        public abstract class DownLoadImageCallBack {
            abstract void getBitMap(Bitmap bitmap);
        }

        public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
            downloadAsyn(url, destFileDir, callback, null);
        }
    }

    //Https相关模块
    public class HttpsDelegate {
        public void setCertificates(InputStream... certificates) {
            setCertificates(certificates, null, null);
        }

        public TrustManager[] prepareTrustManager(InputStream... certificates) {
            if (certificates == null || certificates.length <= 0) {
                return null;
            }

            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    try {
                        if (certificate != null) {
                            certificate.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                TrustManagerFactory trustManagerFactory = null;
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                return trustManagers;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
            try {
                if (bksFile == null || password == null) {
                    return null;
                }
                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
            try {
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
//                mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }

        public class MyTrustManager implements X509TrustManager {
            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException e) {
                    localTrustManager.checkServerTrusted(chain, authType);
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }

    }

    public static abstract class ProgressCallback {

        protected abstract void onProgressChanged(float percent, long length);

        public abstract void onDownloadFinish(String fileAbsolutePath);

        public abstract void onDownloadError(String error);
    }
}
