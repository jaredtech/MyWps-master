package com.example.ysl.mywps.client;

import android.os.RemoteException;

import cn.wps.moffice.client.ActionType;
import cn.wps.moffice.client.AllowChangeCallBack;
import cn.wps.moffice.client.OfficeAuthorization;
import cn.wps.moffice.client.OfficeEventListener;
import cn.wps.moffice.client.OfficeInputStream;
import cn.wps.moffice.client.OfficeOutputStream;
import cn.wps.moffice.client.OfficeServiceClient;
import cn.wps.moffice.client.ViewType;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OfficeClientEventListenerImpl extends OfficeEventListener.Stub {

    @Override
    public int onOpenFile(String s, OfficeOutputStream officeOutputStream) throws RemoteException {
        return 0;
    }

    @Override
    public int onSaveFile(OfficeInputStream officeInputStream, String s) throws RemoteException {
        return 0;
    }

    @Override
    public int onCloseFile() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isActionAllowed(String s, ActionType actionType) throws RemoteException {
        return false;
    }

    @Override
    public boolean isValidPackage(String s, String s1) throws RemoteException {
        return false;
    }

    @Override
    public void setAllowChangeCallBack(AllowChangeCallBack allowChangeCallBack) throws RemoteException {

    }

    @Override
    public int invoke(String s, String s1) throws RemoteException {
        return 0;
    }

    @Override
    public boolean isViewForbidden(String s, ViewType viewType) throws RemoteException {
        return false;
    }

    @Override
    public boolean isViewInVisible(String s, ViewType viewType) throws RemoteException {
        return false;
    }

    @Override
    public void onMenuAtion(String s, String s1) throws RemoteException {

    }

    @Override
    public String getMenuText(String s, String s1) throws RemoteException {
        return null;
    }
}
