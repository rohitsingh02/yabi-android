package com.yabi.yabiuserandroid.network;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by rohitsingh on 28/09/16.
 */
public interface NetworkApi {

    @POST("sendOTP")
    Call<OtpResponse> sendOTP(@Body OtpRequest otpRequest);

    @POST("authenticate")
    Call<AuthenticateOtpResponse> authenticate(@Body AuthenticateOtpRequest authenticateOtpRequest);

    @GET("profile")
    Call<Profile> getProfileData();

    @POST("profile")
    Call<ProfileUpdateResponse> postProfileData(@Body ProfileRequest profileRequest);

    @GET("merchants")
    Call<JsonObject> getMerchantsList();

    @GET
    Call<JsonObject> getMerchantDetailAndOfferList(@Url String url);

    @GET("nearby")
    Call<String> getNearbyMerchants();

    @GET("widget")
    Call<List<WidgetSupportedMerchant>> getSupportedAppsListing();

    @POST("profile/registerFCM")
    Call<ProfileUpdateResponse> postFCMToken(@Body FcmTokenModel fcmTokenModel);



}
