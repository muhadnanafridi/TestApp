package com.valucart_project.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.valucart_project.R;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.utils.Constants;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    //FirebaseInstanceId.getInstance().getToken()
    public static final int NOTIFICATION_ID = 1;
    // response={"data":{"order_status":0,"order_id":"64"},"status":"success"}, gcm.notification.body=Hello doan! your order of AED118 was successfuly Placed}]
    public static final String ACTION_1 = "action_1";
    public static final String CHANNEL_ID = "ValucartID";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        try {

            // Logic to turn on the screen
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                if (!powerManager.isInteractive()) { // if screen is not already on, turn it on (get wake_lock for 10 seconds)
                    PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
                    wl.acquire(10000);
                    PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
                    wl_cpu.acquire(10000);
                }
            }

            //Log.i(TAG, "MyClass.getView() — get item number " + remoteMessage.getData().toString());
            Map<String, String> mapNotification = new HashMap<String, String>();
            //Log.i("fuck", "it" );
            //getId = new JSONObject(remoteMessage.getData().toString());
            mapNotification = remoteMessage.getData();
            vibrate();

            //r.play();

            //addNotification(getId.getJSONObject("response").getJSONObject("data").get("type").toString(),getId.getJSONObject("response").getJSONObject("data").get("order_id").toString());
            //addNotification(mapNotification.get("type"),mapNotification.get("order_id"));

            Intent action1Intent = new Intent(this, DashboardActivity.class);//.setAction(ACTION_1);
            //Intent action1Intent = new Intent("intent_filter_action");
            //action1Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
            action1Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            action1Intent.putExtra("NotificationService", "NotificationService");
            action1Intent.putExtra("type", mapNotification.get("type"));

            //PendingIntent action1PendingIntent = PendingIntent.getService(this, 0, action1Intent,PendingIntent.FLAG_UPDATE_CURRENT);
            //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            //stackBuilder.addNextIntentWithParentStack(action1Intent);

            PendingIntent action1PendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    new Intent(action1Intent)
                            .putExtra("NotificationService", "NotificationService")
                            .putExtra("type", mapNotification.get("type"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_ONE_SHOT);
            Constants.orderReference = mapNotification.get("order_id");
/*
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"ValucartID")
                // optional, this is to make beautiful icon
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getBody())
                .setSubText("Update")
                .setAutoCancel(true)
                .setContentIntent(action1PendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setSound(notificationurl);
                //.setContentText();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, notificationBuilder.build());

        //NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
        //notificationManager1.notify(2,  notificationBuilder.build());

*/
/*            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentTitle("Valucart Update")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(action1PendingIntent)
                    .build();

        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
        notificationManager1.notify(2, notification);*/
/*

        //{"response":{"data":{"order_status":0,"type":"orderstatus","order_id":"69"},"status":"success"}}


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                Intent serviceIntent = new Intent(this, NotificationService.class);
                serviceIntent.putExtra("inputExtra", remoteMessage.getNotification().getBody());
                serviceIntent.putExtra("type", getId.getJSONObject("response").getJSONObject("data").get("type").toString());

            }
            //ContextCompat.startForegroundService(this, serviceIntent);

*/
/*
            Intent likeIntent = new Intent(this, LikeService.class);
            likeIntent.putExtra("notificationId","working"+notificationId);
            likeIntent.putExtra("IMAGE_URL_EXTRA","working");
            PendingIntent likePendingIntent = PendingIntent.getService(this,notificationId+1,likeIntent,PendingIntent.FLAG_ONE_SHOT);
*/


            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //Setting up Notification channels for android O and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(mapNotification.get("title"), mapNotification.get("message"));
            }
            final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
            int notificationId = new Random().nextInt(60000);
            NotificationCompat.Builder notificationBuilder;

            if (mapNotification.get("image") == null) {

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mapNotification.get("title"))
                        //.setSubText(mapNotification.get("message"))
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(mapNotification.get("title")).bigText(remoteMessage.getData().get("message")))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(action1PendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setLights(Color.WHITE, 2000, 3000)
                        .setVibrate(DEFAULT_VIBRATE_PATTERN)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                ;

            } else {
                Bitmap bitmapNotification = getBitmapfromUrl(mapNotification.get("image") + "?w=200"); //obtain the image

                //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        //.setLargeIcon(bitmapNotification)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mapNotification.get("title"))
                        //.setSubText(mapNotification.get("message"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(action1PendingIntent)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .setSummaryText(remoteMessage.getData().get("message"))
                                .bigPicture(bitmapNotification))
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setLights(Color.WHITE, 2000, 3000)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            }
            //.setPriority(Notification.PRIORITY_HIGH)
            // .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
            //.addAction(R.mipmap.ic_launcher, getString(R.string.default_notification_channel_id),likePendingIntent) //Setting the action

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    int notificationId = 0;
    NotificationManager notificationManager;

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    public static class NotificationActionService extends IntentService {

        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            //DebugUtils.log("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
                Toast.makeText(this, "workingds asdasdasda dadsasdasda sdsaddsa ", Toast.LENGTH_LONG).show();
            }
        }
    }

    Uri notificationurl;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(String Title, String Description) {
        //addNotification(Title,Description);
        notificationurl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationurl);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        CharSequence adminChannelName = Title;
        String adminChannelDescription = Description;

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setName(Title);
        adminChannel.enableLights(true);
        adminChannel.setDescription(Description);
        adminChannel.setSound(notificationurl, attributes);
        adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    public void addNotification(String type, String orderReference) {

        SharedPreferences.Editor editor = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("type", type);
        editor.putString("orderReference", orderReference);
        editor.apply();

    }

    //Simple method for image downloading
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

//958674
