//package com.mjb68.test;
//
//import android.util.Base64;
//import android.util.Log;
//
//import com.google.android.gms.common.util.ArrayUtils;
//
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//public class AESEncrypt {
//    final static String key = "v8lpQPElvXTTHgy9"; // 替换成真实key,必须16位,自己随机设置一个
//
//    private static final String TAG = "AESEncrypt--> ";
//
//    /**
//     * 加密算法
//     */
//    private static final String KEY_ALGORITHM = "AES";
//
//    /**
//     * AES 的 密钥长度，32 字节，范围：16 - 32 字节
//     */
//    public static final int SECRET_KEY_LENGTH = 16;
//
//    /**
//     * 字符编码
//     */
//    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
//
//    /**
//     * 秘钥长度不足 16 个字节时，默认填充位数
//     */
//    private static final byte[] DEFAULT_VALUE = {0};
//    /**
//     * 加解密算法/工作模式/填充方式
//     */
//    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
//
//    /**
//     * AES 加密
//     *
//     * @param data      待加密内容
//     * @param secretKey 加密密码，长度：16 或 32 个字符
//     * @return 返回Base64转码后的加密数据
//     */
//    public static String encrypt(String data, String secretKey) {
//        if (secretKey.length() < SECRET_KEY_LENGTH) {
//            throw new RuntimeException("aes length!=" + SECRET_KEY_LENGTH);
//        }
//        try {
//            // 创建密码器
//            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//            // 初始化为加密密码器
//            IvParameterSpec ivSpec = new IvParameterSpec(secretKey.getBytes(CHARSET_UTF8));
//            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey), ivSpec);
//
//            // cipher.doFinal(pkcs7padding(data, cipher.getBlockSize()));
//            byte[] encryptByte = cipher.doFinal(data.getBytes(CHARSET_UTF8));
//            // byte[] encryptByte = cipher.doFinal(pkcs7padding(data,
//            // cipher.getBlockSize()));
//            // 将加密以后的数据进行 Base64 编码
//            return base64Encode(encryptByte);
//        } catch (Exception e) {
//            handleException(e);
//        }
//        return null;
//    }
//
//    /**
//     * AES 解密
//     *
//     * @param base64Data 加密的密文 Base64 字符串
//     * @param secretKey  解密的密钥，长度：16 或 32 个字符
//     */
//    public static String decrypt(String base64Data, String secretKey) {
//        try {
//            byte[] data = base64Decode(base64Data);
//            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//            // 设置为解密模式
//
//            IvParameterSpec ivSpec = new IvParameterSpec(secretKey.getBytes(CHARSET_UTF8));
//            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey), ivSpec);
//            // 执行解密操作
//            byte[] result = cipher.doFinal(data);
//            return new String(result, CHARSET_UTF8);
//        } catch (Exception e) {
//            handleException(e);
//        }
//        return null;
//    }
//
//    /**
//     * 使用密码获取 AES 秘钥
//     */
//    public static SecretKeySpec getSecretKey(String secretKey) {
//        // secretKey = toMakeKey(secretKey, SECRET_KEY_LENGTH, DEFAULT_VALUE);
//        byte[] key = secretKey.getBytes(CHARSET_UTF8);
//        int length = key.length;
//        if (length < SECRET_KEY_LENGTH) {
//            for (int i = 0; i < SECRET_KEY_LENGTH - length; i++) {
//                key = ArrayUtils.concatByteArrays(key, DEFAULT_VALUE);
//            }
//        }
//        return new SecretKeySpec(key, KEY_ALGORITHM);
//
//        // return new SecretKeySpec(secretKey.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
//    }
//
//    /**
//     * 如果 AES 的密钥小于 {@code length} 的长度，就对秘钥进行补位，保证秘钥安全。
//     *
//     * @param secretKey 密钥 key
//     * @param length    密钥应有的长度
//     * @param text      默认补的文本
//     * @return 密钥
//     */
//    private static String toMakeKey(String secretKey, int length, String text) {
//        // 获取密钥长度
//        int strLen = secretKey.length();
//        // 判断长度是否小于应有的长度
//        if (strLen < length) {
//            // 补全位数
//            StringBuilder builder = new StringBuilder();
//            // 将key添加至builder中
//            builder.append(secretKey);
//            // 遍历添加默认文本
//            for (int i = 0; i < length - strLen; i++) {
//                builder.append(text);
//            }
//            // 赋值
//            secretKey = builder.toString();
//        }
//        return secretKey;
//    }
//
//    private static String toIv(String secretKey) {
//        // 获取密钥长度
//        int strLen = secretKey.length();
//        // 判断长度是否小于应有的长度
//        if (strLen < SECRET_KEY_LENGTH) {
//            // 补全位数
//            StringBuilder builder = new StringBuilder();
//            // 将key添加至builder中
//            builder.append(secretKey);
//            // 遍历添加默认文本
//            for (int i = 0; i < SECRET_KEY_LENGTH - strLen; i++) {
//                builder.append("0");
//            }
//            // 赋值
//            secretKey = builder.toString();
//        } else {
//            secretKey = secretKey.substring(0, SECRET_KEY_LENGTH);
//        }
//        Log.i(TAG, "==================iv:" + secretKey);
//        return secretKey;
//    }
//
//    /**
//     * 将 Base64 字符串 解码成 字节数组
//     */
//    public static byte[] base64Decode(String data) {
//        return Base64.decode(data, Base64.NO_WRAP);
//    }
//
//    /**
//     * 将 字节数组 转换成 Base64 编码
//     */
//    public static String base64Encode(byte[] data) {
//        return Base64.encodeToString(data, Base64.NO_WRAP);
//    }
//
//    /**
//     * 处理异常
//     */
//    private static void handleException(Exception e) {
//        e.printStackTrace();
//        Log.e(TAG, TAG + e);
//    }
//
//    /**
//     * pkcs7填充
//     *
//     * @param content
//     * @param blockSize
//     * @return
//     */
//    private static byte[] pkcs7padding(String content, int blockSize) {
//        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
//        int pad = blockSize - (contentBytes.length % blockSize); // 计算需要补位的长度
//        byte padChr = (byte) pad; // 补位字符 (即用补位长度)
//        byte[] paddedBytes = new byte[contentBytes.length + pad]; // 在原有的长度上加上补位长度
//        System.arraycopy(contentBytes, 0, paddedBytes, 0, contentBytes.length); // 原有的先复制过去
//        for (int i = contentBytes.length; i < paddedBytes.length; i++) { // 补位字符填充
//            paddedBytes[i] = padChr;
//        }
//        return paddedBytes;
//    }
//}
