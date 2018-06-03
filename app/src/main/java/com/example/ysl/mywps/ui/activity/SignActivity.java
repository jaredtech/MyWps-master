package com.example.ysl.mywps.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.view.WritingPadView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class SignActivity extends BaseActivity {
//
//    @BindView(R.id.sign_btn_confirm)
//    Button btnConfirm;
//    @BindView(R.id.sign_btn_cancel)
//    Button btnCancel;
//    @BindView(R.id.sign_writing)
//    WritingPadView writingPadView;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_layout);
        ButterKnife.bind(this);

//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                writingPadView.clear();
//            }
//        });
//        writePermission();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    /**
     * 检查存储权限，如果没有就请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void writePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 11) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;

            Log.i("aaa", " " + granted);
        } else {
            Toast.makeText(SignActivity.this, "请开启存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
