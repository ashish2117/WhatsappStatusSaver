package com.ashish2117.whatsappstatussaver.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.ashish2117.whatsappstatussaver.utility.FileUtil;
import com.ashish2117.whatsappstatussaver.R;
import com.ashish2117.whatsappstatussaver.adapter.CustomAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView simpleGrid;
    CustomAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    private final int SORT_MODE_NEW_FIRST = 0;
    private final int SORT_MODE_OLD_FIRST = 1;
    private int sortMode;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sortMode = SORT_MODE_NEW_FIRST;
        simpleGrid = findViewById(R.id.simpleGridView);
        refreshLayout = findViewById(R.id.refreshLayout);
        simpleGrid.setLayoutManager(new LinearLayoutManager(this));
        simpleGrid.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        isReadStoragePermissionGranted();

        refreshLayout.setOnRefreshListener(()-> {
                List<File> list = FileUtil.getStatusFiles(MainActivity.this);
                FileUtil.sortFilesNewFirst(list);
                adapter = new CustomAdapter(MainActivity.this, list);
                simpleGrid.removeAllViews();
                simpleGrid.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
                menu.getItem(1).setChecked(true);
                menu.getItem(3).getSubMenu().getItem(0).setChecked(true);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(1).setChecked(true);
        menu.getItem(3).getSubMenu().getItem(0).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.saved_file_button :
                Intent intent = new Intent(this, SavedFilesActivity.class);
                startActivity(intent);
                break;
            case R.id.old_first:
                sortMode = SORT_MODE_OLD_FIRST;
                if(!item.isChecked()) {
                    item.setChecked(true);
                    List<File> list = adapter.getList();
                    Collections.reverse(list);
                    simpleGrid.removeAllViews();
                    adapter.setList(list);
                    simpleGrid.setAdapter(adapter);
                }
                break;
            case R.id.new_first:
                sortMode = SORT_MODE_NEW_FIRST;
                if(!item.isChecked()) {
                    item.setChecked(true);
                    List<File> list = adapter.getList();
                    Collections.reverse(list);
                    simpleGrid.removeAllViews();
                    adapter.setList(list);
                    simpleGrid.setAdapter(adapter);
                }
                break;

            case R.id.videos_and_images:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    List<File> list = FileUtil.getStatusFiles(this);
                    if (sortMode == SORT_MODE_NEW_FIRST) {
                        FileUtil.sortFilesNewFirst(list);
                    } else {
                        FileUtil.sortFilesOldFirst(list);
                    }
                    adapter = new CustomAdapter(MainActivity.this, list);
                    simpleGrid.removeAllViews();
                    simpleGrid.setAdapter(adapter);
                }
                break;
            case R.id.images_only:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    List<File> list = FileUtil.getStatusFiles(this);
                    ListIterator<File> iterator = list.listIterator();
                    while(iterator.hasNext()){
                        File file = iterator.next();
                        if(file.getName().endsWith(".mp4")) {
                            iterator.remove();
                        }
                    }
                    if (sortMode == SORT_MODE_NEW_FIRST) {
                        FileUtil.sortFilesNewFirst(list);
                    } else {
                        FileUtil.sortFilesOldFirst(list);
                    }
                    adapter = new CustomAdapter(MainActivity.this,list);
                    simpleGrid.removeAllViews();
                    simpleGrid.setAdapter(adapter);
                }
                break;
            case R.id.videos_only:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    List<File> list = FileUtil.getStatusFiles(this);
                    ListIterator<File> iterator = list.listIterator();
                    while(iterator.hasNext()){
                        File file = iterator.next();
                        if(file.getName().endsWith(".jpg")) {
                            iterator.remove();
                        }
                    }
                    if (sortMode == SORT_MODE_NEW_FIRST) {
                        FileUtil.sortFilesNewFirst(list);
                    } else {
                        FileUtil.sortFilesOldFirst(list);
                    }
                    adapter = new CustomAdapter(MainActivity.this,list);
                    simpleGrid.removeAllViews();
                    simpleGrid.setAdapter(adapter);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                isWriteStoragePermissionGranted();
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else {
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                List<File> list = FileUtil.getStatusFiles(this);
                FileUtil.sortFilesNewFirst(list);
                adapter = new CustomAdapter(MainActivity.this, list);
                simpleGrid.setAdapter(adapter);
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    List<File> list = FileUtil.getStatusFiles(this);
                    FileUtil.sortFilesNewFirst(list);
                    adapter = new CustomAdapter(MainActivity.this, list);
                    simpleGrid.setAdapter(adapter);
                }else{
                    System.exit(0);
                }
                break;

            case 3:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    isWriteStoragePermissionGranted();
                }else{
                    System.exit(0);
                }
                break;
        }
    }

}


