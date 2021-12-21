package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

public class PaymentByCardScreen extends Activity {

    //String url = "https://paypage-uat.ngenius-payments.com/?code=b3453291c4288253";
    String url = "";//https://paypage-uat.ngenius-payments.com/?code=73fa398e68184c41";
    //https://paypage-uat.ngenius-payments.com/?code=650148cbfb594931";
    String poll_url,Price ,delivery_charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_by_card_screen);
        getIntentRecord();
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(url);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        GetPaymentApi();
    }

    public void getIntentRecord(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            poll_url = extras.getString("poll_url");
            Price= extras.getString("Price");
            delivery_charge= extras.getString("delivery_charge");
        }

    }

    private void GetPaymentApi() {
        //library.showLoading();
        APIManager.getInstance().getPayment(poll_url,new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //library.hideLoading();
                if(response.getAsJsonObject().get("status").toString().equals("0")) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GetPaymentApi();
                        }
                    }, 3000);

                }else if(response.getAsJsonObject().get("status").toString().equals("1")) {
                    if(response.getAsJsonObject().get("message").getAsString().equals("CAPTURED")){
                        Intent intent = new Intent(getApplicationContext(), DeliverySuccessScreen.class);
                        intent.putExtra("Price", Price);
                        intent.putExtra("Status", "true");
                        intent.putExtra("delivery_charge", delivery_charge);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), DeliverySuccessScreen.class);
                        intent.putExtra("Price", Price);
                        intent.putExtra("Status", "false");
                        intent.putExtra("delivery_charge", delivery_charge);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //library.hideLoading();
                //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                GetPaymentApi();
            }

        });
    }

}

