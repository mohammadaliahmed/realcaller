package com.appsinventiv.realcaller.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.appsinventiv.realcaller.R;

public class BroadcastService extends Service {
    Receiver callReceiver = new Receiver();

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        sendBroadcast(new Intent("YouWillNeverKillMe"));
        Intent resultIntent = null;
        resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Waiting for calls..")
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_NONE;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Real Caller", importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setSound(null, null);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
//        mNotificationManager.notify(num /* Request Code */, mBuilder.build());
        Notification notification = mBuilder.build();
        startForeground(101, notification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.addAction("android.intent.action.SEND_SMS");

        intentFilter.setPriority(100);
        Log.d("service is working", "onCreate: ");

        registerReceiver(callReceiver, intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callReceiver);
        sendBroadcast(new Intent("YouWillNeverKillMe"));

    }
}
