package com.ryan.interviewtest.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ryan.
 */
public class PreferenceHelper {


    public static final String PREFERENCENAME = "gankio";
    private static int MODE = Context.MODE_PRIVATE;
    private static SharedPreferences mSharedPreferences;
    private static Context mContext;


    public static SharedPreferences getPreference(Context context){
        return context.getApplicationContext().getSharedPreferences(
                PREFERENCENAME, MODE);
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCENAME, MODE);
        return settings.getString(key, "");
    }

    public static void setStringValue(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREFERENCENAME, MODE)
                .edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBooleanValue(Context context, String key,boolean defValue){
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCENAME, MODE);
        return settings.getBoolean(key, defValue);
    }

    public static void setBooleanValue(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(PREFERENCENAME, MODE)
                .edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int getIntValue(Context context, String key,int defValue){
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCENAME, MODE);
        return settings.getInt(key, defValue);
    }

    public static void setIntValue(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(PREFERENCENAME, MODE)
                .edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /** 清除某个key的数据 */
    public static void removeValue(Context context, String key) {
        Editor editor = context.getSharedPreferences(PREFERENCENAME, MODE)
                .edit();
        editor.remove(key);
        editor.apply();
    }

}
