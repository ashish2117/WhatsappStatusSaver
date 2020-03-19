package com.ashish2117.whatsappstatussaver.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashish2117.whatsappstatussaver.utility.FileUtil;
import com.ashish2117.whatsappstatussaver.activity.ImageViewerActivity;
import com.ashish2117.whatsappstatussaver.utility.LoadBitmap;
import com.ashish2117.whatsappstatussaver.utility.MediaWriter;
import com.ashish2117.whatsappstatussaver.R;
import com.ashish2117.whatsappstatussaver.activity.VideoViewerActivity;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<MainHolder> {

    Context activity;
    List<File> list;

    public void setList(List<File> list) {
        this.list = list;
    }

    public List<File> getList() {
        return list;
    }



    public CustomAdapter(Context activity, List<File> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent,false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder,final int i) {
        holder.fileTitle.setText(list.get(i).getName());
        holder.fileSize.setText(FileUtil.getFileSize(list.get(i)));
        holder.fileTime.setText(FileUtil.getDateStringForFile(list.get(i)));

        holder.saveButton.setOnClickListener((view) -> {
                saveFile(i);
        });

        holder.layout.setOnClickListener((view) -> {
                Intent intent = null;
                if(list.get(i).getName().endsWith("jpg")) {
                    intent = new Intent(activity, ImageViewerActivity.class);
                    intent.putExtra("imageUri", list.get(i).getAbsolutePath());
                    intent.putExtra("enableSaveButton",true);
                }else{
                    intent = new Intent(activity, VideoViewerActivity.class);
                    intent.putExtra("videoUri", list.get(i).getAbsolutePath());
                    intent.putExtra("enableSaveButton",true);
                }
                activity.startActivity(intent);
        });

        holder.layout.setOnLongClickListener((view) -> {
                showDialog(i);
                return true;
        });

        if(list.get(i).getName().endsWith("jpg"))
            holder.videoIcon.setVisibility(View.INVISIBLE);;

        holder.shareButton.setOnClickListener((view) ->{
                shareFile(i);
        });

        holder.whatsappButton.setOnClickListener((view) -> {
                shareToWhatsapp(i);
        });

        new LoadBitmap(list.get(i),holder.icon, activity).execute();

    }

    private void showDialog(final int position) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.popup_options);

        LinearLayout shareWhatsappOpt = dialog.findViewById(R.id.share_to_whatsapp_opt);
        LinearLayout saveOpt = dialog.findViewById(R.id.save_opt);
        LinearLayout shareOpt = dialog.findViewById(R.id.share_opt);

        shareWhatsappOpt.setOnClickListener((view -> {
              shareToWhatsapp(position);
        }));

        saveOpt.setOnClickListener((view -> {
            saveFile(position);
        }));

        shareOpt.setOnClickListener((view -> {
            shareFile(position);
        }));

        dialog.show();

    }

    private void shareFile(int position) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if(list.get(position).getName().endsWith(".jpg"))
            sharingIntent.setType("image/*");
        else
            sharingIntent.setType("video/*");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(list.get(position).getAbsolutePath()));
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void saveFile(int position) {
        if(list.get(position).getName().endsWith("jpg")) {
            Bitmap bitmap = MediaWriter.getImageBitMap(list.get(position));
            MediaWriter.writeImageStatus(bitmap, list.get(position).getName(),activity);
        }else if(list.get(position).getName().endsWith("mp4")){
            MediaWriter.writeVideoStatus(list.get(position), activity);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void shareToWhatsapp(int position){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if(list.get(position).getName().endsWith(".pg"))
            sharingIntent.setType("image/*");
        else
            sharingIntent.setType("video/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(list.get(position).getAbsolutePath()));
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sharingIntent.setPackage("com.whatsapp");
        try {
            activity.startActivity(sharingIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,"Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

}

class MainHolder extends RecyclerView.ViewHolder{

    TextView fileTitle;
    TextView fileTime;
    TextView fileSize;
    LinearLayout icon;
    Button saveButton;
    Button shareButton;
    Button whatsappButton;
    ImageView videoIcon;
    LinearLayout layout;

    public MainHolder(@NonNull View itemView) {
        super(itemView);
        fileTitle = itemView.findViewById(R.id.saved_file_title);
        fileTime = itemView.findViewById(R.id.saved_file_time);
        fileSize = itemView.findViewById(R.id.saved_file_size);
        icon = itemView.findViewById(R.id.icon);
        saveButton = itemView.findViewById(R.id.saveButton);
        shareButton = itemView.findViewById(R.id.shareButton);
        whatsappButton = itemView.findViewById(R.id.whatsapp_button);
        videoIcon = itemView.findViewById(R.id.video_icon);
        layout = itemView.findViewById(R.id.file_item_layout);
    }

}




