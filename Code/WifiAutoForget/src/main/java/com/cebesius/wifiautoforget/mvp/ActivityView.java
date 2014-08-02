package com.cebesius.wifiautoforget.mvp;

import android.app.Activity;

/**
 * The base MVP View for use with an Activity
 */
abstract public class ActivityView {

    private final Activity activity;

    protected ActivityView(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }
}
