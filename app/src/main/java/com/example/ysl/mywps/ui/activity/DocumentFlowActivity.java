package com.example.ysl.mywps.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.ui.view.IconTextView;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class DocumentFlowActivity extends BaseActivity {

    @BindView(R.id.document_doc)
    IconTextView tvIcon;

    private MyclickListener click = new MyclickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.document_flow_layout);
        ButterKnife.bind(this);

        tvIcon.setOnClickListener(click);
        Logger.i("conatct   " + R.string.contact_title);


        showLeftButton(true, null, new NoDoubleClickListener() {
            @Override
            public void click(View v) {
                finish();
            }
        });
        showRight(true, R.string.message_title, new NoDoubleClickListener() {
            @Override
            public void click(View v) {
                initPermission();
            }
        });
        showTilte(true, "内部公文");
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            newFilew();
        }
    }

    private void newFilew() {

        String filesPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android/com.example.ysl.mywps/";
        File file = new File(filesPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            File file1 = new File(filesPath + "新建文件.doc");
            Logger.i("path  " + file1.getAbsolutePath());
            if (file1.exists()) {
                file1.delete();
            }
            Logger.i("新建文件   " + file1.createNewFile());
            /***
             *打开文件管理器
             */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/msword");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * requestCode:相当于一个标志，
     * permissions：需要传进的permission，不能为空
     * grantResults：用户进行操作之后，或同意或拒绝回调的传进的两个参数;
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 这里实现用户操作，或同意或拒绝的逻辑
        /**
         * grantResults会传进
         * android.content.pm

         .PackageManager.PERMISSION_GRANTED 或 android.content.pm

         .PackageManager.PERMISSION_DENIED
         * 前者代表用户同意程序获取系统权限，后者代表用户拒绝程序获取系统权限
         */
        newFilew();

        switch (requestCode) {
            case 1:
                // 处理后,的操作
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    private void testWps(String myPath) {

        Toast.makeText(this, "正在打开", Toast.LENGTH_SHORT).show();

        Log.i("aaa", myPath + " myPath");
        File file = new File(myPath);

        if (!file.exists()) {
            Log.i("aaa", "" + file.mkdirs());
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "Normal");
        //打开模式
        bundle.putBoolean("SendCloseBroad", false);
        //关闭时是否发送广播
        bundle.putString("ThirdPackage", getPackageName());
        //第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean("ClearTrace", false);
        bundle.putBoolean("ShowReviewingPaneRightDefault", true);
        //打开文件时，在最近列表不显示该打开记录
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName("com.kingsoft.moffice_pro", "cn.wps.moffice.documentmanager.PreStartActivity2");
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

            e.printStackTrace();
            Log.i("aaa", " " + e.toString());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            Log.i("aaa", "path   " + path);
            testWps(path);
        }


    }


    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {
            /***
             *打开文件管理器
             */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/msword");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);


        }
    }

}
