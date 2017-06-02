package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 20/11/16.
 */

public class FcmTokenModel {
    private String fcm_id;
    private int os_type;


    public FcmTokenModel(String fcm_id, int os_type) {
        this.fcm_id = fcm_id;
        this.os_type = os_type;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public int getOs_type() {
        return os_type;
    }

    public void setOs_type(int os_type) {
        this.os_type = os_type;
    }
}
