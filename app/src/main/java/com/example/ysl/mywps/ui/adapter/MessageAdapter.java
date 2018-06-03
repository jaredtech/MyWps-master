package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ysl.mywps.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class MessageAdapter extends MyBaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public MessageAdapter(ArrayList<String> list,Context context) {
        super(list, context);

        this.list = list;
        this.context = context;
    }

    @Override
    public View myView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.message_listview_item_layout, null);
        return view;
    }
}
