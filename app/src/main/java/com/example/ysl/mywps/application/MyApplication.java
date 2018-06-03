package com.example.ysl.mywps.application;


import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.telecom.Call;
import android.util.Log;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.ui.activity.LoginActivity;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class MyApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {
    private String TAG = "aaa";
    private ImageLoaderConfiguration config;
    private static Context context;
    //24874914
    private String appId = "24879068-1";
    //    f8846531340ff4ef9f2d8f1af309a339
    private String appSecret = "f33b8ff4761c1e335521195981485654";
    private String rsaSecret = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvgzRPfPSkWxquXwbO2qXPIFBNNT8uaLIw//xM4qnVFeYr3mzDeVaoHqDhGX8mWkp0b1GrTYEX+pZw" +
            "KxATKNHId6ZJ5ABdPepFImsMYXZ6JFYo1zViyKsbN+1skld9KoN88nHP82x4Bl2Dcv3MQenHNe1EkPiVkbB0OGcwz8MQtKLMaU7aEawP5bCiDurh6SKMcrJRVhjHNb0AluDcG/UZ1TjJ9AuGDRWLF" +
            "bQMIOa/5msrkr8yFT7KhQI9oCzjDAyjsnru1nE9LF3bPXIRIAhfEaQT9fGwHUb9rxs8vGSnojhXMdyBpTChIWG15d8SDW210r0VbblP0FdkZEJtc673AgMBAAECggEAer0PbPMehn2rp/uOYEQppj" +
            "Kb1ZKCD6tpybiKsR3l1L1OzKjseLq3LjTVHvXcpG6KGOjsPRajS6iTYffLcwqdwkpiQNQwqQQue5B5OhxCZWJZ2o+9pspcVlmDMOUaR87eSxelHd7GoR0acl2NPTVvDkAZIFUdckODGZOEHm98o4" +
            "XyAdr2hZAuIHE38V6vC0v+taKqr+rxk03xrIS9AFI5FR/2+kikSzrE3qqB+qW+SEF5QAXxlQXdQK+B3ITo59sbVl1rszjShbeCc3hvzudCRa/vmQPw3AhZZuvAoQMn8Y+14SR6FTbnzjHM8FimxK" +
            "+rxOmgZ6vAL7m0ZDhnri7dmQKBgQD9jPlusZxQD0CGJb9sTh1m5xuV/q7NmN4Pa94VpHFE/q4CwBd+9qdonaRB80jltLumuBDOAndz7w7txh/zKZqdm4B6uaiglN4D1Tcnvs3eBy/uwQWQ1byVmS" +
            "HYadNeT0TjivYzKENYvFY9h0QH8jxPPdCvIoYUHSTZpbO64Ey5xQKBgQCxNT5Cz3fV6dNUdveRJ9WepZKhZltAk/G149Mk+W9NvUBEpNK+LGO8l9QTN4FkWjwL/ZYCILMwmTLvshuUSvlPsu+aTyC" +
            "cn48BcetZRz0M4mgmXOPHEQ4qs80op+9Nq8od23afkfuI6plsA5GEpiJ6dQGbGUt2l+xV24el5TmdiwKBgE9xOj36nFVhUgA2F5Ihr4isZLJMiAyXFwyR6ZWyXmMVwQPPmA0Jz04yBmM7jN2cESed" +
            "iHZjw3Wps23ApAYWRQGY1VqejT0zJ5Tf21YyrGIislwGUm4c6eedgUNDdRyZjaYVxiolNWS6LfM9TI2I0mipn5cgJA5tSzx+dJ/a429ZAoGAe4g4TO7ABTkpfvon/uKcAio+G5F1D4GSwE9/g4j21" +
            "GSXhsK3vqbHKFNXHcZVxxU4QsuWZQlSoRBK99lbbHcPcUW1zgZXH5gDYuh9PhFxN6glHokmqED1dUM+Q5c+NREKpuG0wexMQtf399pM+QGvi1pBslNhzdx+fiO5XiZ7kZ0CgYEA5gnn/teR/KgKF" +
            "TD/UxHKoG/Aml3830TFxSQYasYHA3CBXJPWF9+bAmFENemhghj89rkBAWf0pkG3JwVt19WjDUikO3AWK2jyXZGhxTMBTHR4lmbraJAJt8TizXqqIiBYLUOfJfUaD4h5DmqJYNOWlZCr+uOu+Zk0m" +
            "clsaTgu5HA=";
// 错误的code码说明,有问题自己查
//    /兼容老版本的code说明
//    int CODE_LOAD_SUCCESS = 1;//加载阶段, 成功
//    int CODE_ERR_INBLACKLIST = 4;//加载阶段, 失败设备不支持
//    int CODE_REQ_NOUPDATE = 6;//查询阶段, 没有发布新补丁
//    int CODE_REQ_NOTNEWEST = 7;//查询阶段, 补丁不是最新的
//    int CODE_DOWNLOAD_SUCCESS = 9;//查询阶段, 补丁下载成功
//    int CODE_DOWNLOAD_BROKEN = 10;//查询阶段, 补丁文件损坏下载失败
//    int CODE_UNZIP_FAIL = 11;//查询阶段, 补丁解密失败
//    int CODE_LOAD_RELAUNCH = 12;//预加载阶段, 需要重启
//    int CODE_REQ_APPIDERR = 15;//查询阶段, appid异常
//    int CODE_REQ_SIGNERR = 16;//查询阶段, 签名异常
//    int CODE_REQ_UNAVAIABLE = 17;//查询阶段, 系统无效
//    int CODE_REQ_SYSTEMERR = 22;//查询阶段, 系统异常
//    int CODE_REQ_CLEARPATCH = 18;//查询阶段, 一键清除补丁
//    int CODE_PATCH_INVAILD = 20;//加载阶段, 补丁格式非法
//    //查询阶段的code说明
//    int CODE_QUERY_UNDEFINED = 31;//未定义异常
//    int CODE_QUERY_CONNECT = 32;//连接异常
//    int CODE_QUERY_STREAM = 33;//流异常
//    int CODE_QUERY_EMPTY = 34;//请求空异常
//    int CODE_QUERY_BROKEN = 35;//请求完整性校验失败异常
//    int CODE_QUERY_PARSE = 36;//请求解析异常
//    int CODE_QUERY_LACK = 37;//请求缺少必要参数异常
//    //预加载阶段的code说明
//    int CODE_PRELOAD_SUCCESS = 100;//预加载成功
//    int CODE_PRELOAD_UNDEFINED = 101;//未定义异常
//    int CODE_PRELOAD_HANDLE_DEX = 102;//dex加载异常
//    int CODE_PRELOAD_NOT_ZIP_FORMAT = 103;//基线dex非zip格式异常
//    int CODE_PRELOAD_REMOVE_BASEDEX = 105;//基线dex处理异常
//    //加载阶段的code说明 分三部分dex加载, resource加载, lib加载
//    //dex加载
//    int CODE_LOAD_UNDEFINED = 71;//未定义异常
//    int CODE_LOAD_AES_DECRYPT = 72;//aes对称解密异常
//    int CODE_LOAD_MFITEM = 73;//补丁SOPHIX.MF文件解析异常
//    int CODE_LOAD_COPY_FILE = 74;//补丁拷贝异常
//    int CODE_LOAD_SIGNATURE = 75;//补丁签名校验异常
//    int CODE_LOAD_SOPHIX_VERSION = 76;//补丁和补丁工具版本不一致异常
//    int CODE_LOAD_NOT_ZIP_FORMAT = 77;//补丁zip解析异常
//    int CODE_LOAD_DELETE_OPT = 80;//删除无效odex文件异常
//    int CODE_LOAD_HANDLE_DEX = 81;//加载dex异常
//    // 反射调用异常
//    int CODE_LOAD_FIND_CLASS = 82;
//    int CODE_LOAD_FIND_CONSTRUCTOR = 83;
//    int CODE_LOAD_FIND_METHOD = 84;
//    int CODE_LOAD_FIND_FIELD = 85;
//    int CODE_LOAD_ILLEGAL_ACCESS = 86;
//    //resource加载
//    public static final int CODE_LOAD_RES_ADDASSERTPATH = 123;//新增资源补丁包异常
//    //lib加载
//    int CODE_LOAD_LIB_UNDEFINED = 131;//未定义异常
//    int CODE_LOAD_LIB_CPUABIS = 132;//获取primaryCpuAbis异常
//    int CODE_LOAD_LIB_JSON = 133;//json格式异常
//    int CODE_LOAD_LIB_LOST = 134;//lib库不完整异常
//    int CODE_LOAD_LIB_UNZIP = 135;//解压异常
//    int CODE_LOAD_LIB_INJECT = 136;//注入异常

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        String appVersion = getResources().getString(R.string.version_name);
        Log.e("aaa","版本号"+appVersion);
        // initialize必须放在attachBaseContext最前面，初始化代码直接写在Application类里面，切勿封装到其他类。
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(appId, appSecret, rsaSecret)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.e("aaa", "补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            Log.e("aaa", "补丁生效，需要重启");
                            SophixManager.getInstance().killProcessSafely();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.e("aaa", "其他错误:  " + code);
                        }
                    }
                }).initialize();

    }

    @Override
    public void onCreate() {
        super.onCreate();


        context = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
        config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .threadPoolSize(1)
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(10)
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        int myALias = 3;


        SophixManager.getInstance().queryAndLoadNewPatch();

        JPushInterface.getAlias(this, myALias);

/**
 * 初始化common库
 * 参数1:上下文，不能为空
 * 参数2:友盟 app key
 * 参数3:友盟 channel
 * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
 * 参数5:Push推送业务的secret
 */
        UMConfigure.init(this, "5abcde498f4a9d29b700004b", "comyslexample", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setLogEnabled(true);

//崩溃捕获并处理
        Thread.setDefaultUncaughtExceptionHandler(this);

//
    }


    public static Context getMyContext() {
        return context;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        String result = getStackTrace(throwable);
        Log.e("aaa", "crash " + result);
        SharedPreferenceUtils.loginSave(this, "token", "");
        stopActivity();
    }

    private void stopActivity() {


        // 跳转到崩溃提示Activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        System.exit(0);// 关闭已奔溃的app进程
    }

    private String getStackTrace(Throwable th) {

        final Writer result = new StringWriter();

        final PrintWriter printWriter = new PrintWriter(result);

        // If the exception was thrown in a background thread inside

        // AsyncTask, then the actual exception can be found with getCause

        Throwable cause = th;

        while (cause != null) {

            cause.printStackTrace(printWriter);

            cause = cause.getCause();

        }

        final String stacktraceAsString = result.toString();

        printWriter.close();

        return stacktraceAsString;

    }
}
