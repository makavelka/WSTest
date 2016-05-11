package com.example.wheelytest.view;

import com.example.wheelytest.model.GeoData;

import java.util.ArrayList;

public interface MarkersView extends IView {
    void showMarkers(ArrayList<GeoData> arrayList);
    void initMap();
    void requestPermission();
    void startLocationService();
    void disconnect();
}
