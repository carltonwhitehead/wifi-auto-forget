package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.util.BusPortal;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Presenter for the AutoForgetWifis management screen
 */
public class AutoForgetWifisPresenter {

    private final AutoForgetWifisModel model;
    private final AutoForgetWifisView view;
    private final BusPortal busPortal;

    public AutoForgetWifisPresenter(AutoForgetWifisModel model, AutoForgetWifisView view, BusPortal busPortal) {
        this.model = model;
        this.view = view;
        this.busPortal = busPortal;
    }

    public void init() {
        if (model.isOnboardingEnabled() && !model.hasUserCompletedOnboarding()) {
            busPortal.post(new RequestStartOnboardingEvent());
            return;
        }
        view.showLoading();
        model.loadAutoForgetWifis();
    }

    @Subscribe
    public void onAutoForgetWifisLoaded(AutoForgetWifisLoadedEvent event) {
        view.hideLoading();
        List<AutoForgetWifi> autoForgetWifis = model.getAutoForgetWifis();
        if (autoForgetWifis.size() > 0) {
            view.showAutoForgetWifis(autoForgetWifis);
        } else {
            view.showAutoForgetWifisListEmptyMessage();
        }
    }

    @Subscribe
    public void onRequestEditAutoForgetWifiEvent(RequestEditAutoForgetWifiEvent event) {
        view.showAutoForgetWifiChangeBehaviorDialog(event.autoForgetWifi);
    }

    @Subscribe
    public void onUserChangedAutoForgetBehavior(UserChangedAutoForgetBehaviorEvent event) {
        AutoForgetWifi autoForgetWifi = model.findAutoForgetWifiBySsid(event.ssid);
        model.setAutoForgetWifiBehavior(autoForgetWifi, event.behavior);
        view.onAutoForgetWifiBehaviorChanged(autoForgetWifi);
    }

    public static class RequestStartOnboardingEvent { }

    public static class AutoForgetWifisLoadedEvent { }

    public static class RequestEditAutoForgetWifiEvent {

        private final AutoForgetWifi autoForgetWifi;

        public RequestEditAutoForgetWifiEvent(AutoForgetWifi autoForgetWifi) {
            this.autoForgetWifi = autoForgetWifi;
        }
    }

    public static class UserChangedAutoForgetBehaviorEvent {

        private final String ssid;
        private final AutoForgetWifi.Behavior behavior;

        public UserChangedAutoForgetBehaviorEvent(String ssid, AutoForgetWifi.Behavior behavior) {
            this.ssid = ssid;
            this.behavior = behavior;
        }
    }
}
