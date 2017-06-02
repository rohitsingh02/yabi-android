package com.yabi.yabiuserandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.fragments.OfferListingFragment;
import com.yabi.yabiuserandroid.fragments.OutletListingFragment;
import com.yabi.yabiuserandroid.models.data.Profile;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.ui.uiutils.CustomTypefaceSpan;
import com.yabi.yabiuserandroid.utils.AppConstants;
import com.yabi.yabiuserandroid.utils.AppVersion;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import de.greenrobot.event.EventBus;

import static com.yabi.yabiuserandroid.R.id.toolbar;


/**
 * Created by rohitsingh on 16/12/16.
 */

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SmoothActionBarDrawerToggle toggle;
    private Toolbar mToolbar;
    private boolean viewIsAtHome;
    private Boolean isCameFromDeepLinking;
    private int merchantId;
    private Boolean isOfferLink;
    private int position;
    private ImageView logo;
    private TextView name;
    private TextView email;
    private SharedPrefUtils sharedPrefUtil;
    EventBus eventBus;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234;
    public static int ACCESSIBILITY_FEATURE = 12345;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        setUpToolbar();
        getSupportedMerchantListing();
//        if(!isAccessibilitySettingsOn(getApplicationContext()))
//        {
//            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            startActivityForResult(intent,ACCESSIBILITY_FEATURE);
//        }
//        else if(!Utils.canDrawOverlays(DrawerActivity.this)){
//            requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
//        }
        sharedPrefUtil = new SharedPrefUtils(DrawerActivity.this);
        checkForSenderType();
        if(sharedPrefUtil.readString("FULLNAME",null) == null){
            getProfileData();
        }
        initView();
        setFragment(R.id.home);
        initializeNavigationDrawer();
        if(isCameFromDeepLinking){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportFragmentManager().beginTransaction().add(R.id.container_body, OfferListingFragment.newInstance(merchantId)).addToBackStack(null).commit();
            if(isOfferLink){
                OfferListingFragment.oldInstance().setOfferDeepLinkDetail(isOfferLink,position);
            }
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

    private void checkForSenderType() {

        if(sharedPrefUtil.readBoolean("IsCameFromDeepLinking",false)){
            isCameFromDeepLinking = sharedPrefUtil.readBoolean("IsCameFromDeepLinking",false);
            merchantId = sharedPrefUtil.readInteger("merchantId",-1);
            isOfferLink = sharedPrefUtil.readBoolean("ISOFFERLINK",false);
            position = sharedPrefUtil.readInteger("OFFERPOSITION",-1);
        }else{
            Intent intent = getIntent();
            isCameFromDeepLinking = intent.getBooleanExtra("IsCameFromDeepLinking",false);
            merchantId = intent.getIntExtra("MERCHANTID",-1);
            isOfferLink = intent.getBooleanExtra("ISOFFERLINK",false);
            position = intent.getIntExtra("OFFERPOSITION",-1);
        }
    }


    private void setUpToolbar() {
        try {
            mToolbar = (Toolbar) findViewById(toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            logo = (ImageView) headerView.findViewById(R.id.logo);
            name = (TextView) headerView.findViewById(R.id.name);
            email = (TextView) headerView.findViewById(R.id.email);
            name.setText(sharedPrefUtil.readString("FULLNAME",null));
            email.setText(sharedPrefUtil.readString("EMAIL",null));
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .endConfig()
                    .buildRound(sharedPrefUtil.readString("SHORTNAME","").toLowerCase(),
                            Color.parseColor("#e6db4538"));
            logo.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initializeNavigationDrawer() {
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);
        toggle = new SmoothActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                            getSupportActionBar().setTitle("Yabi");
                        }
                    });
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    if(!toggle.isDrawerIndicatorEnabled()){
                        toggle.setDrawerIndicatorEnabled(true);
                    }
                    toggle.syncState();
                    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });

    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    drawerLayout.openDrawer(GravityCompat.START);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                return true;
            default:
                return false;
        }
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        //Set custom font
        for (int i=0;i<menu.size();i++) {
            MenuItem mi = menu.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
        //Set Version Number
        MenuItem app_version = menu.findItem(R.id.app_version);
        app_version.setTitle("v"+    AppVersion.getVersionNumber(DrawerActivity.this));
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public void setFragment(int id) {
        switch (id) {
            case R.id.home:
                final Menu navMenu = navigationView.getMenu();
                final MenuItem item = navMenu.findItem(R.id.home);
                item.setChecked(true);
                viewIsAtHome = true;
                switchFragment(OutletListingFragment.class);
                getSupportActionBar().setTitle("Yabi");
                break;

            case R.id.profile:
                viewIsAtHome = false;
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DrawerActivity.this, ProfileActivity.class));
//                        getSupportActionBar().setTitle("Profile");
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });
                drawerLayout.closeDrawers();
                break;

            case R.id.favourites:
                viewIsAtHome = false;
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DrawerActivity.this, FavouritesActivity.class));
                        getSupportActionBar().setTitle("Favourites");
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });
                drawerLayout.closeDrawers();
                break;

            case R.id.action_share:
                viewIsAtHome = false;
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent();
                        myIntent.setAction(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        myIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.playstoreLink);
                        myIntent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.shareText);
                        startActivity(Intent.createChooser(myIntent, "Invite via"));
                    }
                });
                break;

            case R.id.rate_us:
                viewIsAtHome = false;
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        new Utils().launchMarket(DrawerActivity.this);
                    }
                });
                break;

            case R.id.feedback:
                viewIsAtHome = false;
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","saurabh@yabi.io", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });
                break;

            default:
                switchFragment(OutletListingFragment.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerLayout.closeDrawers();
    }




    private void switchFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();

    }

    public void selectDrawerItem(MenuItem menuItem) {
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        setFragment(menuItem.getItemId());
    }




    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if(requestCode== ACCESSIBILITY_FEATURE)
