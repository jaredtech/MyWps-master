package com.example.ysl.mywps.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.activity.ColleagueAcitivity;
import com.example.ysl.mywps.ui.activity.DocumentTurnActivity;
import com.example.ysl.mywps.ui.activity.MaterialActivity;
import com.example.ysl.mywps.ui.activity.MeettingActivity;
import com.example.ysl.mywps.ui.activity.MembersActivity;
import com.example.ysl.mywps.ui.activity.ProposalActivity;
import com.example.ysl.mywps.ui.activity.QuestionActivity;
import com.example.ysl.mywps.ui.activity.ReportActivity;
import com.example.ysl.mywps.ui.activity.ThemeActivity;
import com.example.ysl.mywps.ui.activity.WebviewActivity;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.CSCustomServiceInfo;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class WorkFragment extends BaseFragment {

    @BindView(R.id.work_ll_document_turn)
    LinearLayout llTurn;
    @BindView(R.id.work_ll_material)
    LinearLayout llMaterial;
    @BindView(R.id.work_rl_social_condition)
    RelativeLayout rlSocialCondition;
    @BindView(R.id.fragment_work_ll_kefu)
    LinearLayout llKefu;
    @BindView(R.id.work_ll_proposal)
    LinearLayout workLlProposal;
    @BindView(R.id.work_ll_theme_activity)
    LinearLayout llTheme;
    @BindView(R.id.work_ll_meetting)
    LinearLayout llMeeting;
    @BindView(R.id.work_ll_report)
    LinearLayout llReport;
    @BindView(R.id.work_ll_colleagues)
    LinearLayout llColleagues;
    @BindView(R.id.work_ll_quession)
    LinearLayout llQuession;
    @BindView(R.id.work_ll_members)
    LinearLayout llMembers;

    //融云客服信息
    CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
    final CSCustomServiceInfo csInfo = csBuilder.nickName("ysl").build();

    Unbinder unbinder;

    private String token = "";
    private MyclickListener myclickListener = new MyclickListener();

    @Override
    public void initData() {
        token = SharedPreferenceUtils.loginValue(getActivity(), "token");
//        saveFileTypes(token);
    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_work_layout, container, false);
        ButterKnife.bind(this, view);


        return view;
    }


    @Override
    public void afterView(View view) {

        llTurn.setOnClickListener(myclickListener);
        llMaterial.setOnClickListener(myclickListener);
        rlSocialCondition.setOnClickListener(myclickListener);
        llKefu.setOnClickListener(myclickListener);
        workLlProposal.setOnClickListener(myclickListener);
        llTheme.setOnClickListener(myclickListener);
        llMeeting.setOnClickListener(myclickListener);
        llReport.setOnClickListener(myclickListener);
        llColleagues.setOnClickListener(myclickListener);
        llQuession.setOnClickListener(myclickListener);
        llMembers.setOnClickListener(myclickListener);


    }

    @Override
    public void setKindFlag(int kindFlag) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyclickListener extends NoDoubleClickListener {

        @Override
        public void click(View v) {
            Intent intent = null;
            switch (v.getId()) {

                case R.id.work_ll_document_turn:

                    intent = new Intent(getActivity(), DocumentTurnActivity.class);
                    break;

                case R.id.work_ll_material:
                    intent = new Intent(getActivity(), MaterialActivity.class);
                    break;
                case R.id.work_rl_social_condition:

                    intent = new Intent(getActivity(), WebviewActivity.class);
                    break;
                case R.id.fragment_work_ll_kefu:

                    try {
                        RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU152077670318138", "在线客服1", csInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort(getActivity(), "融云登陆异常，请回到登陆界面重新登陆!");
                    }
                    break;
                case R.id.work_ll_proposal:

                    intent = new Intent(getActivity(), ProposalActivity.class);
                    break;
                case R.id.work_ll_theme_activity: //主题活动

                    intent = new Intent(getActivity(), ThemeActivity.class);

                    break;

                case R.id.work_ll_meetting: //会议助手

                    intent = new Intent(getActivity(), MeettingActivity.class);
                    break;
                case R.id.work_ll_report://通知公告

                    intent = new Intent(getActivity(), ReportActivity.class);
                    break;

                case R.id.work_ll_colleagues://同事吧

                    intent = new Intent(getActivity(), ColleagueAcitivity.class);
                    break;

                case R.id.work_ll_quession://问卷调查

                    intent = new Intent(getActivity(), QuestionActivity.class);
                    break;
                case R.id.work_ll_members://委员之家

                    intent = new Intent(getActivity(), MembersActivity.class);

                    break;
//        @BindView(R.id.work_ll_theme_activity)
//        LinearLayout llTheme;
//        @BindView(R.id.work_ll_meetting)
//        LinearLayout llMeeting;
//        @BindView(R.id.work_ll_report)
//        LinearLayout llReport;
//        @BindView(R.id.work_ll_colleagues)
//        LinearLayout llColleagues;
//        @BindView(R.id.work_ll_quession)
//        LinearLayout llQuession;
//        @BindView(R.id.work_ll_members)
//        LinearLayout llMembers;

            }

            if (intent != null) startActivity(intent);
        }
    }
}
