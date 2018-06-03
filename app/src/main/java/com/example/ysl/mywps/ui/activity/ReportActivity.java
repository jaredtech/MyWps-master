package com.example.ysl.mywps.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ysl.mywps.R;
import com.example.ysl.mywps.client.UploadWebChromeClient;
import com.example.ysl.mywps.interfaces.JSCallBack;
import com.example.ysl.mywps.interfaces.JavascriptBridge;
import com.example.ysl.mywps.utils.SharedPreferenceUtils;
import com.example.ysl.mywps.utils.SysytemSetting;
import com.example.ysl.mywps.utils.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class ReportActivity extends BaseActivity  implements JSCallBack {
    @BindView(R.id.webview_webview)
    WebView webView;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.webview_progerss)
    ProgressBar progressbar;

    private String token = "";
    private String realname = "";

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage5;
    public static final int FILECHOOSER_RESULTCODE = 5173;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;

    private static final String TAG = ProposalActivity.class.getName();
    private boolean needToken = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_layout);
        ButterKnife.bind(this);
        setTitleText("通知公告");
        showLeftButton(true, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }

            }
        });
        initView();
        afterView();

    }

    @Override
    public void initView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置js交互
//        webSettings.setUseWideViewPort(true);//设置图片调整到适合webview的大小

        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true);//支持缩放
        webSettings.setBuiltInZoomControls(true);///设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false);//隐藏内置的原生缩放控件

        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//设置适应Html5 重点是这个设置
        String userAgent = webSettings.getUserAgentString();
        userAgent += "webview";
        Log.i(TAG, userAgent + "  useragent");
        webSettings.setUserAgentString(userAgent);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearCache(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }


    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }


        super.onDestroy();

    }

    private void afterView() {

        MyWebChromeClient chromeClient = new MyWebChromeClient();
        MyWebviewClient client = new MyWebviewClient();
//file:///android_asset/index.html
//   http://www.haont.cn/CPPCC/sqmy/#!/submit/    http://www.haont.cn/TiAnPhone/
//        http://www.haont.cn/TiAnPhone/
        webView.addJavascriptInterface(new JavascriptBridge(this), "javaBridge");
        webView.loadUrl("http://www.haont.cn/wxqy/wxqyh/pages/notice/index.html");
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(client);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
//        // 设置是否允许 WebView 使用 File 协议,默认设置为true，即允许在 File 域下执行任意 JavaScript 代码
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            webView.setWebContentsDebuggingEnabled(true);

        }
    }

    //使用Webview的时候，返回键没有重写的时候会直接关闭程序，这时候其实我们要其执行的知识回退到上一步的操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void initData() {
        token = SharedPreferenceUtils.loginValue(this, SysytemSetting.USER_TOKEN);
        realname = SharedPreferenceUtils.loginValue(this, SysytemSetting.REAL_NAME);
    }

    @Override
    public String jsCallBack(String method, String msg) {
        return realname + "," + token;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessage5) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams
                        .parseResult(resultCode, intent));
            }
            mUploadMessage5 = null;
        }
    }

    private static class MyWebviewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.i(TAG, "finish");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

    }
    // Android版本变量
    final int version = Build.VERSION.SDK_INT;

    private void setToken() {
//        webView.loadUrl("javascript:setFile('" + filePath + "','"+fileName+"')");
        Log.i("aaa", "mytoken   " + token);
//        ToastUtils.showLong(ReportActivity.this, "setToken:" + token);
        webView.post(new Runnable() {
            @Override
            public void run() {
                if (version < 18) {
                    webView.loadUrl("javascript:setToken('" + token + "','" + realname + "')");
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript("javascript:setToken('" + token + "','" + realname + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {

                                Log.i("aaa", "return  " + s);
                            }
                        });
                    } else {

                    }
                }
            }
        });
    }
    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = msg.what;
            progressbar.setProgress(progress);
            if (progress == 100) {
                progressbar.setVisibility(View.GONE);
                if (needToken) {
                    setToken();
                    needToken = false;
                }
            }
        }
    };

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, "progress  " + newProgress);
            if (newProgress >= 100) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressHandler.sendEmptyMessage(100);
                    }
                }).start();
            } else {
                progressHandler.sendEmptyMessage(newProgress);
            }
//            Log.i(TAG, "progress  " + newProgress);
//            progressbar.setProgress(newProgress);
//            if (newProgress == 100) {
//                progressbar.setVisibility(View.GONE);
//                webView.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            webView.evaluateJavascript("javascript:getToken('" + token + "')", new ValueCallback<String>() {
//                                @Override
//                                public void onReceiveValue(String s) {
//                                    Log.i("aaa", "return  " + s);
//                                }
//                            });
//                        } else {
//                            webView.loadUrl("javascript:getToken('" + token + "')");
//                        }
//                    }
//                });
//            }
        }



        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            this.openFileChooser(uploadMsg, "*/*");
        }

        // For Android >= 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            this.openFileChooser(uploadMsg, acceptType, null);
        }

        // For Android >= 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }



        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            if (mUploadMessage5 != null) {
                mUploadMessage5.onReceiveValue(null);
                mUploadMessage5 = null;
            }
            mUploadMessage5 = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent,
                        FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
            } catch (ActivityNotFoundException e) {
                mUploadMessage5 = null;
                Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (needToken) {
                setToken();
                needToken = false;
            }
            Log.i(TAG, "title  " + title);
        }
    }
}
