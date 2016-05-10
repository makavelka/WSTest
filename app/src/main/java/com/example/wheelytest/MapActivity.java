package com.example.wheelytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wheelytest.model.GeoData;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    @Bind(R.id.map_mapView_mapActivity)
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initMap();
        startService(new Intent(this, LocationService.class));
    }

    private void initMap() {
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = mMapView.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(Values.START_LAT, Values.START_LON);
        mapController.setCenter(startPoint);
    }

    private void addMarkers(ArrayList<GeoData> arrayList) {
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
}
