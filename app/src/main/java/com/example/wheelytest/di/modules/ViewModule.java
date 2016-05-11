package com.example.wheelytest.di.modules;

import com.example.wheelytest.presenter.AuthPresenterImpl;
import com.example.wheelytest.presenter.MapPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    /**
     * Предоставляет презентер для первого экрана
     *
     * @return презентер
     */
    @Provides
    AuthPresenterImpl provideAuthPresenter() {
        return new AuthPresenterImpl();
    }

    /**
     * Предоставляет презентер для второго экрана
     *
     * @return презентер
     */
    @Provides
    MapPresenterImpl provideMapPresenter() {
        return new MapPresenterImpl();
    }
}