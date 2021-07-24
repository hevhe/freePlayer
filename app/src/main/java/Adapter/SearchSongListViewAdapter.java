package Adapter;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freeplayer.R;
import java.util.List;

import Dao.MyFavoriteSong;
import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import Helper.MusicSql;

public class SearchSongListViewAdapter extends  RecyclerView.Adapter<SearchSongListViewAdapter.ViewHolder> {

    private List<SongInfo> songs;
    MyInterFaceMgr.onRecycleViewPlayListener mlistener;

    public void setMlistener(MyInterFaceMgr.onRecycleViewPlayListener mlistener) {
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.search_fragment_song_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final SongInfo songInfo = songs.get(position);
        holder.songNameText.setText(songInfo.getName());
        if(MediaPlayerHelper.checkfavoriteSongs(songInfo.getRid())){
            holder.loveBut.setImageResource(R.drawable.loved);
        }else{
            holder.loveBut.setImageResource(R.drawable.list_love);
        }
        holder.singerNameText.setText(songInfo.getArtist()+"·"+songInfo.getAlbum());
        holder.playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onPlayListener(songInfo);
            }
        });
        holder.addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerHelper.getInstance(null).songPlayList.add(songInfo);
            }
        });
        holder.loveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaPlayerHelper.checkfavoriteSongs(songInfo.getRid())){
                    holder.loveBut.setImageResource(R.drawable.list_love);
                    MediaPlayerHelper.removeFavoriteSongInfo(songInfo);
                }else{
                    holder.loveBut.setImageResource(R.drawable.loved);
                    MediaPlayerHelper.saveFavoriteSongInfo(songInfo);
                }
                //notifyDataSetChanged();
            }
        });
    }

    public SearchSongListViewAdapter(List<SongInfo> musicList){
        songs=musicList;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView songNameText;
        private TextView singerNameText;
        private LinearLayout playBut;
        private ImageButton addBut;
        private ImageButton loveBut;
        private ImageButton downBut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameText=itemView.findViewById(R.id.search_fragment_song_list_item_song_name);
            singerNameText=itemView.findViewById(R.id.search_fragment_song_list_item_singer_name);
            playBut=itemView.findViewById(R.id.search_fragment_song_list_play_but);
            addBut=itemView.findViewById(R.id.search_fragment_song_list_add_but);
            loveBut=itemView.findViewById(R.id.search_fragment_song_list_love_but);
            downBut=itemView.findViewById(R.id.search_fragment_song_list_download_but);
        }
    }
}



