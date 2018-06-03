package com.example.ysl.mywps.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ysl on 2017/7/13.
 */
public class CommonUtil {

    public static String myPath;

    public static void showShort(Context context, String msg) {


        if (isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {

        if (isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isNotEmpty(String msg) {

        if (msg == null)
            return false;
        else {
            if (msg.trim().isEmpty())
                return false;
            else
                return true;
        }
    }

    public static boolean isEmpty(String msg) {

        if (msg == null)
            return true;
        else {

            if (msg.trim().isEmpty())
                return true;
            else
                return false;
        }
    }

    public static String y_m_d(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);

        return dateFormat.format(date);
    }

    public static int[] getScreenWH(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        int[] wh = new int[2];

        wh[0] = display.getWidth();
        wh[1] = display.getHeight();
        return wh;
    }


    public static String getAndroidId(Context context) {

        String serialnum = null;
        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class, String.class);

            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));

        } catch (Exception ignored) {

            Logger.i(" " + ignored.toString());
        }
        serialnum = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return serialnum;
    }

    /**
     * 返回文件大小
     */
    public static String getFileSize(long length) {
        DecimalFormat df = new DecimalFormat("######0.00");

        double kb = length / 1024;
        double mb = 0;
        double gb = 0;
        if (kb > 1000) {
            mb = kb / 1024;
        }
        if (mb > 1000) {
            gb = mb / 1024;
        }

        if (gb > 0) {
            df.format(gb);

            return gb + " G";
        }else if(mb > 0){
            df.format(mb);
            return mb+" M";
        }else if(kb > 0){
            df.format(kb);
            return kb+" kb";
        }else {
            return length+" b";
        }
    }

    public static boolean isVideo(String name){
        if(isEmpty(name)){
            return false;
        }
        String videoType = "3gp,asf,avi,m4u,m4v,mov,mp4,mpe,mpeg,mpg,mpg4";

        String type = name.substring(name.length()-3,name.length());
        Logger.i("type   "+type);
        type = type.toLowerCase();

        if(videoType.contains(type)) return true;
        else  return false;
    }


    public static String subsSize(String size){
        if(CommonUtil.isEmpty(size)) return size;
        int startIndex = size.indexOf(".");
        int lastIndex = size.length();
        if(lastIndex - startIndex > 5){
            size = size.substring(0,startIndex+2)+size.substring(size.length()-2,size.length());
        }
        return size;
    }



}