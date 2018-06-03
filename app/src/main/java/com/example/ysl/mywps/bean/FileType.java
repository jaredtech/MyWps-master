package com.example.ysl.mywps.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ysl on 2018/2/8.
 * 介绍: 文件类型
 */

public class FileType {


    /**
     * id : 29
     * code : img
     * value : 图片
     * group : filetype
     * ctime : 2018-02-06 14:52:25
     */

    private String id;
    private String code;
    private String value;
    private String group;
    private String ctime;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
