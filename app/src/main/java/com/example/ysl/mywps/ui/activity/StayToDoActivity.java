package com.example.ysl.mywps.ui.activity;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.adapter.PagerAdapter;
import com.example.ysl.mywps.ui.fragment.HasDoFragment;
import com.example.ysl.mywps.ui.fragment.StayDoFragment;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SysytemSetting;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class StayToDoActivity extends BaseActivity {

    @BindView(R.id.stay_rl_container)
    ViewPager viewPager;
    @BindView(R.id.stay_tv_stay_do)
    TextView tvStay;
    @BindView(R.id.stay_tv_have_do)
    TextView tvHave;

    @BindView(R.id.stay_rl_stay_do)
    RelativeLayout rlStay;
    @BindView(R.id.stay_rl_have_do)
    RelativeLayout rlHave;


    //    private FragmentManager fragmentManager;
//    private FragmentTransaction beginTransaction;
//    private StayDoFragment stayFragment;
//    private HasDoFragment doFragment;
//    private Fragment currentFragment;
    private MyclickListener click = new MyclickListener();
    private ColorStateList normal, selected;
    private Drawable selctedBack;
    private PagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments;
    private StayDoFragment stayDoFragment;
    private HasDoFragment hasDoFragment;
    private String wpsMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stay_to_do_layout);
        ButterKnife.bind(this);
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wpsMode = getIntent().getStringExtra(SysytemSetting.WPS_MODE);
        initDatas();
        tvStay.setOnClickListener(click);
        tvHave.setOnClickListener(click);

    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    private void initDatas() {

        selected = getResources().getColorStateList(R.color.white_back);
        normal = getResources().getColorStateList(R.color.text_stay);
        tvStay.setOnClickListener(click);
        tvHave.setOnClickListener(click);
        fragments = new ArrayList<>();
        stayDoFragment = new StayDoFragment();
        hasDoFragment = new HasDoFragment();
        stayDoFragment.setWpsMode(wpsMode);
        hasDoFragment.setWpsMode(wpsMode);
        fragments.add(stayDoFragment);
        fragments.add(hasDoFragment);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        setTitleText("代办事项");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showFragment(int position) {
//        beginTransaction = fragmentManager.beginTransaction();
//        if (currentFragment != null) beginTransaction.hide(currentFragment);
        switch (position) {

            case 0:
                setTitleText("代办事项");
                tvStay.setTextColor(selected);
                tvHave.setTextColor(normal);
                rlStay.setVisibility(View.VISIBLE);
                rlHave.setVisibility(View.GONE);

//                if (stayFragment == null) {
//
//                    stayFragment = new StayDoFragment();
//                    beginTransaction.add(R.id.stay_rl_container, stayFragment);
//                } else {
//
//                    if (stayFragment.isHidden()) beginTransaction.show(stayFragment);
//
//                }
//                currentFragment = stayFragment;
                break;
            case 1:
                setTitleText("已办事项");

                tvStay.setTextColor(normal);
                tvHave.setTextColor(selected);
                rlStay.setVisibility(View.GONE);
                rlHave.setVisibility(View.VISIBLE);
//
//                if (doFragment == null) {
//
//                    doFragment = new HasDoFragment();
//                    beginTransaction.add(R.id.stay_rl_container, doFragment);
//                } else {
//
//                    if (doFragment.isHidden()) beginTransaction.show(doFragment);
//
//                }
                break;
        }

//        beginTransaction.commit();

    }


    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            switch (v.getId()) {

                case R.id.stay_tv_stay_do:

                    viewPager.setCurrentItem(0);
                    break;
                case R.id.stay_tv_have_do:
                    viewPager.setCurrentItem(1);

                    break;


            }

        }
    }


}
