package com.app.feng.waterlevelwatcher.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;
import com.app.feng.waterlevelwatcher.R;

import java.util.List;


/**
 * Created by feng on 2017/4/4.
 */

public abstract class FixTableAdapter implements IDataAdapter {

    public String[] titles;


    public FixTableAdapter(String[] titles) {
        this.titles = titles;
    }

    @Override
    public String getTitleAt(int pos) {
        return titles[pos];
    }

    @Override
    public int getTitleCount() {
        return titles.length;
    }

    @Override
    public void convertData(int i,List<TextView> bindViews) {
        for (TextView bindView : bindViews) {
            bindView.setLines(1);
            int itemWidth = bindView.getContext()
                    .getResources()
                    .getDimensionPixelOffset(R.dimen.table_item_width);
            bindView.setMaxWidth(itemWidth);
            bindView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    @Override
    public void convertLeftData(int i,TextView textView) {
        textView.setLines(1);

    }
}
