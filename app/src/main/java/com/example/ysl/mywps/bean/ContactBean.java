package com.example.ysl.mywps.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ysl on 2018/1/16.
 */

public class ContactBean  implements Parcelable {


    /**
     * uid : 215
     * username : 18688654478
     * mobile : 18688654478
     * realname : null
     * dept : zhengxie
     * dept_name : 政协
     */



    private String capital;
    private String uid;
    private String username;
    private String mobile;
    private String realname;
    private String dept;
    private String dept_name;


    public ContactBean(){

    }


    protected ContactBean(Parcel in) {
        capital = in.readString();
        uid = in.readString();
        username = in.readString();
        mobile = in.readString();
        realname = in.readString();
        dept = in.readString();
        dept_name = in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel in) {
            return new ContactBean(in);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDept_name() {
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(capital);
        parcel.writeString(uid);
        parcel.writeString(username);
        parcel.writeString(mobile);
        parcel.writeString(realname);
        parcel.writeString(dept);
        parcel.writeString(dept_name);
    }
}
