package com.ryan.interviewtest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ryan
 */
public class OkHttpManager {
    private volatile static OkHttpManager mInstance;
    private Handler mDelivery = new Handler(Looper.getMainLooper());
    private OkHttpClient mOkHttpClient;

    public static OkHttpManager getInstance() {
        return initClient(null);
    }

    public static OkHttpManager initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    private OkHttpManager(OkHttpClient okHttpClient) {

        if (okHttpClient == null){
            mOkHttpClient = new OkHttpClient();
        }else {
            mOkHttpClient = okHttpClient;
        }
    }


    /**
     * Get 请求
     * @param url
     * @param callbck
     */
    public void okhttpGet(String url, HttpUtils.ResultCallback callbck){
        Request request = new Request.Builder().url(url).build();
        deliveryCallback(request,callbck);
    }

    /**
     * Post请求
     * @param url
     * @param requestBody
     * @param callbck
     */
    public void okhttpPost(String url, Map<String,String> requestBody, HttpUtils.ResultCallback callbck) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keySet = requestBody.keySet();
        for (String key :
                keySet) {
            builder.add(key,requestBody.get(key));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        deliveryCallback(request,callbck);
    }

    public void okHttpLoadImage(ImageView iv,String srcUrl,int errorImage){
        Request request = new Request.Builder().url(srcUrl).build();
        loadImage(request,iv,errorImage);

    }

    private void loadImage(Request request, final ImageView iv, final int errorImage) {

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(errorImage);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                final InputStream result = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(result);

                Response networkResponse = response.networkResponse();
                Response cacheResponse = response.cacheResponse();
                Log.i("ryan", "networkResponse: "+networkResponse);
                Log.i("ryan", "cacheResponse: "+cacheResponse);
                response.body().close();
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap !=null) {
                            iv.setImageBitmap(bitmap);
                        }else {
                            iv.setImageResource(errorImage);
                        }
                    }
                });
            }
        });

    }


    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }


    /**
     *
     * @param request
     * @param callback
     */
    private void deliveryCallback(final Request request, final HttpUtils.ResultCallback callback){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call,e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String result = response.body().string();

                Response networkResponse = response.networkResponse();
                Response cacheResponse = response.cacheResponse();
                    Log.i("ryan", "networkResponse: "+networkResponse);
                    Log.i("ryan", "cacheResponse: "+cacheResponse);
                response.body().close();
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(call,result);
                    }
                });
            }
        });
    }

}
