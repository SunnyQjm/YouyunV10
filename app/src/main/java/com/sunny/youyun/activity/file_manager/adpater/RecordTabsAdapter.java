package com.sunny.youyun.activity.file_manager.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class RecordTabsAdapter extends FragmentPagerAdapter {

    private String[] titles = {"下载记录", "上传记录"};
    private List<Fragment> fragmentList;
    public RecordTabsAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
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
