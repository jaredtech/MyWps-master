package com.example.ysl.mywps.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.wps.moffice.client.OfficeServiceClient;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MofficeClientService extends Service {

    protected final OfficeServiceClient.Stub mBinder = new OfficeServiceClientImpl();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
