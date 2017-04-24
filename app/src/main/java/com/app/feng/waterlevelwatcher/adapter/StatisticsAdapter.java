package com.app.feng.waterlevelwatcher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.DataBean;
import com.app.feng.waterlevelwatcher.ui.FullScreenTableActivity;

import java.util.ArrayList;

/**
 * Created by feng on 2017/4/4.
 */

public class StatisticsAdapter extends PagerAdapter {

    public String[] title = {"title1","title2","title3","title4","title5","title6","title7",
                             "title8","title9"};
    ArrayList<DataBean> data = new ArrayList<>();

    String[] pageTitleArray;
    FixTableLayout fixTableLayout;
    TextView tvTime;
    View btn_zoom_in;

    SparseArray<View> viewSparseArray;

    Context context;

    public StatisticsAdapter(Context context) {
        pageTitleArray = context.getResources()
                .getStringArray(R.array.statistics_title);

        viewSparseArray = new SparseArray<>(pageTitleArray.length);

        for (int i = 0; i < 50; i++) {
            data.add(new DataBean("id__","data1","data2","data3","data4","data5","data6","data7",
                                  "data8"));
        }
        this.context = context;
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
        }

        fixTableLayout = (FixTableLayout) view.findViewById(R.id.fixtablelayout);
        tvTime = (TextView) view.findViewById(R.id.tv_time_string);
        btn_zoom_in = view.findViewById(R.id.btn_zoom_in);

        fixTableLayout.setAdapter(new FixTableAdapter(title,data));

        btn_zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FullScreenTableActivity.class);
                intent.putExtra(Config.KEY.FIXTABLE_TITLE,title);

                intent.putParcelableArrayListExtra(Config.KEY.FIXTABLE_DATA,data);

                FixTableLayout fixTableLayout = (FixTableLayout) viewSparseArray.get(position)
                        .findViewById(R.id.fixtablelayout);
                context.startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context,fixTableLayout,"fixtable")
                        .toBundle());
            }
        });

        container.addView(view);

        return position;
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
}
