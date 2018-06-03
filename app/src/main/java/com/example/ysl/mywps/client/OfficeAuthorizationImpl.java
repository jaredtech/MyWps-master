package com.example.ysl.mywps.client;

import android.os.RemoteException;

import cn.wps.moffice.client.OfficeAuthorization;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OfficeAuthorizationImpl extends OfficeAuthorization.Stub {
    @Override
    public int getAuthorization(String[] strings) throws RemoteException {
      strings[0] = "YANGSONGLIN";
        return 0;
    }
}
