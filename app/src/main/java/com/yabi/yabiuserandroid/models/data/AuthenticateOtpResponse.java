package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 29/09/16.
 */
public class AuthenticateOtpResponse {
  private Boolean is_valid;
  private Boolean is_new_user;
  private String user_token;

    public Boolean getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Boolean is_valid) {
        this.is_valid = is_valid;
    }

    public Boolean getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(Boolean is_new_user) {
        this.is_new_user = is_new_user;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
