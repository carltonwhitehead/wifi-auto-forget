package com.cebesius.wifiautoforget.util;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.gateway.AutoForgetWifiStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Test for the AutoForget Delegate
 *
 * The AutoForget Delegate is the most essential class in the whole app,
 * so it's very important to have thorough test coverage on it
 */
@RunWith(RobolectricTestRunner.class)
public class AutoForgetDelegateTest {

    // tested object
    private AutoForgetDelegate delegate;

    // injected mock objects
    @Mock
    AutoForgetWifiStorage autoForgetWifiStorage;
    @Mock WifiManager wifiManager;
    @Mock WifiManager.WifiLock wifiLock;

    // injected real objects
    private List<AutoForgetWifi> autoForgetWifis;
    private Map<String, AutoForgetWifi> autoForgetWiFisBySsid;
    /**
     * real List object containing mock WifiConfiguration objects
     */
    private List<WifiConfiguration> wifiConfigurations;
    private Map<String, WifiConfiguration> wifiConfigurationsBySsid;
    private Map<String, WifiInfo> wifiInfosBySsid;

    // support fields
    private final String SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED = "disconnected single autoforget wifi";
    private final String SSID_SINGLE_AUTOFORGET_WIFI_CONNECTED = "connected single autoforget wifi";
    private final String SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED = "disconnected permanent autoforget wifi";
    private final String SSID_PERMANENT_AUTOFORGET_WIFI_CONNECTED = "connected permanent autoforget wifi";
    private final String SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED = "disconnected ignored wifi";
    private final String SSID_NEVER_AUTOFORGET_WIFI_CONNECTED = "connected ignored wifi";
    private final String SSID_UNKNOWN_WIFI_DISCONNECTED = "disconnected unknown wifi";
    private final String SSID_UNKNOWN_WIFI_CONNECTED = "connected unknown wifi";
    private final int NETWORKID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED = 1;
    private final int NETWORKID_SINGLE_AUTOFORGET_WIFI_CONNECTED = 2;
    private final int NETWORKID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED = 3;
    private final int NETWORKID_PERMANENT_AUTOFORGET_WIFI_CONNECTED = 4;
    private final int NETWORKID_NEVER_AUTOFORGET_WIFI_DISCONNECTED = 5;
    private final int NETWORKID_NEVER_AUTOFORGET_WIFI_CONNECTED = 6;
    private final int NETWORKID_UNKNOWN_WIFI_DISCONNECTED = 7;
    private final int NETWORKID_UNKNOWN_WIFI_CONNECTED = 8;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        autoForgetWifis = new ArrayList<>();
        autoForgetWiFisBySsid = new HashMap<>();
        wifiConfigurations = new ArrayList<>();
        wifiConfigurationsBySsid = new HashMap<>();
        wifiInfosBySsid = new HashMap<>();

        // mock disconnected single autoforget wifi
        addMockWifi(
                SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED,
                AutoForgetWifi.Behavior.SINGLE,
                NETWORKID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED,
                false
        );
        // mock connected single autoforget wifi
        addMockWifi(
                SSID_SINGLE_AUTOFORGET_WIFI_CONNECTED,
                AutoForgetWifi.Behavior.SINGLE,
                NETWORKID_SINGLE_AUTOFORGET_WIFI_CONNECTED,
                true
        );
        // mock disconnected permanent autoforget wifi
        addMockWifi(
                SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED,
                AutoForgetWifi.Behavior.PERMANENT,
                NETWORKID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED,
                false
        );
        // mock connected permanent autoforget wifi
        addMockWifi(
                SSID_PERMANENT_AUTOFORGET_WIFI_CONNECTED,
                AutoForgetWifi.Behavior.PERMANENT,
                NETWORKID_PERMANENT_AUTOFORGET_WIFI_CONNECTED,
                true
        );
        // mock disconnected never autoforget wifi
        addMockWifi(
                SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED,
                AutoForgetWifi.Behavior.NEVER,
                NETWORKID_NEVER_AUTOFORGET_WIFI_DISCONNECTED,
                false
        );
        // mock connected never autoforget wifi
        addMockWifi(
                SSID_NEVER_AUTOFORGET_WIFI_CONNECTED,
                AutoForgetWifi.Behavior.NEVER,
                NETWORKID_NEVER_AUTOFORGET_WIFI_CONNECTED,
                true
        );
        // mock disconnected unknown wifi
        addMockWifi(
                SSID_UNKNOWN_WIFI_DISCONNECTED,
                null,
                NETWORKID_UNKNOWN_WIFI_DISCONNECTED,
                false
        );
        // mock connected unknown wifi
        addMockWifi(
                SSID_UNKNOWN_WIFI_CONNECTED,
                null,
                NETWORKID_UNKNOWN_WIFI_CONNECTED,
                true
        );

        when(wifiManager.createWifiLock(anyString())).thenReturn(wifiLock);
        when(wifiManager.getConfiguredNetworks()).thenReturn(wifiConfigurations);
        when(wifiManager.saveConfiguration()).thenReturn(true);

