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
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by ysl on 2018/2/8.
 * 介绍:
 */

public class DocumenChildAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FileListChildBean> list;
    private int kindFlag;
    private Drawable wps, picture, video, music,unknown;
    private DisplayImageOptions options;
    private PassFileChildList passFileChildList;
    private ArrayList<FileListChildBean> selectList = new ArrayList<>();

    public DocumenChildAdapter(ArrayList<FileListChildBean> list, Context context, int kindFlag, PassFileChildList passFileChildList) {

        this.context = context;
        this.list = list;
        this.kindFlag = kindFlag;
        this.passFileChildList = passFileChildList;
        if (context != null) {

            wps = context.getResources().getDrawable(R.mipmap.ft_doc_l);
            picture = context.getResources().getDrawable(R.mipmap.ft_jpg_l);
            video = context.getResources().getDrawable(R.mipmap.ft_mov_l);
            music = context.getResources().getDrawable(R.mipmap.myapplication);
            unknown = context.getResources().getDrawable(R.mipmap.ic_type_unknown);
            options = new DisplayImageOptions.Builder()

                    .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                    .cacheOnDisc(false)//设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
                    .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                    .build();//

        }
        selectList.clear();
    }

    public void update(ArrayList<?> list) {
        this.list = (ArrayList<FileListChildBean>) list;
        selectList.clear();
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
    public View getView(final int postition, View view, ViewGroup viewGroup) {

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listveiw_item_documents_layout, viewGroup, false);
            holder.ivIcon = (ImageView) view.findViewById(R.id.documents_item_icon);
            holder.tvName = (TextView) view.findViewById(R.id.documents_item_title);
            holder.tvDate = (TextView) view.findViewById(R.id.documents_item_time);
            holder.cb = (CheckBox) view.findViewById(R.id.documents_item_cb);

            view.setTag(holder);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectList.clear();
                    if (isChecked) {
                        if (list.size() > postition) selectList.add(list.get(postition));
                        passFileChildList.passFileChild(selectList, 1);
                    } else {
                        if (list.size() > postition) selectList.add(list.get(postition));
                        passFileChildList.passFileChild(selectList, 0);
                    }
                }
            });
        } else {
            holder = (ViewHolder) view.getTag();
        }

        FileListChildBean bean = list.get(postition);

        if (bean.getFilename().endsWith("jpg") || bean.getFilename().endsWith("png") ||  bean.getFilename().endsWith("PNG")) {
            ImageLoader.getInstance().displayImage(bean.getDownload_url(), holder.ivIcon, options);
        } else if (kindFlag == 2 || bean.getFilename().endsWith("docx") || bean.getFilename().endsWith("doc") || bean.getFilename().endsWith("txt") || bean.getFilename().endsWith("pdf")) {
            holder.ivIcon.setBackground(wps);
        } else if (kindFlag == 3 || bean.getFilename().endsWith("mp4")|| bean.getFilename().endsWith("rmvb") ||bean.getFilename().endsWith("3gp")) {
            holder.ivIcon.setBackground(video);
        } else if (kindFlag == 4 || bean.getFilename().endsWith("apk")) {
            holder.ivIcon.setBackground(music);
        }else {
            holder.ivIcon.setBackground(unknown);
        }

        holder.tvName.setText(bean.getFilename());
        holder.tvDate.setText(bean.getCtime());
        return view;
    }


    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvDate;
        CheckBox cb;

    }

    private ViewHolder holder;


}
