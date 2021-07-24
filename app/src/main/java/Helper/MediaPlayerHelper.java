package Helper;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.litepal.LitePal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Dao.KWGetSongUrlResponseBiz;
import Dao.KwLrcDetailBiz;
import Dao.Lrc;
import Dao.LrcSearchBiz;
import Dao.MyFavoriteSong;
import Dao.PlaySongListBiz;
import Dao.PlayingSongList;
import Dao.SongInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MediaPlayerHelper {
    private final String TAG = this.getClass().getName();
    private static MediaPlayerHelper instance;
    private Context mContext;
    public static SongInfo playingSongInfo=new SongInfo();
    public List<Lrc> lrcList=new ArrayList<>();
    public static PlayingSongList playingSongList=new PlayingSongList();
    public String lrc1,lrc2;
    private int songReqNum=0;
    private int lrcReqNum=0;
    public static int playIndex=-1;
    private MediaPlayer mMediaPlayer;
    public static List<SongInfo> songPlayList= LitePal.findAll(SongInfo.class);
    public static List<PlaySongListBiz> songList= LitePal.findAll(PlaySongListBiz.class);
    public static List<MyFavoriteSong> favoriteSongs= LitePal.findAll(MyFavoriteSong.class);
    private OnMediaHelperPrepareListener mOnMediaHelperPrepareListener;
    private OnMediaHelperCompletionListener mOnMediaHelperCompletionListener;
    private OnMediaHelperStatusChangeListener mOnMediaHelperStatusChangeListener;
    private OnInitMusicListener mOnInitMusicListener;
    private int mResID=-5;
    private HttpHelper httpHelper=new HttpHelper();

    public void setmOnInitMusicListener(OnInitMusicListener mOnInitMusicListener) {
        this.mOnInitMusicListener = mOnInitMusicListener;
    }

    public void setmOnMediaHelperStatusChangeListener(OnMediaHelperStatusChangeListener mOnMediaHelperStatusChangeListener) {
        this.mOnMediaHelperStatusChangeListener = mOnMediaHelperStatusChangeListener;
    }

    public void setmOnMediaHelperPrepareListener(OnMediaHelperPrepareListener mOnMediaHelperPrepareListener) {
        this.mOnMediaHelperPrepareListener = mOnMediaHelperPrepareListener;
    }

    public void setmOnMediaHelperCompletionListener(OnMediaHelperCompletionListener mOnMediaHelperCompletionListener) {
        this.mOnMediaHelperCompletionListener = mOnMediaHelperCompletionListener;
    }

    public static MediaPlayerHelper getInstance(Context context) {
        if(instance==null){
            synchronized (MediaPlayerHelper.class){
                if(instance==null){
                    instance=new MediaPlayerHelper(context);
                }
            }
        }
        return instance;
    }

    private MediaPlayerHelper(Context context){
        mContext=context;
        mMediaPlayer=new MediaPlayer();
    }

    /**
     * 当播放本地uri中时调用
     * @param music
     */
    public void setPath(final SongInfo music){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(music.getPlayInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(mOnMediaHelperPrepareListener !=null){
                    if(music.getSourceType().equals("KW")){
                        getLrcByKw(music);
                    }else{
                        searchLrcByKg(music.getName());
                    }

                    mOnMediaHelperPrepareListener.onPrepared(mp);
                }
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(mOnMediaHelperPrepareListener !=null){
                    mOnMediaHelperCompletionListener.onCompletion(mp);
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
    }

    /**
    * 在线播放,通过歌曲ID获取播放URL
    * @param songInfo
    *
    */
    public void getPlayInfo(final SongInfo songInfo){
        mMediaPlayer.reset();
        playingSongInfo=songInfo;
        if(mOnInitMusicListener!=null){
            mOnInitMusicListener.initMode();
        }
        addSongInfoToPlayList(songInfo);
        MusicSql.saveSongInfo(songInfo);
        if(songInfo.getSourceType().equals("LOCAL")){
            setPath(songInfo);
            return;
        }
        songReqNum++;
        final int a=songReqNum;
        String reqUrl=String.format("http://www.kuwo.cn/url?format=mp3&rid=%s&response=url&type=convert_url3&br=320kflac&from=web&t=1624115857985&httpsStatus=1&reqId=7b65cb11-d111-11eb-a3c7-3d5c6f878efd", songInfo.getRid());
        httpHelper.doGetByKW(reqUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                KWGetSongUrlResponseBiz kwGetSongUrlResponse=new KWGetSongUrlResponseBiz(obj);
                if(songReqNum==a){
                    if(kwGetSongUrlResponse.getCode()==200){
                        songInfo.setPlayInfo(kwGetSongUrlResponse.getUrl());
                        playingSongInfo=songInfo;
                        setPath(playingSongInfo);
                    }else{
                        Log.d(TAG, "当前歌曲请求标记: 当前请求标记"+a+"当前总标记"+songReqNum);
                    }
                }

            }
        });

    }

    /**
     * 根据歌名获取歌词---酷狗
     * @param lrcFile
     *
     * */
    public void getLrcByKg(LrcSearchBiz lrcFile){
        lrcReqNum++;
        final int a=lrcReqNum;
        String url = String.format("http://m.kugou.com/app/i/krc.php?cmd=100&keyword=%sAA&hash=%s&timelength=240&d=0.123456789", lrcFile.getFilename(), lrcFile.getHash());
        httpHelper.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                if(lrcReqNum==a){
                    playingSongInfo.setLrc(res);
                    getLrcList(res);
                }else{
                    Log.d(TAG, "当前歌词请求标记: 当前请求标记"+a+"当前总标记"+lrcReqNum);
                }
            }
        });
    }

    private void searchLrcByKg(String songName) {
        String url=String.format("http://mobileservice.kugou.com/api/v3/lyric/search?version=9108&highlight=1&keyword=%s&plat=0&pagesize=1&area_code=1&page=%s&with_res_tag=1",songName,1);
        httpHelper.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string().replace("<!--KG_TAG_RES_START-->","").replace("<!--KG_TAG_RES_END-->","");
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                JsonObject obj =new JsonParser().parse(res).getAsJsonObject();
                List<LrcSearchBiz> ps =gson.fromJson(obj.get("data").getAsJsonObject().get("info"), new TypeToken<List<LrcSearchBiz>>(){}.getType());
                getLrcByKg(ps.get(0));
            }
        });
    }

    private void getLrcByKw(SongInfo songInfo){
        httpHelper.doGet(KwMusicHelper.getLrcReqUrl(songInfo.getRid()), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                JsonObject obj =new JsonParser().parse(res).getAsJsonObject();
               // KwLrcResponseBiz ps =gson.fromJson(obj, new TypeToken<KwLrcResponseBiz>(){}.getType());
                List<KwLrcDetailBiz> lrcRes=gson.fromJson(obj.get("data").getAsJsonObject().get("lrclist"), new TypeToken<List<KwLrcDetailBiz>>(){}.getType());
                getLrcListByKw(lrcRes);

            }
        });
    }

    public int getLrcPlayIndex(){
        int a=lrcList.size();
        int aaa=mMediaPlayer.getCurrentPosition();
        int index=0;
        for(int i=1;i<a;i++){
            if(i<a-1)                                          {
                lrcList.get(i).setTextColor(Color.GRAY);
                lrcList.get(i).setFontSize(15);
                if (lrcList.get(i).getTime() < aaa&&lrcList.get(i+1).getTime() >aaa) {
                    index = i;
                    lrcList.get(i).setTextColor(Color.GREEN);
                    lrcList.get(i).setFontSize(20);
                    lrc1= lrcList.get(i).getLrcText();
                    lrc2= lrcList.get(i+1).getLrcText();
                    break;
                }
                else if(lrcList.get(i).getTime() > aaa){
                    lrcList.get(i).setTextColor(Color.GREEN);
                    lrcList.get(i).setFontSize(20);
                    break;
                }
            }else if (i==a-1){
                index = i;
                lrcList.get(i).setTextColor(Color.GREEN);
                lrcList.get(i).setFontSize(20);
                lrc1= lrcList.get(i).getLrcText();
                lrc2="";
                break;
            }
            else if(i >= a - 2){
                index = i;
                lrcList.get(i).setTextColor(Color.GREEN);
                lrcList.get(i).setFontSize(25);
                lrc1= lrcList.get(i).getLrcText();
                lrc2="";
                break;
            }
        }
        return index ;
    }

    private void getLrcListByKw(List<KwLrcDetailBiz> ps) {
        if(lrcList.size()!=0){
            lrcList.clear();
        }
        if(ps.size()==0){
            Lrc lrc1=new Lrc("暂无",Color.WHITE,30,0);
            lrcList.add(lrc1);
        }else{
            Lrc lrc2=new Lrc("",Color.WHITE,15,0);
            lrcList.add(lrc2);
            try{

                for(KwLrcDetailBiz kwLrc:ps){
                    Lrc lrc3=new Lrc(kwLrc.getLineLyric(),Color.WHITE,15,(int)(Double.valueOf(kwLrc.getTime())*1000));
                    lrcList.add(lrc3);
                }
                Lrc lrc20=new Lrc("---《完》---",Color.WHITE,15,100000);
                lrcList.add(lrc20);
            }catch (Exception e){
                Log.d(TAG, "getLrcList() returned: " + e.getMessage());
            }
            Lrc lrc21=new Lrc("",Color.WHITE,15,100010);
            Lrc lrc22=new Lrc("",Color.WHITE,15,100020);
            lrcList.add(lrc21);
            lrcList.add(lrc22);
        }
    }

    private void getLrcList(String lrc) {
        if(lrcList.size()!=0){
            lrcList.clear();
        }
        if(lrc==null||lrc=="")
        {
            Lrc lrc1=new Lrc("暂无",Color.WHITE,30,0);
            lrcList.add(lrc1);
        }else{
            Lrc lrc2=new Lrc("",Color.WHITE,15,100020);
            lrcList.add(lrc2);
            try{
                String lrc1 = lrc.substring(lrc.indexOf("[00"));
                String[] lrcListTemp;
                //lrc1.replace("\\r\\n","\\r\\n");
                //lrc1.replace("\\r\\n","\\\r\\\n");
                lrcListTemp = lrc1.split("\r\n");
                if(lrcListTemp.length==1){
                    lrcListTemp = lrc1.split("\\\\r\\\\n");
                }
                for(String str:lrcListTemp){
                    Lrc lrc3=new Lrc(str.substring(10),Color.WHITE,15,GetSeconds(str.substring(1, 9)));
                    lrcList.add(lrc3);
                }
                Lrc lrc20=new Lrc("---《完》---",Color.WHITE,15,100000);
                lrcList.add(lrc20);
            }catch (Exception e){
                Log.d(TAG, "getLrcList() returned: " + e.getMessage());
            }
            Lrc lrc21=new Lrc("",Color.WHITE,15,100010);
            Lrc lrc22=new Lrc("",Color.WHITE,15,100020);
            lrcList.add(lrc21);
            lrcList.add(lrc22);
        }
    }

    private int GetSeconds(String time){
        try{
            int minite = Integer.parseInt(time.substring(0, 2));
            int second = Integer.parseInt(time.substring(3, 5));
            int millSecond = Integer.parseInt(time.substring(6, 8));
            return (minite * 60 + second)*1000+millSecond*10;
        }catch (Exception ex){
            return 0;
        }

    }

    private void addSongInfoToPlayList(SongInfo songInfo){
        if(checkSongInfoList(songInfo)){
            for(int i=0;i<songPlayList.size();i++){
                if(songPlayList.get(i).getRid().equals(songInfo.getRid())){
                    playIndex=i;
                }
            }
        }else{
            songPlayList.add(songInfo);
            playIndex=songPlayList.size();
        }
    }

    private boolean checkSongInfoList(SongInfo songInfo){
        for (SongInfo song:songPlayList
             ) {
            if(song.getRid().equals(songInfo.getRid())){
                return true;
            }
        }
        return false;
    }


    public void start(){
        if(mMediaPlayer.isPlaying()){
            return;
        }
        mMediaPlayer.start();
    }

    public void playOrPause(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }else{
            mMediaPlayer.start();
        }
        if(mOnMediaHelperStatusChangeListener!=null){
            mOnMediaHelperStatusChangeListener.statusChangeListener();
        }
    }

    public void pre(){
        if(songPlayList.size()==0) return;
        if(playIndex==0){
            playIndex=songPlayList.size()-1;
        }else if(playIndex>1&&playIndex<songPlayList.size()){
            playIndex--;
        }else{
            playIndex=0;
        }
        getPlayInfo(songPlayList.get(playIndex));
    }

    public void next(){
        if(songPlayList.size()==0) return;
        if(playIndex>=songPlayList.size()-1){
            playIndex=0;
        }
        playIndex++;
        getPlayInfo(songPlayList.get(playIndex));
    }

    public void pause(){
        if(!mMediaPlayer.isPlaying()){
            return;
        }
        mMediaPlayer.pause();
        //mOnMediaHelperPauseListener.showPauseImg();
    }

    public boolean isPlaying(){
        if(mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
            return true;
        }
        return false;
    }

    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mMediaPlayer.getDuration();
    }

    public void seekTo(int progress){
        mMediaPlayer.seekTo(progress);
    }

    public interface OnMediaHelperCompletionListener{
        void onCompletion(MediaPlayer mp);
    }

    public interface OnMediaHelperPrepareListener {
        void onPrepared(MediaPlayer mp);
    }

    public interface OnMediaHelperStatusChangeListener {
        void statusChangeListener();
    }

    public interface OnInitMusicListener{
        void initMode();
    }

    public void playSongListMethod(final PlayingSongList playingList){
        if(playingSongList.getId()==null||playingSongList.getId()==""){
            playingSongList=playingList;
        }else {
            if(playingSongList.getId().equals(playingList.getId())){
                playingSongList.setNowPage(playingSongList.getNowPage()+1);
            }else{
                playingSongList=playingList;
            }
        }
        String reqUrl=null;
        if(playingSongList.getSongListType().equals("BANG")){
            reqUrl= KwMusicHelper.getBangMenuDetail(playingSongList.getId(),playingSongList.getNowPage());
        }else if(playingSongList.getSongListType().equals("LIST")){
            reqUrl= KwMusicHelper.getSongPlayListDetail(playingSongList.getId(),playingSongList.getNowPage());
        }else if(playingSongList.getSongListType().equals("SINGER")){
            reqUrl= KwMusicHelper.searchByKey(playingList.getSongListName(),playingList.getNowPage());
        }
        Log.d(TAG, "请求URL:"+reqUrl);
        httpHelper.doGetByKW(reqUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+"请求错误"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d(TAG, "onResponse: "+res);
                try{
                    Gson gson=new Gson();
                    JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                    List<SongInfo> songInfos=new ArrayList<>();
                    if(playingList.getSongListType().equals("SINGER")){
                        songInfos=gson.fromJson(obj.get("data").getAsJsonObject().get("list"), new TypeToken<List<SongInfo>>(){}.getType());

                    }else{
                        songInfos=gson.fromJson(obj.get("data").getAsJsonObject().get("musicList"), new TypeToken<List<SongInfo>>(){}.getType());
                    }
                    songPlayList=songInfos;
                    getPlayInfo(songInfos.get(0));
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        });
    }

    public static void saveFavoriteSongInfo(SongInfo songInfo ){
        if(!checkfavoriteSongs(songInfo.getRid())){
            MyFavoriteSong favoriteSong=new MyFavoriteSong(1,songInfo.getRid());
            favoriteSongs.add(favoriteSong);
            MusicSql.saveSongInfo(songInfo);
            MusicSql.saveFavoriteSongInfo(favoriteSong);
        }
    }

    public static boolean checkfavoriteSongs(String songId){
        for(MyFavoriteSong song:favoriteSongs){
            if(song.getSongId().equals(songId)){
               return true;
            }
        }
        return false;
    }

    public static void removeFavoriteSongInfo(SongInfo songInfo){
        for(MyFavoriteSong song:favoriteSongs){
            if(song.getSongId().equals(songInfo.getRid())){
                favoriteSongs.remove(song);
                MusicSql.removeFavoriteSongInfo(song.getSongId());
                break;
            }
        }
    }

}
