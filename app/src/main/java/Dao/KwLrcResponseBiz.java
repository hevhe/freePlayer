package Dao;

import java.util.List;

public class KwLrcResponseBiz {
    private String msg;
    private String msgs;
    private String profileid;
    private String reqid;
    private int status;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgs() {
        return msgs;
    }

    public void setMsgs(String msgs) {
        this.msgs = msgs;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
