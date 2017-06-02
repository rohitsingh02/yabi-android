package com.yabi.yabiuserandroid.models.data;

/**
 * Created by rohitsingh on 08/02/17.
 */

public class WidgetSupportedMerchant {

    private int id;
    private String android_app;


    public WidgetSupportedMerchant(int id, String android_app) {
        this.id = id;
        this.android_app = android_app;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAndroid_app() {
        return android_app;
    }

    public void setAndroid_app(String android_app) {
        this.android_app = android_app;
    }
}
