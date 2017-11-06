package mapsoft.com.costomtopbar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

import mapsoft.com.costomtopbar.activity.TabLayoutActivity;

/**
 * Created by wy on 2017/6/27.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;


    public TabAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }




    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //设置tablayout标题
    @Override
    public CharSequence getPageTitle(int position) {
        return TabLayoutActivity.tabTitle[position];

    }
}
