package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class AllUserListResponse implements Serializable {

    private int status = 0;
    private String message = "";
    private AllUserListEntityRes data=new AllUserListEntityRes();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AllUserListEntityRes getData() {
        return data;
    }

    public void setData(AllUserListEntityRes data) {
        this.data = data;
    }



}
