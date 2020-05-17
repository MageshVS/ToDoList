package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    SharedPreferences sharedPreferences;

    public ArrayList<String> array_id,array_label,array_date,array_time,array_notes;

    public ListView listView;
    private TextView textView, emptyTextView;
    private Button button;
    private ImageView emptyImageView;
    private RecyclerView recyclerView;
    public FloatingActionButton floatingActionButton;
    private String nickname;


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

        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        databasehelper = new Databasehelper(this);
        emptyImageView = (ImageView)findViewById(R.id.emptyImageView);
        emptyTextView = (TextView)findViewById(R.id.emptytextView);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatbtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent floatIntent = new Intent(MainActivity.this, AddNotesActivity.class);
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


    }

    public void viewData() {
        Cursor cursor = databasehelper.viewAllData(nickname);
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
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
    public void logout(View view){
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}