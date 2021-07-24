package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freeplayer.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import Adapter.SearchSongListViewAdapter;
import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.HttpHelper;
import Helper.MediaPlayerHelper;
import activity.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    RecyclerView songListView;
    ImageButton searchBut;
    EditText editText;
    LinearLayout dialogLayout;
    HttpHelper httpHelper;
    SearchSongListViewAdapter songAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    loadSongs((List<SongInfo>) msg.obj);
                    break;
                case 2:
                    dialogLayout.setVisibility(View.GONE);
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
        View view=inflater .inflate(R.layout.search_song_fragment,container,false);
        songListView=view.findViewById(R.id.search_fragment_search_song_list_view);
        searchBut=view.findViewById(R.id.search_fragment_search_but);
        editText=view.findViewById(R.id.search_fragment_search_name_text);
        dialogLayout=view.findViewById(R.id.search_song_fragment_dialog_layout);
        httpHelper=new HttpHelper();
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    searchMethod();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    try {
                        searchMethod();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        return view;

    }

    private void searchMethod() throws UnsupportedEncodingException {
        dialogLayout.setVisibility(View.VISIBLE);

            httpHelper.doGetBySearch(URLEncoder.encode(editText.getText().toString(),"utf-8"), "KW", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(null, "onFailure: "+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res=response.body().string();
                    Log.d(null, "onResponse: "+res);
                    try{
                        Gson gson=new Gson();
                        JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                        List<SongInfo> songInfos=gson.fromJson(obj.get("data").getAsJsonObject().get("list"), new TypeToken<List<SongInfo>>(){}.getType());
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=songInfos;
                        handler.sendMessage(msg);
                    }catch (Exception e){
                        Log.d(null, "onResponse: "+e.getMessage());
                    }
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);

                }
            });

    }

    private void loadSongs(List<SongInfo> songInfos) {
        songAdapter=new SearchSongListViewAdapter(songInfos);
        songAdapter.setMlistener(new MyInterFaceMgr.onRecycleViewPlayListener() {
            @Override
            public void onPlayListener(SongInfo songInfo) {
                if(getActivity() instanceof MainActivity){
                    MediaPlayerHelper.getInstance(getContext()).getPlayInfo(songInfo);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        songListView.setLayoutManager(layoutManager);
        songListView.setAdapter(songAdapter);
    }
}
