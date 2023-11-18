package com.mjb68.test;

import android.content.Context;
import android.content.SharedPreferences;

public class Helper {
    public static final void setSharedPreferences(Context context, String key, String val) {
        try {
            if (context == null)
                return;
            SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            sp.edit().putString(key, val).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String getSharedPreferences(Context context, String key) {
        try {
            if (context == null)
                return null;
            SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            return sp.getString(key, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }
}
