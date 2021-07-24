package Helper;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Dao.MyFavoriteSong;
import Dao.SongInfo;

public class MusicSql {

    public static boolean isExsitsSongInfo(String playInfo){
        List<SongInfo> sss= LitePal.where("playInfo = ?", playInfo).find( SongInfo.class);
        if(sss.size()>0){
            return true;
        }else{
            return false;
        }
    }

    public static void saveSongInfo(SongInfo songInfo ){
        List<SongInfo> sss= LitePal.where("rid = ?", songInfo.getRid()).find( SongInfo.class);
        if(sss.size()>0){
            return;
        }else{
            songInfo.save();
        }
    }

//    public static void saveDownloadTask(DownloadService ds ){
//        List<DownloadService> sss= LitePal.where("taskUrl = ?", ds.getTaskUrl().replace("\"","")).find( DownloadService.class);
//        if(sss.size()>0){
//            return;
//        }else{
//            ds.save();
//        }
//    }
//    public static void updateDownloadServiceByProgress(DownloadService ds){
//        ds.setProgressValue(100);
//        ds.updateAll("taskId = ?",ds.getTaskId());
//    }

    public static int removeFavoriteSongInfo(String songId){
        return LitePal.deleteAll(MyFavoriteSong.class, " songId= ?", songId);
    }

    public static void saveFavoriteSongInfo(MyFavoriteSong favoriteSong ){
        List<MyFavoriteSong> sss= LitePal.where("songId = ?", favoriteSong.getSongId()).find(MyFavoriteSong.class);
        if(sss.size()>0){
            return;
        }else{
            favoriteSong.save();
        }
    }

    public static int removeSongInfo(SongInfo songInfo){
        return LitePal.deleteAll(SongInfo.class, " playInfo= ?", songInfo.getPlayInfo());
    }

    public static List<SongInfo> getLoveSongs(){
        List<SongInfo> temp=new ArrayList<>();
        Cursor cursor=LitePal.findBySQL("select * from myfavoritesong\n" +
                "inner join songinfo on songinfo.rid=myfavoritesong.songid order by myfavoritesong.id desc");
        while (cursor.moveToNext()){
            SongInfo songInfo=new SongInfo();
            songInfo.setId(cursor.getLong(2));
            songInfo.setAlbum(cursor.getString(3));
            songInfo.setAlbumid(cursor.getString(4));
            songInfo.setAlbumpic(cursor.getString(5));
            songInfo.setArtist(cursor.getString(6));
            songInfo.setHasmv(cursor.getInt(7));
            songInfo.setLrc(cursor.getString(8));
            songInfo.setMusicrid(cursor.getString(9));
            songInfo.setName(cursor.getString(10));
            songInfo.setPic(cursor.getString(11));
            songInfo.setPic120(cursor.getString(12));
            songInfo.setPlayInfo(cursor.getString(13));
            songInfo.setRid(cursor.getString(14));
            songInfo.setSourceType(cursor.getString(15));
            temp.add(songInfo);
        }
        return temp;
    }
}
