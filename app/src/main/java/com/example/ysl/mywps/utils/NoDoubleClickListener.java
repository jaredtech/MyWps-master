package com.example.ysl.mywps.utils;

import android.provider.Settings;
import android.view.View;

/**
 * Created by ysl on 2017/7/26.
 */
public  abstract class NoDoubleClickListener implements View.OnClickListener {

    private long lastClick;


    @Override
    public void onClick(View v) {

        long current = System.currentTimeMillis();

        if (current - lastClick > 200){
            lastClick = current;
         click(v);
        }
    }
    public  abstract void click(View v);
}
