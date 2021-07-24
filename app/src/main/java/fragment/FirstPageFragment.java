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
import Adapter.TabFragmentPagerAdapter;
import com.example.freeplayer.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class FirstPageFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    String titles[]={"排行","推荐"};
    TabFragmentPagerAdapter adapter;
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater .inflate(R.layout.first_page_fragment,container,false) ;
        viewPager=view.findViewById(R.id.first_page_viewPager);
        tabLayout=view.findViewById(R.id.first_page_tabLayout);
        List<Fragment> list=new ArrayList<>();
        list.add(new FirstPageBangMenuFragment());
        list.add(new FirstPageRecommendFragment());
        viewPager.setCurrentItem(0);
        FragmentManager fragmentManager=getChildFragmentManager();
        adapter=new TabFragmentPagerAdapter(fragmentManager,titles,list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
