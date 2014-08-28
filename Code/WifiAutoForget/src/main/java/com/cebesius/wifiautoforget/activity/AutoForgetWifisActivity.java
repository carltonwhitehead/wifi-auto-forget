package com.cebesius.wifiautoforget.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.fragment.AutoForgetWifisFragment;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.gateway.UserPreferenceStorage;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisModel;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter;
import com.cebesius.wifiautoforget.mvp.AutoForgetWifisView;
import com.cebesius.wifiautoforget.util.BusPortal;
import com.squareup.otto.Subscribe;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.*;

/**
 * Activity that hosts the AutoForgetWifis Fragment
 */
public class AutoForgetWifisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment_host);
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag(AutoForgetWifisFragment.TAG) == null) {
            fm.beginTransaction()
                    .add(R.id.root, new AutoForgetWifisFragment(), AutoForgetWifisFragment.TAG)
                    .commit();
        }
    }
}