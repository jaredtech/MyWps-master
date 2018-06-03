package com.example.ysl.mywps.bean;

/**
 * Created by Administrator on 2018/1/28 0028.
 */

public class FlowBean {


    /**
     * status : 反馈阶段
     * opinion : 啦啦啦
     * opinion_type : 审核人意见
     * ctime : 2018-01-28 17:53:42
     * username : 13240368231
     * realname : null
     */

    private String status;
    private String opinion;
    private String opinion_type;
    private String ctime;
    private String username;
    private String realname;
    private String month;
    private String time;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getOpinion_type() {
        return opinion_type;
    }

    public void setOpinion_type(String opinion_type) {
        this.opinion_type = opinion_type;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
