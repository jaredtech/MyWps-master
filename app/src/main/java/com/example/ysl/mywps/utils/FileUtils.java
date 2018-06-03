package com.example.ysl.mywps.utils;

/**
 * Created by Administrator on 2018/1/20 0020.
 */
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.ysl.mywps.interfaces.HttpFileCallBack;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class FileUtils {
//
//    public static File createFile(Context context){
//
//        File file=null;
//        String state = Environment.getExternalStorageState();
//
//        if(state.equals(Environment.MEDIA_MOUNTED)){
//
//            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.apk");
//        }else {
//            file = new File(context.getCacheDir().getAbsolutePath()+"/test.apk");
//        }
//
//        L.d("vivi","file "+file.getAbsolutePath());
//
//        return file;
//
//    }

    public static void writeFile2Disk(final Response<ResponseBody> response, final File file, final HttpFileCallBack httpCallBack){



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long currentLength = 0;
                OutputStream os =null;

                InputStream is = response.body().byteStream();
                long totalLength =response.body().contentLength();

                try {

                    os = new FileOutputStream(file);

                    int len ;

                    byte [] buff = new byte[1024];

                    while((len=is.read(buff))!=-1){

                        os.write(buff,0,len);
                        currentLength+=len;
                        httpCallBack.onLoading(currentLength,totalLength);
                    }
                    if(os!=null){
                        try {
                            os.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(is!=null){
                        try {
                            is.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Logger.e("文件下载完成");

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();


    }

}

