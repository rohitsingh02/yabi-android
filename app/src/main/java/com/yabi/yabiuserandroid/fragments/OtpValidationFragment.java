package com.yabi.yabiuserandroid.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.DrawerActivity;
import com.yabi.yabiuserandroid.activities.LoginActivity;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.AuthenticateOtpRequest;
import com.yabi.yabiuserandroid.models.data.AuthenticateOtpResponse;
import com.yabi.yabiuserandroid.models.data.FcmTokenModel;
import com.yabi.yabiuserandroid.models.data.OtpRequest;
import com.yabi.yabiuserandroid.models.data.OtpResponse;
import com.yabi.yabiuserandroid.models.data.ProfileUpdateResponse;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.palette.CustomFontEditTextView;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import de.greenrobot.event.EventBus;


public class OtpValidationFragment extends Fragment implements View.OnClickListener, LoadingInterface {

    CustomFontEditTextView otpEt1;
    CustomFontEditTextView otpEt2;
    CustomFontEditTextView otpEt3;
    CustomFontEditTextView otpEt4;
    TextView contactNoTv;
    String phoneNumber;
    Button continueBtn;
    CustomUncancelableLoader customUncancelableLoader;
    SharedPrefUtils sharedPrefUtils;
    String otp;
    private TextView connectionTv;
    private BroadcastReceiver mIntentBroadcastReceiver;
    private Boolean isSmsPermissionGiven;
    private TextView resendOtpTv;
    private EventBus eventBus;

    public OtpValidationFragment() {
        // Required empty public constructor
    }

    public static OtpValidationFragment newInstance(String phoneNumber, Boolean isSmsPermissionGiven) {
        OtpValidationFragment fragment = new OtpValidationFragment();
        Bundle args = new Bundle();
        args.putSerializable("PHONE", phoneNumber);
        args.putSerializable("SMS", isSmsPermissionGiven);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            phoneNumber = (String) getArguments().getSerializable("PHONE");
            isSmsPermissionGiven = (Boolean) getArguments().getSerializable("SMS");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_otp_validation, container, false);

        contactNoTv = (TextView) rootView.findViewById(R.id.contact_number);
        otpEt1 = (CustomFontEditTextView) rootView.findViewById(R.id.otp1);
        otpEt2 = (CustomFontEditTextView) rootView.findViewById(R.id.otp2);
        otpEt3 = (CustomFontEditTextView) rootView.findViewById(R.id.otp3);
        otpEt4 = (CustomFontEditTextView) rootView.findViewById(R.id.otp4);

