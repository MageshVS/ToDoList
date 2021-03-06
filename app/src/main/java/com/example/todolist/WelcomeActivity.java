package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView logotext, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for enabling full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView = (ImageView)findViewById(R.id.logo);
        logotext = (TextView)findViewById(R.id.logotext);
        slogan = (TextView)findViewById(R.id.slogan);

        //setting animations
        imageView.setAnimation(topAnim);
        logotext.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        //Splash Screen for duration 1.5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
