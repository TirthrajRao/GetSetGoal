package com.example.getsetgoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FinalRecyclerAdapter extends RecyclerView.Adapter<FinalRecyclerAdapter.Holder> {
    Context con;
    ArrayList<MilestoneModel> milestonedata;

    public FinalRecyclerAdapter(Context con, ArrayList<MilestoneModel> milestonedata) {
        this.con = con;
        this.milestonedata = milestonedata;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.finaldetail_recycler,parent,false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.mnumber.setText("MS"+milestonedata.get(position).getMilestoneNumber()+"");
        holder.mdetails.setText(milestonedata.get(position).getMilestoneText());
        holder.mdays.setText(milestonedata.get(position).getMilstonedays()+"");
        holder.mstartdate.setText(milestonedata.get(position).getMilestoneStartdate());
        holder.menddate.setText(milestonedata.get(position).getMilestoneEnddate());

    }

    @Override
    public int getItemCount() {
        return milestonedata.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView mnumber,mstartdate,menddate,mdetails,mdays;
        public Holder(@NonNull View itemView) {
            super(itemView);

            mnumber= itemView.findViewById(R.id.mnumber);
            mdetails= itemView.findViewById(R.id.mdetails);
            mdays= itemView.findViewById(R.id.mdays);
            mstartdate= itemView.findViewById(R.id.msdate);
            menddate= itemView.findViewById(R.id.medate);
        }
    }
}
