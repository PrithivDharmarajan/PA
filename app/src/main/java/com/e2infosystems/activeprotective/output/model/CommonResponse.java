package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class CommonResponse implements Serializable {

    private int status = 0;
    private String message = "";

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




}
