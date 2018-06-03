package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/23.
 */

public abstract class MyBaseAdapter extends BaseAdapter{

    protected ArrayList<?> list;
    protected Context context;

    public MyBaseAdapter(ArrayList<?> list,Context context){

        this.list = list;
        this.context = context;
    }


    public void update(ArrayList<?> list){

        this.list = list;
       notifyDataSetInvalidated();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return myView(i,view,viewGroup);
    }

    public abstract View myView(int postition,View view ,ViewGroup viewGroup);
}
