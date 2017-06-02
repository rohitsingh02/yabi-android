package com.yabi.yabiuserandroid.models.data;

import java.io.Serializable;

/**
 * Created by rohitsingh on 28/09/16.
 */
public class OtpResponse implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
