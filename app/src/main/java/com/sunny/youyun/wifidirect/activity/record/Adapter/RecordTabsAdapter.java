package com.sunny.youyun.wifidirect.activity.record.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 *
 * Created by Sunny on 2017/6/25 0025.
 */

public class RecordTabsAdapter extends FragmentPagerAdapter {

    String[] titles = {"接收记录", "发送记录"};
    private List<Fragment> fragmentList;
    public RecordTabsAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
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
