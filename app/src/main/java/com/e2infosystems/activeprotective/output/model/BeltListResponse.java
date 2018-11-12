package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class BeltListResponse implements Serializable {

    private int status = 0;
    private String message = "";
    private BeltListEntityRes data=new BeltListEntityRes();

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

    public BeltListEntityRes getData() {
        return data;
    }

    public void setData(BeltListEntityRes data) {
        this.data = data;
    }



}
