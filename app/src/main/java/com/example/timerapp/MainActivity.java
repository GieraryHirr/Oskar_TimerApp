package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    EditText editTitle;
    NumberPicker numberPickerHours, numberPickerMinutes, numberPickerSeconds;
    private LinearLayout timerContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerContainer = findViewById(R.id.timerContainer);

        numberPickerHours = findViewById(R.id.edit_timer_hours);
        numberPickerMinutes = findViewById(R.id.edit_timer_minutes);
        numberPickerSeconds = findViewById(R.id.edit_timer_seconds);

        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);

        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

        editTitle = findViewById(R.id.edit_title);

        Button btnRun = findViewById(R.id.btn_run);
        Button btnAdd = findViewById(R.id.btn_add);

        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTimer(true);
                numberPickerHours.setValue(0);
                numberPickerMinutes.setValue(0);
                numberPickerSeconds.setValue(0);
                editTitle.setText("");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTimer(false);
                numberPickerHours.setValue(0);
                numberPickerMinutes.setValue(0);
                numberPickerSeconds.setValue(0);
                editTitle.setText("");
            }
        });
    }

    private void runTimer(boolean run) {
        boolean isRunning = false;
        int hours = numberPickerHours.getValue();
        int minutes = numberPickerMinutes.getValue();
        int seconds = numberPickerSeconds.getValue();

        long timeInMillis = ((hours * 60 * 60) + minutes * 60 + seconds) * 1000;

        if (timeInMillis == 0) return;

        View timerRowView = LayoutInflater.from(this).inflate(R.layout.timer_row_layout, timerContainer, false);

        TextView titleTextView = timerRowView.findViewById(R.id.titleTextView);
        TextView timerTextView = timerRowView.findViewById(R.id.timerTextView);
        Button pauseButton = timerRowView.findViewById(R.id.pauseButton);
        Button clearButton = timerRowView.findViewById(R.id.clearButton);

        titleTextView.setText(editTitle.getText().toString());

        timerContainer.addView(timerRowView);

        Timer timer = new Timer(this, timerTextView, timeInMillis);

        if(run) {
            Thread object = new Thread(timer);
            object.start();
        } else {
            NumberFormat f = new DecimalFormat("00");
            long hour = timeInMillis / 3600000;
            long min = (timeInMillis / 60000) % 60;
            long sec = (timeInMillis / 1000) % 60;
            timerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            pauseButton.setText("Start");
        }


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pauseButton.getText().toString().equals("Start")) {
                    Thread object = new Thread(timer);
                    object.start();
                    pauseButton.setText("Pause");
                } else {
                    boolean isPaused = timer.pause();
                    if(isPaused) pauseButton.setText("Resume");
                    else pauseButton.setText("Rause");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.clear();
                timerContainer.removeView(timerRowView);
            }
        });
    }
}
