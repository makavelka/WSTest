package com.example.wheelytest.model;

/**
 * Created by Ghost Surfer on 12.05.2016.
 */
public class NetworkHelper {

    public NetworkHelper(boolean isInternetConnected) {
        this.isInternetConnected = isInternetConnected;
    }

    private boolean isInternetConnected;

    public boolean isInternetConnected() {
        return isInternetConnected;
    }

    public void setInternetConnected(boolean internetConnected) {
        isInternetConnected = internetConnected;
    }
}
