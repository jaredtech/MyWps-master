package com.example.ysl.mywps.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.ysl.mywps.R;

/**
 * Created by Administrator on 2018/2/4 0004.
 */

public class SearchActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setContentView(R.layout.activity_search_layout);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
