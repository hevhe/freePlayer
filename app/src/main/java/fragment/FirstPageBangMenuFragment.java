package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import Adapter.BangMenuAdapter;
import Adapter.FirstPageSingerListAdapter;
import Dao.KWBangMenuResponseBiz;
import Dao.KwBangMenuDataBiz;
import Dao.KwBangMenuDataDetailBiz;
import Dao.KwSingerListDataDetailBiz;
import Dao.MyInterFaceMgr;
import Dao.PlayingSongList;
import Helper.HttpHelper;
import Helper.KwMusicHelper;
import Helper.MediaPlayerHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirstPageBangMenuFragment extends Fragment {

    RecyclerView bangMenuRecyclerView;
    HttpHelper httpHelper;
    TabLayout tabLayout;
    RecyclerView singerListRecycleView;
    FirstPageSingerListAdapter singerListAdapter;
    List<KwSingerListDataDetailBiz> singerListDataDetailBizs;
    KWBangMenuResponseBiz ps;
    TabLayout singerListTabLayout;
    public final String TAG=this.getClass().getName();
    BangMenuAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initBangMenuTabLayout(getBangMenuTitle(ps.getData()));
                    loadBangMenuRecycleView(ps.getData().get(0).getList());
                    break;
                case 2:
                    loadSingerListDetailRecycleView(singerListDataDetailBizs);
                    break;
            }
        }
    };

    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.first_page_bang_menu_fragment,container,false) ;
        httpHelper=new HttpHelper();
        singerListTabLayout=view.findViewById(R.id.first_page_singer_list_tabLayout);
        singerListRecycleView=view.findViewById(R.id.first_page_singer_list_recycle_view);
        bangMenuRecyclerView=view.findViewById(R.id.first_page_bang_menu_recycle_view);
        tabLayout=view.findViewById(R.id.first_page_bang_menu_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: "+"切换到了"+tabLayout.getSelectedTabPosition());
                loadBangMenuRecycleView(ps.getData().get(tabLayout.getSelectedTabPosition()).getList());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        loadBangMenu();
        singerListTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initSingerListTabLayout();
        singerListTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: "+"切换到了"+singerListTabLayout.getSelectedTabPosition());
                TextView tv=(TextView)((LinearLayout)((LinearLayout)singerListTabLayout.getChildAt(0)).getChildAt(singerListTabLayout.getSelectedTabPosition())).getChildAt(1);
                loadSingerList(tv.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        loadSingerList("ALL");

        return view;
    }

    /**
     * 搜索榜单
    * */
    private void loadBangMenu(){
        httpHelper.doGetByKW(KwMusicHelper.getBangMenuListReqUrl(), new Callback() {
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
                    ps =gson.fromJson(obj, new TypeToken<KWBangMenuResponseBiz>(){}.getType());
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        });
    }

    private void loadBangMenuRecycleView(List<KwBangMenuDataDetailBiz> mlist) {
        adapter=new BangMenuAdapter(mlist);
        adapter.setMlistener(new MyInterFaceMgr.onPlaySonglistListener() {
            @Override
            public void onPlaySonglist(PlayingSongList songList) {
                MediaPlayerHelper.getInstance(getContext()).playSongListMethod(songList);
            }
        });
        bangMenuRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, RecyclerView.HORIZONTAL, false));
        bangMenuRecyclerView.setAdapter(adapter);
    }

    private String[] getBangMenuTitle(List<KwBangMenuDataBiz> ps){
        String[] temp=new String[ps.size()];
        for(int i=0;i<ps.size();i++){
            temp[i]=ps.get(i).getName();
        }
        return temp;
    }

    private void initBangMenuTabLayout(String[] titles){
        for(String title:titles){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }
    private void loadSingerList(String tag){
        httpHelper.doGetByKW(KwMusicHelper.getSingerList(tag), new Callback() {
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
                    singerListDataDetailBizs=gson.fromJson(obj.get("data").getAsJsonObject().get("artistList"), new TypeToken<List<KwSingerListDataDetailBiz>>(){}.getType());
                    handler.sendEmptyMessage(2);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        });
    }
    private void loadSingerListDetailRecycleView(List<KwSingerListDataDetailBiz> mlist) {
        singerListAdapter=new FirstPageSingerListAdapter(mlist);
        singerListAdapter.setMlistener(new MyInterFaceMgr.onPlaySonglistListener() {
            @Override
            public void onPlaySonglist(PlayingSongList songList) {
                MediaPlayerHelper.getInstance(getContext()).playSongListMethod(songList);
            }
        });
        singerListRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL, false));
        singerListRecycleView.setAdapter(singerListAdapter);
    }

    private void initSingerListTabLayout(){
        singerListTabLayout.addTab(singerListTabLayout.newTab().setText("ALL"));
        char c;
        for(c = 'A'; c <= 'Z'; ++c){
            singerListTabLayout.addTab(singerListTabLayout.newTab().setText(String.valueOf(c)));
        }

    }



}
