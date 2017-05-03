package com.app.feng.waterlevelwatcher.interfaces;

import java.io.Serializable;

/**
 * Created by feng on 2017/3/11.
 */

public interface ISlidePanelEventControl extends Serializable{
    void openPanel(int sluiceID);
    void closePanel();

    boolean isPanelOpen();

}
