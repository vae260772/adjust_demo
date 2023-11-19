package com.mjb68.test.bcode;

import static com.mjb68.test.bcode.AdjustEventModel.app_url;
import static com.mjb68.test.bcode.AdjustEventModel.first_recharge_success;
import static com.mjb68.test.bcode.AdjustEventModel.recharge_success;
import static com.mjb68.test.bcode.AdjustEventModel.register_success;

import android.content.Intent;
import android.os.Bundle;
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
import com.mjb68.test.A_Activity;
import com.mjb68.test.R;

public class WelcomeActivity extends AppCompatActivity {
    String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_wel);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600 * 24 * 20)//2次成功拉取配置时间间隔：20天
                //.setMinimumFetchIntervalInSeconds(0)
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
                                register_success = mFirebaseRemoteConfig.getString(AdjustEventModel.register_success_key);
                                recharge_success = mFirebaseRemoteConfig.getString(AdjustEventModel.recharge_success_key);
                                first_recharge_success = mFirebaseRemoteConfig.getString(AdjustEventModel.first_recharge_success_key);
                                MyAdjustUtils.init(adjust_key);
                            }

                            Log.d(TAG, "adjust_key: " + adjust_key);
                            Log.d(TAG, "app_url: " + app_url);
                            Log.d(TAG, "register_success: " + register_success);
                            Log.d(TAG, "recharge_success: " + recharge_success);
                            Log.d(TAG, "first_recharge_success: " + first_recharge_success);

                            Toast.makeText(WelcomeActivity.this,
                                    "adjust_key: " + adjust_key + ",app_url: " + app_url +
                                            ",register_success: " + register_success + ",recharge_success: " + recharge_success +
                                            ".first_recharge_success: " + first_recharge_success, Toast.LENGTH_LONG).show();


                            //先不做归因，直接接口返回有值，就跳转
                            if (TextUtils.isEmpty(app_url)) {
                                startActivity(new Intent(WelcomeActivity.this, A_Activity.class));
                            } else {
                                startActivity(new Intent(WelcomeActivity.this, B_WebActivity.class));
                            }

                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "e:" + e.getMessage());
                            startActivity(new Intent(WelcomeActivity.this, A_Activity.class));
                            finish();
                        }
                    }
                });
    }
}
