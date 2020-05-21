package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText nicknameView;
    private Button login;
    private String nickname, raw_nicname;
    private TextView warningMessage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nicknameView = (EditText) findViewById(R.id.nickName);
        login = (Button)findViewById(R.id.loginBtn);
        warningMessage = (TextView)findViewById(R.id.warningMessage);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nickname = nicknameView.getText().toString().replaceAll("\\s+","_");

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
        UserClass user = new UserClass(nickname);
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
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        finish();
    }


}
