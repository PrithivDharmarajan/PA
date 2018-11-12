package com.e2infosystems.activeprotective.output.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
    private int status = 0;
    private String errorCode = "";
    private String errorMessage = "";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
