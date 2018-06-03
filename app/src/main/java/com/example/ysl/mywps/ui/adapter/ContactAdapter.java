package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.ContactBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContactBean> list;


    public ContactAdapter(ArrayList<ContactBean> list, Context context) {


        this.list =  list;
        this.context = context;

    }
    public void update(ArrayList<ContactBean> list){

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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (i == list.size() - 1) {
//
//            view = LayoutInflater.from(context).inflate(R.layout.contact_last_item_layout, null);
//
//        } else {

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.message_listview_item_layout, null);
                holder = new ViewHolder();

                holder.tvName = (TextView) view.findViewById(R.id.contact_tv_name);
                holder.tvDepart = (TextView) view.findViewById(R.id.contact_tv_num);
                holder.tvPhone = (TextView) view.findViewById(R.id.contact_tv_phone);

                view.setTag(holder);
            }else {

                holder = (ViewHolder) view.getTag();
            }

            final ContactBean info = list.get(i);
            holder.tvName.setText(info.getUsername());
            holder.tvDepart.setText(info.getDept_name());
            holder.tvPhone.setText(info.getMobile());

//        }
        return view;
    }

    private class ViewHolder {

        TextView tvName, tvPhone, tvDepart;
    }

    private ViewHolder holder;


}
