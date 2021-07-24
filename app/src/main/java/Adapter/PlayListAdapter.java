package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freeplayer.R;
import java.util.List;

import Dao.IPlayListRecycleViewImageButtonListener;
import Dao.PlaySongListBiz;
import myView.RoundImageView;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder>{

    List<PlaySongListBiz> list;
    ViewGroup viewGroup;
    IPlayListRecycleViewImageButtonListener iPlayListRecycleViewImageButtonListener;

    public void setiPlayListRecycleViewImageButtonListener(IPlayListRecycleViewImageButtonListener iPlayListRecycleViewImageButtonListener) {
        this.iPlayListRecycleViewImageButtonListener = iPlayListRecycleViewImageButtonListener;
    }
    public PlayListAdapter(List<PlaySongListBiz> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.play_list_song_list_recycle_view_item, parent, false);
        PlayListAdapter.ViewHolder holder = new PlayListAdapter.ViewHolder(view);
        viewGroup=parent;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PlaySongListBiz playSongListBiz = list.get(position);
        //Glide.with(viewGroup).load(playSongListBiz.getImageUlr()).into(holder.playListImgBut);
        holder.playListName.setText(playSongListBiz.getPlayListName());
        holder.playListImgBut.setImageResource(R.drawable.music);
        holder.playListImgBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPlayListRecycleViewImageButtonListener.OnPlayListRecycleViewImageButtonListener(playSongListBiz.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView playListName;
        RoundImageView playListImgBut;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playListImgBut=itemView.findViewById(R.id.play_list_song_list_recycle_view_item_but);
            playListName=itemView.findViewById(R.id.play_list_song_list_recycle_view_item_text);
        }
     }
}
