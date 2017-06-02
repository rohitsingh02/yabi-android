package com.yabi.yabiuserandroid.interfaces;

import com.yabi.yabiuserandroid.ui.uiutils.CustomUncancelableLoader;

/**
 * Created by yogeshmadaan on 09/09/15.
 */
public interface LoadingInterface {

    void showLoading(CustomUncancelableLoader customUncancelableLoader);
    void hideLoading(CustomUncancelableLoader customUncancelableLoader);
}
