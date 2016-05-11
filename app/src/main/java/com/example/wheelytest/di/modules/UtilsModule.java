package com.example.wheelytest.di.modules;

import com.example.wheelytest.utils.GsonUtils;
import com.example.wheelytest.utils.NetworkUtils;
import com.example.wheelytest.utils.PrefsUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    PrefsUtils providePrefsUtils() {
        return new PrefsUtils();
    }

    @Provides
    @Singleton
    GsonUtils provideGsonUtils() {
        return new GsonUtils();
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils() {
        return new NetworkUtils();
    }
}
