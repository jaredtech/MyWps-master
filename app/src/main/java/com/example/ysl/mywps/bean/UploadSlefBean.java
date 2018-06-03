package com.example.ysl.mywps.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class UploadSlefBean {
    private String ctime;
    private ArrayList<UploadChildFileBean> files = new ArrayList<>();

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public ArrayList<UploadChildFileBean> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadChildFileBean> files) {
        this.files = files;
    }
}
