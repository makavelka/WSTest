package com.example.wheelytest;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.example.wheelytest.model.GeoData;
import com.example.wheelytest.model.LatLng;
import com.example.wheelytest.model.db.LocationsDbDataSource;
import com.example.wheelytest.utils.GsonUtils;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class LocationService extends Service {

    @Inject GsonUtils mGsonUtils;

    private LocationManager mLocationManager;
    private LocationsDbDataSource mDataSource;
    private WebSocket mWebSocket;
    private boolean isConnected = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static String createURL(Map<String, String> params) {
        Uri.Builder builder = Uri.parse("ws://mini-mdt.wheely.com").buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        App.getComponent().inject(this);
        initWebSocket();
        mDataSource = new LocationsDbDataSource(this);
        mDataSource.open();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, l);
        return START_REDELIVER_INTENT;
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
            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mDataSource.createGeoData(lat, lon, false);
            if (isConnected) {
                mWebSocket.sendText(mGsonUtils.toJson(latLng));
            }
        }
    };

    public void initWebSocket() {
        try {
            Map<String, String> params = new ArrayMap<>();
            params.put(Values.USERNAME_KEY, "a");
            params.put(Values.PASS_KEY, "a");
            mWebSocket = new WebSocketFactory().createSocket(createURL(params));
            mWebSocket.addListener(mWebSocketAdapter);
            mWebSocket.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WebSocketAdapter mWebSocketAdapter = new WebSocketAdapter() {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            isConnected = true;
            Criteria criteria = new Criteria();
            String bestProvider = mLocationManager.getBestProvider(criteria, false);
            android.location.Location location = mLocationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                mWebSocket.sendText(mGsonUtils.toJson(new LatLng(location.getLatitude(), location.getLongitude())));
            } else {
                mWebSocket.sendText(mGsonUtils.toJson(new LatLng(Values.START_LAT, Values.START_LON)));
            }
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            ArrayList<GeoData> arrayList = new ArrayList<>();
            arrayList.addAll(Arrays.asList(mGsonUtils.fromJson(text, GeoData[].class)));
            Toast.makeText(LocationService.this, text, Toast.LENGTH_SHORT).show();
        }
    };
}
