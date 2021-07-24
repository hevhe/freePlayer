package fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Helper.MediaPlayerHelper;
import activity.MainActivity;
import myView.MyListView;
import com.example.freeplayer.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.MusicAdapter;
import Dao.Music;

public class SongListFragment extends Fragment {
    Button button;
    MyListView listView;
    MusicAdapter musicAdapter;
    Bundle savedState;

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.local_song_list_fragment,container,false) ;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
