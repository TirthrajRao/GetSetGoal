package com.example.getsetgoal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GoalFragment extends Fragment {
    String name;
    int id;
    SQLiteDatabase database;
    ArrayList<MilestoneModel> milestonedata = new ArrayList<>();
    TextView gname;
    RecyclerView msdata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.finaldetails, container, false);
        gname = view.findViewById(R.id.fianltitle);
        msdata = view.findViewById(R.id.finalrecycler);
        name = getArguments().getString("name");
        gname.setText("Goal - "+name);
        id = getArguments().getInt("id");
        database = requireActivity().openOrCreateDatabase("goaldata", Context.MODE_PRIVATE, null);
        Cursor cur1 = database.rawQuery("select * from MilestoneDetails where Goal_id='" + id + "' ", null);
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


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            msdata.setLayoutManager(linearLayoutManager);

            FinalRecyclerAdapter finalRecyclerAdapter = new FinalRecyclerAdapter(requireContext(), milestonedata);
            msdata.setAdapter(finalRecyclerAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static GoalFragment addfrag(String val, int val2) {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        args.putString("name", val);
        args.putInt("id", val2);
        fragment.setArguments(args);
        return fragment;
    }

}
