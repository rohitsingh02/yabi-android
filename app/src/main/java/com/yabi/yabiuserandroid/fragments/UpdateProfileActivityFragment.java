package com.yabi.yabiuserandroid.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.DrawerActivity;
import com.yabi.yabiuserandroid.activities.LoginActivity;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
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
 * A placeholder fragment containing a simple view.
 */
public class UpdateProfileActivityFragment extends Fragment implements LoadingInterface {

    private CustomFontEditTextView firstNameEt;
    private CustomFontEditTextView lastNameEt;
    private CustomFontEditTextView emailIdEt;
    static CustomFontEditTextView dobEt;
    private Button submitBtn;
    private ImageButton maleIb;
    private ImageButton femaleIb;
    private ImageButton maleSelectionIb;
    private ImageButton femaleSelectionIb;
    private TextView connectionTv;
    private ImageView orImageView;
    int maleBtnStatus = 0;
    int femaleBtnStatus = 0;
    static String dob;
    private SharedPrefUtils sharedPrefUtils;
    private EventBus eventBus;
    CustomUncancelableLoader customUncancelableLoader;
    public UpdateProfileActivityFragment() {
    }


    public static UpdateProfileActivityFragment newInstance() {
        UpdateProfileActivityFragment fragment = new UpdateProfileActivityFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_update_profile, container, false);
        firstNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.fNameEt);
        lastNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.lNameEt);
        emailIdEt = (CustomFontEditTextView) rootView.findViewById(R.id.emailEt);
        dobEt = (CustomFontEditTextView) rootView.findViewById(R.id.dobEt);
        submitBtn = (Button) rootView.findViewById(R.id.submitBtn);
        maleIb = (ImageButton) rootView.findViewById(R.id.male_btn);
        femaleIb = (ImageButton) rootView.findViewById(R.id.female_btn);
        maleSelectionIb = (ImageButton) rootView.findViewById(R.id.male_tick);
        femaleSelectionIb = (ImageButton) rootView.findViewById(R.id.female_tick);
        connectionTv = (TextView) rootView.findViewById(R.id.noInternet);
        orImageView = (ImageView) rootView.findViewById(R.id.orbtn);
        customUncancelableLoader = new CustomUncancelableLoader(getActivity(),"Please wait ....");
        customUncancelableLoader.setCancelable(false);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.BLACK)
                .endConfig()
                .buildRound("Or",
                        Color.LTGRAY);
        orImageView.setImageDrawable(drawable);
        ((LoginActivity)getActivity()).updateStatusBarColor("#313747");


        sharedPrefUtils = new SharedPrefUtils(getActivity());
        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);

            }
        });

        maleIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orImageView.getVisibility() == View.VISIBLE){
                    orImageView.setVisibility(View.INVISIBLE);
                }
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


                if(orImageView.getVisibility() == View.VISIBLE){
                    orImageView.setVisibility(View.INVISIBLE);
                }
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


                if(new Utils().isConnectedToInternet(getActivity())){
                    updateProfileData(fName,lName,email,dob,sex);
                }else {
                    connectionTv.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }


    private void updateProfileData(final String fName, final String lName, String email, String dob, String sex){
        showLoading(customUncancelableLoader);
        String phoneNumber = new SharedPrefUtils(getActivity()).getUserPhone();
        NetworkManager.getInstance(getActivity()).updateProfile(new ProfileRequest(fName,lName,email,dob,sex,phoneNumber));
    }

    @Override
    public void showLoading(CustomUncancelableLoader customUncancelableLoader) {
        if(customUncancelableLoader != null && !customUncancelableLoader.isShowing()){
            customUncancelableLoader.show();
        }
    }

    @Override
    public void hideLoading(CustomUncancelableLoader customUncancelableLoader) {
        if(customUncancelableLoader != null && customUncancelableLoader.isShowing()){
            customUncancelableLoader.hide();
        }
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
            dob = day + "/" + (month + 1) + "/" + year;
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
        hideLoading(customUncancelableLoader);
        if(profileUpdateResponse == null){
            Toast.makeText(getActivity(), "Sorry! Unable to Update Profile", Toast.LENGTH_SHORT).show();
            showErrorDialog();

            return;
        } else{
            Toast.makeText(getActivity(), profileUpdateResponse.getMessage(), Toast.LENGTH_SHORT).show();
            sharedPrefUtils.setIsProfileUpdated(true);
            Intent myIntent = new Intent(getActivity(), DrawerActivity.class);
            startActivity(myIntent);
        }

    }

    public void onEvent(ErrorTypes errorTypes)
    {
        if(errorTypes.getType() == ErrorTypes.UPDATE_PROFILE_ERROR)
        {
            hideLoading(customUncancelableLoader);
            showErrorDialog();
        }
    }


    private void showErrorDialog(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Unable to update profile!!!");
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


}
