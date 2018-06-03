package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.ContactBean;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.ToastUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ysl on 2018/1/16.
 */

public class ContactMyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContactBean> list = new ArrayList<>();
    private ArrayList<String> exitContact = new ArrayList<>();
    private PasssString passsString;
    private LinkedHashMap<Integer,ContactBean> selected = new LinkedHashMap<>();

    private boolean shouldHide;

    public ContactMyAdapter(ArrayList<ContactBean> list, Context context, Boolean isHide,PasssString passsString) {

        this.list = list;
        this.context = context;
        this.shouldHide = isHide;
        this.passsString = passsString;
    }

    public void update(ArrayList<ContactBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void selectAll(boolean isSelect){

        if(isSelect){
            for (int i = 0; i < list.size(); ++i){

                selected.put(i,list.get(i));
            }
        }else {
            selected.clear();
        }

        notifyDataSetChanged();
    }

    public void docFroward(){

        String uids = null;

        if(selected.size() == 0){
            ToastUtils.showShort(context,"请选择要转发的人");
            return;
        }

        for (ContactBean bean : selected.values()){

            if(uids == null){
                uids = bean.getUid();
            }else {
                uids += ","+uids;
            }
        }

        passsString.setString(uids);

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_contact_layout, viewGroup, false);
            holder.tvCapital = (TextView) view.findViewById(R.id.contact_tv_capital);
            holder.tvName = (TextView) view.findViewById(R.id.contact_tv_name);
            holder.tvDepart = (TextView) view.findViewById(R.id.contact_tv_num);
            holder.tvPhone = (TextView) view.findViewById(R.id.contact_tv_phone);
            holder.llCapital = (LinearLayout) view.findViewById(R.id.contact_ll_capital);
            holder.checkBox = (CheckBox) view.findViewById(R.id.contact_item_cb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ContactBean info = list.get(i);

        if (exitContact.contains(info.getCapital())) {

            holder.llCapital.setVisibility(View.GONE);
        } else {
            exitContact.add(info.getCapital());
            holder.llCapital.setVisibility(View.VISIBLE);
            holder.tvCapital.setText(info.getCapital());
        }
        if(shouldHide){
            holder.checkBox.setVisibility(View.VISIBLE);
            if(selected.get(i) != null) holder.checkBox.setChecked(true);
            else holder.checkBox.setChecked(false);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){

                        selected.put(i,info);
                    }else {

                        selected.remove(i);
                    }

                }
            });
        }
        holder.tvName.setText(info.getUsername());
        holder.tvDepart.setText(info.getDept_name());
        holder.tvPhone.setText(info.getMobile());
        return view;
    }

    private class ViewHolder {
        TextView tvCapital;
        TextView tvName, tvPhone, tvDepart;
        LinearLayout llCapital;
        CheckBox checkBox;
    }

    private ViewHolder holder;


}
