package com.example.wheelytest.view;

public interface AuthView extends IView {
    void showError(boolean isLogin);
    void startMapActivity();
    void restoreEditState(String login, String pass);
}
