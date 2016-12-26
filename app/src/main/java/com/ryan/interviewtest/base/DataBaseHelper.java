package com.ryan.interviewtest.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ryan.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    public DataBaseHelper(Context context) {
        super(context, "GankIo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FieldConfig.TABLE_NAME+ "("
                +FieldConfig._ID+" TEXT UNIQUE,"
                +FieldConfig.CREATEDAT+" TEXT,"
                +FieldConfig.DESC+" TEXT,"
                +FieldConfig.IMAGES+" TEXT,"
                +FieldConfig.PUBLISHEDAT+" TEXT,"
                +FieldConfig.SOURCE+" TEXT,"
                +FieldConfig.TYPE+" TEXT,"
                +FieldConfig.URL+" TEXT,"
                +FieldConfig.USED+" TEXT,"
                +FieldConfig.WHO+" TEXT"
                +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
