//package com.wuegagil.tkoqtktc21.adjust;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.WindowManager;
//import android.webkit.JavascriptInterface;
//import android.webkit.JsResult;
//import android.webkit.SslErrorHandler;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import com.adjust.sdk.AdjustAttribution;
//import com.adjust.sdk.AdjustConfig;
//import com.adjust.sdk.AdjustEvent;
//import com.adjust.sdk.AdjustEventSuccess;
//import com.adjust.sdk.LogLevel;
//import com.adjust.sdk.OnAttributionChangedListener;
//import com.adjust.sdk.OnEventTrackingSucceededListener;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//import com.android.installreferrer.api.InstallReferrerClient;
//import com.android.installreferrer.api.InstallReferrerStateListener;
//import com.android.installreferrer.api.ReferrerDetails;
//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.util.ArrayUtils;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class MainActivity extends AppCompatActivity {
//
//    static class Helper {
//        public static final void setSharedPreferences(Context context, String key, String val) {
//            try {
//                if (context == null)
//                    return;
//                SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//                sp.edit().putString(key, val).apply();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        public static final String getSharedPreferences(Context context, String key) {
//            try {
//                if (context == null)
//                    return null;
//                SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//                return sp.getString(key, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "null";
//        }
//    }
//
//    static class AESEncrypt {
//
//        private static final String TAG = AESEncrypt.class.getSimpleName() + " --> ";
//
//        /**
//         * 加密算法
//         */
//        private static final String KEY_ALGORITHM = "AES";
//
//        /**
//         * AES 的 密钥长度，32 字节，范围：16 - 32 字节
//         */
//        public static final int SECRET_KEY_LENGTH = 16;
//
//        /**
//         * 字符编码
//         */
//        private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
//
//        /**
//         * 秘钥长度不足 16 个字节时，默认填充位数
//         */
//        private static final byte[] DEFAULT_VALUE = { 0 };
//        /**
//         * 加解密算法/工作模式/填充方式
//         */
//        private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
//
//        /**
//         * AES 加密
//         *
//         * @param data      待加密内容
//         * @param secretKey 加密密码，长度：16 或 32 个字符
//         * @return 返回Base64转码后的加密数据
//         */
//        public static String encrypt(String data, String secretKey) {
//            if (secretKey.length() < SECRET_KEY_LENGTH) {
//                throw new RuntimeException("aes length!=" + SECRET_KEY_LENGTH);
//            }
//            try {
//                // 创建密码器
//                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//                // 初始化为加密密码器
//                IvParameterSpec ivSpec = new IvParameterSpec(secretKey.getBytes(CHARSET_UTF8));
//                cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey), ivSpec);
//
//                // cipher.doFinal(pkcs7padding(data, cipher.getBlockSize()));
//                byte[] encryptByte = cipher.doFinal(data.getBytes(CHARSET_UTF8));
//                // byte[] encryptByte = cipher.doFinal(pkcs7padding(data,
//                // cipher.getBlockSize()));
//                // 将加密以后的数据进行 Base64 编码
//                return base64Encode(encryptByte);
//            } catch (Exception e) {
//                handleException(e);
//            }
//            return null;
//        }
//
//        /**
//         * AES 解密
//         *
//         * @param base64Data 加密的密文 Base64 字符串
//         * @param secretKey  解密的密钥，长度：16 或 32 个字符
//         */
//        public static String decrypt(String base64Data, String secretKey) {
//            try {
//                byte[] data = base64Decode(base64Data);
//                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//                // 设置为解密模式
//
//                IvParameterSpec ivSpec = new IvParameterSpec(secretKey.getBytes(CHARSET_UTF8));
//                cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey), ivSpec);
//                // 执行解密操作
//                byte[] result = cipher.doFinal(data);
//                return new String(result, CHARSET_UTF8);
//            } catch (Exception e) {
//                handleException(e);
//            }
//            return null;
//        }
//
//        /**
//         * 使用密码获取 AES 秘钥
//         */
//        public static SecretKeySpec getSecretKey(String secretKey) {
//            // secretKey = toMakeKey(secretKey, SECRET_KEY_LENGTH, DEFAULT_VALUE);
//            byte[] key = secretKey.getBytes(CHARSET_UTF8);
//            int length = key.length;
//            if (length < SECRET_KEY_LENGTH) {
//                for (int i = 0; i < SECRET_KEY_LENGTH - length; i++) {
//                    key = ArrayUtils.concatByteArrays(key, DEFAULT_VALUE);
//                }
//            }
//            return new SecretKeySpec(key, KEY_ALGORITHM);
//
//            // return new SecretKeySpec(secretKey.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
//        }
//
//        /**
//         * 如果 AES 的密钥小于 {@code length} 的长度，就对秘钥进行补位，保证秘钥安全。
//         *
//         * @param secretKey 密钥 key
//         * @param length    密钥应有的长度
//         * @param text      默认补的文本
//         * @return 密钥
//         */
//        private static String toMakeKey(String secretKey, int length, String text) {
//            // 获取密钥长度
//            int strLen = secretKey.length();
//            // 判断长度是否小于应有的长度
//            if (strLen < length) {
//                // 补全位数
//                StringBuilder builder = new StringBuilder();
//                // 将key添加至builder中
//                builder.append(secretKey);
//                // 遍历添加默认文本
//                for (int i = 0; i < length - strLen; i++) {
//                    builder.append(text);
//                }
//                // 赋值
//                secretKey = builder.toString();
//            }
//            return secretKey;
//        }
//
//        private static String toIv(String secretKey) {
//            // 获取密钥长度
//            int strLen = secretKey.length();
//            // 判断长度是否小于应有的长度
//            if (strLen < SECRET_KEY_LENGTH) {
//                // 补全位数
//                StringBuilder builder = new StringBuilder();
//                // 将key添加至builder中
//                builder.append(secretKey);
//                // 遍历添加默认文本
//                for (int i = 0; i < SECRET_KEY_LENGTH - strLen; i++) {
//                    builder.append("0");
//                }
//                // 赋值
//                secretKey = builder.toString();
//            } else {
//                secretKey = secretKey.substring(0, SECRET_KEY_LENGTH);
//            }
//            Log.i(TAG, "==================iv:" + secretKey);
//            return secretKey;
//        }
//
//        /**
//         * 将 Base64 字符串 解码成 字节数组
//         */
//        public static byte[] base64Decode(String data) {
//            return Base64.decode(data, Base64.NO_WRAP);
//        }
//
//        /**
//         * 将 字节数组 转换成 Base64 编码
//         */
//        public static String base64Encode(byte[] data) {
//            return Base64.encodeToString(data, Base64.NO_WRAP);
//        }
//
//        /**
//         * 处理异常
//         */
//        private static void handleException(Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, TAG + e);
//        }
//
//        /**
//         * pkcs7填充
//         *
//         * @param content
//         * @param blockSize
//         * @return
//         */
//        private static byte[] pkcs7padding(String content, int blockSize) {
//            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
//            int pad = blockSize - (contentBytes.length % blockSize); // 计算需要补位的长度
//            byte padChr = (byte) pad; // 补位字符 (即用补位长度)
//            byte[] paddedBytes = new byte[contentBytes.length + pad]; // 在原有的长度上加上补位长度
//            System.arraycopy(contentBytes, 0, paddedBytes, 0, contentBytes.length); // 原有的先复制过去
//            for (int i = contentBytes.length; i < paddedBytes.length; i++) { // 补位字符填充
//                paddedBytes[i] = padChr;
//            }
//            return paddedBytes;
//        }
//    }
//
//    class WebAppInterfaceAndroidJS2 {
//        Context mContext;
//        String TAG = "WebAppInterfaceAndroidJS2";
//
//        Store mStore;
//
//        public void setAdjustEventCfg(JSONObject adjustEventCfg) {
//            this.adjustEventCfg = adjustEventCfg;
//        }
//
//        JSONObject adjustEventCfg;
//
//        /** Instantiate the interface and set the context */
//        WebAppInterfaceAndroidJS2(Context c, Store store, JSONObject adjustEventCfg) {
//            mContext = c;
//            adjustEventCfg = adjustEventCfg;
//            mStore = store;
//        }
//
//        public void trackEvent(String eventName, double value, String currency) {
//            String eventToken = adjustEventCfg.getString(eventName);
//            if (eventToken == null || eventToken.equals("")) {
//                return;
//            }
//            if (currency == null || currency.equals("")) {
//                currency = adjustEventCfg.getString("currency");
//            }
//            JSONObject options = new JSONObject();
//            options.put("callbackId", eventName);
//            // options.put("orderId", this.clickid+ "_" + this.openEventToken);
//
//            JSONObject callbackParameters = new JSONObject();
//            // callbackParameters.put("clickid",this.clickid);
//            callbackParameters.put("eventToken", eventToken);
//            callbackParameters.put("eventName", eventName);
//
//            options.put("callbackParameters", callbackParameters);
//
//            JSONObject partnerParams = new JSONObject();
//            // partnerParams.put("clickid",this.clickid);
//            partnerParams.put("eventName", eventName);
//            options.put("partnerParams", partnerParams);
//
//            if (value > 0) {
//                JSONObject revenue = new JSONObject();
//                revenue.put("revenue", value);
//                revenue.put("currency", currency);
//                options.put("revenue", revenue);
//            }
//            Adjust.getInstance().trackEvent(eventToken, options);
//        }
//
//        /** Show a toast from the web page */
//        @JavascriptInterface
//        public void showToast(String toast) {
//            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
//        }
//
//        @JavascriptInterface
//        public void adjustEvent(String eventName, double revenue) {
//            Log.i(TAG, "eventName:" + eventName + " revenue:" + revenue);
//            this.trackEvent(eventName, revenue, "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "androidJS");
//            report.put("func", "adjustEvent");
//            report.put("eventName", eventName);
//            report.put("revenue", revenue);
//            LogInfo.sendLog(report);
//        }
//
//        @JavascriptInterface
//        public void adjustEvent(String eventName) {
//            Log.i(TAG, "eventName:" + eventName);
//            this.trackEvent(eventName, -1.0d, "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "androidJS");
//            report.put("func", "adjustEvent");
//            report.put("eventName", eventName);
//            LogInfo.sendLog(report);
//        }
//    }
//
//    class WebAppInterfaceAndroidJs {
//        Context mContext;
//        String TAG = "androidJs";
//
//        Store mStore;
//
//        public void setAdjustEventCfg(JSONObject adjustEventCfg) {
//            this.adjustEventCfg = adjustEventCfg;
//        }
//
//        JSONObject adjustEventCfg;
//
//        /** Instantiate the interface and set the context */
//        WebAppInterfaceAndroidJs(Context c, Store store, JSONObject adjustEventCfg) {
//            mContext = c;
//            adjustEventCfg = adjustEventCfg;
//            mStore = store;
//        }
//
//        /*******************
//         * jiliasia：https://www.44jili.com/?pid=pagcor777
//         ***************/
//        public void trackEvent(String eventName, double value, String currency) {
//            String eventToken = adjustEventCfg.getString(eventName);
//            if (eventToken == null || eventToken.equals("")) {
//                return;
//            }
//            if (currency == null || currency.equals("")) {
//                currency = adjustEventCfg.getString("currency");
//            }
//            JSONObject options = new JSONObject();
//            options.put("callbackId", eventName);
//            // options.put("orderId", this.clickid+ "_" + this.openEventToken);
//
//            JSONObject callbackParameters = new JSONObject();
//            // callbackParameters.put("clickid",this.clickid);
//            callbackParameters.put("eventToken", eventToken);
//            callbackParameters.put("eventName", eventName);
//
//            options.put("callbackParameters", callbackParameters);
//
//            JSONObject partnerParams = new JSONObject();
//            // partnerParams.put("clickid",this.clickid);
//            partnerParams.put("eventName", eventName);
//            options.put("partnerParams", partnerParams);
//
//            if (value > 0) {
//                JSONObject revenue = new JSONObject();
//                revenue.put("revenue", value);
//                revenue.put("currency", currency);
//                options.put("revenue", revenue);
//            }
//            Adjust.getInstance().trackEvent(eventToken, options);
//        }
//
//        /** Show a toast from the web page */
//        @JavascriptInterface
//        public void showToast(String toast) {
//            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
//        }
//
//        @JavascriptInterface
//        public void adjustEvent(String eventName, double revenue) {
//            Log.i(TAG, "eventName:" + eventName + " revenue:" + revenue);
//            this.trackEvent(eventName, revenue, "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "androidJs");
//            report.put("func", "adjustEvent");
//            report.put("eventName", eventName);
//            report.put("revenue", revenue);
//            LogInfo.sendLog(report);
//        }
//
//        @JavascriptInterface
//        public void adjustEvent(String eventName, String revenue) {
//            Log.i(TAG, "eventName:" + eventName + " revenue:" + revenue);
//            this.trackEvent(eventName, Double.parseDouble(revenue), "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "androidJs");
//            report.put("func", "adjustEvent");
//            report.put("eventName", eventName);
//            report.put("revenue", revenue);
//            LogInfo.sendLog(report);
//        }
//
//        @JavascriptInterface
//        public void adjustEvent(String eventName) {
//            Log.i(TAG, "eventName:" + eventName);
//            this.trackEvent(eventName, -1.0d, "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "androidJs");
//            report.put("func", "adjustEvent");
//            report.put("eventName", eventName);
//            LogInfo.sendLog(report);
//        }
//
//        @JavascriptInterface
//        public String getgaid() {
//            return Helper.getSharedPreferences(MainActivity.this, "gaid");
//        };
//
//        @JavascriptInterface
//        public String getadid() {
//            return com.adjust.sdk.Adjust.getAdid();
//        };
//
//        /*******************
//         *
//         * ****************/
//    }
//
//    class WebAppInterfaceAndroid {
//        Context mContext;
//        String TAG = "WebAppInterfaceAndroid";
//
//        Store mStore;
//
//        public void setAdjustEventCfg(JSONObject adjustEventCfg) {
//            this.adjustEventCfg = adjustEventCfg;
//        }
//
//        JSONObject adjustEventCfg;
//
//        /** Instantiate the interface and set the context */
//        WebAppInterfaceAndroid(Context c, Store store, JSONObject adjustEventCfg) {
//            mContext = c;
//            adjustEventCfg = adjustEventCfg;
//            mStore = store;
//        }
//
//        public void trackEvent(String eventName, double value, String currency) {
//            String eventToken = adjustEventCfg.getString(eventName);
//            if (eventToken == null || eventToken.equals("")) {
//                return;
//            }
//            if (currency == null || currency.equals("")) {
//                currency = adjustEventCfg.getString("currency");
//            }
//            JSONObject options = new JSONObject();
//            options.put("callbackId", eventName);
//            // options.put("orderId", this.clickid+ "_" + this.openEventToken);
//
//            JSONObject callbackParameters = new JSONObject();
//            // callbackParameters.put("clickid",this.clickid);
//            callbackParameters.put("eventToken", eventToken);
//            callbackParameters.put("eventName", eventName);
//
//            options.put("callbackParameters", callbackParameters);
//
//            JSONObject partnerParams = new JSONObject();
//            // partnerParams.put("clickid",this.clickid);
//            partnerParams.put("eventName", eventName);
//            options.put("partnerParams", partnerParams);
//
//            if (value > 0) {
//                JSONObject revenue = new JSONObject();
//                revenue.put("revenue", value);
//                revenue.put("currency", currency);
//                options.put("revenue", revenue);
//            }
//            Adjust.getInstance().trackEvent(eventToken, options);
//        }
//
//        /** Show a toast from the web page */
//        @JavascriptInterface
//        public void showToast(String toast) {
//            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
//        }
//
//        @JavascriptInterface
//        public void onEvent(String eventName, JSONObject data) {
//            Log.i(TAG, "eventName:" + eventName);
//            this.trackEvent(eventName, -1.0d, "");
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "Android");
//            report.put("func", "onEvent");
//            report.put("eventName", eventName);
//            report.put("data", data);
//            LogInfo.sendLog(report);
//        }
//
//        @JavascriptInterface
//        public void onPurchase(String revenue, String currency, String ext) {
//            Log.i(TAG, "revenue:" + revenue + " currency:" + currency + " ext:" + ext);
//            double value = Double.parseDouble(revenue);
//
//            String eventName = "deposit-success";
//            if (mStore.isFistDeposit()) {
//                eventName = "first-deposit-success";
//                mStore.setFistDeposit();
//            }
//            ;
//            this.trackEvent(eventName, value, currency);
//
//            JSONObject report = new JSONObject();
//            report.put("jsbrige", "Android");
//            report.put("func", "onPurchase");
//            report.put("revenue", revenue);
//            report.put("currency", currency);
//            report.put("ext", ext);
//            LogInfo.sendLog(report);
//        }
//    }
//
//    class Store {
//        private SharedPreferences sp;
//
//        private String existFlag = "1";
//        private String defaultFlag = "0";
//        private String fistDepositKey = "FistDeposit";
//        private Context mContext;
//
//        public Store(Context context) {
//            this.sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//            this.mContext = context;
//        }
//
//        public void setFistDeposit() {
//            SharedPreferences.Editor editor = this.sp.edit();
//            editor.putString(fistDepositKey, existFlag);
//            editor.apply();
//        }
//
//        public boolean isFistDeposit() {
//            String value = this.sp.getString(fistDepositKey, defaultFlag);
//            Log.i("isFistDeposit:", value);
//            return !existFlag.equals(value);
//        }
//    }
//
//    static class LogInfo {
//
//        public static void sendLog(JSONObject data) {
//
//            OkHttpClient client = new OkHttpClient();
//            String url = Config.SERVER + "/api/report";
//            data.put("appid", Config.appid);
//            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//            RequestBody requestBody = RequestBody.create(data.toString(), mediaType);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//
//            Call call = client.newCall(request);
//            ((Call) call).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e("UploadLog", "upload log failed");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d("UploadLog", "upload log success");
//                }
//            });
//        }
//
//    }
//
//    static class Adjust {
//
//        final String TAG = "AdjustLOG";
//
//        private static Adjust instance; // 保留单例类的唯一实例
//
//        public static void init(Context context, String appToken, String env) {
//            instance = new Adjust(context, appToken, env);
//        }
//
//        public static Adjust getInstance() {
//            return instance; // 返回现有实例或新创建的实例
//        }
//
//        private Adjust(Context context, String appToken, String env) {
//            AdjustConfig config = new AdjustConfig(context, appToken, env);
//            config.setLogLevel(LogLevel.VERBOSE);
//            config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
//                @Override
//                public void onAttributionChanged(AdjustAttribution attribution) {
//                    Log.i(TAG, attribution.toString());
//                }
//            });
//
//            config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
//                @Override
//                public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
//                    Log.i(TAG, eventSuccessResponseData.toString());
//                }
//            });
//
//            com.adjust.sdk.Adjust.onCreate(config);
//            com.adjust.sdk.Adjust.onResume();
//
//            Context finalCtx1 = context;
//            new Thread(() -> {
//                try {
//                    AdvertisingIdClient client = new AdvertisingIdClient(finalCtx1);
//                    client.start();
//                    AdvertisingIdClient.Info info = client.getInfo();
//                    if (info == null)
//                        return;
//                    Log.i("initAID", info.getId());
//                    Helper.setSharedPreferences(finalCtx1, "gaid", info.getId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//        }
//
//        public void trackEvent(String eventToken, JSONObject options) {
//            // 调用 Adjust SDK 的 trackEvent 方法，触发相应的事件追踪
//            AdjustEvent adjustEvent = new AdjustEvent(eventToken);
//            Log.i(TAG, eventToken);
//            Log.i(TAG, options.toString());
//
//            if (options.containsKey("callbackId")) {
//                // eventName
//                adjustEvent.callbackId = options.getString("callbackId");
//            }
//
//            if (options.containsKey("orderId")) {
//                // clickid + eventToken
//                adjustEvent.orderId = options.getString("orderId");
//            }
//
//            if (options.containsKey("callbackParameters")) {
//                // clickid eventToken eventName
//                JSONObject callbackParameters = options.getJSONObject("callbackParameters");
//
//                Map<String, String> callbackMap = new HashMap();
//                for (Map.Entry<String, Object> entry : callbackParameters.entrySet()) {
//                    adjustEvent.addCallbackParameter(entry.getKey(), entry.getValue().toString());
//                }
//            }
//
//            if (options.containsKey("partnerParams")) {
//                // clickid eventToken eventName
//                JSONObject partnerParams = options.getJSONObject("partnerParams");
//                Map<String, String> partnerMap = new HashMap();
//                for (Map.Entry<String, Object> entry : partnerParams.entrySet()) {
//                    adjustEvent.addCallbackParameter(entry.getKey(), entry.getValue().toString());
//                }
//            }
//            if (options.containsKey("revenue")) {
//                JSONObject revenue = options.getJSONObject("revenue");
//                adjustEvent.setRevenue(revenue.getFloat("revenue"), revenue.getString("currency"));
//            }
//            com.adjust.sdk.Adjust.trackEvent(adjustEvent);
//        }
//
//        public static void getReferer(final Context context, final RCallback callback) {
//            if (Looper.getMainLooper() != Looper.myLooper()) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        getReferer(context, callback);
//                    }
//                });
//                return;
//            }
//            final AtomicBoolean isCallback = new AtomicBoolean(false);
//
//            try {
//
//                final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(context).build();
//                referrerClient.startConnection(new InstallReferrerStateListener() {
//                    @Override
//                    public void onInstallReferrerSetupFinished(int responseCode) {
//                        switch (responseCode) {
//                            case InstallReferrerClient.InstallReferrerResponse.OK:
//                                // Connection established.
//                                String installReferrer = handleInstallReferrer(referrerClient);
//                                if (!isCallback.getAndSet(true)) {
//                                    callback.installReferer(installReferrer);
//                                }
//                                break;
//                            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
//                                // API not available on the current Play Store app.
//                                // Log.e(TAG, "FEATURE_NOT_SUPPORTED");
//                                if (!isCallback.getAndSet(true)) {
//                                    callback.installReferer("Organic");
//                                }
//                                break;
//                            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
//                                // Connection couldn't be established.
//                                // Log.e(TAG, "SERVICE_UNAVAILABLE");
//                                if (!isCallback.getAndSet(true)) {
//                                    callback.installReferer("Organic");
//                                }
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onInstallReferrerServiceDisconnected() {
//                        // Log.d(TAG, "onInstallReferrerServiceDisconnected!");
//                        if (!isCallback.getAndSet(true)) {
//                            callback.installReferer("Organic");
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                if (!isCallback.getAndSet(true)) {
//                    callback.installReferer("Organic");
//                }
//            }
//
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!isCallback.getAndSet(true)) {
//                        callback.installReferer("Organic");
//                    }
//                }
//            }, 3000);
//        }
//
//        private static String handleInstallReferrer(InstallReferrerClient client) {
//            String referrer = null;
//            try {
//                ReferrerDetails response = client.getInstallReferrer();
//                referrer = response.getInstallReferrer();
//                if (TextUtils.isEmpty(referrer)) {
//                    // Log.e(TAG, "referrer is empty!");
//                }
//                client.endConnection();
//            } catch (Exception ex) {
//                Log.e("InstallReferrerHelper", ex.toString());
//            }
//            return referrer;
//        }
//
//        public interface RCallback {
//            void installReferer(String referer);
//        }
//
//    }
//
//    class Config {
//        final static String SERVER = "https://www.whuan.icu"; // 替换成正式域名
//        final static String appid = "41"; // 替换成正式appid
//        final static String key = "v8lpQPElvXTTHgy9"; // 替换成真实key,必须16位
//        final static String PAGE = "file:///android_asset/7.html"; // 需要替换a 面,目录需要随机化
//    }
//
//    private WebView webView;
//    WebAppInterfaceAndroid webAppInterfaceAndroid;
//    WebAppInterfaceAndroidJs webAppInterfaceAndroidJs;
//
//    WebAppInterfaceAndroidJS2 webAppInterfaceAndroidJS2;
//    private Adjust ad;
//
//    ValueCallback<Uri[]> filePathProcess;
//    ActivityResultLauncher<Intent> chooseFileResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                try {
//                    if (filePathProcess == null)
//                        return;
//                    Uri[] uris = {};
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        if (data != null && data.getData() != null) {
//                            uris = new Uri[] { data.getData() };
//                        }
//                    }
//                    filePathProcess.onReceiveValue(uris);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Store store = new Store(MainActivity.this);
//
//        setContentView(R.layout.activity_main);
//        webView = findViewById(R.id.webview);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setDatabaseEnabled(true);
//        // settings.setAppCacheEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setSupportMultipleWindows(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        MainActivity.this);
//                builder.setMessage(message)
//                        .setPositiveButton("confirm", (arg0, arg1) -> arg0.dismiss()).show();
//                jsResult.cancel();
//                return true;
//            }
//
//            @Override
//            public void onCloseWindow(WebView window) {
//                super.onCloseWindow(window);
//                finish();
//            }
//
//            @Override
//            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//
//                WebView tempWebView = new WebView(MainActivity.this);
//                tempWebView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                        return super.shouldOverrideUrlLoading(view, request);
//                    }
//
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView w, String s) {
//                        webView.loadUrl(s);
//                        return super.shouldOverrideUrlLoading(w, s);
//                    }
//
//                    // @Override
//                    // public void onReceivedSslError(WebView webView, SslErrorHandler
//                    // sslErrorHandler,
//                    // SslError sslError) {
//                    // if (sslErrorHandler != null) {
//                    // sslErrorHandler.proceed();
//                    // }
//                    // } //避免gp 审核报错
//                });
//                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                transport.setWebView(tempWebView);
//                resultMsg.sendToTarget();
//                return true;
//            }
//
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
//                    FileChooserParams fileChooserParams) {
//                MainActivity.this.filePathProcess = filePathCallback;
//                try {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    // intent.setType("image/*");
//                    intent.setType("*/*");
//                    chooseFileResultLauncher.launch(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//
//        });
//
//        webView.setOnKeyListener((view, keyCode, event) -> {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                    webView.goBack();
//                    return true;
//                }
//            }
//            return false;
//        });
//        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
//            try {
//                startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        });
//
//        webAppInterfaceAndroid = new WebAppInterfaceAndroid(this, store, null);
//        webView.addJavascriptInterface(webAppInterfaceAndroid, "Android");
//
//        webAppInterfaceAndroidJs = new WebAppInterfaceAndroidJs(this, store, null);
//        webView.addJavascriptInterface(webAppInterfaceAndroidJs, "androidJs");
//
//        webAppInterfaceAndroidJS2 = new WebAppInterfaceAndroidJS2(this, store, null);
//        webView.addJavascriptInterface(webAppInterfaceAndroidJS2, "androidJS");
//
//        // webView.setWebContentsDebuggingEnabled(true); //测试时打开可以模拟发事件
//        checkStrategyStatus();
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    // 获取策略状态
//    private void checkStrategyStatus() {
//        OkHttpClient client = new OkHttpClient();
//        MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
//        JSONObject json = new JSONObject();
//
//        JSONObject device = new JSONObject();
//        device.put("serial", Build.SERIAL);
//        device.put("deviceModel", Build.MODEL);
//        device.put("id", Build.ID);
//        device.put("manufacturer", Build.MANUFACTURER);
//        device.put("deviceBrand", Build.BRAND);
//        device.put("type", Build.TYPE);
//        device.put("user", Build.USER);
//        device.put("base", Build.VERSION_CODES.BASE);
//        device.put("incremental", Build.VERSION.INCREMENTAL);
//        device.put("board", Build.BOARD);
//        device.put("host", Build.HOST);
//        device.put("fingerprint", Build.FINGERPRINT);
//        device.put("versioncode", Build.VERSION.RELEASE);
//        json.put("device", device);
//
//        Log.i("info", json.toString());
//
//        Context context = this;
//
//        // 获取包管理器
//        PackageManager packageManager = context.getPackageManager();
//
//        // 获取应用程序包名
//        String packageName = context.getPackageName();
//
//        try {
//            // 获取 ApplicationInfo
//            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
//
//            // 获取应用程序名称
//            String appName = packageManager.getApplicationLabel(applicationInfo).toString();
//
//            // 获取应用程序版本号
//            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
//            String versionName = packageInfo.versionName;
//
//            // 打印应用程序信息
//            Log.d("App Info", "App Name: " + appName);
//            Log.d("App Info", "Package Name: " + packageName);
//            Log.d("App Info", "Version Name: " + versionName);
//
//            device.put("appName", appName);
//            device.put("packageName", packageName);
//            device.put("appVersion", versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // 获取应用程序上下文
//
//        // 获取设备分辨率
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
//                .getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//        device.put("screenWidth", width);
//        device.put("screenHeight", height);
//
//        // 打印设备分辨率
//        Log.d("Device Info", "Screen Resolution: " + width + "x" + height);
//
//        // 获取设备ID
//        String deviceId = Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//
//        device.put("deviceId", deviceId);
//
//        device.put("platform", "android");
//
//        String ua = webView.getSettings().getUserAgentString();
//        device.put("ua", ua);
//
//        // 打印设备ID
//        Log.d("Device Info", "Device ID: " + deviceId);
//
//        String signkey = AESEncrypt.encrypt(Config.key, Config.key);
//        Log.i("=======", "==========================signkey:" + signkey);
//
//        String enc = AESEncrypt.encrypt(json.toString(), signkey.substring(0, 16));
//
//        RequestBody requestBody = RequestBody.create(enc, MediaTypeJSON);
//        Request request = new Request.Builder()
//                .url(Config.SERVER + "/api/app/v2")
//                .post(requestBody)
//                .addHeader("appid", Config.appid)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                // 网络请求失败，显示错误页面或者提示用户重新尝试
//                e.printStackTrace();
//                webView.loadUrl(Config.PAGE);
//            }
//
//            public boolean stringIsValidJson(String jsonString) {
//                if (jsonString == null || jsonString.trim().isEmpty()) {
//                    return false;
//                }
//                try {
//                    JSON.parse(jsonString);
//                    return true;
//                } catch (JSONException e) {
//                    return false;
//                }
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response)
//                    throws IOException, NullPointerException {
//                String result = Objects.requireNonNull(response.body()).string();
//                result = AESEncrypt.decrypt(result, signkey.substring(0, 16));
//                Log.i("onResponse ", result);
//                if (stringIsValidJson(result)) {
//                    JSONObject jsonObject = JSON.parseObject(result);
//                    int code = jsonObject.getInteger("code");
//                    if (code == 0) {
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        String adjust_key = data.getString("adjust_key");
//                        String environment = data.getString("adjust_env");
//                        JSONObject adjust_event_cfg = data.getJSONObject("adjust_event_cfg");
//                        Adjust.init(MainActivity.this, adjust_key, environment);
//                        webAppInterfaceAndroid.setAdjustEventCfg(adjust_event_cfg);
//                        webAppInterfaceAndroidJS2.setAdjustEventCfg(adjust_event_cfg);
//                        webAppInterfaceAndroidJs.setAdjustEventCfg(adjust_event_cfg);
//                        boolean seen = data.getBoolean("seen");
//                        String jump_url = data.getString("jump_url");
//                        // seen = true; //测试打开
//                        if (data != null && seen) {
//                            String effective_traffic = data.getString("effective_traffic");
//                            if (effective_traffic.equals("organic")) {
//                                // 需要判断 adjust 归因 是否是不是自然流量，如果是需要过滤掉
//                                Adjust.getReferer(MainActivity.this, new Adjust.RCallback() {
//                                    @Override
//                                    public void installReferer(String referer) {
//                                        if (referer == null || referer.equals("") || referer.contains("Organic")) {
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    webView.loadUrl(Config.PAGE);
//                                                    // 不上报，避免发现协议规律
//
//                                                    // JSONObject report = new JSONObject();
//                                                    // report.put("func","installReferer");
//                                                    // report.put("referer",referer);
//                                                    // LogInfo.sendLog(report);
//                                                }
//                                            });
//                                        } else {
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    webView.loadUrl(jump_url);
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        webView.loadUrl(jump_url);
//                                    }
//                                });
//                            }
//                        } else {
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    webView.loadUrl(Config.PAGE);
//                                }
//                            });
//                        }
//                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                webView.loadUrl(Config.PAGE);
//                            }
//                        });
//                    }
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            webView.loadUrl(Config.PAGE);
//                        }
//                    });
//                }
//            }
//        });
//    }
//}
