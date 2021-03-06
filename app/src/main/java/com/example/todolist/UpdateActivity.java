package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    private int code;
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

        //setting the toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary, null));
        toolbar.setTitleTextAppearance(this, R.style.righteous_regular);
        setSupportActionBar(toolbar);

        //getting the sharedPreference key-value(session variable)
        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        //Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        notificationManager = NotificationManagerCompat.from(this);

        //Instance of databaseHelper class
        databasehelper = new Databasehelper(UpdateActivity.this);

        labelUpdateView = (TextView)findViewById(R.id.update_label);
        spinner = (Spinner) findViewById(R.id.update_labelSpinner);
        dateUpdateView = (TextView) findViewById(R.id.update_dateView);
        timeUpdateView = (TextView) findViewById(R.id.update_TimeView);
        notesUpdateView = (EditText) findViewById(R.id.update_notesConatiner);
        update = (Button) findViewById(R.id.update_buttoncls);

        //Adding values for label(dropdown list box)
        labelArray = new ArrayList<String>();
        labelArray.add("Select a Label");
        labelArray.add("Work");
        labelArray.add("Event");
        labelArray.add("Shopping");
        labelArray.add("Custom");
        getAndSetIntentData();

        //setting the array in a adapter
        final ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(UpdateActivity.this,
                android.R.layout.simple_list_item_1,labelArray);
        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //getting the existing label value
        final int a = labelAdapter.getPosition(a_update_label);
        update_label = (String) spinner.getItemAtPosition(a);
        //setting that value into the label dropdown lost box
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(a);
            }
        });
        spinner.setAdapter(labelAdapter);

        //finding the value which the user clicked in dropdown box
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0 && i<4) {
                    //if the user clicks the custom label this will open a dialog box
                   // Toast.makeText(getApplicationContext(), (String)adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                    update_label = (String) adapterView.getItemAtPosition(i);
                }
                else if(i == 4){
                    //if the user clicks the custom label this will open a dialog box
                    openDialog();
                }
                else{
                    //if user either selects a default value or doesn't select any value
                    update_label = "No Label";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                update_label = "Work";
            }
        });

        //getting the current system date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);

        year_for_alarm = year;
        month_for_alarm = month;
        day_for_alarm = date;

        //Creating a datePickerDialog to allow the user select the time they wanted
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

        //changing the months from number format to text format for better user experience
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

        //Creating a timePickerDialog to allow the user select the time they wanted
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
        //Finding whether the time entered is A.M or P.M
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
                setAlarm(view);
                Intent updateIntent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(updateIntent);
            }
        });
        //if the user clicks delete button
        //this will delete the current note data
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

    //when the user clicks the save button this method will notify with a message "Updated Successfully"
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
    public void setAlarm(View v){
        Cursor cursor = databasehelper.viewAllData(nickname);
        if(cursor.getCount() != 0){
            //adding the alarm for the last created note.
            cursor.moveToLast();
            code = cursor.getInt(0);
        }
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);
        //to achieve multiple alarm each pending intent should have unique number
        //here "code" is used unique number
        intent.putExtra("Code", code);
        intent.putExtra("Title", update_label);
        intent.putExtra("Notes", notesUpdateView.getText().toString());
        //startService(intent);
        PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(getApplicationContext(),code, intent, 0);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.clear();
        //passing te user entered date and time
        c.set(Calendar.DAY_OF_MONTH, day_for_alarm);
        c.set(Calendar.MONTH, month_for_alarm);
        c.set(Calendar.YEAR, year_for_alarm);
        c.set(Calendar.HOUR_OF_DAY, hour_for_alarm);
        c.set(Calendar.MINUTE, minute_for_alarm);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntentAlarm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //when a user clicks any recycler view in MainActivity
    //the entire data of that particular view is send and received here
    public void getAndSetIntentData() {
        if (getIntent().hasExtra("ID") && getIntent().hasExtra("LABEL")
                && getIntent().hasExtra("DATE") && getIntent().hasExtra("TIME") && getIntent().hasExtra("NOTES")) {

            update_id = getIntent().getStringExtra("ID");
            a_update_label = getIntent().getStringExtra("LABEL");
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
