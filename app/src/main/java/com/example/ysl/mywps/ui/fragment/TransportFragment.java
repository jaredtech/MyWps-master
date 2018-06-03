package com.example.ysl.mywps.ui.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.ui.adapter.PagerAdapter;
import com.example.ysl.mywps.utils.NoDoubleClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/4 0004.
 */

public class TransportFragment extends BaseFragment implements PassFileChildList,PasssString {

    @BindView(R.id.fragment_transport_viewpager)
    ViewPager viewPager;

    @BindView(R.id.material_ll_download)
    RelativeLayout llDownload;
    @BindView(R.id.material_tv_download)
    TextView tvDownload;
    @BindView(R.id.material_rl_download)
    RelativeLayout rlDownload;

    @BindView(R.id.material_ll_upload)
    RelativeLayout llUpload;
    @BindView(R.id.material_tv_upload)
    TextView tvUpload;
    @BindView(R.id.material_rl_upload)
    RelativeLayout rlUpload;

    private PagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private TransportFragmentsFragment fragment1;
    private TransportFragmentsUpload fragment2;
    private PasssString  passsString2;
    private PassFileChildList passFileChildList;
    private ColorStateList normal, select;

    private MyclickListener click = new MyclickListener();

    @Override
    public void initData() {

    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_tranport_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void afterView(View view) {

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 viewPager.setCurrentItem(0);
            }
        });
        llUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        normal = getResources().getColorStateList(R.color.text_gray);
        select = getResources().getColorStateList(R.color.text_black);

        fragment1 = new TransportFragmentsFragment();
        passFileChildList = fragment1;
        fragment1.setKindFlag(1);

        fragment2 = new TransportFragmentsUpload();
        passsString2 = fragment2;
        fragment2.setKindFlag(2);

        fragments.add(fragment1);
        fragments.add(fragment2);

        pagerAdapter = new PagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBack(0);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBack(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setKindFlag(int kindFlag) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBack(int position) {

        switch (position) {
            case 0:

                tvDownload.setTextColor(select);
                rlDownload.setVisibility(View.VISIBLE);
                rlDownload.setBackground(getResources().getDrawable(R.color.tool_bar));
                tvUpload.setTextColor(normal);
                rlUpload.setBackground(getResources().getDrawable(R.color.text_gray));
                break;
            case 1:
                tvDownload.setTextColor(normal);
                rlDownload.setBackground(getResources().getDrawable(R.color.text_gray));
                tvUpload.setTextColor(select);
                rlUpload.setBackground(getResources().getDrawable(R.color.tool_bar));
                break;


        }

    }



    @Override
    public void passFileChild(ArrayList<FileListChildBean> files, int kind) {
        passFileChildList.passFileChild(files,kind);
    }

    @Override
    public void setString(String... args) {
        passsString2.setString(args);
    }

    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

//            switch (v.getId()) {
//
//                case R.id.material_tv_download:

//                    break;
//                case R.id.material_tv_upload:
//
//                    break;
//
//            }

        }
    }
}
