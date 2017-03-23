package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;

import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.PolylineOptions;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MarkView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by feng on 2017/3/11.
 */

public class MarkerManager {

    Context context;

    List<MarkView> markViewList = new ArrayList<>();
    ISlidePanelEventControl panelControler;

    public MarkerManager(Context context) {
        this.context = context;
    }

    public Iterator<MarkView> iteratorAllMarkView() {
        return markViewList.iterator();
    }

    public void generateMarker(RealmResults<MonitoringStationBean> monitoringStationBeen) {
        for (MonitoringStationBean b : monitoringStationBeen) {
            MarkView markView = new MarkView(context);
            markView.setPosition(b.getLongitude(),b.getLatitude())
                    .setSluiceID(b.getSluiceID());

            markViewList.add(markView);
        }
    }

    public void clickMarker(Marker marker) {
        int temp = (int) marker.getObject();
        for (MarkView m : markViewList) {
            if (temp == m.getSluiceID()) {
                //find this MarkView
                //提示Activity弹窗
                panelControler.openPanel(m.getSluiceID());
                break;
            }
        }
    }

    public void setPanelControl(ISlidePanelEventControl panelControl) {
        panelControler = panelControl;
    }

    public PolylineOptions getPolylineOptions() {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (MarkView markView : markViewList) {
            polylineOptions.add(markView.getPosition());
        }
        polylineOptions.width(3);
        polylineOptions.color(context.getResources()
                                      .getColor(R.color.colorAccent));
        return polylineOptions;
    }

    public void updateMarker(MonitoringStationBean theBean) {
        for (MarkView markView : markViewList) {
            if (markView.getSluiceID() == theBean.getSluiceID()) {
                markView.setPosition(theBean.getLongitude(),theBean.getLatitude());
                //更新Marker
                Marker marker = markView.getMarker();
                marker.setPosition(markView.getPosition());
            }
        }
    }
}