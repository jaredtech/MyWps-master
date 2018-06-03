package com.example.ysl.mywps.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.bean.UploadChildFileBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class DocumentDetailActivity extends BaseActivity {

    @BindView(R.id.documnet_detail_iv_icon)
    ImageView ivIcon;
    @BindView(R.id.document_detail_tv_title)
    TextView tvTitle;
    @BindView(R.id.document_detail_tv_id)
    TextView tvId;

    @BindView(R.id.document_detail_tv_id_name)
    TextView tvIdName;
    @BindView(R.id.contact_detail_tv_time)
    TextView tvTime;
    @BindView(R.id.contact_detail_tv_url)
    TextView tvUrl;

    private FileListChildBean childBean;
    private UploadChildFileBean uploadChildFileBean;
    private TransportBean transportBean;

    private Drawable wps, picture, video, music,unknown;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail_layout);
        ButterKnife.bind(this);

        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitleText("文档详情");

        String flag = getIntent().getStringExtra("flag");


        if(flag.equals("file")){
            childBean = getIntent().getExtras().getParcelable("file");

        }else if(flag.equals("download")){

            transportBean = getIntent().getExtras().getParcelable("download");

        }else {
            uploadChildFileBean = getIntent().getExtras().getParcelable("upload");

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {


        wps = getResources().getDrawable(R.mipmap.ft_doc_l);
        picture = getResources().getDrawable(R.mipmap.ft_jpg_l);
        video = getResources().getDrawable(R.mipmap.ft_mov_l);
        music = getResources().getDrawable(R.mipmap.myapplication);
        unknown = getResources().getDrawable(R.mipmap.ic_type_unknown);
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

        if(childBean != null){
            FileListChildBean bean = childBean;
            if (bean.getFilename().endsWith("jpg") || bean.getFilename().endsWith("png") ||  bean.getFilename().endsWith("PNG")) {
                ImageLoader.getInstance().displayImage(bean.getDownload_url(), ivIcon, options);
            } else if (bean.getFilename().endsWith("docx") || bean.getFilename().endsWith("doc") || bean.getFilename().endsWith("txt") || bean.getFilename().endsWith("pdf")) {
                ivIcon.setBackground(wps);
            } else if (bean.getFilename().endsWith("mp4")|| bean.getFilename().endsWith("rmvb") ||bean.getFilename().endsWith("3gp")) {
                ivIcon.setBackground(video);
            } else if (bean.getFilename().endsWith("apk")) {
                ivIcon.setBackground(music);
            }else {
                ivIcon.setBackground(unknown);
            }

            tvTitle.setText(bean.getFilename());
            tvId.setText(bean.getId());
            tvTime.setText(bean.getCtime());
            tvUrl.setText(bean.getDownload_url());
        }else if(transportBean != null){
TransportBean bean = transportBean;

            if (bean.getName().endsWith("jpg") || bean.getName().endsWith("png") ||  bean.getName().endsWith("PNG")) {
                ImageLoader.getInstance().displayImage("file://" + bean.getPath(), ivIcon, options);
            } else if (bean.getName().endsWith("docx") || bean.getName().endsWith("doc") || bean.getName().endsWith("txt") || bean.getName().endsWith("pdf")) {
                ivIcon.setBackground(wps);
            } else if (bean.getName().endsWith("mp4")|| bean.getName().endsWith("rmvb") ||bean.getName().endsWith("3gp")) {
                ivIcon.setBackground(video);
            } else if (bean.getName().endsWith("apk")) {
                ivIcon.setBackground(music);
            }else {
                ivIcon.setBackground(unknown);
            }

            tvIdName.setText("大小：");
            tvTitle.setText(bean.getName());
            tvId.setText(bean.getSize());
            tvTime.setText(bean.getDate());
            tvUrl.setText(bean.getPath());

        }else {

             UploadChildFileBean bean = uploadChildFileBean;
            if (bean.getFilename().endsWith("jpg") || bean.getFilename().endsWith("png") ||  bean.getFilename().endsWith("PNG")) {
                ImageLoader.getInstance().displayImage(bean.getDownload_url(), ivIcon, options);
            } else if (bean.getFilename().endsWith("docx") || bean.getFilename().endsWith("doc") || bean.getFilename().endsWith("txt") || bean.getFilename().endsWith("pdf")) {
                ivIcon.setBackground(wps);
            } else if (bean.getFilename().endsWith("mp4")|| bean.getFilename().endsWith("rmvb") ||bean.getFilename().endsWith("3gp")) {
                ivIcon.setBackground(video);
            } else if (bean.getFilename().endsWith("apk")) {
                ivIcon.setBackground(music);
            }else {
                ivIcon.setBackground(unknown);
            }

            tvTitle.setText(bean.getFilename());
            tvId.setText(bean.getId());
            tvTime.setText(bean.getCtime());
            tvUrl.setText(bean.getDownload_url());
        }

    }

    @Override
    public void initData() {

    }
}
