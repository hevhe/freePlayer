package Dao;


public class KwSong {

    private String album ;
    private String albumid ;
    private String hasmv ;
    private String name ;
    private String pic ;
    private String artist ;
    private String musicrid ;
    private String rid ;
    private String sourceType ="KW";

    public KwSong(String album, String albumid, String hasmv, String name, String pic, String artist, String musicrid, String rid) {
        this.album = album;
        this.albumid = albumid;
        this.hasmv = hasmv;
        this.name = name;
        this.pic = pic;
        this.artist = artist;
        this.musicrid = musicrid;
        this.rid = rid;
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

    public String getHasmv() {
        return hasmv;
    }

    public void setHasmv(String hasmv) {
        this.hasmv = hasmv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}