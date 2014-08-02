package com.cebesius.wifiautoforget.mvp;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AddWifiNotificationUsageStorage;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.util.AutoForgetDelegate;

/**
 * Implementation of ConnectivityChangeModel
 */
public class ConnectivityChangeModel {

    private final AutoForgetWifiStorage autoForgetWifiStorage;
    private final AddWifiNotificationUsageStorage addWifiNotificationUsageStorage;
    private final ConnectivityManager connectivityManager;
    private final WifiManager wifiManager;
    private final AutoForgetDelegate autoForgetDelegate;

    public ConnectivityChangeModel(AutoForgetWifiStorage autoForgetWifiStorage, ConnectivityManager connectivityManager, WifiManager wifiManager, AddWifiNotificationUsageStorage addWifiNotificationUsageStorage) {
        this.autoForgetWifiStorage = autoForgetWifiStorage;
        this.addWifiNotificationUsageStorage = addWifiNotificationUsageStorage;
        this.connectivityManager = connectivityManager;
        this.wifiManager = wifiManager;
        autoForgetDelegate = new AutoForgetDelegate(autoForgetWifiStorage, wifiManager);
    }

    public boolean isConnectedWifi() {
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    public void autoForget() {
        autoForgetDelegate.autoForget();
    }

    public boolean isConnectedWifiUnknown() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return !autoForgetWifiStorage.has(wifiInfo.getSSID());
    }

    public boolean isConnectedWifiPermanentAutoForget() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        AutoForgetWifi autoForgetWifi = autoForgetWifiStorage.load(wifiInfo.getSSID());
        if (autoForgetWifi == null) {
            return false;
        }
        return AutoForgetWifi.Behavior.PERMANENT == autoForgetWifi.getBehavior();
    }

    public AutoForgetWifi buildNetwork() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return new AutoForgetWifi(
                wifiInfo.getSSID(),
                null
        );
    }

    public int getUsedAddWifiNotificationActionCount() {
        return addWifiNotificationUsageStorage.getActionCount();
    }

    public boolean hasUsedAllAddWifiNotificationActionTypes() {
        return addWifiNotificationUsageStorage.hasUsedSingle()
                && addWifiNotificationUsageStorage.hasUsedPermanent()
                && addWifiNotificationUsageStorage.hasUsedIgnore();
    }
}
