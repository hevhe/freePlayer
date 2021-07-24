package Dao;

import org.litepal.crud.LitePalSupport;

public class PlaySongListBiz extends LitePalSupport {
    private int id;
    private String ImageUlr;
    private String playListName;

    public PlaySongListBiz(String imageUlr, String playListName) {
        ImageUlr = imageUlr;
        this.playListName = playListName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUlr() {
        return ImageUlr;
    }

    public void setImageUlr(String imageUlr) {
        ImageUlr = imageUlr;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
}
