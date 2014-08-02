package com.cebesius.wifiautoforget.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.preference.PreferenceManager;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AddWifiNotificationUsageStorage;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.gateway.NotificationIds;
import com.cebesius.wifiautoforget.mvp.AddWifiModel;
import com.cebesius.wifiautoforget.mvp.AddWifiPresenter;
import com.cebesius.wifiautoforget.mvp.AddWifiView;
import com.cebesius.wifiautoforget.util.BusPortal;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AddWifiService extends IntentService {

    public static final String EXTRA_SSID = "ssid";
    public static final String EXTRA_AUTOFORGET_BEHAVIOR = "autoForgetBehavior";

    private AddWifiPresenter addWifiPresenter;

    public AddWifiService() {
        super(AddWifiService.class.getCanonicalName());
    }

    public static final Intent buildIntent(Context context, AutoForgetWifi autoForgetWifi, AutoForgetWifi.Behavior behavior) {
        return new Intent(context, AddWifiService.class)
                .putExtra(EXTRA_SSID, autoForgetWifi.getSsid())
                .putExtra(EXTRA_AUTOFORGET_BEHAVIOR, behavior);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationIds.CONNECTED_UNKNOWN_WIFI);
        AutoForgetWifi autoForgetWifi = new AutoForgetWifi(
                intent.getStringExtra(EXTRA_SSID),
                (AutoForgetWifi.Behavior) intent.getSerializableExtra(EXTRA_AUTOFORGET_BEHAVIOR)
        );
        AddWifiModel addWifiModel = new AddWifiModel(
                new AutoForgetWifiStorage(),
                new AddWifiNotificationUsageStorage(PreferenceManager.getDefaultSharedPreferences(this)));
        AddWifiView addWifiView = new AddWifiView(this, BusPortal.getInstance());
        addWifiPresenter = new AddWifiPresenter(addWifiModel, addWifiView);
        addWifiPresenter.addNetwork(autoForgetWifi);
    }
}
