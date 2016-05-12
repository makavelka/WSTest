package com.example.wheelytest.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;

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
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            return null;
        }
    }
}
