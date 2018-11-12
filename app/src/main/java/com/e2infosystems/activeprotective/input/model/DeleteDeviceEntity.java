package com.e2infosystems.activeprotective.input.model;

import java.io.Serializable;

public class DeleteDeviceEntity implements Serializable {

    private String deviceId = "";
    private String userId = "0";

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
