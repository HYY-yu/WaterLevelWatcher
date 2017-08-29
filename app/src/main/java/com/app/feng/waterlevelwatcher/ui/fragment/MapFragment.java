package com.app.feng.waterlevelwatcher.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.ui.view.MarkView;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.manager.MarkerManager;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import io.realm.Realm;
import io.realm.RealmResults;


public class MapFragment extends Fragment {

    private MapView mapView;
    private SearchView searchView;

    public AMap aMap;

    Realm realm;

    public MarkerManager markerManager;

    private ISlidePanelEventControl panelControl;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map,container,false);
        searchView = (SearchView) v.findViewById(R.id.sv_map_station);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        return v;
    }


    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        initSearchView();

        initMap();

        initMarker();

        //提示用户如何点击搜索框
        showSearchViewMask();

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        boolean isNightMode = SharedPref.getInstance(getContext().getApplicationContext())
                .getBoolean(Config.KEY.ISNIGHT,false);
        setMapMode(isNightMode);

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
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        fitForSuggestionList();
    }

    private void fitForSuggestionList() {
        Class clazz = searchView.getClass();
        try {
            Field mDDA = clazz.getDeclaredField("mDropDownAnchor");
            Field mSSTV = clazz.getDeclaredField("mSearchSrcTextView");
            mDDA.setAccessible(true);
            mSSTV.setAccessible(true);
            final View mDDA_View = (View) mDDA.get(searchView);
            final SearchView.SearchAutoComplete mSSTV_IMPL = (SearchView.SearchAutoComplete) mSSTV.get(
                    searchView);
            //最大高度
            final int list_height = getResources().getDimensionPixelSize(R.dimen.sv_list_height);
            mSSTV_IMPL.setMaxHeight(list_height);

            //拿 mPopup
            final Field mPOP = mSSTV_IMPL.getClass()
                    .getSuperclass()
                    .getSuperclass()
                    .getDeclaredField("mPopup");
            mPOP.setAccessible(true);
            final android.widget.ListPopupWindow mPop_IMPL = (android.widget.ListPopupWindow) mPOP.get(
                    mSSTV_IMPL);

            //在夜间模式下 AnchorView有可能为空 , 在此重新设置
            mPop_IMPL.setAnchorView(mDDA_View);


            //            int widthDp = ConfigurationHelper.getScreenWidthDp(getResources());
            //            final int screenWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            //                                                                    widthDp,
            //                                                                    getResources().getDisplayMetrics());
            final int list_margin = getResources().getDimensionPixelOffset(R.dimen.sv_list_margin);

            mDDA_View.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            //                            Logger.d(
                            //                                    "screenWidth: " + screenWidth + "searchWidth" + searchView.getMeasuredWidth());
                            mPop_IMPL.setWidth(searchView.getMeasuredWidth() - list_margin * 3);
                            mPop_IMPL.setHorizontalOffset(list_margin / 2);
                            mPop_IMPL.setVerticalOffset(list_margin);
                        }
                    });


        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void initMarker() {
        RealmResults<MonitoringStationBean> monitoringStationBeen = RealmUtil.loadAllStation(realm);

        markerManager = new MarkerManager(getContext());
        markerManager.generateMarker(monitoringStationBeen);
        markerManager.setPanelControl(panelControl);

        Iterator<MarkView> markViewIterator = markerManager.iteratorAllMarkView();
        while (markViewIterator.hasNext()) {
            MarkView markView = markViewIterator.next();
            Marker marker = aMap.addMarker(markView.getMarkerOptions());
            marker.setObject(markView.getSluiceID());
            markView.setMarker(marker);
        }

        //aMap.addPolyline(markerManager.getPolylineOptions());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            panelControl = ((MainActivity) context).slidePanelEventControlIMPL;
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

    private void showSearchViewMask() {
        new MaterialIntroView.Builder(getActivity()).enableDotAnimation(true)
                .enableFadeAnimation(true)
                .enableIcon(false)
                .performClick(false)
                .dismissOnTouch(true)
                .setTarget(searchView)
                .setInfoText("点击搜索图标进行搜索")
                .setUsageId("mask3")
                .setShape(ShapeType.RECTANGLE)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

//    @Subscribe
//    public void changeMapMode(Boolean isNight){
//        if(aMap != null){
//            setMapMode(isNight);
//        }
//    }

    public void setMapMode(boolean isNight){
        if (isNight) {
            aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        } else {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    public void moveMapToStation(int id) {
        MonitoringStationBean stationBean = RealmUtil.loadStationDataById(realm,id);
        double la = Double.parseDouble(stationBean.getLatitude());
        double lo = Double.parseDouble(stationBean.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(new LatLng(la,lo));
//        aMap.animateCamera(cameraUpdate,1000L,null);
        List<Marker> markers = aMap.getMapScreenMarkers();
        int i = 0;
        for (Marker m : markers) {
            int temp = (int) m.getObject();
            if (temp == id) {
                markerManager.selectMarker(i,m);
            }
            i++;
        }
        aMap.removecache();
        aMap.moveCamera(cameraUpdate);
    }

    public void clearSearchViewFocus() {
        if (searchView != null) {
            searchView.clearFocus();
        }
    }
}
