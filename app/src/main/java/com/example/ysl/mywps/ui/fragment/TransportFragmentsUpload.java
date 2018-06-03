package com.example.ysl.mywps.ui.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListBean;
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.bean.UploadBean;
import com.example.ysl.mywps.bean.UploadChildFileBean;
import com.example.ysl.mywps.bean.UploadSlefBean;
import com.example.ysl.mywps.interfaces.HttpFileCallBack;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.interfaces.UploadCallback;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.net.ProgressListener;
import com.example.ysl.mywps.provider.DownLoadProvider;
import com.example.ysl.mywps.provider.UploadProvider;
import com.example.ysl.mywps.ui.activity.DocumentDetailActivity;
import com.example.ysl.mywps.ui.adapter.FileUploadAdapter;

import com.example.ysl.mywps.ui.view.MatchListView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.FileUtils;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/2/4 0004.
 */

public class TransportFragmentsUpload extends BaseFragment implements PasssString, UploadCallback {

    @BindView(R.id.fragment_documents_listview)
    MatchListView listView;

    @BindView(R.id.transport_loading)
    LinearLayout loadingContent;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;


    private FileUploadAdapter adapter;
    ArrayList<UploadSlefBean> list = new ArrayList<>();
    private int kindFlag;
    private String token;
    private ContentResolver contentResolver;
    private ArrayList<UploadBean> uploadList = new ArrayList<>();
    private ArrayList<UploadBean> loadingBean = new ArrayList<>();
    private UploadBean cunrrentBean;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private ArrayList<UploadChildFileBean> selectList = new ArrayList<>();
//    private ArrayList<UploadBean>

    @Override
    public void initData() {

        token = SharedPreferenceUtils.loginValue(getActivity(), "token");
        adapter = new FileUploadAdapter(getActivity(),list,this);


    }



