package com.example.chronoreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.UUID;


public class BookDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_dashboard);
        String bookUUID = getIntent().getStringExtra("bookUUID");
        Log.d("BookDashboardActivity", "UUID: " + bookUUID);



    }

    public void startSession(View view) {
        Intent intent = new Intent(this, ReadingViewActivity.class);
        startActivity(intent);
    }
}