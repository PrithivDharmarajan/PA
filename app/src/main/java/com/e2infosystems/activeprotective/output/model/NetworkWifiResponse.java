package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class NetworkWifiResponse implements Serializable {
    private String SSID = "";
    private String Password = "";

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
