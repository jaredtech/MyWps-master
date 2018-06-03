package com.example.ysl.mywps.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.bean.FileListChildBean;
import com.example.ysl.mywps.bean.FileType;
import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.interfaces.PassFileChildList;
import com.example.ysl.mywps.interfaces.PasssString;
import com.example.ysl.mywps.net.HttpUtl;
import com.example.ysl.mywps.net.ProgressListener;
import com.example.ysl.mywps.provider.UploadProvider;
import com.example.ysl.mywps.ui.fragment.DocumentFrageMent;
import com.example.ysl.mywps.ui.fragment.TransportFragment;
import com.example.ysl.mywps.utils.CommonUtil;
import com.example.ysl.mywps.utils.NoDoubleClickListener;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/2/4 0004.
 */


public class MaterialActivity extends BaseActivity implements View.OnClickListener, PassFileChildList {

    @BindView(R.id.material_ll_document)
    LinearLayout llDocument;
    @BindView(R.id.material_tv_document)
    TextView tvDocument;

    @BindView(R.id.material_ll_transport)
    LinearLayout llTransport;
    @BindView(R.id.material_tv_transport)
    TextView tvTransport;

    @BindView(R.id.material_rl_content)
    RelativeLayout rlContent;

    private ColorStateList normal, select;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Window myWindow;
    private WindowManager.LayoutParams myLp;
    private PassFileChildList passFileChildList;
    private int kindType = 0;//1图片 2文档 3 视频 4应用

    private List<FileType> fileTypes;
    private String fileType = "";
    private PasssString passsString;


    private MyclickListener click = new MyclickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_layout);
        myWindow = this.getWindow();
        myLp = myWindow.getAttributes();
        ButterKnife.bind(this);
        showLeftButton(true, "", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleText("素材共享");
        showRight(true, getResources().getString(R.string.material_right), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MaterialActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        showRight1(true, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                findDocuments();

                showTopWindow();
            }
        });


        fileTypes = SharedPreferenceUtils.getFileTypeDataList(this);
        manager = getSupportFragmentManager();

        normal = getResources().getColorStateList(R.color.text_gray);
        select = getResources().getColorStateList(R.color.text_black);
        llDocument.setOnClickListener(this);
        llTransport.setOnClickListener(this);

        showMessage(1);

        showMessage(0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();

            String path = uri.getPath();

            Logger.i("path  " + path);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后


                try {
                    path = getPath(this, uri);
                } catch (Exception e) {

                }
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }

            if (CommonUtil.isEmpty(path)) {
                ToastUtils.showLong(this, "当前文件不能上传");
                return;
            }
            int startIndex = path.lastIndexOf("/");
            String name = path.substring(startIndex + 1, path.length());
