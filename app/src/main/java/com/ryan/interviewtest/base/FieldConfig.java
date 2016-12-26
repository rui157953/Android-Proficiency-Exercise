package com.ryan.interviewtest.base;

/**
 * Created by Ryan.
 */
public class FieldConfig {

    /** 数据库相关 **/
    /**
     * _id : 5840df64421aa939bb4637ea
     * createdAt : 2016-12-02T10:41:40.452Z
     * desc : 类似 Apple music 弹起卡片效果
     * images : ["http://img.gank.io/4ba3aed5-a943-48a9-ab20-dc4d4cd81fec","http://img.gank.io/9b987c0f-1682-4500-bf09-1d6a2064d83e"]
     * publishedAt : 2016-12-02T12:13:34.224Z
     * source : chrome
     * type : iOS
     * url : https://github.com/jobandtalent/CardStackController
     * used : true
     * who : 代码家
     */
    public static final String TABLE_NAME = "data";
    public static final String _ID = "_id";
    public static final String CREATEDAT = "createdAt";
    public static final String DESC = "desc";
    public static final String IMAGES = "images";
    public static final String PUBLISHEDAT = "publishedAt";
    public static final String SOURCE = "source";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String USED = "used";
    public static final String WHO = "who";

    /** 页面相关 **/
    public static int PAGE_SIZE = 10;
    public static String BASE_URL = "http://gank.io/api/data/";

    public static String TYPE_ANDROID = "Android";
    public static String TYPE_IOS = "iOS";
    public static String TYPE_FRONT_END = "前端";



}
