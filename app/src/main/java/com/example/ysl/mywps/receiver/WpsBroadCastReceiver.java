package com.example.ysl.mywps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.ysl.mywps.utils.WpsModel;
import com.orhanobut.logger.Logger;

/**
 * Created by ysl on 2018/1/23.
 */

public class WpsBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WpsModel.Reciver.ACTION_BACK://返回键广播
                Logger.i("wps 返回键广播");
                break;
            case WpsModel.Reciver.ACTION_CLOSE://关闭文件时候的广播

                Logger.i("wps 关闭文件");

                break;
            case WpsModel.Reciver.ACTION_HOME://home键广播

                Logger.i("wps home键广播");

                break;
            case WpsModel.Reciver.ACTION_SAVE://保存广播

                Logger.i("wps 保存广播");
                break;
            default:
                break;
        }

    }
}
