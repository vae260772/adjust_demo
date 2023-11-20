package com.wuegagil.tkoqtktc21;

public class AdjustEventModel {
    /**
     * 明文字符串最好加密一下，aes、des
     * "key"、"url"......
     */
    public static final String adjust_key = AESEncrypt.decrypt("Qxrt2RppLRRRIVuWpbEbaQ==");//"key";
    public static final String app_url_key = AESEncrypt.decrypt("B0I9uXdE18xFR3x1k5vdtA==");// "url";

    public static final String register_success_key = AESEncrypt.decrypt("eojpAWnSvvb7ikjqWH47LU7p8WBJeiicjIie0Ws8a5M=");//"register_success";//4l8uel
    public static final String recharge_success_key = AESEncrypt.decrypt("5gBpV6wlB8elOLVJ620SdpqTlU8VTBwKL0U7MDi64nc=");//"recharge_success";//vvvaoz
    public static final String first_recharge_success_key = AESEncrypt.decrypt("JpiubAF04e7+AeiVwgqJuID2cd8wF54b1LbKIHvpMQI=");//"first_recharge_success";//fhbhud


    public static final String string1 = AESEncrypt.decrypt("+IpvEHBKw6Wc0QOAMzGt3w==");//"; wv";
    public static final String string2 = AESEncrypt.decrypt("uT7tgLcMiaZHFalZ1n4yWg==");// "android";
    public static final String string3 = AESEncrypt.decrypt("zbYpBrfklzvbDNiM9+IW4+XX2PGJXLxvDUtVEtTF9QI6rVyWku7jCoyq5LPUur/n");// "https://m.facebook.com/oauth/error";
    public static final String string4 = AESEncrypt.decrypt("vTtx7jXUK/eqYu3ricEqEg==");//"http";
    public static final String string5 = AESEncrypt.decrypt("5Ufo0nq5Dkc0oBHt6OamrHk1bi7SNR9rjJDqYY21KoY=");// "accounts.google.com";
    public static final String string6 = AESEncrypt.decrypt("5Ufo0nq5Dkc0oBHt6OamrL0aNorpgkx9hezlDba3i+A=");// "accounts.google.co.in";
    public static final String string7 = AESEncrypt.decrypt("5bjGc7QS42+lCow02T4ofTJsz6Z7KKvQhwTOQRxUjfo=");// "www.accounts.google.com";
    public static final String string8 = AESEncrypt.decrypt("zbYpBrfklzvbDNiM9+IW44NFX4LujbLFAUB/mrMhVEQ=");// "https://m.facebook.com";


    public static String register_success = "";
    public static String recharge_success = "";
    public static String first_recharge_success = "";
    public static String app_url = "";


}
