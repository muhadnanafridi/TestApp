package com.valucart_project.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.application.ValuCartApp;
import com.valucart_project.fragments.DashboardFragment;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Products;
import com.valucart_project.models.WalletTransactions;
import com.valucart_project.rest.ApiClient;
import com.valucart_project.utils.Constants;
import com.valucart_project.webservices.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends Activity {

    String alreadySeenOnBoarding = "alreadySeenOnBoarding";
    String version;
    public static ValuCartApp application;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("ValuCart");
        Constants.analyticEnable = false;

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        application = (ValuCartApp) getApplicationContext();

         //Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);

        //ApiClient.BASE_URL = "https://v2api.valucart.com/";
        //ApiClient.BASE_URL = "http://testing.v2.api.valucart.com/";


        setContentView(R.layout.splash_screen);
        //String token =  ""+FirebaseInstanceId.getInstance().getToken();//com.google.android.gms.tasks.zzu@dde52b4
        prefs = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //version="5.6";

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


        restoredText = prefs.getString("alreadySeenOnBoarding", null);
        Constants.showPromo=true;
        DashboardFragment.featureProductsList = new Products();
        //Toast.makeText(getApplicationContext(),"testing",Toast.LENGTH_LONG).show();

        loadTransactions();
    }

    public void display(){
        if(restoredText != null) {
            try {
                if (version.equals(prefs.getString("versiondata", null).toString())) {
                    PicassoCacheUtil.clearImageDiskCache(getApplicationContext());
                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                    finish();
                } else {
                    Constants.userLogin = false;
                    splashDelay();
                }
            }catch (Exception e){
                Constants.userLogin = false;
                splashDelay();
            }
        }else {
            Constants.userLogin = false;
            splashDelay();
        }
    }

    String restoredText;

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
                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
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

    public static final class PicassoCacheUtil {

        public static boolean clearImageDiskCache(Context context) {
            File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
            if (cache.exists() && cache.isDirectory()) {
                return deleteDir(cache);
            }
            return false;
        }

        private static boolean deleteDir(File dir) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
            // The directory is now empty so delete it
            return dir.delete();
        }
    }

    private void loadTransactions() {

        APIManager.getInstance().getVersion(new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //Toast.makeText(getApplicationContext(),response.getAsJsonObject().getAsJsonObject("data").get("android_version").getAsString(),Toast.LENGTH_LONG).show();
                try {
                    if (Float.parseFloat(response.getAsJsonObject().getAsJsonObject("data").get("android_version").getAsString()) > Float.parseFloat(version)) {
                        showForceUpdateDialog("" + Float.parseFloat(response.getAsJsonObject().getAsJsonObject("data").get("android_version").getAsString()));
                    } else {
                        display();
                    }
                }catch(Exception e){
                    display();
                }

            }

            @Override
            public void onResponseError(JSONObject jObjError, Throwable error) {
                try {
                    display();
                }catch (Exception e){
                    display();
                }
            }

        });

    }

    public void showForceUpdateDialog(String versionApp){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(SplashScreen.this,
                R.style.animationdialog));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogBuilder.setTitle(getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(getString(R.string.youAreNotUpdatedMessage) + " " );
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.valucart_project")));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

}

