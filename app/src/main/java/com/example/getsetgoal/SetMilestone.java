package com.example.getsetgoal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SetMilestone extends AppCompatActivity {

    RecyclerView milestone;
    Button addmilestone, nextbtn, removemilestone;
    int count;
    String startdate, endDate;
    TextView enddate;
    SimpleDateFormat sdf;
    Calendar c, e;
    String textstring, goaltitle;
    ArrayList<MilestoneModel> milestonedata = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_milestone);
        count = 1;
        milestonedata.add(0, new MilestoneModel(0, ""));
        milestone = findViewById(R.id.setmilstonerecyclerview);
        addmilestone = findViewById(R.id.addmilestone);
        enddate = findViewById(R.id.enddate);
        nextbtn = findViewById(R.id.setmilstonenext);
        removemilestone = findViewById(R.id.removemilestone);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            startdate = (String) b.get("startdate");
            goaltitle = (String) b.get("goalname");

        }


        sdf = new SimpleDateFormat("dd/MM/yyyy");
        c = Calendar.getInstance();
        e = Calendar.getInstance();
        try {
            e.setTime(sdf.parse(startdate));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        e.add(Calendar.DATE, 1);
        endDate = sdf.format(e.getTime());
        enddate.setText(endDate);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetMilestone.this);
        milestone.setLayoutManager(linearLayoutManager);
        final SetMilestoneAdapter setMilestoneAdapter = new SetMilestoneAdapter(SetMilestone.this);
        milestone.setAdapter(setMilestoneAdapter);

        addmilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                milestonedata.add(count - 1, new MilestoneModel(0, ""));

                e.add(Calendar.DATE, 1);
                endDate = sdf.format(e.getTime());
                enddate.setText(endDate);

                setMilestoneAdapter.notifyItemInserted(count);
            }
        });
        removemilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    int k = milestonedata.get(count - 1).getMilstonedays();
                    milestonedata.remove(count - 1);
                    setMilestoneAdapter.notifyItemRemoved(count);
                    count = count - 1;
                    e.add(Calendar.DATE, -k);
                    endDate = sdf.format(e.getTime());
                    enddate.setText(endDate);
                }
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int t = 1;
                for (int z = 0; z < milestonedata.size(); z++) {
                    String s = milestonedata.get(z).getMilestoneText();
                    String s1 = s.replaceAll("[\\s]", "");

                    if (s1.length() == 0) {
                        t = 0;
                        Toast.makeText(SetMilestone.this, "Milestone details cannot be empty", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (t == 1) {
                    if (milestonedata.size() == 1) {
                        Toast.makeText(SetMilestone.this, "Select minimum two milestone", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(SetMilestone.this, ReviewDetails.class);
                        i.putExtra("milestonedata", milestonedata);
                        i.putExtra("startdate", startdate);
                        i.putExtra("goalname", goaltitle);
                        i.putExtra("enddate", enddate.getText().toString());
                        startActivity(i);
                        finish();
                    }
                }

            }
        });
    }

    public class SetMilestoneAdapter extends RecyclerView.Adapter<SetMilestoneAdapter.holder> {
        Context con;


        public SetMilestoneAdapter(Context con) {
            this.con = con;
        }

        @NonNull
        @Override
        public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(con).inflate(R.layout.ms_item_recycler, parent, false);
            return new holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final holder holder, final int position) {
            holder.mssnumber.setText("MS" + (position + 1));
            milestonedata.get(position).setMilstonedays(Integer.parseInt(holder.daycount.getText().toString()));
            milestonedata.get(position).setMilestoneNumber(position + 1);
            holder.increaseday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.daycount.getText().toString().equals("7")) {

                    } else if (!holder.daycount.getText().toString().equals("7")) {
                        int j = Integer.parseInt((String) holder.daycount.getText());
                        j = j + 1;
                        holder.daycount.setText(j + "");
                        milestonedata.get(position).setMilstonedays(Integer.parseInt(holder.daycount.getText().toString()));

                        e.add(Calendar.DATE, 1);
                        endDate = sdf.format(e.getTime());
                        enddate.setText(endDate);
                    }
                }
            });
            holder.reduceday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.daycount.getText().toString().equals("1")) {

                    } else if (!holder.daycount.getText().toString().equals("1")) {
                        int j = Integer.parseInt((String) holder.daycount.getText());
                        j = j - 1;
                        holder.daycount.setText(j + "");
                        milestonedata.get(position).setMilstonedays(Integer.parseInt(holder.daycount.getText().toString()));

                        e.add(Calendar.DATE, -1);
                        endDate = sdf.format(e.getTime());
                        enddate.setText(endDate);
                    }
                }
            });
            holder.details.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textstring = s.toString();
                    milestonedata.get(position).setMilestoneText(textstring);


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        @Override
        public int getItemCount() {

            return count;
        }

        public class holder extends RecyclerView.ViewHolder {
            Button mssnumber, increaseday, reduceday;
            TextView daycount, details;

            public holder(@NonNull View itemView) {
                super(itemView);
                mssnumber = itemView.findViewById(R.id.msitemnumber);
                increaseday = itemView.findViewById(R.id.increaseday);
                reduceday = itemView.findViewById(R.id.reduceday);
                daycount = itemView.findViewById(R.id.daycount);
                details = itemView.findViewById(R.id.milestonedetails);
            }
        }

    }

}
