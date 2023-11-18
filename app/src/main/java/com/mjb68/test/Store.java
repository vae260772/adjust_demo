package com.mjb68.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Store {
    private SharedPreferences sp;

    private String existFlag = "1";
    private String defaultFlag = "0";
    private String fistDepositKey = "FistDeposit";
    private Context mContext;

    public Store(Context context) {
        this.sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.mContext = context;
    }

    public void setFistDeposit() {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString(fistDepositKey, existFlag);
        editor.apply();
    }

    public boolean isFistDeposit() {
        String value = this.sp.getString(fistDepositKey, defaultFlag);
        Log.i("isFistDeposit:", value);
        return !existFlag.equals(value);
    }
}
