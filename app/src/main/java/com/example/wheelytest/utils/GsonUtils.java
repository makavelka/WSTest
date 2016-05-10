package com.example.wheelytest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public final class GsonUtils {
    private Gson gson;

    public String toJson(Object o) {
        gson = new GsonBuilder().create();
        return gson.toJson(o);
    }

    public <T> T fromJson(String s, Type typeOfT){
        gson = new GsonBuilder().create();
        return gson.fromJson(s, typeOfT);
    }

    public String toJsonWithNull(Object o) {
        gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);
    }

    public <T> T fromJsonWithNull(String s, Type typeOfT){
        gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(s, typeOfT);
    }
}