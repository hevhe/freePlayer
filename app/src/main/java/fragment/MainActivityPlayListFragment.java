package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freeplayer.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.PlayListAdapter;
import Adapter.PlayListDetailAdapter;
import Dao.IPlayListRecycleViewImageButtonListener;
import Dao.PlaySongListBiz;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;

public class MainActivityPlayListFragment extends Fragment {
    PlayListAdapter playListAdapter;
    PlayListDetailAdapter playListDetailAdapter;

    List<PlaySongListBiz> playSongListBizs;
    List<SongInfo> songInfos;
    RelativeLayout relativeLayout;
    RecyclerView playListRecycleView;
    RecyclerView playListDetailRecycleView;


    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.play_list_fragment,container,false) ;
        Log.d("", "MainActivityPlayListFragment: "+"我被创建");
        playListRecycleView=view.findViewById(R.id.play_list_song_list_recycle_view);
        playListDetailRecycleView=view.findViewById(R.id.play_list_song_list_detail_recycle_view);
        relativeLayout=view.findViewById(R.id.play_list_fragment_detail_toolbar);
        loadSongs(getPlaySongLists());
        return view;
    }

    private  List<PlaySongListBiz> getPlaySongLists(){
        List<PlaySongListBiz> playSongListTemp=new ArrayList<>();
        playSongListTemp.add(new PlaySongListBiz(null,"歌单1"));
        playSongListTemp.add(new PlaySongListBiz("https://bai","歌单2"));
        return playSongListTemp;
    }

    private void loadSongs(List<PlaySongListBiz> playSongLists) {
        playListAdapter=new PlayListAdapter(playSongLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        playListRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1, RecyclerView.HORIZONTAL, false));
        playListRecycleView.setAdapter(playListAdapter);
        playListAdapter.setiPlayListRecycleViewImageButtonListener(new IPlayListRecycleViewImageButtonListener() {
            @Override
            public void OnPlayListRecycleViewImageButtonListener(int id) {
                relativeLayout.setVisibility(View.VISIBLE);
                loadSongInfoDetail(getPlaySongListDetail(id));
            }
        });
    }

    private  List<SongInfo> getPlaySongListDetail(int id){
        List<SongInfo> playSongListTemp=MediaPlayerHelper.songPlayList;
        return playSongListTemp;
    }

    private void loadSongInfoDetail(List<SongInfo> SongInfos) {
        playListDetailAdapter=new PlayListDetailAdapter(SongInfos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        playListDetailRecycleView.setLayoutManager(layoutManager);
        playListDetailRecycleView.setAdapter(playListDetailAdapter);
    }
}
