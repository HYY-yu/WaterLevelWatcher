package com.app.feng.waterlevelwatcher.interfaces;

/**
 * Created by feng on 2017/3/11.
 */

public interface ISlidePanelEventControl {
    void openPanel(int sluiceID);
    void closePanel();

    boolean isPanelOpen();

}
