package fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freeplayer.R;

import java.util.List;

import Adapter.PlayListDetailAdapter;
import Adapter.UserPageSongListAdapter;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import Helper.MusicSql;

public class UserPageFragment extends Fragment {


    ImageView loveSongImg;
    ImageView lovePlayListImg;
    ImageView personPlayListImg;
    ImageView historyImg;
    ImageView userImg;
    TextView  loveSongNumText;
    TextView  lovePlayListText;
    TextView  personPlayListText;
    TextView  historyNumText;
    LinearLayout loveSongLayout;
    LinearLayout lovePlayListLayout;
    LinearLayout personPlayListLayout;
    LinearLayout historyLayout;
    RecyclerView recyclerView;
    TextView userNameText;
    UserPageSongListAdapter adapter;

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater .inflate(R.layout.user_page_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        lovePlayListLayout=view.findViewById(R.id.user_page_love_song_list_layout);
        loveSongLayout=view.findViewById(R.id.user_page_love_song_layout);
        personPlayListLayout=view.findViewById(R.id.user_page_person_song_list_layout);
        historyLayout=view.findViewById(R.id.user_page_history_song_list_layout);
        loveSongImg=view.findViewById(R.id.user_page_love_song_img);
        lovePlayListImg=view.findViewById(R.id.user_page_love_song_list_img);
        personPlayListImg=view.findViewById(R.id.user_page_person_song_list_img);
        historyImg=view.findViewById(R.id.user_page_history_song_list_img);
        loveSongNumText=view.findViewById(R.id.user_page_love_song_num_text);
        lovePlayListText=view.findViewById(R.id.user_page_love_song_list_num_text);
        personPlayListText=view.findViewById(R.id.user_page_person_song_list_num_text);
        historyNumText=view.findViewById(R.id.user_page_history_song_list_num_text);
        recyclerView=view.findViewById(R.id.play_list_song_list_recycle_view);
        userImg=view.findViewById(R.id.user_page_user_img);
        userNameText=view.findViewById(R.id.user_page_user_text);
        initViewData();
        setViewListener();
    }

    private void initViewData(){
        loveSongNumText.setText(String.valueOf(MediaPlayerHelper.favoriteSongs.size()));
        historyNumText.setText(String.valueOf(MediaPlayerHelper.songPlayList.size()));
    }

    private void setViewListener(){
        loveSongLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSongs(MusicSql.getLoveSongs());
                loveSongLayout.setBackgroundResource(R.drawable.backgroud);
            }
        });
    }

    private void loadSongs(List<SongInfo> songInfos){
        adapter=new UserPageSongListAdapter(songInfos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViewData();
    }
}
