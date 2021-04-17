package com.example.trackerapp.result;

public enum ResultCode {
    Unknown(0),
    NetworkError(-1);

    int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ResultCode get(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (code == resultCode.code)
                return resultCode;
        }
        return Unknown;
    }
}
