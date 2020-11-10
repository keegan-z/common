package com.keegan.common.util;

import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {


    /**
     * MD5加密
     *
     * @param resource 原始明文
     * @return 加密后的MD5密文
     */
    public static String encodeMd5(String resource) {
        if (resource == null || "".equals(resource)) {
            return "";
        }
        XLog.d(TextUtils.isEmpty(resource) ? "md5加密前数据为null" : "md5加密前[" + resource + "]");
        String result;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(resource.getBytes("UTF-8"));
            result = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            result = String.valueOf(resource);
        }
        XLog.d(TextUtils.isEmpty(result) ? "md5加密后数据为null" : "md5加密后[" + result + "]");
        return result;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}