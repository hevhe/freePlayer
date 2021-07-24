package Adapter;

import android.content.Context;
import android.graphics.ColorSpace;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freeplayer.R;

import java.util.List;

import Dao.KwBangMenuDataDetailBiz;
import Dao.MyInterFaceMgr;
import Dao.PlayingSongList;
import Helper.MediaPlayerHelper;
import myView.RoundImageView;

public class BangMenuAdapter extends RecyclerView.Adapter<BangMenuAdapter.ViewHolder> {

    MyInterFaceMgr.onPlaySonglistListener mlistener;

    public void setMlistener(MyInterFaceMgr.onPlaySonglistListener mlistener) {
        this.mlistener = mlistener;
    }

    private List<KwBangMenuDataDetailBiz> mlist;
    Context context;

    public BangMenuAdapter(List<KwBangMenuDataDetailBiz> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_song_list_recycle_view_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        context=parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final KwBangMenuDataDetailBiz kw=mlist.get(position);
        holder.textView.setText(kw.getName());
        Glide.with(context).load(kw.getPic()).into(holder.imageView);
        holder.playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onPlaySonglist(new PlayingSongList(kw.getName(),kw.getSourceid(),"BANG"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView imageView;
        private TextView textView;
        private ImageButton playBut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.play_list_song_list_recycle_view_item_but);
            textView=itemView.findViewById(R.id.play_list_song_list_recycle_view_item_text);
            playBut=itemView.findViewById(R.id.recycle_view_item_play_but);
        }


    }


}
