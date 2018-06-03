package com.example.ysl.mywps.interfaces;

import com.example.ysl.mywps.bean.FileListChildBean;

import java.util.ArrayList;

/**
 * Created by ysl on 2018/2/10.
 * 介绍:
 */

public interface PassFileChildList {
//kind 0下载 1上传
    void passFileChild(ArrayList<FileListChildBean> files,int kind);
}
