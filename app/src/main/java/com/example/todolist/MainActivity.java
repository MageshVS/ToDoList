package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Databasehelper databasehelper;

    public ArrayList<String> array_id,array_label,array_date,array_time,array_notes;

    public ListView listView;
    private TextView textView;
    private Button button;
    private RecyclerView recyclerView;
    public FloatingActionButton floatingActionButton;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary, null));
        toolbar.setTitleTextAppearance(this, R.style.righteous_regular);
        setSupportActionBar(toolbar);

        databasehelper = new Databasehelper(this);
        textView = (TextView) findViewById(R.id.addNotes);
        listView = (ListView)findViewById(R.id.listView);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatbtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent floatIntent = new Intent(MainActivity.this,AddNotes.class);
                startActivity(floatIntent);
            }
        });

        databasehelper = new Databasehelper(MainActivity.this);
        array_id = new ArrayList<>();
        array_label= new ArrayList<>();
        array_date = new ArrayList<>();
        array_time = new ArrayList<>();
        array_notes = new ArrayList<>();

        viewData();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        DataAdapterClass dataAdapterClass = new DataAdapterClass(MainActivity.this, this,
                array_id, array_label, array_date, array_time, array_notes);
        recyclerView.setAdapter(dataAdapterClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        deleteData();
    }

    public void viewData() {
        Cursor cursor = databasehelper.viewAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), " not Empty", Toast.LENGTH_LONG).show();
            while (cursor.moveToNext()) {
                array_id.add(cursor.getString(0));
                array_label.add(cursor.getString(1));
                array_date.add(cursor.getString(2));
                array_time.add(cursor.getString(3));
                array_notes.add(cursor.getString(4));

            }

        }
    }
    public  void deleteData(){
        button = (Button)findViewById(R.id.deletebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deleteRows = databasehelper.deleteData("May 12, 2020");
                if(deleteRows != 0){
                    Toast.makeText(getApplicationContext(), "Data deleted", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Data Not Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}