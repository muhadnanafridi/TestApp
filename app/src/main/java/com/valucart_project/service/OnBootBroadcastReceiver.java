package com.valucart_project.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("", "it" );
        try {
            //Intent i = new Intent("com.valucart_project.service.MyFirebaseMessagingService");
            //i.setClass(context, MyFirebaseMessagingService.class);
            //context.startService(i);
            Intent intents = new Intent(context, MyFirebaseMessagingService.class);
            context.startService(intents);
        }catch (Exception e){}
    }

}

