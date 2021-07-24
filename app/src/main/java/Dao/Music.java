package Dao;


import org.litepal.crud.LitePalSupport;

public class Music extends LitePalSupport {
    private String singerName;
    private String albumName;
    private String playUrl;
    private String lrc;
    private String backImageUrl;
    private String playIconImageUrl;
    private String songName;
    private String sourceType;

    public Music() {
    }

    public Music(String singerName, String albumName, String playUrl, String lrc, String backImageUrl, String playIconImageUrl, String songName, String sourceType) {
        this.singerName = singerName;
        this.albumName = albumName;
        this.playUrl = playUrl;
        this.lrc = lrc;
        this.backImageUrl = backImageUrl;
        this.playIconImageUrl = playIconImageUrl;
        this.songName = songName;
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getBackImageUrl() {
        return backImageUrl;
    }

    public void setBackImageUrl(String backImageUrl) {
        this.backImageUrl = backImageUrl;
    }

    public String getPlayIconImageUrl() {
        return playIconImageUrl;
    }

    public void setPlayIconImageUrl(String playIconImageUrl) {
        this.playIconImageUrl = playIconImageUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
