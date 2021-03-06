package com.example.todolist;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);
        /*Toast.makeText(context, " Alarm Message", Toast.LENGTH_LONG).show();*/
        Intent ActivityIntent = new Intent(context, MainActivity.class);
        Integer request_code = intent.getIntExtra("Code",0);
        String title = intent.getStringExtra("Title");
        String notes = intent.getStringExtra("Notes");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, request_code, ActivityIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, NotificationChannelClass.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.poll)
                .setContentTitle("Remainder For "+title)
                .setContentText(notes)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
               // .setColor(getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER).build();

        notificationManager.notify(1, notification);
    }

}
