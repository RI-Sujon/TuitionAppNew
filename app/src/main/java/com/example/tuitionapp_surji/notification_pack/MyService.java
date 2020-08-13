package com.example.tuitionapp_surji.notification_pack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.starting.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyService extends FirebaseMessagingService {
    public MyService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("------------------->>" + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            if (true) {
                scheduleJob(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            } else {
                handleNow(remoteMessage.getData());
            }
        }

        if (remoteMessage.getNotification() != null) {
            System.out.println(remoteMessage.getNotification().getBody());
        }
    }

    private void handleNow(Map<String, String> data) {
        String title = data.get("title").toString();
        String body = data.get("body").toString();

        Intent intent = new Intent(MyService.this, MainActivity.class) ;
        PendingIntent pi = PendingIntent.getActivity(MyService.this, 0, intent, 0) ;

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "example.myapplication.service.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Team Tarang");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        //RemoteViews contentView = new RemoteViews(getPackageName());

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setContentIntent(pi)
                .setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);


    }

    private void scheduleJob(String title, String body) {
        Intent intent = new Intent(MyService.this, MainActivity.class) ;
        //PendingIntent pi = PendingIntent.getActivity(MyService.this, 1, intent, Intent.FLAG_ACTIVITY_NEW_TASK) ;

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "example.myapplication.service.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Team Tarang");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_calendar_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setPriority(Notification.PRIORITY_HIGH);

        //.setContentIntent(pi)

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);

        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}