package com.cebesius.wifiautoforget;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.cebesius.wifiautoforget.mvp.AppModel;
import com.cebesius.wifiautoforget.mvp.AppPresenter;
import com.cebesius.wifiautoforget.mvp.AppView;
import com.cebesius.wifiautoforget.util.BusPortal;

public class WifiAutoForgetApplication extends Application {

    private static WifiAutoForgetApplication instance;

    public static WifiAutoForgetApplication getInstance() {
        return instance;
    }

    private AppPresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        presenter = new AppPresenter(
                new AppModel(),
                new AppView(this)
        );
        ActiveAndroid.initialize(this);
        BusPortal.getInstance().register(presenter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BusPortal.getInstance().unregister(presenter);
        ActiveAndroid.dispose();
    }
}
