package com.example.ysl.mywps.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileType;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.provider.UploadProvider;
import com.example.ysl.mywps.ui.adapter.PagerAdapter;
import com.example.ysl.mywps.ui.fragment.ContactFragment;
import com.example.ysl.mywps.ui.fragment.MessageFragment;
import com.example.ysl.mywps.ui.fragment.MineFragment;
import com.example.ysl.mywps.ui.fragment.WorkFragment;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //    TextView tvText;
    private static final int BACK_CODE = 1;
    private static final String TAG = "aaa";
    @BindView(R.id.main_tv_message)
    TextView tvMessage;
    @BindView(R.id.main_ib_message)
    ImageButton ibMessage;
    @BindView(R.id.main_ll_message)
    LinearLayout llMessage;

    @BindView(R.id.main_tv_work)
    TextView tvWork;
    @BindView(R.id.main_ib_work)
    ImageButton ibWork;
    @BindView(R.id.main_ll_work)
    LinearLayout llWork;

    @BindView(R.id.main_tv_contact)
    TextView tvConact;
    @BindView(R.id.main_ib_contact)
    ImageButton ibContact;
    @BindView(R.id.main_ll_contact)
    LinearLayout llContact;

    @BindView(R.id.main_tv_mine)
    TextView tvMine;
    @BindView(R.id.main_ib_mine)
    ImageButton ibMine;
    @BindView(R.id.main_ll_mine)
    LinearLayout llMine;

//    @BindView(R.id.main_vp_container)
//    ViewPager viewPager;


    private Fragment messageFragment, contactFragment, workFragment, mineFragment;
    private ColorStateList colorNomal, colorSelect;
    private MyclickListener click;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    float x1,x2,y1,y2;
    int currentIndex = 0;
