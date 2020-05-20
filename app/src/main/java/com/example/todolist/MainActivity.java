package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    SharedPreferences sharedPreferences;

    public ArrayList<String> array_id,array_label,array_date,array_time,array_notes;

    public ListView listView;
    public TextView textView, emptyTextView,
            username, dateText, timeText, temperatureText, descText;
    private Button button;
    private ImageView emptyImageView;
    private RecyclerView recyclerView;
    public FloatingActionButton floatingActionButton;
    private String nickname;

    class Weather extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char)data;
                    content = content + ch;
                    data = inputStreamReader.read();
                }
                return content;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            finish();
            startActivity(getIntent());
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
        username = (TextView)findViewById(R.id.userName);
        dateText = (TextView)findViewById(R.id.dateText);
        timeText = (TextView)findViewById(R.id.timeText);
        temperatureText = (TextView)findViewById(R.id.temperatureView);
        descText = (TextView)findViewById(R.id.descView);


        username.setText(nickname);

        Calendar calendar  = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        Format dateFormatData = new SimpleDateFormat("EEE, dd/MM");
        Format timeFormatData = new SimpleDateFormat( "HH:mm a");
        String dateFormat = dateFormatData.format(new Date());
        String timeFormat = timeFormatData.format(new Date());

        updateWeatherInfo();


        dateText.setText(dateFormat);
        timeText.setText(timeFormat);

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

    public void updateWeatherInfo() {
        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q=Chennai&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            Log.i("content", content);

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");

            JSONArray jsonArray = new JSONArray(weatherData);

            String main = "";
            String desc = "";

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject weatherPart = jsonArray.getJSONObject(i);
                main = weatherPart.getString("main");
                desc = weatherPart.getString("description");
            }
            JSONObject mainTemp = new JSONObject(mainTemperature);
            String tempreature = mainTemp.getString("temp");
            String temp = tempreature.substring(0,2)+"\u00B0"+"C";
          //  Toast.makeText(MainActivity.this, "main "+main+" desc "+desc+" temp "+tempreature, Toast.LENGTH_LONG).show();

            temperatureText.setText(temp);
            descText.setText(desc);
        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(getApplicationContext(), "weatherApi"+e, Toast.LENGTH_LONG).show();
        }

    }

    public void viewData() {
        Cursor cursor = null;
        try {
            cursor = databasehelper.viewAllData(nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        finish();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutit){
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}