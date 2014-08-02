package com.cebesius.wifiautoforget.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

@Table(name = "AutoForgetWifis")
public class AutoForgetWifiModel extends Model {

    public static final String COLUMN_SSID = "ssid";
    public static final String COLUMN_BEHAVIOR = "behavior";

    @Column(name = COLUMN_SSID)
    public String ssid;
    @Column(name = COLUMN_BEHAVIOR)
    public AutoForgetWifi.Behavior behavior;

    public AutoForgetWifiModel() {
        super();
    }

    public AutoForgetWifiModel(String ssid, AutoForgetWifi.Behavior behavior) {
        super();
        this.ssid = ssid;
        this.behavior = behavior;
    }
}
