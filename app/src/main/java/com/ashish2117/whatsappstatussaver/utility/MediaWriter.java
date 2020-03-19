package com.ashish2117.whatsappstatussaver.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MediaWriter {


    public static void writeVideoStatus(File src, Context context){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/WhatsappStatus");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return;
            }
        }

        File mediaFile;
        String mImageName=src.getName();
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(mediaFile)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context,"Saved to " + mediaStorageDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public static void writeImageStatus(Bitmap image, String name, Context context){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/WhatsappStatus");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return;
            }
        }

        File mediaFile;
        String mImageName=name;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "Saved to " + mediaStorageDir.getPath(), Toast.LENGTH_SHORT ).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Bitmap getImageBitMap(File file){
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
}