        ((LoginActivity) getActivity()).updateStatusBarColor("#BFBFBF");
        contactNoTv.setText("+91- " + new SharedPrefUtils(getContext()).getUserPhone());
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(), "Please wait ....");
        customUncancelableLoader.setCancelable(true);
        if (isSmsPermissionGiven) {
            showLoading(customUncancelableLoader);
        }
        continueBtn = (Button) rootView.findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(this);
        resendOtpTv = (TextView) rootView.findViewById(R.id.resendOtp);
        sharedPrefUtils = new SharedPrefUtils(getContext());
        connectionTv = (TextView) rootView.findViewById(R.id.noInternet);

        resendOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(customUncancelableLoader);
                resendOTP();
            }
        });
        otpEt1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpEt1.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpEt2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        otpEt2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpEt2.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpEt3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        otpEt3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpEt3.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpEt4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_continue:
                String otp = otpEt1.getText().toString() + otpEt2.getText().toString() + otpEt3.getText().toString() + otpEt4.getText().toString();
                verifyOTP(otp);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("send");
        mIntentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                otp = intent.getStringExtra("OTP");
                otp = otp.replaceAll("\n", "");
                verifyOTP(otp);

            }
        };
        getActivity().registerReceiver(mIntentBroadcastReceiver, intentFilter);
    }

    private void verifyOTP(String otp) {

        if (new Utils().isConnectedToInternet(getActivity())) {
            if (connectionTv.getVisibility() == View.VISIBLE) {
                connectionTv.setVisibility(View.GONE);
            }
            NetworkManager.getInstance(getActivity()).authenticate(new AuthenticateOtpRequest(phoneNumber, otp));

        } else {
            hideLoading(customUncancelableLoader);
            connectionTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mIntentBroadcastReceiver);

    }

    private void resendOTP() {

        String userPhoneNumber = sharedPrefUtils.getUserPhone();
        if (new Utils().isConnectedToInternet(getActivity())) {
            showLoading(customUncancelableLoader);
            if (connectionTv.getVisibility() == View.VISIBLE) {
                connectionTv.setVisibility(View.GONE);
            }
            NetworkManager.getInstance(getActivity()).sendOtp(new OtpRequest(userPhoneNumber));
        } else {
            hideLoading(customUncancelableLoader);
            connectionTv.setVisibility(View.VISIBLE);
        }
    }


    private void postFcmToken() {
        FcmTokenModel fcmTokenModel = new FcmTokenModel(sharedPrefUtils.getUserFcmToken(), 1);
        NetworkManager.getInstance(getActivity()).postFCMToken(fcmTokenModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading(customUncancelableLoader);
        eventBus.unregister(this);
    }


    public void onEvent(OtpResponse otpResponse) {
        if (otpResponse == null) {
            Toast.makeText(getActivity(), "No OTP SEND", Toast.LENGTH_LONG).show();
            //show proper handle here with retry button
            showErrorDialog();

            return;
        } else {
            hideLoading(customUncancelableLoader);
            Toast.makeText(getActivity(), "OTP Sent successfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void onEvent(ProfileUpdateResponse fcmTokenPostResponse) {
        sharedPrefUtils.setIsFcmTokenposted(true);
        Toast.makeText(getContext(), fcmTokenPostResponse.getMessage(), Toast.LENGTH_LONG).show();
        return;
    }

    public void onEvent(AuthenticateOtpResponse authenticateOtpResponse) {
        if (authenticateOtpResponse.getUser_token() != null) {
            sharedPrefUtils.setUserToken(authenticateOtpResponse.getUser_token());
            sharedPrefUtils.setUserPhone(phoneNumber);
        }
        if (authenticateOtpResponse.getIs_valid() == true) {
            hideLoading(customUncancelableLoader);
            postFcmToken();

            if (authenticateOtpResponse.getIs_new_user() == true)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, UpdateProfileActivityFragment.newInstance()).commit();
             else {
                sharedPrefUtils.setIsProfileUpdated(true);
                Intent myIntent = new Intent(getActivity(), DrawerActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        } else {
            Toast.makeText(getContext(), "Entered OTP is not correct", Toast.LENGTH_SHORT).show();
            //show proper handle here with retry button
        }
    }

    public void onEvent(ErrorTypes errorTypes) {
        hideLoading(customUncancelableLoader);
        if (errorTypes.getType() == ErrorTypes.OTP_VALIDATION_ERROR)
            Toast.makeText(getActivity(), "Sorry! Unable to verify your phone number", Toast.LENGTH_SHORT).show();
        else if (errorTypes.getType() == ErrorTypes.SEND_OTP_ERROR)
            showErrorDialog();
    }

    private void showErrorDialog() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Unable to send otp !!!");
        alertDialog2.setMessage("Please click on retry button to try again");
        alertDialog2.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resendOTP();
                    }
                });
        alertDialog2.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }


    @Override
    public void showLoading(CustomUncancelableLoader customUncancelableLoader) {
        if (customUncancelableLoader != null && !customUncancelableLoader.isShowing()) {
            customUncancelableLoader.show();
        }
    }

    @Override
    public void hideLoading(CustomUncancelableLoader customUncancelableLoader) {
        if (customUncancelableLoader != null && customUncancelableLoader.isShowing()) {
            customUncancelableLoader.hide();
        }
    }
}
