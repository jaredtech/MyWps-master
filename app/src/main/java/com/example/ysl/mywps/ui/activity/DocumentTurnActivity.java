package com.example.ysl.mywps.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SysytemSetting;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class DocumentTurnActivity extends BaseActivity {

    @BindView(R.id.document_ll_inside)
    LinearLayout llInside;

    @BindView(R.id.out_document)
    LinearLayout outDocument;
    @BindView(R.id.query_document)
    LinearLayout queryDocument;
    @BindView(R.id.issue_document)
    LinearLayout issueDocument;

    private MyClickListener click = new MyClickListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_document_turn_layout);
        ButterKnife.bind(this);
       llInside.setOnClickListener(click);
       outDocument.setOnClickListener(click);
       queryDocument.setOnClickListener(click);
       issueDocument.setOnClickListener(click);
    }

    @Override
    public void initView() {

        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleText("公文流转");
    }

    @Override
    public void initData() {

    }

    private class MyClickListener extends NoDoubleClickListener{
        @Override
        public void click(View v) {

            Intent intent = null;
            switch (v.getId()){

                case R.id.document_ll_inside:
                    intent =   new Intent(DocumentTurnActivity.this,StayToDoActivity.class);
                    intent.putExtra(SysytemSetting.WPS_MODE,SysytemSetting.INSIDE_WPS);
                    break;
                case R.id.out_document:
                    intent =   new Intent(DocumentTurnActivity.this,StayToDoActivity
                            .class);
                    intent.putExtra(SysytemSetting.WPS_MODE,SysytemSetting.OUT_WPS);
                    break;
                case  R.id.query_document:
                    intent =   new Intent(DocumentTurnActivity.this,HandleActivity.class);
                    intent.putExtra(SysytemSetting.WPS_MODE,SysytemSetting.HANDLE_WPS);
                    break;
                case R.id.issue_document:
                    intent =   new Intent(DocumentTurnActivity.this,HandleActivity.class);
                    intent.putExtra(SysytemSetting.WPS_MODE,SysytemSetting.ISSUE_WPS);
                    break;

            }

            startActivity(intent);


        }
    }
}
