package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity implements AlertDialogActivity.AlertDialogInterface{

    Spinner spinner;
    TextView dateUpdateView, timeUpdateView, labelUpdateView;
    EditText notesUpdateView;
    Button update, deleteOneRow;
    String label, format, nickname;
    ArrayList<String> labelArray;
    private  int hour_for_alarm ,minute_for_alarm, day_for_alarm, month_for_alarm, year_for_alarm;
    String update_id, update_label, update_date, update_time, update_notes;
    String a_update_label;
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    private Databasehelper databasehelper;
    SharedPreferences sharedPreferences;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary, null));
        toolbar.setTitleTextAppearance(this, R.style.righteous_regular);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        //Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        notificationManager = NotificationManagerCompat.from(this);

        labelUpdateView = (TextView)findViewById(R.id.update_label);
        spinner = (Spinner) findViewById(R.id.update_labelSpinner);
        dateUpdateView = (TextView) findViewById(R.id.update_dateView);
        timeUpdateView = (TextView) findViewById(R.id.update_TimeView);
        notesUpdateView = (EditText) findViewById(R.id.update_notesConatiner);
        update = (Button) findViewById(R.id.update_buttoncls);

        labelArray = new ArrayList<String>();
        labelArray.add("Select a Label");
        labelArray.add("Work");
        labelArray.add("Event");
        labelArray.add("Shopping");
        labelArray.add("Custom");
        getAndSetIntentData();

        final ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(UpdateActivity.this,
                android.R.layout.simple_list_item_1,labelArray);
        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final int a = labelAdapter.getPosition(a_update_label);
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(a);
            }
        });
        spinner.setAdapter(labelAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0 && i<4) {
                   // Toast.makeText(getApplicationContext(), (String)adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                    update_label = (String) adapterView.getItemAtPosition(i);
                }
                else if(i == 4){
                    openDialog();
                }
                else{
                    update_label = "No Label";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                update_label = "Work";
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);

        year_for_alarm = year;
        month_for_alarm = month;
        day_for_alarm = date;

        dateUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                        R.style.DatePickerDialogTheme,
                        setListener, year, month, date);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month = month + 1;
                year_for_alarm = year;
                month_for_alarm = month;
                day_for_alarm = date;
                String mon;
                mon = "Jan";
                switch (month){
                    case 1:
                        mon = "Jan";
                        break;
                    case 2:
                        mon = "Feb";
                        break;
                    case 3:
                        mon = "Mar";
                        break;
                    case 4:
                        mon = "Apr";
                        break;
                    case 5:
                        mon = "May";
                        break;
                    case 6:
                        mon = "June";
                        break;
                    case 7:
                        mon = "July";
                        break;
                    case 8:
                        mon = "Aug";
                        break;
                    case 9:
                        mon = "Sep";
                        break;
                    case 10:
                        mon = "Oct";
                        break;
                    case 11:
                        mon = "Nov";
                        break;
                    case 12:
                        mon = "Dec";
                        break;
                }
                String day = mon+" "+date+" "+year;
                dateUpdateView.setText(day);

            }
        };

        timeUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                final int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(UpdateActivity.this,
                        R.style.DatePickerDialogTheme, timeSetListener, hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();

            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                hour_for_alarm = hour;
                minute_for_alarm = minute;
                if (hour == 0) {

                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                if(minute<10){
                    String time = hour + " : " + "0"+minute + " " + format;
                    timeUpdateView.setText(time);
                }
                else{
                    String time = hour + " : " + minute + " " + format;
                    timeUpdateView.setText(time);
                }

            }
        };


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Databasehelper databasehelper = new Databasehelper(UpdateActivity.this);
                update_date = dateUpdateView.getText().toString();
                update_time = timeUpdateView.getText().toString();
                update_notes = notesUpdateView.getText().toString();
                databasehelper.updateData(nickname,update_id,update_label,update_date,update_time,update_notes);
                sendOnChannel1();
                Intent updateIntent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(updateIntent);
            }
        });
        deleteOneRow = (Button)findViewById(R.id.recyclerDeletebtn);
        deleteOneRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Databasehelper databasehelper = new Databasehelper(UpdateActivity.this);
                databasehelper.deleteOneRow(nickname,update_id);
                finish();
            }
        });
    }

    public void sendOnChannel1(){
        Intent ActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ActivityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, NotificationChannelClass.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.poll)
                .setContentTitle("Remainder")
                .setContentText("Updates Successfully")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER).build();

        notificationManager.notify(1, notification);

    }

    public void getAndSetIntentData() {
        if (getIntent().hasExtra("ID") && getIntent().hasExtra("LABEL")
                && getIntent().hasExtra("DATE") && getIntent().hasExtra("TIME") && getIntent().hasExtra("NOTES")) {

            update_id = getIntent().getStringExtra("ID");
            a_update_label = getIntent().getStringExtra("LABEL");
            //Toast.makeText(getApplicationContext(), "a_update"+a_update_label,Toast.LENGTH_LONG).show();
            update_date = getIntent().getStringExtra("DATE");
            update_time = getIntent().getStringExtra("TIME");
            update_notes = getIntent().getStringExtra("NOTES");

            dateUpdateView.setText(update_date);
            timeUpdateView.setText(update_time);
            notesUpdateView.setText(update_notes);
        } else {
           // Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
        }
    }
    public void openDialog(){
        AlertDialogActivity alertDialog_helper = new AlertDialogActivity();
        alertDialog_helper.show(getSupportFragmentManager(), "Dialog");
    }
    @Override
    public void applyTexts(String alert_label) {
        update_label = alert_label;
        //Toast.makeText(getApplicationContext(),update_label, Toast.LENGTH_SHORT).show();
    }

    public void logout(){
        SessionManagement sessionManagement = new SessionManagement(UpdateActivity.this);
        sessionManagement.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(UpdateActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void moveToHome(){
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void moveToProfile(){
        Intent intent = new Intent(UpdateActivity.this, ProfileActivity.class);
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
        else if(item.getItemId() == R.id.home){
            moveToHome();
            return true;
        }
        else if(item.getItemId() == R.id.profile){
            moveToProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
