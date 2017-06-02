package com.yabi.yabiuserandroid.fragments;


import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.yabi.yabiuserandroid.activities.LoginActivity;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.OtpRequest;
import com.yabi.yabiuserandroid.models.data.OtpResponse;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.palette.PrefixEditText;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;
import com.yabi.yabiuserandroid.utils.ValidatorUtils;

import de.greenrobot.event.EventBus;


public class SigninProfileFragment extends Fragment implements LoadingInterface {

    private PrefixEditText txtProfilePhone;
    Button nextButton;
    String userPhoneNumber;
    CustomUncancelableLoader customUncancelableLoader;
    private SharedPrefUtils sharedPrefUtils;
    private Boolean isSmsPermissionGiven = false;
    private EventBus eventBus;
    private TextView connectionTv;

    public SigninProfileFragment() {
        // Required empty public constructor
    }


    public static SigninProfileFragment newInstance() {
        SigninProfileFragment fragment = new SigninProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signin_profile, container, false);
        txtProfilePhone = (PrefixEditText) rootView.findViewById(R.id.txt_profile_phone);
        nextButton = (Button) rootView.findViewById(R.id.signup_btn);
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(), "Please wait ....");
        customUncancelableLoader.setCancelable(false);
        sharedPrefUtils = new SharedPrefUtils(getContext());
        connectionTv = (TextView) rootView.findViewById(R.id.noInternet);
        ((LoginActivity) getActivity()).updateStatusBarColor("#313747");
        changeBtnColorWhenEtEdited();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidatorUtils.validatePhone(txtProfilePhone)) {
                    if (Build.VERSION.SDK_INT < 23) {
                        isSmsPermissionGiven = true;
                        getOTPFromPhoneNumber();
                    } else {
                        requestSMSPermission();
                    }
                } else {
                    Toast.makeText(getActivity(), "Phone number not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


    private void requestSMSPermission() {

        int hasSmsReadPermission = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECEIVE_SMS);
        if (hasSmsReadPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECEIVE_SMS}, Utils.REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            isSmsPermissionGiven = true;
            getOTPFromPhoneNumber();
        }
    }


    private void getOTPFromPhoneNumber() {

        showLoading(customUncancelableLoader);
        if (new Utils().isConnectedToInternet(getActivity())) {

            if (connectionTv.getVisibility() == View.VISIBLE) {
                connectionTv.setVisibility(View.GONE);
            }
            userPhoneNumber = txtProfilePhone.getText().toString();


            NetworkManager.getInstance(getActivity()).sendOtp(new OtpRequest(userPhoneNumber));

        } else {
            connectionTv.setVisibility(View.VISIBLE);
            hideLoading(customUncancelableLoader);
        }
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
        eventBus.unregister(this);
    }

    public void changeBtnColorWhenEtEdited() {
        txtProfilePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (str.toString().trim().length() > 0) {
                    nextButton.setBackground(getResources().getDrawable(R.drawable.bg_next_edited_drawable));
                    nextButton.setText("Next");
                    nextButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    nextButton.setBackground(getResources().getDrawable(R.drawable.bg_btn_next));
                    nextButton.setText("Next");
                    nextButton.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utils.REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isSmsPermissionGiven = true;
                    getOTPFromPhoneNumber();

                } else {
                    isSmsPermissionGiven = false;
                    getOTPFromPhoneNumber();
                }
                break;

        }

    }

    public void onEvent(OtpResponse otpResponse) {
        hideLoading(customUncancelableLoader);
        if (otpResponse == null) {
            //show proper handle here with retry button
            showErrorDialog();
            return;
        } else {
            sharedPrefUtils.setUserPhone(userPhoneNumber);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, OtpValidationFragment.newInstance(userPhoneNumber, isSmsPermissionGiven)).commit();
        }

    }

    public void onEvent(ErrorTypes errorTypes) {
        if (errorTypes.getType() == ErrorTypes.SEND_OTP_ERROR) {
            hideLoading(customUncancelableLoader);
            //show proper handle here with retry button
            showErrorDialog();
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Unable to send otp !!!");
        alertDialog2.setMessage("Please click on retry button to try again");
//        alertDialog2.setIcon(R.drawable.yabi_splash);
        alertDialog2.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getOTPFromPhoneNumber();
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

