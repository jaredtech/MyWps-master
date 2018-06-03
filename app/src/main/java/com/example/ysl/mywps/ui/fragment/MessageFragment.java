package com.example.ysl.mywps.ui.fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.adapter.MessageAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class MessageFragment extends BaseFragment {

    private ListView listView;
    private MessageAdapter adapter;

    @Override
    public void initData() {


    }


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_message_layout, container, false);

        listView = (ListView) view.findViewById(R.id.message_listview);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            list.add(i + "");
        }
        adapter = new MessageAdapter(list,getActivity());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void afterView(View view) {

    }

    @Override
    public void setKindFlag(int kindFlag) {

    }
}
