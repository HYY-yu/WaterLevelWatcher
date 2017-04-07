package com.app.feng.waterlevelwatcher.utils;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by feng on 2017/3/11.
 */

public class AnimSetUtil {

    public static ScaleAnimation getScaleAnimationFABSHOW() {
        ScaleAnimation scaleAnimation_show = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,
                                                                0.5f,Animation.RELATIVE_TO_SELF,
                                                                0.5f);
        scaleAnimation_show.setDuration(300);
        return scaleAnimation_show;
    }

    public static ScaleAnimation getScaleAnimationFABHIDDEN() {
        ScaleAnimation scaleAnimation_hidden = new ScaleAnimation(1,0,1,0,
                                                                  Animation.RELATIVE_TO_SELF,0.5f,
                                                                  Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation_hidden.setDuration(300);
        return scaleAnimation_hidden;
    }
}
