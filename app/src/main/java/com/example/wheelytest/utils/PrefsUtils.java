package com.example.wheelytest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wheelytest.App;
import com.example.wheelytest.Values;

import javax.inject.Inject;

public class PrefsUtils {

    @Inject
    Context mContext;

    public PrefsUtils() {
        App.getComponent().inject(this);
    }

    public SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(Values.PREFS_TAG, Context.MODE_PRIVATE);
    }

    public String getLogin() {
        return getPrefs().getString(Values.LOGIN_TAG, "aaa");
    }

    public String getPass() {
        return getPrefs().getString(Values.PASS_TAG, "aaa");
    }

    public void saveLogin(String login) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(login, null);
        editor.commit();
    }

    public void savePass(String pass) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(pass, null);
        editor.commit();
    }
}
