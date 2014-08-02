package com.cebesius.wifiautoforget.mvp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.NotificationIds;
import com.cebesius.wifiautoforget.service.AddWifiService;
import com.cebesius.wifiautoforget.util.BusPortal;

import static com.cebesius.wifiautoforget.mvp.AppPresenter.*;

/**
 * Implementation of ConnectivityChangeView
 */
public class ConnectivityChangeView {

    private static final String TAG = ConnectivityChangeView.class.getSimpleName();
    private static final int REQUEST_CODE_UNKNOWN_WIFI_SINGLE_AUTOFORGET = 0;
    private static final int REQUEST_CODE_UNKNOWN_WIFI_PERMANENT_AUTOFORGET = 1;
    private static final int REQUEST_CODE_UNKNOWN_WIFI_PERMANENT_IGNORE = 2;

    private final Context context;
    private final NotificationManager notificationManager;

    public ConnectivityChangeView(Context context, NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.context = context;
    }

    public void showUnknownWifiNotification(AutoForgetWifi autoForgetWifi, NotificationVerbosity notificationVerbosity) {
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_unknown_wifi_title))
                .setContentText(autoForgetWifi.getSsid());

        if (notificationVerbosity == NotificationVerbosity.EXTENDED) {
            notificationBuilder.setSubText(context.getString(R.string.notification_unknown_wifi_subtext));
        }

        // Single AutoForget action
        notificationBuilder.addAction(
                android.R.drawable.ic_input_add,
                context.getString(R.string.add_single_autoforget),
                PendingIntent.getService(
                        context,
                        REQUEST_CODE_UNKNOWN_WIFI_SINGLE_AUTOFORGET,
                        AddWifiService.buildIntent(
                                context,
                                autoForgetWifi,
                                AutoForgetWifi.Behavior.SINGLE
                        ),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        );

        // Permanent AutoForget action
        notificationBuilder.addAction(
                android.R.drawable.ic_input_add,
                context.getString(R.string.add_permanent_autoforget),
                PendingIntent.getService(
                        context,
                        REQUEST_CODE_UNKNOWN_WIFI_PERMANENT_AUTOFORGET,
                        AddWifiService.buildIntent(
                                context,
                                autoForgetWifi,
                                AutoForgetWifi.Behavior.PERMANENT
                        ),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        );

        // Permanent Ignore action
        notificationBuilder.setDeleteIntent(
                PendingIntent.getService(
                        context,
                        REQUEST_CODE_UNKNOWN_WIFI_PERMANENT_IGNORE,
                        AddWifiService.buildIntent(
                                context,
                                autoForgetWifi,
                                AutoForgetWifi.Behavior.NEVER
                        ),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        );

        notificationManager.notify(NotificationIds.CONNECTED_UNKNOWN_WIFI, notificationBuilder.build());
    }

    public void showPermanentAutoForgetNotification(AutoForgetWifi autoForgetWifi, NotificationVerbosity notificationVerbosity) {
        // TODO: implement showPermanentAutoForgetNotification()
        BusPortal.getInstance().post(new ShowToastEvent("TODO: show permanent AutoForget notification", Toast.LENGTH_SHORT));
        Log.v(TAG, "showPermanentAutoForgetNotification() not yet implemented");

    }

    public enum NotificationVerbosity {
        BRIEF,
        EXTENDED
    }
}
