package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private EditText nicknameView;
    private Button login;
    private String nickname;
    private TextView warningMessage;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nicknameView = (EditText) findViewById(R.id.nickName);
        login = (Button)findViewById(R.id.loginBtn);
        warningMessage = (TextView)findViewById(R.id.warningMessage);

        sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        nickname = sharedPreferences.getString("Session_user","");
        //Toast.makeText(getApplicationContext(),"n is "+nickname, Toast.LENGTH_LONG).show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = nicknameView.getText().toString();

                if(nickname.matches("")){
                    warningMessage.setText("Enter a Valid Name");
                    warningMessage.setVisibility(View.VISIBLE);
                }
                else{
                    //Toast.makeText(getApplicationContext(),nickname,Toast.LENGTH_LONG).show();
                    warningMessage.setVisibility(View.GONE);
                    Databasehelper databasehelper = new Databasehelper(LoginActivity.this);
                    boolean result = databasehelper.insertUser(nickname);
                    if(result == true){
                        //Toast.makeText(getApplicationContext(), "UPDATED", Toast.LENGTH_LONG).show();
                        login(view);
                    }
                    else {
                        //Toast.makeText(getApplicationContext(), "UPDATE FAILED", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }
    public void checkUser(){
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String isLoggedIn = sessionManagement.getSession();
        //Toast.makeText(getApplicationContext(),"isLoggedIn is "+isLoggedIn, Toast.LENGTH_LONG).show();
        if(isLoggedIn.equals("logout")){

        }
        else {
            moveToMainActivity();
        }
    }
    public void login(View view){
        User user = new User(nickname);
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        sessionManagement.saveSession(user);

        Databasehelper databasehelper = new Databasehelper(LoginActivity.this);
        databasehelper.CreateUserTable(nickname);
        moveToMainActivity();
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
