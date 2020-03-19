package com.ashish2117.whatsappstatussaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.ashish2117.whatsappstatussaver.utility.MediaWriter;
import com.ashish2117.whatsappstatussaver.R;

import java.io.File;

public class VideoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);
        VideoView videoView = findViewById(R.id.video_view);
        Bundle b=getIntent().getExtras();
        final String videoUri = b.getString("videoUri");
        final boolean enableSaveButtoon = b.getBoolean("enableSaveButton");
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.start();

        Button saveButton = findViewById(R.id.save_button_1);
        if(!enableSaveButtoon)
            saveButton.setVisibility(View.INVISIBLE);
        Button backButton = findViewById(R.id.back_button_1);
        Button shareButton = findViewById(R.id.share_button_1);

        Button whatsappButton = findViewById(R.id.whatsapp_button);

        whatsappButton.setOnClickListener((view)->{
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            if(videoUri.endsWith(".pg"))
                sharingIntent.setType("image/*");
            else
                sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoUri));
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
                 MediaWriter.writeVideoStatus(new File(videoUri), VideoViewerActivity.this);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoUri));
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoViewerActivity.super.onBackPressed();
            }
        });
    }
}
