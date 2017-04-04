package com.app.feng.waterlevelwatcher.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.R;

/**
 * Created by feng on 2017/4/4.
 */

public class StatisticsAdapter extends PagerAdapter {


    public String[] title = {"title1","title2","title3","title4","title5","title6","title7",
                             "title8","title9"};

    public String[][] data = {
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"},
            {"data1","data2","data3","data4","data5","data6","data7","data8","data9"}};

    String[] pageTitleArray;
    FixTableLayout fixTableLayout;
    TextView tvTime;

    SparseArray<View> viewSparseArray;

    public StatisticsAdapter(Context context) {
        pageTitleArray = context.getResources()
                .getStringArray(R.array.statistics_title);

        viewSparseArray = new SparseArray<>(pageTitleArray.length);

    }

    @Override
    public int getCount() {
        return pageTitleArray.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position) {
        View view = viewSparseArray.get(position);
        if (view == null) {
            view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.pageradapter_item,container,false);
            viewSparseArray.put(position,view);

            fixTableLayout = (FixTableLayout) view.findViewById(R.id.fixtablelayout);
            tvTime = (TextView) view.findViewById(R.id.tv_time_string);

            fixTableLayout.setAdapter(new FixTableAdapter(title,data));

        }

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
