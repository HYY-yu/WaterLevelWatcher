package com.app.feng.waterlevelwatcher.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.utils.MarkerManager;
import com.app.feng.waterlevelwatcher.utils.RealmUtils;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmResults;


public class MapFragment extends Fragment {

    private MapView mapView;
    private SearchView searchView;

    AMap aMap;

    Realm realm;

    public MarkerManager markerManager;

    private ISlidePanelEventControl panelControl;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment getInstance() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map,container,false);
        searchView = (SearchView) v.findViewById(R.id.sv_map_station);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(32.671478,111.715668),Config.MAP_ZOOM_LEVEL);
        aMap.animateCamera(cameraUpdate);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerManager.clickMarker(marker);
                return false;
            }
        });

        realm = Realm.getDefaultInstance();

        return v;
    }

    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initMarker();

    }

    private void initMarker() {

        RealmResults<MonitoringStationBean> monitoringStationBeen = RealmUtils.loadAllStation(
                realm);

        markerManager = new MarkerManager(getContext());
        markerManager.generateMarker(monitoringStationBeen);
        markerManager.setPanelControl(panelControl);

        Iterator<MarkView> markViewIterator = markerManager.iteratorAllMarkView();
        while (markViewIterator.hasNext()) {
            MarkView markView = markViewIterator.next();
            Marker marker = aMap.addMarker(markView.getMarkerOptions());
            marker.setObject(markView.getFlag());
        }

        //aMap.addPolyline(markerManager.getPolylineOptions());

        markerManager.addMarkerFromAMap(aMap.getMapScreenMarkers());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISlidePanelEventControl) {
            panelControl = (ISlidePanelEventControl) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement ISlidePanelEventControl");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelControl = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        realm.close();
    }

    public void moveMapToStation(int id) {
        MonitoringStationBean stationBean = RealmUtils.loadStationDataById(realm,id);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(stationBean.getLatitude(),stationBean.getLongitude()),
                Config.MAP_ZOOM_LEVEL);
        aMap.animateCamera(cameraUpdate);
    }
}
