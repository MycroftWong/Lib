package com.mycroft.lib.util;

import com.blankj.utilcode.util.LogUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mycroft on 2016/2/5.
 */
public final class Md5FileNameGenerator {

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 36;

    public Md5FileNameGenerator() {
    }

    public static String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = (new BigInteger(md5)).abs();
        return bi.toString(RADIX);
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;

        try {
            MessageDigest e = MessageDigest.getInstance(HASH_ALGORITHM);
            e.update(data);
            hash = e.digest();
        } catch (NoSuchAlgorithmException var4) {
            LogUtils.e(var4);
        }

        return hash;
    }
}
