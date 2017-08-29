package com.app.feng.waterlevelwatcher.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.ZDDM_StatisticsBean;
import com.app.feng.waterlevelwatcher.network.BaseSubscriber;
import com.app.feng.waterlevelwatcher.network.RetrofitManager;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.interfaces.DataService;
import com.app.feng.waterlevelwatcher.ui.FullScreenTableActivity;
import com.app.feng.waterlevelwatcher.ui.fragment.StatisticsFragment;
import com.app.feng.waterlevelwatcher.utils.ClassUtil;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.Utils;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 抽出大量可重用代码，统一管理在ClassUtils  当Page增加或者删除 ，只需在ClassUtil中处理相应逻辑即可
 * Created by feng on 2017/4/4.
 */

public class StatisticsAdapter extends PagerAdapter {

    private String[] pageTitleArray;

    //尝试hold所有的View，以内存空间的牺牲换取速度
    private SparseArray<View> viewSparseArray;

    private StatisticsFragment statisticsFragment;

    private Realm realm;

    public StatisticsAdapter(StatisticsFragment statisticsFragment,Realm realm) {
        pageTitleArray = statisticsFragment.getResources()
                .getStringArray(R.array.statistics_title);

        viewSparseArray = new SparseArray<>(pageTitleArray.length);

        this.statisticsFragment = statisticsFragment;
        this.realm = realm;
    }

    @Override
    public int getCount() {
        return pageTitleArray.length;
    }

    @Override
    public Object instantiateItem(final ViewGroup container,final int position) {
        View view = viewSparseArray.get(position);
        if (view == null) {
            view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.pageradapter_item,container,false);

            viewSparseArray.put(position,view);

            final String dEndTime = SharedPref.getInstance(statisticsFragment.getContext())
                    .getString(Config.KEY.DEFAULT_END_TIME,Utils.format(new Date()));

            mapViewData(view,position,dEndTime);
        }

        container.addView(view);

        return position;
    }

    private void mapViewData(View view,final int pos,String time) {

        TextView tvTime = (TextView) view.findViewById(R.id.tv_time_string);
        View btn_zoom_in = view.findViewById(R.id.btn_zoom_in);

        String tvTimeString = statisticsFragment.getResources()
                .getString(R.string.view_page_time);

        if (pos == 3 || pos == 4) {
            //截取s的日期部分
            time = time.split(" ")[0];
        }
        tvTime.setText(String.format(tvTimeString,time));

        Class clazz = ClassUtil.jugeClass(pos);
        final RealmResults schedulingAnalysisBeanList;

        // LoadData From DateBase
        if (pos == 3 || pos == 4) {
            schedulingAnalysisBeanList = RealmUtil.loadAllAllLineBeanByTimeString(clazz,realm,time);
        } else {
            schedulingAnalysisBeanList = RealmUtil.loadAllAllLineBeanByTime(clazz,realm,
                                                                            Utils.parse(time));
        }
        if (Utils.isListEmpty(schedulingAnalysisBeanList)) {
            //Database无数据，读网加载
            loadFromNetworkSyncAnalysis(clazz,pos,time);
        } else {
            setToAdapter(pos,clazz,schedulingAnalysisBeanList);
        }

        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        bundle.putString("time",time);
        btn_zoom_in.setTag(bundle);
        btn_zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(statisticsFragment.getContext(),
                                           FullScreenTableActivity.class);
                //在新的Activity中重新查询数据库   注意此时数据库必有数据
                Bundle b = (Bundle) v.getTag();
                String time = b.getString("time");
                intent.putExtra(Config.KEY.QUERY_TIME,time);
                int pos = b.getInt("pos");    ///当前位置
                intent.putExtra(Config.KEY.QUERY_POS,pos);

                FixTableLayout fixTableLayout = (FixTableLayout) viewSparseArray.get(pos)
                        .findViewById(R.id.fixtablelayout);
                statisticsFragment.startActivity(intent,
                                                 ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                         statisticsFragment.getActivity(),
                                                         fixTableLayout,"fixtable")
                                                         .toBundle());
            }
        });

    }

    private void setToAdapter(
            int pos,Class clazz,RealmResults beanList) {
        FixTableAdapter fixTableAdapter = ClassUtil.genFixTableAdapter(clazz,beanList);

        FixTableLayout fixTableLayout = (FixTableLayout) viewSparseArray.get(pos)
                .findViewById(R.id.fixtablelayout);
        fixTableLayout.setAdapter(fixTableAdapter);
    }

    private String tempTime;
    private int tempPos;

    private void loadFromNetworkSyncAnalysis(
            final Class clazz,final int pos,final String time) {
        tempPos = pos;
        tempTime = time;
        DataService dataService = RetrofitManager.getRetrofit()
                .create(DataService.class);
        Observable observable = ClassUtil.getObservable(clazz,time,dataService);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(statisticsFragment.getContext()) {
                    @Override
                    public void onStartLoad() {
                        if (!statisticsFragment.isLoading()) {
                            statisticsFragment.loadingData();
                        }
                    }

                    @Override
                    public void onEndLoad(boolean load) {
                        if (load) {
                            statisticsFragment.loadComplete();
                        } else {
                            statisticsFragment.loadError();
                        }
                    }

                    @Override
                    public void onSuccess(
                            ResponseBean responseBean) {

                        List analysisBeanList = responseBean.getDatas();
                        if (Utils.isListEmpty(analysisBeanList)) {
                            Toast.makeText(statisticsFragment.getContext(),"当前时间段没有数据",
                                           Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (pos == 4) {
                            for (ZDDM_StatisticsBean b : (List<ZDDM_StatisticsBean>) analysisBeanList) {
                                MonitoringStationBean stationBean = RealmUtil.loadStationDataById(
                                        realm,b.getJctid());
                                if (stationBean != null) {
                                    b.jctName = stationBean.getName();
                                }else{
                                    b.jctName = " ";
                                }
                            }
                        }

                        RealmUtil.saveToRealm(analysisBeanList);

                        final RealmResults beanList;
                        if (pos == 3 || pos == 4) {
                            beanList = RealmUtil.loadAllAllLineBeanByTimeString(clazz,realm,time);
                        } else {
                            beanList = RealmUtil.loadAllAllLineBeanByTime(clazz,realm,
                                                                          Utils.parse(time));
                        }

                        setToAdapter(pos,clazz,beanList);
                    }
                });
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object) {
        container.removeView(viewSparseArray.get(position));
    }

    @Override
    public boolean isViewFromObject(View view,Object object) {
        return view == viewSparseArray.get((Integer) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitleArray[position];
    }

    public void notifyCurrentPageTimeChange(int currentIndex,String time) {
        View view = viewSparseArray.get(currentIndex);
        mapViewData(view,currentIndex,time);
    }

    public void notifyReloadData() {
        loadFromNetworkSyncAnalysis(ClassUtil.jugeClass(tempPos),tempPos,tempTime);
    }

}
