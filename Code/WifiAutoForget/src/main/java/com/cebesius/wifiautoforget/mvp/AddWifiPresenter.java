package com.cebesius.wifiautoforget.mvp;


import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

/**
 * Presenter for Add Wifi flow
 */
public class AddWifiPresenter {

    private final AddWifiModel model;
    private final AddWifiView view;

    public AddWifiPresenter(AddWifiModel model, AddWifiView view) {
        this.model = model;
        this.view = view;
    }

    public void addNetwork(AutoForgetWifi autoForgetWifi) {
        try {
            model.addNetwork(autoForgetWifi);
            view.onAddWifiSuccess(autoForgetWifi);
            model.incrementAddWifiNotificationActionCount();
            model.setAddWifiNotificationTypeUsed(autoForgetWifi.getBehavior());
        } catch (Exception e) {
            view.onAddWifiFailure(autoForgetWifi);
        }
    }
}
