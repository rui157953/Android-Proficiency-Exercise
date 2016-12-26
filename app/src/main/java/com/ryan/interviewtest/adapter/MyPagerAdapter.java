package com.ryan.interviewtest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ryan.interviewtest.MainFragment;

import java.util.List;

/**
 * Created by Ryan.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<MainFragment> fragments;
    public MyPagerAdapter(FragmentManager fm, List<MainFragment> fragments) {
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

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getType();
    }
}
