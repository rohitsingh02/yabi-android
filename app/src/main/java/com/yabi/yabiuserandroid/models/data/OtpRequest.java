package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 28/09/16.
 */
public class OtpRequest {

    public OtpRequest(String phone_number) {
        this.phone_number = phone_number;
    }

    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
