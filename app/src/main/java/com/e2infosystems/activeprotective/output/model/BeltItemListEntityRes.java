package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class BeltItemListEntityRes implements Serializable {

    private String devSSID = "";
    private long createdEpochMs = 0;
    private String accountId = "";
    private String devModal = "";
    private String communityId = "";
    private String devMAC = "";
    private String createdBy = "";
    private String deviceId = "";
    private int assignStatus = 0;
    private String communityName = "";
    private String devPasswd = "";
    private String devSize = "";
    private long modifiedEpochMs = 0;
    private int wiFiConfiguredStatus = 0;
    private String modifiedBy = "";

    private int buckleAlert = 0;
    private long modifedEpochMs = 0;
    private int systemAlert = 0;
    private int userAlert = 0;
    private int volumeLevel = 0;
    private int persButton = 0;
    private int vibrationLevel = 0;
    private int deviceAlwaysOn = 0;
    private int ledIntensity = 0;
    private int unBuckleAlert = 0;
    private String userId = "0";
    private String userName="";


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getModifiedEpochMs() {
        return modifiedEpochMs;
    }

    public void setModifiedEpochMs(long modifiedEpochMs) {
        this.modifiedEpochMs = modifiedEpochMs;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getBuckleAlert() {
        return buckleAlert;
    }

    public void setBuckleAlert(int buckleAlert) {
        this.buckleAlert = buckleAlert;
    }

    public long getModifedEpochMs() {
        return modifedEpochMs;
    }

    public void setModifedEpochMs(long modifedEpochMs) {
        this.modifedEpochMs = modifedEpochMs;
    }

    public int getSystemAlert() {
        return systemAlert;
    }

    public void setSystemAlert(int systemAlert) {
        this.systemAlert = systemAlert;
    }

    public int getUserAlert() {
        return userAlert;
    }

    public void setUserAlert(int userAlert) {
        this.userAlert = userAlert;
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public int getPersButton() {
        return persButton;
    }

    public void setPersButton(int persButton) {
        this.persButton = persButton;
    }

    public int getVibrationLevel() {
        return vibrationLevel;
    }

    public void setVibrationLevel(int vibrationLevel) {
        this.vibrationLevel = vibrationLevel;
    }

    public int getDeviceAlwaysOn() {
        return deviceAlwaysOn;
    }

    public void setDeviceAlwaysOn(int deviceAlwaysOn) {
        this.deviceAlwaysOn = deviceAlwaysOn;
    }

    public int getLedIntensity() {
        return ledIntensity;
    }

    public void setLedIntensity(int ledIntensity) {
        this.ledIntensity = ledIntensity;
    }

    public int getUnBuckleAlert() {
        return unBuckleAlert;
    }

    public void setUnBuckleAlert(int unBuckleAlert) {
        this.unBuckleAlert = unBuckleAlert;
    }


    public String getDevSSID() {
        return devSSID;
    }

    public void setDevSSID(String devSSID) {
        this.devSSID = devSSID;
    }

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

    public long getCreatedEpochMs() {
        return createdEpochMs;
    }

    public void setCreatedEpochMs(long createdEpochMs) {
        this.createdEpochMs = createdEpochMs;
    }

    public int getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(int assignStatus) {
        this.assignStatus = assignStatus;
    }

    public String getDevModal() {
        return devModal;
    }

    public void setDevModal(String devModal) {
        this.devModal = devModal;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getDevPasswd() {
        return devPasswd;
    }

    public void setDevPasswd(String devPasswd) {
        this.devPasswd = devPasswd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDevSize() {
        return devSize;
    }

    public void setDevSize(String devSize) {
        this.devSize = devSize;
    }

    public String getDevMAC() {
        return devMAC;
    }

    public void setDevMAC(String devMAC) {
        this.devMAC = devMAC;
    }

    public int getWiFiConfiguredStatus() {
        return wiFiConfiguredStatus;
    }

    public void setWiFiConfiguredStatus(int wiFiConfiguredStatus) {
        this.wiFiConfiguredStatus = wiFiConfiguredStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
