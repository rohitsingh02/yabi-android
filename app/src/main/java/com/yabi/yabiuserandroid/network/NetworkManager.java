package com.yabi.yabiuserandroid.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
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
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yogeshmadaan on 23/11/16.
 */
public class NetworkManager {
    private Context context;
    private static NetworkManager _instance = null;
    private NetworkService networkService = null;
    SharedPrefUtils  sharedPrefUtils;
    private NetworkManager(Context context)
    {
        this.context = context;
        this.networkService = new NetworkService(context);
    }

    public static NetworkManager getInstance(Context context)
    {
        if(_instance == null)
            _instance = new NetworkManager(context);
        return _instance;
    }

    public static NetworkManager setNetworkManagerToNull(){
        _instance = null;
        return _instance;
    }

    public void sendOtp(OtpRequest otpRequest)
    {
        networkService.sendOtp(otpRequest).enqueue(otpResponseCallback);
    }

    private Callback<OtpResponse> otpResponseCallback = new Callback<OtpResponse>() {
        @Override
        public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post(response.body());
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.SEND_OTP_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<OtpResponse> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.SEND_OTP_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);
        }
    };

    public void authenticate(AuthenticateOtpRequest authenticateOtpRequest)
    {
        networkService.authenticate(authenticateOtpRequest).enqueue(authenticateOtpResponseCallback);
    }
    private Callback<AuthenticateOtpResponse> authenticateOtpResponseCallback = new Callback<AuthenticateOtpResponse>() {
        @Override
        public void onResponse(Call<AuthenticateOtpResponse> call, Response<AuthenticateOtpResponse> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post(response.body());
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.OTP_VALIDATION_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<AuthenticateOtpResponse> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.OTP_VALIDATION_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);
        }
    };

    public void getProfile()
    {
        networkService.getProfile().enqueue(profileCallback);
    }
    private Callback<Profile> profileCallback = new Callback<Profile>() {
        @Override
        public void onResponse(Call<Profile> call, Response<Profile> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post(response.body());
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.GET_PROFILE_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<Profile> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.GET_PROFILE_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);
        }
    };



    public void getSupportedAppsList()
    {
        networkService.getSupportedAppsListing().enqueue(supportedListsCallback);
    }
    private Callback<List<WidgetSupportedMerchant>> supportedListsCallback = new Callback<List<WidgetSupportedMerchant>>() {
        @Override
        public void onResponse(Call<List<WidgetSupportedMerchant>> call, Response<List<WidgetSupportedMerchant>> response) {
            if(response.isSuccessful()) {
               // EventBus.getDefault().post(response.body());
                sharedPrefUtils = new SharedPrefUtils(context);
                sharedPrefUtils.writeString("SUPPORTEDMERCHANT",new Gson().toJson(response.body()));
                Log.d("qwerty",new Gson().toJson(response.body()));
            }
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.GET_SUPPORTED_APPS_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<List<WidgetSupportedMerchant>> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.GET_SUPPORTED_APPS_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);
        }
    };




    public void updateProfile(ProfileRequest profileRequest)
    {
        Log.d("LOGD",new Gson().toJson(profileRequest));
        networkService.setProfile(profileRequest).enqueue(profileUpdateResponseCallback);
    }


    private Callback<ProfileUpdateResponse> profileUpdateResponseCallback = new Callback<ProfileUpdateResponse>() {
        @Override
        public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post(response.body());
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.UPDATE_PROFILE_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.UPDATE_PROFILE_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);
        }
    };

    public void getMerchants()
    {
        networkService.getMerchants().enqueue(merchantListCallback);
    }

    private Callback<JsonObject> merchantListCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if(response.isSuccessful()){
                EventBus.getDefault().post((response.body()));
            }
            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.FETCH_MERCHANT_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.FETCH_MERCHANT_ERROR);
                errorTypes.setThrowable(t);
                EventBus.getDefault().post(errorTypes);

        }
    };

    public void getMerchantDetails(String url)
    {
        networkService.getMerchantDetails(url).enqueue(merchantDetailCallback);
    }

    private Callback<JsonObject> merchantDetailCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post((response.body()));

            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.FETCH_MERCHANT_DETAIL_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.FETCH_MERCHANT_DETAIL_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);

        }
    };

    public void postFCMToken(FcmTokenModel fcmTokenModel)
    {
        networkService.postFCMToken(fcmTokenModel).enqueue(fcmPostTokenCallback);
    }

    private Callback<ProfileUpdateResponse> fcmPostTokenCallback = new Callback<ProfileUpdateResponse>() {
        @Override
        public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
            if(response.isSuccessful())
                EventBus.getDefault().post((response.body()));

            else {
                ErrorTypes errorTypes = new ErrorTypes();
                errorTypes.setType(ErrorTypes.GET_FCM_TOKEN_POST_ERROR);
                EventBus.getDefault().post(errorTypes);
            }
        }

        @Override
        public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
            ErrorTypes errorTypes = new ErrorTypes();
            errorTypes.setType(ErrorTypes.GET_FCM_TOKEN_POST_ERROR);
            errorTypes.setThrowable(t);
            EventBus.getDefault().post(errorTypes);

        }
    };
}
