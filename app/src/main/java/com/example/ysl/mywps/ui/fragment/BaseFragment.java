package com.example.ysl.mywps.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ysl on 2017/7/26.
 */
public abstract class BaseFragment extends Fragment {
    public int kindFlag = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return setView(inflater, container);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onResume(getActivity());

    }

    public abstract void initData();



    public abstract View setView(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterView(view);
    }

    public abstract void afterView(View view);
    public abstract void setKindFlag(int kindFlag);
}
