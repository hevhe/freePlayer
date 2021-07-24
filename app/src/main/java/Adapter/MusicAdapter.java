package Adapter;

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

import Dao.MyInterFaceMgr;
import Dao.SongInfo;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<SongInfo> musics;
    MyInterFaceMgr.onRecycleViewPlayListener onRecycleViewPlayListener;

    public void setiItemOnclickUpdateActivityListener(MyInterFaceMgr.onRecycleViewPlayListener onRecycleViewPlayListener) {
        this.onRecycleViewPlayListener = onRecycleViewPlayListener;
    }

    public MusicAdapter(List<SongInfo> musics) {
        this.musics = musics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_fragment_song_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SongInfo music=musics.get(position);
        holder.singer.setText(music.getArtist());
        holder.name.setText(music.getName());
        holder.playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycleViewPlayListener.onPlayListener(music);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView singer;
        private TextView name;
        private LinearLayout playBut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            singer = itemView.findViewById(R.id.search_fragment_song_list_item_singer_name);
            name = itemView.findViewById(R.id.search_fragment_song_list_item_song_name);
            playBut = itemView.findViewById(R.id.search_fragment_song_list_play_but);
        }
    }
}
