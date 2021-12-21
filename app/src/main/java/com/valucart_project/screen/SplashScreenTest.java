package com.valucart_project.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.valucart_project.R;
import com.valucart_project.fragments.DashboardFragment;
import com.valucart_project.models.Products;
import com.valucart_project.rest.ApiClient;
import com.valucart_project.utils.Constants;

import io.fabric.sdk.android.Fabric;

public class SplashScreenTest extends Activity {

    String alreadySeenOnBoarding = "alreadySeenOnBoarding";
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("ValuCart Test");
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        super.onCreate(savedInstanceState);

        //ApiClient.BASE_URL = "http://testing.v2.api.valucart.com/";
        //ApiClient.BASE_URL = "https://v2api.valucart.com/";

        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);
        //String token =  ""+FirebaseInstanceId.getInstance().getToken();//com.google.android.gms.tasks.zzu@dde52b4
        SharedPreferences prefs = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.i("mobile token", token);
                        Constants.fireBaseToken = token ;

                    }
                });


        String restoredText = prefs.getString("alreadySeenOnBoarding", null);
        Constants.showPromo=true;
        DashboardFragment.featureProductsList = new Products();
        //Toast.makeText(getApplicationContext(),"testing",Toast.LENGTH_LONG).show();
        if(restoredText != null) {
           try {
               if (version.equals(prefs.getString("versiondata", null).toString())) {
                   startActivity(new Intent(SplashScreenTest.this, DashboardActivity.class));
                   finish();
               } else {
                   Constants.userLogin = false;
                   splashDelay();
               }
           }catch (Exception e){
               Constants.userLogin = false;
               DashboardFragment.featureProductsList = new Products();
               splashDelay();
           }
        }else {
           Constants.userLogin = false;
           DashboardFragment.featureProductsList = new Products();
           splashDelay();
       }
    }

    public void splashDelay() {
        emptyTotalCart(getApplicationContext());
        SharedPreferences.Editor editor = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString(alreadySeenOnBoarding, "yes");
        editor.putString("versiondata", version);
        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    startActivity(new Intent(SplashScreenTest.this, OnBoardingFirstScreen.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

    public static void emptyTotalCart(Context context ){

        Constants.cart_id ="";
        Constants.totalCart = 0;

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "");
        editor.putString("cart_id", "");
        editor.putString("token", "");
        editor.putString("bundle_id", "");
        editor.apply();

    }

}

