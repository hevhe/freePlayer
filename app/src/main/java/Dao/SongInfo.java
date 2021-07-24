package Dao;


import org.litepal.crud.LitePalSupport;

public class SongInfo extends LitePalSupport {

    private long id;
    private String rid;
    private int hasmv;
    private String pic;
    private String albumpic;
    private String pic120;
    private String album ;
    private String albumid ;
    private String name ;
    private String artist ;
    private String musicrid ;
    private String playInfo ;
    private String lrc;
    private String sourceType ="KW";
    private boolean isPlaying;

    public SongInfo() {

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(String playInfo) {
        this.playInfo = playInfo;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getHasmv() {
        return hasmv;
    }

    public void setHasmv(int hasmv) {
        this.hasmv = hasmv;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAlbumpic() {
        return albumpic;
    }

    public void setAlbumpic(String albumpic) {
        this.albumpic = albumpic;
    }

    public String getPic120() {
        return pic120;
    }

    public void setPic120(String pic120) {
        this.pic120 = pic120;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMusicrid() {
        return musicrid;
    }

    public void setMusicrid(String musicrid) {
        this.musicrid = musicrid;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }


}
