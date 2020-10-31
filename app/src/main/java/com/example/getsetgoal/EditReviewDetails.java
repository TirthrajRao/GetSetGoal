package com.example.getsetgoal;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditReviewDetails extends AppCompatActivity {
    TextView startdate, enddate, goaltitle;
    RecyclerView details;
    Button editbtn, confirmandcreate;
    SimpleDateFormat sdf;
    Calendar c, e;
    ArrayList<MilestoneModel> milestonedata;
    SQLiteDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_milestone);
        startdate = findViewById(R.id.re_startdate);
        enddate = findViewById(R.id.re_enddate);
        details = findViewById(R.id.re_recycler);
        editbtn = findViewById(R.id.editbtn);
        goaltitle = findViewById(R.id.reviewtitle);
        confirmandcreate = findViewById(R.id.confirmandcreate);

        database = openOrCreateDatabase("goaldata", Context.MODE_PRIVATE, null);
        database.execSQL("create table  if not exists GoalDetails (Goal_id integer primary key autoincrement,Goal_Name text,Goal_Startdate text,Goal_Enddate text,Goal_State integer default 1,GoalCreatedDate text,GoalUpdatedDate text)");
        database.execSQL("create table  if not exists MilestoneDetails (Milestone_id integer primary key autoincrement,Goal_id integer,Milestone_Number integer,Milestone_Text text,Milestone_Days integer,Milestone_Startdate text,Milestone_Enddate text,foreign key(Goal_id) references GoalDetails(Goal_id))");


        final String sdate = getIntent().getStringExtra("startdate");
        final String edate = getIntent().getStringExtra("enddate");
        final String title = getIntent().getStringExtra("goalname");
        final int id = getIntent().getIntExtra("goalid",0);

        goaltitle.setText(title);


        milestonedata = (ArrayList<MilestoneModel>) getIntent().getSerializableExtra("milestonedata");
        startdate.setText(sdate);
        enddate.setText(edate);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        c = Calendar.getInstance();
        final String createddate = sdf.format(c.getTime());

        try {
            int daycount = 0;
            for (int i = 0; i < milestonedata.size(); i++) {
                if (i == 0) {
                    milestonedata.get(0).setMilestoneStartdate(sdate);
                } else {
                    c.setTime(sdf.parse(sdate));
                    daycount = daycount + milestonedata.get(i - 1).getMilstonedays();
                    c.add(Calendar.DATE, daycount);
                    String date = sdf.format(c.getTime());
                    milestonedata.get(i).setMilestoneStartdate(date);
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        e = Calendar.getInstance();
        try {
            int daycount1 = 0;
            for (int i = 0; i < milestonedata.size(); i++) {
                e.setTime(sdf.parse(sdate));
                daycount1 = daycount1 + milestonedata.get(i).getMilstonedays();
                e.add(Calendar.DATE, daycount1);
                String date = sdf.format(e.getTime());
                milestonedata.get(i).setMilestoneEnddate(date);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditReviewDetails.this);
        details.setLayoutManager(linearLayoutManager);
        ReviewRecyclerAdapter detailsadpter = new ReviewRecyclerAdapter(EditReviewDetails.this, milestonedata);
        details.setAdapter(detailsadpter);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmandcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("delete from MilestoneDetails where Goal_id='"+id+"'");

                database.execSQL("update GoalDetails set Goal_Enddate ='"+edate+"',GoalUpdatedDate = '"+createddate+"' where Goal_id ='"+id+"'");

                for (int j = 0; j < milestonedata.size(); j++) {
                    database.execSQL("insert into MilestoneDetails (Goal_id,Milestone_Number,Milestone_Text,Milestone_Days,Milestone_Startdate,Milestone_Enddate) values ('" + id + "','" + milestonedata.get(j).getMilestoneNumber() + "','" + milestonedata.get(j).getMilestoneText()+ "','" +milestonedata.get(j).getMilstonedays()+ "','" + milestonedata.get(j).getMilestoneStartdate()+ "','" + milestonedata.get(j).getMilestoneEnddate() + "')");
                }


                Intent i = new Intent(EditReviewDetails.this, FinalDetails.class);
                i.putExtra("milestonedata", milestonedata);
                i.putExtra("startdate", startdate.getText().toString());
                i.putExtra("goalname", goaltitle.getText().toString());
                i.putExtra("enddate", enddate.getText().toString());
                startActivity(i);
                finish();

            }
        });


    }
}
