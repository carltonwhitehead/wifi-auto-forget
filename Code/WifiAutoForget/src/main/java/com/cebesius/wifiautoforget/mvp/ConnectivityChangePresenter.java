package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

/**
 * Presenter for the Connectivity Change flow
 */
public class ConnectivityChangePresenter {

    private final ConnectivityChangeModel model;
    private final ConnectivityChangeView view;

    public ConnectivityChangePresenter(ConnectivityChangeModel model, ConnectivityChangeView view) {
        this.model = model;
        this.view = view;
    }

    public void present() {
        if (model.isConnectedWifi()) {
            onJoinWifi();
        } else {
            onJoinOther();
        }
    }

    private void onJoinWifi() {
        if (model.isConnectedWifiUnknown()) {
            AutoForgetWifi autoForgetWifi = model.buildNetwork();
            ConnectivityChangeView.NotificationVerbosity notificationVerbosity = chooseNotificationVerbosity();
            view.showUnknownWifiNotification(autoForgetWifi, notificationVerbosity);
        } else if (model.isConnectedWifiPermanentAutoForget()) {
            // no-op
        }
        model.autoForget();
    }

    /**
     * Choose the appropriate notification verbosity
     * @return the NotificationVerbosity to be used when setting up the notification
     */
    private ConnectivityChangeView.NotificationVerbosity chooseNotificationVerbosity() {
        int addWifiNotificationActionCount = model.getUsedAddWifiNotificationActionCount();
        boolean usedAllAddWifiNotificationActionTypes = model.hasUsedAllAddWifiNotificationActionTypes();
        return addWifiNotificationActionCount >= 10
                || usedAllAddWifiNotificationActionTypes
                ? ConnectivityChangeView.NotificationVerbosity.BRIEF
                : ConnectivityChangeView.NotificationVerbosity.EXTENDED;
    }

    private void onJoinOther() {
        model.autoForget();
    }
}
