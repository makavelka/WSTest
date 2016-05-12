package com.example.wheelytest.di.component;

import com.example.wheelytest.LocationService;
import com.example.wheelytest.NetworkReceiver;
import com.example.wheelytest.di.modules.AppModule;
import com.example.wheelytest.di.modules.UtilsModule;
import com.example.wheelytest.di.modules.ViewModule;
import com.example.wheelytest.di.modules.WsModule;
import com.example.wheelytest.model.ws.WebSocketHelper;
import com.example.wheelytest.presenter.AuthPresenterImpl;
import com.example.wheelytest.utils.NetworkUtils;
import com.example.wheelytest.utils.PrefsUtils;
import com.example.wheelytest.view.activity.AuthActivity;
import com.example.wheelytest.view.activity.MapActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UtilsModule.class, AppModule.class, ViewModule.class, WsModule.class})
public interface AppComponent {
    void inject(PrefsUtils prefsUtils);
    void inject(LocationService locationService);
    void inject(AuthPresenterImpl authPresenter);
    void inject(MapActivity mapActivity);
    void inject(AuthActivity authActivity);
    void inject(NetworkUtils networkUtils);
    void inject(WebSocketHelper webSocketHelper);
    void inject(NetworkReceiver networkReceiver);
}
