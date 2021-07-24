package Adapter;


import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private List<Fragment> mlist;

    public TabFragmentPagerAdapter(FragmentManager fm,String [] titles, List<Fragment> list) {
        super(fm);
        this.titles=titles;
        this.mlist = list;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
