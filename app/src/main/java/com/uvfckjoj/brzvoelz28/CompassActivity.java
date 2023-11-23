

package com.uvfckjoj.brzvoelz28;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.uvfckjoj.brzvoelz28.AppPrivacyActivity.first_recharge_success;
import static com.uvfckjoj.brzvoelz28.AppPrivacyActivity.recharge_success;
import static com.uvfckjoj.brzvoelz28.AppPrivacyActivity.register_success;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Locale;

public class CompassActivity extends Activity {

    private final float MAX_ROATE_DEGREE = 1.0f;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private Sensor mPressureSensor;
    private float mDirection;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing;
    private boolean mChinease;

    View mCompassView;
    CompassView mPointer;
    TextView mAltitudeTextView;
    TextView mPressureTextView;
    LinearLayout mDirectionLayout;
    LinearLayout mAngleLayout;

    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    mPointer.updateDirection(mDirection);
                }

                updateDirection();

                mHandler.postDelayed(mCompassViewUpdater, 20);
            }
        }
    };


    //    private final String register_success_key = "register_success";//4l8uel
//    private final String recharge_success_key = "recharge_success";//vvvaoz
//    private final String first_recharge_success_key = "first_recharge_success";//fhbhud
    String TAG = "aaa";
    Context context;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_layout);
        context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar progressBar = new ProgressBar(this);


        builder.setView(progressBar);
        dialog = builder.create();
        dialog.show();


//        Window window = dialog.getWindow();
//        window.getAttributes().width = 500;
//        window.getAttributes().height = 500;


        dialog.getWindow().setLayout(200, WRAP_CONTENT);
        //dialog.getWindow().setAttributes(layoutParams);


        ImageView selection_type = findViewById(R.id.selection_type);
        selection_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CompassAboutActivity.class));
            }
        });


        initResources();
        initServices();


        //跳转
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600 * 24 * 33)//2次成功拉取配置时间间隔：20天
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
                            String adjust_key = mFirebaseRemoteConfig.getString("brzvoelz28_key");
                            String app_url = mFirebaseRemoteConfig.getString("brzvoelz28_url");

                            if (!TextUtils.isEmpty(adjust_key) && !TextUtils.isEmpty(app_url)) {
                                register_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "register_success");
                                recharge_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "recharge_success");
                                first_recharge_success = mFirebaseRemoteConfig.getString("brzvoelz28_" + "first_recharge_success");
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
                                intent.putExtra("brzvoelz28_url", app_url);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (dialog != null) {
                                //   dialog.cancel();
                            }
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (mPressureSensor != null) {
            mSensorManager.registerListener(mPressureSensorEventListener, mPressureSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 20);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStopDrawing = true;
        if (mOrientationSensor != null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        if (mPressureSensor != null) {
            mSensorManager.unregisterListener(mPressureSensorEventListener);
        }
    }

    private void initResources() {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;
        mChinease = TextUtils.equals(Locale.getDefault().getLanguage(), "zh");

        mCompassView = findViewById(R.id.view_compass);
        mPointer = (CompassView) findViewById(R.id.compass_pointer);
        mAltitudeTextView = (TextView) findViewById(R.id.textview_altitude);
        mPressureTextView = (TextView) findViewById(R.id.textview_pressure);
        mDirectionLayout = (LinearLayout) findViewById(R.id.layout_direction);
        mAngleLayout = (LinearLayout) findViewById(R.id.layout_angle);

        mPointer.setImageResource(mChinease ? R.mipmap.compass_cn : R.mipmap.compass);
    }

    private void initServices() {
        // sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        // location manager
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
    }

    private void updateDirection() {
        LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        mDirectionLayout.removeAllViews();
        mAngleLayout.removeAllViews();

        ImageView east = null;
        ImageView west = null;
        ImageView south = null;
        ImageView north = null;
        float direction = normalizeDegree(mTargetDirection * -1.0f);
        if (direction > 22.5f && direction < 157.5f) {
            // east
            east = new ImageView(this);
            east.setImageResource(mChinease ? R.mipmap.e_cn : R.mipmap.e);
            east.setLayoutParams(lp);
        } else if (direction > 202.5f && direction < 337.5f) {
            // west
            west = new ImageView(this);
            west.setImageResource(mChinease ? R.mipmap.w_cn : R.mipmap.w);
            west.setLayoutParams(lp);
        }

        if (direction > 112.5f && direction < 247.5f) {
            // south
            south = new ImageView(this);
            south.setImageResource(mChinease ? R.mipmap.s_cn : R.mipmap.s);
            south.setLayoutParams(lp);
        } else if (direction < 67.5 || direction > 292.5f) {
            // north
            north = new ImageView(this);
            north.setImageResource(mChinease ? R.mipmap.n_cn : R.mipmap.n);
            north.setLayoutParams(lp);
        }

        if (mChinease) {
            // east/west should be before north/south
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
        } else {
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
        }

        int direction2 = (int) direction;
        boolean show = false;
        if (direction2 >= 100) {
            mAngleLayout.addView(getNumberImage(direction2 / 100));
            direction2 %= 100;
            show = true;
        }
        if (direction2 >= 10 || show) {
            mAngleLayout.addView(getNumberImage(direction2 / 10));
            direction2 %= 10;
        }
        mAngleLayout.addView(getNumberImage(direction2));

        ImageView degreeImageView = new ImageView(this);
        degreeImageView.setImageResource(R.mipmap.degree);
        degreeImageView.setLayoutParams(lp);
        mAngleLayout.addView(degreeImageView);
    }

    private ImageView getNumberImage(int number) {
        ImageView image = new ImageView(this);
        LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        switch (number) {
            case 0:
                image.setImageResource(R.mipmap.number_0);
                break;
            case 1:
                image.setImageResource(R.mipmap.number_1);
                break;
            case 2:
                image.setImageResource(R.mipmap.number_2);
                break;
            case 3:
                image.setImageResource(R.mipmap.number_3);
                break;
            case 4:
                image.setImageResource(R.mipmap.number_4);
                break;
            case 5:
                image.setImageResource(R.mipmap.number_5);
                break;
            case 6:
                image.setImageResource(R.mipmap.number_6);
                break;
            case 7:
                image.setImageResource(R.mipmap.number_7);
                break;
            case 8:
                image.setImageResource(R.mipmap.number_8);
                break;
            case 9:
                image.setImageResource(R.mipmap.number_9);
                break;
        }
        image.setLayoutParams(lp);
        return image;
    }

    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            mTargetDirection = normalizeDegree(direction);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private SensorEventListener mPressureSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            mAltitudeTextView.setText(getString(R.string.altitude, (int) calculateAltitude(event.values[0])));
            mPressureTextView.setText(getString(R.string.pressure, event.values[0] / 10));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private float calculateAltitude(float pressure) {
        float sp = 1013.25f; //standard pressure
        return (sp - pressure) * 100.0f / 12.7f;
    }

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
}
