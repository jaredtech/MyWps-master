package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.interfaces.TransportCallBack;
import com.example.ysl.mywps.utils.CommonUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ysl on 2018/2/10.
 * 介绍:展示数据
 */

public class TransportAdater extends BaseAdapter {

    private ArrayList<TransportBean>  list;
    private Context context;
    private DisplayImageOptions options;
    private Drawable wps, picture, video, music,unknown,img;
    private TransportCallBack transportCallBack;
    private LinkedHashMap<Integer,TransportBean> selected =  new LinkedHashMap<>();



    public TransportAdater(ArrayList<TransportBean> list, Context context,TransportCallBack transportCallBack) {

        this.list = list;
        this.context = context;
        this.transportCallBack = transportCallBack;
        selected.clear();

        if(context != null){
            wps = context.getResources().getDrawable(R.mipmap.ft_doc_l);
            picture = context.getResources().getDrawable(R.mipmap.ft_jpg_l);
            video = context.getResources().getDrawable(R.mipmap.ft_mov_l);
            music = context.getResources().getDrawable(R.mipmap.myapplication);
            unknown = context.getResources().getDrawable(R.mipmap.ic_type_unknown);
            img =context.getResources().getDrawable(R.mipmap.img_default);

        }

        options = new DisplayImageOptions.Builder()

                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(false)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(false)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//
    }
    public void update(ArrayList<TransportBean> list){
        selected.clear();
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
    private ViewHolder holder;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view == null){

            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_transport_item_layout,parent,false);

            holder.ivIcon = (ImageView) view.findViewById(R.id.documents_item_icon1);
            holder.tvTitle = (TextView) view.findViewById(R.id.documents_item_title1);
            holder.tvDate = (TextView) view.findViewById(R.id.documents_item_time1);
            holder.tvSize = (TextView) view.findViewById(R.id.documents_item_size1);
            holder.tvPath = (TextView) view.findViewById(R.id.documents_item_path);
            holder.cb = (CheckBox) view.findViewById(R.id.documents_item_cb);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(position > list.size()) return;

                    try {

                        if(isChecked){
                            selected.put(position,list.get(position));
                            transportCallBack.setTransports(list.get(position),1);
                        }else {
                            transportCallBack.setTransports(list.get(position),0);
                            selected.remove(position);

                        }
                    }catch (Exception e){}
                }
            });

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if(selected.get(position) != null) ;
        else  holder.cb.setChecked(false);

        if(list.size() > position){
            TransportBean bean = list.get(position);

            if(bean.getName() != null){
                if(bean.getName().endsWith("png") || bean.getName().contains("jpg")){
                    holder.tvTitle.setText(bean.getName());
holder.ivIcon.setBackground(img);
//                    ImageLoader.getInstance().displayImage("file:///"+bean.getPath(), holder.ivIcon, options);
                } else if ( bean.getName().endsWith("docx") || bean.getName().endsWith("doc") || bean.getName().endsWith("txt") || bean.getName().endsWith("pdf")) {
                    holder.ivIcon.setBackground(wps);
                    holder.tvTitle.setText(bean.getName());
                } else if (bean.getName().endsWith("mp4") || bean.getName().endsWith("rmvb")) {
                    holder.ivIcon.setBackground(video);
                    holder.tvTitle.setText(bean.getName());
                } else if (bean.getName().endsWith("apk")) {
                    holder.ivIcon.setBackground(music);
                    holder.tvTitle.setText(bean.getName());
                }else {
                    holder.tvTitle.setText(bean.getName());
                    holder.ivIcon.setBackground(unknown);
                }

            }


            holder.tvSize.setText(CommonUtil.subsSize(bean.getSize()));
            holder.tvDate.setText(bean.getDate());
            holder.tvPath.setText(bean.getPath());
        }

        return view;
    }


    private class ViewHolder {

        CheckBox cb;
        ImageView ivIcon;
        TextView tvTitle,tvDate,tvSize,tvPath;

    }



}
