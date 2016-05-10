package com.example.wheelytest.di.component;

import com.example.wheelytest.LocationService;
import com.example.wheelytest.di.modules.AppModule;
import com.example.wheelytest.di.modules.UtilsModule;
import com.example.wheelytest.utils.PrefsUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UtilsModule.class, AppModule.class})
public interface AppComponent {
    void inject(PrefsUtils prefsUtils);
    void inject(LocationService locationService);
}
