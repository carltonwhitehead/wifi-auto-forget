package com.cebesius.wifiautoforget.mvp;

import android.os.Bundle;

/**
 * Fragment Model Proxy interface which all Fragment Model Proxy classes in this project must implement
 *
 * Allows the Fragment to delegate Android Fragment-specific behaviors that belong to the Model
 * without having to hold a reference directly to the Model, or to define any more public Model
 * methods than are absolutely necessary
 *
 * Implementations should be a public inner class of a FragmentModel class.
 */
public interface FragmentModelProxy {
    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle savedInstanceState);
    void onResume();
    void onPause();
}
