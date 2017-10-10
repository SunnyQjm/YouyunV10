package com.sunny.youyun.activity.person_info.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 *
 * Created by Sunny on 2017/6/25 0025.
 */

public class RecordTabsAdapter extends FragmentPagerAdapter {

    String[] titles = {"动态", "关注"};
    private List<Fragment> fragmentList;
    public RecordTabsAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }
    public RecordTabsAdapter(String[] titles, FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
