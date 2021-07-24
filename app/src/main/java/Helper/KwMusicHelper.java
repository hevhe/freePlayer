package Helper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class KwMusicHelper {
    HttpHelper httpHelper=new HttpHelper();
    /**
     * 获取歌单列表
     * */
    public static String getSongPlayList() {
        return  "http://www.kuwo.cn/api/www/playlist/getTagList?httpsStatus=1&reqId=1b445f10-d8e8-11eb-8ebe-ff96e43e5bb4";
    }

    public static String getSongList(String tag){
        return String.format("http://www.kuwo.cn/api/www/classify/playlist/getRcmPlayList?pn=1&rn=30&order=%s&httpsStatus=1&reqId=224d9580-d990-11eb-841a-9db68004ad28",tag);
    }
    /**
     * 获取歌手列表
     * */
    public void getFontPageSingerList(){
        //http://www.kuwo.cn/api/www/artist/artistInfo?category=11&pn=1&rn=6&httpsStatus=1&reqId=d6469640-d8e6-11eb-8ebe-ff96e43e5bb4
    }

    /**
     * 获取歌单列表明细
     * */
    public static String getSongPlayListDetail(String id,int page){
        return String.format("http://www.kuwo.cn/api/www/playlist/playListInfo?pid=%s&pn=%s&rn=30&httpsStatus=1&reqId=b308b630-d991-11eb-af10-03fc83074ab3",id,page);

        //歌单详细列表，最新 order=new
        //http://www.kuwo.cn/api/www/classify/playlist/getRcmPlayList?pn=1&rn=30&order=new&httpsStatus=1&reqId=1b443800-d8e8-11eb-8ebe-ff96e43e5bb4
        //歌单详细列表，最热  order=ho
        //http://www.kuwo.cn/api/www/classify/playlist/getRcmPlayList?pn=1&rn=30&order=hot&httpsStatus=1&reqId=77166220-d8e8-11eb-8ebe-ff96e43e5bb4
    }

    /**
     * 音乐榜单
     * */
    public static String getBangMenuListReqUrl() {
        return  "http://www.kuwo.cn/api/www/bang/bang/bangMenu?httpsStatus=1&reqId=80fc5e80-d8e7-11eb-8ebe-ff96e43e5bb4";
    }
    /**
     * 音乐榜单明细
     * */
    public static String getBangMenuDetail(String id,int page){
        return String.format("http://www.kuwo.cn/api/www/bang/bang/musicList?bangId=%s&pn=%s&rn=30&httpsStatus=1&reqId=82e1c050-d8e7-11eb-8ebe-ff96e43e5bb4",id,page);
    }
    /**
     * MV首播
    * */
    public static String getMvList(){
        return String.format("http:://www.kuwo.cn/api/www/music/mvList?pid=236682871&pn=1&rn=30&httpsStatus=1&reqId=fb1e6410-d8e7-11eb-8ebe-ff96e43e5bb4");
    }

    public static String getLrcReqUrl(String music_id){
        return String.format("http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId=%s&httpsStatus=1&reqId=74bb6af0-bd38-11eb-a204-bf63c43eb79b",music_id);
    }

    /**
     * 歌手列表
     * */
    public static String getSingerList(String tag) {
        if (tag.equals("ALL")) {
            return "http://www.kuwo.cn/api/www/artist/artistInfo?category=0&pn=1&rn=20&httpsStatus=1&reqId=fb08c3c0-d8e8-11eb-8ebe-ff96e43e5bb4";
        } else {
            return String.format("http://www.kuwo.cn/api/www/artist/artistInfo?category=0&prefix=%s&pn=1&rn=20&httpsStatus=1&reqId=1f9489e0-d8e9-11eb-8ebe-ff96e43e5bb4", tag);
        }
    }

    public static String searchByKey(String key,int page){
        return String.format("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=%s&pn=%s&rn=20&httpsStatus=1&reqId=d7446a30-bd3a-11eb-b1bb-1104bf986836", key, page);
    }
}