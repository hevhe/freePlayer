package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freeplayer.R;

import java.util.ArrayList;
import java.util.List;
import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import Helper.MusicSql;

public class PlayListDetailAdapter extends RecyclerView.Adapter<PlayListDetailAdapter.ViewHolder> {

    List<SongInfo> songInfoList;

    MyInterFaceMgr.onRecycleViewPlayListener mPlayListener;

    public PlayListDetailAdapter(List<SongInfo> songInfos){
        songInfoList=songInfos;
    }

    public void setmPlayListener(MyInterFaceMgr.onRecycleViewPlayListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_support_delete_item ,parent,false);
        PlayListDetailAdapter.ViewHolder holder = new PlayListDetailAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SongInfo songInfo = songInfoList.get(position);
        holder.songNameText.setText(songInfo.getName());
        holder.idText.setText(String.valueOf(position+1));
        holder.singerNameText.setText(songInfo.getArtist()+"·"+songInfo.getAlbum());
        if(position==MediaPlayerHelper.playIndex){
            holder.playImg.setVisibility(View.VISIBLE);
        }else{
            holder.playImg.setVisibility(View.INVISIBLE);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayListener.onPlayListener(songInfo);
                notifyDataSetChanged();
            }
        });
        holder.deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerHelper.getInstance(null).songPlayList.remove(songInfo);
                MusicSql.removeSongInfo(songInfo);
                notifyDataSetChanged();
            }
        });
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
        private ImageView deleteBut;
        private ImageView playImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_song_name);
            idText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_id);
            singerNameText=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_singer_name);
            deleteBut=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_delete_but);
            playImg=itemView.findViewById(R.id.play_song_list_support_delete_song_list_item_play_img);
            mainLayout=itemView.findViewById(R.id.support_main_layout);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
