package com.uvfckjoj.brzvoelz28;

import static android.webkit.WebSettings.LOAD_DEFAULT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;

import java.util.HashMap;


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
        settings.setCacheMode(LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUserAgentString(settings.getUserAgentString().replaceAll("; wv", ""));
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        setWebSettings(webView);
        //setWebChromeClient();

        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading url===" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.addJavascriptInterface(this, jsBridgeName);
        webView.loadUrl(mH5Url);
    }

    public static HashMap<String, String> adjustMap = new HashMap();
    public static String jsBridgeName = "ReactNative";
    public static String mH5Url = "https://AAYY.COM";

//
//    public static String register_success = "";
//    public static String recharge_success = "";
//    public static String first_recharge_success = "";


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    //=======================框架1：
    //1.注册成功
    @JavascriptInterface
    public void onEventJs(String eventName) {
        Log.i(TAG, "onEventJs:" + eventName);
        AdjustEvent adjustEvent = new AdjustEvent(adjustMap.get(eventName));
        Adjust.trackEvent(adjustEvent);
        Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();
    }


    //2.充值成功
    @JavascriptInterface
    public void onEventJsRecharge(String eventName) {
        Log.i(TAG, "onEventJsRecharge:" + eventName);
        AdjustEvent adjustEvent = new AdjustEvent(adjustMap.get(eventName));
        Adjust.trackEvent(adjustEvent);
        Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();
    }

    //3.首充成功
    @JavascriptInterface
    public void onEventJsFirstRecharge(String eventName) {
        Log.i(TAG, "onEventJsFirstRecharge:" + eventName);
        AdjustEvent adjustEvent = new AdjustEvent(adjustMap.get(eventName));
        Adjust.trackEvent(adjustEvent);
        Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();
    }

    //==================框架2
    @JavascriptInterface
    public void postMessage(String eventName) {
        Log.i(TAG, "1:postMessage:" + eventName);
        AdjustEvent adjustEvent = new AdjustEvent(adjustMap.get(eventName));
        Adjust.trackEvent(adjustEvent);
        Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();
    }


    //window.ReactNative.postMessage("first-deposit-success", e.detail.amount);
    @JavascriptInterface
    public void postMessage(String eventName, String data) {
        Log.i(TAG, "2:postMessage:" + eventName);
        AdjustEvent adjustEvent = new AdjustEvent(adjustMap.get(eventName));
        // adjust上传金额
        adjustEvent.setRevenue(Double.parseDouble(data), "BRL");
        Adjust.trackEvent(adjustEvent);
        Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();
    }

}
