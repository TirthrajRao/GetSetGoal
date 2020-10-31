package com.example.getsetgoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.Holder> {

    Context con;
    ArrayList<MilestoneModel> milestonedata;

    public ReviewRecyclerAdapter(Context con, ArrayList<MilestoneModel> milestonedata) {
        this.con = con;
        this.milestonedata = milestonedata;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.review_recycler_item,parent,false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.msnumber.setText("MS"+milestonedata.get(position).getMilestoneNumber()+"");
        holder.daycount.setText(milestonedata.get(position).getMilstonedays()+"");
        holder.details.setText(milestonedata.get(position).getMilestoneText());


    }

    @Override
    public int getItemCount() {
        return milestonedata.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView daycount,details;
        Button msnumber;
        public Holder(@NonNull View itemView) {
            super(itemView);
            daycount = itemView.findViewById(R.id.daycount1);
            details = itemView.findViewById(R.id.milestonedetails1);
            msnumber = itemView.findViewById(R.id.msitemnumber1);
        }
    }
}
