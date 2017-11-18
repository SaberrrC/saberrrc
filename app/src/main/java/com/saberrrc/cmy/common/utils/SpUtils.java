package com.saberrrc.cmy.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SpUtils {

    // sp的名字
    private final static String SP_NAME = "config";
    private static SharedPreferences sp;

    public static void saveBoolean(Context context, String key, boolean value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putBoolean(key, value).apply();
    }

    public static void saveInt(Context context, String key, int value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putInt(key, value).apply();
    }

    public static void saveString(Context context, String key, String value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putString(key, value).apply();
    }


    public static void saveSet(Context context, String key, Set<String> value) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        sp.edit().putStringSet(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getInt(key, defValue);

    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getString(key, defValue);

    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getBoolean(key, defValue);
    }

    public static Set<String> getSet(Context context, String key, Set<String> defValue) {

        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);

        return sp.getStringSet(key, defValue);
    }

    public static void removeValue(Context context, String value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(value);
        edit.apply();
    }

    public static void clear(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }
}
