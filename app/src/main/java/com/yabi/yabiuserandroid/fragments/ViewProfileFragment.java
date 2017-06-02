package com.yabi.yabiuserandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.interfaces.LoadingInterface;
import com.yabi.yabiuserandroid.models.data.Profile;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;
import com.yabi.yabiuserandroid.ui.uiutils.palette.CustomFontEditTextView;
import com.yabi.yabiuserandroid.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment implements LoadingInterface {

    private CustomFontEditTextView firstNameEt;
    private CustomFontEditTextView lastNameEt;
    private CustomFontEditTextView emailIdEt;
    static CustomFontEditTextView dobEt;
    private ImageButton maleIb;
    private ImageButton femaleIb;
    private ImageButton maleSelectionIb;
    private ImageButton femaleSelectionIb;
    CustomUncancelableLoader customUncancelableLoader;
    private FrameLayout headerFl;
    private LinearLayout footerLl;
    private ImageView errorImage;
    private TextView errorMsgTitleTv;
    private TextView errorMsgDescTv;
    private Button retryBtn;
    private LinearLayout errorLayout;
    int maleBtnStatus = 0;
    int femaleBtnStatus = 0;
    Profile profile;
    private EditText phone;
    static String dobValue;
    EventBus eventBus;

    public static ViewProfileFragment newInstance() {
        ViewProfileFragment fragment = new ViewProfileFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_profile, container, false);
        firstNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.fNameEt);
        lastNameEt = (CustomFontEditTextView) rootView.findViewById(R.id.lNameEt);
        emailIdEt = (CustomFontEditTextView) rootView.findViewById(R.id.emailEt);
        dobEt = (CustomFontEditTextView) rootView.findViewById(R.id.dobEt);
        phone = (EditText) rootView.findViewById(R.id.phone);
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

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(customUncancelableLoader);
                getProfileData();
            }
        });

        return rootView;
    }


    private void getProfileData(){
        errorLayout.setVisibility(View.GONE);
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


    public void onEvent(Profile profile)
    {
        hideLoading(customUncancelableLoader);
        if (profile == null){
            Toast.makeText(getActivity(), "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
            showIfErrorOccurred();
        }else{
            this.profile = profile;
            initViews();
            headerFl.setVisibility(View.VISIBLE);
            footerLl.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(ErrorTypes errorTypes)
    {

        if(errorTypes.getType() == ErrorTypes.GET_PROFILE_ERROR)
        {
            hideLoading(customUncancelableLoader);
            Toast.makeText(getActivity(), "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
            showIfErrorOccurred();
        }

    }

    private void showIfErrorOccurred(){
        errorLayout.setVisibility(View.VISIBLE);
        errorMsgTitleTv.setText(getResources().getString(R.string.error_title));
        errorMsgTitleTv.setVisibility(View.GONE);
        errorMsgDescTv.setText(getResources().getString(R.string.error_desc));
        hideLoading(customUncancelableLoader);
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
}

