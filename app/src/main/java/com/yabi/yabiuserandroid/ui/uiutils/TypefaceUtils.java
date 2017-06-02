package com.yabi.yabiuserandroid.ui.uiutils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by rohitsingh on 16/12/16.
 */

public class TypefaceUtils {
    private static String typefacepath = "fonts/";
    public static String COUR=typefacepath+"cour.ttf";
    public static String COURIERNEWBOLD=typefacepath+"courtbold.ttf";
    public static String ROBOTOLIGHT=typefacepath+"roboto_light.ttf";
    public static String SF_UI_BOLD=typefacepath+"sf_ui_bold.ttf";
    public static String SF_UI_REGULAR=typefacepath+"sf_ui_regular.ttf";
    public static String SF_UI_THIN=typefacepath+"sf_ui_thin.ttf";
    public static String SF_UI_ULTRA_THIN=typefacepath+"sf_ui_ultra_thin.ttf";
    public static String ROBOTO_LIGHT=typefacepath+"roboto_light.ttf";
    public static String ROBOTO_BOLD=typefacepath+"roboto_bold.ttf";
    public static String ROBOTO_MEDIUM=typefacepath+"roboto_medium.ttf";
    public static String ROBOTO_THIN=typefacepath+"roboto_thin.ttf";
    public static String ROBOTO_THIN_ITALIC=typefacepath+"roboto_thin_italic.ttf";
    public static String type=".ttf";

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface setTypeface(Context context, String typeface){
        return Typeface.createFromAsset(context.getAssets(),typeface);
    }


    public static Typeface get(String name, Context context) {
        name = typefacepath+name+type;
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(),name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

}
