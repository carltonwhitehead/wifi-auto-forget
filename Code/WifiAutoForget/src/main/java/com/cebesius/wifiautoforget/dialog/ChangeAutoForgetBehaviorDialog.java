package com.cebesius.wifiautoforget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.util.BusPortal;

/**
 * A dialog to let the user pick a different AutoForget.Behavior
 */
public class ChangeAutoForgetBehaviorDialog extends DialogFragment {

    private static final String KEY_AUTO_FORGET_WIFI_SSID = ChangeAutoForgetBehaviorDialog.class.getSimpleName() + ".autoForgetWifi.ssid";
    private static final String KEY_AUTO_FORGET_WIFI_BEHAVIOR = ChangeAutoForgetBehaviorDialog.class.getSimpleName() + ".autoForgetWifi.behavior";
    private static final String TAG = ChangeAutoForgetBehaviorDialog.class.getSimpleName();

    public static ChangeAutoForgetBehaviorDialog newInstance(AutoForgetWifi autoForgetWifi) {
        if (autoForgetWifi == null) {
            throw new IllegalArgumentException("Programmer error: no AutoForgetWifi instance passed");
        }
        ChangeAutoForgetBehaviorDialog dialog = new ChangeAutoForgetBehaviorDialog();
        Bundle args = new Bundle();
        args.putString(KEY_AUTO_FORGET_WIFI_SSID, autoForgetWifi.getSsid());
        args.putSerializable(KEY_AUTO_FORGET_WIFI_BEHAVIOR, autoForgetWifi.getBehavior());
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int checkedItem = -1;
        String ssid = getArguments().getString(KEY_AUTO_FORGET_WIFI_SSID);
        AutoForgetWifi.Behavior behavior = (AutoForgetWifi.Behavior) getArguments().getSerializable(KEY_AUTO_FORGET_WIFI_BEHAVIOR);
        AutoForgetWifi.Behavior[] behaviors = AutoForgetWifi.Behavior.values();
        String[] behaviorsLabels = new String[behaviors.length];
        for (int i = 0; i < behaviors.length; i++) {
            if (behavior == behaviors[i]) {
                checkedItem = i;
            }
            behaviorsLabels[i] = getString(behaviors[i].getLabelResId());
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(ssid)
                .setSingleChoiceItems(
                        behaviorsLabels,
                        checkedItem,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                Log.v(TAG, "onClick(position = " + position + ")");
                                BusPortal.getInstance().post(new AutoForgetBehaviorChangedEvent(
                                        getArguments().getString(KEY_AUTO_FORGET_WIFI_SSID),
                                        AutoForgetWifi.Behavior.values()[position]
                                ));
                                dismiss();
                            }
                        }
                )
                .create();
    }

    public static class AutoForgetBehaviorChangedEvent {
        public AutoForgetBehaviorChangedEvent(String ssid, AutoForgetWifi.Behavior behavior) {
            this.ssid = ssid;
            this.behavior = behavior;
        }

        public final String ssid;
        public final AutoForgetWifi.Behavior behavior;
    }
}
