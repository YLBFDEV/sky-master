package com.skytech.android.util;

import com.google.gson.Gson;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Java对象和JSON字符串相互转化工具类
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return (T) gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    public static JSONArray list2JSONArray(List<String> list) {
        JSONArray ja = new JSONArray();
        if (null != list) {
            for (String value : list) {
                ja.put(value);
            }
        }
        return ja;
    }

    public static List<String> jsonArray2List(JSONArray ja) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < ja.length(); i++) {
            list.add(ja.optString(i));
        }
        return list;
    }

}
