package com.yabi.yabiuserandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.fragments.SplashFragment;
import com.yabi.yabiuserandroid.interfaces.ToolbarInterface;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashActivity extends AppCompatActivity implements ToolbarInterface {

    private Toolbar toolbar;
    private SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.yabi.yabiuserandroid",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }




        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, SplashFragment.newInstance()).commit();
        toggleToolbarVisibility(false);
        sharedPrefUtils = new SharedPrefUtils(SplashActivity.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Toolbar Interface Starts

    @Override
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...

                    Boolean isMerchantLink = Boolean.valueOf(referringParams.optString("ISMERCHANTLINK", "false"));
                    int merchantId = Integer.valueOf(referringParams.optString("MERCHANTID", ""));
//                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    if(!sharedPrefUtils.getIsProfileUpdated()){
                        sharedPrefUtils.writeBoolean("IsCameFromDeepLinking",true);
                        sharedPrefUtils.writeInteger("merchantId",merchantId);
                        if (!isMerchantLink) {
                            int position = Integer.valueOf(referringParams.optString("OFFERPOSITION", ""));
                            sharedPrefUtils.writeBoolean("ISOFFERLINK",true);
                            sharedPrefUtils.writeInteger("OFFERPOSITION",position);
                        }
                    }else{

                        Intent i = new Intent(SplashActivity.this, DrawerActivity.class);
                        i.putExtra("IsCameFromDeepLinking", true);
                        i.putExtra("MERCHANTID", merchantId);
                        if (!isMerchantLink) {
                            int position = Integer.valueOf(referringParams.optString("OFFERPOSITION", ""));
                            i.putExtra("ISOFFERLINK", true);
                            i.putExtra("OFFERPOSITION", position);
                        }

                        startActivity(i);
                    }


                    Log.i("MyApp", "deep link data: " + referringParams.toString());
                } else {
                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        branch.setIdentity("your user id");
    }

    @Override
    public void onStop() {
        super.onStop();
        Branch.getInstance(getApplicationContext()).closeSession();
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
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
//        s.setSpan(new com.limetray.appbuilder.onlineorders.uiutils.TypefaceSpan(this,"Roboto-Bold.ttf"), 0, s.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
