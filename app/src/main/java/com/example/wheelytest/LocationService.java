package com.example.wheelytest;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.wheelytest.model.GeoData;
import com.example.wheelytest.model.LatLng;
import com.example.wheelytest.model.StopService;
import com.example.wheelytest.model.ws.WebSocketHelper;
import com.example.wheelytest.utils.GsonUtils;
import com.example.wheelytest.view.activity.MapActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.inject.Inject;

public class LocationService extends Service {

    private final String mServiceName = ".LocationService";
    @Inject
    GsonUtils mGsonUtils;
    @Inject
    EventBus mEventBus;
    @Inject
    WebSocketHelper mWebSocketHelper;
    @Inject
    LocationManager mLocationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void start() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Intent notificationIntent = new Intent(this, MapActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.service_is_up))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true);
        startForeground(Values.NOTIFICATION_ID, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        App.getComponent().inject(this);
        mEventBus.register(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, l);
        start();
        return START_STICKY;
    }

    @Subscribe
    public void onMessageEvent(StopService stopService){
        stopForeground(stopService.isStop());
        mWebSocketHelper.disconnect();
        mEventBus.unregister(this);
    }

    LocationListener l = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mWebSocketHelper.sendMessage(latLng);
        }
    };
}
