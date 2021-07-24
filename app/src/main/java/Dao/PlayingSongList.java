package Dao;

public class PlayingSongList {
    String songListName;
    String id;
    String songListType;
    int nowPage;
    int maxPage;

    public PlayingSongList() {
    }

    public PlayingSongList(String songListName, String id, String songListType) {
        this.songListName = songListName;
        this.id = id;
        this.songListType = songListType;
        this.nowPage = 0;
        this.maxPage = 10;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public String getSongListType() {
        return songListType;
    }

    public void setSongListType(String songListType) {
        this.songListType = songListType;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