    private void netWork() {
        list.clear();
        if (loading != null) loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {

                Call<String> call = HttpUtl.selfUpload("User/Share/my_share_files/", token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Logger.i("上传的文件 "+response.body());
                        if (response.isSuccessful()) {
                            String data = response.body();
//                            {"code":0,"msg":"文件列表获取成功","data":[{"ctime":"2018-02-26","files":[{"id":"53","filen
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");

                                if(code != 0){
                                    emitter.onNext("N");
                                    emitter.onNext(msg);
                                    emitter.onComplete();
                                    return;
                                }

                                JSONArray array = jsonObject.getJSONArray("data");
                                Gson gson = new Gson();
                                for (int i = 0; i < array.length(); ++i){

                                    JSONObject object = array.getJSONObject(i);
                                    UploadSlefBean bean = gson.fromJson(object.toString(),UploadSlefBean.class);
                                    list.add(bean);
                                }
                                Logger.i("upload_size "+list.size());
                                emitter.onNext("Y");

                            } catch (JSONException e) {
                                e.printStackTrace();
                                emitter.onNext("N");

                            }

                        } else {
                            String msg = response.message();

                            emitter.onNext(msg);
                            emitter.onNext("N");

                        }
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {

                        emitter.onNext(throwable.getMessage());
                        emitter.onNext("N");
                        emitter.onComplete();
                    }
                });


            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull String s) {
               if(adapter != null && list != null) adapter.updateList(list);
                if(s.equals("Y")){

                }else if (s.equals("N")){

                }else {
                    ToastUtils.showShort(getActivity(), s);
                }


            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {
//                adapter.update(fileListBeens);
//                listView.onRefreshComplete();
                if (loading != null) loading.setVisibility(View.GONE);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


    }




    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {


        View view = inflater.inflate(R.layout.fragment_transport_fragment_layou, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void afterView(View view) {

        loading.setVisibility(View.GONE);

        contentResolver = getActivity().getContentResolver();
        netWork();

        listView.setAdapter(adapter);
    }

    @Override
    public void setKindFlag(int kindFlag) {

        this.kindFlag = kindFlag;
    }


    /***
     * 上传文件
     * */
    @Override
    public void setString(String... args) {

        if(getActivity() == null) return;
        final UploadBean bean = new UploadBean();
        bean.setPath(args[0]);
        bean.setName(args[1]);
        bean.setType(args[2]);
        cunrrentBean = bean;

        if (loadingContent != null)
            addUploadView(bean);

    }

    private void addUploadView(UploadBean bean) {

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_transport_task_layout, null);


        ImageView ivIcon = (ImageView) view.findViewById(R.id.documents_item_icon2);
        TextView tvTitle = (TextView) view.findViewById(R.id.documents_item_title2);
        TextView tvDate = (TextView) view.findViewById(R.id.documents_item_time2);
        final TextView tvSize = (TextView) view.findViewById(R.id.documents_item_size2);
        final ProgressBar progress = (ProgressBar) view.findViewById(R.id.transport_prgress_upload2);
        tvSize.setText("等待中");
        tvTitle.setText(bean.getName());
        tvDate.setText(bean.getPath());
        loadingContent.addView(view);

        File file = new File(bean.getPath());
        if (file != null && file.exists()) {
            tvSize.setText(CommonUtil.getFileSize(file.length()));
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int pro = msg.what;
                if (pro == 0 && msg.obj != null) {
                    String message = msg.obj.toString();
                    ToastUtils.showShort(getActivity(), message);


                } else {
                    progress.setProgress(pro);

                    if (pro == 100) {

                        loadingContent.removeView(view);
                        tvSize.setText("上传中");
                    }
                }
            }
        };

        uploadNetWork(handler, bean, tvSize);

    }

    private void uploadNetWork(final Handler handler, final UploadBean bean, final TextView tvStatus) {

        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvStatus.setText("上传中");
                    }
                });

                ProgressListener progressListener = new ProgressListener() {
                    @Override
                    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {

                        int progress = ((int) (hasWrittenLen * 100 / totalLen));

                        if (progress > 99){
                            try {
                                Thread.sleep(2000);
                                handler.sendEmptyMessage(progress);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else
                        handler.sendEmptyMessage(progress);


                    }
                };


                Call<String> call = HttpUtl.sharedUpload("User/Share/upload_file/", bean.getType(), token, bean.getName(), bean.getPath(), progressListener);
                if (call != null) call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Logger.i("data  " + response.body() + "\n" + response.message());

                        if (response.isSuccessful()) {

                            String data = response.body();
                            try {
                                JSONObject jsonObject = new JSONObject(data);

                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                Message message = new Message();
                                message.what = 0;
                                message.obj = msg;
                                handler.sendMessage(message);

                                if (code == 0) {//上传成功，记录上传的文件

                                    File file = new File(bean.getPath());
                                    String length = CommonUtil.getFileSize(file.length());
                                    TransportBean bean1 = new TransportBean();
                                    bean1.setName(bean.getName());
                                    bean1.setPath(bean.getPath());
                                    bean1.setSize(length);
//                                    ContentValues values = bean1.toContentValues();
//
//                                    getActivity().getContentResolver().insert(UploadProvider.CONTENT_URI, values);

                                    if(getActivity() == null) return;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            netWork();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Message message = new Message();
                                message.what = 0;
                                message.obj = "上传失败";
                                handler.sendMessage(message);

                            }
                        } else {

                            Message message = new Message();
                            message.what = 0;
                            message.obj = "上传失败";
                            handler.sendMessage(message);

                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Logger.i("failure  " + t.getMessage());

                        Message message = new Message();
                        message.what = 0;
                        message.obj = "上传失败";
                        handler.sendMessage(message);

                    }
                });

            }
        });
    }

    /**
     * 获取子菜单选择的item
     *@param kind 1 加 0 删
     * */
    @Override
    public void setUploads(UploadChildFileBean bean, int kind) {

        switch (kind){

            case 0:
                try {
                    selectList.remove(bean);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 1:
                selectList.add(bean);
                break;

        }
        if (bottomWindow == null) {
            showBottomWindow();
        }else if(!bottomWindow.isShowing()){
            showBottomWindow();
        } else if (selectList.size() == 0 && bottomWindow != null && bottomWindow.isShowing()) {
            bottomWindow.dismiss();
        }
    }

    private PopupWindow bottomWindow;

    private void showBottomWindow() {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.material_popupwindow_bottom, null);


        if (bottomWindow == null) {
            bottomWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            RelativeLayout rlDownload = (RelativeLayout) view.findViewById(R.id.material_pop_bottom_download);
            RelativeLayout rlDelete = (RelativeLayout) view.findViewById(R.id.material_pop_bottom_delete);
            RelativeLayout rlMessage = (RelativeLayout) view.findViewById(R.id.material_pop_bottom_message);

            rlDownload.setVisibility(View.GONE);


//            bottomWindow.setBackgroundDrawable(new ColorDrawable());
            bottomWindow.setOutsideTouchable(false);
            bottomWindow.setAnimationStyle(R.style.Popupwindow);
            bottomWindow.setFocusable(false);

            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDocument();
                    bottomWindow.dismiss();

                }
            });
            rlMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(selectList.size() > 1){
                        ToastUtils.showShort(getActivity(),"只能选择一个文件查看信息");
                    }else if(selectList.size() == 0){
                        ToastUtils.showShort(getActivity(),"请选择文件查看信息");
                    }else if(selectList.size() == 1){
                        Intent intent = new Intent(getActivity(), DocumentDetailActivity.class);
                        intent.putExtra("flag","upload");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("upload",selectList.get(0));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }



                    bottomWindow.dismiss();
                    selectList.clear();
                }
            });

            bottomWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
        } else {

            bottomWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
        }
    }


    private void deleteDocument() {

        loading.setVisibility(View.VISIBLE);

        if(selectList.size() <=0){
            ToastUtils.showShort(getActivity(),"请选择要删除的文件");
            return;
        }
        String id = "";
        for (int i = 0; i < selectList.size() ;++ i){
            if("".equals(id)){
                id = selectList.get(i).getId();
            }else {
                id += ","+selectList.get(i).getId();
            }

        }

        final String ids = id;
        Logger.i("ids  "+id);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {


                Call<String> call = HttpUtl.deleteFile("User/Share/del_file/",ids, token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.isSuccessful()) {

                            String data = response.body();

                            Logger.i("删除信息  " + data);
                            try {
                                JSONObject jsonObject = new JSONObject(data);

                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");

                                emitter.onNext(msg);
                                if (code == 0) {
                                    emitter.onNext("Y");
                                } else {
                                    emitter.onNext("N");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        emitter.onNext(throwable.getMessage());
                        emitter.onNext("N");
                    }
                });
            }
        });
        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (loading.getVisibility() == View.VISIBLE) loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {
                    netWork();
                } else {
                    ToastUtils.showLong(getActivity(), s);
                }
                selectList.clear();

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


}


