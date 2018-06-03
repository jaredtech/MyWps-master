package com.example.ysl.mywps.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ysl.mywps.bean.FileType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class SharedPreferenceUtils {


    public static void loginSave(Context context,String key,String value){

        SharedPreferences preferences = context.getSharedPreferences(SysytemSetting.USER_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key,value);
        editor.commit();

    }

    public static String loginValue(Context context,String key){
        String value = "";
        if(context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(SysytemSetting.USER_FILE,Context.MODE_PRIVATE);

        value = preferences.getString(key,"");

        return value;
    }


    /**
     * 保存List
     * @param datalist
     */
    public static void setFileTypeList(Context context, List<FileType> datalist) {
        if (null == datalist || datalist.size() <= 0 || context == null)
            return;
        SharedPreferences preferences = context.getSharedPreferences("fileType",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存


        String strJson = gson.toJson(datalist);
        editor.putString("fileType", strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @return
     */
    public static List<FileType> getFileTypeDataList(Context context) {
        List<FileType> datalist=new ArrayList<FileType>();
        SharedPreferences preferences = context.getSharedPreferences("fileType",Context.MODE_PRIVATE);
        String strJson = preferences.getString("fileType", null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        try {
            JSONArray array = new JSONArray(strJson);

            for (int i = 0; i < array.length(); ++i){

                JSONObject child = array.getJSONObject(i);
                FileType fileType = gson.fromJson(child.toString(),FileType.class);
               datalist.add(fileType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;

    }


}
