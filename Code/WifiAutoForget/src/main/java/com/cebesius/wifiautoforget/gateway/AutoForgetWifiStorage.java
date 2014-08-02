package com.cebesius.wifiautoforget.gateway;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.cebesius.wifiautoforget.domain.AutoForgetWifi;
import com.cebesius.wifiautoforget.model.AutoForgetWifiModel;

import java.util.ArrayList;
import java.util.List;

import static com.cebesius.wifiautoforget.domain.AutoForgetWifi.Behavior;
import static com.cebesius.wifiautoforget.model.AutoForgetWifiModel.COLUMN_BEHAVIOR;
import static com.cebesius.wifiautoforget.model.AutoForgetWifiModel.COLUMN_SSID;

/**
 * Storage gateway for AutoForgetWifi objects
 */
public class AutoForgetWifiStorage {

    public List<AutoForgetWifi> getAllAutoForgetWifis() {
        List<AutoForgetWifiModel> models = new Select().all().from(AutoForgetWifiModel.class)
                .execute();
        return mapAll(models);
    }

    public List<AutoForgetWifi> getSingleAndPermanentAutoForgetWifis() {
        List<AutoForgetWifiModel> models = new Select().all().from(AutoForgetWifiModel.class)
                .where(COLUMN_BEHAVIOR + " = ?", Behavior.SINGLE.name())
                .or(COLUMN_BEHAVIOR + " = ?", Behavior.PERMANENT.name())
                .execute();
        return mapAll(models);
    }

    public List<AutoForgetWifi> getSingleAutoForgetWifis() {
        List<AutoForgetWifiModel> models = new Select().all().from(AutoForgetWifiModel.class)
                .where(COLUMN_BEHAVIOR + " = ?", Behavior.SINGLE.name())
                .execute();
        return mapAll(models);
    }

    public List<AutoForgetWifi> getPermanentAutoForgetWifis() {
        List<AutoForgetWifiModel> models = new Select().all().from(AutoForgetWifiModel.class)
                .where(COLUMN_BEHAVIOR + " = ?", Behavior.PERMANENT.name())
                .execute();
        return mapAll(models);
    }

    public void delete(AutoForgetWifi autoForgetWifi) {
        if (autoForgetWifi == null) {
            return;
        }
        new Delete().from(AutoForgetWifiModel.class)
                .where(COLUMN_SSID + " = ?", autoForgetWifi.getSsid())
                .execute();
    }

    public void save(AutoForgetWifi autoForgetWifi) {
        if (autoForgetWifi == null) {
            return;
        }
        AutoForgetWifiModel model = find(autoForgetWifi);
        if (model == null) {
            model = new AutoForgetWifiModel();
        }
        model.ssid = autoForgetWifi.getSsid();
        model.behavior = autoForgetWifi.getBehavior();
        model.save();
    }

    public boolean has(String ssid) {
        return load(ssid) != null;
    }

    public AutoForgetWifi load(String ssid) {
        return map(find(new AutoForgetWifi(ssid, null)));
    }

    private AutoForgetWifiModel find(AutoForgetWifi autoForgetWifi) {
        return new Select().all().from(AutoForgetWifiModel.class)
                .where(COLUMN_SSID + " = ?", autoForgetWifi.getSsid())
                .executeSingle();
    }

    private AutoForgetWifi map(AutoForgetWifiModel autoForgetWifiModel) {
        if (autoForgetWifiModel == null) {
            return null;
        }
        return new AutoForgetWifi(
                autoForgetWifiModel.ssid,
                autoForgetWifiModel.behavior
        );
    }

    private List<AutoForgetWifi> mapAll(List<AutoForgetWifiModel> autoForgetWifiModels) {
        if (autoForgetWifiModels == null) {
            return null;
        }
        List<AutoForgetWifi> autoForgetWifis = new ArrayList<>(autoForgetWifiModels.size());
        for (AutoForgetWifiModel autoForgetWifiModel : autoForgetWifiModels) {
            autoForgetWifis.add(map(autoForgetWifiModel));
        }
        return autoForgetWifis;
    }
}
