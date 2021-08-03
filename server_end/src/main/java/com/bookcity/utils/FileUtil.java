package com.bookcity.utils;

import java.io.File;

public class FileUtil {
    public static boolean deleteFile(File file){
        if(!file.exists()){
            return false;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files.length > 0) {
                for(File fileItem : files){
                    deleteFile(fileItem);
                }
            }
            return file.delete();
        } else {
            return file.delete();
        }
    }
}
