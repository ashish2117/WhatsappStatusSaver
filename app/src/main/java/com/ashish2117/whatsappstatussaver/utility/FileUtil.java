package com.ashish2117.whatsappstatussaver.utility;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {

    public static String getFileSize(File file) {
        double kiloBytes =  file.length() / 1024;
        if(kiloBytes >= 1024)
            return String.valueOf(kiloBytes/1024 ).substring(0,3)+ " MB";
        else
            return Math.round(kiloBytes) + " KB";
    }

    public static String getDateStringForFile(File file){
        Long timeStamp = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy hh:mm a");
        String dateString = formatter.format(new Date(timeStamp));
        return dateString;
    }

    public static void sortFilesNewFirst(List<File> list){
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return file.lastModified() > t1.lastModified() ? -1 : 1;
            }
        });
    }

    public static void sortFilesOldFirst(List<File> list){
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return file.lastModified() > t1.lastModified() ? 1 : -1;
            }
        });
    }

    public static List<File> getSavedFiles(Context context){
        File mediaStorageDir;
        List resultList = null;
        try {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/WhatsAppStatus");
            resultList = new LinkedList<>(Arrays.asList(mediaStorageDir.listFiles()));
            Collections.reverse(resultList);
        }catch (Exception e){
            Toast.makeText(context,"No saved statuses!",Toast.LENGTH_SHORT).show();
            return new LinkedList<File>();
        }

        return resultList;
    }

    public static List<File> getStatusFiles(Context context){
        File mediaStorageDir;
        List<File> resultList = null;
        try {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/WhatsApp/Media/.Statuses");
            resultList = new LinkedList<File>(Arrays.asList(mediaStorageDir.listFiles()));
            for (File file : resultList) {
                if (file.getName().equals(".nomedia")) {
                    resultList.remove(file);
                    break;
                }
            }
        }catch (Exception e){
            Toast.makeText(context,"Whatsapp is not installed", Toast.LENGTH_LONG).show();
            return new LinkedList<File>();
        }
        Collections.reverse(resultList);
        return resultList;
    }

}
