package com.yabi.yabiuserandroid.ui.uiutils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.yabi.yabiuserandroid.R;

import static com.yabi.yabiuserandroid.R.id.custom_loader;

/**
 * Created by rohitsingh on 26/10/16.
 */

public class CustomUncancelableLoader extends Dialog{


    public Activity c;
    public Dialog d;
    private TextView loaderTv;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    String  loaderText;



    public CustomUncancelableLoader(Activity a,String loaderText) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.loaderText = loaderText;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_loader_content);

        loaderTv = (TextView) findViewById(R.id.loadingContent);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(custom_loader);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
        loaderTv.setText(loaderText);

    }


}
