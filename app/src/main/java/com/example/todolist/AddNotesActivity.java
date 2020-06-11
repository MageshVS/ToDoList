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
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNotesActivity extends AppCompatActivity implements AlertDialogActivity.AlertDialogInterface {

    private String format;
    public String label;
    public Button btncls;
    public int alarm_value;

    private  int hour_for_alarm ,minute_for_alarm, day_for_alarm, month_for_alarm, year_for_alarm;

    private Spinner labelSpinner;
    private ToggleButton remainderToggle;
    private EditText notesView;
    public ArrayList<String> labelArray;
    private NotificationManagerCompat notificationManager;

    private TextView dateView, timeView;
    DatePickerDialog.OnDateSetListener setListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    public Databasehelper databasehelper;
    private int code;
    SharedPreferences sharedPreferences;
    public String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        //setting the toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary, null));
        toolbar.setTitleTextAppearance(this, R.style.righteous_regular);
        setSupportActionBar(toolbar);

        //getting the sharedPreference key-value(session variable)
        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        //Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        //Instance of databaseHelper class
        databasehelper = new Databasehelper(this);

        labelSpinner = (Spinner) findViewById(R.id.labelSpinner);
        remainderToggle = (ToggleButton)findViewById(R.id.toggleButton);

        notificationManager = NotificationManagerCompat.from(this);

        //Adding values for label(dropdown list box)
        labelArray = new ArrayList<String>();
        labelArray.add("Select a Label");
        labelArray.add("Work");
        labelArray.add("Event");
        labelArray.add("Shopping");
        labelArray.add("Custom");

        //setting the array in a adapter
        final ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(AddNotesActivity.this,
                android.R.layout.simple_list_item_1, labelArray);
        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelSpinner.setAdapter(labelAdapter);

        //finding the value which the user clicked in dropdown box
        labelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0 && i<4) {
                     //Toast.makeText(getApplicationContext(), (String)adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                     label = (String) adapterView.getItemAtPosition(i);
                }
                else if(i == labelArray.size()-1){
                    //if the user clicks the custom label this will open a dialog box
                    openDialog();
                }
                else{
                    //if user either selects a default value or doesn't select any value
                    label = "No Label";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                label = "Work";
            }
        });

        dateView = (TextView) findViewById(R.id.dateView);
        timeView = (TextView) findViewById(R.id.TimeView);
        notesView = (EditText)findViewById(R.id.notesConatiner);

        //getting the current system date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);

        year_for_alarm = year;
        month_for_alarm = month;
        day_for_alarm = date;

        String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        dateView.setText(currentDate);

        //Creating a datePickerDialog to allow the user select the time they wanted
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNotesActivity.this,
                        R.style.DatePickerDialogTheme,
                        setListener, year, month, date);

                try {
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    datePickerDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                dateView.setText(day);

            }
         };

        //Creating a timePickerDialog to allow the user select the time they wanted
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                final int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AddNotesActivity.this,
                        R.style.DatePickerDialogTheme, timeSetListener, hour, minute, false);
                try {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                    timeView.setText(time);
                }
                else{
                    String time = hour + " : " + minute + " " + format;
                    timeView.setText(time);
                }

            }
        };
       remainderToggle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(remainderToggle.isChecked()){
                   alarm_value = 1;
               }
               else {
                   alarm_value = 0;
               }
           }
       });
       btncls = (Button)findViewById(R.id.buttoncls);
       btncls.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //calling the method which inserts the data into the database
               addDataToDatabase();
               if(alarm_value == 1){
                   setAlarm(view);
               }
               sendOnChannel1();
               Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
           }
       });
    }

    //when the user clicks the save button this method will notify with a message "Saved Successfully"
    public void sendOnChannel1(){
        Intent ActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ActivityIntent, 0);
        //for better readability the NotificationChannelClass is saved in a separate file
        Notification notification = new NotificationCompat.Builder(this, NotificationChannelClass.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.poll)
                .setContentTitle("Remainder")
                .setContentText("Saved Successfully")
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
        intent.putExtra("Code", code);
        intent.putExtra("Title", label);
        intent.putExtra("Notes", notesView.getText().toString());
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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntentAlarm);

    }
    //Adding the values into the database
    public void addDataToDatabase(){
        boolean isInserted = databasehelper.insertData(nickname,label, dateView.getText().toString(),
                timeView.getText().toString(), notesView.getText().toString());
        if(isInserted){
            //Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
        }
        else{
            //Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_LONG).show();
        }
    }

    //dialog box will open when user selects "Custom" field in label dropdown list box
    public void openDialog(){
        AlertDialogActivity alertDialog_helper = new AlertDialogActivity();
        alertDialog_helper.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void applyTexts(String alert_label) {
            label = alert_label;
            //adding the received label value into the array
            labelArray.add(4, label);
            Toast.makeText(getApplicationContext(),label, Toast.LENGTH_SHORT).show();
    }

    public void logout(){
        SessionManagement sessionManagement = new SessionManagement(AddNotesActivity.this);
        sessionManagement.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(AddNotesActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void moveToHome(){
        Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void moveToProfile(){
        Intent intent = new Intent(AddNotesActivity.this, ProfileActivity.class);
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
            moveToProfile();
            return true;
        }
        else if(item.getItemId() == R.id.home){
            moveToHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
