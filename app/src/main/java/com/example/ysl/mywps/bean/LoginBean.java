package com.example.ysl.mywps.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ysl on 2018/1/17.
 */

public class LoginBean {


    /**
     * code : 0
     * msg : 登录成功
     * data : {"token":"94721ad0fb81a31b4fad59808097f290d521fd2a"}
     */

    private int code;
    private String msg;
    private String data;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    //    public  class DataBean {
//        /**
//         * token : 94721ad0fb81a31b4fad59808097f290d521fd2a
//         */
//
//        private String token;
//
//        public String getToken() {
//            return token;
//        }
//
//        public void setToken(String token) {
//            this.token = token;
//        }
//    }
}
