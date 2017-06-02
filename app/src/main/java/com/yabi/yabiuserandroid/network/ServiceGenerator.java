package com.yabi.yabiuserandroid.network;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.yabi.yabiuserandroid.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yogeshmadaan on 23/11/16.
 */
public class ServiceGenerator {
    public final static String BASE_URL = "http://api.yabi.in/api/v1/user/";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static <S> S createService(Class<S> serviceClass, final Context context)
    {
        Retrofit builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat(DATE_TIME_FORMAT).create()))
                    .client(createOkHttpClient(context))
                    .build();

            return builder.create(serviceClass);
    }
    private static OkHttpClient createOkHttpClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        int connTimeout = context.getResources().getInteger(R.integer.http_client_connection_timeout);
        int readTimeout = context.getResources().getInteger(R.integer.http_client_read_timeout);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(connTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS).addInterceptor(new HeaderInterceptor(context))
                .addInterceptor(logging).build();
        return client;
    }
}