//        {
//            if(!Utils.canDrawOverlays(DrawerActivity.this)){
//                requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
//            }
//        }
//        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD) {
//            if (!Utils.canDrawOverlays(DrawerActivity.this)) {
//                needPermissionDialog(requestCode);
//            }
//        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_body);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);

    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void getProfileData(){

        NetworkManager.getInstance(DrawerActivity.this).getProfile();
    }

    public void onEvent(Profile profile)
    {
        if (profile == null){
            Toast.makeText(DrawerActivity.this, "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
        }else{

            String firstName = profile.getFirst_name();
            String lastName = profile.getLast_name();
            String fullName = profile.getFirst_name() + " " + profile.getLast_name();
            String email = profile.getEmail_id();
            String shortName = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0));
            sharedPrefUtil.writeString("FULLNAME",fullName);
            sharedPrefUtil.writeString("EMAIL",email);
            sharedPrefUtil.writeString("SHORTNAME",shortName);
            initHeaderView();
        }
    }

    private void initHeaderView(){
        name.setText(sharedPrefUtil.readString("FULLNAME",null));
        email.setText(sharedPrefUtil.readString("EMAIL",null));
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .endConfig()
                .buildRound(sharedPrefUtil.readString("SHORTNAME","").toLowerCase(), R.color.primary_color);
        logo.setImageDrawable(drawable);
    }

    public void onEvent(ErrorTypes errorTypes)
    {

        if(errorTypes.getType() == ErrorTypes.GET_PROFILE_ERROR)
        {
            Toast.makeText(this, "Sorry! Unable to Get Profile Data", Toast.LENGTH_SHORT).show();
        }

    }


    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {
        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }

    }




//    private void requestPermission(int requestCode){
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, requestCode);
//    }

//    private boolean isAccessibilitySettingsOn(Context mContext) {
//        int accessibilityEnabled = 0;
//        String TAG = "LOG";
//        final String service = getPackageName() + "/" + "com.yabi.yabiuserandroid.services.widget.services.WindowChangeDetectingService";
//        try {
//            accessibilityEnabled = Settings.Secure.getInt(
//                    mContext.getApplicationContext().getContentResolver(),
//                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
//        } catch (Settings.SettingNotFoundException e) {
//            Log.e(TAG, "Error finding setting, default accessibility to not found: "
//                    + e.getMessage());
//        }
//        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
//
//        if (accessibilityEnabled == 1) {
//            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
//            String settingValue = Settings.Secure.getString(
//                    mContext.getApplicationContext().getContentResolver(),
//                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//            if (settingValue != null) {
//                mStringColonSplitter.setString(settingValue);
//                while (mStringColonSplitter.hasNext()) {
//                    String accessibilityService = mStringColonSplitter.next();
//                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
//                    if (accessibilityService.equalsIgnoreCase(service)) {
//                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
//                        return true;
//                    }
//                }
//            }
//        } else {
//            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
//        }
//
//        return false;
//    }


//    private void needPermissionDialog(final int requestCode){
//        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
//        builder.setMessage("You need to allow permission");
//        builder.setPositiveButton("OK",
//                new android.content.DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        requestPermission(requestCode);
//                    }
//                });
//        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        builder.setCancelable(false);
//        builder.show();
//    }


    public void getSupportedMerchantListing(){

        NetworkManager.getInstance(DrawerActivity.this).getSupportedAppsList();

    }


}

