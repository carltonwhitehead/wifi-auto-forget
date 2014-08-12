package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for Connectivity Change flow presenter
 */
@RunWith(RobolectricTestRunner.class)
public class ConnectivityChangePresenterTest {

    private ConnectivityChangePresenter presenter;

    @Mock ConnectivityChangeModel model;
    @Mock ConnectivityChangeView view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ConnectivityChangePresenter(model, view);
    }

    @Test
    public void itShouldHandleJoinUnknownWifi() {
        when(model.isConnectedWifi()).thenReturn(true);
        when(model.isConnectedWifiUnknown()).thenReturn(true);
        presenter.present();
        verify(view).showUnknownWifiNotification(any(AutoForgetWifi.class), any(ConnectivityChangeView.NotificationVerbosity.class));
        verify(model).autoForget();
    }

    @Test
    public void itShouldDoNothingSpecialWhenJoinPermanentAutoForgetWifi() {
        when(model.isConnectedWifi()).thenReturn(true);
        when(model.isConnectedWifiUnknown()).thenReturn(false);
        when(model.isConnectedWifiPermanentAutoForget()).thenReturn(true);
        presenter.present();
        verify(view, never()).showPermanentAutoForgetNotification(any(AutoForgetWifi.class), any(ConnectivityChangeView.NotificationVerbosity.class));
        verify(model).autoForget();
    }

    @Test
    public void itShouldHandleJoinOther() {
        when(model.isConnectedWifi()).thenReturn(false);
        presenter.present();
        verify(view, never()).showPermanentAutoForgetNotification(any(AutoForgetWifi.class), any(ConnectivityChangeView.NotificationVerbosity.class));
        verify(view, never()).showUnknownWifiNotification(any(AutoForgetWifi.class), any(ConnectivityChangeView.NotificationVerbosity.class));
        verify(model).autoForget();
    }
}
