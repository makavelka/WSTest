package com.example.wheelytest.di.modules;

import com.example.wheelytest.model.ws.WebSocketHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class WsModule {

    @Provides
    WebSocketHelper providesWebSocketHelper (){
        return new WebSocketHelper();
    }
}
