package com.yabi.yabiuserandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yabi.yabiuserandroid.activities.LoginActivity;
import com.yabi.yabiuserandroid.interfaces.ToolbarInterface;
import com.yabi.yabiuserandroid.models.data.UserFriends;
import com.yabi.yabiuserandroid.utils.AppConstants;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FbLoginActivity extends AppCompatActivity implements ToolbarInterface {

    Button fbLoginButton;
    private CallbackManager callbackManager;
    private Toolbar toolbar;
    private TextView mobileSignInTv;
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        final AccessToken accessToken =  loginResult.getAccessToken();
                        Log.d("ACCESSTOKEN",loginResult.getAccessToken().getToken());
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
//                                        Log.d("ID",object.optString("id"));
//                                        Log.d("NAME",object.optString("name"));
//                                        Log.d("LINK",object.optString("link"));
                                        sharedPrefUtils.writeString(AppConstants.PROFILE_PIC,"http://graph.facebook.com/" + object.optString("id") + "/picture?type=large");
                                        sharedPrefUtils.writeString(AppConstants.PROFILE_LINK,object.optString("link"));
                                        sharedPrefUtils.writeBoolean(AppConstants.IS_LOGIN_USING_FACEBOOK,true);
                                        startActivity(new Intent(FbLoginActivity.this, LoginActivity.class));
                                        myNewGraphReq(object.optString("id"), accessToken);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FbLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FbLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        AccessToken.setCurrentAccessToken(null);
                        LoginManager.getInstance().logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("public_profile","email","user_friends"));

                    }
                });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggleToolbarVisibility(false);
        mobileSignInTv = (TextView) findViewById(R.id.mobile_signin);
        fbLoginButton = (Button) findViewById(R.id.fb_login);
        sharedPrefUtils = new SharedPrefUtils(getBaseContext());

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("public_profile","email","user_friends"));
            }
        });

        mobileSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FbLoginActivity.this, LoginActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void myNewGraphReq(String friendlistId, AccessToken token) {
        final String graphPath = "/"+ friendlistId +"/friends/";

        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");


                    List<UserFriends> userFriendsList = new Gson().fromJson(arrayOfUsersInFriendList.toString(), new TypeToken<List<UserFriends>>(){}.getType());
                    sharedPrefUtils.writeString(AppConstants.USER_FRIENDS,new Gson().toJson(userFriendsList));


//                    for(int i = 0; i < arrayOfUsersInFriendList.length(); i++){
//
//
//
//                        JSONObject user = arrayOfUsersInFriendList.getJSONObject(i);
//                        String usersName = user.getString("name");
//                        String id = user.getString("id");
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("data","summary");
        request.setParameters(param);
        request.executeAsync();
    }




    @Override
    public void toggleToolbarVisibility(boolean value) {
        if (value) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void setToolbarTitle(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
        setActionBarFont();

    }

    private void setActionBarFont() {
        SpannableString s = new SpannableString(getTitle());
        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) actionBar.setTitle(s);
    }

    @Override
    public void setToolbarTheme(int toolbarTheme) {
        toolbar.setPopupTheme(toolbarTheme);
    }

    @Override
    public void setHomeUpEnabled(boolean value) {
        // getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    @Override
    public void setToolbarTitleTextColor(int toolbarTitleTextColor) {
        toolbar.setTitleTextColor(toolbarTitleTextColor);
    }

    @Override
    public void setHomeUpIndicator(int homeUpIndicator) {
        getSupportActionBar().setHomeAsUpIndicator(homeUpIndicator);
    }

    @Override
    public void setToolbarMenu(Menu menu) {

    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    @Override
    public void showMenuItems(boolean value) {
    }


}
