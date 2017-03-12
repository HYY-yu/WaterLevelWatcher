package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;

import com.amap.api.maps2d.model.Marker;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MarkView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feng on 2017/3/11.
 */

public class MarkerManager {

    Context context;

    List<MarkView> markViewList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    ISlidePanelEventControl panelControler;

    public MarkerManager(Context context) {
        this.context = context;
    }

    public Iterator<MarkView> iteratorAllMarkView() {
        return markViewList.iterator();
    }

    public void generateMarker() {
        MarkView markView1 = new MarkView(context);
        MarkView markView2 = new MarkView(context);

        markView1.setPosition(116.397972,39.906901)
                .setFlag(1);
        markView2.setMode(MarkView.MODE_ALARM)
                .setFlag(13)
                .setPosition(116.490841,39.826784);

        markViewList.add(markView1);
        markViewList.add(markView2);
    }

    public void addMarkerFromAMap(List<Marker> mapScreenMarkers) {
        markerList.addAll(mapScreenMarkers);
    }

    public void clickMarker(Marker marker) {
        int temp = (int) marker.getObject();
        for (MarkView m : markViewList) {
            if (temp == m.getFlag()) {
                //find this MarkView

                //提示Activity弹窗
                panelControler.openPanel(temp);
                break;
            }
        }
    }

    public void setPanelControl(ISlidePanelEventControl panelControl) {
        panelControler = panelControl;
    }
}
