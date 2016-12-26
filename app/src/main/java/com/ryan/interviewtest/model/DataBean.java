package com.ryan.interviewtest.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan.
 */
public class DataBean {


    /**
     * error : false
     * results : [{"_id":"5840df64421aa939bb4637ea","createdAt":"2016-12-02T10:41:40.452Z","desc":"类似 Apple music 弹起卡片效果","images":["http://img.gank.io/4ba3aed5-a943-48a9-ab20-dc4d4cd81fec","http://img.gank.io/9b987c0f-1682-4500-bf09-1d6a2064d83e"],"publishedAt":"2016-12-02T12:13:34.224Z","source":"chrome","type":"iOS","url":"https://github.com/jobandtalent/CardStackController","used":true,"who":"代码家"},{"_id":"583ce7af421aa939b83535f4","createdAt":"2016-11-29T10:27:59.569Z","desc":"iOS 屏幕录制工具","publishedAt":"2016-12-01T11:36:13.685Z","source":"chrome","type":"iOS","url":"https://github.com/kosso/TiScreenRecorder","used":true,"who":"ko"},{"_id":"583f8f22421aa939befafaea","createdAt":"2016-12-01T10:46:58.462Z","desc":"简单轻量的一个分割线组件","publishedAt":"2016-12-01T11:36:13.685Z","source":"chrome","type":"iOS","url":"https://github.com/Marcocanc/NELineLabel","used":true,"who":"代码家"},{"_id":"583e3ecb421aa939bb4637d0","createdAt":"2016-11-30T10:51:55.538Z","desc":"一个内置小巧的终端，输出 App print 信息，调试的时候很有用哦。","images":["http://img.gank.io/e92a2bfc-19bb-47b4-957f-a2cca4cf2a44"],"publishedAt":"2016-11-30T11:35:55.916Z","source":"chrome","type":"iOS","url":"https://github.com/Cosmo/TinyConsole","used":true,"who":"代码家"},{"_id":"583e401e421aa939b8353602","createdAt":"2016-11-30T10:57:34.184Z","desc":"下雨效果的下拉刷新","images":["http://img.gank.io/ea55db9c-8240-4c6f-904b-f95ae072daad"],"publishedAt":"2016-11-30T11:35:55.916Z","source":"chrome","type":"iOS","url":"https://github.com/Onix-Systems/RainyRefreshControl","used":true,"who":"代码家"},{"_id":"583bf8f0421aa9710cf54c45","createdAt":"2016-11-28T17:29:20.600Z","desc":"iOS平台键盘再也不会挡住输入控件","images":["http://img.gank.io/c9bdeea9-d931-4daf-a599-38fcdc40e39f"],"publishedAt":"2016-11-29T11:38:58.378Z","source":"web","type":"iOS","url":"https://github.com/netyouli/WHC_KeyboardManager","used":true,"who":"吴海超(WHC)"},{"_id":"583c3aaf421aa971108b6595","createdAt":"2016-11-28T22:09:51.637Z","desc":"漂亮的日期选择，支持日历模式和Picker模式","images":["http://img.gank.io/b9e4b287-4c50-48f2-bed3-f2be6bb6fff9"],"publishedAt":"2016-11-29T11:38:58.378Z","source":"web","type":"iOS","url":"https://github.com/kRadius/HYYCalendar","used":true,"who":null},{"_id":"583ce75a421aa939bb4637bf","createdAt":"2016-11-29T10:26:34.811Z","desc":"有人把 Chrome 的恐龙游戏搬到了 TouchBar 上！","images":["http://img.gank.io/6993e497-872e-44c5-a36c-952d75a8316f"],"publishedAt":"2016-11-29T11:38:58.378Z","source":"chrome","type":"iOS","url":"https://github.com/yuhuili/TouchBarDino","used":true,"who":"代码家"},{"_id":"583ce8fb421aa939befafac9","createdAt":"2016-11-29T10:33:31.48Z","desc":"iOS 信用卡输入效果","images":["http://img.gank.io/0c21e0c1-7d05-42c9-b6b4-79f6aee30198"],"publishedAt":"2016-11-29T11:38:58.378Z","source":"chrome","type":"iOS","url":"https://github.com/orazz/CreditCardForm-iOS","used":true,"who":"代码家"}]
     */

    private boolean error;
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

    private List<ResultsBean> results;

    public static DataBean objectFromData(String str) {

        return new Gson().fromJson(str, DataBean.class);
    }

    public static DataBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DataBean> arrayDataBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<DataBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DataBean> arrayDataBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DataBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

}
