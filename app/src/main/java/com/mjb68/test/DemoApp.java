package com.mjb68.test;

import android.app.Application;
import android.content.Context;

public class DemoApp extends Application {


    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
