package com.example.wheelytest.di.modules;

import android.content.Context;
import android.location.LocationManager;

import com.example.wheelytest.App;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    LocationManager provideLocationManager() {
       return (LocationManager) mApp.getSystemService(Context.LOCATION_SERVICE);
    }
}