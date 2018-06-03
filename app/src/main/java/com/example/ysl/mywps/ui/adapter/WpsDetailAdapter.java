package com.example.ysl.mywps.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.DocumentImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class WpsDetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DocumentImageBean> list;
    private DisplayImageOptions options;
    private Bitmap imgBitmap;
    private Thread imageThread;

    public WpsDetailAdapter(ArrayList<DocumentImageBean> list, Context context) {

        this.list = list;
        this.context = context;

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

    public Bitmap getImgBitmap() {
        return imgBitmap;
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
    public View getView(final int postion, View view, ViewGroup viewGroup) {

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_wpcdetail_layout, viewGroup, false);
            holder.ivIcon = (ImageView) view.findViewById(R.id.wpcdetail_iv_item_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(postion).getImg(), holder.ivIcon, options);

        if (postion == 0) {

            if (imageThread == null) {

                imageThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        imgBitmap = ImageLoader.getInstance().loadImageSync((list.get(postion).getImg()), options);
                    }
                });
                imageThread.setDaemon(true);
                imageThread.start();
            }
        }
        return view;

    }

    private class ViewHolder {

        ImageView ivIcon;
    }


    private ViewHolder holder;



}
