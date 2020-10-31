package com.example.getsetgoal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FinalDetails extends AppCompatActivity {

    SQLiteDatabase database;
    ArrayList<String> goalnames = new ArrayList<>();
    ArrayList<Integer> id = new ArrayList<>();

    TabLayout tab;
    ViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalview);


        tab = findViewById(R.id.tabs);
        vp = findViewById(R.id.vp);

        database = openOrCreateDatabase("goaldata", Context.MODE_PRIVATE, null);
        database.execSQL("create table  if not exists GoalDetails (Goal_id integer primary key autoincrement,Goal_Name text,Goal_Startdate text,Goal_Enddate text,Goal_State integer default 1,GoalCreatedDate text,GoalUpdatedDate text)");
        database.execSQL("create table  if not exists MilestoneDetails (Milestone_id integer primary key autoincrement,Goal_id integer,Milestone_Number integer,Milestone_Text text,Milestone_Days integer,Milestone_Startdate text,Milestone_Enddate text,foreign key(Goal_id) references GoalDetails(Goal_id))");

        Cursor cur = database.rawQuery("select * from GoalDetails ", null);
        if (cur != null) {
            int nameindex = cur.getColumnIndex("Goal_Name");
            int idindex = cur.getColumnIndex("Goal_id");
            while (cur.moveToNext()) {
                String name = cur.getString(nameindex);
                int Gid = cur.getInt(idindex);
                id.add(Gid);
                goalnames.add(name);
            }
            for (int i = 0; i < goalnames.size(); i++) {
                tab.addTab(tab.newTab().setText(goalnames.get(i) + ""));
            }

            TabAdapter adapter = new TabAdapter
                    (getSupportFragmentManager(), tab.getTabCount());
            vp.setAdapter(adapter);
            vp.setOffscreenPageLimit(1);
            vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
            tab.setupWithViewPager(vp);
            for (int i = 0; i < goalnames.size(); i++) {
                tab.getTabAt(i).setText("G " + (i + 1));
            }


        }

        if(id.size()==0){
            Intent in = new Intent(FinalDetails.this,MainActivity.class);
            startActivity(in);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                if (tab.getTabCount() < 3) {
                    Intent i = new Intent(FinalDetails.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(FinalDetails.this, "you reach max goal limit", Toast.LENGTH_SHORT).show();

                }
                return true;
            case R.id.edit1 :
                int k = tab.getSelectedTabPosition();
                String name = goalnames.get(k);

                Cursor cur1 = database.rawQuery("select * from MilestoneDetails where Goal_id='" + (id.get(k)) + "' ", null);
                ArrayList<MilestoneModel> milestonedata = new ArrayList<>();
                if (cur1 != null) {
                    int milestonenumberindex = cur1.getColumnIndex("Milestone_Number");
                    int milestonetextindex = cur1.getColumnIndex("Milestone_Text");
                    int milestonedaysindex = cur1.getColumnIndex("Milestone_Days");
                    int milestonestartdateindex = cur1.getColumnIndex("Milestone_Startdate");
                    int milestoneenddateindex = cur1.getColumnIndex("Milestone_Enddate");
                    while (cur1.moveToNext()) {
                        int milestonenumber = cur1.getInt(milestonenumberindex);
                        String milestonetext = cur1.getString(milestonetextindex);
                        int milestonedays = cur1.getInt(milestonedaysindex);
                        String milestonestartdate = cur1.getString(milestonestartdateindex);
                        String milestoneenddate = cur1.getString(milestoneenddateindex);
                        milestonedata.add(new MilestoneModel(milestonenumber, milestonedays, milestonetext, milestonestartdate, milestoneenddate));
                    }
                }
                Intent i = new Intent(FinalDetails.this, ClickAndPickEdit.class);
                i.putExtra("startdate", milestonedata.get(0).getMilestoneStartdate());
                i.putExtra("goalname", name);
                i.putExtra("goalid", id.get(k));
                i.putExtra("milestonedata", milestonedata);
                i.putExtra("enddate", milestonedata.get(milestonedata.size() - 1).getMilestoneEnddate());
                startActivity(i);
                return true;
            case R.id.delete:
                int j = tab.getSelectedTabPosition();
                database.execSQL("delete from GoalDetails where Goal_id='" + (id.get(j)) + "'");
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public class TabAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        public TabAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return GoalFragment.addfrag(goalnames.get(position), id.get(position));
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
