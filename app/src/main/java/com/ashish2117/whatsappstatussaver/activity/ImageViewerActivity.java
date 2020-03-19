package com.ashish2117.whatsappstatussaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashish2117.whatsappstatussaver.utility.MediaWriter;
import com.ashish2117.whatsappstatussaver.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        PhotoView imageView = findViewById(R.id.image_view);
        Bundle b=getIntent().getExtras();
        final String imageUri = b.getString("imageUri");
        final boolean enableSaveButtoon = b.getBoolean("enableSaveButton");
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri);
        imageView.setImageBitmap(bitmap);

        Button saveButton = findViewById(R.id.save_button);
        if(!enableSaveButtoon)
            saveButton.setVisibility(View.INVISIBLE);
        Button backButton = findViewById(R.id.back_button);
        Button shareButton = findViewById(R.id.share_button);
        Button whatsappButton = findViewById(R.id.whatsapp_button);

        whatsappButton.setOnClickListener((view)->{
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            if(imageUri.endsWith(".pg"))
                sharingIntent.setType("image/*");
            else
                sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sharingIntent.setPackage("com.whatsapp");
            try {
                startActivity(sharingIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,"Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = MediaWriter.getImageBitMap(new File(imageUri));
                MediaWriter.writeImageStatus(bitmap, new File(imageUri).getName(), ImageViewerActivity.this);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageViewerActivity.super.onBackPressed();
            }
        });
    }
}
