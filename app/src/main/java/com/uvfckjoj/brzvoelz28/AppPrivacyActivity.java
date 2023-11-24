package com.uvfckjoj.brzvoelz28;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class AppPrivacyActivity extends AppCompatActivity {
    String TAG = "AppPrivacyActivity";
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebSettings(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        // settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUserAgentString(settings.getUserAgentString().replaceAll("; wv", ""));
    }

    String brzvoelz28_url;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brzvoelz28_url = getIntent().getStringExtra("brzvoelz28_url");
        webView = findViewById(R.id.webview);
        setWebSettings(webView);
        setWebChromeClient();


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Log.i(TAG, "82----url===" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //Log.i(TAG, "92----request===" + request);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
            }
            return false;
        });
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            try {

                //Log.i(TAG, "103----url===" + url);

                startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        webView.addJavascriptInterface(this, "Android");
        webView.addJavascriptInterface(this, "Andsadroiwdww");
        webView.addJavascriptInterface(this, "Anwwdqgqqroid");
        webView.addJavascriptInterface(this, "Anwbdwqqdroid");
        webView.addJavascriptInterface(this, "Anfgdcxzroid");
        webView.addJavascriptInterface(this, "Anasddfdroid");
        webView.addJavascriptInterface(this, "Andrghtroid");

        webView.loadUrl("https://www.mrcreditloan.com/mrcash/index.html#/login");
        // webView.setWebContentsDebuggingEnabled(true); //测试时打开可以模拟发事件
    }

    public static String register_success = "";
    public static String recharge_success = "";
    public static String first_recharge_success = "";


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    AlertDialog builder;

    void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                setWebSettings(newWebView);

                newWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newWebView.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onCloseWindow(WebView window) {
                        super.onCloseWindow(window);
                        window.destroy();
                        if (builder != null) {
                            builder.dismiss();
                            builder = null;
                        }
                    }
                });
                // Enable Cookies
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setAcceptThirdPartyCookies(view, true);
                cookieManager.setAcceptThirdPartyCookies(newWebView, true);

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        String url = request.getUrl().toString().toLowerCase();
                        //Log.d(TAG, "183 url=================" + url);
                        if (url.contains("https://m.facebook.com/oauth/error")) {
                            return true;
                        }

                        if (url.contains("http") && (url.contains("accounts.google.com") || url.contains("accounts.google.co.in")
                                || url.contains("www.accounts.google.com"))) {
                            //google登录直接弹窗webview加载
                            //Log.d(TAG, "194 =================false");
                            return false;
                        } else {
                            //Log.d(TAG, "197 ====================false");
                            if (url.startsWith("https://m.facebook.com")) {//facebook登录
                                return false;
                            } else {
                                //下载apk https://cpf.bet/download
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                                    startActivity(intent);
                                    closeNewWebView();
                                    return true;
                                } catch (Exception e) {
                                    // 防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                                    closeNewWebView();
                                    return true;// 没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                                }
                            }
                        }
                    }

                    private void closeNewWebView() {
                        newWebView.destroy();
                        if (builder != null) {
                            builder.dismiss();
                            builder = null;
                        }
                    }
                });

                if (builder != null) {
                    builder.dismiss();
                    builder = null;
                }

                builder = new AlertDialog.Builder(AppPrivacyActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();


                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        newWebView.destroy();
                    }
                });

                builder.setTitle("");
                builder.setView(newWebView);
                builder.show();
                Window dialogWindow = builder.getWindow();
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialogWindow.getDecorView().setPadding(20, 20, 20, 20);
                dialogWindow.setAttributes(layoutParams);
                newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });
    }

    //1.注册成功
    @JavascriptInterface
    public void callAndroidMethod(String obj) {
        Log.i(TAG, "1callAndroidMethod:" + obj);
        Map hashMap = JSON.parseObject(obj, Map.class);
        if (hashMap.containsKey("adjust") && (Boolean) hashMap.get("adjust")) {
            if (hashMap.containsKey("money") && (Double) hashMap.get("money") > 0) {
                AdjustEvent adjustEvent = new AdjustEvent((String) hashMap.get("eventToken"));
                adjustEvent.setRevenue((Double) hashMap.get("money"), "INR");
                Adjust.trackEvent(adjustEvent);
            } else {
                AdjustEvent adjustEvent = new AdjustEvent((String) hashMap.get("eventToken"));
                Adjust.trackEvent(adjustEvent);
            }
        } else {
            webView.evaluateJavascript("javascript:receiveDataFromAndroid('adid,googleadid')", null);
        }


//        /// 参数如上，当 adjust = true && money = 0 时 需要记录事件
//        AdjustEvent adjustEvent = new AdjustEvent(eventToken);
//        Adjust.trackEvent(adjustEvent);
//
//        ///   adjust = true && money ！= 0 时 需要记录事件
//        AdjustEvent adjustEvent = new AdjustEvent(eventToken);
//        adjustEvent.setRevenue(money, "INR");
//        Adjust.trackEvent(adjustEvent);
//
//        ///  当adjust = false 时，返回adid  googleadid
//        evaluateJavascript("javascript:receiveDataFromAndroid('adid,googleadid')", null);
    }

    @JavascriptInterface
    public void callAndroidMethod(JSONObject obj) {
        Log.i(TAG, "2callAndroidMethod:" + obj);
        AdjustEvent adjustEvent = new AdjustEvent(register_success);
        Adjust.trackEvent(adjustEvent);
        if (BuildConfig.DEBUG) {

        }
    }

    @JavascriptInterface
    public void callAndroidMethod(HashMap obj) {
        Log.i(TAG, "3callAndroidMethod:" + obj);
        AdjustEvent adjustEvent = new AdjustEvent(register_success);
        Adjust.trackEvent(adjustEvent);
        if (BuildConfig.DEBUG) {

        }
    }
}
