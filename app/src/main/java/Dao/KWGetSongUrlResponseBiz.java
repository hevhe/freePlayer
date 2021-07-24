package Dao;

import com.google.gson.JsonObject;

public class KWGetSongUrlResponseBiz {
    public String msg;
    public int code;
    public String url;


    public KWGetSongUrlResponseBiz(JsonObject jsonObject){
        try{
            this.code = Integer.valueOf(jsonObject.get("code").toString()) ;
            this.msg = jsonObject.get("msg").toString().replace("\"","");
            this.url = jsonObject.get("url").toString().replace("\"","");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
