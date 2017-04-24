package com.app.feng.waterlevelwatcher.utils.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.view.MarkView;
import com.app.feng.waterlevelwatcher.utils.Utils;

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

    MarkView selectView;

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
        int i = 0;
        for (MarkView m : markViewList) {
            if (temp == m.getSluiceID()) {
                //find this MarkView
                selectMarker(i,marker);
                //提示Activity弹窗
                panelControler.openPanel(m.getSluiceID());
                break;
            }
            i++;
        }
    }

    public void selectMarker(int index,Marker marker) {
        MarkView markView = markViewList.get(index);
        if (selectView == markView) {
            return;
        }

        if (selectView != null) {
            selectView.resetSelect();
            Bitmap bitmap = Utils.getViewBitmap(selectView);
            selectView.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            bitmap.recycle();
        }

        selectView = markView;
        selectView.select();
        Bitmap bitmap = Utils.getViewBitmap(selectView);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        bitmap.recycle();
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
