package mapsoft.com.costomtopbar.activity;


import android.os.Bundle;

import android.secondbook.com.buttonfragment.R;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;
import java.util.List;

import mapsoft.com.costomtopbar.adapter.TabAdapter;
import mapsoft.com.costomtopbar.fragment.GPSFragment;
import mapsoft.com.costomtopbar.fragment.ReportStationFragment;
import mapsoft.com.costomtopbar.fragment.SIMFragment;
import mapsoft.com.costomtopbar.fragment.SettingFragment;

public class TabLayoutActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    public static final String[] tabTitle = new String[]{"GPS自检", "SIM卡自检", "参数设置", "自动报站"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        initviews();
    }

    private void initviews() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabTitle.length; i++) {
            if (i == 0 ) {
                fragments.add(GPSFragment.newInstance(i));
            }
            if (i == 1 ) {
                fragments.add(SIMFragment.newInstance(i));
            }
            if (i ==2 ) {
                fragments.add(SettingFragment.newInstance(i));
            }
            if (i ==3 ) {
                fragments.add(ReportStationFragment.newInstance(i));
            }

        }
        mTabAdapter = new TabAdapter(getSupportFragmentManager(), fragments);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mTabAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setLayoutParams();
        //设置可以滑动
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //均分
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

}
