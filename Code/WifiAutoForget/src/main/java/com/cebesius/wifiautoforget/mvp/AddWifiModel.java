package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AddWifiNotificationUsageStorage;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;

/**
 * Implementation of Add Wifi model
 */
public class AddWifiModel {

    private final AutoForgetWifiStorage autoForgetWifiStorage;
    private final AddWifiNotificationUsageStorage addWifiNotificationUsageStorage;

    public AddWifiModel(AutoForgetWifiStorage autoForgetWifiStorage, AddWifiNotificationUsageStorage addWifiNotificationUsageStorage) {
        this.autoForgetWifiStorage = autoForgetWifiStorage;
        this.addWifiNotificationUsageStorage = addWifiNotificationUsageStorage;
    }

    public void incrementAddWifiNotificationActionCount() {
        addWifiNotificationUsageStorage.incrementActionCount();
    }

    public void setAddWifiNotificationTypeUsed(AutoForgetWifi.Behavior behavior) {
        switch (behavior) {
            case SINGLE:
                addWifiNotificationUsageStorage.setUsedSingle();
                break;
            case PERMANENT:
                addWifiNotificationUsageStorage.setUsedPermanent();
                break;
            case NEVER:
                addWifiNotificationUsageStorage.setUsedIgnore();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized AutoForgetBehavior");
        }
    }

    public void addNetwork(AutoForgetWifi autoForgetWifi) {
        autoForgetWifiStorage.save(autoForgetWifi);
    }
}
