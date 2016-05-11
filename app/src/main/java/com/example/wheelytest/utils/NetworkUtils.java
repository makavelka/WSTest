package com.example.wheelytest.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.example.wheelytest.App;

import javax.inject.Inject;

public class NetworkUtils {
    @Inject
    Context mContext;
    @Inject
    LocationManager mLocationManager;

    /**
     * В конструкторе делаем инъекцию необходимых нам данных
     */
    public NetworkUtils() {
        App.getComponent().inject(this);
    }

    /**
     * Проверить наличие подключения к интернету.
     */
    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public Location getLastKnownLocation() {
        Criteria criteria = new Criteria();
        return mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
    }
}
