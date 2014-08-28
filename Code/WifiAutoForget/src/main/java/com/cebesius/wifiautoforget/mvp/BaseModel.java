package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.util.BusPortal;

/**
 * Base Model from which all MVP model classes in this project must inherit
 */
public abstract class BaseModel {
    protected final BusPortal busPortal;

    public BaseModel(BusPortal busPortal) {
        this.busPortal = busPortal;
    }

    protected BusPortal getBusPortal() {
        return busPortal;
    }
}
