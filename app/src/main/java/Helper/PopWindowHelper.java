package Helper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freeplayer.R;

import Adapter.PlayListDetailAdapter;
import Adapter.PlayingListAdapter;
import Dao.MyInterFaceMgr;
import Dao.PlayingSongList;
import Dao.SongInfo;

public class PopWindowHelper {
    public static PopupWindow mPopWindow;

    public static void showPlayListPopWindow(Context context, final MediaPlayerHelper mediaPlayer){
        View contentView= LayoutInflater.from(context).inflate(R.layout.song_play_list_pop_window,null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        LinearLayout layout=contentView.findViewById(R.id.pop_win_top_layout);
        ImageView closeBut=contentView.findViewById(R.id.close_pop_window);
        ImageView refreshBut=contentView.findViewById(R.id.pop_window_refresh_song_list);
        TextView playListName=contentView.findViewById(R.id.pop_win_top_layout_play_list_name);
        if(mediaPlayer.playingSongList.getId()==""||mediaPlayer.playingSongList.getId()==null){
            playListName.setVisibility(View.INVISIBLE);
        }else{
            playListName.setVisibility(View.VISIBLE);
            playListName.setText("当前歌单："+mediaPlayer.playingSongList.getSongListName());
        }
        mPopWindow.setContentView(contentView);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        RecyclerView listView=contentView.findViewById(R.id.song_play_list_pop_window_recycle_view);
        final PlayingListAdapter adapter=new PlayingListAdapter(mediaPlayer.songPlayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        listView.setLayoutManager(layoutManager);
        adapter.setmPlayListener(new MyInterFaceMgr.onRecycleViewPlayListener() {
            @Override
            public void onPlayListener(SongInfo songInfo) {
                mediaPlayer.getPlayInfo(songInfo);
            }
        });
        listView.setAdapter(adapter);
        View rootview = LayoutInflater.from(context).inflate(R.layout.song_play_list_pop_window, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
        closeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });
        refreshBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.playSongListMethod(mediaPlayer.playingSongList);
            }
        });
    }
}
