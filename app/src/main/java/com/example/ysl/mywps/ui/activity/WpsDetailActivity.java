package com.example.ysl.mywps.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.DocumentListBean;
import com.example.ysl.mywps.bean.WpsdetailFinish;
import com.example.ysl.mywps.interfaces.HttpFileCallBack;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.ui.adapter.PreviewAdapter;
import com.example.ysl.mywps.ui.view.DragViewGroup;
import com.example.ysl.mywps.ui.view.MoviewImage;
import com.example.ysl.mywps.ui.view.WritingPadView;
import com.example.ysl.mywps.utils.CommonFun;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.FileUtils;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.SysytemSetting;
import com.example.ysl.mywps.utils.ToastUtils;
import com.example.ysl.mywps.utils.WpsModel;
import com.example.ysl.mywps.utils.WpsUtils;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.lx.fit7.Fit7Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class WpsDetailActivity extends BaseActivity {

    private static final String TAG = WpsDetailActivity.class.getSimpleName();
    public static final int GET_OPTION = 0x0001;

    private String opinion = "";

    @BindView(R.id.wpcdetail_iv_artical)
    ImageView ivArtical;
    @BindView(R.id.wpcdetail_ll_artical)
    LinearLayout llArtival;
    @BindView(R.id.wpcdetail_iv_message)
    ImageView ivMessage;
    @BindView(R.id.wpcdetail_ll_message)
    LinearLayout llMessage;
    @BindView(R.id.wpcdetail_iv_sign)
    ImageView ivSign;
    @BindView(R.id.wpcdetail_ll_sign)
    LinearLayout llSign;
    @BindView(R.id.wpcdetail_iv_send)
    ImageView ivSend;
    @BindView(R.id.wpcdetail_ll_send)
    LinearLayout llSend;
    @BindView(R.id.wpcdetail_iv_icon)
    ImageView ivIcon;
    @BindView(R.id.rlDragContent)
    DragViewGroup rlDragContent;
    @BindView(R.id.wpcdetal_pb_top)
    ProgressBar progressBar;
//    @BindView(R.id.wpcdetail_listview)
//    ListView listView;

//    @BindView(R.id.preview_viewpager)
//    MyViewPager previewViewPager;

    @BindView(R.id.previewList)
    RecyclerView mRecyclerView;
    @BindView(R.id.textPager)
    AppCompatTextView textPager;

    @BindView(R.id.wpcdetail_rl_loading)
    RelativeLayout rlLoading;
    @BindView(R.id.wpcdetail_rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;


    private MyclickListener click = new MyclickListener();
    private WpsBroadCast reciver = new WpsBroadCast();
    private int screenH = 0;
    private DocumentListBean documentInfo = null;
    //    private WpsDetailAdapter adapter;
    private PreviewAdapter adapter;
    private String uploadImagePath = "";
    private String downloadWpsPath = "";
    private String token = "";
    private boolean haveSigned = false;
    private String localImagePath = "";
    private String uploadImageName = "";
    float ivWith;
    float ivHeight;
    private String wpsMode = "";
    private String mAccount = "";


    private SharedPreferences wpsPreference;
//    提交审核后是2 文档返回给拟稿人后是5  提交文件领导签署后是3 签署完成后 成功是4 失败是五，继续提交审核

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressBar.setProgress(msg.what);
            if (msg.what == 100) {
                rlLoading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
//            Log.i("aaa", "progress  " + msg.what);
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };
    private int firstItemPosition = 0;
    private MotionEvent mSignOldPosition;
    private String remoteMd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wpc_details_layout);
        ButterKnife.bind(this);
        int[] screenWH = CommonUtil.getScreenWH(this);
        wpsPreference = getSharedPreferences("wpsStatus", Context.MODE_PRIVATE);

        wpsMode = getIntent().getStringExtra(SysytemSetting.WPS_MODE);

        ivArtical.setOnClickListener(click);
        llArtival.setOnClickListener(click);
        ivMessage.setOnClickListener(click);
        llMessage.setOnClickListener(click);
        ivSign.setOnClickListener(click);
        llSign.setOnClickListener(click);
        ivSend.setOnClickListener(click);
        llSend.setOnClickListener(click);

        rlLoading.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        rlDragContent.setVisibility(View.GONE);

        this.registerReceiver(reciver, new IntentFilter("com.example.ysl.mywps.sign"));
        token = SharedPreferenceUtils.loginValue(this, "token");

        screenH = CommonUtil.getScreenWH(this)[1];
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showRight(true, "流程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WpsDetailActivity.this, FlowActivity.class);
                intent.putExtra("docId", documentInfo.getId());
                startActivity(intent);
            }
        });
        setRightSize(16);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        loading.setVisibility(View.GONE);


        if (wpsMode.equals(SysytemSetting.HANDLE_WPS) || wpsMode.equals(SysytemSetting.ISSUE_WPS) || wpsMode.equals(SysytemSetting.OUT_WPS)) {

            llMessage.setVisibility(View.INVISIBLE);
            llSign.setVisibility(View.INVISIBLE);
            llSend.setVisibility(View.INVISIBLE);
        }

        afterData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {


    }


    @SuppressLint("ClickableViewAccessibility")
    private void afterData() {

        mAccount = SharedPreferenceUtils.loginValue(this, "name");

        Bundle bundle = getIntent().getExtras();
        documentInfo = bundle.getParcelable("documentben");
        setTitleText(documentInfo.getDoc_name());

//        http:\/\/p2c152618.bkt.clouddn.com\/1_测试中文.docx_2.png?v=1517064503"
        Log.d(TAG, "afterData: " + documentInfo.getOpinion());
        adapter = new PreviewAdapter(documentInfo.getDoc_imgs(), this);
        if (documentInfo.getDoc_imgs() != null && documentInfo.getDoc_imgs().size() > 0) {
            String imagePath = documentInfo.getDoc_imgs().get(0).getImg();
            int nameStartIndex = imagePath.lastIndexOf("/") + 1;
            int nameEndIndex = imagePath.lastIndexOf("png") + 3;

            uploadImageName = imagePath.substring(nameStartIndex, nameEndIndex);
            Logger.i("   " + uploadImageName);
            textPager.setText("1/" + documentInfo.getDoc_imgs().size());
        }
        int height = rlContent.getHeight();
        int width = rlContent.getWidth();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    textPager.setText((firstItemPosition + 1) + "/" + documentInfo.getDoc_imgs().size());
//                    if (ivIcon.getVisibility() == View.VISIBLE && mSignOldPosition != null) {
//                        ivIcon.autoMouse(mSignOldPosition);
//                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);

        //只有处理人才会下载文件
        if (mAccount.equals(documentInfo.getNow_nickname()) || mAccount.equals(documentInfo.getNow_username())) {
//            downLoadWps(false);
            if (checkFileExist()) {
                checkMd5AndDownload(false);
            } else {
                downLoadWps(false);
            }
        }

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //TODO 点击消失

                float clickx = event.getX();

                float x1, x2, y1, y2;
                x1 = rlDragContent.getX();
                x2 = rlDragContent.getX() + rlDragContent.getWidth();
                y1 = event.getY();
                y2 = rlDragContent.getY();
                float sumx = Math.abs(x2 - x1);
                float sumy = Math.abs(y2 - y1);
                if (clickx > x1 && clickx < x2 && sumy < 350 && y1 > y2) {
                    rlDragContent.autoMouse(event);
//                    ivIcon.setBackgroundResource(R.drawable.dash1dp);
                    mSignOldPosition = event;
//                    Log.d(TAG, "onTouch: left = " + ivIcon.getX() + " | top = " + ivIcon.getY());
                    return true;
                }
//                if ()
//                ivIcon.setBackgroundColor(Color.TRANSPARENT);
                return false;
            }
        });

    }

    public void downLoadWps(final boolean shouldOpen) {
//        String wpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
//
//        File file = new File(wpsPath);
//
//        boolean shouldUpdate = false;
//
//
//        if (file.exists()) {
//
//            String existsStatus = wpsPreference.getString(documentInfo.getDoc_name(), "");
//            if (CommonUtil.isNotEmpty(existsStatus)) {
//                if (documentInfo.getStatus().equals(existsStatus)) {
//                    downloadWpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
//                    shouldUpdate = false;
//                } else {
//                    shouldUpdate = true;
//                }
//            } else {
//                shouldUpdate = true;
//            }
//        } else {
//            shouldUpdate = true;
//        }
//
//        if (!shouldUpdate) {
//            return;
//        }

        if (checkFileExist()) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
            File file = new File(path);
            file.delete();
        }

        rlLoading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {
                String url = documentInfo.getDoc_url();
                int headIndex = url.indexOf("com/") + 3;
                String headUrl = url.substring(0, headIndex + 1);
                String bodyUrl = url.substring(headIndex + 1);


                Call<ResponseBody> call = HttpUtl.donwoldWps(headUrl, bodyUrl);
                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

                        File file = new File(path);

                        if (!file.exists()) {
                            boolean mkdirs = file.mkdirs();
                            if (!mkdirs){
                                ToastUtils.showShort(WpsDetailActivity.this, "创建文件夹失败，请检查权限");
                                return;
                            }
                        }
                        downloadWpsPath = path + "/" + documentInfo.getDoc_name();
                        file = new File(downloadWpsPath);

                        FileUtils.writeFile2Disk(response, file, new HttpFileCallBack() {
                            @Override
                            public void onLoading(long currentLength, long totalLength) {
                                int precent = (int) (currentLength * 100 / totalLength);
                                handler.sendEmptyMessage(precent);
                                if (precent == 100) {
                                    emitter.onNext("Y");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        emitter.onNext(t.getMessage());
                    }
                });

            }
        });
        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y")) {
                    SharedPreferences.Editor editor = wpsPreference.edit();
                    editor.putString(documentInfo.getDoc_name(), documentInfo.getStatus());
                    editor.apply();
                    ToastUtils.showShort(getApplicationContext(), "文档下载成功");
                    if (shouldOpen) openWps(downloadWpsPath);
                } else {
                    ToastUtils.showShort(getApplicationContext(), s);
                }

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * @param myPath
     */
    private boolean openWps(String myPath) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        switch (wpsMode) {
            case SysytemSetting.HANDLE_WPS:

                bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.READ_ONLY); // 只读模式
                break;
            case SysytemSetting.OUT_WPS:
                bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.READ_ONLY); // 只读模式
                break;
            case SysytemSetting.ISSUE_WPS:
                bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.READ_ONLY); // 只读模式
                break;

            case SysytemSetting.INSIDE_WPS:
                if (documentInfo.getIs_writable() == 1)
                    bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 正常模式
                else
                    bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.READ_ONLY); // 只读模式
                break;
        }
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true);//文件保存是是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        File file = new File(myPath);
        if (!file.exists()) {
            ToastUtils.showShort(WpsDetailActivity.this, "文件为空或者不存在");
            return false;
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = Fit7Utils.getUriForFile(this, file);
        } else {
            uri = Uri.fromFile(file);
        }
        Logger.i("uri path  " + uri.getPath());
        intent.setData(uri);

        intent.setDataAndType(uri, WpsUtils.getMIMEType(file));
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showShort(WpsDetailActivity.this, "打开wps异常,请确认是否安装了WPS");
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 保存签署文件
     */
    private void saveImage() {

//        holder.ivIcon.setDrawingCacheEnabled(true);
//        imgBitmap = holder.ivIcon.getDrawingCache();
        if (adapter == null) {
            return;
        }
        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        loading.setVisibility(View.VISIBLE);
        ivIcon.setDrawingCacheEnabled(true);

        final Bitmap backBitmap = adapter.getImgBitmap();
//        int blackEreaHeigh = 0;
//        int blackEreaWidth = 0;
//        float contentRatio = (float)rlContent.getWidth() / rlContent.getHeight();
//        float bitmapRatio = (float)backBitmap.getWidth() / backBitmap.getHeight();
//        if (contentRatio - bitmapRatio < 0) {
//            int i = rlContent.getWidth() / backBitmap.getWidth() * backBitmap.getHeight();
//            blackEreaHeigh = (rlContent.getHeight() - i) / 2;
//            Log.d(TAG, "saveImage: blackEreaHeigh = " + blackEreaHeigh);
//        }else if (contentRatio - bitmapRatio > 0){
//            int i = rlContent.getHeight() / backBitmap.getHeight() * backBitmap.getWidth();
//            blackEreaWidth = (rlContent.getWidth() - i) / 2;
//            Log.d(TAG, "saveImage: blackEreaWidth = " + blackEreaWidth);
//        }

//        Log.d(TAG, "saveImage: " + ivIcon.getX() + " - " + rlDragContent.getX() + " - " + blackEreaWidth);
//        Log.d(TAG, "saveImage: " + ivIcon.getY() + " - " + rlDragContent.getY() + " - " + blackEreaHeigh);
        final float left = getSignPositionX(ivIcon.getX() + rlDragContent.getX(), backBitmap);
        final float top = getSignPositionY(ivIcon.getY() + rlDragContent.getY(), backBitmap);
//        int i = CommonFun.px2dip(this, 87);
//        int j = CommonFun.px2dip(this, 105);
        if (backBitmap == null || CommonUtil.isEmpty(uploadImageName)) {
            ToastUtils.showShort(WpsDetailActivity.this, "列表中没有图片不能上传图片");
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {
                try {
                    FileInputStream fis = new FileInputStream(localImagePath);
                    Bitmap localBitmap = BitmapFactory.decodeStream(fis);
                    int height = localBitmap.getHeight();
                    int width = localBitmap.getWidth();
                    float scaleW = CommonFun.dip2pxFloat(WpsDetailActivity.this,78.0f) / width;
                    float scaleH = CommonFun.dip2pxFloat(WpsDetailActivity.this,58.0f) / height;
                    Matrix matrix = new Matrix();
//                    matrix.setScale(0.3f, 0.3f);
//                    matrix.postScale(scaleW - 0.15f, scaleH - 0.04f); // w h
                    matrix.setScale(scaleW, scaleH); // w h
                    Log.d("压缩前  ", scaleW + "  " + scaleH);
                    Bitmap forgroundBitmao = Bitmap.createBitmap(localBitmap, 0, 0, width,
                            height, matrix, true);
                    Log.d("压缩后  ", forgroundBitmao.getWidth() + "  " + forgroundBitmao.getHeight());
                    localBitmap.recycle();

//                    Bitmap myBitmap = toConformBitmap(backBitmap, forgroundBitmao, left + 40, top + 15);
                    Bitmap myBitmap = toConformBitmap(backBitmap, forgroundBitmao, left, top);

                    getUploadFileName(documentInfo.getDoc_imgs().get(firstItemPosition).getImg());
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + uploadImageName;
                    Log.d(TAG, "subscribe: " + path);
                    Log.d(TAG, "subscribe: " + uploadImageName);
                    saveDownload(path, myBitmap);
                    uploadImagePath = path;

                    e.onNext("保存签收图片成功");
                    e.onNext("Y");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.onNext("保存签收图片失败");
                }
            }
        });
        Consumer<String> oberver = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y")) {
                    signCompleted("签署成功", "2");
                }
                ToastUtils.showShort(WpsDetailActivity.this, s);
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oberver);
    }


    /**
     * 签署成功完成返回给拟稿人
     */
    private void signCompleted(final String opinion, final String signed) {


        loading.setVisibility(View.VISIBLE);
        Log.d(TAG, "signCompleted: " + uploadImageName);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<String> emitter) throws Exception {
                Call<String> call = HttpUtl.signedCommit("User/Oa/back_signed_doc/", documentInfo.getProce_id(), documentInfo.getId(), opinion, signed, uploadImageName, uploadImagePath, token);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }

                        try {
                            String msg = response.body();
                            Logger.i("commit  " + msg);
                            if (CommonUtil.isEmpty(msg)) {
                                return;
                            }
                            Logger.i("commitSign  " + msg);
                            JSONObject jsonObject = new JSONObject(msg);
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("msg");

                            emitter.onNext(message);
                            if (code == 0) {
                                emitter.onNext("Y");
                            } else {
                                emitter.onNext("N");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onNext(e.getMessage());

                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());


                    }
                });
            }
        });


        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {


                    if (s.equals("Y")) {
//                        File file = new File(uploadImagePath);
//                        if (file.exists()) {
//                            file.delete();
//                        }
                        onEvent(new WpsdetailFinish("结束"));
                    }
                } else {
                    ToastUtils.showLong(getApplicationContext(), s);
                }

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 签署失败返回给拟稿人
     */
    private void signUnCompleted(final String opinion, final String signed) {

//        if (CommonUtil.isEmpty(uploadIamgePath)) {
//            ToastUtils.showShort(this, "图片保存失败，请重新点击信息按钮");
//            return;
//        }
        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {

                Call<String> call = HttpUtl.signedCommit("User/Oa/back_signed_doc/", documentInfo.getProce_id(), documentInfo.getId(), opinion, signed, documentInfo.getDoc_name(), downloadWpsPath, token);
                Logger.i("commit  " + opinion);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }

                        try {
                            String msg = response.body();
                            Logger.i("commit  " + msg);
                            if (CommonUtil.isEmpty(msg)) {
                                return;
                            }
                            Logger.i("commitSign  " + msg);
                            JSONObject jsonObject = new JSONObject(msg);
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("msg");

                            emitter.onNext(message);
                            if (code == 0) {
                                emitter.onNext("Y");
                            } else {
                                emitter.onNext("N");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onNext(e.getMessage());

                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());


                    }
                });
            }
        });


        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {


                    if (s.equals("Y")) {
                        EventBus.getDefault().post(new WpsdetailFinish("commit 提交成功"));
                        finish();
                    }
                } else {
                    ToastUtils.showLong(getApplicationContext(), s);
                }

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 合成bitmap
     */
    private Bitmap toConformBitmap(Bitmap background, Bitmap foreground, float left, float top) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //int fgWidth = foreground.getWidth();
        //int fgHeight = foreground.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
