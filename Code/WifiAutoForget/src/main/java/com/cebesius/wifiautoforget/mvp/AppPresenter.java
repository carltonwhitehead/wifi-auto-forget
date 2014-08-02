package com.cebesius.wifiautoforget.mvp;

import android.text.TextUtils;

import com.squareup.otto.Subscribe;

/**
 * A presenter to handle global app behaviors
 */
public class AppPresenter {

    private final AppModel model;
    private final AppView view;

    public AppPresenter(AppModel model, AppView view) {
        this.model = model;
        this.view = view;
    }

    @Subscribe
    public void onShowToastEvent(ShowToastEvent event) {
        if (!TextUtils.isEmpty(event.message)) {
            view.showToast(event.message, event.length);
        } else if (event.messageResId != 0) {
            view.showToast(event.messageResId, event.length);
        }
    }

    public static class ShowToastEvent {
        private final String message;
        private final int messageResId;
        private final int length;

        public ShowToastEvent(String message, int length) {
            this.message = message;
            this.messageResId = 0;
            this.length = length;
        }

        public ShowToastEvent(int messageResId, int length) {
            this.message = null;
            this.messageResId = messageResId;
            this.length = length;
        }
    }
}
