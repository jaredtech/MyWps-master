package com.example.ysl.mywps.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class PingYinUtils {

    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        //截取第一个汉字的字母，convert获得的是所有汉字的首字母相加，转换成大写字母，才能进行排序，利用正则表达式匹配
        return convert.substring(0, 1).toUpperCase();
    }

    public static String getPingYin(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        //截取第一个汉字的字母，convert获得的是所有汉字的首字母相加，转换成大写字母，才能进行排序，利用正则表达式匹配
        return convert;
    }


}
