package com.e2infosystems.activeprotective.input.model;

import java.io.Serializable;

public class AddUserEntity {


    private String communityId = "";
    private String accountId = "";
    private String communityName = "";
    private String title = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String address1 = "";
    private String address2 = "";
    private String zipCode = "";
    private String countryCode = "";
    private String primNumber = "";
    private String secNumber = "";
    private String primEmail = "";

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPrimNumber() {
        return primNumber;
    }

    public void setPrimNumber(String primNumber) {
        this.primNumber = primNumber;
    }

    public String getSecNumber() {
        return secNumber;
    }

    public void setSecNumber(String secNumber) {
        this.secNumber = secNumber;
    }

    public String getPrimEmail() {
        return primEmail;
    }

    public void setPrimEmail(String primEmail) {
        this.primEmail = primEmail;
    }

}
