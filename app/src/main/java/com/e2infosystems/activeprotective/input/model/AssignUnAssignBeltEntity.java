package com.e2infosystems.activeprotective.input.model;

import java.io.Serializable;

public class AssignUnAssignBeltEntity implements Serializable {

    private String deviceId = "";
    private String userId = "";
    private String userName = "";
    private String communityId = "";

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }



}
