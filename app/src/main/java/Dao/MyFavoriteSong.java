package Dao;

import org.litepal.crud.LitePalSupport;

public class MyFavoriteSong extends LitePalSupport {
    int id;
    String songId;

    public MyFavoriteSong(int id, String songId) {
        this.id = id;
        this.songId = songId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
