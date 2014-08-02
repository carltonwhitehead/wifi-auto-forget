package com.cebesius.wifiautoforget.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.adapter.AutoForgetWifisAdapter;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.util.BusPortal;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.RequestEditAutoForgetWifiEvent;

/**
 * View for the AutoForgetWifis management screen
 */
public class AutoForgetWifisView extends ActivityView {

    @InjectView(R.id.list)
    ListView autoForgetWifisList;
    @InjectView(R.id.message_wrapper)
    RelativeLayout messageWrapperView;
    @InjectView(R.id.message_subject)
    TextView messageSubjectView;
    @InjectView(R.id.message_body)
    TextView messageBodyView;

    private final ActivityViewProxy activityViewProxy = new ActivityViewProxy();
    private final BusPortal busPortal;

    private AutoForgetWifisAdapter autoForgetWifisAdapter;

    public AutoForgetWifisView(Activity activity, BusPortal busPortal) {
        super(activity);
        this.busPortal = busPortal;
    }

    public ActivityViewProxy getActivityViewProxy() {
        return activityViewProxy;
    }

    void showLoading() {
        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    void hideLoading() {
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    void showAutoForgetWifis(List<AutoForgetWifi> autoForgetWifis) {
        setupAutoForgetWifisList(autoForgetWifis);
        setupMessage(null, null);
    }

    void showAutoForgetWifisListEmptyMessage() {
        setupAutoForgetWifisList(null);
        setupMessage(
                getActivity().getString(R.string.autoforget_wifis_message_list_empty_subject),
                getActivity().getString(R.string.autoforget_wifis_message_list_empty_body)
        );
    }

    private void setupAutoForgetWifisList(List<AutoForgetWifi> autoForgetWifis) {
        if (autoForgetWifis != null) {
            if (autoForgetWifisAdapter == null) {
                autoForgetWifisAdapter = new AutoForgetWifisAdapter();
                autoForgetWifisList.setAdapter(autoForgetWifisAdapter);
                autoForgetWifisList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Log.v("onItemClickListener", "position: " + position);
                        AutoForgetWifi autoForgetWifi = (AutoForgetWifi) adapterView.getItemAtPosition(position);
                        busPortal.post(new RequestEditAutoForgetWifiEvent(autoForgetWifi));
                    }
                });
            }
            autoForgetWifisAdapter.setAutoForgetWifis(autoForgetWifis);
            autoForgetWifisList.setVisibility(View.VISIBLE);
        } else {
            autoForgetWifisList.setVisibility(View.GONE);
        }
    }

    private void setupMessage(String subject, String body) {
        if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(body)) {
            messageSubjectView.setText(subject);
            messageBodyView.setText(body);
            messageWrapperView.setVisibility(View.VISIBLE);
        } else {
            messageWrapperView.setVisibility(View.GONE);
        }
    }

    public class ActivityViewProxy {

        private ActivityViewProxy() {
            // no-op
        }

        public void saveState(Bundle outState) {
            // TODO: save state
        }

        public void restoreState(Bundle savedInstanceState) {
            // TODO: restore state
        }

        public void onCreateView() {
            getActivity().setContentView(R.layout.activity_auto_forget_wifis);
            ButterKnife.inject(AutoForgetWifisView.this, getActivity());
        }
    }
}
