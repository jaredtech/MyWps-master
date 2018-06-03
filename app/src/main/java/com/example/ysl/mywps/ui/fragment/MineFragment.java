package com.example.ysl.mywps.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.activity.LoginActivity;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_rl_loginout)
    RelativeLayout rlLoginOut;
    @BindView(R.id.mine_tv_name)
    TextView mineTvName;

    private MyclickListener click = new MyclickListener();

    @Override
    public void initData() {

    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_mine_layout, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void afterView(View view) {

        rlLoginOut.setOnClickListener(click);
        String name = SharedPreferenceUtils.loginValue(getActivity(),"name");
        mineTvName.setText(name);
    }

    @Override
    public void setKindFlag(int kindFlag) {

    }

    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            switch (v.getId()) {

                case R.id.mine_rl_loginout:

                    SharedPreferenceUtils.loginSave(getActivity(), "token", "");

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

            }

        }
    }
}
