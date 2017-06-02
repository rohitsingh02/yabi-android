package com.yabi.yabiuserandroid.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.utils.AppConstants;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import static com.yabi.yabiuserandroid.activities.DrawerActivity.ACCESSIBILITY_FEATURE;
import static com.yabi.yabiuserandroid.activities.DrawerActivity.OVERLAY_PERMISSION_REQ_CODE_CHATHEAD;

/**
 * Created by rohitsingh on 22/03/17.
 */

public class WidgetPermissionPopupActivity extends AppCompatActivity {

    private Button cancelButton;
    private Button enableWidget;
    private SharedPrefUtils sharedPrefUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_permission);
        cancelButton = (Button)findViewById(R.id.cancelDialog);
        enableWidget = (Button) findViewById(R.id.enableWidget);
        sharedPrefUtils = new SharedPrefUtils(WidgetPermissionPopupActivity.this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8),(int)(height * 0.7));


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        enableWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!isAccessibilitySettingsOn(getApplicationContext()))
                {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent,ACCESSIBILITY_FEATURE);
                }
                else if(!Utils.canDrawOverlays(WidgetPermissionPopupActivity.this)){
                    requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
                }
            }
        });

    }


    private void requestPermission(int requestCode){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, requestCode);
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        String TAG = "LOG";
        final String service = getPackageName() + "/" + "com.yabi.yabiuserandroid.services.widget.services.WindowChangeDetectingService";
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");

            sharedPrefUtils.writeBoolean(AppConstants.IS_ACCESSIBILITY_PERMISSION_GIVEN, true);

            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }


    private void needPermissionDialog(final int requestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(WidgetPermissionPopupActivity.this);
        builder.setMessage("You need to allow permission");
        builder.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        requestPermission(requestCode);
                    }
                });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.setCancelable(false);
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== ACCESSIBILITY_FEATURE)
        {
            if(!Utils.canDrawOverlays(WidgetPermissionPopupActivity.this)){
                requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
            }
        }
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD) {
            if (!Utils.canDrawOverlays(WidgetPermissionPopupActivity.this)) {
                needPermissionDialog(requestCode);
            }else{
                sharedPrefUtils.writeBoolean(AppConstants.IS_CAN_DRAW_OVERLAY_PERMISSION_GIVEN, true);
            }
        }

        finish();

    }


}
