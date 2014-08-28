package com.cebesius.wifiautoforget.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;

import com.cebesius.wifiautoforget.util.BusPortal;

/**
 * The base MVP View for use with a Fragment
 */
abstract public class FragmentView {

    private final Fragment fragment;
    private final BusPortal busPortal;

    protected FragmentView(Fragment fragment, BusPortal busPortal) {
        this.fragment = fragment;
        this.busPortal = busPortal;
    }

    protected Fragment getFragment() {
        return fragment;
    }

    protected Activity getFragmentActivity() {
        return fragment.getActivity();
    }

    protected Resources getResources() {
        return fragment.getResources();
    }

    protected BusPortal getBusPortal() {
        return busPortal;
    }

    public abstract FragmentViewProxy getFragmentViewProxy();
}
