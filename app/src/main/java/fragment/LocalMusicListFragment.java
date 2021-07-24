package fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freeplayer.R;
import java.util.ArrayList;
import java.util.List;
import Adapter.MusicAdapter;
import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import activity.MainActivity;

public class LocalMusicListFragment extends Fragment {
    RecyclerView listView;
    MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.local_song_list_fragment,container,false) ;
        MainActivity.musicList=getMusicList(MainActivity.context);
        listView=view.findViewById(R.id.song_list_fragment_recycle_listView);
        loadSongs();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadSongs() {
        musicAdapter=new MusicAdapter(getMusicList(getContext()));
        musicAdapter.setiItemOnclickUpdateActivityListener(new MyInterFaceMgr.onRecycleViewPlayListener() {
            @Override
            public void onPlayListener(SongInfo songInfo) {
                if(getActivity() instanceof MainActivity){
                    MediaPlayerHelper.getInstance(getContext()).getPlayInfo(songInfo);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(musicAdapter);
    }


    private List<SongInfo> getMusicList(Context context){
        Cursor cursor = null;
        List<SongInfo> mediaList = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[] {
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DISPLAY_NAME,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.SIZE},
                    null, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
            if(cursor == null) {
                Log.d(null, "The getMediaList cursor is null.");
                return mediaList;
            }
            int count= cursor.getCount();
            if(count <= 0) {
                Log.d(null, "The getMediaList cursor count is 0.");
                return mediaList;
            }
            mediaList = new ArrayList<>();
            SongInfo music = null;
            while (cursor.moveToNext()) {
                music = new SongInfo();
                music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setArtist( cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setRid(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setPlayInfo(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setPic("R.drawable.zhuanji");
                music.setPic120("R.drawable.zhuanji");
                music.setSourceType("LOCAL");
                mediaList.add(music);
            }
        } catch (Exception e) {
            Log.d("ceshi", "getMusicList: "+e.getMessage());
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return mediaList;
    }
}
