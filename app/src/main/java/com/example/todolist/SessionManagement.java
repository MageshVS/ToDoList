package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String SHARED_PRE_NAME = "Session";
    private String SHARED_KEY = "Session_user";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(User user){
        String nickname = user.getNickname();
        editor.putString(SHARED_KEY, nickname).commit();

    }
    public String getSession(){
        return sharedPreferences.getString(SHARED_KEY, "a");
    }
    public void removeSession(){
        editor.putInt(SHARED_KEY, -1).commit();
    }
}
