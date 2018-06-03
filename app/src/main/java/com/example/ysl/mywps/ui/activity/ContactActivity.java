package com.example.ysl.mywps.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.ContactBean;
import com.example.ysl.mywps.bean.DocumentListBean;
import com.example.ysl.mywps.bean.WpsdetailFinish;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.ui.adapter.ContactAdapter;
import com.example.ysl.mywps.ui.adapter.ContactMyAdapter;
import com.example.ysl.mywps.ui.view.IconTextView;
import com.example.ysl.mywps.utils.CommonSetting;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.MatchesUtil;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.PingYinUtils;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ysl on 2018/1/16.
 */

public class ContactActivity extends BaseActivity implements PasssString{

    @BindView(R.id.contact_listview)
    ListView listView;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.contact_itv_search)
    IconTextView tvSearch;
    @BindView(R.id.contact_et_search)
    EditText etSearch;
    @BindView(R.id.contact_rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.contact_bt_confirm)
    ButtonRectangle btConfirm;
    @BindView(R.id.contact_bt_cancel)
    ButtonRectangle btCancel;
    @BindView(R.id.contact_cb_all)
    CheckBox cbAll;
    @BindView(R.id.contact_tv_all)
    TextView tvAll;


    private ContactMyAdapter adapter;
    private ArrayList<ContactBean> list = new ArrayList<>();
    private ArrayList<ContactBean> searchList = new ArrayList<>();
    private String token = "";
    private String docPath = "";
    private DocumentListBean documentInfo;
    private MyclickListener click = new MyclickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_layout);
        ButterKnife.bind(this);

        docPath = getIntent().getStringExtra("path");
        documentInfo = getIntent().getExtras().getParcelable("documentInfo");

        if(documentInfo.getStatus().equals("4")) {
            rlBottom.setVisibility(View.VISIBLE);
            cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(adapter == null) return;
                    adapter.selectAll(isChecked);

                }
            });


        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                拟稿1-》审核2-》审核通过5-》签署3（不同意）-》审核通过5
                Logger.i("提交人  " + list.get((int) id).getUsername());
                if (documentInfo.getStatus().equals("1")) {
                    commitAudit(list.get((int) id).getUid());
                } else if(documentInfo.getStatus().equals("5")){
                    commitSign(list.get((int) id).getUid());
                }

            }
        });
        token = SharedPreferenceUtils.loginValue(this, "token");
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleText("通讯录");
        netWork();
    }

    /**
     * 提交审核
     */
    private void commitAudit(final String uid) {

        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {

                Call<String> call = HttpUtl.commitAudit("User/Oa/submit_review/", documentInfo.getId(), uid, token, documentInfo.getDoc_name(), docPath);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if(!response.isSuccessful()){
                                emitter.onNext(response.message());
                                return;
                            }
                            Logger.i("contact  " + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());

                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");

                            emitter.onNext(msg);
                            if (code == 0) emitter.onNext("Y");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        emitter.onNext(t.getMessage());
                        emitter.onNext("N");
                    }
                });


            }
        }); //18511234650
        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {

                    if (s.equals("Y")) {
                        EventBus.getDefault().post(new WpsdetailFinish("通讯录提交成功"));
                        finish();
                    }

                } else {
                    ToastUtils.showShort(ContactActivity.this, s);
                }

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 提交文件领导签署
     */
    private void commitSign(final String uid) {
        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {

                Call<String> call = HttpUtl.commitSign("User/Oa/doc_sign/", documentInfo.getId(), token, documentInfo.getDoc_name(), docPath, uid);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(!response.isSuccessful()){
                            emitter.onNext(response.message());
                            return;
                        }

                        try {

                            Logger.i("contact  " + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());

                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");

                            emitter.onNext(msg);
                            if (code == 0) emitter.onNext("Y");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        emitter.onNext(t.getMessage());
                        emitter.onNext("N");
                    }
                });

            }
        }); //18511234650
        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {

                    if (s.equals("Y")) {
                        EventBus.getDefault().post(new WpsdetailFinish("通讯录提交成功"));
                        finish();
                    }

                } else {
                    ToastUtils.showShort(ContactActivity.this, s);
                }

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

