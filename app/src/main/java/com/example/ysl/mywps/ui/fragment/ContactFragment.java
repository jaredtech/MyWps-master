package com.example.ysl.mywps.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.ContactBean;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.ui.activity.ContactActivity;
import com.example.ysl.mywps.ui.activity.ContactDetailActivity;
import com.example.ysl.mywps.ui.adapter.ContactAdapter;
import com.example.ysl.mywps.ui.view.IconTextView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.MatchesUtil;
import com.example.ysl.mywps.utils.PingYinUtils;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class ContactFragment extends BaseFragment {
    @BindView(R.id.contact_listview)
    ListView listView;

    @BindView(R.id.contact_itv_search)
    IconTextView tvSearch;
    @BindView(R.id.contact_et_search)
    EditText etSearch;


    private ContactAdapter adapter;

    private ArrayList<ContactBean> list = new ArrayList<>();
    private ArrayList<ContactBean> searchList = new ArrayList<>();

    @Override
    public void initData() {
        adapter = new ContactAdapter(list, getActivity());
        netWork();
        checkPermission();
    }


    /**
     * 获取通讯录联系人
     * */
    private void netWork() {


        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) {

                String token = SharedPreferenceUtils.loginValue(getActivity(), "token");
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

                if (s.equals("Y")) {

                    adapter.update(list);
                } else {
                    ToastUtils.showShort(getActivity(), s);
                }

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);

    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_contact_layout, container, false);
        ButterKnife.bind(this,view);





        return view;
    }

    @Override
    public void afterView(View view) {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ContactBean bean = list.get((int) id);

                Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("contact",bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String value = etSearch.getText().toString();

                if (CommonUtil.isEmpty(value)) {
                    ToastUtils.showShort(getActivity(), "请输入姓名或电话号码");
                    return;
                }
                final boolean isNumber = MatchesUtil.isInteger(value);
                searchList.clear();

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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (adapter != null) {
                                    adapter.update(searchList);
                                }
                            }
                        });
                    }
                });

                searchThread.setDaemon(true);
                searchThread.start();

            }
        });

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
        if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
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
                Toast.makeText(getActivity(), "请开启电话权限", Toast.LENGTH_SHORT).show();
            }
        } else {


//            finish();
        }
    }

    @Override
    public void setKindFlag(int kindFlag) {

    }
}
