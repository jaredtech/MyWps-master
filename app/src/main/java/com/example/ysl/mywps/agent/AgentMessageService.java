package com.example.ysl.mywps.agent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.wps.moffice.agent.OfficeServiceAgent;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class AgentMessageService extends Service {

    private final OfficeServiceAgent.Stub mBinder = new OfficeServiceAgentImpl();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
