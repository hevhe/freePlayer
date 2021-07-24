package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freeplayer.R;

import java.util.List;

import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import Helper.MusicSql;

public class UserPageSongListAdapter extends RecyclerView.Adapter<UserPageSongListAdapter.ViewHolder> {

    List<SongInfo> songInfoList;

    MyInterFaceMgr.onRecycleViewPlayListener mPlayListener;

    public UserPageSongListAdapter(List<SongInfo> songInfos){
        songInfoList=songInfos;
    }

    public void setmPlayListener(MyInterFaceMgr.onRecycleViewPlayListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_support_delete_item ,parent,false);
        UserPageSongListAdapter.ViewHolder holder = new UserPageSongListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SongInfo songInfo = songInfoList.get(position);
        holder.songNameText.setText(songInfo.getName());
        holder.idText.setText(String.valueOf(position+1));
        holder.singerNameText.setText(songInfo.getArtist()+"·"+songInfo.getAlbum());
    }

    @Override
    public int getItemCount() {
        return songInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameText;
        private TextView singerNameText;
        private TextView idText;
        private RelativeLayout mainLayout;
        private LinearLayout deleteBut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_song_name);
            idText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_id);
            singerNameText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_singer_name);
            deleteBut=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_delete_but);
            mainLayout=itemView.findViewById(R.id.support_main_layout);

        }
    }
}
