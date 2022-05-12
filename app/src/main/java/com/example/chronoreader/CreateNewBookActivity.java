package com.example.chronoreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Object;
import java.nio.charset.StandardCharsets;

import java.util.UUID;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;



public class CreateNewBookActivity extends AppCompatActivity {
    public static final BookObject EXTRA_MESSAGE = new BookObject();

    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookPageCount;
    private EditText bookGenreSpinner; //TODO FIGURE OUT SPINNERS!
    private EditText bookWordsPerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor =  findViewById(R.id.bookAuthor);
        bookPageCount = findViewById(R.id.bookPageCount);
        //TODO Create bookGenreSpinner
        bookWordsPerPage = findViewById(R.id.bookWordsPerPage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_new_book, menu);
        return true;
    }

    public void createNewBookAction(MenuItem item) {
        UUID newBookUUID = UUID.randomUUID();
        BookObject newBook = new BookObject(newBookUUID, bookTitle.getText().toString(),
                                            bookAuthor.getText().toString(),
                                            "genre",
                                            Integer.parseInt(bookPageCount.getText().toString()),
                                            Integer.parseInt(bookWordsPerPage.getText().toString()));

        Log.d("createNewBook", "\nUUID: " + newBook.getuuid()
                                        +"\nTitle: "+ newBook.getTitle()
                                        +"\nAuthor: "+ newBook.getAuthor()
                                        +"\nGenre: "+ newBook.getGenre()
                                        +"\npageCount: "+newBook.getGenre()
                                        +"\nwordsPerPg: "+newBook.getWordsPerPage());

        String dirPath2 = getFilesDir()+ File.separator + newBook.getTitle(); //Creating Dir string
        File newBookDir2 = new File(dirPath2);  //Converting Dir string to File object
        String newBookPath2 = newBookDir2+File.separator+ newBook.getTitle() + ".json";
        try {
            //Creating Book Folder
            String filename = newBook.getTitle() + ".json"; //Creating Filename
            String dirPath = getFilesDir()+ File.separator + newBook.getTitle(); //Creating Dir string
            File newBookDir = new File(dirPath);  //Converting Dir string to File object
            if (!newBookDir.exists())  newBookDir.mkdirs(); //Create dir if it doesnt exist

            //Initializing File Creation
            String newBookPath = newBookDir+File.separator+filename;
            FileOutputStream fos = new FileOutputStream(newBookPath);

            //Converting book object to JSON and writing.
            Gson gson = new Gson();
            String newBookJSON = gson.toJson(newBook);
            Log.d("createNewBook", newBookJSON);
            fos.write(newBookJSON.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "Book Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("createNewBook", "FileCreated?");



        try {
            Log.d("createNewBook", "Searching at dir:" + newBookPath2);
            FileInputStream fis = new FileInputStream(newBookPath2);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String str;
            while((str = bufferedReader.readLine()) != null){
                sb.append(str);
            }
            String retrievedBook = sb.toString();
            Log.d("createNewBook", retrievedBook);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Intent intent = new Intent(this, BookDashboardActivity.class);
        intent.putExtra("bookUUID",newBookUUID.toString());
        startActivity(intent);
    }
}