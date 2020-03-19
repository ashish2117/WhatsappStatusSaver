package com.ashish2117.whatsappstatussaver.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.LinearLayout;

import java.io.File;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class LoadBitmap extends AsyncTask<Void, Void, Void> {

    File file;
    LinearLayout linearLayout;
    Bitmap bitmap = null;
    Context context;

    public LoadBitmap(File file, LinearLayout linearLayout, Context context){
        this.file = file;
        this.linearLayout = linearLayout;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... files) {
        String filePath = file.getPath();

        if(filePath.endsWith("jpg")) {
            final int THUMBSIZE = 128;
            bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath),
                    THUMBSIZE, THUMBSIZE);
        }else if(filePath.endsWith("mp4")){


               // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //bitmap = ThumbnailUtils.createVideoThumbnail(file , new Size(200,200), new CancellationSignal());
                //}else
                    bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        RoundedBitmapDrawable background = RoundedBitmapDrawableFactory
                .create(context.getResources(),bitmap);
        background.setCircular(true);
        linearLayout.setBackground(background);
    }
}
