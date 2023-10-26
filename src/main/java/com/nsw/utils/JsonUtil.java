package com.nsw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json格式化公具
 *
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
