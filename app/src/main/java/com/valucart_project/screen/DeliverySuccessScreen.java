package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvInterval;
import com.valucart_project.application.ValuCartApp;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.DateTimeChangeListener;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Intervals;
import com.valucart_project.popups.DateDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class DeliverySuccessScreen extends FragmentActivity implements View.OnClickListener , OnItemSelection {

    Library library;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout llDate;
    TextView tvDate;
    SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DateFormat);
    RelativeLayout rlScheduleIt,rlHome;
    String Price ,delivery_charge;
    TextView tvTotalPrice,tvTotalAmount,tvOrderId,tvMessage,tvDeliveryCharge;

    private AdapterRvInterval intervalAdapter;
    Intervals intervalsList;
    String intervalId="",intervalDate="";
    private RecyclerView rvItemsCategories;
    ImageView ivOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_success_screen);
        library = new Library(this);

        ivOrder = findViewById(R.id.ivOrder);
        tvMessage = findViewById(R.id.tvMessage);

        rlScheduleIt = findViewById(R.id.rlScheduleIt);
        rlScheduleIt.setOnClickListener(this);

        rlHome = findViewById(R.id.rlHome);
        rlHome.setOnClickListener(this);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);

        getIntentRecord();
        Constants.totalCart = 0;
        Constants.cart_id = "";
        Constants.bundleId = "";
        displayTotalCart(getApplicationContext(),"");
        getInterval();
    }

    public static void displayTotalCart(Context context , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "");
        editor.putString("cart_id", "");
        editor.putString("bundle_id", "");
        editor.apply();
    }

    public void getIntentRecord(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Price = extras.getString("Price");
            delivery_charge = extras.getString("delivery_charge");
            tvOrderId.setText("Order ID : "+Constants.orderReference);
            if(delivery_charge.equals("0.0"))
                tvDeliveryCharge.setText("FREE ");
            else
                tvDeliveryCharge.setText("AED "+delivery_charge);

            if( extras.getString("Status").toString().equals("false")) {
                ivOrder.setImageResource(R.mipmap.ic_failed_order);
                tvMessage.setText("Payment was Unsuccessful");
            }else{
                ivOrder.setImageResource(R.mipmap.ic_success_order);
                tvTotalPrice.setText(""+Price);
                tvTotalAmount.setText(""+Price);
                if(Constants.analyticEnable)
                    sendAnalyticsData(getApplicationContext(),Price,Constants.orderReference);
            }
        }

    }

    private FirebaseAnalytics mFirebaseAnalytics;
    private static Tracker mTracker;

    public void sendAnalyticsData(Context context,String price , String orderId){

        //productsList.getData()
        ValuCartApp application = (ValuCartApp) getApplication();
        mTracker = application.getDefaultTracker();
        //Log.i(TAG, "Setting screen name: " + name);AED 108.49
        mTracker.setScreenName("Image~" + "Payment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Payment")
                .setAction("Click")
                .build());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "dirham");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Payment");
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, orderId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Payment");
        bundle.putDouble(FirebaseAnalytics.Param.PRICE,Float.parseFloat(price.split("\\s+")[1]));
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY,0);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, 1);
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        //mFirebaseAnalytics.logEvent("share_image", bundle);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        mFirebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        mFirebaseAnalytics.setSessionTimeoutDuration(500);

        //Sets the user ID property.
        mFirebaseAnalytics.setUserId(Constants.bundleId);

        //Sets a user property to a given value.
        mFirebaseAnalytics.setUserProperty("property", "Payment");



        //Facebook Login

        setAutoLogAppEventsEnabled(true);
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);

        logger = AppEventsLogger.newLogger(context);
        //logger.logEvent(AppEventsConstants.EVENT_NAME_AD_CLICK);

        JSONObject jsonObject;
        jsonObject = new JSONObject();
        try {
            jsonObject.put("id",  orderId);
            jsonObject.put("price", Float.parseFloat(price.split("\\s+")[1]));
            jsonObject.put("name", "Payment");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "Durham");
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Payment");
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT,jsonArray.toString());
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "");
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Payment");
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO,
                0,
                params);

    }

    AppEventsLogger logger;
    @Override
    public void onClick(View v) {
        if (rlScheduleIt == v) {
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_schedule_product, null);

            RelativeLayout rlDone = view.findViewById(R.id.rlDone);
            tvDate = view.findViewById(R.id.tvDate);


            rlDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                    if(intervalId.equals(""))
                        Toast.makeText(getApplicationContext(), "Select Interval Duration".toString(), Toast.LENGTH_LONG).show();
                    else if(intervalDate.equals(""))
                        Toast.makeText(getApplicationContext(), "Select Start Date".toString(), Toast.LENGTH_LONG).show();
                    else
                        sendScheduleApi(""+Constants.customer_id,intervalId,intervalDate);
                }
            });

            ImageView ivCancel = view.findViewById(R.id.ivCancel);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                }
            });

            llDate = view.findViewById(R.id.llDate);
            llDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateDialogFragment dateTimeDialogFragment = new DateDialogFragment();
                    dateTimeDialogFragment.show(getSupportFragmentManager(), "");
                    dateTimeDialogFragment.setMinDate(Calendar.getInstance().getTimeInMillis());
                    dateTimeDialogFragment.setDateTimeChangeListener(new DateTimeChangeListener() {
                        @Override
                        public void onDateTimeChange(String dateTime) {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date newDate = format.parse(dateTime);
                                tvDate.setText(dateFormatter.format(newDate));
                                intervalDate = ""+dateFormatter.format(newDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            rvItemsCategories = view.findViewById(R.id.rvItemsCategories);
            intervalAdapter = new AdapterRvInterval(intervalsList, getApplicationContext(), this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            rvItemsCategories.setLayoutManager(horizontalLayoutManager);
            rvItemsCategories.setAdapter(intervalAdapter);


            bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.show();
        }

        if (rlHome == v) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }



    private void getInterval() {
        APIManager.getInstance().getInterval(new APIResponseCallback<Intervals>() {

            @Override
            public void onResponseLoaded(@NonNull Intervals response) {
                try {
                    intervalsList = response;
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void sendScheduleApi(String orderId , String intervalId,String intervalDate) {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        library.showLoading();
        APIManager.getInstance().sendScheduleApi(orderId , intervalId ,intervalDate, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    Toast.makeText(DeliverySuccessScreen.this, response.getAsJsonObject().get("message").getAsString(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                }catch (Exception e){}
            }
            //{"status":1,"message":"The given data was invalid.","errors":{"start_date":["Start date can not be today or past and can not be Friday."]}}
            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.hideLoading();
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("start_date")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("start_date").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString().toString());
                        //Toast.makeText(DeliverySuccessScreen.this, jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("interval_id")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("interval_id").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString().toString());
                        //Toast.makeText(DeliverySuccessScreen.this, jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    library.alertErrorMessage(""+"The given data was invalid.".toString());
                    //Toast.makeText(DeliverySuccessScreen.this,"The given data was invalid.",Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The given data was invalid.");
                    //Toast.makeText(DeliverySuccessScreen.this,"The given data was invalid.",Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    @Override
    public void onItemSelected(String value, int position) {
        intervalId = ""+intervalsList.getData().get(position).getId();
        if (intervalsList.getData().get(position).getSelectedItem()) {
            intervalsList.getData().get(position).setSelectedItem(false);
        } else {

            for (int counter = 0; counter < intervalsList.getData().size(); counter++) {
                if (counter == position)
                    intervalsList.getData().get(counter).setSelectedItem(true);
                else
                    intervalsList.getData().get(counter).setSelectedItem(false);
            }
        }

        if (intervalAdapter == null) {
            intervalAdapter = new AdapterRvInterval(intervalsList, this, this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvItemsCategories.setLayoutManager(horizontalLayoutManager);
            rvItemsCategories.setAdapter(intervalAdapter);
        } else
            intervalAdapter.notifyDataSetChanged();
    }



}

