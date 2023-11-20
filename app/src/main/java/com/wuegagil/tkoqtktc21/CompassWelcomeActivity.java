package com.wuegagil.tkoqtktc21;

import static com.wuegagil.tkoqtktc21.AdjustEventModel.app_url;
import static com.wuegagil.tkoqtktc21.AdjustEventModel.first_recharge_success;
import static com.wuegagil.tkoqtktc21.AdjustEventModel.recharge_success;
import static com.wuegagil.tkoqtktc21.AdjustEventModel.register_success;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class CompassWelcomeActivity extends AppCompatActivity {
    String TAG = "CompassWelcomeActivity";
    String version = "1.0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_wel);

        /**
         * 2023-11-20 23:19:56.903 23384-23384/? D/AESEncrypt-->: key,加密:Qxrt2RppLRRRIVuWpbEbaQ==
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: Qxrt2RppLRRRIVuWpbEbaQ==,解密:key
         *
         *
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: url,加密:B0I9uXdE18xFR3x1k5vdtA==
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: B0I9uXdE18xFR3x1k5vdtA==,解密:url
         *
         *
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: register_success,加密:eojpAWnSvvb7ikjqWH47LU7p8WBJeiicjIie0Ws8a5M=
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: eojpAWnSvvb7ikjqWH47LU7p8WBJeiicjIie0Ws8a5M=,解密:register_success
         *
         *
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: recharge_success,加密:5gBpV6wlB8elOLVJ620SdpqTlU8VTBwKL0U7MDi64nc=
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: 5gBpV6wlB8elOLVJ620SdpqTlU8VTBwKL0U7MDi64nc=,解密:recharge_success
         *
         *
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: first_recharge_success,加密:JpiubAF04e7+AeiVwgqJuID2cd8wF54b1LbKIHvpMQI=
         * 2023-11-20 23:19:56.904 23384-23384/? D/AESEncrypt-->: JpiubAF04e7+AeiVwgqJuID2cd8wF54b1LbKIHvpMQI=,解密:first_recharge_success
         *
         *
         *
         * 2023-11-20 23:19:56.905 23384-23384/? D/AESEncrypt-->: ; wv,加密:+IpvEHBKw6Wc0QOAMzGt3w==
         * 2023-11-20 23:19:56.905 23384-23384/? D/AESEncrypt-->: +IpvEHBKw6Wc0QOAMzGt3w==,解密:; wv
         *
         *
         * 2023-11-20 23:19:56.905 23384-23384/? D/AESEncrypt-->: android,加密:uT7tgLcMiaZHFalZ1n4yWg==
         * 2023-11-20 23:19:56.905 23384-23384/? D/AESEncrypt-->: uT7tgLcMiaZHFalZ1n4yWg==,解密:android
         *
         *
         *
         * 2023-11-20 23:19:56.905 23384-23384/? D/AESEncrypt-->: https://m.facebook.com/oauth/error,
         * 加密:zbYpBrfklzvbDNiM9+IW4+XX2PGJXLxvDUtVEtTF9QI6rVyWku7jCoyq5LPUur/n
         */

//        AESEncrypt.decrypt(AdjustEventModel.adjust_key);
//        AESEncrypt.decrypt(AdjustEventModel.app_url_key);
//
//        AESEncrypt.decrypt(AdjustEventModel.register_success_key);
//        AESEncrypt.decrypt(AdjustEventModel.recharge_success_key);
//
//        AESEncrypt.decrypt(AdjustEventModel.first_recharge_success_key);
//        /////
//        AESEncrypt.decrypt(AdjustEventModel.string1);
//        AESEncrypt.decrypt(AdjustEventModel.string2);
//        AESEncrypt.decrypt(AdjustEventModel.string3);
//        AESEncrypt.decrypt(AdjustEventModel.string4);
//        AESEncrypt.decrypt(AdjustEventModel.string5);
//        AESEncrypt.decrypt(AdjustEventModel.string6);
//        AESEncrypt.decrypt(AdjustEventModel.string7);
//        AESEncrypt.decrypt(AdjustEventModel.string8);
        ////


        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                //.setMinimumFetchIntervalInSeconds(3600 * 24 * 33)//2次成功拉取配置时间间隔：20天
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        //5个
        ////////////////////测试找我调试，我会返回3个数据；确保返回正常，用firebase返回参数
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        //url、key、force2B 自行aes、des加密解密，不要使用明文。防止封号
                        try {

                            String adjust_key = mFirebaseRemoteConfig.getString(AdjustEventModel.adjust_key);
                            app_url = mFirebaseRemoteConfig.getString(AdjustEventModel.app_url_key);

                            if (!TextUtils.isEmpty(adjust_key)) {
                                version = mFirebaseRemoteConfig.getString("version");
                                register_success = mFirebaseRemoteConfig.getString(AdjustEventModel.register_success_key);
                                recharge_success = mFirebaseRemoteConfig.getString(AdjustEventModel.recharge_success_key);
                                first_recharge_success = mFirebaseRemoteConfig.getString(AdjustEventModel.first_recharge_success_key);
                                MyAdjustUtils.init(adjust_key);
                                MyAdjustUtils.getReferer(new Handler(), CompassApp.appContext, new MyAdjustUtils.RCallback() {
                                    @Override
                                    public void installReferer(String referer) {
                                        Log.d(TAG, "referer:" + referer);
                                        if (!TextUtils.isEmpty(app_url) && !TextUtils.equals(version, "1.0")) {
                                            startActivity(new Intent(CompassWelcomeActivity.this, CompassWebActivity.class));
                                        } else if (TextUtils.isEmpty(app_url) || TextUtils.equals(referer, "Organic") || TextUtils.isEmpty(referer)) {
                                            startActivity(new Intent(CompassWelcomeActivity.this, CompassActivity.class));
                                        } else {
                                            startActivity(new Intent(CompassWelcomeActivity.this, CompassWebActivity.class));
                                        }
                                    }
                                });
                            } else {
                                startActivity(new Intent(CompassWelcomeActivity.this, CompassActivity.class));
                            }

                            Log.d(TAG, "version:" + version);
                            Log.d(TAG, "adjust_key:" + adjust_key);
                            Log.d(TAG, "app_url:" + app_url);
                            Log.d(TAG, "register_success:" + register_success);
                            Log.d(TAG, "recharge_success:" + recharge_success);
                            Log.d(TAG, "first_recharge_success:" + first_recharge_success);

                            Toast.makeText(CompassWelcomeActivity.this,
                                    "adjust_key: " + adjust_key + ",app_url: " + app_url +
                                            ",register_success: " + register_success + ",recharge_success: " + recharge_success +
                                            ".first_recharge_success: " + first_recharge_success, Toast.LENGTH_LONG).show();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "e:" + e.getMessage());
                            startActivity(new Intent(CompassWelcomeActivity.this, CompassActivity.class));
                            finish();
                        }
                    }
                });
    }
}
