package com.example.ysl.mywps.ui.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListBean;
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.bean.UploadBean;
import com.example.ysl.mywps.interfaces.HttpFileCallBack;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.interfaces.TransportCallBack;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.net.ProgressListener;
import com.example.ysl.mywps.provider.DownLoadProvider;
import com.example.ysl.mywps.provider.UploadProvider;
import com.example.ysl.mywps.ui.activity.DocumentDetailActivity;
import com.example.ysl.mywps.ui.adapter.DocumentAdapter;
import com.example.ysl.mywps.ui.adapter.TransportAdater;
import com.example.ysl.mywps.ui.view.MatchListView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.FileUtils;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/2/4 0004.
 */

public class TransportFragmentsFragment extends BaseFragment implements PasssString, PassFileChildList, TransportCallBack {

    @BindView(R.id.fragment_documents_listview)
    MatchListView listView;

    @BindView(R.id.transport_loading)
    LinearLayout loadingContent;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;


    TransportAdater adapter;
    ArrayList<TransportBean> list = new ArrayList<>();
    ArrayList<TransportBean> selectList = new ArrayList<>();
    private int kindFlag;
    private String token;
    private ArrayList<UploadBean> uploadList = new ArrayList<>();
    private ArrayList<UploadBean> loadingBean = new ArrayList<>();
    private UploadBean cunrrentBean;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    private ArrayList<View> downLoadView = new ArrayList<>();
    private ArrayList<View> uploadView = new ArrayList<>();
    private ContentResolver contentResolver;


    @Override
    public void initData() {

        token = SharedPreferenceUtils.loginValue(getActivity(), "token");
        adapter = new TransportAdater(list, getActivity(), this);


    }

    private void getDownloadData() {


        Cursor cursor = contentResolver.query(DownLoadProvider.CONTENT_URI, TransportBean.TRANSPORTBEANS, null, null, null);
        if (cursor != null) {

            list = TransportBean.getTransportBeans(cursor);
            notifyHandler.sendEmptyMessage(111);
        }

        cursor.close();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();

    }

