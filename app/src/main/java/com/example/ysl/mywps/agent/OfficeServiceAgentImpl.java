package com.example.ysl.mywps.agent;

import android.os.IBinder;
import android.os.RemoteException;

import cn.wps.moffice.agent.OfficeServiceAgent;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class OfficeServiceAgentImpl extends OfficeServiceAgent.Stub {

    private static final String MYCLIENTS = "[" +
            "{name:cn.wps.moffice.client.OfficeServiceClient," +
            "type:Package-ID," +
            "id:com.example.ysl.mywps," +
            "Security-Level:Full-access," +
            "Authorization:YANGSONGLIN}]";


    @Override
    public int getClients(String[] clients, int[] expiredDays) throws RemoteException {

        clients[0] = MYCLIENTS;
        expiredDays[0] = 30;
        return 0;
    }

    @Override
    public boolean isValidPackage(String s, String s1) throws RemoteException {
        return false;
    }
}
