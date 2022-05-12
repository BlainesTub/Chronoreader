package com.example.chronoreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class ReadingViewActivity extends AppCompatActivity {
    //Top header
    private ProgressBar bookProgress;
    private TextView bookTitle_view;
    private EditText wppInput_view;
    //Page sliders
    private SeekBar pageSlider;
    private ProgressBar pageSliderFill;
    private TextView manualMeasurement;
    //Bottom Actions
    private EditText pgStart_view;
    private EditText pgEnd_view;
    private Chronometer chronometer;

    //Variables
    private int wordsPerPage = 350; //FIXME Must create initialization
    private int partialPagesRead;
    private int totalPagesRead;
    private int standardPagesRead;
    private int partialWordsRead;
    private int standardWordsRead;
    private int totalWordsRead;
    private double timePerPage;
    private double wordsPerMinute;
    private boolean chronoRunning = false;
    private long pauseOffset = 0;
    private long elapsedTime = 0;
    private double elapsedMinutes;
    private double sliderFill;       //This double contains a 0-100 number dictating how full the partialPage is
    private Vector<Double> partialPagesVec = new Vector<>(0);  //This vector will hold percentages

    //Intent Variables
    public static final String readingReportDataExtra = "readingReportDataExtra";
    public static final String wordsPerMinuteExtra = "wordsPerMinuteExtra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readingview);

        //Acquire Top header
        bookProgress = findViewById(R.id.bookProgress);
        bookTitle_view = findViewById(R.id.bookTitle);
        wppInput_view = findViewById(R.id.wppInput); //FIXME Temporary field
        //Bottom Actions
        pgStart_view = findViewById(R.id.pgStart);
        pgEnd_view = findViewById(R.id.pgEnd);
        chronometer = findViewById(R.id.chronometer);
        //Acquire Page sliders
        pageSlider = findViewById(R.id.pageWidgetSlider);
        pageSliderFill = findViewById(R.id.pageWidgetFill);
        manualMeasurement = findViewById(R.id.manualMeasurement);
        //Match progressBar to SLider
        pageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wordsPerPage = Integer.parseInt(wppInput_view.getText().toString()); //FIXME Temporary WPP input
                //Converting number of lines on page to a percentage
                double pageSliderMaximum = pageSlider.getMax();
                sliderFill = progress/pageSliderMaximum*100.0;    //This will be a number between 0 to 100
                pageSliderFill.setProgress((int) sliderFill);

                //Calculating number of words on page
                int wordsPerManualPage = (int) (wordsPerPage*sliderFill/100);
                manualMeasurement.setText(String.format("%d words", wordsPerManualPage));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    
    
    
    public void startChronometer(){
        if(!chronoRunning){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            chronoRunning = true;
        }
    }
    public void stopChronometer(){
        if(chronoRunning){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronoRunning = false;
            elapsedMinutes = (pauseOffset/ 60000.0);
            System.out.println("Elapsed Minutes: " + elapsedMinutes);
        }
    }
    public void showPauseButton(){
    }  //TODO MUST CHANGE SHAPE AND COLOR TO PAUSE
    public void showPlayButton(){

    } //TODO MUST CHANGE SHAPE AND COLOR TO PLAY
    public void playPauseAction(View view){
        if (!chronoRunning){
            startChronometer();
        } else{
            stopChronometer();
        }
    }
    //hello
    public void insertPartialPage(View view) {
        wordsPerPage = Integer.parseInt(wppInput_view.getText().toString()); //FIXME Temporary WPP input
        partialPagesVec.add(sliderFill);
        partialWordsRead += sliderFill*wordsPerPage/100; //sliderFill is a number between 0-100. Must divide to obtain percentage.
        Log.d("ReadingViewActivity", "Adding partialPage: "+ sliderFill);
        Log.d("ReadingViewActivity", "Adding " +sliderFill*wordsPerPage/100 + "words");
        Log.d("ReadingViewActivity", "Vector: " + partialPagesVec.toString());
    }
    public void generateReport(View view){
        wordsPerPage = Integer.parseInt(wppInput_view.getText().toString()); //FIXME Temporary WPP input
        //Need to report:
        //Elapsed Time TODO, CONVERT MILISECONDS TO HH:MM:SS
        Log.d("ReadingViewActivity", "Words" + TimeUnit.MILLISECONDS.toHours(elapsedTime) + ":" + TimeUnit.MILLISECONDS.toMinutes(elapsedTime)+ ":"+TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
        //Elapsed Minutes
        Log.d("ReadingViewActivity", "Elapsed Minutes: " + elapsedMinutes);
        //Total Pages Read
        totalPagesRead = Integer.parseInt(pgEnd_view.getText().toString()) - Integer.parseInt((pgStart_view.getText().toString()));
        Log.d("ReadingViewActivity", "Total Pages Read: " + totalPagesRead);
        //partialPages Read
        partialPagesRead = partialPagesVec.size();
        Log.d("ReadingViewActivity", "Partial Pages Read: " + partialPagesRead);
        //Standard Pages Read
        standardPagesRead = totalPagesRead - partialPagesRead;
        Log.d("ReadingViewActivity", "Standard Pages Read: "+ standardPagesRead);
        //Average time per page
        timePerPage = elapsedMinutes/totalPagesRead;
        Log.d("ReadingViewActivity", "Time Per Page: "+ timePerPage);
        //Approximate #Words Read
        //Partial Words Read
        Log.d("ReadingViewActivity", "Partial Words Read: "+ partialWordsRead);
        //Standard Words Read
        standardWordsRead = wordsPerPage * standardPagesRead;
        Log.d("ReadingViewActivity","Standrad Words Read: "+ standardWordsRead);
        //Total Words Read
        totalWordsRead = standardWordsRead + partialWordsRead;
        Log.d("ReadingViewActivity", "Total Words Read: " + totalWordsRead);
        //Words per minute
        wordsPerMinute = totalWordsRead/elapsedMinutes;
        Log.d("ReadingViewActivity", "Words Per Minute: " + wordsPerMinute);

        //Time elapsed, mins elapsed, stdPgs prtlPgs stdWrds, PrtlWrds, TotlWrds, Mins/page
        String message1 = "--" + "\n"
                + elapsedMinutes + "\n"
                + standardPagesRead + "\n"
                + partialPagesRead + "\n"
                + standardWordsRead + "\n"
                + partialWordsRead + "\n"
                + totalWordsRead + "\n"
                + timePerPage;
        double message2 = wordsPerMinute;

/*
        ReadingReportDialog readingReportDialog = new ReadingReportDialog();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", message1);
        readingReportDialog.setArguments(bundle);
        readingReportDialog.show(getSupportFragmentManager(), "ReaadingReport Diaalog");
        TextView readingReportDataView = (TextView) alert
*/

    }

    public void generateReport2(View vew) {
        wordsPerPage = Integer.parseInt(wppInput_view.getText().toString()); //FIXME Temporary WPP input
        //Need to report:
        //Elapsed Time TODO, CONVERT MILISECONDS TO HH:MM:SS
        Log.d("ReadingViewActivity", "Words" + TimeUnit.MILLISECONDS.toHours(elapsedTime) + ":" + TimeUnit.MILLISECONDS.toMinutes(elapsedTime)+ ":"+TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
        //Elapsed Minutes
        Log.d("ReadingViewActivity", "Elapsed Minutes: " + elapsedMinutes);
        //Total Pages Read
        totalPagesRead = Integer.parseInt(pgEnd_view.getText().toString()) - Integer.parseInt((pgStart_view.getText().toString()));
        Log.d("ReadingViewActivity", "Total Pages Read: " + totalPagesRead);
        //partialPages Read
        partialPagesRead = partialPagesVec.size();
        Log.d("ReadingViewActivity", "Partial Pages Read: " + partialPagesRead);
        //Standard Pages Read
        standardPagesRead = totalPagesRead - partialPagesRead;
        Log.d("ReadingViewActivity", "Standard Pages Read: "+ standardPagesRead);
        //Average time per page
        timePerPage = elapsedMinutes/totalPagesRead;
        Log.d("ReadingViewActivity", "Time Per Page: "+ timePerPage);
        //Approximate #Words Read
        //Partial Words Read
        Log.d("ReadingViewActivity", "Partial Words Read: "+ partialWordsRead);
        //Standard Words Read
        standardWordsRead = wordsPerPage * standardPagesRead;
        Log.d("ReadingViewActivity","Standrad Words Read: "+ standardWordsRead);
        //Total Words Read
        totalWordsRead = standardWordsRead + partialWordsRead;
        Log.d("ReadingViewActivity", "Total Words Read: " + totalWordsRead);
        //Words per minute
        wordsPerMinute = totalWordsRead/elapsedMinutes;
        Log.d("ReadingViewActivity", "Words Per Minute: " + wordsPerMinute);


        //Time elapsed, mins elapsed, stdPgs prtlPgs stdWrds, PrtlWrds, TotlWrds, Mins/page








        //create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the custo layout
        final View readingReportLayout = getLayoutInflater().inflate(R.layout.dialog_readingreport, null);
        builder.setTitle("Reading Report");
        builder.setView(readingReportLayout)                // Adding action buttons
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        //create and show the alert dialog
        AlertDialog readingReportDialog = builder.create();
        readingReportDialog.show();
        TextView readingReportData = readingReportLayout.findViewById(R.id.readingReportData);
        TextView readingReportWPM = readingReportLayout.findViewById(R.id.readingSpeed);
        readingReportData.setText(String.format("-- \n%.2f \n%d \n%d \n%d \n%d \n%d \n%.2f \n", elapsedMinutes, standardPagesRead, partialPagesRead, standardWordsRead, partialWordsRead, totalWordsRead, timePerPage));
        readingReportWPM.setText(String.format("Reading Speed:\n %.2f Words Per Minute", wordsPerMinute));
    }

}