package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 28/09/16.
 */
public class AuthenticateOtpRequest {

    private String phone_number;
    private String otp;

    public AuthenticateOtpRequest(String phone_number, String otp) {
        this.phone_number = phone_number;
        this.otp = otp;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
