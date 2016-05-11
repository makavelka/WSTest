package com.example.wheelytest.presenter;

import android.os.Bundle;

import com.example.wheelytest.App;
import com.example.wheelytest.utils.PrefsUtils;
import com.example.wheelytest.view.AuthView;
import com.example.wheelytest.view.IView;

import javax.inject.Inject;

public class AuthPresenterImpl implements AuthPresenter {

    private final String BUNDLE_LOGIN_KEY = "BUNDLE_LOGIN_KEY";
    private final String BUNDLE_PASS_KEY = "BUNDLE_PASS_KEY";

    @Inject
    PrefsUtils mPrefsUtils;

    private AuthView mView;
    private String mLogin;
    private String mPass;

    @Inject
    public AuthPresenterImpl() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState, IView view) {
        App.getComponent().inject(this);
        mView = (AuthView) view;
        if (savedInstanceState != null) {
            mLogin = savedInstanceState.getString(BUNDLE_LOGIN_KEY);
            mPass = savedInstanceState.getString(BUNDLE_PASS_KEY);
            if (mLogin != null || mPass != null) {
                mView.restoreEditState(mLogin, mPass);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mLogin != null) {
            outState.putString(BUNDLE_LOGIN_KEY, mLogin);
        }
        if (mPass != null) {
            outState.putString(BUNDLE_PASS_KEY,  mPass);
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void authorize(String login, String pass) {
        if (!isFirstCharA(login)) {
            mView.showError(true);
            return;
        }
        if (!isFirstCharA(pass)) {
            mView.showError(false);
            return;
        }
        mPrefsUtils.saveLogin(login);
        mPrefsUtils.savePass(pass);
        mView.startMapActivity();
    }

    @Override
    public boolean isFirstCharA(String s) {
        return s.charAt(0) == 'a';
    }
}
