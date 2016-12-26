package com.ryan.interviewtest.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.text.TextUtils;

import com.ryan.interviewtest.base.DataBaseHelper;
import com.ryan.interviewtest.base.DeferredHandler;
import com.ryan.interviewtest.base.FieldConfig;
import com.ryan.interviewtest.utils.HttpUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;

/**
 * Created by Ryan.
 */
public class GankModel {

    private DataBaseHelper mOpenHelper;
    SimpleDateFormat format;
    private Context mContext;

    public GankModel(Context context) {
        mContext = context;
        mOpenHelper =  new DataBaseHelper(context);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
    }

    private static final HandlerThread sWorkerThread = new HandlerThread("checked-loader");

    static {
        sWorkerThread.start();
    }

    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    public void insertData(final List<ResultsBean> results) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                for (ResultsBean resultsBean : results) {
                    SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(FieldConfig._ID, resultsBean.get_id());
                    values.put(FieldConfig.CREATEDAT, resultsBean.getCreatedAt());
                    values.put(FieldConfig.DESC, resultsBean.getDesc());
                    List<String> images = resultsBean.getImages();
                    if (images != null) {
                        StringBuilder stringBuffer = new StringBuilder("");
                        for (String imageUrl : images) {
                            stringBuffer.append(imageUrl).append(",");
                        }
                        String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
                        values.put(FieldConfig.IMAGES, substring);
                    }
                    values.put(FieldConfig.PUBLISHEDAT, resultsBean.getPublishedAt());
                    values.put(FieldConfig.SOURCE, resultsBean.getSource());
                    values.put(FieldConfig.TYPE, resultsBean.getType());
                    values.put(FieldConfig.URL, resultsBean.getUrl());
                    values.put(FieldConfig.USED, resultsBean.isUsed());
                    values.put(FieldConfig.WHO, resultsBean.getWho());
                    db.insert(FieldConfig.TABLE_NAME, FieldConfig._ID, values);
//                db.close();
                }
            }
        };
        runOnWorkerThread(r);
    }


    public List<ResultsBean> queryData(int page, String... t) {
        List<ResultsBean> beanList = new ArrayList<>();
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int offset = page>0?page-1:0;
        Cursor cursor = db.query(FieldConfig.TABLE_NAME, null, FieldConfig.TYPE+" = ?", t, null, null,FieldConfig.CREATEDAT+" desc LIMIT 10 OFFSET "+(offset*10),null);
        while (cursor.moveToNext()){
            ResultsBean resultsBean = new ResultsBean();
            String _id = cursor.getString(cursor.getColumnIndex(FieldConfig._ID));
            String createdAt = cursor.getString(cursor.getColumnIndex(FieldConfig.CREATEDAT));
            String desc = cursor.getString(cursor.getColumnIndex(FieldConfig.DESC));
            String publishedAt = cursor.getString(cursor.getColumnIndex(FieldConfig.PUBLISHEDAT));
            String source = cursor.getString(cursor.getColumnIndex(FieldConfig.SOURCE));
            String type = cursor.getString(cursor.getColumnIndex(FieldConfig.TYPE));
            String url = cursor.getString(cursor.getColumnIndex(FieldConfig.URL));
            String used = cursor.getString(cursor.getColumnIndex(FieldConfig.USED));
            String who = cursor.getString(cursor.getColumnIndex(FieldConfig.WHO));
            String imagesString = cursor.getString(cursor.getColumnIndex(FieldConfig.IMAGES));
            if (!TextUtils.isEmpty(imagesString)){
                List<String> images = Arrays.asList(imagesString.split(","));
                resultsBean.setImages(images);
            }
            resultsBean.set_id(_id);
            resultsBean.setCreatedAt(createdAt);
            resultsBean.setDesc(desc);
            resultsBean.setPublishedAt(publishedAt);
            resultsBean.setSource(source);
            resultsBean.setType(type);
            resultsBean.setUrl(url);
            resultsBean.setUsed(used);
            resultsBean.setWho(who);
            beanList.add(resultsBean);
        }
        cursor.close();
        db.close();
        return beanList ;
    }

    public void requestData(String baseUrl, String type, int page, final CallBack callBack) {
        String url = baseUrl + type + "/" + FieldConfig.PAGE_SIZE + "/" + page;
        if (isNetworkAvailable()) {
            HttpUtils.requestGet(url, null, new HttpUtils.ResultCallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callBack.bindData(null);
                }
                @Override
                public void onResponse(Call call, String response) {
                    DataBean dataBean = DataBean.objectFromData(response);
                    List<ResultsBean> resultsBeen = dataBean.getResults();
                    callBack.bindData(resultsBeen);
                }
            });
        }else {
            List<ResultsBean> resultsBeen = queryData(page, type);
            callBack.bindData(resultsBeen);
        }

    }

    private DeferredHandler mHandler = new DeferredHandler();

    /**
     * Runs the specified runnable immediately if called from the main thread, otherwise it is
     * posted on the main thread handler.
     */
    private void runOnMainThread(Runnable r) {
        runOnMainThread(r, 0);
    }

    private void runOnMainThread(Runnable r, int type) {
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            // If we are on the worker thread, post onto the main handler
            mHandler.post(r);
        } else {
            r.run();
        }
    }

    /**
     * Runs the specified runnable immediately if called from the worker thread, otherwise it is
     * posted on the worker thread handler.
     */
    private static void runOnWorkerThread(Runnable r) {
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            // If we are not on the worker thread, then post to the worker handler
            sWorker.post(r);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        } else {
            return false;
        }
    }

    public interface CallBack{
        void bindData(List<ResultsBean> resultsBeen);
    }

}
