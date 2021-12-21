package com.valucart_project.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.valucart_project.R;
import com.valucart_project.utils.TypefaceUtil;

public class ValuCartApp extends Application {

    public static final String CHANNEL_ID = "ValucartID";
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/avenirbook.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/AvenirLTStd-Roman.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/avenirblack.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        createNotificationChannel();
        sAnalytics = GoogleAnalytics.getInstance(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}

