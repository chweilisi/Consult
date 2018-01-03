package com.cheng.consult.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cheng.consult.app.App;
import com.google.gson.internal.$Gson$Types;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cheng on 2017/11/13.
 */

public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        File sdcache = App.getApplication().getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient=builder.build();

        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    private void getRequest(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private void postUpload(String url, final ResultCallback callback, HashMap<String, Object> paramsMap) {
        Request request = buildUploadRequest(url, paramsMap);
        deliveryResult(callback, request);
    }

    private void postDownload(String url, final ResultCallback callback, List<Param> params){
        Request request = buildDownloadRequest(url, params);
        deliveryResult(callback, request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);
                    } else {
                        Object object = JsonUtils.deserialize(str, callback.mType);
                        sendSuccessCallBack(callback, object);
                    }
                } catch (final Exception e) {
                    LogUtils.e(TAG, "convert json failure", e);
                    sendFailCallback(callback, e);
                }
            }
        }

        );


    }

    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }

    private Request buildUploadRequest(String url, HashMap<String, Object> paramsMap){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, object.toString());
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
            }
        }

        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request
        Request request = new Request.Builder().url(url).post(body).build();
        return request;
    }

    private Request buildDownloadRequest(String url, List<Param> params){

        FormBody.Builder fbb = new FormBody.Builder();
        //追加参数
        for (Param param : params) {
            fbb.add(param.key, param.value);
        }

        Request request = new Request.Builder().url(url).post(fbb.build()).build();
        return request;
    }

    private Request buildPostRequest(String url, List<Param> params) {
        FormBody.Builder formbodybuild = new FormBody.Builder();

        for (Param param : params) {
            formbodybuild.add(param.key, param.value);
        }

        return new Request.Builder().url(url).post(formbodybuild.build()).build();
    }

    /**********************对外接口************************/

    /**
     * get请求
     * @param url  请求url
     * @param callback  请求回调
     */
    public static void get(String url, ResultCallback callback) {
        getmInstance().getRequest(url, callback);
    }

    /**
     * post请求
     * @param url       请求url
     * @param callback  请求回调
     * @param params    请求参数
     */
    public static void post(String url, final ResultCallback callback, List<Param> params) {
        getmInstance().postRequest(url, callback, params);
    }

    /**
     *
     * @param url 请求url
     * @param callback 请求回调
     * @param paramsMap 请求参数,文件
     */
    public static void uploadFile(String url, final ResultCallback callback, HashMap<String, Object> paramsMap){
        getmInstance().postUpload(url, callback, paramsMap);
    }

    public static void downloadFile(String url, String fileUrl, final String destFileDir, final ResultCallback callback, List<Param> params){
        //getmInstance().postDownload(url, callback, params);

        final File file = new File(destFileDir, fileUrl);
        if (file.exists()) {
            return;
        }

        Request request = getmInstance().buildDownloadRequest(url, params);

        final Call call = mInstance.mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    LogUtils.e(TAG, "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        LogUtils.e(TAG, "current------>" + current);

                    }
                    fos.flush();

                } catch (IOException e) {
                    LogUtils.e(TAG, e.toString());

                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        LogUtils.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     * @param <T>
     */
    public static abstract class ResultCallback<T> {

        Type mType;

        public ResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }

    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
