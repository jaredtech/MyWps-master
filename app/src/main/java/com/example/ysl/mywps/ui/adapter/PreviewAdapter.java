package com.example.ysl.mywps.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.DocumentImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyHolder> {
    private static final String TAG = PreviewAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<DocumentImageBean> list;
    private DisplayImageOptions options;
    private Bitmap imgBitmap;
    private Thread imageThread;

    public PreviewAdapter(ArrayList<DocumentImageBean> list, Context context) {

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
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.listview_item_wpcdetail_layout, viewGroup, false);
        return new MyHolder(imageLayout);
    }

    @Override
    public void onBindViewHolder(MyHolder myHolder, @SuppressLint("RecyclerView") final int position) {
        ImageLoader.getInstance().displayImage(list.get(position).getImg(), myHolder.mView, options);
        if (position == 0) {

            if (imageThread == null) {

                imageThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        imgBitmap = ImageLoader.getInstance().loadImageSync((list.get(position).getImg()), options);
                    }
                });
                imageThread.setDaemon(true);
                imageThread.start();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mView;

        MyHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.wpcdetail_iv_item_content);
        }
    }
}
