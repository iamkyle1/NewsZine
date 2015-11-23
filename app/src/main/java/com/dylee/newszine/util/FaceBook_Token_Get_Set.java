package com.dylee.newszine.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.facebook.AccessToken;

/**
 * Created by 1000128 on 15. 11. 7..
 */
public class FaceBook_Token_Get_Set {


    public static void setAppPreferences(Activity context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("FacebookCon", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);

        prefEditor.commit();
    }

    // app 쉐어드 프레퍼런스에서 값을 읽어옴
    public static String getAppPreferences(Activity context, String key) {
        String returnValue = null;

        SharedPreferences pref = null;
        pref = context.getSharedPreferences("FacebookCon", 0);

        returnValue = pref.getString(key, "");

        return returnValue;
    }

    public static void setAppPreferences4Ad(Activity context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("Ad", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);

        prefEditor.commit();
    }

    // app 쉐어드 프레퍼런스에서 값을 읽어옴
    public static String getAppPreferences4Ad(Activity context, String key) {
        String returnValue = null;

        SharedPreferences pref = null;
        pref = context.getSharedPreferences("Ad", 0);

        returnValue = pref.getString(key, "");

        return returnValue;
    }
}
