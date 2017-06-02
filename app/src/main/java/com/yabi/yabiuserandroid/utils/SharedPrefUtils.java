package com.yabi.yabiuserandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yabi.yabiuserandroid.R;

/**
 * Created by yogeshmadaan on 23/01/16.
 */
public class SharedPrefUtils {
    private static final int MODE = Context.MODE_PRIVATE;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SharedPrefUtils(Context context) {
        sharedPreferences = getPreferences(context);
        editor = getEditor(context);
    }

    public SharedPreferences getPreferences(Context context) {
        String appName = context.getResources().getString(R.string.app_name);
        return context.getSharedPreferences(appName, MODE);
    }

    public void clearSharedPreferenceFile(Context context) {
        getEditor(context).clear().apply();
    }

    public SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", null);
    }

    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("userEmail", null);
    }

    public void setUserEmail(String userEmail) {
        editor.putString("userEmail", userEmail);
        editor.commit();
    }

    public String getUserPhone() {
        return sharedPreferences.getString("userPhone", null);
    }

    public void setUserPhone(String userPhone) {
        editor.putString("userPhone", userPhone);
        editor.commit();
    }

    public String getUserToken() {
        return sharedPreferences.getString("userToken", null);
    }

    public void setUserToken(String user_token) {
        editor.putString("userToken", user_token);
        editor.commit();
    }

    public int getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setUserId(int userId) {
        editor.putInt("userId", userId);
        MyLogger.e("user id saved", "" + userId);
        editor.commit();
    }

    public String getUserProfilePic() {
        return sharedPreferences.getString("userProfilePic", null);
    }

    public void setUserProfilePic(String userProfilePic) {
        editor.putString("userProfilePic", userProfilePic);
        editor.commit();
    }

    public String getUserFcmToken() {
        return sharedPreferences.getString("fcmtoken", null);
    }

    public void setUserFcmToken(String userId) {
        editor.putString("fcmtoken", userId);
        editor.commit();
    }

    public Boolean getIsProfileUpdated() {
        return sharedPreferences.getBoolean("PROFILE", false);
    }

    public void setIsProfileUpdated(Boolean profile) {
        editor.putBoolean("PROFILE", profile);
        editor.commit();
    }

    public int getMerchantId() {
        return sharedPreferences.getInt("fcmtoken", -1);
    }

    public void setMerchantId(int userId) {
        editor.putInt("merchantId", userId);
        editor.commit();
    }

    public Boolean getIsFcmTokenposted() {
        return sharedPreferences.getBoolean("FCM", false);
    }

    public void setIsFcmTokenposted(Boolean smsPermissionFlag) {
        editor.putBoolean("FCM", smsPermissionFlag);
        editor.commit();
    }


    public void writeBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public  boolean readBoolean(String key,
                                      boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    public  void writeInteger( String key, int value) {
        editor.putInt(key, value).commit();

    }

    public int readInteger( String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void writeString( String key, String value) {
        editor.putString(key, value).commit();

    }

    public String readString( String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void writeFloat( String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public float readFloat( String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public void writeLong( String key, long value) {
        editor.putLong(key, value).commit();
    }

    public long readLong( String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public void removeParticularPreference(String key){
        editor.remove(key);
        editor.commit();
    }

}
