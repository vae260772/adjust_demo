package com.mjb68.test;

import static com.mjb68.test.DemoApp.appContext;

import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.OnAttributionChangedListener;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;

public class MyAdjustUtils {

    final String TAG = "MyAdjustUtils";

    private static MyAdjustUtils instance; // 保留单例类的唯一实例

    public static void init(String appToken) {
        instance = new MyAdjustUtils(appToken);
    }

    public static MyAdjustUtils getInstance() {
        return instance; // 返回现有实例或新创建的实例
    }

    private MyAdjustUtils(String appToken) {
        AdjustConfig config = new AdjustConfig(appContext, appToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
        config.setLogLevel(LogLevel.VERBOSE);
        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                Log.i(TAG, "onAttributionChanged," + attribution.toString());
            }
        });

        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
            @Override
            public void onFinishedEventTrackingFailed(AdjustEventFailure adjustEventFailure) {
                Log.i(TAG, "onFinishedEventTrackingFailed," + adjustEventFailure.toString());
            }
        });


        config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
            @Override
            public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
                Log.i(TAG, "onFinishedEventTrackingSucceeded---" + eventSuccessResponseData.toString());
            }
        });

        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
            @Override
            public void onFinishedEventTrackingFailed(AdjustEventFailure adjustEventFailure) {
                Log.i(TAG, "onFinishedEventTrackingFailed---" + adjustEventFailure.toString());
            }
        });

        Adjust.onCreate(config);
        Adjust.onResume();
        Log.i(TAG, "Adjust onResume=" + config);
    }

/***
 public static void getReferer(final Context context, final RCallback callback) {
 if (Looper.getMainLooper() != Looper.myLooper()) {
 new Handler(Looper.getMainLooper()).post(new Runnable() {
@Override public void run() {
getReferer(context, callback);
}
});
 return;
 }
 final AtomicBoolean isCallback = new AtomicBoolean(false);

 try {

 final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(context).build();
 referrerClient.startConnection(new InstallReferrerStateListener() {
@Override public void onInstallReferrerSetupFinished(int responseCode) {
switch (responseCode) {
case InstallReferrerClient.InstallReferrerResponse.OK:
// Connection established.
String installReferrer = handleInstallReferrer(referrerClient);
if (!isCallback.getAndSet(true)) {
callback.installReferer(installReferrer);
}
break;
case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
// API not available on the current Play Store app.
// Log.e(TAG, "FEATURE_NOT_SUPPORTED");
if (!isCallback.getAndSet(true)) {
callback.installReferer("Organic");
}
break;
case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
// Connection couldn't be established.
// Log.e(TAG, "SERVICE_UNAVAILABLE");
if (!isCallback.getAndSet(true)) {
callback.installReferer("Organic");
}
break;
}
}

@Override public void onInstallReferrerServiceDisconnected() {
// Log.d(TAG, "onInstallReferrerServiceDisconnected!");
if (!isCallback.getAndSet(true)) {
callback.installReferer("Organic");
}
}
});
 } catch (Exception e) {
 e.printStackTrace();
 if (!isCallback.getAndSet(true)) {
 callback.installReferer("Organic");
 }
 }

 new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
@Override public void run() {
if (!isCallback.getAndSet(true)) {
callback.installReferer("Organic");
}
}
}, 3000);
 }

 private static String handleInstallReferrer(InstallReferrerClient client) {
 String referrer = null;
 try {
 ReferrerDetails response = client.getInstallReferrer();
 referrer = response.getInstallReferrer();
 if (TextUtils.isEmpty(referrer)) {
 // Log.e(TAG, "referrer is empty!");
 }
 client.endConnection();
 } catch (Exception ex) {
 Log.e("InstallReferrerHelper", ex.toString());
 }
 return referrer;
 }

 public interface RCallback {
 void installReferer(String referer);
 }
 **/
}
