package com.example.wheelytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.wheelytest.model.NetworkHelper;
import com.example.wheelytest.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class NetworkReceiver extends BroadcastReceiver {

    @Inject
    NetworkUtils mNetworkUtils;
    @Inject
    EventBus mEventBus;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getComponent().inject(this);
        mEventBus.post(new NetworkHelper(mNetworkUtils.isInternetConnected()));
    }
}
