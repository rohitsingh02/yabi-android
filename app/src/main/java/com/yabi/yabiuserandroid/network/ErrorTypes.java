package com.yabi.yabiuserandroid.network;

/**
 * Created by yogeshmadaan on 23/11/16.
 */
public class ErrorTypes {

    public static final int INTERNET_ERROR = 1;
    public static final int OTP_VALIDATION_ERROR = 2;
    public static final int SEND_OTP_ERROR = 3;
    public static final int FETCH_MERCHANT_ERROR = 4;
    public static final int FETCH_MERCHANT_DETAIL_ERROR = 5;
    public static final int UPDATE_PROFILE_ERROR = 6;
    public static final int GET_PROFILE_ERROR = 7;
    public static final int GET_SUPPORTED_APPS_ERROR = 9;
    public static final int GET_FCM_TOKEN_POST_ERROR = 8;
    private int type;
    private Throwable throwable;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
