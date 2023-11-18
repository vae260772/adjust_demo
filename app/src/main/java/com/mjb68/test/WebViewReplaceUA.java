package com.mjb68.test;

import android.util.Log;
import android.webkit.WebView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * webview替换UA类
 * 外部调用replaceUA，传入需要修改的webview
 */

class WebViewReplaceUA {

    //可用的可替换的有效UA数组（根据情况自行追加数据）
    private static final String UA_DATA[] = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0 Mobile Safari/537.36"
    };

    //检测是否被系统识别为webview
    public static boolean isWebviewUA(String useragent) {
        String[] rules = {"WebView", "Android.*(wv|\\.0\\.0\\.0)"};
        String regex = "(" + String.join("|", rules) + ")";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(useragent);
        return matcher.find();
    }

    //随机数
    public static int getRandom(int min, int max) {

        Random rand = new Random();
        int randNumber = rand.nextInt(max - min + 1) + min;

        return randNumber;
    }

    //替换UA（先通过方法判断是否被识别成了webview）
    public static void replaceUA(WebView mWebview) {
        String ua = mWebview.getSettings().getUserAgentString();
        boolean isWebviewUA = WebViewReplaceUA.isWebviewUA(ua);
        Log.i("WebViewReplaceUA", "isWebviewUA：" + isWebviewUA);

        if (isWebviewUA) {
            int index = WebViewReplaceUA.getRandom(0, WebViewReplaceUA.UA_DATA.length - 1);
            ua = WebViewReplaceUA.UA_DATA[index];
        }

        ua = ua.replaceAll("; wv", ""); //兼容web google登录
        Log.i("WebViewReplaceUA", "ua：" + ua);

        mWebview.getSettings().setUserAgentString(ua);
    }
}
