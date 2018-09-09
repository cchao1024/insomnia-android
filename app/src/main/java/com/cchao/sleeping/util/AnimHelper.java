package com.cchao.sleeping.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author cchao
 * @version 9/9/18.
 */
public class AnimHelper {

    public static void startRotate(View view) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view
            , "rotation", 0f, 360f);
        rotation.setDuration(3600);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatMode(ValueAnimator.RESTART);
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.start();
    }

    public static void cancel(View view) {
        if (view == null || view.getAnimation() == null) {
            return;
        }
        view.getAnimation().cancel();
    }
}