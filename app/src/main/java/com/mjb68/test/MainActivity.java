//package com.mjb68.test;
//
//import static com.mjb68.test.AdjustEventModel.app_url;
//import static com.mjb68.test.AdjustEventModel.first_recharge_success;
//import static com.mjb68.test.AdjustEventModel.recharge_success;
//import static com.mjb68.test.AdjustEventModel.register_success;
//import static com.mjb68.test.WebViewReplaceUA.replaceUA;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Message;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.webkit.JsResult;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.alibaba.fastjson.JSONObject;
//
//import java.net.URISyntaxException;
//
//
//public class MainActivity extends AppCompatActivity {
//    private String TAG = "MainActivity";
//    private WebView webView;
//    WebAppInterfaceAndroidJs webAppInterfaceAndroidJs;
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
//                            uris = new Uri[]{data.getData()};
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
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d(TAG, "85-shouldOverrideUrlLoading url=" + url);
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//
//        String ua = webView.getSettings().getUserAgentString();
//        Log.d(TAG, "92-ua=" + ua);
//      ///  replaceUA(webView);
//
//
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
//                Log.d(TAG, "110 onCreateWindow resultMsg=" + resultMsg);
//                // WebView支持多窗口
//                WebView tempWebView = new WebView(MainActivity.this);
//                // 修改ua
//                String ua = tempWebView.getSettings().getUserAgentString();
//                Log.d(TAG, "121-ua=" + ua);
//
//                //   boolean isWebviewUA = isWebviewUA(ua);
////                if (isWebviewUA) {
////                    String[] uas = mContext.getResources().getStringArray(R.array.userAgent);
////                    if (uas != null && uas.length > 0) {
////                        int index = CommonUtil.getRandom(0, uas.length - 1);
////                        ua = uas[index];
////                    }
////                }
//                String webview_ua = ua.replace("; wv", ""); //兼容web google登录
//                //  tempWebView.getSettings().setUserAgentString(webview_ua);
//
//                replaceUA(tempWebView);
//                tempWebView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView w, String s) {
//                       // tempWebView.loadUrl(s);
//                        Log.d(TAG, "130 shouldOverrideUrlLoading s=" + s);
//
//                        return false;
//                    }
//                });
//                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                transport.setWebView(tempWebView);
//                resultMsg.sendToTarget();
//                return true;
//            }
//
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
//                                             FileChooserParams fileChooserParams) {
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
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(AdjustEventModel.register_success_key, register_success);
//        jsonObject.put(AdjustEventModel.recharge_success_key, recharge_success);
//        jsonObject.put(AdjustEventModel.first_recharge_success_key, first_recharge_success);
//
//
//        webAppInterfaceAndroidJs = new WebAppInterfaceAndroidJs(this, jsonObject);
//        webView.addJavascriptInterface(webAppInterfaceAndroidJs, "android");
//        // webView.setWebContentsDebuggingEnabled(true); //测试时打开可以模拟发事件
//        webView.loadUrl(app_url);
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
//}
