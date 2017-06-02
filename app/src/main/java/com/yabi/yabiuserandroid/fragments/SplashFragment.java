package com.yabi.yabiuserandroid.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.CoachActivity;
import com.yabi.yabiuserandroid.activities.DrawerActivity;
import com.yabi.yabiuserandroid.interfaces.ToolbarInterface;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import java.util.Timer;
import java.util.TimerTask;


public class SplashFragment extends Fragment {

    private ImageView imgLogo;
    private Animation animation2;
    SharedPrefUtils sharedPrefUtils;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    ToolbarInterface toolbarInterface;
    Timer timer;
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView =  (ViewGroup)inflater.inflate(R.layout.fragment_splash, container, false);
        imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        sharedPrefUtils = new SharedPrefUtils(getContext());
        avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.custom_loader);
        avLoadingIndicatorView.show();
        animate();

        return rootView;
    }

    private void performUITransaction() {
        Intent intent ;
        if(sharedPrefUtils.getUserPhone()!= null && sharedPrefUtils.getIsProfileUpdated())
            intent = new Intent(getContext(), DrawerActivity.class);
        else
            intent = new Intent(getContext(), CoachActivity.class);

        startActivity(intent);
    }


    private void animate() {
        try {
            imgLogo.setVisibility(View.VISIBLE);

            animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            animation2.setDuration(700);
            animation2.restrictDuration(700);
            animation2.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    textRestaurantName.setVisibility(View.VISIBLE);
                }
            });
            animation2.scaleCurrentDuration(1);

            Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_center);
            animation1.setDuration(700);
            animation1.restrictDuration(700);
            animation1.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imgLogo.setVisibility(View.VISIBLE);
//                    textRestaurantName.setVisibility(View.VISIBLE);
//                    textRestaurantName.startAnimation(animation2);
                }
            });
            animation1.scaleCurrentDuration(1);
            imgLogo.startAnimation(animation1);
            imgLogo.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            imgLogo.setVisibility(View.VISIBLE);
            imgLogo.setVisibility(View.VISIBLE);
//            textRestaurantName.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        callTimer(3000);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }




    @Override
    public void onAttach(Activity activity) {
        try {
            toolbarInterface = (ToolbarInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
     super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null){
            timer.cancel();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        toolbarInterface = null;
    }

    private void callTimer(int timerSec){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                performUITransaction();
                getActivity().finish();
                Log.e("jumping","activity");
            }
        },timerSec);
    }


}

