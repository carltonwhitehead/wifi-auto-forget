package com.cebesius.wifiautoforget.mvp;

import com.cebesius.wifiautoforget.util.BusPortal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.cebesius.wifiautoforget.mvp.AutoForgetWifisPresenter.*;
import static org.mockito.Mockito.*;

/**
 * Test for the Configuration presenter
 */
@RunWith(RobolectricTestRunner.class)
public class AutoForgetWifisPresenterTest {

    private AutoForgetWifisPresenter presenter;

    @Mock
    AutoForgetWifisModel model;
    @Mock
    AutoForgetWifisView view;
    @Mock BusPortal busPortal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new AutoForgetWifisPresenter(model, view, busPortal);
    }

    @Test
    public void itShouldStartOnboardingOnInitIfOnboardingIsEnabledAndIncomplete() {
        mockOnboardingEnabled();
        mockUserNotCompletedOnboarding();

        presenter.init();

        verify(busPortal).post(any(RequestStartOnboardingEvent.class));
    }

    @Test
    public void itShouldNotStartOnboardingOnInitIfOnboardingIsEnabledAndComplete() {
        mockOnboardingEnabled();
        mockUserCompletedOnboarding();

        presenter.init();

        verify(busPortal, never()).post(any(RequestStartOnboardingEvent.class));
    }

    @Test
    public void itShouldNotStartOnboardingOnInitIfOnboardingIsDisabled() {
        mockOnboardingDisabled();
        mockUserNotCompletedOnboarding();

        presenter.init();

        verify(busPortal, never()).post(any(RequestStartOnboardingEvent.class));
    }

    @Test
    public void itShouldNotStartOnboardingOnInitIfOnboardingIsDisabledAndComplete() {
        /*
         this is a fairly contrived case that should never occur in a release, but might
         come up in dev and potentially be irritating if behavior is undefined
         */
        mockOnboardingDisabled();
        mockUserCompletedOnboarding();

        presenter.init();

        verify(busPortal, never()).post(any(RequestStartOnboardingEvent.class));
    }

    private void mockOnboardingEnabled() {
        when(model.isOnboardingEnabled()).thenReturn(true);
    }

    private void mockOnboardingDisabled() {
        when(model.isOnboardingEnabled()).thenReturn(false);
    }

    private void mockUserCompletedOnboarding() {
        when(model.hasUserCompletedOnboarding()).thenReturn(true);
    }

    private void mockUserNotCompletedOnboarding() {
        when(model.hasUserCompletedOnboarding()).thenReturn(false);
    }
}
