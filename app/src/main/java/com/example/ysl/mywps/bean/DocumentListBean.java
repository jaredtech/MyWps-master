package com.example.ysl.mywps.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ysl on 2018/1/18.
 */

public class DocumentListBean implements Parcelable {


//    {
//        "id": "40",          //文档id
//            "uid": "215",
//            "title": "12312",   //文档标题
//            "dept": "123123",   //部门名称
//            "des": "123123123", //文档描述
//            "doc_name": "40_中文.docx",
//            "doc_url": "p2c152618.bkt.clouddn.com/40_中文.docx", //文档下载地址
//            "doc_imgs": {
//        "1": "p2c152618.bkt.clouddn.com/40_中文.docx_1.png", //文档图片地址
//                "2": "p2c152618.bkt.clouddn.com/40_中文.docx_2.png",
//                "3": "p2c152618.bkt.clouddn.com/40_中文.docx_3.png"
//    },
//        "status": "1",   //文档当前状态   1拟稿 2审核 3签发 4流转
//            "ctime": "2018-01-12 17:39:32",
//            "now_uid": "215",   //当前流程所属用户
//            "from_uid": "0",
//            "new_time": "2018-01-12 17:40:59",  //最新更新时间
//            "username": "18688654478",   //当前流程所属用户用户名
//            "nickname": "不知道啊",     //当前流程所属用户昵称
//            "realname": null
//    },





    /**
     * id : 3
     * uid : 1
     * title : 公文1
     * dept : zhengxie
     * des : 公文一发布
     * doc_name : 3_公文1.doc
     * doc_url : http://p2c152618.bkt.clouddn.com/3_公文1.doc?v=1517039892
     * status : 1
     * ctime : 2018-01-25 22:30:01
     * proce_id : 83
     * now_uid : 1
     * from_uid : 1
     * n_time : 2018-01-25 22:30:01
     * opinion : null
     * is_writable : 0
     * c_username : admin
     * c_nickname : admin
     * c_realname : null
     * now_username : admin
     * now_nickname : admin
     * now_realname : null
     * dept_name : 政协
     * is_forward:"" 1是被转发的 0不是
     */
    private ArrayList<DocumentImageBean> doc_imgs;
    private String id;
    private String uid;
    private String title;
    private String dept;
    private String des;
    private String doc_name;
    private String doc_url;
    private String status;
    private String ctime;
    private String proce_id;
    private String now_uid;
    private String from_uid;
    private String n_time;
    private String opinion;
    private int is_writable;
    private String c_username;
    private String c_nickname;
    private String  c_realname;
    private String now_username;
    private String now_nickname;
    private String  now_realname;
    private String dept_name;
    private String is_forward;


    public String getIs_forward() {
        return is_forward;
    }

    public void setIs_forward(String is_forward) {
        this.is_forward = is_forward;
    }

    public DocumentListBean(){

    }


    protected DocumentListBean(Parcel in) {
        doc_imgs = in.createTypedArrayList(DocumentImageBean.CREATOR);
        id = in.readString();
        uid = in.readString();
        title = in.readString();
        dept = in.readString();
        des = in.readString();
        doc_name = in.readString();
        doc_url = in.readString();
        status = in.readString();
        ctime = in.readString();
        proce_id = in.readString();
        now_uid = in.readString();
        from_uid = in.readString();
        n_time = in.readString();
        opinion = in.readString();
        is_writable = in.readInt();
        c_username = in.readString();
        c_nickname = in.readString();
        c_realname = in.readString();
        now_username = in.readString();
        now_nickname = in.readString();
        now_realname = in.readString();
        dept_name = in.readString();
        is_forward = in.readString();
    }

    public static final Creator<DocumentListBean> CREATOR = new Creator<DocumentListBean>() {
        @Override
        public DocumentListBean createFromParcel(Parcel in) {
            return new DocumentListBean(in);
        }

        @Override
        public DocumentListBean[] newArray(int size) {
            return new DocumentListBean[size];
        }
    };

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getC_realname() {
        return c_realname;
    }

    public void setC_realname(String c_realname) {
        this.c_realname = c_realname;
    }

    public String getNow_realname() {
        return now_realname;
    }

    public void setNow_realname(String now_realname) {
        this.now_realname = now_realname;
    }

    public ArrayList<DocumentImageBean> getDoc_imgs() {
        return doc_imgs;
    }

    public void setDoc_imgs(ArrayList<DocumentImageBean> doc_imgs) {
        this.doc_imgs = doc_imgs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getProce_id() {
        return proce_id;
    }

    public void setProce_id(String proce_id) {
        this.proce_id = proce_id;
    }

    public String getNow_uid() {
        return now_uid;
    }

    public void setNow_uid(String now_uid) {
        this.now_uid = now_uid;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getN_time() {
        return n_time;
    }

    public void setN_time(String n_time) {
        this.n_time = n_time;
    }




    public int getIs_writable() {
        return is_writable;
    }

    public void setIs_writable(int is_writable) {
        this.is_writable = is_writable;
    }

    public String getC_username() {
        return c_username;
    }

    public void setC_username(String c_username) {
        this.c_username = c_username;
    }

    public String getC_nickname() {
        return c_nickname;
    }

    public void setC_nickname(String c_nickname) {
        this.c_nickname = c_nickname;
    }


    public String getNow_username() {
        return now_username;
    }

    public void setNow_username(String now_username) {
        this.now_username = now_username;
    }

    public String getNow_nickname() {
        return now_nickname;
    }

    public void setNow_nickname(String now_nickname) {
        this.now_nickname = now_nickname;
    }



    public String getDept_name() {

        if(dept_name == null){
            return "";
        }
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(doc_imgs);
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(dept);
        dest.writeString(des);
        dest.writeString(doc_name);
        dest.writeString(doc_url);
        dest.writeString(status);
        dest.writeString(ctime);
        dest.writeString(proce_id);
        dest.writeString(now_uid);
        dest.writeString(from_uid);
        dest.writeString(n_time);
        dest.writeString(opinion);
        dest.writeInt(is_writable);
        dest.writeString(c_username);
        dest.writeString(c_nickname);
        dest.writeString(c_realname);
        dest.writeString(now_username);
        dest.writeString(now_nickname);
        dest.writeString(now_realname);
        dest.writeString(dept_name);
        dest.writeString(is_forward);
    }
}