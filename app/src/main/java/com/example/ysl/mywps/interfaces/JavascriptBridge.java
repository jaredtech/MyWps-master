package com.example.ysl.mywps.interfaces;

import android.webkit.JavascriptInterface;

/**
 * Created by ysl on 2018/2/28.
 * 介绍:
 */

public class JavascriptBridge extends Object {

    private JSCallBack jsCallBack;

    public JavascriptBridge(JSCallBack jsCallBack) {

        this.jsCallBack = jsCallBack;
    }

    /**
     * 调用相机拍照
     * **/
    @JavascriptInterface
    public void callCamera() {

        jsCallBack.jsCallBack("callCamera", "callCamera");
    }
    /**
     * 选择文件
     * **/
    @JavascriptInterface
    public void callDocument(){

        jsCallBack.jsCallBack("callDocument","callDocument");

    }

    /**
     * 选择提交
     * **/

    public void callCommit(){

        jsCallBack.jsCallBack("callCommit","callDocument");

    }

    /**
     * 获取token 和name
     * */
    @JavascriptInterface

    public String callToken(){

       return jsCallBack.jsCallBack("callToken","callToken");

    }

//    /**
//     * 传递token和name
//     * */
//
//    public String callToken(){
//
//    }

}
