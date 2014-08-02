package com.cebesius.wifiautoforget.gateway;

import android.content.SharedPreferences;

/**
 * The storage gateway for accessing information about add wifi notification usage
 */
public class AddWifiNotificationUsageStorage {

    private final SharedPreferences sharedPreferences;

    public AddWifiNotificationUsageStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int getActionCount() {
        return sharedPreferences.getInt(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_COUNT, 0);
    }

    public void incrementActionCount() {
        int actionCount = getActionCount();
        sharedPreferences.edit().putInt(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_COUNT, ++actionCount).apply();
    }

    public boolean hasUsedSingle() {
        return sharedPreferences.getBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_SINGLE, false);
    }

    public boolean hasUsedPermanent() {
        return sharedPreferences.getBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_PERMANENT, false);
    }

    public boolean hasUsedIgnore() {
        return sharedPreferences.getBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_IGNORE, false);
    }

    public void setUsedSingle() {
        sharedPreferences.edit().putBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_SINGLE, true).apply();
    }

    public void setUsedPermanent() {
        sharedPreferences.edit().putBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_PERMANENT, true).apply();
    }

    public void setUsedIgnore() {
        sharedPreferences.edit().putBoolean(SharedPreferenceKeys.ADD_WIFI_NOTIFICATION_ACTION_USED_IGNORE, true).apply();
    }


}
