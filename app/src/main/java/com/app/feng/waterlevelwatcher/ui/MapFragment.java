package com.app.feng.waterlevelwatcher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.Marker;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.log.XLog;
import com.app.feng.waterlevelwatcher.utils.MarkerManager;

import java.util.Iterator;


public class MapFragment extends Fragment {

    private MapView mapView;
    private SearchView searchView;

    AMap aMap;

    public MarkerManager markerManager;

    private ISlidePanelEventControl panelControl;

    public MapFragment() {
        // Required empty public constructor
        XLog.d("MapFragment : " + "MapFragment");
    }


    public static MapFragment getInstance() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.d("MapFragment : " + "onCreate");

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map,container,false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerManager.clickMarker(marker);

                return false;
            }
        });

        XLog.d("MapFragment : " + "onCreateView");

        return v;
    }

    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initMarker();

    }

    private void initMarker() {

        markerManager = new MarkerManager(getContext());
        markerManager.generateMarker();
        markerManager.setPanelControl(panelControl);

        Iterator<MarkView> markViewIterator = markerManager.iteratorAllMarkView();
        while (markViewIterator.hasNext()) {
            MarkView markView = markViewIterator.next();
            Marker marker = aMap.addMarker(markView.getMarkerOptions());
            marker.setObject(markView.getFlag());
        }
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
        XLog.d("MapFragment : " + "onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelControl = null;
        XLog.d("MapFragment : " + "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        XLog.d("MapFragment : " + "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        XLog.d("MapFragment : " + "onPause");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        XLog.d("MapFragment : " + "onSaveInstanceState");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        XLog.d("MapFragment : " + "onDestroy");
    }
}
