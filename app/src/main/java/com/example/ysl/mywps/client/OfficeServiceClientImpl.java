package com.example.ysl.mywps.client;

import android.os.RemoteException;

import cn.wps.moffice.client.OfficeAuthorization;
import cn.wps.moffice.client.OfficeEventListener;
import cn.wps.moffice.client.OfficeServiceClient;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OfficeServiceClientImpl extends OfficeServiceClient.Stub {
    @Override
    public OfficeAuthorization getAuthorization() throws RemoteException {
        return null;
    }

    @Override
    public OfficeEventListener getOfficeEventListener() throws RemoteException {
        return null;
    }
}
