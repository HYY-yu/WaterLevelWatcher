package com.app.feng.waterlevelwatcher.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.feng.waterlevelwatcher.R;

/**
 * Created by feng on 2017/4/18.
 */

public abstract class BaseLoadingFragment extends Fragment {

    View errorView;
    View loadingView;

    @Override
    public View onCreateView(
            LayoutInflater inflater,@Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        loadingView = LayoutInflater.from(getContext())
                .inflate(R.layout.loading_view,null);
        loadingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        errorView = LayoutInflater.from(getContext())
                .inflate(R.layout.error_view,null);
        errorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void loadingData() {
        RelativeLayout rootView = (RelativeLayout) getView();
        rootView.addView(loadingView,rootView.getChildCount(),
                         new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                         ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public boolean isLoading() {
        return loadingView.getParent() != null;
    }



    public void loadComplete() {
        RelativeLayout rootView = (RelativeLayout) getView();
        rootView.removeView(loadingView);
    }

    public void loadError() {
        final RelativeLayout rootView = (RelativeLayout) getView();
        rootView.removeView(errorView);
        rootView.addView(errorView,rootView.getChildCount(),
                         new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                         ViewGroup.LayoutParams.MATCH_PARENT));
        final View reloadBtn = errorView.findViewById(R.id.btn_reload);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.removeView(errorView);
                rootView.removeView(loadingView);
                reloadingData();
            }
        });
    }

    protected void reloadingData() {

    }

}
