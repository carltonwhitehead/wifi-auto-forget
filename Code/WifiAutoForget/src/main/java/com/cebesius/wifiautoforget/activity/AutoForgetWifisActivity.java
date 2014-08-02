package com.cebesius.wifiautoforget.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.gateway.UserPreferenceStorage;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisModel;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisView;
import com.cebesius.wifiautoforget.util.BusPortal;
import com.squareup.otto.Subscribe;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.*;

/**
 * Activity that hosts the AutoForgetWifis management screen
 */
public class AutoForgetWifisActivity extends Activity {

    private AutoForgetWifisPresenter presenter;
    private AutoForgetWifisModel.ActivityModelProxy modelProxy;
    private AutoForgetWifisView.ActivityViewProxy viewProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        // dependencies
        AutoForgetWifiStorage autoForgetWifiStorage = new AutoForgetWifiStorage();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BusPortal busPortal = BusPortal.getInstance();
        AutoForgetWifisModel model = new AutoForgetWifisModel(autoForgetWifiStorage, new UserPreferenceStorage(sharedPreferences), busPortal);
        AutoForgetWifisView view = new AutoForgetWifisView(this, busPortal);

        // MVP
        modelProxy = model.getActivityModelProxy();
        viewProxy = view.getActivityViewProxy();
        presenter = new AutoForgetWifisPresenter(model, view, busPortal);

        if (savedInstanceState != null) {
            modelProxy.restoreState(savedInstanceState);
            viewProxy.restoreState(savedInstanceState);
        }

        viewProxy.onCreateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusPortal.getInstance().register(this);
        BusPortal.getInstance().register(presenter);
        presenter.init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusPortal.getInstance().unregister(presenter);
        BusPortal.getInstance().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        modelProxy.saveState(outState);
        viewProxy.saveState(outState);
    }

    @Subscribe
    public void onRequestStartOnboarding(RequestStartOnboardingEvent event) {
        // TODO: start OnboardingActivity
    }
}
