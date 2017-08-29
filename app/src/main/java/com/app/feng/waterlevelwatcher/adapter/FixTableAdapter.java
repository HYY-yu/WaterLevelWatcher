package com.app.feng.waterlevelwatcher.adapter;

import com.app.feng.fixtablelayout.inter.IDataAdapter;


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

}
