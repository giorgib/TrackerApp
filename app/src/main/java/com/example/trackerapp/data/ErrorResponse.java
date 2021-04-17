package com.example.trackerapp.data;

import com.example.trackerapp.result.ResultCode;

public class ErrorResponse {
    private ResultCode errorCode = ResultCode.Unknown;
    private String message;

    public ErrorResponse() {

    }

    public ErrorResponse(Integer errorCode, String message) {
        this.errorCode = ResultCode.get(errorCode);
        this.message = message;
    }

    public ErrorResponse(ResultCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResultCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ResultCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
