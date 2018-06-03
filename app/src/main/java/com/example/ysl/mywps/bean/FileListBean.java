package com.example.ysl.mywps.bean;

import java.util.ArrayList;

/**
 * Created by ysl on 2018/2/8.
 * 介绍:文件列表
 */

public class FileListBean {

    private String ctime;
    private ArrayList<FileListChildBean> files;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public ArrayList<FileListChildBean> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileListChildBean> files) {
        this.files = files;
    }
}
