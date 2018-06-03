package com.example.ysl.mywps.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class ToastUtils {

    public static void showShort(Context context, String msg) {


        if(CommonUtil.isEmpty(msg) || context == null){
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {

        if(CommonUtil.isEmpty(msg) || context == null){
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
