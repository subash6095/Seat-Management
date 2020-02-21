package com.nokia.test.seatmanagement.util;

public enum ERROR_CODE{
    EC_001("EC_001","USER NOT FOUND"),
    EC_002("EC_002","COMPANY NOT FOUND"),
    EC_003("EC_003","FLOOR NOT FOUND"),
    EC_004("EC_004","SEAT NOT FOUND"),
    EC_005("EC_005","REQUEST NOT FOUND"),
    EC_006("EC_006","REQUESTED SEAT IS UNAVAILABLE");

    private String errorMessage;
    private String errorCode;

    ERROR_CODE(String errorCode, String errorMessage) {
        this.errorCode    = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
