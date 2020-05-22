package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private AnyChartView pieChart;
    SharedPreferences sharedPreferences;
    Databasehelper databasehelper;
    String nickname;

    ArrayList<String> array_label_profile;
    ArrayList<Integer> array_activity_profile;
    String[] labels;
    Integer[] activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary, null));
        toolbar.setTitleTextAppearance(this, R.style.righteous_regular);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        databasehelper = new Databasehelper(this);
        array_label_profile = new ArrayList<>();
        array_activity_profile = new ArrayList<>();

        pieChart = (AnyChartView)findViewById(R.id.pieChart);
        viewLabelData();

        viewPieChart();
    }


    public void viewLabelData() {
        Cursor cursor ;
        cursor = databasehelper.userChartInfo(nickname);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    array_label_profile.add(cursor.getString(0));
                    array_activity_profile.add(cursor.getInt(1));
                }
            }
            labels = new String[array_label_profile.size()];
            activity = new Integer[array_activity_profile.size()];
            array_label_profile.toArray(labels);
            array_activity_profile.toArray(activity);
            Toast.makeText(ProfileActivity.this, "array : "+labels[0], Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "array : "+e, Toast.LENGTH_LONG).show();
        }
    }
    private void viewPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i< array_label_profile.size(); i++){
            dataEntries.add(new ValueDataEntry(labels[i], activity[i]));
        }
        pie.data(dataEntries);
        pieChart.setChart(pie);
    }

    public void logout(){
        SessionManagement sessionManagement = new SessionManagement(ProfileActivity.this);
        sessionManagement.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void moveToHome(){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
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
        else if(item.getItemId() == R.id.profile){
            return true;
        }
        else if(item.getItemId() == R.id.home){
            moveToHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
