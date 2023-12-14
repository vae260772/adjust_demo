package com.uvfckjoj.brzvoelz28;

import static com.uvfckjoj.brzvoelz28.AppPrivacyActivity.adjustMap;
import static com.uvfckjoj.brzvoelz28.AppPrivacyActivity.mH5Url;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    Context context;
    String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        //跳转
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600 * 24 * 10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        //5个
        ////////////////////测试找我调试，我会返回3个数据；确保返回正常，用firebase返回参数
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        Log.d(TAG, "onComplete task: " + task.getResult());
                        try {
                            String adjust_key = "9m8mlaxqeyv4";//mFirebaseRemoteConfig.getString("brzvoelz28_key");
                            mH5Url = "https://AAYY.COM";//mFirebaseRemoteConfig.getString("brzvoelz28_url");

                            if (!TextUtils.isEmpty(adjust_key)) {
                                AppPrivacyActivity.jsBridgeName = "ReactNative";
                                adjustMap.put("login", "mnvrag");
                                adjustMap.put("register", "lhkdd0");
                                adjustMap.put("first-deposit-success", "xqzcmu");
                                adjustMap.put("chargeSuccess", "bsckgm");
                                //  register_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "register_success");
                                //  recharge_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "recharge_success");
                                //  first_recharge_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "first_recharge_success");
                                AdjustConfig config = new AdjustConfig(getApplicationContext(), adjust_key, AdjustConfig.ENVIRONMENT_PRODUCTION);
                                Adjust.onCreate(config);
                                Adjust.onResume();
//                                Log.d(TAG, "adjust_key: " + adjust_key);
//                                Log.d(TAG, "app_url: " + app_url);
//                                Log.d(TAG, "register_success: " + register_success);
//                                Log.d(TAG, "recharge_success: " + recharge_success);
//                                Log.d(TAG, "first_recharge_success: " + first_recharge_success);

//                                Toast.makeText(CompassActivity.this,
//                                        "adjust_key: " + adjust_key + ",app_url: " + app_url +
//                                                ",register_success: " + register_success + ",recharge_success: " + recharge_success +
//                                                ".first_recharge_success: " + first_recharge_success, Toast.LENGTH_LONG).show();
                                //先不做归因，直接接口返回有值，就跳转；没值就A面
                                Intent intent = new Intent(context, AppPrivacyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
