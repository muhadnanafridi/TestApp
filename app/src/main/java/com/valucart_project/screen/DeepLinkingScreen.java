package com.valucart_project.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.valucart_project.fragments.DashboardFragment;
import com.valucart_project.models.Products;
import com.valucart_project.rest.ApiClient;
import com.valucart_project.utils.Constants;

import java.net.URL;

import bolts.AppLinks;
import io.fabric.sdk.android.Fabric;

public class DeepLinkingScreen extends Activity {

    String alreadySeenOnBoarding = "alreadySeenOnBoarding";
    URL url;
    //valucart://type=offer
    //valucart://type=product?id=8Mr6ovzWnlR5
    //valucart://type=product_department?id=Gbg56GmrJmlR&name=Fruits and Vegetables
    //valucart://type=byob
    //valucart://type=bundleDetail?id=xqVz61E6yW7K
    //valucart://type=bundleCategory?id=RX1PnydJ5oVD
    //valucart://type=bulk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("ValuCart");
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();

        super.onCreate(savedInstanceState);

        //ApiClient.BASE_URL = "https://v2api.valucart.com/";

        //Fabric.with(this, new Crashlytics());
        //setContentView(R.layout.splash_screen);

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
                        Constants.fireBaseToken = token;

                    }
                });

        Constants.showPromo = true;

        try {
            Constants.userLogin = false;
            DashboardFragment.featureProductsList = new Products();
            splashDelay(url.toString());
        } catch (Exception e) {}

        try {
            FacebookSdk.sdkInitialize(this);

            Uri targetUrl =
                    AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
            if (targetUrl != null) {
                deepLinkHandler(targetUrl.toString());
            }
        }catch (Exception e){}
    }

    public void deepLinkHandler(String url){

        Constants.facebookAds = true;
        if(url.contains("offer")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            Constants.linkingType = url.split("=")[1];
            startActivity(intent);
            finish();
            return;
        }else  if(url.contains("product_department")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            Constants.linkingType = url.split("=")[1].split("\\?")[0];
            Constants.linkingId = url.split("id=")[1].split("\\&")[0];
            Constants.linkingName = url.split("name=")[1];
            startActivity(intent);
            finish();
            return;
        }else  if(url.contains("product")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            Constants.linkingType = url.split("=")[1].split("\\?")[0];
            Constants.linkingId = url.split("id=")[1];
            startActivity(intent);
            finish();
            return;
        }else  if(url.contains("byob")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            Constants.linkingType = url.split("=")[1];
            startActivity(intent);
            finish();
            return;
        } else if (url.contains("bundleDetail")) {
            Constants.linkingId = url.split("id=")[1];
            Constants.linkingType = url.split("=")[1].split("\\?")[0];
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }else if (url.contains("bundleCategory")) {
            Constants.linkingCategoryId = url.split("id=")[1];
            Constants.linkingType = url.split("=")[1].split("\\?")[0];
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }else if (url.contains("bulk")) {
            Constants.linkingType = url.split("=")[1];
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }

    }

    public void splashDelay(String url) {
        emptyTotalCart(getApplicationContext());
        SharedPreferences.Editor editor = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString(alreadySeenOnBoarding, "yes");
        editor.apply();
        Constants.facebookAds = true;
    }

    public static void emptyTotalCart(Context context) {

        Constants.cart_id = "";
        Constants.totalCart = 0;

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "");
        editor.putString("cart_id", "");
        editor.putString("token", "");
        editor.putString("bundle_id", "");
        editor.apply();

    }

}

