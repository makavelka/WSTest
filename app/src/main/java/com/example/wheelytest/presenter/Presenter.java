package com.example.wheelytest.presenter;

import android.os.Bundle;

import com.example.wheelytest.view.IView;

/**
 * Общий презентер, имеющий методы для сохранения состояния экрана и остановки
 */
public interface Presenter {
    void onCreate(Bundle savedInstanceState, IView view);
    void onSaveInstanceState(Bundle outState);
    void onStop();
}
