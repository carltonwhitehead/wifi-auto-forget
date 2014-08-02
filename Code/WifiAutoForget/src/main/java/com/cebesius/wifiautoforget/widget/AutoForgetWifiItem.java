package com.cebesius.wifiautoforget.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List item to display an AutoForgetWifi object
 */
public class AutoForgetWifiItem extends FrameLayout {

    @InjectView(R.id.ssid)
    TextView ssidView;
    @InjectView(R.id.behavior)
    TextView behaviorView;

    public AutoForgetWifiItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_autoforget_wifi, this, true);
        ButterKnife.inject(this, this);
    }

    public void setAutoForgetWifi(AutoForgetWifi autoForgetWifi) {
        setupSsid(autoForgetWifi);
        setupBehavior(autoForgetWifi);
    }

    private void setupSsid(AutoForgetWifi autoForgetWifi) {
        ssidView.setText(autoForgetWifi.getSsid());
    }

    private void setupBehavior(AutoForgetWifi autoForgetWifi) {
        String behaviorText;
        AutoForgetWifi.Behavior behavior = autoForgetWifi.getBehavior();
        Resources res = getResources();
        if (behavior != null) {
            behaviorText = res.getString(behavior.getLabelResId());
        } else {
            behaviorText = res.getString(R.string.autoforgetbehavior_unknown);
        }
        behaviorView.setText(behaviorText);
    }
}