//            type = type.substring(type.lastIndexOf("."));
//            String name = System.currentTimeMillis() + type;
            topWindow.dismiss();

            Logger.i("path  " + path + "  name " + name);

            if (fileTypes != null) {
                for (FileType fileInfo : fileTypes) {

                    if (kindType == 1 && fileInfo.getValue().equals("图片")) {
                        fileType = fileInfo.getCode();
//                        uploadFile(path, name,fileType,token);
                        passsString.setString(path, name, fileType);
                    } else if (kindType == 2 && fileInfo.getValue().equals("文档")) {
                        fileType = fileInfo.getCode();
//                        uploadFile(path, name,fileType,token);
                        passsString.setString(path, name, fileType);
                    } else if (kindType == 3 && fileInfo.getValue().equals("视频")) {

                        fileType = fileInfo.getCode();
//                        uploadFile(path, name,fileType,token);
                        passsString.setString(path, name, fileType);
                    } else if (kindType == 4 && fileInfo.getValue().equals("应用")) {

                        fileType = fileInfo.getCode();
                        passsString.setString(path, name, fileType);
//                        uploadFile(path, name,fileType,token);
                    }


                }
            }


        }

    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    @Override
    public void initView() {




    }

    @Override
    public void initData() {

    }

    private PopupWindow topWindow;


    private void showTopWindow() {


        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_material_top_layout, null);
        myLp.alpha = 0.4f;
        myWindow.setAttributes(myLp);

        if (topWindow == null) {
            topWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            RelativeLayout rlPicture = (RelativeLayout) view.findViewById(R.id.material_top_picture);
            RelativeLayout rlDocument = (RelativeLayout) view.findViewById(R.id.material_top_documents);
            RelativeLayout rlVideo = (RelativeLayout) view.findViewById(R.id.material_top_video);
            RelativeLayout rlApplication = (RelativeLayout) view.findViewById(R.id.material_top_application);


            rlPicture.setOnClickListener(click);
            rlDocument.setOnClickListener(click);
            rlVideo.setOnClickListener(click);
            rlApplication.setOnClickListener(click);
            topWindow.setBackgroundDrawable(new ColorDrawable());
            topWindow.setAnimationStyle(R.style.Popupwindow_top);
            topWindow.setFocusable(true);
            topWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    myLp.alpha = 1f;
                    myWindow.setAttributes(myLp);

                }
            });
            topWindow.showAtLocation(rlContent, Gravity.TOP, 0, 40);
        } else {

            topWindow.showAtLocation(rlContent, Gravity.TOP, 0, 40);
        }


    }


    private void setBackx(int position) {


        switch (position) {

            case 0:

                tvDocument.setTextColor(select);
                tvTransport.setTextColor(normal);
                setRightVisible(true);
                setRight1Visible(true);
                break;
            case 1:

                tvDocument.setTextColor(normal);
                tvTransport.setTextColor(select);
                setRightVisible(false);
                setRight1Visible(false);
                break;
        }

    }

    DocumentFrageMent documentFrageMent;
    TransportFragment transportFragment;
    Fragment currentFragment;

    private void showMessage(int position) {
        transaction = manager.beginTransaction();

        if (currentFragment != null) transaction.hide(currentFragment);

        switch (position) {
            case 0:

                if (documentFrageMent == null) {

                    documentFrageMent = new DocumentFrageMent();
                    documentFrageMent.setPassFileChildList(MaterialActivity.this);
                    transaction.add(R.id.material_rl_content, documentFrageMent);
                } else transaction.show(documentFrageMent);
                setBackx(0);
                currentFragment = documentFrageMent;
                break;

            case 1:

                if (transportFragment == null) {

                    transportFragment = new TransportFragment();
                    passsString = transportFragment;
                    passFileChildList = transportFragment;
                    transaction.add(R.id.material_rl_content, transportFragment);
                } else transaction.show(transportFragment);
                setBackx(1);
                currentFragment = transportFragment;
                break;

        }
        transaction.commit();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.material_ll_document:

                showMessage(0);
                break;

            case R.id.material_ll_transport:
                showMessage(1);
                break;
        }

    }

    private void findDocuments(int type) {

        /***
         *打开文件管理器
         */
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        switch (type) {

            case 1://图片
                kindType = 1;
                intent.setType("image/*");
                break;
            case 2://doc
                kindType = 2;
                intent.setType("application/msword");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");//设置类型，我这里是任意类型，任意后缀的可以这样写。

                break;
            case 3://video
                kindType = 3;
                intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
                break;

            case 4://application
//
                kindType = 4;
                intent.setType("application/vnd.android.package-archive"); //选择视频 （mp4 3gp 是android支持的视频格式）

                break;
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


    @Override
    public void passFileChild(ArrayList<FileListChildBean> files, int kind) {

        passFileChildList.passFileChild(files,kind);

    }

    private class MyclickListener extends NoDoubleClickListener {
        @Override
        public void click(View v) {

            switch (v.getId()) {

                case R.id.material_top_picture:
                    findDocuments(1);
                    break;
                case R.id.material_top_documents:
                    findDocuments(2);
                    break;
                case R.id.material_top_video:
                    findDocuments(3);
                    break;
                case R.id.material_top_application:
                    findDocuments(4);
                    break;
            }

        }
    }
}
