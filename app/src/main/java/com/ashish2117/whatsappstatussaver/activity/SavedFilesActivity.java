package com.ashish2117.whatsappstatussaver.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ashish2117.whatsappstatussaver.utility.FileUtil;
import com.ashish2117.whatsappstatussaver.R;
import com.ashish2117.whatsappstatussaver.adapter.SavedFilesAdapter;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedFilesActivity extends AppCompatActivity {
    
        SavedFilesAdapter adapter;
        RecyclerView savedFilesView;
        private final int SORT_MODE_NEW_FIRST = 0;
        private final int SORT_MODE_OLD_FIRST = 1;
        private int sortMode;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_saved_files);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Saved Statuses");
            sortMode = SORT_MODE_NEW_FIRST;
            savedFilesView = findViewById(R.id.saved_files_view);
            savedFilesView.setLayoutManager(new LinearLayoutManager(this));
            savedFilesView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            List<File> list = FileUtil.getSavedFiles(this);
            FileUtil.sortFilesNewFirst(list);
            adapter = new SavedFilesAdapter(SavedFilesActivity.this, list);
            savedFilesView.setAdapter(adapter);
        }
    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.saved_files_menu, menu);
            menu.getItem(0).setChecked(true);
            menu.getItem(2).getSubMenu().getItem(0).setChecked(true);
            return super.onCreateOptionsMenu(menu);
        }
    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case android.R.id.home:
                        super.onBackPressed();
                        break;
                case R.id.old_first:
                    sortMode = SORT_MODE_OLD_FIRST;
                    if(!item.isChecked()) {
                        item.setChecked(true);
                        List<File> list = adapter.getList();
                        Collections.reverse(list);
                        adapter.setList(list);
                        savedFilesView.removeAllViews();
                        savedFilesView.setAdapter(adapter);
                    }
                    break;
                case R.id.new_first:
                    sortMode = SORT_MODE_NEW_FIRST;
                    if(!item.isChecked()) {
                        item.setChecked(true);
                        List<File> list = adapter.getList();
                        Collections.reverse(list);
                        adapter.setList(list);
                        savedFilesView.removeAllViews();
                        savedFilesView.setAdapter(adapter);
                    }
                    break;
                case R.id.videos_and_images:
                    if(!item.isChecked()) {
                        item.setChecked(true);
                        List<File> list = FileUtil.getSavedFiles(this);
                        if (sortMode == SORT_MODE_NEW_FIRST) {
                            FileUtil.sortFilesNewFirst(list);
                        } else {
                            FileUtil.sortFilesOldFirst(list);
                        }
                        adapter = new SavedFilesAdapter(SavedFilesActivity.this, list);
                        savedFilesView.removeAllViews();
                        savedFilesView.setAdapter(adapter);
                    }
                    break;
                case R.id.images_only:
                    if(!item.isChecked()) {
                        item.setChecked(true);
                        List<File> list = FileUtil.getSavedFiles(this);
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
                        savedFilesView.removeAllViews();
                        adapter = new SavedFilesAdapter(SavedFilesActivity.this,list);
                        savedFilesView.setAdapter(adapter);
                    }
                    break;
                case R.id.videos_only:
                    if(!item.isChecked()) {
                        item.setChecked(true);
                        List<File> list = FileUtil.getSavedFiles(this);
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
                        savedFilesView.removeAllViews();
                        adapter = new SavedFilesAdapter(SavedFilesActivity.this,list);
                        savedFilesView.setAdapter(adapter);
                    }
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
    }