//        Log.d(TAG, "toConformBitmap: w = " + bgWidth + " h = " + bgHeight);
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        cv.drawBitmap(foreground, left, top, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.restore();//存储
        return newbmp;
    }

    /**
     * 保存画板
     * 保存图片
     */
    public void saveDownload(String path, Bitmap myBitmap) {


        Bitmap bitmap = myBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            try {
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private PopupWindow popupWindow;
    private WritingPadView writingPadView;
    private TextView tvCancel;
    private TextView tvClear;
    private TextView tvHint;

    RelativeLayout rlSignConfirm;
    float alpha = 1;

    private void setSign() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();


        if (popupWindow == null) {

            View view = LayoutInflater.from(this).inflate(R.layout.sign_layout, null);

            writingPadView = (WritingPadView) view.findViewById(R.id.sign_writing);
            tvCancel = (TextView) view.findViewById(R.id.sign_tv_cancel);
            tvClear = (TextView) view.findViewById(R.id.sign_tv_clear);
            tvHint = (TextView) view.findViewById(R.id.sign_tv_hint);
            rlSignConfirm = (RelativeLayout) view.findViewById(R.id.sign_rl_confirm);
            popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, (int) (screenH * 0.6), true);

            writingPadView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (tvHint != null && tvHint.getVisibility() == View.VISIBLE) {
                        tvHint.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            // 菜单背景色。加了一点透明度
//            ColorDrawable dw = new ColorDrawable(0xddffffff);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            // 设置背景半透明
//            view.setAlpha(0.7f);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.Popupwindow);
//        popupWindow.setWidth();
            popupWindow.setFocusable(true);
            //TODO 注意：这里的 R.layout.activity_main，不是固定的。你想让这个popupwindow盖在哪个界面上面。就写哪个界面的布局。这里以主界面为例
            popupWindow.showAtLocation(LayoutInflater.from(this).inflate(R.layout.activity_wpc_details_layout, null),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            rlAll.setVisibility(View.VISIBLE);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //此处while的条件alpha不能<= 否则会出现黑屏
                            while (alpha < 1f) {
                                try {
                                    Thread.sleep(4);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
//                                Log.d("HeadPortrait", "alpha:" + alpha);
                                Message msg = mHandler.obtainMessage();
                                msg.what = 1;
                                alpha += 0.01f;
                                msg.obj = alpha;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }).start();

//                    rlAll.setVisibility(View.GONE);
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    rlDragContent.setVisibility(View.GONE);
                }
            });
            tvClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlDragContent.setVisibility(View.GONE);
                    writingPadView.clear();
                }
            });
            rlSignConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Thread saveThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wpsSign";
                            File file = new File(path);
                            if (!file.exists()) file.mkdirs();
                            path += File.separator + "qianming.png";
                            try {
                                writingPadView.save(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Logger.i("原始   " + path);


                            final String mypath = path;
                            localImagePath = mypath;
                            WpsDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rlDragContent.setVisibility(View.VISIBLE);

                                    ImageLoader.getInstance().displayImage("file://" + mypath, ivIcon);
                                    popupWindow.dismiss();
                                }
                            });


                        }
                    });
                    saveThread.setDaemon(true);
                    saveThread.start();
                }
            });


        } else {
//            rlAll.setVisibility(View.VISIBLE);
            popupWindow.showAtLocation(LayoutInflater.from(this).inflate(R.layout.activity_wpc_details_layout, null),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }


    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    private class WpsBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "mybitmap", Toast.LENGTH_SHORT).show();
            Logger.i("static  " + CommonUtil.myPath);

        }


    }

    @Subscribe
    public void onEvent(WpsdetailFinish finish) {
        Logger.i("finishe " + finish.getMsg());

        Intent intent = new Intent(this, StayToDoActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unregisterReceiver(reciver);
    }


    /**
     * 检查是否应该下载当前文件
     */
    private boolean checkFileExist() {

        String wpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
        Log.d(TAG, "checkFileExist: " + documentInfo.getDoc_name());
        File file = new File(wpsPath);

        //            String existsStatus = wpsPreference.getString(documentInfo.getDoc_name(), "");
//            if (CommonUtil.isNotEmpty(existsStatus)) {
//                if (documentInfo.getStatus().equals(existsStatus)) {
//                    downloadWpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
//                    shouldUpdate = false;
//                } else {
//                    shouldUpdate = true;
//                }
//            } else {
//                shouldUpdate = true;
//            }
        if (file.exists()) {
            downloadWpsPath = wpsPath;
        }
        return file.exists();
    }

    private void checkMd5AndDownload(final boolean needOpen) {
        final String wpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
        File file = new File(wpsPath);
        final String md5Value = CommonFun.getMD5Three(file);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                Call<String> call = HttpUtl.getDocumentMd5("User/Oa/get_doc_md5/", token, documentInfo.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }
                        String msg = response.body();
                        Logger.i("commit  " + msg + "   " + md5Value);
                        try {
                            JSONObject jsonObject = new JSONObject(msg);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("msg");
                            JSONObject data = jsonObject.getJSONObject("data");
                            remoteMd5 = data.getString("md5");
                            if (code == 0) {
                                if (remoteMd5.equals(md5Value)) {
                                    emitter.onNext("N");
                                }else {
                                    emitter.onNext("Y");
                                }
                            } else {
                                emitter.onNext(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onNext(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());
                    }
                });
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (s.equals("Y")) {
                    downLoadWps(needOpen);
                }else if (s.equals("N")){
                    if (needOpen) {
                        openWps(wpsPath);
                    }
                }else {
                    ToastUtils.showShort(WpsDetailActivity.this, s);
                }
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            int id = v.getId();

            if (id == R.id.wpcdetail_iv_artical || id == R.id.wpcdetail_ll_artical) {

                if (checkFileExist()) {
//                    ToastUtils.showShort(WpsDetailActivity.this, "正在下载");
                    checkMd5AndDownload(true);
//                    downLoadWps(true);
                } else {
                    downLoadWps(true);
                }
//                if(!mAccount.equals(documentInfo.getNow_nickname()) && !mAccount.equals(documentInfo.getNow_username())){
//                    if(checkFileExist()){
//                        ToastUtils.showShort(WpsDetailActivity.this,"正在下载");
//                        downLoadWps(true);
//                    }else {
//                        openWps(downloadWpsPath);
//                    }
////                    ToastUtils.showShort(WpsDetailActivity.this,"只有处理人才能查看文件");
//                    return;
//                }else {
//                    if(checkFileExist()){
//                        ToastUtils.showShort(WpsDetailActivity.this,"正在下载");
//                        downLoadWps(true);
//                    }else {
//                        openWps(downloadWpsPath);
//                    }
//
//                }

            } else if (id == R.id.wpcdetail_iv_message || id == R.id.wpcdetail_ll_message) {

//                if (documentInfo.getStatus().equals("1")) {
////               //                拟稿1-》审核2-》审核通过5-》签署3（不同意）-》审核通过4

//     1 拟文 2 审核  3 签署  4转发  5审核通过  6 反馈阶段
//                    ToastUtils.showShort(WpsDetailActivity.this, "文档当前在拟稿状态");
//                    return;
//                }
                    Intent intent = new Intent(WpsDetailActivity.this, CommitActivity.class);
                    intent.putExtra("wpspath", downloadWpsPath);
                    intent.putExtra("opinion", opinion);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("documentInfo", documentInfo);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, GET_OPTION);
            } else if (id == R.id.wpcdetail_iv_sign || id == R.id.wpcdetail_ll_sign) {

                if (!mAccount.equals(documentInfo.getNow_nickname()) && !mAccount.equals(documentInfo.getNow_username())) {
                    ToastUtils.showShort(WpsDetailActivity.this, "只有处理人才能签署文件");
                    return;
                }

                if (!documentInfo.getStatus().equals("3")) {
                    ToastUtils.showShort(WpsDetailActivity.this, "只有签署阶段才能签署文件");
                    return;
                }

                haveSigned = true;
                setSign();
            } else if (id == R.id.wpcdetail_iv_send || id == R.id.wpcdetail_ll_send) {
                if (!mAccount.equals(documentInfo.getNow_nickname()) && !mAccount.equals(documentInfo.getNow_username())) {
                    ToastUtils.showShort(WpsDetailActivity.this, "只有处理人才能发送文件");
                    return;
                }
                if (documentInfo.getStatus().equals("1") || documentInfo.getStatus().equals("5") || documentInfo.getStatus().equals("4")) {
                    Intent intent = new Intent(WpsDetailActivity.this, ContactActivity.class);
                    intent.putExtra("path", downloadWpsPath);
                    Log.d(TAG, "click: " + downloadWpsPath);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("documentInfo", documentInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (documentInfo.getStatus().equals("2")) {
                    if (opinion == null || opinion.equals("")) {
                        ToastUtils.showShort(WpsDetailActivity.this, "请填写意见");
                        return;
                    }
                    uploadFile(opinion);
                } else if (documentInfo.getStatus().equals("3")) {
                    if (!haveSigned || CommonUtil.isEmpty(localImagePath)) {
                        if (opinion.equals("")) {
                            ToastUtils.showShort(WpsDetailActivity.this, "请填写意见或者签署文件");
                        } else {
                            signUnCompleted(opinion, "1");
                        }
                    } else {
                        if (adapter != null) {
                            adapter.loadImage(firstItemPosition, loadImageHandler);
                        } else {
                            ToastUtils.showShort(WpsDetailActivity.this, "图片没有加载或者为空");
                        }
                    }
//                    saveImage();
                } else if (documentInfo.getStatus().equals("6")) {
                    feedBack(opinion);
                } else {
                    ToastUtils.showShort(WpsDetailActivity.this, "该文档所在流程不能进入通讯录");
                }

            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler loadImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                saveImage();
            }
        }
    };

    public void getUploadFileName(String imagePath) {
        int nameStartIndex = imagePath.lastIndexOf("/") + 1;
        int nameEndIndex = imagePath.lastIndexOf("png") + 3;

        uploadImageName = imagePath.substring(nameStartIndex, nameEndIndex);
    }

    public float getSignPositionX(float left, Bitmap bitmap) {
        return left / rlContent.getWidth() * bitmap.getWidth();
    }

    public float getSignPositionY(float top, Bitmap bitmap) {
        return top / rlContent.getHeight() * bitmap.getHeight();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode + "  resultCode = " + resultCode);
        if (requestCode == GET_OPTION) {
            if (resultCode == GET_OPTION && data != null) {
                this.opinion = data.getStringExtra("opinion");
                Log.d(TAG, "onActivityResult: " + opinion);
            }
        }
    }

    /**
     * 签署审核意见
     */
    private void uploadFile(final String opinion) {

        loading.setVisibility(View.VISIBLE);
        final Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                Call<String> call = HttpUtl.uploadWps("User/Oa/back_doc/", documentInfo.getId(), documentInfo.getProce_id(), token, opinion, documentInfo.getDoc_name(), downloadWpsPath);
                Logger.i("commit  " + opinion);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }
                        String msg = response.body();
                        Logger.i("commit  " + msg);
                        try {
                            JSONObject jsonObject = new JSONObject(msg);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("msg");

                            emitter.onNext(message);
                            if (code == 0) {
                                emitter.onNext("Y");
                            } else {
                                emitter.onNext("N");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onNext(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());
                    }
                });
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {
                    if (s.equals("Y")) {
                        EventBus.getDefault().post(new WpsdetailFinish("commit 提交成功"));
                        finish();
                    }
                } else {
                    ToastUtils.showShort(WpsDetailActivity.this, s);
                }

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);


    }

    /**
     * 反馈意见
     */
    private void feedBack(final String opinion) {

        loading.setVisibility(View.VISIBLE);
        final Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                Call<String> call = HttpUtl.feedBack("User/Oa/feedback/", documentInfo.getId(), opinion, token);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (!response.isSuccessful()) {
                            emitter.onNext(response.message());
                            return;
                        }
                        String msg = response.body();
                        Logger.i("commit  " + msg);
                        try {
                            if (msg == null) {
                                emitter.onNext("N");
                                return;
                            }
                            JSONObject jsonObject = new JSONObject(msg);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("msg");

                            emitter.onNext(message);
                            if (code == 0) {
                                emitter.onNext("Y");
                            } else {
                                emitter.onNext("N");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onNext(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        emitter.onNext(t.getMessage());
                    }
                });
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loading.setVisibility(View.GONE);
                if (s.equals("Y") || s.equals("N")) {

                    if (s.equals("Y")) {
                        EventBus.getDefault().post(new WpsdetailFinish("commit 提交成功"));
                        finish();
                    }
                } else {
                    ToastUtils.showShort(WpsDetailActivity.this, s);
                }

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);


    }
    // 根据路径获得图片并压缩，返回bitmap用于显示
//        private Bitmap getSmallBitmap(String filePath) {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(filePath, options);
//
//            // Calculate inSampleSize
//            options.inSampleSize = calculateInSampleSize(options, 480, 800);
//
//            // Decode bitmap with inSampleSize set
//            options.inJustDecodeBounds = false;
//
//            return BitmapFactory.decodeFile(filePath, options);
//        }

}
