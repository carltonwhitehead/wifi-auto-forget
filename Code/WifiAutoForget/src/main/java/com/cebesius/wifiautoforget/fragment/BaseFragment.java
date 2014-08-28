package com.cebesius.wifiautoforget.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cebesius.wifiautoforget.mvp.BasePresenter;
import com.cebesius.wifiautoforget.mvp.FragmentModel;
import com.cebesius.wifiautoforget.mvp.FragmentModelProxy;
import com.cebesius.wifiautoforget.mvp.FragmentView;
import com.cebesius.wifiautoforget.mvp.FragmentViewProxy;
import com.cebesius.wifiautoforget.util.BusPortal;

/**
 * Base Fragment from which all Fragment classes in this project must inherit
 */
public abstract class BaseFragment<M extends FragmentModel, V extends FragmentView, P extends BasePresenter> extends Fragment {

    private P presenter;
    private FragmentModelProxy modelProxy;
    private FragmentViewProxy viewProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter == null) {
            M model = buildModel();
            V view = buildView();
            presenter = buildPresenter(model, view);
            modelProxy = model.getFragmentModelProxy();
            viewProxy = view.getFragmentViewProxy();
        }
        if (savedInstanceState != null) {
            modelProxy.onRestoreInstanceState(savedInstanceState);
            viewProxy.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = viewProxy.onCreateView(inflater, container);
        if (savedInstanceState != null) {
            modelProxy.onRestoreInstanceState(savedInstanceState);
            viewProxy.onRestoreInstanceState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        modelProxy.onSaveInstanceState(outState);
        viewProxy.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusPortal.getInstance().register(this);
        BusPortal.getInstance().register(presenter);
        modelProxy.onResume();
        viewProxy.onResume();
        presenter.init();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewProxy.onPause();
        modelProxy.onPause();
        BusPortal.getInstance().unregister(presenter);
        BusPortal.getInstance().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewProxy.onDestroyView();
    }

    protected abstract M buildModel();
    protected abstract V buildView();
    protected abstract P buildPresenter(M model, V view);

    protected P getPresenter() {
        return presenter;
    }

    protected FragmentModelProxy getModelProxy() {
        return modelProxy;
    }

    protected FragmentViewProxy getViewProxy() {
        return viewProxy;
    }
}
