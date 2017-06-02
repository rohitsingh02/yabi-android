package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 06/10/16.
 */

public class RequestHeader {

    String user_token;
    String phone_number;


    public RequestHeader(String user_token, String phone_number) {
        this.user_token = user_token;
        this.phone_number = phone_number;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