/**
 * 获取通讯录联系人
 * */
    private void netWork() {

        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) {

                String token = SharedPreferenceUtils.loginValue(ContactActivity.this, "token");
                Call<String> call = HttpUtl.contact("User/User/contacts/", token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(!response.isSuccessful()){
                            e.onNext(response.message());
                            return;
                        }
                        Logger.i("response " + response.body());
                        String data = response.body().toString();
                        String msg = null;
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int code = jsonObject.getInt("code");
                            msg = jsonObject.getString("msg");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONArray jsonArray1 = object.getJSONArray("contact");

                                for (int j = 0; j < jsonArray1.length(); ++j) {

                                    JSONObject childObject = jsonArray1.getJSONObject(j);
                                    ContactBean bean = gson.fromJson(childObject.toString(), ContactBean.class);
                                    bean.setCapital(PingYinUtils.getPinYinHeadChar(bean.getUsername()));
                                    list.add(bean);
                                }
                            }
                            e.onNext("Y");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            e.onNext(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Logger.i("通讯录  " + t.getMessage());
                        e.onNext(t.getMessage());
                    }
                });

            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y")) {

                    boolean shouldHide = false;
                    if(documentInfo.getStatus().equals("4")){
                        shouldHide = true;
                    }
                    adapter = new ContactMyAdapter(list, ContactActivity.this,shouldHide,ContactActivity.this);
                    listView.setAdapter(adapter);
                } else {
                    ToastUtils.showShort(ContactActivity.this, s);
                }

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);

    }

    /**
     * 转发进入反馈流程
     * */
    private void docForward(final String uids){

        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) {

                Call<String> call = HttpUtl.docForward("User/Oa/doc_forward/",documentInfo.getId(),uids,token);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(!response.isSuccessful()){
                            emitter.onNext(response.message());
                            return;
                        }

                        String data = response.body();
                        Logger.i("反馈流程  "+data);
                        if(CommonUtil.isEmpty(data)) {
                            emitter.onNext("N");
                            emitter.onComplete();
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int code = jsonObject.getInt("code");
                           String msg = jsonObject.getString("msg");

                            emitter.onNext(msg);
                          if(code == 0)  emitter.onNext("Y");
                            else  emitter.onNext("N");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        emitter.onComplete();


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());
                        emitter.onNext("N");
                        emitter.onComplete();

                    }
                });

            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {



                if(s.equals("Y") || s.equals("N")){

                    if(s.equals("Y")) EventBus.getDefault().post(new WpsdetailFinish("转发成功"));
                }else ToastUtils.showShort(getApplicationContext(),s);



            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {


                loading.setVisibility(View.GONE);
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);



    }

    @Override
    public void initView() {


        tvSearch.setOnClickListener(click);
        tvAll.setOnClickListener(click);
        btConfirm.setOnClickListener(click);
        btCancel.setOnClickListener(click);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (CommonUtil.isEmpty(s.toString())) {

                    if (adapter != null) adapter.update(list);
                }

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void setString(String... datas) {

        if(datas.length > 0){

            docForward(datas[0]);
        }

    }


    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            switch (v.getId()) {
                case R.id.contact_itv_search:

                    final String value = etSearch.getText().toString();

                    if (CommonUtil.isEmpty(value)) {
                        ToastUtils.showShort(ContactActivity.this, "请输入姓名或电话号码");
                        return;
                    }
                    final boolean isNumber = MatchesUtil.isInteger(value);
                    searchList.clear();
                    loading.setVisibility(View.VISIBLE);
                    Thread searchThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (ContactBean bean : list) {

                                if (isNumber && bean.getMobile().contains(value)) {

                                    searchList.add(bean);
                                } else {

                                    if (bean.getUsername().contains(value) || PingYinUtils.getPingYin(bean.getUsername()).contains(value)) {
                                        searchList.add(bean);
                                    }

                                }

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                    if (adapter != null) {
                                        adapter.update(searchList);
                                    }
                                }
                            });
                        }
                    });

                    searchThread.setDaemon(true);
                    searchThread.start();
                    break;
                case R.id.contact_bt_confirm:


                    if(adapter != null){
                        adapter.docFroward();
                    }

                    break;

                case R.id.contact_bt_cancel:

                    cbAll.setChecked(false);
                    break;
                case R.id.contact_tv_all:

                    cbAll.setChecked(!cbAll.isChecked());
                    break;


            }

        }
    }
}
