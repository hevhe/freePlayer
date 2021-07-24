package Dao;

public class MyInterFaceMgr {

    public interface onRecycleViewPlayListener{
        void onPlayListener(SongInfo songInfo);
    }

    public interface onRecycleViewAddListener{
        void onAddListener(SongInfo songInfo);
    }

    public interface onRecycleViewDeleteListener{
        void onDeleteListener(SongInfo songInfo);
    }

    public interface onPlaySonglistListener{
        void onPlaySonglist(PlayingSongList songList);
    }
}
