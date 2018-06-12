package com.google.firebase.udacity.friendlychat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

class NotificationHelper extends ContextWrapper {
    private Context mContext;
    private NotificationManager mNotificationManager;
    public static final String CHANNEL_ID = "com.google.firebase.udacity.friendlychat.Promotions";
    public static final String CHANNEL_NAME = "Promotions";

    //Create your notification channels
    public NotificationHelper(Context context) {
        super(context);
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);
    }

    private NotificationCompat.Builder getNotification(String title, String message) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.cover);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(getPendingIntent())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(icon)
                .addAction(android.R.drawable.ic_menu_send, "Send", getPendingIntent())
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon))
                .setAutoCancel(true);
    }


    public void notify(int id, String title, String message) {
        getManager().notify(id, getNotification(title, message).build());
    }

    private PendingIntent getPendingIntent() {
        Intent resultIntent = new Intent(mContext, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    //Send your notifications to the NotificationManager system service
    private NotificationManager getManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }
}