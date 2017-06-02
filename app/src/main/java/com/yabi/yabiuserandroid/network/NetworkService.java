package com.yabi.yabiuserandroid.network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yabi.yabiuserandroid.models.data.AuthenticateOtpRequest;
import com.yabi.yabiuserandroid.models.data.AuthenticateOtpResponse;
import com.yabi.yabiuserandroid.models.data.FcmTokenModel;
import com.yabi.yabiuserandroid.models.data.OtpRequest;
import com.yabi.yabiuserandroid.models.data.OtpResponse;
import com.yabi.yabiuserandroid.models.data.Profile;
import com.yabi.yabiuserandroid.models.data.ProfileRequest;
import com.yabi.yabiuserandroid.models.data.ProfileUpdateResponse;
import com.yabi.yabiuserandroid.models.data.WidgetSupportedMerchant;

import java.util.List;

import retrofit2.Call;

/**
 * Created by yogeshmadaan on 23/11/16.
 */
public class NetworkService {

    private Context context = null;
    private NetworkApi networkApi = null;

    public NetworkService(Context context)
    {
        this.context = context;
        this.networkApi = ServiceGenerator.createService(NetworkApi.class, context);
    }

    public Call<OtpResponse> sendOtp(OtpRequest otpRequest)
    {
        return networkApi.sendOTP(otpRequest);
    }

    public Call<AuthenticateOtpResponse> authenticate(AuthenticateOtpRequest authenticateOtpRequest)
    {
        return networkApi.authenticate(authenticateOtpRequest);
    }

    public Call<Profile> getProfile()
    {
        return networkApi.getProfileData();
    }

    public Call<List<WidgetSupportedMerchant>> getSupportedAppsListing()
    {
        return networkApi.getSupportedAppsListing();
    }

    public Call<ProfileUpdateResponse> setProfile(ProfileRequest profileRequest)
    {
        return networkApi.postProfileData(profileRequest);
    }

    public Call<JsonObject> getMerchants()
    {
        return networkApi.getMerchantsList();
    }

    public Call<JsonObject> getMerchantDetails(String url)
    {
        return networkApi.getMerchantDetailAndOfferList(url);
    }

    public Call<ProfileUpdateResponse> postFCMToken(FcmTokenModel fcmTokenModel){
        return networkApi.postFCMToken(fcmTokenModel);
    }

}
