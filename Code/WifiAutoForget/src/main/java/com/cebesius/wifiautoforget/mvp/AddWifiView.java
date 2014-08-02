package com.cebesius.wifiautoforget.mvp;

import android.content.Context;
import android.widget.Toast;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.util.BusPortal;

import static com.cebesius.wifiautoforget.mvp.AppPresenter.*;

/**
 * Implementation of Add Wifi view
 */
public class AddWifiView {

    private final Context context;
    private final BusPortal busPortal;

    public AddWifiView(Context context, BusPortal busPortal) {
        this.context = context;
        this.busPortal = busPortal;
    }

    public void onAddWifiSuccess(AutoForgetWifi autoForgetWifi) {
        String autoForgetBehaviorLabel = context.getString(
                autoForgetWifi.getBehavior().getLabelResId()
        );
        String message = context.getString(
                R.string.add_network_success,
                autoForgetWifi.getSsid(),
                autoForgetBehaviorLabel
        );
        busPortal.post(new ShowToastEvent(message, Toast.LENGTH_LONG));
    }

    public void onAddWifiFailure(AutoForgetWifi autoForgetWifi) {
        String autoForgetBehaviorLabel = context.getString(
                autoForgetWifi.getBehavior().getLabelResId()
        );
        String message = context.getString(
                R.string.add_network_failure,
                autoForgetWifi.getSsid(),
                autoForgetBehaviorLabel
        );
        busPortal.post(new ShowToastEvent(message, Toast.LENGTH_LONG));
    }
}
