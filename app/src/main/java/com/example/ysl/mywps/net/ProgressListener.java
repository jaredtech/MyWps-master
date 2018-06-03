package com.example.ysl.mywps.net;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ysl on 2018/2/8.
 * 介绍:封装一个RetrofitCallback，用于进度的回调。
 */

public interface ProgressListener{
    void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
}

