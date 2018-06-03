package com.example.ysl.mywps.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class FileClass {

    public static List<File> readDirctory(File dir) {
        List<File> list = new ArrayList<>();
        if (dir.isDirectory()) {

            File[] files = dir.listFiles();
            for (File file : files){

                list.add(file);
            }

        } else if (dir.getName().endsWith(".doc")) {
            list.add(dir);
        }

        return  list;
    }

}
