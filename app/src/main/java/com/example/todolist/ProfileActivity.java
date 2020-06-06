package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    private TextView profileUserName, profileTextView, emailTextView, cityTextView, profileEmptyTextView, changeProfilePicture;
    private EditText profileEditView, emailEditView, cityEditView;
    private Button profileSaveBtn, profileEditBtn, emailSaveBtn, emailEditBtn, cityEditBtn, citySaveBtn;
    private ImageView profileEmptyImageView, profilePicture;
    SharedPreferences sharedPreferences;
    Databasehelper databasehelper;
    String nickname, profileEmail, profileCity;
    String profilePhone;

    ArrayList<String> array_label_profile;
    ArrayList<Integer> array_activity_profile;
    String[] labels;
    Integer[] activity;
    Bitmap bitmap;

    public static final int IMAGE_PICK_CODE = 100;
    public static final int PERMISSION_CODE = 101;

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
        //Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();

        final String raw_nickname =nickname.replaceAll("_"," ");

        databasehelper = new Databasehelper(this);
        array_label_profile = new ArrayList<>();
        array_activity_profile = new ArrayList<>();

        profilePicture = (ImageView)findViewById(R.id.profilePicture);
        changeProfilePicture = (TextView)findViewById(R.id.changeProfilePicture);
        profileUserName = (TextView)findViewById(R.id.profileUserName);
        profileUserName.setText(raw_nickname);
        pieChart = (AnyChartView)findViewById(R.id.pieChart);
        profileEmptyTextView = (TextView)findViewById(R.id.profileEmptytextView);
        profileEmptyImageView = (ImageView) findViewById(R.id.profileEmptyImageView);

        viewProfilePicture();
        viewLabelData();

        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePicture();
            }
        });
        viewPieChart();

        profileTextView = (TextView)findViewById(R.id.phoneTextView);
        profileEditView = (EditText)findViewById(R.id.phoneEditView);
        profileSaveBtn = (Button)findViewById(R.id.phoneSaveBtn);
        profileEditBtn = (Button)findViewById(R.id.phoneEditBtn);
        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileTextView.setVisibility(View.GONE);
                profileEditView.setVisibility(View.VISIBLE);
                profileEditView.requestFocus();
                profileEditView.setFocusableInTouchMode(true);
                profileSaveBtn.setVisibility(View.VISIBLE);
                profileEditBtn.setVisibility(View.GONE);
            }
        });
        profileEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    profilePhoneFunction();
                    return true;
                }
                return false;
            }
        });
        profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePhoneFunction();
            }
        });

        emailTextView = (TextView)findViewById(R.id.emailTextView);
        emailEditView = (EditText)findViewById(R.id.emailEditView);
        emailEditBtn = (Button)findViewById(R.id.emailEditBtn);
        emailSaveBtn = (Button)findViewById(R.id.emailSaveBtn);
        emailEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailTextView.setVisibility(View.GONE);
                emailEditBtn.setVisibility(View.GONE);
                emailEditView.setVisibility(View.VISIBLE);
                emailEditView.requestFocus();
                emailEditView.setFocusableInTouchMode(true);
                emailSaveBtn.setVisibility(View.VISIBLE);
            }
        });

        emailEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    profileEmailFunction();
                    return true;
                }
                return false;
            }
        });
        emailSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileEmailFunction();
            }
        });
        cityTextView = (TextView)findViewById(R.id.cityTextView);
        cityEditView = (EditText)findViewById(R.id.cityEditView);
        cityEditBtn = (Button)findViewById(R.id.cityEditBtn);
        citySaveBtn = (Button)findViewById(R.id.citySaveBtn);
        cityEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityTextView.setVisibility(View.GONE);
                cityEditBtn.setVisibility(View.GONE);
                cityEditView.setVisibility(View.VISIBLE);
                profileEditView.setFocusableInTouchMode(false);
                cityEditView.requestFocus();
                cityEditView.setFocusableInTouchMode(true);
                citySaveBtn.setVisibility(View.VISIBLE);
            }
        });
        cityEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    profileCityFunction();
                    return true;
                }
                return false;
            }
        });
        citySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileCityFunction();
            }
        });

        profileData();
    }

    private void profilePhoneFunction(){
        profilePhone =  profileEditView.getText().toString();
        String profileEmailText = emailTextView.getText().toString();
        String profileCityText = cityTextView.getText().toString();
        profileEditView.setVisibility(View.GONE);
        profileTextView.setText(profilePhone);
        profileSaveBtn.setVisibility(View.GONE);
        profileTextView.setVisibility(View.VISIBLE);
        profileEditBtn.setVisibility(View.VISIBLE);
        boolean insertedphone = databasehelper.insertPhoneNumber(profilePhone,profileEmailText,profileCityText);
        if(insertedphone){
            //Toast.makeText(getApplicationContext(), "Email Inserted", Toast.LENGTH_LONG).show();
            profileData();
        }
        else{
            Toast.makeText(getApplicationContext(), "Email Insertion Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void profileEmailFunction(){
        String profilePhoneText =  profileTextView.getText().toString();
        profileEmail = emailEditView.getText().toString();
        String profileCityText = cityTextView.getText().toString();
        emailTextView.setText(profileEmail);
        emailTextView.setVisibility(View.VISIBLE);
        emailEditView.requestFocus();
        emailEditView.setFocusableInTouchMode(true);
        emailEditView.setVisibility(View.GONE);
        emailSaveBtn.setVisibility(View.GONE);
        emailEditBtn.setVisibility(View.VISIBLE);
        boolean insertedEmail = databasehelper.insertEmailName(profilePhoneText,profileEmail,profileCityText);
        if(insertedEmail){
            Toast.makeText(getApplicationContext(), "Email Inserted", Toast.LENGTH_LONG).show();
            profileData();
        }
        else{
            Toast.makeText(getApplicationContext(), "Email Insertion Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void profileCityFunction(){
        String profilePhoneText =  profileTextView.getText().toString();
        String profileEmailText = emailTextView.getText().toString();
        profileCity = cityEditView.getText().toString();
        cityTextView.setText(profileCity);
        cityTextView.setVisibility(View.VISIBLE);
        cityEditView.setVisibility(View.GONE);
        citySaveBtn.setVisibility(View.GONE);
        cityEditBtn.setVisibility(View.VISIBLE);
        boolean insertedCity = databasehelper.insertCityName(profilePhoneText,profileEmailText,profileCity);
        if(insertedCity){
            Toast.makeText(getApplicationContext(), "City Name Inserted", Toast.LENGTH_LONG).show();
            profileData();
        }
        else{
            Toast.makeText(getApplicationContext(), "City Name Insertion Failed", Toast.LENGTH_LONG).show();

        }
    }
    private void viewProfilePicture() {
        Cursor cursor;
        byte[] imageData = {};
        cursor = databasehelper.retriveProfileImages(nickname);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                imageData = cursor.getBlob(0);
            }
            if (imageData != null && imageData.length!=0) {
                Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                profilePicture.setImageBitmap(image);
            }else{
                profileEmptyImageView.setImageResource(R.drawable.user_icon);
            }
        }

    }


    private void changeProfilePicture() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                pickImagefromGallery();
            }
        }else{
            pickImagefromGallery();
        }
    }

    private void pickImagefromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/+");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImagefromGallery();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Permission Denied..", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profilePicture.setImageURI(data.getData());
            profilePicture.invalidate();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) profilePicture.getDrawable();
            bitmap = bitmapDrawable.getBitmap();
            saveProfilePicture();
            viewProfilePicture();
        }
    }

    private void saveProfilePicture() {
        databasehelper.saveProfileImage(nickname, bitmap);
    }

    public void profileData() {
        Cursor cursor;
        String phone="";
        String email="";
        String city="";
        cursor = databasehelper.viewProfileData();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                phone = cursor.getString(2);
                email = cursor.getString(3);
                city = cursor.getString(4);
            }
            if(phone == null || phone.equals("") || phone.length()<=0){
                profileTextView.setText("phone");
            }
            else{
                profileTextView.setText(phone);
            }
            if(email == null || email.equals("") || email.length()<=0){
                emailTextView.setText("Email");
            }
            else {
                emailTextView.setText(email);
            }
            if(city == null || city.equals("") || city.length()<=0) {
                cityTextView.setText("City");
            }
            else {
                cityTextView.setText(city);
            }
        }
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
            profileEmptyImageView.setVisibility(View.VISIBLE);
            profileEmptyTextView.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
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
