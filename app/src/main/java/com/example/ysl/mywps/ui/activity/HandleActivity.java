package com.example.ysl.mywps.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.fragment.HandleFragment;
import com.example.ysl.mywps.ui.fragment.HasDoFragment;
import com.example.ysl.mywps.utils.SysytemSetting;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HandleActivity extends BaseActivity {


    @BindView(R.id.handle_rl_content)
    RelativeLayout rl_content;

    private String wpsMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handle_layout);
        ButterKnife.bind(this);

        setTitleText("经办查询");
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wpsMode = getIntent().getStringExtra(SysytemSetting.WPS_MODE);
    }

    @Override
    public void initView() {

        HandleFragment fragment = new HandleFragment();
        fragment.setWpsMode(wpsMode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.handle_rl_content,fragment);
        transaction.commit();
    }

    @Override
    public void initData() {

    }
}
