package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.util.BusPortal;

/**
 * Fragment Model interface which all Model implementations in this project that are intended to be
 * hosted by Fragments must implement
 */
public abstract class FragmentModel extends BaseModel {

    public FragmentModel(BusPortal busPortal) {
        super(busPortal);
    }

    public abstract FragmentModelProxy getFragmentModelProxy();
}
