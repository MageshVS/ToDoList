package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private AnyChartView pieChart;
    SharedPreferences sharedPreferences;
    Databasehelper databasehelper;
    String nickname;

    ArrayList<String> array_label;
    String[] labels;
    int[] activity = {40, 10, 20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        databasehelper = new Databasehelper(this);
        array_label = new ArrayList<>();

        pieChart = (AnyChartView)findViewById(R.id.pieChart);
        viewLabelData();

        viewPieChart();
    }


    public void viewLabelData() {
        Cursor cursor ;
        cursor = databasehelper.viewAllData(nickname);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    array_label.add(cursor.getString(1));
                }
            }
            labels = new String[array_label.size()];
            array_label.toArray(labels);
            Toast.makeText(ProfileActivity.this, "array : "+labels[0], Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "array : "+e, Toast.LENGTH_LONG).show();
        }
    }
    private void viewPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i<array_label.size(); i++){
            dataEntries.add(new ValueDataEntry(labels[i], activity[i]));
        }
        pie.data(dataEntries);
        pieChart.setChart(pie);
    }
}
