package com.yabi.yabiuserandroid.ui.uiutils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.models.data.MerchantDetail;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.utils.BranchDeepLinkHelper;
import com.yabi.yabiuserandroid.utils.Utils;

/**
 * Created by rohitsingh on 23/10/16.
 */

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {


    public Activity activity;
    public Button shareBtn, okBtn,saveBtn;
    TextView merchNameTv,couponTv,conditionTv;
    Offers offer;
    MerchantDetail merchantDetail;
    int location;
    String merchName;

    public CustomDialog(Activity activity, MerchantDetail merchantDetail,int location, Offers offers) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.merchantDetail = merchantDetail;
        this.location = location;
        this.offer = offers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.offer_doalog_content);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        okBtn = (Button) findViewById(R.id.okBtn);
        merchNameTv = (TextView) findViewById(R.id.merchNameTv);
        couponTv = (TextView) findViewById(R.id.couponTv);
        conditionTv = (TextView) findViewById(R.id.conditionTv);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        merchNameTv.setText(merchantDetail.getName());
        couponTv.setText(offer.getCode());
        conditionTv.setText(offer.getDescription());
        shareBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareBtn:
                BranchDeepLinkHelper.getInstance().createOfferLinkToShare(activity,merchantDetail,location,offer);
                break;
            case R.id.saveBtn:
                Utils.toggleFavourite(getContext(),offer);
                Toast.makeText(getContext(),"Deal Saved Successfully!!",Toast.LENGTH_LONG).show();
                dismiss();
                break;
            case R.id.okBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}
