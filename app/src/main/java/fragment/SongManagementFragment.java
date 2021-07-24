package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.freeplayer.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import Adapter.TabFragmentPagerAdapter;


public class SongManagementFragment extends Fragment {

    TabLayout tabLayout;
    private ViewPager viewPager;
    TabFragmentPagerAdapter tabFragmentPagerAdapter;
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.song_management_fragment,container,false);
        Log.d("", "SongManagementFragment: "+"我被创建");
        tabLayout=view.findViewById(R.id.timeline_tablayout);
        viewPager=view.findViewById(R.id.song_management_fragment_viewPager);
        String[] titles={"搜索","本地","下载"};
        List<Fragment> list=new ArrayList<>();
        list.add(new SearchFragment());
        list.add(new LocalMusicListFragment());
        list.add(new DownloadListFragment());
        viewPager.setCurrentItem(0);
        FragmentManager fragmentManager=getChildFragmentManager();
        tabFragmentPagerAdapter=new TabFragmentPagerAdapter(fragmentManager,titles,list);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
