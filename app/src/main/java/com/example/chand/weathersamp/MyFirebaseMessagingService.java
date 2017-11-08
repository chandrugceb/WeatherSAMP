package com.example.chand.weathersamp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by chand on 05-11-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Integer notification_id = 1;
        notification_id = (int)System.currentTimeMillis();
        Intent notifyIntent = null;
        Log.v("Victor", "From : " + remoteMessage.getFrom().toString());
        Log.v("Victor","Notification Message Body : "+ remoteMessage.getNotification().getBody().toString());
        Log.v("Victor", "Notification Message Label : "+ remoteMessage.getNotification().getTitle().toString());
        switch (remoteMessage.getData().get("NotificationType"))
        {
            case "install":
                Log.v("Victor", "Notification Type : " + remoteMessage.getData().get("NotificationType"));
                Log.v("Victor","Install Location Name : " + remoteMessage.getData().get("LocationName"));
                // Creates an Intent for the Activity
                notifyIntent = new Intent(getApplicationContext(), InstallActivity.class);
                notifyIntent.putExtra("IconId",R.drawable.install_icon );
                notifyIntent.putExtra("IconText","Install");
                notifyIntent.putExtra("InstallLocationName",remoteMessage.getData().get("LocationName"));
                break;
        }

        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,notification_id,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Instantiate a Builder object.
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            mBuilder = new NotificationCompat.Builder(this,"CM_Channel_ID_01");
        }
        else
        {
            mBuilder = new NotificationCompat.Builder(this);
        }
        mBuilder.setSmallIcon(R.drawable.install_icon);
        mBuilder.setContentTitle(remoteMessage.getNotification().getTitle().toString());
        mBuilder.setContentText(remoteMessage.getNotification().getBody().toString());
        mBuilder.setAutoCancel(true);


        // Puts the PendingIntent into the notification builder
        mBuilder.setContentIntent(pendingIntent);
        // Notifications are issued by sending them to the
        // NotificationManager system service.
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel("CM_Channel_ID_01", "CM_Channel_Name_01", NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId("CM_Channel_ID_01");
            mNotificationManager.createNotificationChannel(channel);
        }
        // Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationManager
        mNotificationManager.notify(notification_id, mBuilder.build());
   }
}
