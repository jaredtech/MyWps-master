package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
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
import com.example.ysl.mywps.bean.UploadChildFileBean;
import com.example.ysl.mywps.interfaces.UploadCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class FileUploadChildAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UploadChildFileBean> list;
    private Drawable wps, picture, video, music,unknown;
    private DisplayImageOptions options;
    private UploadCallback uploadCallback;
    private LinkedHashMap<Integer,UploadChildFileBean> selected = new LinkedHashMap<>();

    public FileUploadChildAdapter(Context context, ArrayList<UploadChildFileBean> list,UploadCallback uploadCallback){

        this.context = context;
        this.list = list;
        this.uploadCallback = uploadCallback;
        selected.clear();
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

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    selected.clear();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    selected.clear();
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
        ImageView ivIcon;
        TextView tvName, tvDate;
        CheckBox cb;

    }

    ViewHolder holder;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
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
                    if(i > list.size()) return;
                    if(uploadCallback == null) return;
                   try {
                       if(isChecked){

                           selected.put(i,list.get(i));
                           uploadCallback.setUploads(list.get(i),1);
                       }else {
                           uploadCallback.setUploads(list.get(i),0);
                           selected.remove(i);

                       }
                   }catch (Exception e){}

                }
            });
        } else {
            holder = (ViewHolder) view.getTag();
        }
        /**
         * id : 53
         * filename : 1519652477320.mp4
         * filename_qn : video_1519652477320.mp4
         * ctime : 2018-02-26 22:29:41
         * download_url : http://p2c152618.bkt.clouddn.com/video_1519652477320.mp4
         */

        if(selected.get(i) != null) holder.cb.setChecked(true);
        else  holder.cb.setChecked(false);
        UploadChildFileBean bean = list.get(i);

        if (bean.getFilename().endsWith("jpg") || bean.getFilename().endsWith("png") ||  bean.getFilename().endsWith("PNG")) {
            ImageLoader.getInstance().displayImage(bean.getDownload_url(), holder.ivIcon, options);
        } else if (bean.getFilename().endsWith("docx") || bean.getFilename().endsWith("doc") || bean.getFilename().endsWith("txt") || bean.getFilename().endsWith("pdf")) {
            holder.ivIcon.setBackground(wps);
        } else if (bean.getFilename().endsWith("mp4")|| bean.getFilename().endsWith("rmvb") ||bean.getFilename().endsWith("3gp")) {
            holder.ivIcon.setBackground(video);
        } else if (bean.getFilename().endsWith("apk")) {
            holder.ivIcon.setBackground(music);
        }else {
            holder.ivIcon.setBackground(unknown);
        }
        holder.tvName.setText(bean.getFilename());
        holder.tvDate.setText(bean.getCtime());

        return view;
    }

}
