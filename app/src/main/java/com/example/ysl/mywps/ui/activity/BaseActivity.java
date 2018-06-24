package com.example.ysl.mywps.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.view.IconTextView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ysl on 2017/7/26.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private LinearLayout llRoot;

    private TextView tvBack;
    private TextView tvTitle;
    private IconTextView tvRight,tvRight1;
    private LinearLayout llBack;
    private RelativeLayout rlCotent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_title_layout);


        findView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initData();
    }

    @Override
    public Resources getResources() {

        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public abstract void initView();

    public abstract void initData();

    private void findView() {

        tvBack = (TextView) findViewById(R.id.title_tv_back);
        tvTitle = (TextView) findViewById(R.id.title_tv_title);
        tvRight = (IconTextView) findViewById(R.id.title_tv_right);
        tvRight1 = (IconTextView) findViewById(R.id.title_tv_right1);
        llRoot = (LinearLayout) findViewById(R.id.ll_basetitle_root);
        llBack = (LinearLayout) findViewById(R.id.title_ll_back);
        rlCotent = (RelativeLayout) findViewById(R.id.tittle_rl_content);
        llBack.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.INVISIBLE);


    }


    public void showLeftButton(boolean isShow, String back, View.OnClickListener click) {
        if (isShow)
            llBack.setVisibility(View.VISIBLE);
        else
            llBack.setVisibility(View.INVISIBLE);
        if (CommonUtil.isNotEmpty(back)) tvBack.setText(back);
        if (click != null) llBack.setOnClickListener(click);

    }

    public void setTitleContent(int visible) {
        rlCotent.setVisibility(visible);
    }

    public void setTitleText(String text) {
      if(tvTitle.getVisibility() != View.VISIBLE)  tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(text);
    }

    public void setRightText(String text) {

        tvRight.setText(text);
    }

    public void showTilte(boolean isShow, String title) {

        if (isShow) tvTitle.setVisibility(View.VISIBLE);
        else tvTitle.setVisibility(View.INVISIBLE);
        tvTitle.setText(title);
    }

    public void showRight(boolean isShow, String text, View.OnClickListener click) {

        if (isShow) tvRight.setVisibility(View.VISIBLE);
        else tvRight.setVisibility(View.INVISIBLE);

        if (CommonUtil.isNotEmpty(text)) tvRight.setText(text);
        if (click != null) tvRight.setOnClickListener(click);
    }
    public void setRightSize(int size){
        tvRight.setTextSize(size);
    }
    public void showRight1(boolean isShow,int resource,View.OnClickListener click){

        if(isShow)tvRight1.setVisibility(View.VISIBLE);
        else tvRight1.setVisibility(View.GONE);

        if(resource != 0) tvRight1.setText(resource);
        tvRight1.setOnClickListener(click);
    }

    public void showRight(boolean isShow, int resource, View.OnClickListener click) {

        if (isShow) tvRight.setVisibility(View.VISIBLE);
        else tvRight.setVisibility(View.INVISIBLE);

        tvRight.setText(resource);
        if (click != null) tvRight.setOnClickListener(click);
    }
    public void setRightVisible(boolean isShow){
        if (isShow) tvRight.setVisibility(View.VISIBLE);
        else tvRight.setVisibility(View.INVISIBLE);
    }
    public void setRight1Visible(boolean isShow){
        if (isShow) tvRight1.setVisibility(View.VISIBLE);
        else tvRight1.setVisibility(View.INVISIBLE);
    }

    /**
     * 重点是重写setContentView，让继承者可以继续设置setContentView
     * 重写setContentView
     *
     * @param resId
     */
    @Override
    public void setContentView(int resId) {
        View view = getLayoutInflater().inflate(resId, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.ll_basetitle_root);
        if (null != llRoot)
            llRoot.addView(view, lp);

    }


}