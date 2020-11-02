package com.example.getsetgoal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MotionpathRecyclerview extends AppCompatActivity {

    String name;
    int id;
    SQLiteDatabase database;
    ArrayList<MilestoneModel> milestonedata = new ArrayList<>();

    RecyclerView rcymotionpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motionpath_recyclerview);
        rcymotionpath=findViewById(R.id.rcymotionpath);

//        rcymotionpath.setLayoutManager(new LinearLayoutManager(MotionpathRecyclerview.this));
//        rcymotionpath.setAdapter(new Motionpathadapter());

    }
}