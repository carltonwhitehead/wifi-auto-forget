package com.cebesius.wifiautoforget.adapter;

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

    private List<AutoForgetWifi> autoForgetWifis = new ArrayList<>();

    public AutoForgetWifisAdapter() {
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
    public View getView(int position, View recycleView, ViewGroup viewGroup) {
        AutoForgetWifiItem view = (AutoForgetWifiItem) recycleView;
        if (view == null) {
            view = new AutoForgetWifiItem(viewGroup.getContext());
        }
        view.setAutoForgetWifi(getItem(position));
        return view;
    }

    public void setAutoForgetWifis(List<AutoForgetWifi> autoForgetWifis) {
        if (this.autoForgetWifis != autoForgetWifis) {
            this.autoForgetWifis = autoForgetWifis;
            notifyDataSetChanged();
        }
    }
}