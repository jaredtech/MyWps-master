package com.example.ysl.mywps.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileType;
import com.example.ysl.mywps.utils.CommonSetting;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.SysytemSetting;
import com.example.ysl.mywps.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ysl on 2018/1/16.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_et_password)
    EditText etPassword;
    @BindView(R.id.login_et_phone)
    EditText etPhone;
    @BindView(R.id.login_rl_confirm)
    RelativeLayout rlConfirm;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;

    private String name;
    private String password;
    private String identity;
    private boolean havIdentity = false;

    private MyclickListener clik = new MyclickListener();
    private SharedPreferences preferences;

    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        rlConfirm.setOnClickListener(clik);
        loading.setVisibility(View.GONE);
        getLogin();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    private void saveLogin() {


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SysytemSetting.USER_NAME, name);
        editor.putString(SysytemSetting.USER_PASSWORD, password);
        if (!havIdentity)
            editor.putString(SysytemSetting.USER_IDENTITY, identity);

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getLogin() {


        preferences = getSharedPreferences(SysytemSetting.USER_FILE, Context.MODE_PRIVATE);
        String name = preferences.getString(SysytemSetting.USER_NAME, "");
        String passowrd = preferences.getString(SysytemSetting.USER_PASSWORD, "");
        identity = preferences.getString(SysytemSetting.USER_IDENTITY, "");
        String token = preferences.getString(SysytemSetting.USER_TOKEN, "");
        String imToken = preferences.getString(SysytemSetting.ROIM_TOKEN,"");

        if(CommonUtil.isEmpty(imToken)){
            return;
        }
        CommonSetting.HTTP_TOKEN = token;
        if (CommonUtil.isNotEmpty(token)) {
            rongYun(imToken);
            loginSuccess();
        }
        if (CommonUtil.isEmpty(identity)) {
            havIdentity = false;
        } else {
            havIdentity = true;
        }
        if (CommonUtil.isNotEmpty(name)) {
            etPhone.setText(name);
        }
        if (CommonUtil.isNotEmpty(passowrd)) {
            etPassword.setText(passowrd);
        }

    }



    private void doLogin() {
        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                Call<String> call = HttpUtl.login("User/Login/user_login/", name, password, identity);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(!response.isSuccessful()){
                            emitter.onNext(response.message());
                            return;
                        }
                        emitter.onNext(response.body().toString());

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        emitter.onNext(throwable.getMessage());

                    }
                });
            }
        });

        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) {
//
//                {
//                    "code": 0,
//                        "msg": "登录成功",
//                        -"data": {
//                    "token": "94721ad0fb81a31b4fad59808097f290d521fd2a"
//                }
//                }
                Logger.i("登录  " + s);
                try {
                    JSONObject object = new JSONObject(s);

                    int code = object.getInt("code");
                    String msg = object.getString("msg");

                    CommonUtil.showShort(getApplicationContext(), msg);
                    JSONObject childObject = object.getJSONObject("data");
                    if (childObject.has("token")) {


                        String token = childObject.getString("token");
                        String realName = childObject.getString("realname");
                        saveFileTypes(token);
                        getRomiToken(token);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SysytemSetting.USER_TOKEN, token);
                        editor.putString(SysytemSetting.REAL_NAME,realName);
                        editor.commit();
                        loginSuccess();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

//                    CommonUtil.showLong(getApplicationContext(), s);
                }

                loading.setVisibility(View.GONE);

            }


        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

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
                        if(!response.isSuccessful()){

                            return;
                        }

                        String data = response.body();
                        Logger.i("fileType  " + data);
                        if (CommonUtil.isEmpty(data))
                            return;
                        try {
                            JSONObject object = new JSONObject(data);
                            int code = object.getInt("code");
                            JSONArray array = object.getJSONArray("data");
                            if (code == 0) {
                                Gson gson = new Gson();
                                List<FileType> dataList = new ArrayList<FileType>();

                                for (int i = 0; i < array.length(); ++i) {

                                    JSONObject child = array.getJSONObject(i);
                                    FileType bean = gson.fromJson(child.toString(),FileType.class);
                                    dataList.add(bean);
                                }

                                SharedPreferenceUtils.setFileTypeList(getApplicationContext(),dataList);

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

    private void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getRomiToken(final String token){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                Call call = HttpUtl.getRoimToken("http://oa.qupeiyi.cn/user/Rongcloud/getToken/",token);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
// {"code":0,"msg":"登录成功","data":{"token":"37fb685f485667f35ac1ac92bab3fbe5783aa3a3","realname":null}}
                        Logger.i("融云token  "+response.body());

                        if(response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");

                                if(code != 0){

                                    ToastUtils.showShort(LoginActivity.this,msg);
                                }else {
                                    JSONObject child = jsonObject.getJSONObject("data");

                                    String iMtoken = child.getString("token");

//                                    if(realName == null) realName = "";
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(SysytemSetting.ROIM_TOKEN,iMtoken);

                                    editor.commit();
                                    rongYun(iMtoken);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }



                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });



            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    private void rongYun(String imToken){

        try {
            RongIM.init(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        RongIM.connect(imToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

                Log.i(TAG,"onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG,"链接成功" +s);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i(TAG,"链接失败" +errorCode);

            }
        });

    }

    private class MyclickListener extends NoDoubleClickListener {

        @Override
        public void click(View v) {

            switch (v.getId()) {

                case R.id.login_rl_confirm:

                    name = etPhone.getText().toString();
                    password = etPassword.getText().toString();
                    havIdentity = false;
                    if (!havIdentity)
                        identity = JPushInterface.getRegistrationID(LoginActivity.this);
                    Logger.i("identity  " + identity);
                    if (CommonUtil.isEmpty(name)) {
                        CommonUtil.showShort(LoginActivity.this, "请填写手机号");
                        return;
                    }
                    if (CommonUtil.isEmpty(password)) {
                        CommonUtil.showShort(LoginActivity.this, "请填写密码");
                        return;
                    }
                    saveLogin();
                    doLogin();
                    break;


            }

        }
    }
}
