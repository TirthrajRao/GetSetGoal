package com.example.getsetgoal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class GoalTitle extends AppCompatActivity {

    TextView startDate;
    Button nextbtn;
    EditText goaltitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_title);

        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int month = Calendar.getInstance().get(Calendar.MONTH);
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        int month1 = month + 1;
        startDate = findViewById(R.id.startdate);
        nextbtn = findViewById(R.id.goaltitlenext);
        goaltitle = findViewById(R.id.goaltitle);

        startDate.setText(day + "/" + month1 + "/" + year);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(GoalTitle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = goaltitle.getText().toString();
                String s1 = title.replaceAll("[\\s]", "");

                if (s1.length() != 0) {
                    Intent i = new Intent(GoalTitle.this, SetMilestone.class);
                    i.putExtra("startdate", startDate.getText().toString());
                    i.putExtra("goalname", goaltitle.getText().toString());
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(GoalTitle.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
