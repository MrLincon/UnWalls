package com.whitespace.unwalls.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class StorageChecker {
    public static void checkStorageAvailability(Context context){
        boolean isStorageExist = false;
        boolean isStorageWritable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)){
            isStorageExist = isStorageWritable = true;
        } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            isStorageExist = true;
            isStorageWritable = false;
            Toast.makeText(context, "Storage is read only", Toast.LENGTH_SHORT).show();
        } else {
            isStorageExist = isStorageWritable = false;
            Toast.makeText(context, "Storage is not exist", Toast.LENGTH_SHORT).show();
        }
    }
}
