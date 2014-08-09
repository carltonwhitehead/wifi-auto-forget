package com.cebesius.wifiautoforget;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.cebesius.wifiautoforget.mvp.AppModel;
import com.cebesius.wifiautoforget.mvp.AppPresenter;
import com.cebesius.wifiautoforget.mvp.AppView;
import com.cebesius.wifiautoforget.util.BusPortal;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import antoche.HockeySender;

@ReportsCrashes(formKey = Secrets.HOCKEYAPP_APP_ID)
public class WifiAutoForgetApplication extends Application {

    private static WifiAutoForgetApplication instance;

    public static WifiAutoForgetApplication getInstance() {
        return instance;
    }

    private AppPresenter presenter;

    @Override
    public void onCreate() {
        initializeACRA();
        super.onCreate();
        this.instance = this;
        presenter = new AppPresenter(
                new AppModel(),
                new AppView(this)
        );
        ActiveAndroid.initialize(this);
        BusPortal.getInstance().register(presenter);
    }

    private void initializeACRA() {
        if (BuildConfig.FEATURE_CRASH_REPORTS_ENABLED) {
            ACRA.init(this);
            ACRA.getErrorReporter().setReportSender(new HockeySender());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BusPortal.getInstance().unregister(presenter);
        ActiveAndroid.dispose();
    }
}
