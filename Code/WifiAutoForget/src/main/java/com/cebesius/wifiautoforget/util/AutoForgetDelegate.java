package com.cebesius.wifiautoforget.util;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;

import java.util.List;


/**
 * This delegate implements the AutoForget flow
 */
public class AutoForgetDelegate {

    private static final String TAG = AutoForgetWifi.Behavior.class.getSimpleName();

    private final AutoForgetWifiStorage autoForgetWifiStorage;
    private final WifiManager wifiManager;

    public AutoForgetDelegate(AutoForgetWifiStorage autoForgetWifiStorage, WifiManager wifiManager) {
        this.autoForgetWifiStorage = autoForgetWifiStorage;
        this.wifiManager = wifiManager;
    }

    /**
     * Forgets and purges all SingleAutoForget networks.
     * Forgets all Permanent AutoForget networks.
     */
    public void autoForget() {
        List<AutoForgetWifi> autoForgetWifis = autoForgetWifiStorage.getAllAutoForgetWifis();
        WifiManager.WifiLock wifiLock = wifiManager.createWifiLock(AutoForgetDelegate.class.getCanonicalName());
        wifiLock.acquire();
        for (AutoForgetWifi autoForgetWifi : autoForgetWifis) {
            AutoForgetWifi.Behavior behavior = autoForgetWifi.getBehavior();
            if (behavior == null) {
                continue;
            }
            switch (autoForgetWifi.getBehavior()) {
                case SINGLE:
                    boolean wasForgotten = forget(autoForgetWifi);
                    if (wasForgotten) {
                        purge(autoForgetWifi);
                    }
                    break;
                case PERMANENT:
                    forget(autoForgetWifi);
                    break;
                case NEVER:
                    // no-op
                    break;
            }
        }
        wifiLock.release();
    }

    /**
     * Delete a Network from storage, unless currently connected to it
     * @param autoForgetWifi the network to delete
     *
     */
    private void purge(AutoForgetWifi autoForgetWifi) {
        if (isCurrentlyConnected(autoForgetWifi)) {
            // Do not purge connected wifis
            return;
        }
        autoForgetWifiStorage.delete(autoForgetWifi);
    }

    /**
     * Tell the system WifiManager to forget the Network, unless currently connected to it
     * @param autoForgetWifi
     * @return true if the network was forgotten or false if not
     */
    private boolean forget(AutoForgetWifi autoForgetWifi) {
        if (autoForgetWifi == null) {
            return false;
        }
        if (isCurrentlyConnected(autoForgetWifi)) {
            // Do not forget connected wifis
            return false;
        }
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
        if (wifiConfigurations == null) {
            // WifiManager can't even tell us what networks it has
            return false;
        }
        for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
            if (autoForgetWifi.represents(wifiConfiguration)) {
                wifiManager.removeNetwork(wifiConfiguration.networkId);
                return wifiManager.saveConfiguration();
            }
        }
        return false; // no match was found
    }

    private boolean isCurrentlyConnected(AutoForgetWifi autoForgetWifi) {
        if (autoForgetWifi == null) {
            return false;
        }
        WifiInfo connectedWifi = wifiManager.getConnectionInfo();
        return autoForgetWifi.represents(connectedWifi);
    }
}
