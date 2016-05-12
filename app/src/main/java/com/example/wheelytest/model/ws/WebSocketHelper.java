package com.example.wheelytest.model.ws;

import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.wheelytest.App;
import com.example.wheelytest.Values;
import com.example.wheelytest.model.GeoData;
import com.example.wheelytest.model.LatLng;
import com.example.wheelytest.model.NetworkHelper;
import com.example.wheelytest.utils.GsonUtils;
import com.example.wheelytest.utils.NetworkUtils;
import com.example.wheelytest.utils.PrefsUtils;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class WebSocketHelper {

    @Inject
    GsonUtils mGsonUtils;
    @Inject
    EventBus mEventBus;
    @Inject
    PrefsUtils mPrefs;
    @Inject
    LocationManager mLocationManager;
    @Inject
    NetworkUtils mNetworkUtils;

    private WebSocket mWebSocket;

    public WebSocketHelper() {
        App.getComponent().inject(this);
        mEventBus.register(this);
        initWebSocket();
    }

    private String createURL(Map<String, String> params) {
        Uri.Builder builder = Uri.parse(Values.BASE_URL).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().toString();
    }

    public void initWebSocket() {
        try {
            Map<String, String> params = new ArrayMap<>();
            params.put(Values.USERNAME_KEY, mPrefs.getLogin());
            params.put(Values.PASS_KEY, mPrefs.getPass());
            mWebSocket = new WebSocketFactory().createSocket(createURL(params));
            mWebSocket.addListener(mWebSocketAdapter);
            mWebSocket.setPingPayloadGenerator(() -> new Date().toString().getBytes());
            mWebSocket.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(LatLng latLng) {
        if (mWebSocket.isOpen())
            mWebSocket.sendText(mGsonUtils.toJson(latLng));
    }

    public void disconnect() {
        mWebSocket.disconnect();
    }

    public void reconnect() throws IOException{
        mWebSocket = mWebSocket.recreate().connectAsynchronously();
    }

    @Subscribe
    public void onMessageEvent(NetworkHelper helper) throws IOException {
        if (helper.isInternetConnected()) {
            reconnect();
        }
    }

    private WebSocketAdapter mWebSocketAdapter = new WebSocketAdapter() {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            android.location.Location location = mNetworkUtils.getLastKnownLocation();
            if (location != null) {
                mWebSocket.sendText(mGsonUtils.toJson(new LatLng(location.getLatitude(), location.getLongitude())));
            } else {
                mWebSocket.sendText(mGsonUtils.toJson(new LatLng(Values.START_LAT, Values.START_LON)));
            }
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            Log.d("WebSocket", "OnDisconnected");
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            Log.d("WebSocket", "OnError");
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            ArrayList<GeoData> arrayList = new ArrayList<>();
            arrayList.addAll(Arrays.asList(mGsonUtils.fromJson(text, GeoData[].class)));
            mEventBus.post(arrayList);
        }

        @Override
        public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onPingFrame(websocket, frame);
        }

        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
            super.onStateChanged(websocket, newState);
            if (newState.name().equals(WebSocketState.CLOSED)) {
                reconnect();
            }
            Log.d("WebSocket", newState.name());
        }
    };
}
