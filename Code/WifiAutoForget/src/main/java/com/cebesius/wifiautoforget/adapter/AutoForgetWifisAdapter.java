package com.cebesius.wifiautoforget.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.widget.AutoForgetWifiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * List adapter for AutoForgetWifi objects and AutoForgetWifiItem views
 */
public class AutoForgetWifisAdapter extends BaseAdapter {

    private List<AutoForgetWifi> autoForgetWifis;
    private final SparseArray<AutoForgetWifiItem> viewsByPosition = new SparseArray<>();

    public AutoForgetWifisAdapter(List<AutoForgetWifi> autoForgetWifis) {
        this.autoForgetWifis = autoForgetWifis;
    }

    @Override
    public int getCount() {
        return autoForgetWifis.size();
    }

    @Override
    public AutoForgetWifi getItem(int position) {
        return autoForgetWifis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AutoForgetWifiItem getView(int position, View recycleView, ViewGroup viewGroup) {
        AutoForgetWifiItem view = (AutoForgetWifiItem) recycleView;
        if (view == null) {
            view = new AutoForgetWifiItem(viewGroup.getContext());
        }
        int formerPositionOfView = viewsByPosition.indexOfValue(view);
        if (formerPositionOfView >= 0) {
            viewsByPosition.delete(formerPositionOfView);
        }
        viewsByPosition.put(position, view);
        view.setAutoForgetWifi(getItem(position));
        return view;
    }

    public void setAutoForgetWifis(List<AutoForgetWifi> autoForgetWifis) {
        if (this.autoForgetWifis != autoForgetWifis) {
            this.autoForgetWifis = autoForgetWifis;
            notifyDataSetChanged();
        }
    }

    public void onAutoForgetWifiBehaviorChanged(AutoForgetWifi autoForgetWifi) {
        int position = autoForgetWifis.indexOf(autoForgetWifi);
        if (position < 0) {
            // invalid object, abort
            return;
        }
        AutoForgetWifiItem view = viewsByPosition.get(position);
        if (view == null) {
            // view is off-screen, no need to update
            return;
        }
        view.onAutoForgetWifiBehaviorChanged(autoForgetWifi);
    }
}