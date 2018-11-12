package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class AllUserItemListEntityRes implements Serializable {

    private long createdEpochMs = 0;
    private String countryCode = "";
    private String firstName = "";
    private String lastName = "";
    private String communityId = "";
    private String address = "";
    private String createdBy = "";
    private String primNumber = "";
    private int assignStatus = 0;
    private String userId = "0";
    private String primEmail = "0";
    private String zipCode = "0";
    private String secNumber = "0";
    private String title = "0";

    public long getCreatedEpochMs() {
        return createdEpochMs;
    }

    public void setCreatedEpochMs(long createdEpochMs) {
        this.createdEpochMs = createdEpochMs;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPrimNumber() {
        return primNumber;
    }

    public void setPrimNumber(String primNumber) {
        this.primNumber = primNumber;
    }

    public int getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(int assignStatus) {
        this.assignStatus = assignStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrimEmail() {
        return primEmail;
    }

    public void setPrimEmail(String primEmail) {
        this.primEmail = primEmail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSecNumber() {
        return secNumber;
    }

    public void setSecNumber(String secNumber) {
        this.secNumber = secNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
