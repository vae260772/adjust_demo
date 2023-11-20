package com.wuegagil.tkoqtktc21;

import android.app.Application;
import android.content.Context;

public class CompassApp extends Application {


    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
