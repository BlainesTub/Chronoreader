package com.example.chronoreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_book, menu);
        return true;
    }
    public void openNoTW(View view) {
        Intent intent = new Intent(this, BookDashboardActivity.class);
        startActivity(intent);
    }

    public void createNewBookIntent(View view) {
        Intent intent = new Intent(this, CreateNewBookActivity.class);
        startActivity(intent);
    }
}