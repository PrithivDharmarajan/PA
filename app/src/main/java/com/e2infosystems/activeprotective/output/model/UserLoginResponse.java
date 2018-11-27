package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserLoginResponse implements Serializable {

    private int status=0;
    private  UserLoginEntityRes  data=new UserLoginEntityRes();
    private String message="";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserLoginEntityRes getData() {
        return data;
    }

    public void setData(UserLoginEntityRes data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




}
