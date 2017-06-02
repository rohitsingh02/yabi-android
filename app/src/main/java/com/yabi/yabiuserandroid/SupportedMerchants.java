package com.yabi.yabiuserandroid;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yabi.yabiuserandroid.models.data.WidgetSupportedMerchant;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitsingh on 23/12/16.
 */

public class SupportedMerchants {

    private List<WidgetSupportedMerchant> merchants = new ArrayList<>();
    private Context context;


//    private void addmerchants()
//    {
//        merchants = new ArrayList<>();
//
//        merchants.add(new Merchant("freecharge","com.freecharge.android"));
//        merchants.add(new Merchant("Uber","com.ubercab"));
//        merchants.add(new Merchant("Ola Cabs","com.olacabs.customer"));
//        merchants.add(new Merchant("Amazon","in.amazon.mShop.android.shopping"));
//    }



    public WidgetSupportedMerchant isMerchant(String activityName,Context context)
    {
        Log.d("qwerty","supported list");
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(context);
        String supportedMerchants =  sharedPrefUtils.readString("SUPPORTEDMERCHANT","");
        merchants = new Gson().fromJson(supportedMerchants,new TypeToken<List<WidgetSupportedMerchant>>() {}.getType());

        if(merchants != null){
            for(WidgetSupportedMerchant merchant: merchants){

                if(activityName.contains(merchant.getAndroid_app()))
                    return merchant;

            }
        }
        return null;
    }


//    public class Merchant{
//        private String merchantName;
//        private String merchantPackage;
//
//        public Merchant(String merchantName, String merchantPackage)
//        {
//            this.merchantName = merchantName;
//            this.merchantPackage = merchantPackage;
//        }
//        public String getMerchantName() {
//            return merchantName;
//        }
//
//        public void setMerchantName(String merchantName) {
//            this.merchantName = merchantName;
//        }
//
//        public String getMerchantPackage() {
//            return merchantPackage;
//        }
//
//        public void setMerchantPackage(String merchantPackage) {
//            this.merchantPackage = merchantPackage;
//        }
//    }

}
