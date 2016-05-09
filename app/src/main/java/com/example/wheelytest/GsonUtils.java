package com.example.wheelytest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public final class GsonUtils {
    private static Gson gson;

    public static String toJson(Object o) {
        gson = new GsonBuilder().create();
        return gson.toJson(o);
    }

    public static <T> T fromJson(String s, Type typeOfT){
        gson = new GsonBuilder().create();
        return gson.fromJson(s, typeOfT);
    }

    public static String toJsonWithNull(Object o) {
        gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);
    }

    public static <T> T fromJsonWithNull(String s, Type typeOfT){
        gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(s, typeOfT);
    }
}