package com.example.wheelytest.presenter;

import android.os.Bundle;

import com.example.wheelytest.model.GeoData;
import com.example.wheelytest.view.IView;
import com.example.wheelytest.view.MarkersView;

import java.util.ArrayList;

public class MapPresenterImpl implements MapPresenter {

    private final String BUNDLE_ARRAY_KEY = "BUNDLE_ARRAY_KEY";

    private MarkersView mView;
    private ArrayList<GeoData> mArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState, IView view) {
        mView = (MarkersView) view;
        if (savedInstanceState != null) {
            mArrayList = (ArrayList<GeoData>) savedInstanceState.getSerializable(BUNDLE_ARRAY_KEY);
            if (mArrayList != null) {
                mView.showMarkers(mArrayList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mArrayList != null) {
            outState.putSerializable(BUNDLE_ARRAY_KEY, mArrayList);
        }
    }

    @Override
    public void onStop() {
    }
}
