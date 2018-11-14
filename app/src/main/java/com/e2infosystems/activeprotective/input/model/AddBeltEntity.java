package com.e2infosystems.activeprotective.input.model;

import java.io.Serializable;

public class AddBeltEntity implements Serializable {



    private String deviceId = "";
    private String communityId = "";
    private String accountId = "";
    private String communityName = "";
    private String devSSID = "";
    private String devPasswd = "";
    private String devMAC = "";
    private String devModal = "";
    private String devSize = "";


    private int ledIntensity = 0;
    private int systemAlert = 0;
    private int unBuckleAlert = 0;
    private int buckleAlert = 0;
    private int deviceAlwaysOn = 0;
    private int userAlert = 0;
    private int volumeLevel = 0;
    private int vibrationLevel = 0;
    private int wiFiConfiguredStatus = 0;
    private int persButton = 0;



    public int getPersButton() {
        return persButton;
    }

    public void setPersButton(int persButton) {
        this.persButton = persButton;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getSystemAlert() {
        return systemAlert;
    }

    public void setSystemAlert(int systemAlert) {
        this.systemAlert = systemAlert;
    }

    public int getUnBuckleAlert() {
        return unBuckleAlert;
    }

    public void setUnBuckleAlert(int unBuckleAlert) {
        this.unBuckleAlert = unBuckleAlert;
    }

    public int getBuckleAlert() {
        return buckleAlert;
    }

    public void setBuckleAlert(int buckleAlert) {
        this.buckleAlert = buckleAlert;
    }

    public int getUserAlert() {
        return userAlert;
    }

    public void setUserAlert(int userAlert) {
        this.userAlert = userAlert;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDevSSID() {
        return devSSID;
    }

    public void setDevSSID(String devSSID) {
        this.devSSID = devSSID;
    }

    public String getDevPasswd() {
        return devPasswd;
    }

    public void setDevPasswd(String devPasswd) {
        this.devPasswd = devPasswd;
    }

    public String getDevMAC() {
        return devMAC;
    }

    public void setDevMAC(String devMAC) {
        this.devMAC = devMAC;
    }

    public String getDevModal() {
        return devModal;
    }

    public void setDevModal(String devModal) {
        this.devModal = devModal;
    }

    public String getDevSize() {
        return devSize;
    }

    public void setDevSize(String devSize) {
        this.devSize = devSize;
    }

    public int getWiFiConfiguredStatus() {
        return wiFiConfiguredStatus;
    }

    public void setWiFiConfiguredStatus(int wiFiConfiguredStatus) {
        this.wiFiConfiguredStatus = wiFiConfiguredStatus;
    }

    public int getLedIntensity() {
        return ledIntensity;
    }

    public void setLedIntensity(int ledIntensity) {
        this.ledIntensity = ledIntensity;
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        this.volumeLevel = volumeLevel;
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

}
