package com.yabi.yabiuserandroid.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.DrawerActivity;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.Profile;
import com.yabi.yabiuserandroid.models.data.ProfileRequest;
import com.yabi.yabiuserandroid.models.data.ProfileUpdateResponse;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.palette.CustomFontEditTextView;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;
import com.yabi.yabiuserandroid.utils.ValidatorUtils;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements LoadingInterface{

    private CustomFontEditTextView firstNameEt;
    private CustomFontEditTextView lastNameEt;
    private CustomFontEditTextView emailIdEt;
    static CustomFontEditTextView dobEt;
    private Button submitBtn;
    private ImageButton maleIb;
    private ImageButton femaleIb;
    private ImageButton maleSelectionIb;
    private ImageButton femaleSelectionIb;
    CustomUncancelableLoader customUncancelableLoader;
    private ImageView errorImage;
    private TextView errorMsgTitleTv;
    private TextView errorMsgDescTv;
    private Button retryBtn;
    private LinearLayout errorLayout;
    private FrameLayout headerFl;
    private LinearLayout footerLl;
    int maleBtnStatus = 0;
    int femaleBtnStatus = 0;
    Profile profile;
    private EditText phone;
    static String dobValue;
    EventBus eventBus;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        firstNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.fNameEt);
        lastNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.lNameEt);
        emailIdEt = (CustomFontEditTextView) rootView.findViewById(R.id.emailEt);
        dobEt = (CustomFontEditTextView) rootView.findViewById(R.id.dobEt);
        phone = (EditText) rootView.findViewById(R.id.phone);
        submitBtn = (Button) rootView.findViewById(R.id.submitBtn);
        maleIb = (ImageButton) rootView.findViewById(R.id.male_btn);
        femaleIb = (ImageButton) rootView.findViewById(R.id.female_btn);
        headerFl = (FrameLayout) rootView.findViewById(R.id.headerFl);
        footerLl = (LinearLayout) rootView.findViewById(R.id.footerLl);
        maleSelectionIb = (ImageButton) rootView.findViewById(R.id.male_tick);
        femaleSelectionIb = (ImageButton) rootView.findViewById(R.id.female_tick);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.errorLayout);
        errorImage = (ImageView) rootView.findViewById(R.id.errorImg);
        errorMsgTitleTv = (TextView) rootView.findViewById(R.id.errorMsgTitle);
        errorMsgDescTv = (TextView) rootView.findViewById(R.id.errorMsgDesc);
        retryBtn = (Button) rootView.findViewById(R.id.tryAgainBtn);
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(),"Please wait ....");
        customUncancelableLoader.setCancelable(false);



        if(new Utils().isConnectedToInternet(getActivity())){
            showLoading(customUncancelableLoader);
            getProfileData();

        }else{
            headerFl.setVisibility(View.GONE);
            footerLl.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorMsgTitleTv.setText(getResources().getString(R.string.no_internet_title));
            errorMsgDescTv.setText(getResources().getString(R.string.no_internet_desc));
            hideLoading(customUncancelableLoader);
        }

        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);

            }
        });

        maleIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                femaleBtnStatus = 0;
                femaleIb.setImageDrawable(getResources().getDrawable(R.drawable.female_user_disabled));
                femaleSelectionIb.setVisibility(View.INVISIBLE);

                if(maleBtnStatus == 0){
                    maleBtnStatus = 1;
                    maleIb.setImageDrawable(getResources().getDrawable(R.drawable.male_user_selected));
                    maleSelectionIb.setVisibility(View.VISIBLE);
                }else{
                    maleBtnStatus = 0;
                    maleIb.setImageDrawable(getResources().getDrawable(R.drawable.male_user_disabled));
                    maleSelectionIb.setVisibility(View.INVISIBLE);
                }
            }
        });

        femaleIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                maleBtnStatus = 0;
                maleIb.setImageDrawable(getResources().getDrawable(R.drawable.male_user_disabled));
                maleSelectionIb.setVisibility(View.INVISIBLE);

                if(femaleBtnStatus == 0){
                    femaleBtnStatus = 1;
                    femaleIb.setImageDrawable(getResources().getDrawable(R.drawable.female_user_selected));
                    femaleSelectionIb.setVisibility(View.VISIBLE);
                }else{
                    femaleBtnStatus = 0;
                    femaleIb.setImageDrawable(getResources().getDrawable(R.drawable.female_user_disabled));
                    femaleSelectionIb.setVisibility(View.INVISIBLE);

                }

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateProfile();
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(customUncancelableLoader);
                getProfileData();
            }
        });

        return rootView;
    }

    private void updateProfile(){

        if (ValidatorUtils.validateName(firstNameEt) && (ValidatorUtils.validateName(lastNameEt)) && ValidatorUtils.validateEmail(emailIdEt)) {

            int sexId = 0;
            if(maleBtnStatus!=1 || femaleBtnStatus != 1){
                if(maleBtnStatus == 1){
                    sexId = 1;
                }else{
                    sexId = 2;
                }
                String fName = firstNameEt.getText().toString();
                String lName = lastNameEt.getText().toString();
                String email = emailIdEt.getText().toString();

                String sex = String.valueOf(sexId);
                updateProfileData(fName,lName,email,dobValue,sex);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    private void getProfileData(){

        NetworkManager.getInstance(getActivity()).getProfile();
    }

    private void initViews() {

        if(profile.getGender().equals("1")){
            maleBtnStatus = 1;
            maleIb.setImageDrawable(getResources().getDrawable(R.drawable.male_user_selected));
            maleSelectionIb.setVisibility(View.VISIBLE);
        }else if(profile.getGender().equals("2")){
            femaleBtnStatus = 1;
            femaleIb.setImageDrawable(getResources().getDrawable(R.drawable.female_user_selected));
            femaleSelectionIb.setVisibility(View.VISIBLE);
        }

        firstNameEt.setText(profile.getFirst_name());
        lastNameEt.setText(profile.getLast_name());
        emailIdEt.setText(profile.getEmail_id());
        phone.setText(profile.getPhone_number());


        dobEt.setText(profile.getDate_of_birth());
    }


    public static class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dobEt.setText(day + "/" + (month + 1) + "/" + year);
            dobValue = day + "/" + (month + 1) + "/" + year;
        }
    }


    private void updateProfileData(final String fName, final String lName, String email, String dob, String sex){
        if (new Utils().isConnectedToInternet(getContext())){
            showLoading(customUncancelableLoader);
            String phoneNumber = new SharedPrefUtils(getActivity()).getUserPhone();
            NetworkManager.getInstance(getActivity()).updateProfile(new ProfileRequest(fName,lName,email,dob,sex,phoneNumber));
        }
        else{
            hideLoading(customUncancelableLoader);
            Utils.showInternetDialog(getContext());
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


    public void onEvent(ProfileUpdateResponse profileUpdateResponse) {
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(getActivity());

        hideLoading(customUncancelableLoader);
        if(profileUpdateResponse == null){
            Toast.makeText(getActivity(), "Sorry! Unable to Update Profile", Toast.LENGTH_SHORT).show();
            showErrorDialog();
            return;
        } else{
            Toast.makeText(getActivity(), profileUpdateResponse.getMessage(), Toast.LENGTH_SHORT).show();
            sharedPrefUtils.setIsProfileUpdated(true);
            Intent myIntent = new Intent(getActivity(), DrawerActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
        }

    }

    public void onEvent(Profile profile)
    {
        hideLoading(customUncancelableLoader);
        if (profile == null){
            Toast.makeText(getActivity(), "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
            //show proper handle here with retry button
            showIfErrorOccurred();
        }else{
            this.profile = profile;
            initViews();
            headerFl.setVisibility(View.VISIBLE);
            footerLl.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }

    public void onEvent(ErrorTypes errorTypes)
    {
        hideLoading(customUncancelableLoader);
        if(errorTypes.getType() == ErrorTypes.UPDATE_PROFILE_ERROR)
        {
            Toast.makeText(getActivity(), "Sorry! Unable to Update Profile", Toast.LENGTH_SHORT).show();
            showErrorDialog();
        }
        if(errorTypes.getType() == ErrorTypes.GET_PROFILE_ERROR)
        {
            Toast.makeText(getActivity(), "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
            showIfErrorOccurred();
        }

    }

    private void showIfErrorOccurred(){
        headerFl.setVisibility(View.GONE);
        footerLl.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMsgTitleTv.setText(getResources().getString(R.string.error_title));
        errorMsgTitleTv.setVisibility(View.GONE);
        errorMsgDescTv.setText(getResources().getString(R.string.error_desc));
        hideLoading(customUncancelableLoader);
    }


    private void showErrorDialog(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Unable to update profile !!!");
        alertDialog2.setMessage("Please click on retry button to try again");
        alertDialog2.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateProfile();
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
