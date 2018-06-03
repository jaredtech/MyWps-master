package com.example.ysl.mywps.net;

import com.example.ysl.mywps.bean.PostQueryInfo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ysl on 2018/1/17.
 */

public interface NetApi {
    @POST("query")
    Call<String> login(@Query("username") String username, @Query("password") String password, @Query("regid") String regId);

    @POST("query")
    Call<String> doucmentList(@Query("token") String token, @Query("page") String page, @Query("pagelimit") String pagemilit,@Query("proce_type") String type);

    /**
     * 提交审核
     */
    @Multipart
    @POST("query")
    Call<String> commitAudit(@Query("doc_id") String doc_id, @Query("to_uid") String to_uid, @Query("token") String token, @Part MultipartBody.Part file);

    /**
     * 下载文档
     */
    @GET
    Call<ResponseBody> downloadWps(@Url String url);

    /**
     * 获取通讯录联系人
     * */
    @POST("query")
    Call<String> contact(@Query("token") String token);

    /**
     * 文档返回拟稿人
     */
    @Multipart
    @POST("query")
    Call<String> uploadWps(@Query("doc_id") String docId, @Query("proce_id") String proceId, @Query("token") String token, @Query("opinion") String opinion, @Part MultipartBody.Part file);

    /**
     * 提交文件领导签署
     */
    @Multipart
    @POST("query")
    Call<String> commitSign(@Query("doc_id") String docId, @Query("token") String token, @Part MultipartBody.Part file, @Query("leader_id") String leaderId);

    /***
     *签署完成返回公文给拟稿人
     */
    @Multipart
    @POST("query")
    Call<String> signedCommit(@Query("proce_id") String proceId, @Query("doc_id") String docId, @Query("opinion") String opinion, @Query("is_signed") String signed, @Query("token") String token, @Part MultipartBody.Part file);

    /***
    * 获取流程
    * */
    @POST("query")
    Call<String>  getFlow(@Query("doc_id") String docId,@Query("token") String token);

    /**
     * 转发进入反馈流程
     * */
 @POST("query")
 Call<String> doc_forward(@Query("doc_id") String docId,@Query("uids") String uids,@Query("token") String token);

    /**
     * 反馈
     * */
    @POST("query")
    Call<String> feed_back(@Query("doc_id") String docId,@Query("opinion") String opinion,@Query("token") String token);

    /**
     * 上传文件
     * */
    @Multipart
    @POST("query")
    Call<String>  sharedUpload(@Query("file_type") String fileType,@Query("token")String token,@PartMap Map<String, RequestBody> file);

    /**
     * 文件类目
     * */
    @POST("query")
    Call<String> getFileType(@Query("token") String token);

    /**
     * 文件列表
     * */
    @POST("query")
    Call<String> fileList(@Query("token") String token,@Query("file_type") String fileType,@Query("page") String page,@Query("pagelimit") String pagelimit);

    /**
     *删除文件
     * */
    @POST("query")
    Call<String> deleteDocument (@Query("file_id") String id,@Query("token") String token);

    /**
     * 社情民意上传文件
     * */
    @Multipart
    @POST("query")
    Call<String>  socialUpload(@Query("name") String name,@Query("token")String token,@PartMap Map<String, RequestBody> file);

    /**
     *获取自己上传的文件
     * */
    @POST("query")
    Call<String> selfUpload (@Query("token") String token);

    /**
     * 获取融云token
     * */
    @POST("query")
    Call<String> roimToken(@Query("token") String token);


}
