package com.example.tuitionapp_surji.notification_pack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.starting.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyService extends FirebaseMessagingService {
    private int notificationID ;
    private FirebaseFirestore databaseFireStore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;

    public MyService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
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

    private void scheduleJob(String title, String body) {
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

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_calendar_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pi) ;

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);

        if(title.equals("New Friend")){
            notificationID = 1 ;
        }
        else if(title.equals("Response Post")){
            notificationID = 2 ;
        }
        else if(title.equals("Group Tutor")){
            notificationID = 3 ;
        }
        else if(title.equals("Message")){
            notificationID = 4 ;
        }

        Map<String,Object> counter = new HashMap<>() ;
        counter.put(user.getUid(), 1) ;

 
        databaseFireStore.collection("System").document("NotificationCounter")
                .set(counter) ;

        notificationManager.notify(notificationID,notificationBuilder.build());
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

        if(title.equals("New Friend")){
            notificationID = 1 ;
        }
        else if(title.equals("Response Post")){
            notificationID = 2 ;
        }
        else if(title.equals("Group Tutor")){
            notificationID = 3 ;
        }
        else if(title.equals("Message")){
            notificationID = 4 ;
        }

        notificationManager.notify(notificationID,notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}