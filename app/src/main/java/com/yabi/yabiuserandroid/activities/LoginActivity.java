package com.yabi.yabiuserandroid.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.fragments.SigninProfileFragment;
import com.yabi.yabiuserandroid.interfaces.ToolbarInterface;

public class LoginActivity extends AppCompatActivity implements ToolbarInterface {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, LoginFragment.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, SigninProfileFragment.newInstance()).commit();

    }
    // Toolbar Interface Starts

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

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

}


