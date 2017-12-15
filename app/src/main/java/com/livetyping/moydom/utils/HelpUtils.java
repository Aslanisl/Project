package com.livetyping.moydom.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ivan on 28.11.2017.
 */

public class HelpUtils {

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger md5Data = new BigInteger(1, md.digest(input.getBytes()));
            return String.format("%032X", md5Data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateNoOfColumns(Context context, int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / width);
    }
}
