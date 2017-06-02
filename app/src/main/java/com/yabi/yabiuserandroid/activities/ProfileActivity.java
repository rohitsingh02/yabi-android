package com.yabi.yabiuserandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.yabi.yabiuserandroid.FbLoginActivity;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.fragments.ProfileFragment;
import com.yabi.yabiuserandroid.fragments.ViewProfileFragment;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, ViewProfileFragment.newInstance()).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //getSupportActionBar().setTitle("Yabi");
                onBackPressed();

                return true;
            case R.id.logout:
                SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(getBaseContext());
                        sharedPrefUtils.clearSharedPreferenceFile(getBaseContext());
                        sharedPrefUtils.setFirstTimeLaunch(false);
                        disconnectFromFacebook();
                        sharedPrefUtils.setIsProfileUpdated(false);

                        //NetworkManager.setNetworkManagerToNull();
                        Intent intent = new Intent(ProfileActivity.this, FbLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                return true;
            case R.id.edit:
                getSupportFragmentManager().beginTransaction().add(R.id.main_frame, ProfileFragment.newInstance()).commit();


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

}
