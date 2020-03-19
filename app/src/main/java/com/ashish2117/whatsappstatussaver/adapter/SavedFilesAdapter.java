package com.ashish2117.whatsappstatussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashish2117.whatsappstatussaver.utility.FileUtil;
import com.ashish2117.whatsappstatussaver.activity.ImageViewerActivity;
import com.ashish2117.whatsappstatussaver.utility.LoadBitmap;
import com.ashish2117.whatsappstatussaver.R;
import com.ashish2117.whatsappstatussaver.activity.VideoViewerActivity;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedFilesAdapter extends RecyclerView.Adapter<SavedFilesAdapter.Holder> {

    List<File> list;
    Context context;

    public void setList(List<File> list) {
        this.list = list;
    }

    public List<File> getList() {
        return list;
    }

    public SavedFilesAdapter(Context context, List<File> list){
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_status_item, viewGroup,false);
        return new Holder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
           holder.fileTitle.setText(list.get(i).getName());
           holder.fileSize.setText(FileUtil.getFileSize(list.get(i)));
           holder.fileTime.setText(FileUtil.getDateStringForFile(list.get(i)));
           new LoadBitmap(list.get(i),holder.fileIcon,context).execute();
           if(list.get(i).getName().endsWith(".jpg"))
               holder.videoIcon.setVisibility(View.INVISIBLE);
           holder.fileItemLayout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = null;
                   if(list.get(i).getName().endsWith("jpg")) {
                       intent = new Intent(context, ImageViewerActivity.class);
                       intent.putExtra("imageUri", list.get(i).getAbsolutePath());
                       intent.putExtra("enableSaveButton",false);
                   }else{
                       intent = new Intent(context, VideoViewerActivity.class);
                       intent.putExtra("videoUri", list.get(i).getAbsolutePath());
                       intent.putExtra("enableSaveButton",false);
                   }
                   context.startActivity(intent);
               }
           });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView fileTitle;
        TextView fileTime;
        TextView fileSize;
        LinearLayout fileIcon;
        ImageView videoIcon;
        LinearLayout fileItemLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            fileTitle = itemView.findViewById(R.id.saved_file_title);
            fileTime = itemView.findViewById(R.id.saved_file_time);
            fileSize = itemView.findViewById(R.id.saved_file_size);
            fileIcon = itemView.findViewById(R.id.saved_file_icon);
            videoIcon = itemView.findViewById(R.id.video_icon_1);
            fileItemLayout = itemView.findViewById(R.id.file_item_layout);
        }

    }

}


