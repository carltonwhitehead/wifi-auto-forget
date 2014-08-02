package com.cebesius.wifiautoforget.mvp;

import android.os.AsyncTask;
import android.os.Bundle;

import com.cebesius.wifiautoforget.BuildConfig;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.gateway.UserPreferenceStorage;
import com.cebesius.wifiautoforget.util.BusPortal;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.AutoForgetWifisLoadedEvent;

/**
 * Model for the AutoForgetWifis management screen
 */
public class AutoForgetWifisModel {

    private final ActivityModelProxy activityModelProxy = new ActivityModelProxy();
    private final AutoForgetWifiStorage autoForgetWifiStorage;
    private final UserPreferenceStorage userPreferenceStorage;
    private final BusPortal busPortal;
    private List<AutoForgetWifi> autoForgetWifis;
    private HashMap<String, AutoForgetWifi> autoForgetWifisBySsid;

    public AutoForgetWifisModel(AutoForgetWifiStorage autoForgetWifiStorage, UserPreferenceStorage userPreferenceStorage, BusPortal busPortal) {
        this.autoForgetWifiStorage = autoForgetWifiStorage;
        this.userPreferenceStorage = userPreferenceStorage;
        this.busPortal = busPortal;
    }

    boolean isOnboardingEnabled() {
        return BuildConfig.FEATURE_ONBOARDING_ENABLED;
    }

    boolean hasUserCompletedOnboarding() {
        return userPreferenceStorage.hasUserCompletedOnboarding();
    }

    public ActivityModelProxy getActivityModelProxy() {
        return activityModelProxy;
    }

    void loadAutoForgetWifis() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                autoForgetWifis = autoForgetWifiStorage.getAllAutoForgetWifis();
                autoForgetWifisBySsid = new HashMap<>(autoForgetWifis.size());
                for (AutoForgetWifi autoForgetWifi : autoForgetWifis) {
                    autoForgetWifisBySsid.put(autoForgetWifi.getSsid(), autoForgetWifi);
                }
                busPortal.post(new AutoForgetWifisLoadedEvent());
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    List<AutoForgetWifi> getAutoForgetWifis() {
        if (autoForgetWifis == null) {
            throw new IllegalStateException("Programmer error: must call loadAutoForgetWifis and receive success event before calling getAutoForgetWifis()");
        }
        return autoForgetWifis;
    }

    AutoForgetWifi findAutoForgetWifiBySsid(String ssid) {
        return autoForgetWifisBySsid.get(ssid);
    }

    void setAutoForgetWifiBehavior(final AutoForgetWifi autoForgetWifi, AutoForgetWifi.Behavior behavior) {
        autoForgetWifi.setBehavior(behavior);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                autoForgetWifiStorage.save(autoForgetWifi);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class ActivityModelProxy {

        private ActivityModelProxy() {
            // no-op
        }

        public void saveState(Bundle outState) {
            // TODO: save state
        }

        public void restoreState(Bundle savedInstanceState) {
            // TODO: restore state
        }
    }
}
