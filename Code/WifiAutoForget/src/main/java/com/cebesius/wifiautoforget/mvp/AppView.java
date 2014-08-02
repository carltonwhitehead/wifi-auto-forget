package com.cebesius.wifiautoforget.mvp;

import android.widget.Toast;

import com.cebesius.wifiautoforget.WifiAutoForgetApplication;

/**
 *
 */
public class AppView {

    private WifiAutoForgetApplication application;

    public AppView(WifiAutoForgetApplication application) {
        this.application = application;
    }

    public void showToast(String message, int length) {
        Toast.makeText(application, message, length).show();
    }

    public void showToast(int messageResId, int length) {
        Toast.makeText(application, messageResId, length).show();
    }
}
