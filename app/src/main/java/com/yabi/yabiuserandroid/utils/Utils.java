package com.yabi.yabiuserandroid.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.activities.SplashActivity;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.provider.OffersProvider;
import com.yabi.yabiuserandroid.provider.OffersSQLiteHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by yogeshmadaan on 31/08/15.
 */

public class Utils {

    private static final String LT_PREFIX = "LTM";
    private final static String MS_DATE_FORMAT = "yy-MM-dd HH:mm:ss";
    private final static int FONT_SIZE_27 = 27;
    private final static int FONT_SIZE_16 = 16;
    public static final String LogTag ="LOG";

    public  final static String BASE_URL = "http://52.34.165.75:3000/api/v1/user/";
    public final static int REQUEST_CODE_ASK_PERMISSIONS = 123;


    /**
     * Android unique device id
     *
     * @param context
     * @return
     */
    public static String getAndroidDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }


    public static void showInternetDialog(Context context){
        android.support.v7.app.AlertDialog.Builder alertDialog2 = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog2.setTitle("No Internet!!!");
        alertDialog2.setMessage("Please connect to internet to proceed");
        alertDialog2.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }

    /**
     * Checks if the device is connected to the Internet.
     *
     * @param context
     * @return
     */
    public static boolean isConnectedToInternet(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackgroundOfView(Drawable drawable, View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    /**
     * No internet dialog
     *
     * @param context
     * @return
     */
    /**
     * Show error dialog
     *
     * @param context
     * @return
     */
    public static boolean showSuccessDialog(Context context, String successMessage, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(successMessage);
        builder.setPositiveButton(context.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        return false;
    }

    /**
     * hide keyboard for the edittext
     *
     * @param context
     * @param editText
     */
    public static void hideKeyboardForField(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void hideKeyboardForField(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void showKeyboardForField(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String replaceAllSpecialCharsAndSpaces(String text) {
        if (!TextUtils.isEmpty(text)) {
            text = text.replaceAll("[^\\w\\s]", "");
            text = text.replaceAll(" ", "");
        }
        return text;
    }


    public static boolean isLocationServiceEnabled(final Context context) {
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        }
        return true;
    }

    public static String generateLTX() {
        String currentTimestamp = getMSDateString(new Date());
        String txnId = LT_PREFIX + currentTimestamp
                + generateRandomNumberLTX().toString();
        txnId = txnId.replaceAll("-", "").replaceAll(" ", "")
                .replaceAll(":", "").replace(".", "");
        return txnId;
    }

    private static Integer generateRandomNumberLTX() {
        Random rn = new Random();
//        int number = (1 + rn.nextInt(100)) * 10 + (1 + rn.nextInt(1000)) * 100
//                + (1 + rn.nextInt(10000)) * 1000 + (1 + rn.nextInt(10000));
        int number = (1 + rn.nextInt(1000)) * 10 + (1 + rn.nextInt(1000));
        return number;
    }

    public static String getMSDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(MS_DATE_FORMAT);
        return sdf.format(date);
    }

    public static Integer getPxInDpUnits(Context ctxt, Integer px) {
        float scale = ctxt.getResources().getDisplayMetrics().density;
        Integer dps = (int) ((px - 0.5) / scale);
        return dps;
    }

    public static Integer getDpInPxUnits(Context ctxt, Integer dps) {
        float scale = ctxt.getResources().getDisplayMetrics().density;
        Integer px = (int) (dps * scale + 0.5f);
        return px;
    }

//    public static boolean isServiceRunning(Context context, Class serviceName) {
//        ActivityManager manager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager
//                .getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceName.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static Date getDisplayDate(Date date, String timeZoneOffset) {
        TimeZone timeZone = TimeZone.getTimeZone("GMT" + timeZoneOffset);
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeZone(timeZone);
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, timeZone.getRawOffset());
        return cal.getTime();

    }

    public static void launchAppMarketUrl(Activity activity) {
        String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }



    public static void shareUsingApps(Context context, String subject, String text) {
//        String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Using"));

    }

    public static boolean isFragmentAdded(Fragment fragment, Activity activity) {
        if (fragment.isAdded() && activity != null) {
            return true;
        }
        return false;
    }

    public static Calendar parseTime(String _time) {
        final String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Calendar startCalendar = Calendar.getInstance();
        try {
            startCalendar.setTime(dateFormat.parse(_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startCalendar;
    }

    public static String parseTimeToDate(String _time) {

        Calendar startCalendar = Calendar.getInstance();
        try {
            final String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            startCalendar.setTime(dateFormat.parse(_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(startCalendar.getTime());
    }
    public static File getInternalStorageLocation(Context context, String filename)
    {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("splashImages", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename);
        return mypath;
    }

        public static String findDateDifference(String date)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(date);
                d2 = new Date();

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffYears = diffDays / 365;
                System.out.print(diffDays + " days, ");
                System.out.print(diffHours + " hours, ");
                System.out.print(diffMinutes + " minutes, ");
                System.out.print(diffSeconds + " seconds.");
                if (diffYears > 0) {
                    return String.valueOf(diffYears) + " years ago";
                } else if (diffDays > 0) {
                    return String.valueOf(diffDays) + " days ago";
                } else if (diffHours > 0) {
                    return String.valueOf(diffHours) + " hours ago";
                } else if (diffMinutes > 0) {
                    return String.valueOf(diffMinutes) + " minutes ago";
                } else return "Just Now";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    public static  int getScreenWidth(Context context)
    {
        Display display = null;
        try{
            display = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay();
        }catch(Exception e)
        {

        }
        try{
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
        }catch(Exception e)
        {

        }        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }


    public static int getScreenHeight(Context context)
    {
        Display display = null;
        try{
            display = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay();
        }catch(Exception e)
        {

        }
        try{
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
        }catch(Exception e)
        {

        }
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return height;
    }


    public static Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    return b;
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void restartActivity(Activity activity)
    {
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        if(activity!=null)
            activity.finish();
    }
    public static void restartActivity(Context context)
    {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(300);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static enum PaymentType {
        LOAD_MONEY, CITRUS_CASH, PG_PAYMENT, DYNAMIC_PRICING;
    }

    public enum DPRequestType {
        SEARCH_AND_APPLY, CALCULATE_PRICING, VALIDATE_RULE;
    }

//    public void showBackButton(Activity activity) {
//        if (activity instanceof HomeActivity) {
//
//            ((HomeActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            ((HomeActivity) activity).getSupportActionBar().setTitle("Profile");
//            //mDrawerToggle.setDrawerIndicatorEnabled(false);
//
//        }
//    }

    public void launchMarket(Activity activity)
    {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
//        Uri uri = Uri.parse("http://onelink.to/provakil");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try
        {
            activity.startActivity(myAppLinkToMarket);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(activity, " Sorry, Not able to open playstore!", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isFavourite(Context context,Offers offers)
    {
        String URL = OffersProvider.URL;
        Uri movies = Uri.parse(URL);
        Cursor cursor = null;
        cursor = context.getContentResolver().query(movies, null, OffersSQLiteHelper.ID+" = "+createOfferId(offers), null, OffersSQLiteHelper.ROW_ID);
        if (cursor != null&&cursor.moveToNext()) {
            return true;
        } else {
            return false;
        }
    }


    public static int createOfferId(Offers offers)
    {
        int id ;
        String s = offers.getMerchant_id()+"_"+offers.getCode();
        id = s.hashCode();
        return id;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static List<Offers> getFavouriteOffers(Context context)
    {
        List<Offers> offers = new ArrayList<>();
        String URL = OffersProvider.URL;
        Uri movie = Uri.parse(URL);
        Cursor cursor = null;
        cursor = context.getContentResolver().query(movie, null, null, null, OffersSQLiteHelper.ROW_ID);
        if (cursor != null) {
            while (cursor.moveToNext())
            {
                Offers offer = new Offers();
                offer.setCode(cursor.getString(cursor.getColumnIndex(OffersSQLiteHelper.CODE)));
                offer.setDescription(cursor.getString(cursor.getColumnIndex(OffersSQLiteHelper.DESCRIPTION)));
                offer.setMerchant_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(OffersSQLiteHelper.MECHANT_ID))));
                offer.setTitle(cursor.getString(cursor.getColumnIndex(OffersSQLiteHelper.TITLE)));
                offer.setT_n_c(cursor.getString(cursor.getColumnIndex(OffersSQLiteHelper.TNC)));
                offers.add(offer);
            }
        }
        if(offers.size()==0)
            Toast.makeText(context,context.getResources().getString(R.string.text_no_favourites),Toast.LENGTH_LONG).show();
        return offers;
    }

    public static int toggleFavourite(Context context, Offers offers)
    {

        Uri.Builder uriBuilder = OffersProvider.CONTENT_URI.buildUpon();

        if(isFavourite(context, offers))
            context.getContentResolver().delete(uriBuilder.build(),String.valueOf(createOfferId(offers)),null);
        else
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(OffersSQLiteHelper.ID, createOfferId(offers));
            contentValues.put(OffersSQLiteHelper.TITLE, offers.getTitle());
            contentValues.put(OffersSQLiteHelper.CODE, offers.getCode());
            contentValues.put(OffersSQLiteHelper.DESCRIPTION, offers.getDescription());
            contentValues.put(OffersSQLiteHelper.MECHANT_ID, offers.getMerchant_id());
            contentValues.put(OffersSQLiteHelper.TNC, offers.getT_n_c());
            context.getContentResolver().insert(OffersProvider.CONTENT_URI, contentValues);
        }
        return 0;
    }




    public static void saveFavourite(Context context, Offers offers)
    {
        Uri.Builder uriBuilder = OffersProvider.CONTENT_URI.buildUpon();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OffersSQLiteHelper.ID, createOfferId(offers));
        contentValues.put(OffersSQLiteHelper.TITLE, offers.getTitle());
        contentValues.put(OffersSQLiteHelper.CODE, offers.getCode());
        contentValues.put(OffersSQLiteHelper.DESCRIPTION, offers.getDescription());
        contentValues.put(OffersSQLiteHelper.MECHANT_ID, offers.getMerchant_id());
        contentValues.put(OffersSQLiteHelper.TNC, offers.getT_n_c());
        context.getContentResolver().insert(OffersProvider.CONTENT_URI, contentValues);
    }

    public static boolean canDrawOverlays(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }else{
            return Settings.canDrawOverlays(context);
        }
    }


}
