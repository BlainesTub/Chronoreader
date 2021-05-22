package com.example.readometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Vector;

public class ReadingReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_report);
        Intent intent = getIntent();

        double elapsedMinutes = intent.getDoubleExtra(MainActivity.elapsedMinutesExtra, -1);
        double standardPagesRead = intent.getDoubleExtra(MainActivity.standardPagesReadExtra, -1);
        double manualPagesRead = intent.getDoubleExtra(MainActivity.manualPagesReadExtra, -1);
        double manualWordsRead = intent.getDoubleExtra(MainActivity.manualWordsReadExtra, -1);
        double wordsRead;
        String wppString = getResources().getString(R.string.wpp_TextView);
        double wpp = Double.parseDouble(wppString);
        double wpm;

        //Calculate Words per minute
        wordsRead = standardPagesRead * wpp;
        wordsRead += manualWordsRead;
        wpm = wordsRead/elapsedMinutes;
        Log.d("MainActivity", "Elapsed Minutes: " + elapsedMinutes);
        Log.d("MainActivity", "Standard Pages Read: " + standardPagesRead);
        Log.d("MainActivity", "Manual Pages Read: " + manualPagesRead);
        Log.d("MainActivity", "Words Read: " + wordsRead);
        Log.d("MainActivity", "Words per minute:" + wpm);

        //Update the screen
        TextView elapsedMinutesView = findViewById(R.id.elapsedMinsView);
        TextView standardPagesReadView = findViewById(R.id.standardPagesReadView);
        TextView manualPagesReadView = findViewById(R.id.manualPagesReadView);
        TextView totalWordsReadView = findViewById(R.id.totalWordsReadView);
        TextView wpmView = findViewById(R.id.wpmView);

        elapsedMinutesView.setText(String.format("Elapsed Minutess: %.3f", elapsedMinutes));
        standardPagesReadView.setText(String.format("Standard Pages: %.0f", standardPagesRead));
        manualPagesReadView.setText(String.format("Manual Pages: %.0f", manualPagesRead));
        totalWordsReadView.setText(String.format("Words Read: %.0f", wordsRead));
        wpmView.setText(String.format("%.2f wpm", wpm));


    }


}