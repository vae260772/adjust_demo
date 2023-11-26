package com.mjb68.test.bcode;

import static com.mjb68.test.DemoApp.appContext;

import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;

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

        Adjust.onCreate(config);
        Adjust.onResume();
        Log.i(TAG, "Adjust onResume=" + config);
    }
}
