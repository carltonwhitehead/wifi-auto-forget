package com.cebesius.wifiautoforget.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

import com.cebesius.wifiautoforget.gateway.AddWifiNotificationUsageStorage;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;
import com.cebesius.wifiautoforget.mvp.ConnectivityChangeModel;
import com.cebesius.wifiautoforget.mvp.ConnectivityChangePresenter;
import com.cebesius.wifiautoforget.mvp.ConnectivityChangeView;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ConnectivityChangeService extends IntentService {

    public static final String EXTRA_NETWORK_TYPE = ConnectivityManager.EXTRA_NETWORK_TYPE;
    /**
     * Starts this service to handle the given broadcast intent. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void start(Context context, Intent broadcastIntent) {
        Intent intent = new Intent(context, ConnectivityChangeService.class);
        intent.putExtra(EXTRA_NETWORK_TYPE, broadcastIntent.getIntExtra(EXTRA_NETWORK_TYPE, 0));
        context.startService(intent);
    }

    public ConnectivityChangeService() {
        super(ConnectivityChangeService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AutoForgetWifiStorage autoForgetWifiStorage = new AutoForgetWifiStorage();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        AddWifiNotificationUsageStorage addWifiNotificationUsageStorage = new AddWifiNotificationUsageStorage(
                PreferenceManager.getDefaultSharedPreferences(this)
        );
        ConnectivityChangePresenter presenter = new ConnectivityChangePresenter(
                new ConnectivityChangeModel(
                        autoForgetWifiStorage,
                        connectivityManager,
                        wifiManager,
                        addWifiNotificationUsageStorage
                ),
                new ConnectivityChangeView(this, notificationManager)
        );
        presenter.present();
    }
}
