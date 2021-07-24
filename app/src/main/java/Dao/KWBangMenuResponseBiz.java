package Dao;

import java.util.List;

public class KWBangMenuResponseBiz {
    private String code;
    private String curTime;
    private List<KwBangMenuDataBiz> data;
    private String msg;
    private String profileId;
    private String reqId;
    private String tId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public List<KwBangMenuDataBiz> getData() {
        return data;
    }

    public void setData(List<KwBangMenuDataBiz> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }
}
