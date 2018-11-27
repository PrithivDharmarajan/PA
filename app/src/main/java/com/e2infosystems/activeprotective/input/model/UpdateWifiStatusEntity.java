package com.e2infosystems.activeprotective.input.model;

import java.io.Serializable;

public class UpdateWifiStatusEntity implements Serializable {

    private String deviceId = "";
    private int wiFiConfiguredStatus = 0;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getWiFiConfiguredStatus() {
        return wiFiConfiguredStatus;
    }

    public void setWiFiConfiguredStatus(int wiFiConfiguredStatus) {
        this.wiFiConfiguredStatus = wiFiConfiguredStatus;
    }


}
