package com.ryan.interviewtest.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ryan.interviewtest.utils.OkHttpManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ryan.
 */

public class MyApp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();


        File cacheFile = new File(getApplicationContext().getExternalCacheDir().toString(),"cache");
        int cacheSize = 100*1024*1024;
        Cache cache = new Cache(cacheFile,cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache)
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L,TimeUnit.MILLISECONDS)
                .build();
        OkHttpManager.initClient(okHttpClient);
        Fresco.initialize(getApplicationContext());

    }

    /**
     * Cache
     */
    public class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Response response1 = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    //cache for 5 s
                    .header("Cache-Control", "max-age=" + 5)
                    .build();
            return response1;
        }
    }
}
