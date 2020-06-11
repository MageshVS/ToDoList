package com.example.todolist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationChannelClass extends Application {
    public static final String CHANNEL_1_ID = "Channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    public void createNotificationChannels(){
        //checking the Android OS version of user
        //if the user's version is greater than or equal to OREO
        //then a separate Notification channel should be created
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Alarm Message");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

    }
}
