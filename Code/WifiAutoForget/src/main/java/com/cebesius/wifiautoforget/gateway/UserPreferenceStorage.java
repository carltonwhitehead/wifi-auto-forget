package com.cebesius.wifiautoforget.gateway;

import android.content.SharedPreferences;

import static com.cebesius.wifiautoforget.gateway.SharedPreferenceKeys.*;

/**
 * Storage wrapper for user preferences
 */
public class UserPreferenceStorage {

    private final SharedPreferences sharedPreferences;

    public UserPreferenceStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean hasUserCompletedOnboarding() {
        return sharedPreferences.getBoolean(USER_PREFERENCE_USER_COMPLETED_ONBOARDING, false);
    }

    public void setUserCompletedOnboarding(boolean userCompletedOnboarding) {
        sharedPreferences.edit().putBoolean(USER_PREFERENCE_USER_COMPLETED_ONBOARDING, userCompletedOnboarding).apply();
    }
}
