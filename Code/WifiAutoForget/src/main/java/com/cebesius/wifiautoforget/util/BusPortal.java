package com.cebesius.wifiautoforget.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * A facade for a global Otto bus
 */
public class BusPortal {

    private static BusPortal instance;

    private final Bus bus = new Bus(ThreadEnforcer.MAIN);
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private BusPortal() { /* no-op */ }

    public static BusPortal getInstance() {
        if (instance == null) {
            instance = new BusPortal();
        }
        return instance;
    }

    public void register(Object object) {
        bus.register(object);
    }

    public void unregister(Object object) {
        bus.unregister(object);
    }

    public void post(final Object object) {
        // post to main thread, otherwise schedule runnable to post on main thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bus.post(object);
        } else {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(object);
                }
            });
        }
    }
}
