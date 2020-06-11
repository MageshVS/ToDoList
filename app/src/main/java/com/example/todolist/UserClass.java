package com.example.todolist;

public class UserClass {
    private String nickname;

    //getter and setter method
    public UserClass(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
