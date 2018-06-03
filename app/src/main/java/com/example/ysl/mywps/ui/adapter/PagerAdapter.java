package com.example.ysl.mywps.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list;
    private FragmentManager mFragmentManager;
    private FragmentTransaction  transaction;


    public PagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
        super(fm);
        mFragmentManager = fm;
        this.list = list;
        transaction = mFragmentManager.beginTransaction();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        list.set(position,fragment);
        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        Fragment fragment = getItem(position);
        transaction.remove(fragment);

    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
