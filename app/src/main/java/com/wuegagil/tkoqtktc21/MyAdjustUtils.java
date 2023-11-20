package com.wuegagil.tkoqtktc21;

import static com.wuegagil.tkoqtktc21.CompassApp.appContext;

import android.content.Context;
import android.os.Handler;
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
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.adjust.sdk.OnSessionTrackingFailedListener;
import com.adjust.sdk.OnSessionTrackingSucceededListener;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

public class MyAdjustUtils {
    private static final String TAG = "MyAdjustUtils";

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
        config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
            @Override
            public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess adjustSessionSuccess) {
                Log.i(TAG, "onFinishedSessionTrackingSucceeded," + adjustSessionSuccess.toString());

            }
        });
        config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
            @Override
            public void onFinishedSessionTrackingFailed(AdjustSessionFailure adjustSessionFailure) {
                Log.i(TAG, "onFinishedSessionTrackingFailed," + adjustSessionFailure.toString());
            }
        });

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

    public static String referrer = "Organic";

    public static void getReferer(Handler handler, final Context context, final RCallback callback) {
        try {
            ///  handler.postDelayed(new Runnable() {
            //   @Override
            ///    public void run() {
            final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(context).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    Log.d(TAG, "onInstallReferrerSetupFinished responseCode===" + responseCode);
                    if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                        try {
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            referrer = response.getInstallReferrer();
                            referrerClient.endConnection();
                            Log.d(TAG, "1 referrer=" + referrer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        callback.installReferer(referrer);
                    } else {
                        Log.d(TAG, "2 referrer=" + referrer);
                        callback.installReferer("Organic");
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    Log.d(TAG, "3 referrer=" + referrer);
                    callback.installReferer("Organic");
                }
            });
            ///    }
            //}, 2000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "4 referrer=" + referrer);
            callback.installReferer("Organic");
        }
    }

    public interface RCallback {
        void installReferer(String referer);
    }

}