        delegate = new AutoForgetDelegate(autoForgetWifiStorage, wifiManager);
    }

    private void addMockWifi(String ssid, AutoForgetWifi.Behavior behavior, int networkId, boolean connected) {

        // mock Network
        AutoForgetWifi mockAutoForgetWifi = mock(AutoForgetWifi.class);
        when(mockAutoForgetWifi.getSsid()).thenReturn(ssid);
        when(mockAutoForgetWifi.getBehavior()).thenReturn(behavior);
        autoForgetWifis.add(mockAutoForgetWifi);
        autoForgetWiFisBySsid.put(ssid, mockAutoForgetWifi);

        // mock WifiConfiguration matching network
        WifiConfiguration mockWifiConfig = mock(WifiConfiguration.class);
        mockWifiConfig.SSID = ssid;
        mockWifiConfig.networkId = networkId;
        when(mockAutoForgetWifi.represents(mockWifiConfig)).thenReturn(true);
        wifiConfigurations.add(mockWifiConfig);
        wifiConfigurationsBySsid.put(ssid, mockWifiConfig);

        // mock WifiInfo matching network
        WifiInfo mockWifiInfo = mock(WifiInfo.class);
        when(mockWifiInfo.getSSID()).thenReturn(ssid);
        when(mockWifiInfo.getNetworkId()).thenReturn(networkId);
        when(mockAutoForgetWifi.represents(mockWifiInfo)).thenReturn(true);
        wifiInfosBySsid.put(ssid, mockWifiInfo);
    }

    @Test
    public void itShouldAlwaysAcquireAndForgetWifiLocks() {
        delegate.autoForget();
        verify(wifiManager).createWifiLock(anyString());
        verify(wifiLock).acquire();
        verify(wifiLock).release();
    }

    @Test
    public void itShouldForgetAndPurgeDisconnectedSingleAutoForgetWifis() {
        AutoForgetWifi sand = autoForgetWiFisBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(null);
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{sand}));

        delegate.autoForget();

        verify(wifiManager).removeNetwork(NETWORKID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED);
        verify(autoForgetWifiStorage).delete(sand);
    }

    @Test
    public void itShouldNeitherForgetNorPurgeConnectedSingleAutoForgetWifis() {
        AutoForgetWifi sanc = autoForgetWiFisBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_CONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfosBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_CONNECTED));
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{sanc}));

        delegate.autoForget();

        verify(wifiManager, never()).removeNetwork(NETWORKID_SINGLE_AUTOFORGET_WIFI_CONNECTED);
        verify(autoForgetWifiStorage, never()).delete(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldForgetButNotPurgeDisconnectedPermanentAutoForgetWifis() {
        AutoForgetWifi pand = autoForgetWiFisBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(null);
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{pand}));

        delegate.autoForget();

        verify(wifiManager).removeNetwork(NETWORKID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED);
        verify(autoForgetWifiStorage, never()).delete(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldNeitherForgetNorPurgeConnectedPermanentAutoForgetWifis() {
        AutoForgetWifi panc = autoForgetWiFisBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_CONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfosBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_CONNECTED));
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{panc}));

        delegate.autoForget();

        verify(wifiManager, never()).removeNetwork(NETWORKID_PERMANENT_AUTOFORGET_WIFI_CONNECTED);
        verify(autoForgetWifiStorage, never()).delete(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldNeitherForgetNorPurgeDisconnectedNeverAutoForgetWifis() {
        AutoForgetWifi iand = autoForgetWiFisBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfosBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED));
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{iand}));

        delegate.autoForget();

        verify(wifiManager, never()).removeNetwork(anyInt());
        verify(autoForgetWifiStorage, never()).delete(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldNeitherForgetNorPurgeConnectedNeverAutoForgetWifis() {
        AutoForgetWifi ianc = autoForgetWiFisBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_CONNECTED);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfosBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_CONNECTED));
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{ianc}));

        delegate.autoForget();

        verify(wifiManager, never()).removeNetwork(anyInt());
        verify(autoForgetWifiStorage, never()).delete(any(AutoForgetWifi.class));
    }

    @Test
    public void itShouldNeitherForgetNorPurgeDisconnectedUnknownWifis() {
        when(wifiManager.getConnectionInfo()).thenReturn(null);
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{
                autoForgetWiFisBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED),
                autoForgetWiFisBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED),
                autoForgetWiFisBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED)
        }));

        delegate.autoForget();

        verify(wifiManager, never()).removeNetwork(NETWORKID_UNKNOWN_WIFI_DISCONNECTED);
        verify(autoForgetWifiStorage, never()).delete(autoForgetWiFisBySsid.get(SSID_UNKNOWN_WIFI_DISCONNECTED));
    }

    @Test
    public void itShouldNeitherForgetNorPurgeConnectedUnknownWifis() {
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfosBySsid.get(SSID_UNKNOWN_WIFI_CONNECTED));
        when(autoForgetWifiStorage.getAllAutoForgetWifis()).thenReturn(Arrays.asList(new AutoForgetWifi[]{
                autoForgetWiFisBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED),
                autoForgetWiFisBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED),
                autoForgetWiFisBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED)
        }));

        delegate.autoForget();

        // verify tested network
        verify(wifiManager, never()).removeNetwork(NETWORKID_UNKNOWN_WIFI_CONNECTED);
        verify(autoForgetWifiStorage, never()).delete(autoForgetWiFisBySsid.get(SSID_UNKNOWN_WIFI_CONNECTED));

        // secondary verifications to catch any miscellaneous misbehavior
        verify(wifiManager, times(2)).removeNetwork(anyInt());
        verify(wifiManager, never()).removeNetwork(NETWORKID_NEVER_AUTOFORGET_WIFI_DISCONNECTED);
        verify(autoForgetWifiStorage, times(1)).delete(autoForgetWiFisBySsid.get(SSID_SINGLE_AUTOFORGET_WIFI_DISCONNECTED));
        verify(autoForgetWifiStorage, never()).delete(autoForgetWiFisBySsid.get(SSID_PERMANENT_AUTOFORGET_WIFI_DISCONNECTED));
        verify(autoForgetWifiStorage, never()).delete(autoForgetWiFisBySsid.get(SSID_NEVER_AUTOFORGET_WIFI_DISCONNECTED));
    }

}
