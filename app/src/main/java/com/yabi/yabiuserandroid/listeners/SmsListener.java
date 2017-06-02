package com.yabi.yabiuserandroid.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by rohit_singh on 17/02/16.
 */

public class SmsListener extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                strMessage += " : ";
                strMessage += messages[i].getMessageBody();
                strMessage += "\n";
            }

            Log.e("SMS", strMessage);
            strMessage = strMessage.substring(strMessage.lastIndexOf(" ") + 1);
            Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();

            Intent i = new Intent("send");
            // You can also include some extra data.
            i.putExtra("OTP", strMessage);
            context.sendBroadcast(i);
        }
    }


}