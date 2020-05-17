package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String SHARED_PRE_NAME = "Session";
    private String SHARED_KEY = "Session_user";
    private  String nickname;

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(User user){
        nickname = user.getNickname();
        editor.putString(SHARED_KEY, nickname).commit();

    }
    public String getSession(){
        return sharedPreferences.getString(SHARED_KEY, "logout");
    }
    public void removeSession(){
        editor.putString(SHARED_KEY, "logout").commit();
    }
}
