package com.cchao.simplelib.util;

import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by Jason on 2016/4/5.
 */
public class AnimationUtils {

    public static void showView(View view) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 100, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);

        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    public static void hideView(View view) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 100);
        animation.setDuration(500);
        animation.setFillAfter(true);

        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

}
