package com.ryan.interviewtest.utils;

import android.widget.ImageView;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * Created by Ryan
 */
public class HttpUtils {


    public static void loadImag(ImageView imageView,String srcUrl,int errorImage){
        OkHttpManager.getInstance().okHttpLoadImage(imageView,srcUrl,errorImage);
    }


    public static void requestGet(String baseUrl, Map<String,String> requestParam, ResultCallback callbck){
        String url = baseUrl;
        if (requestParam != null) {
            StringBuilder sb = new StringBuilder(baseUrl);
            Set<String> strings = requestParam.keySet();
            Iterator<String> iterator = strings.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next()).append("=").append(requestParam.get(iterator.next())).append("/");
            }
            url = sb.toString();
        }
        OkHttpManager.getInstance().okhttpGet(url,callbck);
    }

    public static void requestPost(String url, Map<String,String> requestBody,ResultCallback callbck) {
        OkHttpManager.getInstance().okhttpPost(url,requestBody,callbck);
    }

    public interface ResultCallback{
        void onFailure(Call call, IOException e);
        void onResponse(Call call, String response);
    }
}
