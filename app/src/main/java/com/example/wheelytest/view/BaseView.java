package com.example.wheelytest.view;

/**
 * Базовый интерфейс для слоя View, имеющий метод:
 * showToast(String message) - показать информационное сообщение
 */
public interface BaseView {
    void showToast(String message);
    void showToast(int resId);
}
