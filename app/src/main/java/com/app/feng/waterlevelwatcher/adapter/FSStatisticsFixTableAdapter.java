package com.app.feng.waterlevelwatcher.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.FS_StatisticsBean;

import java.util.List;

/**
 * Created by feng on 2017/4/27.
 */

public class FSStatisticsFixTableAdapter extends FixTableAdapter {

    private List<FS_StatisticsBean> data;

    public FSStatisticsFixTableAdapter(String[] titles,List<FS_StatisticsBean> data) {
        super(titles);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void convertData(int i,List<TextView> bindViews) {
        FS_StatisticsBean bean = data.get(i);

        for (TextView bindView : bindViews) {
            bindView.setLines(1);
            int itemWidth = bindView.getContext()
                    .getResources()
                    .getDimensionPixelOffset(R.dimen.table_item_width);
            bindView.setMaxWidth(itemWidth);
            bindView.setEllipsize(TextUtils.TruncateAt.END);
        }

        bindViews.get(0)
                .setText(bean.getFsknm());
        bindViews.get(1)
                .setText(String.valueOf(bean.getFlow()));
        bindViews.get(2)
                .setText(String.valueOf(bean.getCurflow()));
        bindViews.get(3)
                .setText(String.valueOf(bean.getTotalflow()));
    }

    @Override
    public void convertLeftData(int i,TextView textView) {
        FS_StatisticsBean bean = data.get(i);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(bean.getFsknm());
    }
}