    private Handler notifyHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.update(list);
            Logger.i("list_size "+list.size());
        }
    } ;

    private void getUploadData() {


        Cursor cursor = contentResolver.query(UploadProvider.CONTENT_URI, TransportBean.TRANSPORTBEANS, null, null, null);

        if (cursor != null) {

            list = TransportBean.getTransportBeans(cursor);

            adapter.update(list);
        }
        cursor.close();


    }


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {


        View view = inflater.inflate(R.layout.fragment_transport_fragment_layou, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void afterView(View view) {
        contentResolver = getActivity().getContentResolver();
        loading.setVisibility(View.GONE);

        if (kindFlag == 2) {
//            getUploadData();
        } else if (kindFlag == 1) {
            getDownloadData();
        }

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

//        final UploadBean bean = new UploadBean();
//        bean.setPath(args[0]);
//        bean.setName(args[1]);
//        bean.setType(args[2]);
//        cunrrentBean = bean;
//
//        if (loadingContent != null)
//            addUploadView(bean);

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
                    loadingContent.removeView(view);
                } else {
                    progress.setProgress(pro);

                    if (pro == 100) {

                        loadingContent.removeView(view);
                        tvSize.setText("下载中");
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
//                uploadFile(bean.getPath(), bean.getName(), bean.getType(), token);

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
                                    ContentValues values = bean1.toContentValues();
                                    getActivity().getContentResolver().insert(UploadProvider.CONTENT_URI, values);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getUploadData();
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

    private synchronized void uploadFile(final String path, final String name, String fileType, String token) {


    }

    /***
     *
     * 下载文件
     */
    @Override
    public void passFileChild(ArrayList<FileListChildBean> files, int kind) {

        if (files != null) {
            Logger.i("传递过来的下载文件  " + files.size());
            for (int i = 0; i < files.size(); ++i) {
                FileListChildBean downloadBean = files.get(i);
                if (loadingContent != null) addDownloadView(downloadBean);
            }

//            llProgress1.setVisibility(View.VISIBLE);
//            tvTitle1.setText(downloadBean.getFilename());
//            tvDate1.setText(downloadBean.getDownload_url());
//            tvSize1.setText(downloadBean.getCtime());
//            downLoadFile();
        }

    }


    private void addDownloadView(final FileListChildBean downloadBean) {

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wpsSign" + "/" + downloadBean.getFilename();

        if(getActivity() == null) return;
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_transport_task_layout, null);

        TextView tvTitle =  view.findViewById(R.id.documents_item_title2);
        TextView tvDate = view.findViewById(R.id.documents_item_time2);
        final TextView tvSize =  view.findViewById(R.id.documents_item_size2);

        final ProgressBar progress =  view.findViewById(R.id.transport_prgress_upload2);

        tvTitle.setText(downloadBean.getFilename());
        tvDate.setText(downloadBean.getCtime());
        tvSize.setText("等待中");

        loadingContent.addView(view);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int progr = msg.what;
                if (progr == 0 && msg.obj != null) {
                    String message = msg.obj.toString();
                    if (message.equals("下载成功")) {
                    }
                    ToastUtils.showShort(getActivity(), message);
                } else {
                    progress.setProgress(progr);

                    if (progr == 100) {
                        loadingContent.removeView(view);
                        tvSize.setText("下载完毕");
                    }
                }


            }
        };

        downLoadNetWork(handler, downloadBean, tvSize);

    }

    private void downLoadNetWork(final Handler handler, final FileListChildBean downloadBean, final TextView tvStay) {

        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvStay.setText("下载中");

                    }
                });

                String url = downloadBean.getDownload_url();
                int headIndex = url.indexOf("com/") + 3;
                String headUrl = url.substring(0, headIndex + 1);
                String bodyUrl = url.substring(headIndex + 1);


                Call<ResponseBody> call = HttpUtl.donwoldWps(headUrl, bodyUrl);
                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                            }
                        });


                        if(!response.isSuccessful()){
//                            emitter.onNext(response.message());
                            Message msg = new Message();
                            msg.obj = response.message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                            return;
                        }
                        try {

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wpsSign";

                            File file = new File(path);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String name = downloadBean.getFilename();
                            path = path + "/" + name;
                            file = new File(path);

                            FileUtils.writeFile2Disk(response, file, new HttpFileCallBack() {
                                @Override
                                public void onLoading(long currentLength, long totalLength) {

                                    int precent = (int) (currentLength * 100 / totalLength);

                                    if(precent > 99) try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(precent);


                                }
                            });

                            TransportBean bean = new TransportBean();
                            bean.setName(downloadBean.getFilename());
                            bean.setDate(downloadBean.getCtime());
                            bean.setPath(path);
                            bean.setSize(CommonUtil.getFileSize(file.length()));

                            contentResolver.insert(DownLoadProvider.CONTENT_URI, bean.toContentValues());

                            Message msg = new Message();
                            msg.obj = "下载成功";
                            msg.what = 0;
                            handler.sendMessage(msg);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getDownloadData();
                                }
                            });

                        } catch (Exception e) {
                            Message msg = new Message();
                            msg.obj = "下载失败";
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        Message msg = new Message();
                        msg.obj = msg;
                        msg.what = 0;
                        handler.sendMessage(msg);
//                        emitter.onNext(t.getMessage());
                    }
                });


            }
        });
    }


    @Override
    public void setTransports(TransportBean bean, int kind) {
        switch (kind) {

            case 0:
                try {
                    selectList.remove(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                selectList.add(bean);
                break;

        }
        if (bottomWindow == null) {
            showBottomWindow();
        } else if (!bottomWindow.isShowing()) {
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
                    adapter.notifyDataSetChanged();
                }
            });
            rlMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (selectList.size() > 1) {
                        ToastUtils.showShort(getActivity(), "只能选择一个文件查看信息");
                    } else if (selectList.size() == 0) {
                        ToastUtils.showShort(getActivity(), "请选择文件查看信息");
                    } else if (selectList.size() == 1) {
                        Intent intent = new Intent(getActivity(), DocumentDetailActivity.class);
                        intent.putExtra("flag", "download");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("download", selectList.get(0));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                    bottomWindow.dismiss();

                }
            });

            bottomWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
        } else {

            bottomWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
        }
    }

    private void deleteDocument() {

        if (selectList.size() <= 0) {
            ToastUtils.showShort(getActivity(), "请选择要删除的文件");
            return;
        }
        loading.setVisibility(View.VISIBLE);
final ArrayList<TransportBean> tmpList = selectList;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < tmpList.size(); ++i) {

                    try {
                        contentResolver.delete(DownLoadProvider.CONTENT_URI, TransportBean.NAME + " = ?", new String[]{tmpList.get(i).getName()});

                        Thread.sleep(1000);
                        File file = new File(tmpList.get(i).getPath());
                        file.delete();
                    } catch (Exception e) {
                    }

                }


                Logger.i("deleteSize "+tmpList.size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        selectList.clear();
                        getDownloadData();
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();


    }

}


