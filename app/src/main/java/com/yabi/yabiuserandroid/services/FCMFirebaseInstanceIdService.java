package com.yabi.yabiuserandroid.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

/**
 * Created by rohitsingh on 13/10/16.
 */

public class FCMFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "FCMFirebaseInstanceId";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        sendRegistrationToServer(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current
        if(token != null){
            new SharedPrefUtils(getApplicationContext()).setUserFcmToken(token);
            Log.d(TAG, "Refreshed token: " + new SharedPrefUtils(getApplicationContext()).getUserFcmToken());
        }
    }
}
