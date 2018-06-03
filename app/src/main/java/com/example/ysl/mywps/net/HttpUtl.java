package com.example.ysl.mywps.net;


import android.os.Handler;
import android.util.Log;

import com.example.ysl.mywps.application.MyApplication;
import com.example.ysl.mywps.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ysl on 2018/1/17.
 */

public class HttpUtl {
    private final static String HTTP_URL = "http://oa.qupeiyi.cn/index.php/";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(1,TimeUnit.DAYS)
            .writeTimeout(1,TimeUnit.DAYS)
            .build();


    private static Retrofit getRetrofit(String httpurl) {


        Retrofit retrofit = new Retrofit.Builder()

                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(httpurl)
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit geRomRetrofit(String url){
        Retrofit retrofit = new Retrofit.Builder()

                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(url)
                .client(client)
                .build();
        return retrofit;
    }

    public static Call<String> login(String url, String name, String passowrd, String idetitiy) {

        String httpUrl = HTTP_URL + url;
        NetApi login = getRetrofit(httpUrl).create(NetApi.class);
        return login.login(name, passowrd, idetitiy);

    }

    public static Call<String> documentList(String url, String token, String page, String pageLimit, String type) {
        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.doucmentList(token, page, pageLimit, type);
    }

    public static Call<ResponseBody> donwoldWps(String headUrl, String end) {
        NetApi api = getRetrofit(headUrl).create(NetApi.class);
        return api.downloadWps(end);
    }

    public static Call<String> contact(String url, String token) {
        String httpUrl = HTTP_URL + url;
        NetApi api = getRetrofit(httpUrl).create(NetApi.class);
        return api.contact(token);
    }

    public static Call<String> commitAudit(String url, String docId, String toUid, String token, String docName, String path) {

        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("name", docName, requestFile);
        return netApi.commitAudit(docId, toUid, token, body);
    }

    /**
     * 文档发挥拟稿人
     */
    public static Call<String> uploadWps(String url, String docId, String proceId, String token, String opinion, String docName, String path) {

        File file = new File(path);
        String httpUrl = HTTP_URL + url;

        if (!file.exists()) {
            ToastUtils.showShort(MyApplication.getMyContext(), "文件不存在");
            Logger.i("filenotexists");
        }
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("name", docName, requestFile);

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);

        return netApi.uploadWps(docId, proceId, token, opinion, body);
    }

    /**
     * 提交文件领导签署
     */
    public static Call<String> commitSign(String url, String docId, String token, String docName, String path, String leaderId) {
        File file = new File(path);

        if (!file.exists()) {
            ToastUtils.showShort(MyApplication.getMyContext(), "文件不存在");
            Logger.i("filenotexists");
        }
        String httpUrl = HTTP_URL + url;
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("name", docName, requestFile);

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.commitSign(docId, token, body, leaderId);
    }

    /**
     * 签署完成返回公文给拟稿人
     */
    public static Call<String> signedCommit(String url, String proceId, String docId, String opinion, String signed, String docName, String path, String token) {

        File file = new File(path);

        if (!file.exists()) {
            ToastUtils.showShort(MyApplication.getMyContext(), "文件不存在");
            Logger.i("filenotexists");
        }
        String httpUrl = HTTP_URL + url;
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("name", docName, requestFile);

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.signedCommit(proceId, docId, opinion, signed, token, body);
    }

    /**
     * 获取公文流程
     */
    public static Call<String> getFlow(String url, String docId, String token) {
        String httpUrl = HTTP_URL + url;

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.getFlow(docId, token);

    }

    /**
     * 流程
     */
    public static Call<String> docForward(String url, String docId, String uids, String token) {

        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.doc_forward(docId, uids, token);
    }

    /***
     * 流程反馈
     */
    public static Call<String> feedBack(String url, String docId, String opinion, String token) {

        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.feed_back(docId, opinion, token);
    }

    /**
     * 文件共享上传文件
     */
    public static Call<String> sharedUpload(String url, String fileType, String token, String fileName, String filePath,ProgressListener listener) {

        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);

        File file = new File(filePath);

        if (!file.exists()) {
            ToastUtils.showShort(MyApplication.getMyContext(), "文件不存在");
            Logger.i("filenotexists");
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("name", fileName, requestFile);
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(requestFile,listener);
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("file\"; filename=\"" + fileName, fileRequestBody);
        return netApi.sharedUpload(fileType, token, requestBodyMap);

    }

    /**
     * 文件类目
     */
    public static Call<String> getFileType(String url, String token) {

        String httpUrl = HTTP_URL + url;

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.getFileType(token);
    }

    /**
     * 文件列表
     */
    public static Call<String> fileList(String url, String fileType, String page, String pagelimit, String token) {

        String httpUrl = HTTP_URL + url;

        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);

        return netApi.fileList(token, fileType, page, pagelimit);
    }

    /***
     * 删除文件
     */
    public static Call<String> deleteFile(String url, String id, String token) {

        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);
        return netApi.deleteDocument(id, token);
    }
    /**
     * 社情民意上传文件
     * */

    public static Call<String> socialUpload(String url,  String token, String fileName, String filePath,ProgressListener listener){
        String httpUrl = HTTP_URL + url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);

        File file = new File(filePath);

        if (!file.exists()) {
            ToastUtils.showShort(MyApplication.getMyContext(), "文件不存在");
            Logger.i("filenotexists");
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("name", fileName, requestFile);
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(requestFile,listener);
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("file\"; filename=\"" + fileName, fileRequestBody);
        return netApi.socialUpload(fileName, token, requestBodyMap);

    }

    /**
     * 获取自己的上传文件
     * */

    public static Call<String> selfUpload(String url,  String token){

        String httpUrl  = HTTP_URL+url;
        NetApi netApi = getRetrofit(httpUrl).create(NetApi.class);

        return netApi.selfUpload(token);
    }

    /***
     * 获取融云token
     * */
    public static Call<String> getRoimToken(String url,String token){

        String httpUrl  = url;
        NetApi netApi = geRomRetrofit(httpUrl).create(NetApi.class);
        return netApi.roimToken(token);
    }

}
