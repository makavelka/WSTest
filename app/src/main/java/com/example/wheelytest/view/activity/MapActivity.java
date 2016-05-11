package com.example.wheelytest.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.wheelytest.App;
import com.example.wheelytest.LocationService;
import com.example.wheelytest.R;
import com.example.wheelytest.Values;
import com.example.wheelytest.model.GeoData;
import com.example.wheelytest.model.StopService;
import com.example.wheelytest.presenter.MapPresenterImpl;
import com.example.wheelytest.utils.NetworkUtils;
import com.example.wheelytest.view.MarkersView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends BaseActivity implements MarkersView {

    final private int REQUEST_ACCESS_FINE_LOCATION = 123;
    final private int REQUEST_WRITE_STORAGE = 112;

    @Inject
    EventBus mEventBus;
    @Inject
    LocationManager mLocationManager;
    @Inject
    NetworkUtils mNetworkUtils;
    @Inject
    MapPresenterImpl mPresenter;

    @Bind(R.id.map_mapView_mapActivity)
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        App.getComponent().inject(this);
        mEventBus.register(this);
        ButterKnife.bind(this);
        initMap();
        requestPermission();
        mPresenter.onCreate(savedInstanceState, this);
    }

    @Override
    public void requestPermission() {
        boolean isLocationsEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean isWriteEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (isLocationsEnabled && isWriteEnabled) {
            startLocationService();
        } else {
            if (!isLocationsEnabled) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);
            }
            if (!isWriteEnabled) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }
    }

    @Override
    public void startLocationService() {
        startService(new Intent(this, LocationService.class));
    }

    @OnClick(R.id.disconnect)
    @Override
    public void disconnect() {
        mEventBus.post(new StopService(true));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                } else {
                    showToast(R.string.permission_denied_location);
                }
                break;
            case REQUEST_WRITE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                } else {
                    showToast(R.string.permission_denied_storage);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void initMap() {
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        IMapController mapController = mMapView.getController();
        mapController.setZoom(10);
        Location location = mNetworkUtils.getLastKnownLocation();
        GeoPoint startPoint;
        if (location != null) {
            startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        } else {
           startPoint = new GeoPoint(Values.START_LAT, Values.START_LON);
        }
        mapController.setCenter(startPoint);
    }

    @Override
    public void showMarkers(ArrayList<GeoData> arrayList) {
        ArrayList<OverlayItem> items = new ArrayList<>();
        for (GeoData geoData : arrayList) {
            items.add(new OverlayItem(String.valueOf(geoData.getLat()),
                    String.valueOf(geoData.getLon()),
                    new GeoPoint(geoData.getLat(), geoData.getLon())));
        }
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, MapActivity.this);
        mOverlay.setFocusItemsOnTap(true);
        mMapView.getOverlays().add(mOverlay);
    }

    @Subscribe
    public void onMessageEvent(ArrayList<GeoData> arrayList){
        mMapView.getOverlays().clear();
        showMarkers(arrayList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStopEventBus();
    }

    @Override
    public void onStopEventBus() {
        mEventBus.unregister(this);
    }
}
