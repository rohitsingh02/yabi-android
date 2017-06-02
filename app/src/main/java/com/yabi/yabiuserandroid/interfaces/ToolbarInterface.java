package com.yabi.yabiuserandroid.interfaces;

import android.view.Menu;

/**
 * Created by yogeshmadaan on 01/09/15.
 */
public interface ToolbarInterface {
    void toggleToolbarVisibility(boolean value);
    void setToolbarTitle(String toolbarTitle);
    void setToolbarTheme(int toolbarTheme);
    void setHomeUpEnabled(boolean value);
    void setToolbarTitleTextColor(int toolbarTitleTextColor);
    void setHomeUpIndicator(int homeUpIndicator);
    void setToolbarMenu(Menu menu);
    void setToolbarBackgroundColor(int color);
    void showMenuItems(boolean value);
}
