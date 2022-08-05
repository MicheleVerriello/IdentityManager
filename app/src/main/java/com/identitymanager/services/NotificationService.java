package com.identitymanager.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.identitymanager.R;
import com.identitymanager.activities.MainActivity;

public class NotificationService extends Service {

    public static boolean isServiceRunning;
    private String CHANNEL_ID = "NOTIFICATION_CHANNEL";

    public NotificationService() {
        isServiceRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service is Running")
                .setContentText("Listening for Screen Off/On events")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.purple_500, getTheme()))
                .build();

        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        stopForeground(true);

        // call MyReceiver which will restart this service via a worker
        Intent broadcastIntent = new Intent(this, NotificationService.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }
}