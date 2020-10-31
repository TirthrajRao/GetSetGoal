package com.example.getsetgoal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MotionpathRecyclerview extends AppCompatActivity {

    RecyclerView rcymotionpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motionpath_recyclerview);
        rcymotionpath=findViewById(R.id.rcymotionpath);

        rcymotionpath.setLayoutManager(new LinearLayoutManager(MotionpathRecyclerview.this));
        rcymotionpath.setAdapter(new Motionpathadapter());
    }
}