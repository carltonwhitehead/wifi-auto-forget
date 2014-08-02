package com.cebesius.wifiautoforget.domain;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;

import com.cebesius.wifiautoforget.R;

import java.io.Serializable;

/**
 * Represents a Wi-Fi network and behavior associated with it
 */
public class AutoForgetWifi implements Serializable {

    private String ssid;
    private Behavior behavior;

    public AutoForgetWifi(String ssid, Behavior behavior) {
        this.ssid = ssid;
        this.behavior = behavior;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public boolean represents(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return false;
        }
        return represents(wifiConfiguration.SSID);
    }

    public boolean represents(WifiInfo wifiInfo) {
        if (wifiInfo == null) {
            return false;
        }
        return represents(wifiInfo.getSSID());
    }

    private boolean represents(String ssid) {
        if (TextUtils.isEmpty(ssid)) {
            return false;
        } else if (!TextUtils.equals(this.ssid, ssid)) {
            return false;
        }
        return true;
    }

    public static AutoForgetWifi from(WifiConfiguration wifiConfiguration) {
        return new AutoForgetWifi(wifiConfiguration.SSID, null);
    }

    public static AutoForgetWifi from(WifiInfo wifiInfo) {
        return new AutoForgetWifi(wifiInfo.getSSID(), null);
    }

    public enum Behavior {
        SINGLE(R.string.autoforgetbehavior_single),
        PERMANENT(R.string.autoforgetbehavior_permanent),
        NEVER(R.string.autoforgetbehavior_never);

        private final int labelResId;

        Behavior(int labelResId) {
            this.labelResId = labelResId;
        }

        public int getLabelResId() {
            return labelResId;
        }
    }
}
