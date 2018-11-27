package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class UserLoginDetailsEntityRes implements Serializable {


  private  String zipCode="";

    private  String address1="";

    private  long createdEpochMs=0;

    private   long modifiedEpochMs=0;

    private  String deviceId="";

    private  String userId="";

    private  String accountId="";

    private  String firstName="";

    private  String lastName="";

    private  String primEmail="";

    private  String primNumber="";

    private  String assignStatus="";

    private  String createdBy="";

    private  String countryCode="";

    private  String communityName="";

    private  String modifiedBy="";
    
    private  String communityId="";

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public long getCreatedEpochMs() {
        return createdEpochMs;
    }

    public void setCreatedEpochMs(long createdEpochMs) {
        this.createdEpochMs = createdEpochMs;
    }

    public long getModifiedEpochMs() {
        return modifiedEpochMs;
    }

    public void setModifiedEpochMs(long modifiedEpochMs) {
        this.modifiedEpochMs = modifiedEpochMs;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimEmail() {
        return primEmail;
    }

    public void setPrimEmail(String primEmail) {
        this.primEmail = primEmail;
    }

    public String getPrimNumber() {
        return primNumber;
    }

    public void setPrimNumber(String primNumber) {
        this.primNumber = primNumber;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }


}
