package com.example.ysl.mywps.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.ContactBean;
import com.example.ysl.mywps.utils.CommonUtil;
import com.gc.materialdesign.views.ButtonRectangle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class ContactDetailActivity extends BaseActivity {

    @BindView(R.id.contact_detail_iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.contact_detail_tv_uname)
    TextView tvUname;
    @BindView(R.id.contact_detail_tv_id)
    TextView tvId;
    @BindView(R.id.contact_detail_tv_rname)
    TextView tvRname;
    @BindView(R.id.contact_detail_tv_dept)
    TextView tvDept;
    @BindView(R.id.contact_detail_tv_tel)
    TextView tvTel;
    @BindView(R.id.contact_detail_bt_tel)
    ButtonRectangle contactDetailBtTel;

    ContactBean contactBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_detail_layout);
        ButterKnife.bind(this);
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitleText("通讯录详情");

        contactBean = getIntent().getExtras().getParcelable("contact");

    }

    @Override
    public void initView() {

        tvUname.setText(contactBean.getUsername());
        tvId.setText(contactBean.getUid());
        tvRname.setText(contactBean.getRealname());
        tvDept.setText(contactBean.getDept_name());
        tvTel.setText(contactBean.getMobile());


    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.contact_detail_bt_tel)
    public void onViewClicked() {

        try {
            if(CommonUtil.isNotEmpty(contactBean.getMobile())){

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactBean.getMobile()));
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
            checkPermission();
        }
    }


    /**
     * 检查电话，如果没有就请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
//       动态的请求权限
//        ActivityCompat.requestPermissions(getActivity(), new String[]{
//                Manifest.permission.CALL_PHONE
//        }, 11);
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 11) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("CMCC", "权限被允许");
            } else {
                checkPermission();
                Log.i("CMCC", "权限被拒绝");
                Toast.makeText(this, "请开启电话权限", Toast.LENGTH_SHORT).show();
            }
        } else {


//            finish();
        }
    }

}
