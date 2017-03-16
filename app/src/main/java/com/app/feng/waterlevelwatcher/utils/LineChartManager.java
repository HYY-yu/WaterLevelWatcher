package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 两种模式: 附着于Panel  显示在Fragment
 * 三个维度: 开度 闸前水位  闸后水位
 * Created by feng on 2017/3/10.
 */

public class LineChartManager {

    private Context context;

    public static int MODE_PANEL = 1;
    public static int MODE_FRAGMENT = 2;

    private int mode;

    private int axis_color;
    private int line_color;
    private int point_color;

    private List<String> xLabel = new ArrayList<>();

    public static LineChartManager lineChartManager;

    private LineChartManager(Context context) {
        this.context = context;

        axis_color = context.getResources()
                .getColor(R.color.divider_color);
        line_color = context.getResources()
                .getColor(R.color.colorAccent);
        point_color = context.getResources()
                .getColor(R.color.colorPrimary);

    }

    public static LineChartManager getInstanse(Context context) {
        if (lineChartManager == null) {
            lineChartManager = new LineChartManager(context);
        }
        return lineChartManager;
    }

    public void initChartData(
            LineChartView lineChartView,List<AxisValue> XValue,List<PointValue> data,String title,
            int mode) {
        Axis xAxis = new Axis(XValue);
        xAxis.setName(title);
        xAxis.setTextColor(point_color);
        xAxis.setHasLines(false);
        if (mode == MODE_PANEL) {
            xAxis.setHasTiltedLabels(true);
            xAxis.setMaxLabelChars(8);
            Viewport v = new Viewport(lineChartView.getMaximumViewport());
            v.left = 0;
            v.right = 5;
            lineChartView.setCurrentViewport(v);
        }

        Axis yAxis = new Axis();
        yAxis.setHasLines(true);

        Line line = new Line(data);
        line.setColor(line_color);
        line.setHasLabelsOnlyForSelected(true);

        List<Line> lineList = new ArrayList<>();
        lineList.add(line);

        LineChartData lineChartData = new LineChartData(lineList);
        lineChartData.setAxisXBottom(xAxis);
        lineChartData.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(lineChartData);
        lineChartView.invalidate();
    }

    public void initChart(
            LineChartView chartView,final ISlidePanelEventControl panelControl,int mode) {
        chartView.setZoomEnabled(true);
        chartView.setInteractive(true);
        chartView.setValueSelectionEnabled(true);
        chartView.setZoomType(ZoomType.HORIZONTAL);
        chartView.invalidate();

        if (mode == MODE_PANEL) {
            return;
        } else {
            //chartView.setZoomLevelWithAnimation(3,0,1.3f);
            chartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
                @Override
                public void onValueSelected(
                        int lineIndex,int pointIndex,PointValue value) {
                    String temp = xLabel.get(pointIndex);
                    panelControl.openPanel(Integer.parseInt(temp));
                }

                @Override
                public void onValueDeselected() {

                }
            });
        }
    }

    public List<AxisValue> mapXAxis(List<String> xLabel) {
        this.xLabel.addAll(xLabel);
        List<AxisValue> temp = new ArrayList<>();
        for (int i = 0; i < xLabel.size(); i++) {
            temp.add(new AxisValue(i).setLabel(xLabel.get(i)));
        }
        return temp;
    }

    public List<PointValue> mapDataValue(List<Float> y_data) {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < y_data.size(); i++) {
            pointValues.add(new PointValue((float) i,y_data.get(i)));
        }

        return pointValues;
    }

    public void changeData(LineChartView lineChartView,List<PointValue> data) {
        LineChartData linedata = lineChartView.getLineChartData();
        for (Line line : linedata.getLines()) {
            List<PointValue> oldValues = line.getValues();
            for (int i = 0; i < data.size(); i++) {
                PointValue oldValue = oldValues.get(i);
                oldValue.setTarget(oldValue.getX(),data.get(i)
                        .getY());
            }
        }
        lineChartView.startDataAnimation();
    }
}
