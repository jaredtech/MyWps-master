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
import com.example.ysl.mywps.ui.view.MoviewImage;
import com.example.ysl.mywps.ui.view.WritingPadView;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.FileUtils;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.SysytemSetting;
import com.example.ysl.mywps.utils.ToastUtils;
import com.example.ysl.mywps.utils.WpsModel;
import com.example.ysl.mywps.utils.WpsUtils;
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
    MoviewImage ivIcon;
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
    private float x1, x2, y1, y2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wpc_details_layout);
        ButterKnife.bind(this);

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
        ivIcon.setVisibility(View.GONE);

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

        adapter = new PreviewAdapter(documentInfo.getDoc_imgs(), this);
        if (documentInfo.getDoc_imgs() != null && documentInfo.getDoc_imgs().size() > 0) {
            String imagePath = documentInfo.getDoc_imgs().get(0).getImg();
            int nameStartIndex = imagePath.lastIndexOf("/") + 1;
            int nameEndIndex = imagePath.lastIndexOf("png") + 3;

            uploadImageName = imagePath.substring(nameStartIndex, nameEndIndex);
            Logger.i("   " + uploadImageName);


            textPager.setText( "1/" + documentInfo.getDoc_imgs().size());
        }
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
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    textPager.setText((firstItemPosition+1) + "/" + documentInfo.getDoc_imgs().size());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);

        //只有处理人才会下载文件
        if (mAccount.equals(documentInfo.getNow_nickname()) || mAccount.equals(documentInfo.getNow_username()))
            downLoadWps(false);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                float clickx = event.getX();

                x1 = ivIcon.getX() - 90;
                x2 = ivIcon.getX() + ivIcon.getWidth() + 90;
                y1 = event.getY();
                y2 = ivIcon.getY();

                float sumx = Math.abs(x2 - x1);
                float sumy = Math.abs(y2 - y1);
                if (clickx > x1 && clickx < x2 && sumy < 350 && y1 > y2) {
                    ivIcon.autoMouse(event);
                    return true;
                }
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

        if (!checkFileExist()) return;

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
                            file.mkdirs();
                        }
                        downloadWpsPath = path + "/" + documentInfo.getDoc_name();
                        file = new File(downloadWpsPath);

                        FileUtils.writeFile2Disk(response, file, new HttpFileCallBack() {
                            @Override
                            public void onLoading(long currentLength, long totalLength) {

                                int precent = (int) (currentLength * 100 / totalLength);
                                handler.sendEmptyMessage(precent);

                            }
                        });

                        emitter.onNext("Y");

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

                    if (shouldOpen) openWps(downloadWpsPath);
                    SharedPreferences.Editor editor = wpsPreference.edit();
                    editor.putString(documentInfo.getDoc_name(), documentInfo.getStatus());
                    editor.apply();
//                    ToastUtils.showShort(getApplicationContext(), "文件下载成功");
                } else if (s.equals("N")) {

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
        if (file == null || !file.exists()) {
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
        final float left = Math.abs(ivIcon.getX() - 80);
        final float top = Math.abs(ivIcon.getY() - 140);

        if (backBitmap == null || CommonUtil.isEmpty(uploadImageName)) {
            ToastUtils.showShort(WpsDetailActivity.this, "列表中没有图片不能上传图片");
            return;
        }
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {

                try {
                    Bitmap forBitmap = null;
                    FileInputStream fis = new FileInputStream(localImagePath);
                    forBitmap = BitmapFactory.decodeStream(fis);
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.3f, 0.3f);

                    Logger.i("压缩前  " + forBitmap.getWidth() + "  " + forBitmap.getHeight());
                    forBitmap = Bitmap.createBitmap(forBitmap, 0, 0, forBitmap.getWidth(),
                            forBitmap.getHeight(), matrix, true);
                    Logger.i("压缩后  " + forBitmap.getWidth() + "  " + forBitmap.getHeight());


                    Bitmap myBitmap = toConformBitmap(backBitmap, forBitmap, left, top);
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + uploadImageName;
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
                    signCompleted();
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
    private void signCompleted() {


        loading.setVisibility(View.VISIBLE);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<String> emitter) throws Exception {
//
                Call<String> call = HttpUtl.signedCommit("User/Oa/back_signed_doc/", documentInfo.getProce_id(), documentInfo.getId(), "签署成功", "2", uploadImageName, uploadImagePath, token);


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
                        File file = new File(uploadImagePath);
                        if (file.exists()) {
                            file.delete();
                        }
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
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, left, top, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
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
                                Log.d("HeadPortrait", "alpha:" + alpha);
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
                    ivIcon.setVisibility(View.GONE);
                }
            });
            tvClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivIcon.setVisibility(View.GONE);
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
                                    ivIcon.setVisibility(View.VISIBLE);

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

        File file = new File(wpsPath);

        boolean shouldUpdate = false;


        if (file.exists()) {

            String existsStatus = wpsPreference.getString(documentInfo.getDoc_name(), "");
            if (CommonUtil.isNotEmpty(existsStatus)) {
                if (documentInfo.getStatus().equals(existsStatus)) {
                    downloadWpsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + documentInfo.getDoc_name();
                    shouldUpdate = false;
                } else {
                    shouldUpdate = true;
                }
            } else {
                shouldUpdate = true;
            }
        } else {
            shouldUpdate = true;
        }

        return shouldUpdate;
    }


    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            int id = v.getId();

            if (id == R.id.wpcdetail_iv_artical || id == R.id.wpcdetail_ll_artical) {

                if (checkFileExist()) {
                    ToastUtils.showShort(WpsDetailActivity.this, "正在下载");
                    downLoadWps(true);
                } else {
                    openWps(downloadWpsPath);
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


                if (documentInfo.getStatus().equals("3") || documentInfo.getStatus().equals("5") || documentInfo.getStatus().equals("2") || documentInfo.getStatus().equals("6")) {

                    Intent intent = new Intent(WpsDetailActivity.this, CommitActivity.class);
                    intent.putExtra("wpspath", downloadWpsPath);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("documentInfo", documentInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else ToastUtils.showShort(WpsDetailActivity.this, "该文档目前不在审核或签署流程");

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
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("documentInfo", documentInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (documentInfo.getStatus().equals("3")) {
                    if (!haveSigned || CommonUtil.isEmpty(localImagePath)) {
                        ToastUtils.showShort(WpsDetailActivity.this, "请先签名图片");
                        return;
                    }

                    saveImage();

                } else {
                    ToastUtils.showShort(WpsDetailActivity.this, "该文档所在流程不能进入通讯录");
                }

            }
        }
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
