package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.UploadBean;
import com.example.ysl.mywps.bean.UploadSlefBean;
import com.example.ysl.mywps.interfaces.UploadCallback;
import com.example.ysl.mywps.ui.view.MatchListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class FileUploadAdapter extends BaseAdapter {

    private ArrayList<UploadSlefBean> list;
    private Context context;
    private UploadCallback uploadCallback;


    public FileUploadAdapter(Context context,ArrayList<UploadSlefBean> list,UploadCallback uploadCallback){

        this.context = context;
        this.list = list;
        this.uploadCallback = uploadCallback;
    }

    public void updateList(ArrayList<UploadSlefBean> list){

        this.list = list;
        notifyDataSetChanged();
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

    private class ViewHolder {
        TextView tvDate;
        MatchListView listView;

    }
    private ViewHolder holder;

    @Override
    public View getView(int postition, View view, ViewGroup viewGroup) {

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_document_layout, viewGroup, false);

            holder.tvDate = (TextView) view.findViewById(R.id.documents_item_sum_date);
            holder.listView = (MatchListView) view.findViewById(R.id.documents_item_listview);

            FileUploadChildAdapter adapter = new FileUploadChildAdapter(context,list.get(postition).getFiles(),uploadCallback);
holder.listView.setTag(adapter);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            FileUploadChildAdapter adapter = new FileUploadChildAdapter(context,list.get(postition).getFiles(),uploadCallback);

            holder.listView.setTag(adapter);
        }
        FileUploadChildAdapter adapter  = (FileUploadChildAdapter) holder.listView.getTag();


        holder.tvDate.setText(list.get(postition).getCtime());
        holder.listView.setAdapter(adapter);

        return view;

    }


}
