package com.cebesius.wifiautoforget.fragment;

import android.app.Activity;
import android.preference.PreferenceManager;

import com.cebesius.wifiautoforget.activity.AutoForgetWifisActivity;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.gateway.UserPreferenceStorage;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisModel;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisView;
import com.cebesius.wifiautoforget.util.BusPortal;
import com.squareup.otto.Subscribe;

/**
 * Fragment that hosts the AutoForgetWifis MVP triad
 */
public class AutoForgetWifisFragment extends BaseFragment<AutoForgetWifisModel, AutoForgetWifisView, AutoForgetWifisPresenter> {

    public static final String TAG = AutoForgetWifisFragment.class.getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof AutoForgetWifisActivity)) {
            throw new IllegalArgumentException(AutoForgetWifisFragment.class.getSimpleName() + " may only attach to " + AutoForgetWifisActivity.class.getSimpleName());
        }
        super.onAttach(activity);
    }

    @Override
    protected AutoForgetWifisPresenter buildPresenter(AutoForgetWifisModel model, AutoForgetWifisView view) {
        return new AutoForgetWifisPresenter(
                model,
                view,
                BusPortal.getInstance()
        );
    }

    @Override
    protected AutoForgetWifisModel buildModel() {
        return new AutoForgetWifisModel(
                BusPortal.getInstance(),
                new AutoForgetWifiStorage(),
                new UserPreferenceStorage(PreferenceManager.getDefaultSharedPreferences(getActivity()))
        );
    }
    @Override
    protected AutoForgetWifisView buildView() {
        return new AutoForgetWifisView(
                this,
                BusPortal.getInstance()
        );
    }

    @Subscribe
    public void onRequestStartOnboarding(AutoForgetWifisPresenter.RequestStartOnboardingEvent event) {
        // TODO: start OnboardingActivity
    }
}
