package com.app.feng.waterlevelwatcher.utils;

import android.annotation.SuppressLint;

import com.app.feng.waterlevelwatcher.adapter.*;
import com.app.feng.waterlevelwatcher.bean.*;
import com.app.feng.waterlevelwatcher.network.interfaces.DataService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by feng on 2017/4/26.
 */

public class ClassUtil {

    public static Class jugeClass(int pos) {
        if (pos == 0) {
            //全线调度辅助分析
            return AllLineSchedulingAnalysisBean.class;
        } else if (pos == 1) {
            //流速统计
            return AllLineFlowSpeedBean.class;
        } else if (pos == 2) {
            //水深统计
            return AllLineWaterDepthBean.class;
        } else if (pos == 3) {
            //分水统计
            return FS_StatisticsBean.class;
        } else if (pos == 4) {
            //重点断面
            return ZDDM_StatisticsBean.class;
        } else {
            return Object.class;
        }
    }

    public static Observable getObservable(
            Class clazz,String time,DataService dataService) {
        Observable observable = null;
        if (clazz == AllLineSchedulingAnalysisBean.class) {
            observable = dataService.allScheduleAnalysis(time,-1,-1);
        } else if (clazz == AllLineFlowSpeedBean.class) {
            observable = dataService.allLineFLowSpeed(time,-1,-1);
        } else if (clazz == AllLineWaterDepthBean.class) {
            observable = dataService.allLineWaterDepth(time,-1,-1);
        } else if (clazz == FS_StatisticsBean.class) {
            observable = dataService.fsStatistics(time);
        }else if (clazz == ZDDM_StatisticsBean.class) {
            observable = dataService.zddmStatistics(time);
        }
        return observable;
    }

    @SuppressLint("WrongConstant")
    public static FixTableAdapter genFixTableAdapter(Class clazz,RealmResults beanList) {
        FixTableAdapter fixTableAdapter = null;
        if (clazz == AllLineSchedulingAnalysisBean.class) {
            fixTableAdapter = new AllLineSchedulingAnalysisFixTableAdapter(
                    AllLineSchedulingAnalysisBean.getTitles(),beanList);
        } else if (clazz == AllLineFlowSpeedBean.class) {
            AllLineFlowSpeedBean allLineFlowSpeedBean = (AllLineFlowSpeedBean) beanList.get(0);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(allLineFlowSpeedBean.queryDate);
            SimpleDateFormat format = new SimpleDateFormat("dd日 HH时");
            String[] titles = new String[14];

            for (int i = 0; i < titles.length; i++) {
                if (i == 0) {
                    titles[i] = "节制闸名称";
                    continue;
                }
                if (i == 1) {
                    titles[i] = "安装位置";
                    continue;
                }

                if (i != 2) {
                    currentCalendar.add(Calendar.HOUR_OF_DAY,-2);
                }

                titles[i] = format.format(currentCalendar.getTime());
            }


            fixTableAdapter = new AllLineFlowSpeedFixTableAdapter(titles,beanList);
        } else if (clazz == AllLineWaterDepthBean.class) {
            fixTableAdapter = new AllLineWaterDepthFixTableAdapter(
                    AllLineWaterDepthBean.getTitles(),beanList);
        } else if (clazz == FS_StatisticsBean.class) {
            fixTableAdapter = new FSStatisticsFixTableAdapter(FS_StatisticsBean.getTitles(),
                                                              beanList);
        } else if (clazz == ZDDM_StatisticsBean.class) {
            fixTableAdapter = new ZDDMStatisticsFixTableAdapter(ZDDM_StatisticsBean.getTitles(),
                                                              beanList);
        }

        return fixTableAdapter;
    }

    //    public static List ChangeType(
    //            Class clazz,List<LinkedTreeMap> analysisBeanList) throws IllegalAccessException {
    //        List tempList = new ArrayList();
    //        if (clazz == AllLineSchedulingAnalysisBean.class) {
    //            for (LinkedTreeMap map : analysisBeanList) {
    //                AllLineSchedulingAnalysisBean bean = new AllLineSchedulingAnalysisBean();
    //                Field[] fields = clazz.getDeclaredFields();
    //                for (Field field : fields) {
    //                    String name = field.getName();
    //                    field.setAccessible(true);
    //
    //                    Object value = map.get(name);
    //                    if(value instanceof Double){
    //                        Float f = Float.parseFloat(String.valueOf(map.get(name)));
    //                        field.set(bean,f);
    //                    }else{
    //                        field.set(bean,map.get(name));
    //
    //                    }
    //                }
    //                tempList.add(bean);
    //            }
    //        }
    //
    //        return tempList;
    //    }
}
