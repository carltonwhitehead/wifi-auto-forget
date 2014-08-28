package com.cebesius.wifiautoforget.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cebesius.wifiautoforget.R;
import com.cebesius.wifiautoforget.adapter.AutoForgetWifisAdapter;
import com.cebesius.wifiautoforget.dialog.ChangeAutoForgetBehaviorDialog;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.util.BusPortal;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.RequestEditAutoForgetWifiEvent;

/**
 * View for the AutoForgetWifis management screen
 */
public class AutoForgetWifisView extends FragmentView {

    @InjectView(R.id.list)
    ListView autoForgetWifisList;
    @InjectView(R.id.message_wrapper)
    RelativeLayout messageWrapperView;
    @InjectView(R.id.message_subject)
    TextView messageSubjectView;
    @InjectView(R.id.message_body)
    TextView messageBodyView;

    private final FragmentProxy fragmentProxy = new FragmentProxy();

    private AutoForgetWifisAdapter autoForgetWifisAdapter;

    public AutoForgetWifisView(Fragment fragment, BusPortal busPortal) {
        super(fragment, busPortal);
    }

    public FragmentProxy getFragmentViewProxy() {
        return fragmentProxy;
    }

    void showLoading() {
        getFragmentActivity().setProgressBarIndeterminate(true);
        getFragmentActivity().setProgressBarIndeterminateVisibility(true);
    }

    void hideLoading() {
        getFragmentActivity().setProgressBarIndeterminateVisibility(false);
    }

    void showAutoForgetWifis(List<AutoForgetWifi> autoForgetWifis) {
        setupAutoForgetWifisList(autoForgetWifis);
        setupMessage(null, null);
    }

    void showAutoForgetWifisListEmptyMessage() {
        setupAutoForgetWifisList(null);
        setupMessage(
                getResources().getString(R.string.autoforget_wifis_message_list_empty_subject),
                getResources().getString(R.string.autoforget_wifis_message_list_empty_body)
        );
    }

    void showAutoForgetWifiChangeBehaviorDialog(AutoForgetWifi autoForgetWifi) {
        ChangeAutoForgetBehaviorDialog dialog = ChangeAutoForgetBehaviorDialog.newInstance(autoForgetWifi);
        getFragmentActivity().getFragmentManager()
                .beginTransaction()
                .add(dialog, ChangeAutoForgetBehaviorDialog.TAG)
                .commit();
    }

    void onAutoForgetWifiBehaviorChanged(AutoForgetWifi autoForgetWifi) {
        autoForgetWifisAdapter.onAutoForgetWifiBehaviorChanged(autoForgetWifi);
    }

    @Subscribe
    public void onUserChangedAutoForgetEvent(ChangeAutoForgetBehaviorDialog.AutoForgetBehaviorChangedEvent event) {
        getBusPortal().post(new AutoForgetWifisPresenter.UserChangedAutoForgetBehaviorEvent(
                event.ssid,
                event.behavior
        ));
    }

    private void setupAutoForgetWifisList(List<AutoForgetWifi> autoForgetWifis) {
        if (autoForgetWifis != null) {
            if (autoForgetWifisAdapter == null) {
                autoForgetWifisAdapter = new AutoForgetWifisAdapter(autoForgetWifis);
                autoForgetWifisList.setAdapter(autoForgetWifisAdapter);
                autoForgetWifisList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        AutoForgetWifi autoForgetWifi = (AutoForgetWifi) adapterView.getItemAtPosition(position);
                        getBusPortal().post(new RequestEditAutoForgetWifiEvent(autoForgetWifi));
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

    public class FragmentProxy implements FragmentViewProxy {

        private FragmentProxy() {
            // no-op
        }

        @Override
        public void onResume() {
            getBusPortal().register(AutoForgetWifisView.this);
        }

        @Override
        public void onPause() {
            getBusPortal().unregister(AutoForgetWifisView.this);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            // no-op
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            // no-op
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container) {
            View view = inflater.inflate(R.layout.view_auto_forget_wifis, container, false);
            ButterKnife.inject(AutoForgetWifisView.this, view);
            return view;
        }

        @Override
        public void onDestroyView() {
            ButterKnife.reset(AutoForgetWifisView.this);
        }
    }
}