//    private PagerAdapter pagerAdapter;


    //    公文流转  内部公文 代办事项   流程 调专业版
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        click = new MyclickListener();

        llMessage.setOnClickListener(this);
        llWork.setOnClickListener(this);
        llMine.setOnClickListener(this);
        llContact.setOnClickListener(this);



        colorNomal = getResources().getColorStateList(R.color.bottom_normal);
        colorSelect = getResources().getColorStateList(R.color.bottom_selected);
        fragmentManager = getSupportFragmentManager();
        showMessage(1);

    }



    /**
     * 当token过期后跳转到登陆界面
     * */
    private void jumpToLogin(){

        SharedPreferenceUtils.loginSave(this, "token", "");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }



    /**
     * 获取文件类目
     * */
    private void saveFileTypes(final String token){
        final Thread fileTypeThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<String> call = HttpUtl.getFileType("User/Share/file_type/", token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String data = response.body();
                        Logger.i("fileType  " + data);
                        if (CommonUtil.isEmpty(data))
                            return;
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            String msg = object.getString("msg");

                            if(CommonUtil.isNotEmpty(msg) && msg.contains("登陆信息有误") || code == 1){
                                jumpToLogin();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Logger.i("fileType   " + t.getMessage());
                    }
                });
            }
        });
        fileTypeThread.setDaemon(true);
        fileTypeThread.start();
    }


    @Override
    public Resources getResources() {

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void initView() {




//        ArrayList<Fragment> fragments = new ArrayList<>();
//        messageFragment = new MessageFragment();
//        workFragment = new WorkFragment();
//        contactFragment = new ContactFragment();
//        mineFragment = new MineFragment();


//        fragments.add(messageFragment);
//        fragments.add(workFragment);
//        fragments.add(contactFragment);
//        fragments.add(mineFragment);
//
//        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
//        viewPager.setAdapter(pagerAdapter);
//
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("aaa", "position  " + position);
//                showMessage(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


    }


    @Override
    public void initData() {


        final String token = SharedPreferenceUtils.loginValue(this, "token");
        saveFileTypes(token);

    }


    @Override
    protected void onResume() {
        super.onResume();
//        viewPager.setCurrentItem(1);
        writePermission();

//        ContentResolver resolver = this.getContentResolver();
//        TransportBean bean = new TransportBean();
//        bean.setPath("222");
//        bean.setSize("1111");
//        bean.setName("3333");
//        ContentValues values = bean.toContentValues();
//
//        resolver.insert(UploadProvider.CONTENT_URI,values);
//
//
//
//        Cursor cursor = resolver.query(UploadProvider.CONTENT_URI,TransportBean.TRANSPORTBEANS,null,null,null);
//        if(cursor != null){
//
//            ArrayList<TransportBean> list = TransportBean.getTransportBeans(cursor);
//
//            Logger.i("uploadSize  "+list.size());
//        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    private int currentMyIndex = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setTextBack(int index) {

        switch (index) {
//            case 0:
//                tvMessage.setTextColor(colorSelect);
//                tvConact.setTextColor(colorNomal);
//                tvWork.setTextColor(colorNomal);
//                tvMine.setTextColor(colorNomal);
//
//                setTitleText("消息");
//                ibMessage.setBackground(getResources().getDrawable(R.mipmap.icon_message_selected));
//                ibWork.setBackground(getResources().getDrawable(R.mipmap.icon_work_normal));
//                ibContact.setBackground(getResources().getDrawable(R.mipmap.icon_contact_normal));
//                ibMine.setBackground(getResources().getDrawable(R.mipmap.icon_mine_normal));
//
//                break;
            case 1:

                setTitleText("工作");

                tvMessage.setTextColor(colorNomal);
                tvConact.setTextColor(colorNomal);
                tvWork.setTextColor(colorSelect);
                tvMine.setTextColor(colorNomal);

                ibMessage.setBackground(getResources().getDrawable(R.mipmap.icon_message_normal));
                ibWork.setBackground(getResources().getDrawable(R.mipmap.icon_work_selected));
                ibContact.setBackground(getResources().getDrawable(R.mipmap.icon_contact_normal));
                ibMine.setBackground(getResources().getDrawable(R.mipmap.icon_mine_normal));

                currentMyIndex = 1;
                break;
            case 2:
                setTitleText("通讯录");
                tvMessage.setTextColor(colorNomal);
                tvConact.setTextColor(colorSelect);
                tvWork.setTextColor(colorNomal);
                tvMine.setTextColor(colorNomal);


                ibMessage.setBackground(getResources().getDrawable(R.mipmap.icon_message_normal));
                ibWork.setBackground(getResources().getDrawable(R.mipmap.icon_contact_normal));
                ibContact.setBackground(getResources().getDrawable(R.mipmap.icon_contact_select));
                ibMine.setBackground(getResources().getDrawable(R.mipmap.icon_mine_normal));
                currentMyIndex = 2;

                break;
            case 3:
                setTitleText("我的");
                tvMessage.setTextColor(colorNomal);
                tvConact.setTextColor(colorNomal);
                tvWork.setTextColor(colorNomal);
                tvMine.setTextColor(colorSelect);

                ibMessage.setBackground(getResources().getDrawable(R.mipmap.icon_message_normal));
                ibWork.setBackground(getResources().getDrawable(R.mipmap.icon_work_normal));
                ibContact.setBackground(getResources().getDrawable(R.mipmap.icon_contact_normal));
                ibMine.setBackground(getResources().getDrawable(R.mipmap.icon_mine_selected));
                currentMyIndex = 3;
                break;


        }

    }

    /**
     * viewpager下的
     * */
//    private void showMessage(int index) {
//
//
//        switch (index) {
//
//            case 0:
//
//                setTextBack(1);
//
//                break;
//            case 1:
//                setTextBack(2);
//                break;
//
//            case 2:
//                setTextBack(3);
//
//                break;
//            case 3:
//                setTextBack(4);
//                break;
//        }
//
//    }



    public void showMessage(int index){

        fragmentTransaction = fragmentManager.beginTransaction();
        if(currentFragment != null && index != currentMyIndex) fragmentTransaction.hide(currentFragment);
        switch (index){

//            case 0:
//                currentIndex = 0;
//                if(messageFragment == null){
//                    messageFragment = new MessageFragment();
//                    fragmentTransaction.add(R.id.main_rl_container,messageFragment);
//                }else {
//                    fragmentTransaction.show(messageFragment);
//                }
//                setTextBack(0);
//                currentFragment = messageFragment;
//                break;
            case 1:
                currentIndex = 1;
                if(workFragment == null){
                    workFragment = new WorkFragment();
                    fragmentTransaction.add(R.id.main_rl_container,workFragment);
                }else {

                 if(workFragment.isHidden())   fragmentTransaction.show(workFragment);
                }
                currentFragment = workFragment;
                setTextBack(1);
                break;
            case 2:

                currentIndex = 2;
                if(contactFragment == null){
                    contactFragment = new ContactFragment();
                    fragmentTransaction.add(R.id.main_rl_container,contactFragment);
                }else {
                    if(contactFragment.isHidden())       fragmentTransaction.show(contactFragment);
                }
                currentFragment = contactFragment;
                setTextBack(2);
                break;
            case 3:

                currentIndex = 3;
                if(mineFragment == null){

                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.main_rl_container,mineFragment);
                }else {
                    if(mineFragment.isHidden())     fragmentTransaction.show(mineFragment);
                }
                currentFragment = mineFragment;
                setTextBack(3);
                break;

        }
        fragmentTransaction.setCustomAnimations(R.anim.fragment_out,R.anim.fragment_back,R.anim.fragment_out,R.anim.fragment_back);
        fragmentTransaction.commit();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:

                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_UP:

                x2 = ev.getX();
                y2 = ev.getY();


                float reduceX = x1 - x2;
                float reduceY = Math.abs(y1 - y2);

                if(reduceY < 100){

                    if(Math.abs(reduceX) >  50){

                        if(reduceX < 0){
                            if (currentIndex > 1){
                                int movieIndex = currentIndex - 1;
                                showMessage(movieIndex);
                            }

                        }else {
                            if (currentIndex < 3) {
                                int movieIndex = currentIndex+1;
                                showMessage(movieIndex);
                            }
                        }
                    }

                }

                break;


        }



        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id == R.id.main_ll_message || id == R.id.main_ib_message) {
            showMessage(0);

        } else if (id == R.id.main_ll_work || id == R.id.main_ib_work) {

            showMessage(1);
        } else if (id == R.id.main_ll_contact || id == R.id.main_ib_contact) {

            showMessage(2);
        } else if (id == R.id.main_ll_mine || id == R.id.main_ib_mine) {

            showMessage(3);
        }


    }

    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {
            int id = v.getId();
            if (id == R.id.main_ll_message || id == R.id.main_ib_message) {


            } else if (id == R.id.main_ll_work || id == R.id.main_ib_work) {

            } else if (id == R.id.main_ll_contact || id == R.id.main_ib_contact) {

            } else if (id == R.id.main_ll_mine || id == R.id.main_ib_mine) {

            }

        }
    }



    /**
     * 检查存储权限，如果没有就请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void writePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
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
                Log.i("CMCC", "权限被拒绝");
                ToastUtils.showShort(this,"请开启存储权限");
                writePermission();
            }

        } else {

            writePermission();
//            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
