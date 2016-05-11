package com.example.wheelytest.presenter;

public interface AuthPresenter extends Presenter {
    void authorize(String login, String pass);
    boolean isFirstCharA(String s);
}
