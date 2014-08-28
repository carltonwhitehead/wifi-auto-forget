package com.cebesius.wifiautoforget.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment View Proxy interface which all Fragment View Proxy classes in this project must implement
 *
 * Allows the Fragment to delegate Android Fragment-specific behaviors that belong to the View
 * without having to hold a reference directly to the View, or to define any more public View
 * methods than are absolutely necessary.
 *
 * Implementations should be a public inner class of a FragmentView class.
 **/
public interface FragmentViewProxy {
    View onCreateView(LayoutInflater inflater, ViewGroup container);
    void onDestroyView();
    void onResume();
    void onPause();
    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle savedInstanceState);
}
