package com.yabi.yabiuserandroid.app;

import android.app.Application;

import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.branch.referral.Branch;
import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.Fabric;


/**
 * Created by rohit_singh on 23/01/16.
 */
public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "OKXXZYqfbzu3oUGae2Ny53wfo";
    private static final String TWITTER_SECRET = "4UGS4QhC2iPisyDAaGoCUCYUjErtvFlNI34JYWhhH7olxhOitv";

    @Override
    public void onCreate() {
        super.onCreate();

        Branch.getAutoInstance(this);
        if(!    BuildConfig.DEBUG){
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new TwitterCore(authConfig), new Digits());
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);




    }
}
