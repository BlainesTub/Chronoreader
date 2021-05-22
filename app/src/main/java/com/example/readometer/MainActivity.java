package com.example.readometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    //Calculation Values
    private TextView wpp_view;
    private TextView wpm_view;
    private EditText pageStart_view;
    private EditText pageEnd_view;
    private TextView pageCount_view;
    //Variables
    private double standardPagesRead;
    private double wpp;
    private double wordsRead;
    private double wpm;

    //Chronometer Values
    private Chronometer chronometer;   //There is a chronometer class. Built in
    private long pauseOffset = 0;
    private boolean running;
    private double elapsedMinutes=1; //initialize as 1 to prevent dividing by 9
    //Slider
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private TextView manualWordCount_view;
    private int manualWordCount;
    private double manualPagesRead;
    private Vector<Integer> manualPagesVec = new Vector<>(0);
    private double manualWordsRead=0;

    //Intent Variables
    public static final String elapsedMinutesExtra = "elapsedMinutesExtra";
    public static final String standardPagesReadExtra = "standardPagesReadExtra";
    public static final String manualPagesReadExtra = "manualPagesReadExtra";
    public static final String manualWordsReadExtra = "manualWordsReadExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //WPM Calculation Get all the Views
        wpp_view = findViewById(R.id.wpp_TextView);
        wpm_view = findViewById(R.id.wpm_TextView);
        pageStart_view = findViewById(R.id.pageStart_EditText);
        pageEnd_view = findViewById(R.id.pageEnd_EditText);
        pageCount_view = findViewById(R.id.PageCount_TextView);

        //Chronometer
        chronometer = findViewById(R.id.stopwatch);         //assigns our chronometer element to the chronometer class we created
        //chronometer.setFormat("Time: %s");                    //concatenates a string to the timer. %s is the timer value
        chronometer.setBase(SystemClock.elapsedRealtime());    //Initializes the chrono "set format" to the textview

        //Slider
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        manualWordCount_view = (TextView) findViewById(R.id.manualWordCount_TextView);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                wpp = Integer.parseInt(wpp_view.getText().toString());
                int wppInt = (int) wpp;
                manualWordCount = progress*wppInt/100;
                manualWordCount_view.setText(String.format("%d Words", manualWordCount));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    public void StartChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void ResetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
    public void plusManualPage(View view) {
        manualPagesVec.add(manualWordCount);
        Log.d("MainActivity","VecElements" + manualPagesVec);
        manualWordsRead += manualWordCount;
    }

    public void launchReadingReport(View view) {
        Log.d("MainActivity", "Button Clicked!");

        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            elapsedMinutes = (double) (pauseOffset /(double) 60000.0); //pauseOffset(millis) / convert to minutes
            Log.d("MainActivity", "Minutes: " + elapsedMinutes);
        }


        manualPagesRead = manualPagesVec.size();
        standardPagesRead = (double) Long.parseLong(pageEnd_view.getText().toString()) - Long.parseLong(pageStart_view.getText().toString()) - manualPagesRead ; //Reader ends on the next page they have not yet read.

        Intent intent = new Intent(this, ReadingReportActivity.class);
        double message1 = elapsedMinutes;
        double message2 = standardPagesRead;
        double message3 = manualPagesRead;
        double message4 = manualWordsRead;

        intent.putExtra(elapsedMinutesExtra, message1);
        intent.putExtra(standardPagesReadExtra, message2);
        intent.putExtra(manualPagesReadExtra, message3);
        intent.putExtra(manualWordsReadExtra, message4);
        startActivity(intent);
    }
}