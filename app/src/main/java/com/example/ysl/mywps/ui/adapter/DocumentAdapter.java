package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListBean;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.ui.view.MatchListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class DocumentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FileListBean> list;
    private int kindFlag;
    private Drawable wps, picture, video, music;
    private PassFileChildList passFileChildList;


    public DocumentAdapter(ArrayList<FileListBean> list, Context context, PassFileChildList passFileChildList) {

        this.context = context;
        this.list = list;
        this.passFileChildList = passFileChildList;
        if (context != null) {

            wps = context.getResources().getDrawable(R.mipmap.ft_doc_l);
            picture = context.getResources().getDrawable(R.mipmap.ft_jpg_l);
            video = context.getResources().getDrawable(R.mipmap.ft_mov_l);
            music = context.getResources().getDrawable(R.mipmap.material_music);

        }
    }

    public void setKindFlag(int kindFlag) {
        this.kindFlag = kindFlag;

    }
    public void update(ArrayList<FileListBean> list){

        this.list = list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            DocumenChildAdapter adapter = new DocumenChildAdapter(list.get(postition).getFiles(), context, this.kindFlag, passFileChildList);
            holder.listView.setTag(adapter);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            DocumenChildAdapter adapter = new DocumenChildAdapter(list.get(postition).getFiles(), context, this.kindFlag, passFileChildList);
            holder.listView.setTag(adapter);
        }
        DocumenChildAdapter adapter  = (DocumenChildAdapter) holder.listView.getTag();


        holder.tvDate.setText(list.get(postition).getCtime());
        holder.listView.setAdapter(adapter);
        return view;

    }





}
