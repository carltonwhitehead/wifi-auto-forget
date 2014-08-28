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
public class AutoForgetWifisModel extends FragmentModel {

    private final FragmentProxy fragmentProxy = new FragmentProxy();
    private final AutoForgetWifiStorage autoForgetWifiStorage;
    private final UserPreferenceStorage userPreferenceStorage;
    private List<AutoForgetWifi> autoForgetWifis;
    private HashMap<String, AutoForgetWifi> autoForgetWifisBySsid;

    public AutoForgetWifisModel(BusPortal busPortal, AutoForgetWifiStorage autoForgetWifiStorage, UserPreferenceStorage userPreferenceStorage) {
        super(busPortal);
        this.autoForgetWifiStorage = autoForgetWifiStorage;
        this.userPreferenceStorage = userPreferenceStorage;
    }

    boolean isOnboardingEnabled() {
        return BuildConfig.FEATURE_ONBOARDING_ENABLED;
    }

    boolean hasUserCompletedOnboarding() {
        return userPreferenceStorage.hasUserCompletedOnboarding();
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
                getBusPortal().post(new AutoForgetWifisLoadedEvent());
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

    @Override
    public FragmentModelProxy getFragmentModelProxy() {
        return fragmentProxy;
    }

    public static class FragmentProxy implements FragmentModelProxy {

        private FragmentProxy() {
            // no-op
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            // no-op
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            // no-op
        }

        @Override
        public void onResume() {
        }

        @Override
        public void onPause() {

        }
    }
}
