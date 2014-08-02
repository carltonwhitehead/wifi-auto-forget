package com.cebesius.wifiautoforget.mvp;


import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test for Add Wifi flow presenter
 */
@RunWith(RobolectricTestRunner.class)
public class AddWifiPresenterTest {

    private AddWifiPresenter presenter;

    @Mock AddWifiModel model;
    @Mock AddWifiView view;

    private AutoForgetWifi autoForgetWifi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new AddWifiPresenter(model, view);
        autoForgetWifi = new AutoForgetWifi(
                "unit-test-wifi",
                AutoForgetWifi.Behavior.SINGLE
        );
    }

    @Test
    public void itShouldAddNetwork() {
        presenter.addNetwork(autoForgetWifi);
        verify(model).addNetwork(autoForgetWifi);
        verify(view).onAddWifiSuccess(autoForgetWifi);
        verify(view, never()).onAddWifiFailure(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldFailToAddNetwork() {
        doThrow(Exception.class).when(model).addNetwork(autoForgetWifi);
        presenter.addNetwork(autoForgetWifi);
        verify(view).onAddWifiFailure(autoForgetWifi);
        verify(model, never()).incrementAddWifiNotificationActionCount();
        verify(model, never()).setAddWifiNotificationTypeUsed(any(AutoForgetWifi.Behavior.class));
    }
}
