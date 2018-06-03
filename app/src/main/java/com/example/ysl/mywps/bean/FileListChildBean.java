package com.example.ysl.mywps.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ysl on 2018/2/8.
 * 介绍:filelist数组的bean文件
 */

public class FileListChildBean implements Parcelable{
//    {"id":"1","filename":"my_doc.docx","filename_qn":"my_doc.docx","ctime":"2018-02-05 14:44:47","download_url":"http:\/\/p2c152618.bkt.clouddn.com\/my_doc.docx"}

    /**
     * filename : my_doc.docx
     * ctime : 2018-02-05 14:44:47
     * download_url : http://p2c152618.bkt.clouddn.com/my_doc.docx
     */



    private String id;
    private String filename_qn;
    private String filename;
    private String ctime;
    private String download_url;

    public FileListChildBean(){

    }


    protected FileListChildBean(Parcel in) {
        id = in.readString();
        filename_qn = in.readString();
        filename = in.readString();
        ctime = in.readString();
        download_url = in.readString();
    }

    public static final Creator<FileListChildBean> CREATOR = new Creator<FileListChildBean>() {
        @Override
        public FileListChildBean createFromParcel(Parcel in) {
            return new FileListChildBean(in);
        }

        @Override
        public FileListChildBean[] newArray(int size) {
            return new FileListChildBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename_qn() {
        return filename_qn;
    }

    public void setFilename_qn(String filename_qn) {
        this.filename_qn = filename_qn;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    @Override
    public int hashCode() {
        return download_url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        FileListChildBean bean = (FileListChildBean) obj;


        return this.download_url.equals(bean.getDownload_url());
    }

    @Override
    public String toString() {
        return "{id: "+id+",filename_qn:"+filename_qn+",filename:"+filename+",ctime:"+ctime+",download_url:"+download_url+"}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(filename_qn);
        parcel.writeString(filename);
        parcel.writeString(ctime);
        parcel.writeString(download_url);
    }
}
