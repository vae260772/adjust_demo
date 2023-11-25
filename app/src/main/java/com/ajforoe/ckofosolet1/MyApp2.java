package com.ajforoe.ckofosolet1;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.AdjustSessionFailure;
import com.adjust.sdk.AdjustSessionSuccess;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.OnAttributionChangedListener;
import com.adjust.sdk.OnDeeplinkResponseListener;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.adjust.sdk.OnSessionTrackingFailedListener;
import com.adjust.sdk.OnSessionTrackingSucceededListener;

/**
 * Created by pfms on 17/12/14.
 */
public class MyApp2 extends Application {
    public static String AdjustToken = "";

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure adjust SDK.

        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;

        AdjustConfig config = new AdjustConfig(this, AdjustToken, environment);

        // Change the log level.
        config.setLogLevel(LogLevel.VERBOSE);

        // Set attribution delegate.
        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                Log.d("example", "Attribution callback called!");
                Log.d("example", "Attribution: " + attribution.toString());
            }
        });

        // Set event success tracking delegate.
        config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
            @Override
            public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
                Log.d("example", "Event success callback called!");
                Log.d("example", "Event success data: " + eventSuccessResponseData.toString());
            }
        });

        // Set event failure tracking delegate.
        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
            @Override
            public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
                Log.d("example", "Event failure callback called!");
                Log.d("example", "Event failure data: " + eventFailureResponseData.toString());
            }
        });

        // Set session success tracking delegate.
        config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
            @Override
            public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess sessionSuccessResponseData) {
                Log.d("example", "Session success callback called!");
                Log.d("example", "Session success data: " + sessionSuccessResponseData.toString());
            }
        });

        // Set session failure tracking delegate.
        config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
            @Override
            public void onFinishedSessionTrackingFailed(AdjustSessionFailure sessionFailureResponseData) {
                Log.d("example", "Session failure callback called!");
                Log.d("example", "Session failure data: " + sessionFailureResponseData.toString());
            }
        });

        // Evaluate deferred deep link to be launched.
        config.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
            @Override
            public boolean launchReceivedDeeplink(Uri deeplink) {
                Log.d("example", "Deferred deep link callback called!");
                Log.d("example", "Deep link URL: " + deeplink);

                return true;
            }
        });

        config.setSendInBackground(true);
        Adjust.onCreate(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    // You can use this class if your app is for Android 4.0 or higher
    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Log.d("example", "onActivityResumed callback called!");
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d("example", "onActivityPaused callback called!");
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d("example", "onActivityStopped callback called!");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d("example", "onActivitySaveInstanceState callback called!");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d("example", "onActivityDestroyed callback called!");
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d("example", "onActivityCreated callback called!");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d("example", "onActivityStarted callback called!");
        }
    }
}