package com.uvfckjoj.brzvoelz28;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.OnDeviceIdsRead;
import com.alibaba.fastjson.JSON;

import java.util.Map;


public class Web1Activity extends AppCompatActivity {
    String TAG = "Web1Activity";
    private WebView webView;
    public static String brzvoelz28_url;
    public static String brzvoelz28_jsObject;
    public static String brzvoelz28_adjust;
    public static String brzvoelz28_eventToken;
    public static String brzvoelz28_money;

    public static String brzvoelz28_INR = "";//INR

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUserAgentString(settings.getUserAgentString().replaceAll("; wv", ""));
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android  >= 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Web1Activity.this.mUploadCallBackAboveL = filePathCallback;
                openFileChooseProcess();
                return true;
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Log.i(TAG, "71-url===" + url);
                if (url.contains("t.me") || url.contains("whatsapp:")) {
                    Intent intent = new Intent();
                    // 设置意图动作为打开浏览器
                    intent.setAction(Intent.ACTION_VIEW);
                    // 声明一个Uri
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.addJavascriptInterface(this, brzvoelz28_jsObject);
        webView.loadUrl(brzvoelz28_url);
    }

    private final int REQUEST_CODE_FILE_CHOOSER = 1;

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_FILE_CHOOSER);
    }

    private ValueCallback<Uri> mUploadCallBack;
    private ValueCallback<Uri[]> mUploadCallBackAboveL;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // //Log.e(TAG, "onActivityResult requestCode = " + requestCode + "      resultCode = " + resultCode);
        if (requestCode == this.REQUEST_CODE_FILE_CHOOSER) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                if (mUploadCallBackAboveL != null) {
                    mUploadCallBackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    mUploadCallBackAboveL = null;
                    return;
                }
            }
            clearUploadMessage();
        }
    }

    private void clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL.onReceiveValue(null);
            mUploadCallBackAboveL = null;
        }
        if (mUploadCallBack != null) {
            mUploadCallBack.onReceiveValue(null);
            mUploadCallBack = null;
        }
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    //1.注册成功
    @JavascriptInterface
    public void callAndroidMethod(String obj) {
        Log.i(TAG, "1callAndroidMethod:" + obj);
        Map hashMap = JSON.parseObject(obj, Map.class);
        if ((Boolean) hashMap.get(brzvoelz28_adjust)) {
            String eventToken = (String) hashMap.get(brzvoelz28_eventToken);
            Log.i(TAG, "eventToken:" + eventToken);
            //java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Double
            if ((Integer) hashMap.get(brzvoelz28_money) > 0) {
                AdjustEvent adjustEvent = new AdjustEvent(eventToken);
                adjustEvent.setRevenue((Integer) hashMap.get(brzvoelz28_money), brzvoelz28_INR);
                Adjust.trackEvent(adjustEvent);
                Log.i(TAG, "1 adjustEvent:" + adjustEvent);
            } else {
                AdjustEvent adjustEvent = new AdjustEvent(eventToken);
                Adjust.trackEvent(adjustEvent);
                Log.i(TAG, "2 adjustEvent:" + adjustEvent);
            }
        } else {
            Adjust.getGoogleAdId(this, new OnDeviceIdsRead() {
                @Override
                public void onGoogleAdIdRead(String googleAdId) {
                    ///  //Log.d(TAG, "Adjust.getAdid()=" + Adjust.getAdid());
                    ///  //Log.d(TAG, "googleAdId=" + googleAdId);
                    webView.evaluateJavascript("javascript:receiveDataFromAndroid('" +
                            Adjust.getAdid() +
                            "," +
                            googleAdId +
                            "')", null);
                }
            });

        }
    }
}
