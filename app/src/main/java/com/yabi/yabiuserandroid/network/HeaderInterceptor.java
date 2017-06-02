package com.yabi.yabiuserandroid.network;

import android.content.Context;

import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rohitsingh on 06/10/16.
 */

public class HeaderInterceptor implements Interceptor {

    public static final String HEADER_USER_TOKEN = "user_token";
    public static final String HEADER_PHONE_NUMBER = "phone_number";
    public static final String HEADER_CONTENT_TYPE = "content-type";
    public static final String CONTENT_APPLICATION_JSON = "application/json";
    public Context context;

    public HeaderInterceptor(Context context)
    {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain)
            throws IOException {
        Request request = chain.request().newBuilder()
                .header(HEADER_USER_TOKEN, new SharedPrefUtils(context).getUserToken()==null?"":new SharedPrefUtils(context).getUserToken())
                .header(HEADER_PHONE_NUMBER, new SharedPrefUtils(context).getUserPhone()==null?"":new SharedPrefUtils(context).getUserPhone())
                .header(HEADER_CONTENT_TYPE,CONTENT_APPLICATION_JSON)
                .build();

        return chain.proceed(request);
    }

}
