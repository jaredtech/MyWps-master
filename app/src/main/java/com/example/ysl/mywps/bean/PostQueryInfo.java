package com.example.ysl.mywps.bean;

import java.util.List;

/**
 * Created by ysl on 2018/1/17.
 */

public class PostQueryInfo {

    private String message;
    private String nu;
    private String ischeck;
    private String com;
    private String status;
    private String condition;
    private String state;
    private List<DataBean> data;
    public static class DataBean {
        private String time;
        private String context;
        private String ftime;
    }


}
