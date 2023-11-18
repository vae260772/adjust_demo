package com.mjb68.test.bcode;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.mjb68.test.DemoApp;

import java.util.HashMap;

public class WebAppInterfaceAndroidJs {
    String TAG = "androidJs";


    HashMap<String, String> mMap;

    /**
     * Instantiate the interface and set the context
     */
    Handler mHandler;

    WebAppInterfaceAndroidJs(Handler handler, HashMap<String, String> map) {
        mHandler = handler;
        mMap = map;
    }

    /*******************
     * jiliasia：https://www.44jili.com/?pid=pagcor777
     ***************/
    public void trackEvent(String eventName) {
        String eventToken = mMap.get(eventName);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DemoApp.appContext, "上报埋点：" + eventName + ",eventToken=" + eventToken, Toast.LENGTH_LONG).show();
            }
        });

        AdjustEvent adjustEvent = new AdjustEvent(eventToken);
        Adjust.trackEvent(adjustEvent);
    }

    //1.注册成功
    @JavascriptInterface
    public void onEventJs(String eventName) {
        Log.i(TAG, "eventName:" + eventName);
        this.trackEvent(eventName);
    }


    //2.充值成功
    @JavascriptInterface
    public void onEventJsRecharge(String eventName) {
        Log.i(TAG, "eventName:" + eventName);
        this.trackEvent(eventName);
    }

    //3.首充成功
    @JavascriptInterface
    public void onEventJsFirstRecharge(String eventName) {
        Log.i(TAG, "eventName:" + eventName);
        this.trackEvent(eventName);

    }
}
