package com.example.ysl.mywps.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.coremedia.iso.Hex;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CommonFun {

    /**
     * 只允许输入汉字字母数字的正则
     */
    private static final String NOSPICALCHARREGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";

    //反序列化
    static public Object deserialize(String info) {
        String redStr;
        try {
            redStr = java.net.URLDecoder.decode(info, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            Object ret = objectInputStream.readObject();

            objectInputStream.close();
            byteArrayInputStream.close();

            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static public String getGuidId() {

        return UUID.randomUUID().toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2pxFloat(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void Toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static int getBinaryCount(int n) {
        int c = 0; // 计数器
        for (c = 0; n > 0; n >>= 1) // 循环移位
            c += n & 1; // 如果当前位是1，则计数器加1
        return c;
    }

    //把日期转为字符串
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
//        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return df.format(date);
    }

    public static String ConverToString(Long longDate) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

        return df.format(new Date(longDate));
    }

    public static String MinuteToString(Long longDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);

        return df.format(new Date(longDate));
    }

    public static String ConverToString(String longDate) {

        long date = Long.parseLong(longDate);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

        return df.format(new Date(date));
    }

    public static String ConverToString_(String longDate) {

        long date = Long.parseLong(longDate);

        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日", Locale.SIMPLIFIED_CHINESE);

        return df.format(new Date(date));
    }


    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

        return df.parse(strDate);
    }

    /**
     * 把日期转为字符串，精确到分钟
     */
    public static String ConverToString_PreciseMinute(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);

        return df.format(date);
    }

    /**
     * 把日期转为字符串，精确到分钟
     */
    public static String ConverToString_PreciseMinuteSecond(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);

        return df.format(date);
    }

    /**
     * 把日期转为字符串，精确到秒
     * 2017/4/19 16:13:22
     */
    public static String ConverToString_PreciseSecond(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);

        return df.format(date);
    }

    //把二位数组按照顺序拼接成一维数组
    public static byte[] byteArraysToBytes(byte[][] data) {
        int length = 0;

        for (int send = 0; send < data.length; ++send) {
            length += data[send].length;
        }

        byte[] var6 = new byte[length];
        int k = 0;

        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[i].length; ++j) {
                var6[k++] = data[i][j];
            }
        }

        return var6;
    }


    /**
     * 禁止Editext输入特殊字符
     */

    public static final InputFilter[] NOSPICALCHAR = new InputFilter[]{new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
//                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals(".") && !Character.toString(source.charAt(i)).equals(".")) {
//                    return "";
//                }
                if (!source.toString().matches(NOSPICALCHARREGEX) && !source.equals(".")) {
                    return "";
                }

            }
            return null;
        }
    }
    };

    public static boolean isNotEmpty(String s) {

        if (s == null) {
            return false;
        }
        if (s.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        if (s.trim().length() == 0) {
            return true;
        }

        return false;
    }


    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String getMD5Three(File path) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return Hex.encodeHex(MD5.digest()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
