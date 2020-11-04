package com.example.getsetgoal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class MilestonesFragment extends Fragment {

    AlertDialog.Builder db;
    AlertDialog alertDialog;

    String name;
    int id;
    SQLiteDatabase database;
    ArrayList<MilestoneModel> milestonedata = new ArrayList<>();
    TextView gname;
    RecyclerView msdata;

    View view;
    Motionpathadapter motionpathadapter;

    int completedms=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_milestones, container, false);
        gname = view.findViewById(R.id.fianltitle);
        msdata = view.findViewById(R.id.finalrecycler);
        name = getArguments().getString("name");
        gname.setText("Goal - " + name);
        id = getArguments().getInt("id");
        database = requireActivity().openOrCreateDatabase("goalDb", Context.MODE_PRIVATE, null);
        Cursor cur1 = database.rawQuery("select * from MilestoneDetails where Goal_id='" + id + "' ", null);
        if (cur1 != null) {
            int milestonenumberindex = cur1.getColumnIndex("Milestone_Number");
            int milestonetextindex = cur1.getColumnIndex("Milestone_Text");
            int milestonedaysindex = cur1.getColumnIndex("Milestone_Days");
            int milestonestartdateindex = cur1.getColumnIndex("Milestone_Startdate");
            int milestoneenddateindex = cur1.getColumnIndex("Milestone_Enddate");
            int milestoneIscompletedIndex = cur1.getColumnIndex("Milestone_Iscomplete");
            while (cur1.moveToNext()) {
                int milestonenumber = cur1.getInt(milestonenumberindex);
                String milestonetext = cur1.getString(milestonetextindex);
                int milestonedays = cur1.getInt(milestonedaysindex);
                String milestonestartdate = cur1.getString(milestonestartdateindex);
                String milestoneenddate = cur1.getString(milestoneenddateindex);
                int milestoneIscompleted = cur1.getInt(milestoneIscompletedIndex);
                completedms=completedms+milestoneIscompleted;
                milestonedata.add(new MilestoneModel(milestonenumber, milestonedays, milestonetext, milestonestartdate, milestoneenddate,milestoneIscompleted));
            }
            Collections.reverse(milestonedata);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            msdata.setLayoutManager(linearLayoutManager);

            motionpathadapter = new Motionpathadapter(getActivity(),milestonedata, new Motionpathadapter.MilestoneInterface(){
                @Override
                public void onmilestoneUpdate(final MilestoneModel milestoneModel){
                    ContentValues cv = new ContentValues();
                    cv.put("Milestone_Iscomplete",1); //These Fields should be your String values of actual column names
                    database.update("MilestoneDetails", cv, "Milestone_Number="+milestoneModel.getMilestoneNumber()+" AND "+"Goal_id="+id, null);
                }
            },(milestonedata.size()==completedms) ? true : false);
            msdata.setAdapter(motionpathadapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static MilestonesFragment addfrag(String val, int val2) {
        MilestonesFragment fragment = new MilestonesFragment();
        Bundle args = new Bundle();
        args.putString("name", val);
        args.putInt("id", val2);
        fragment.setArguments(args);
        return fragment;
    }
}