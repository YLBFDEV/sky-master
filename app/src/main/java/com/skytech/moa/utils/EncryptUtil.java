package com.skytech.moa.utils;

import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    /**
     * 用户名和源密码经HmacSHA256算法加密，然后经Base64编码
     *
     * @param loginName 登录名
     * @param password  前台传递的密码
     * @param secretKey 前台传递的密码
     * @return
     */
    public static String encodePassword(String loginName, String password, String secretKey) {
        return signDataWithSecretKey(encryption(loginName, password), secretKey);
    }

    /**
     * 用户名和源密码经HmacSHA256算法加密，然后经Base64编码
     *
     * @param loginName 登录名
     * @param password  前台传递的密码
     * @return
     */
    private static String encryption(String loginName, String password) {
        String pwdBase64Str = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(password.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secret);
            byte[] digest = mac.doFinal(loginName.getBytes("UTF-8"));
            pwdBase64Str = Base64.encodeToString(digest, Base64.DEFAULT).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwdBase64Str;
    }

    /**
     * 结合密钥再次经HmacSHA256加密，再次经Base64编码
     *
     * @param pwdBase64Str 签名前的数据
     * @param secretKey    密钥
     * @return 签名后的数据（该返回值就是第二次登陆请求要传输的签名后的密码）
     */
    private static String signDataWithSecretKey(String pwdBase64Str, String secretKey) {
        String signData = null;
        if (secretKey != null && pwdBase64Str != null) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), mac.getAlgorithm());
                mac.init(secret);
                byte[] digest = mac.doFinal(pwdBase64Str.getBytes("UTF-8"));
                signData = Base64.encodeToString(digest, Base64.DEFAULT).trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return signData;
    }
}
