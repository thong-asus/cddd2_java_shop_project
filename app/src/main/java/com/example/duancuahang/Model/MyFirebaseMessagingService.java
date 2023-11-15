package com.example.duancuahang.Model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.duancuahang.HomeShop;
import com.example.duancuahang.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notification = message.getNotification();
        if (notification == null){
            return;
        }
        String title = notification.getTitle();
        String strMessage = notification.getBody();
        sendNotification(title,strMessage);

    }

    private void sendNotification(String title, String strMessage) {
        Intent intent = new Intent(this, HomeShop.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, "push_notification")
                .setContentTitle(title)
                .setContentText(strMessage)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);
        Notification notification = notificationCompat.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(1,notification);
        }

    }
}
