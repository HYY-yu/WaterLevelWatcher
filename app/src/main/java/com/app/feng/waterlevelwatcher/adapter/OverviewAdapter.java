package com.app.feng.waterlevelwatcher.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.fragment.OverviewFragmentFSK;
import com.app.feng.waterlevelwatcher.ui.fragment.OverviewFragmentJZZ;

/**
 * Created by feng on 2017/5/2.
 */

public class OverviewAdapter extends FragmentPagerAdapter {

    private String[] pageTitleArray;

    private Context context;

    private ISlidePanelEventControl panelEventControl;

    public OverviewAdapter(Context context,FragmentManager fragmentManager,
                           ISlidePanelEventControl panelEventControl) {
        super(fragmentManager);
        pageTitleArray = context.getResources()
                .getStringArray(R.array.overview_title);

        this.context = context;
        this.panelEventControl = panelEventControl;
    }
    @Override
    public int getCount() {
        return pageTitleArray.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Fragment overviewFragmentJZZ = OverviewFragmentJZZ.newInstance(panelEventControl);
            return overviewFragmentJZZ;
        }else{
            Fragment overviewFragmentFSK = OverviewFragmentFSK.newInstance();
            return overviewFragmentFSK;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitleArray[position];
    }

}
