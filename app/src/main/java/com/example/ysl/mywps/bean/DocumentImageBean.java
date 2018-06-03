package com.example.ysl.mywps.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class DocumentImageBean implements Parcelable {
    /**
     * page : 1
     * img : http://p2c152618.bkt.clouddn.com/1_测试中文.docx_1.png
     */

    private int page;
    private String img;

    public DocumentImageBean() {

    }

    protected DocumentImageBean(Parcel in) {
        page = in.readInt();
        img = in.readString();
    }

    public static final Creator<DocumentImageBean> CREATOR = new Creator<DocumentImageBean>() {
        @Override
        public DocumentImageBean createFromParcel(Parcel in) {
            return new DocumentImageBean(in);
        }

        @Override
        public DocumentImageBean[] newArray(int size) {
            return new DocumentImageBean[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(img);
    }

//    {"code":0,"msg":"公文列表获取成功","data":{"total":1,"list":[
//            {"id":"1","uid":"2","title":"1231","dept":"123123","des":"123123","doc_name":"1_测试中文.docx",
//                    "doc_url":"http:\/\/p2c152618.bkt.clouddn.com\/1_测试中文.docx",
//                    "doc_imgs":[{"page":1,"img":"http:\/\/p2c152618.bkt.clouddn.com\/1_测试中文.docx_1.png"},
//
//                {"page":2,"img":"http:\/\/p2c152618.bkt.clouddn.com\/1_测试中文.docx_2.png"},
//                {"page":3,"img":"http:\/\/p2c152618.bkt.clouddn.com\/1_测试中文.docx_3.png"}],
//
//                "status":"1","ctime":"2018-01-16 16:47:17","now_uid":"2","from_uid":"2","n_time":"2018-01-16 16:47:19",
// "is_writable":0,"username":"18688654475","nickname":"系统消息","realname":null},
//
// {"id":"2","uid":"2","title":"1231","dept":"123123","des":"123123","doc_name":"2_测试中文.docx","doc_url":"http:\/\/p2c152618.bkt.clouddn.com\/2_测试中文.docx",
//
//                "doc_imgs":[{"page":1,"img":"http:\/\/p2c152618.bkt.clouddn.com\/2_测试中文.docx_1.png"},
// {"page":2,"img":"http:\/\/p2c152618.bkt.clouddn.com\/2_测试中文.docx_2.png"},
// {"page":3,"img":"http:\/\/p2c152618.bkt.clouddn.com\/2_测试中文.docx_3.png"}],
// "status":"1","ctime":"2018-01-16 16:55:52","now_uid":"2","from_uid":"2",
// "n_time":"2018-01-16 17:32:34","is_writable":0,"username":"18688654475","nickname":"系统消息","realname":null}]}}

}
