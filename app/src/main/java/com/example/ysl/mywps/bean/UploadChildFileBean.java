package com.example.ysl.mywps.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class UploadChildFileBean implements Parcelable{


    /**
     * id : 53
     * filename : 1519652477320.mp4
     * filename_qn : video_1519652477320.mp4
     * ctime : 2018-02-26 22:29:41
     * download_url : http://p2c152618.bkt.clouddn.com/video_1519652477320.mp4
     */

    private String id;
    private String filename;
    private String filename_qn;
    private String ctime;
    private String download_url;


    public UploadChildFileBean(){

    }
    protected UploadChildFileBean(Parcel in) {
        id = in.readString();
        filename = in.readString();
        filename_qn = in.readString();
        ctime = in.readString();
        download_url = in.readString();
    }

    public static final Creator<UploadChildFileBean> CREATOR = new Creator<UploadChildFileBean>() {
        @Override
        public UploadChildFileBean createFromParcel(Parcel in) {
            return new UploadChildFileBean(in);
        }

        @Override
        public UploadChildFileBean[] newArray(int size) {
            return new UploadChildFileBean[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
    UploadChildFileBean bean = (UploadChildFileBean) obj;



        return this.id.equals(bean.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename_qn() {
        return filename_qn;
    }

    public void setFilename_qn(String filename_qn) {
        this.filename_qn = filename_qn;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(filename);
        parcel.writeString(filename_qn);
        parcel.writeString(ctime);
        parcel.writeString(download_url);
    }
}
