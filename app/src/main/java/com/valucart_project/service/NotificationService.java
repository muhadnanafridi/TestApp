package com.valucart_project.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.valucart_project.R;
import com.valucart_project.screen.OrderSummaryDetailScreen;

public class NotificationService extends Service {

    public static final String CHANNEL_ID = "ValucartID";
    public static final String ACTION_1 = "action_1";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("fuck", "it" );
        String input = intent.getStringExtra("inputExtra");
        vibrate();
        Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationsound);
        //r.play();


        Intent notificationIntent = new Intent(this, OrderSummaryDetailScreen.class);
        //Intent notificationIntent = new Intent("your intent_filter_action");
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_NEW_TASK);

        notificationIntent.putExtra("NotificationService","NotificationService");
        notificationIntent.putExtra("type",intent.getExtras().getString("type"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,  new Intent(notificationIntent)
                        .putExtra("NotificationService","NotificationService")
                        .putExtra("type",intent.getExtras().getString("type"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Valucart Update")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(notificationsound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .build();
       // notification.notify();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //try {
         //   startForeground(123, notification);
        //}catch (Exception e){}

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }
    public void vibrate()
    {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

