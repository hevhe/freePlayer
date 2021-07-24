package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freeplayer.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import Adapter.FirstPageSongListAdapter;
import Dao.KWSongListDataDetailBiz;
import Dao.KWSongListResponseBiz;
import Dao.MyInterFaceMgr;
import Dao.PlayingSongList;
import Helper.HttpHelper;
import Helper.KwMusicHelper;
import Helper.MediaPlayerHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirstPageRecommendFragment extends Fragment {

    HttpHelper httpHelper;
    RecyclerView songListRecycleView;
    RecyclerView allSongListRecycleView;
    KWSongListResponseBiz kwSongListResponse;
    FirstPageSongListAdapter songListadapter;
    TabLayout songListTabLayout;
    TabLayout allSongListTabLayout;
    List<KWSongListDataDetailBiz> songListDataDetailBizs;
    public final String TAG=this.getClass().getName();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    loadSongListRecycleView(songListDataDetailBizs);
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.first_page_song_list_fragment,container,false) ;
        Log.d("ceshi", "窗口3：我被创建了 ");
        songListTabLayout=view.findViewById(R.id.first_page_song_list_tabLayout);
        allSongListTabLayout=view.findViewById(R.id.first_page_all_song_list_tabLayout);
        songListRecycleView=view.findViewById(R.id.first_page_song_list_recycle_view);
        allSongListRecycleView=view.findViewById(R.id.first_page_all_song_list_recycle_view);
        songListTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        allSongListTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initSongListTabLayout();
        httpHelper=new HttpHelper();
        songListTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: "+"切换到了"+songListTabLayout.getSelectedTabPosition());
                if(songListTabLayout.getSelectedTabPosition()==1){
                    loadSongList("hot");
                    return;
                }
                loadSongList("new");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadSongList("new");

        return view;

    }
    /**
     * 搜索歌单
     * */
    private void loadSongList(String tag){
        httpHelper.doGetByKW(KwMusicHelper.getSongList(tag), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d(TAG, "onResponse: "+res);
                try{
                    Gson gson=new Gson();
                    JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                    //kwSongListResponse =gson.fromJson(obj, new TypeToken<KWSongListResponseBiz>(){}.getType());
                    songListDataDetailBizs=gson.fromJson(obj.get("data").getAsJsonObject().get("data"), new TypeToken<List<KWSongListDataDetailBiz>>(){}.getType());
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        });
    }

    private void loadAllSongList(String tag){
        httpHelper.doGetByKW(KwMusicHelper.getSongList(tag), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d(TAG, "onResponse: "+res);
                try{
                    Gson gson=new Gson();
                    JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                    //kwSongListResponse =gson.fromJson(obj, new TypeToken<KWSongListResponseBiz>(){}.getType());
                    songListDataDetailBizs=gson.fromJson(obj.get("data").getAsJsonObject().get("data"), new TypeToken<List<KWSongListDataDetailBiz>>(){}.getType());
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        });
    }

    private void loadSongListRecycleView(List<KWSongListDataDetailBiz> mlist) {
        songListadapter=new FirstPageSongListAdapter(mlist);
        songListadapter.setMlistener(new MyInterFaceMgr.onPlaySonglistListener() {
            @Override
            public void onPlaySonglist(PlayingSongList songList) {
                MediaPlayerHelper.getInstance(getContext()).playSongListMethod(songList);
            }
        });
        songListRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL, false));
        songListRecycleView.setAdapter(songListadapter);
    }

    private void loadAllSongListRecycleView(List<KWSongListDataDetailBiz> mlist) {
        songListadapter=new FirstPageSongListAdapter(mlist);
        songListRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL, false));
        songListRecycleView.setAdapter(songListadapter);
    }

    private void initAllSongListTabLayout(){
        String[] titles={"最新","最热"};
        for(String title:titles){
            songListTabLayout.addTab(songListTabLayout.newTab().setText(title));
        }
    }

    private void initSongListTabLayout(){
        String[] titles={"最新","最热"};
        for(String title:titles){
            songListTabLayout.addTab(songListTabLayout.newTab().setText(title));
        }
    }

}
