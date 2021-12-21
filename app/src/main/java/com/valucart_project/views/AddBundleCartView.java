package com.valucart_project.views;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.valucart_project.R;
import com.valucart_project.application.ValuCartApp;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnAddBYOBToCartSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class AddBundleCartView extends RelativeLayout implements View.OnClickListener {

    private LayoutInflater inflater;
    RelativeLayout rlAddtoCart, rlAddItems,rlOutOfStock;
    ImageView ivRemoveItem, ivAddItem;
    TextView tvTotalItem;
    OnAddBYOBToCartSelection onAddToCartSelection;
    int position;
    static double d = 1.0;
    static float aFloat = (float) d;
    int limitOfItem = 5;
    String id = "" ,itemType = "";
    String ItemSelectFinder = "",priceItem="",name="";
    ProgressBar pbTotalItem;
    Library library;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static Tracker mTracker;

    public AddBundleCartView(Context context) {
        super(context);
        init(null);
    }

    public AddBundleCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AddBundleCartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        library = new Library(getContext());

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_bundle_cart_view, null);

        rlAddtoCart = view.findViewById(R.id.rlAddtoCart);
        rlAddtoCart.setOnClickListener(this);

        rlOutOfStock = view.findViewById(R.id.rlOutOfStock);

        rlAddItems = view.findViewById(R.id.rlAddItems);

        ivRemoveItem = view.findViewById(R.id.ivRemoveItem);
        ivRemoveItem.setOnClickListener(this);

        tvTotalItem = view.findViewById(R.id.tvTotalItem);

        ivAddItem = view.findViewById(R.id.ivAddItem);
        ivAddItem.setOnClickListener(this);
        pbTotalItem = view.findViewById(R.id.pbTotalItem);

        addView(view);
    }

    @Override
    public void onClick(View v) {
        if (rlAddtoCart == v) {
            if (pbTotalItem.getVisibility() == View.GONE) {
                pbTotalItem.setVisibility(VISIBLE);
                ItemSelectFinder = "AddingFirstItem";
                loadByob("add", id, itemType);
            }
        }

        if (ivRemoveItem == v) {
            if (pbTotalItem.getVisibility() == View.GONE) {
                ItemSelectFinder = "RemoveItem";
                pbTotalItem.setVisibility(VISIBLE);
                //loadByob("subtract", id, itemType);
                loadByob("reduce", id, itemType);

                tvTotalItem.setVisibility(GONE);
            }
        }

        if (ivAddItem == v) {
            if (pbTotalItem.getVisibility() == View.GONE) {
                ItemSelectFinder = "AddingSecondOrMoreThenThis";
                pbTotalItem.setVisibility(VISIBLE);
                tvTotalItem.setVisibility(GONE);
                loadByob("add", id, itemType);
            }
        }
    }

    public void showAddMoreItems() {
        rlAddtoCart.setVisibility(GONE);
        rlAddItems.setVisibility(VISIBLE);
    }

    public void initInterface(OnAddBYOBToCartSelection onAddToCartSelection1, int position1,String id1 , String itemType1,String price1 , String name1 ) {
        onAddToCartSelection = onAddToCartSelection1;
        position = position1;
        id = id1;
        itemType = itemType1;
        limitOfItem = 5;
        priceItem=price1;
        name=name1;
    }

    public void addItemListener(int totalItem){
        /*if(totalItem==0) {
            rlAddtoCart.setVisibility(VISIBLE);
            rlAddItems.setVisibility(GONE);
        }else {*/
            tvTotalItem.setText("" + totalItem);
            showAddMoreItems();
        //}
    }

    public void showAddToCard() {
        rlAddtoCart.setVisibility(VISIBLE);
        rlAddItems.setVisibility(GONE);
        rlOutOfStock.setVisibility(GONE);
    }

    public void outOfStock() {
        rlAddtoCart.setVisibility(GONE);
        rlAddItems.setVisibility(GONE);
        rlOutOfStock.setVisibility(VISIBLE);
    }

    public void changeLimit(int limitValue){
        limitOfItem = limitValue;
    }

    private void loadByob(final String action , String product_id , String itemType) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        if(Constants.analyticEnable)
            sendAnalyticsData(getContext(),action,product_id,itemType);
        APIManager.getInstance().addByobSummary(action, product_id,Constants.bundleId, new APIResponseCallback<ByobSummary>() {

            @Override
            public void onResponseLoaded(@NonNull ByobSummary response) {
                try {
                    pbTotalItem.setVisibility(GONE);
                    tvTotalItem.setVisibility(VISIBLE);
                    if(response.getStatus()==1) {
                        performActionOnApiResponse(response, action);
                    }else {
                        library.alertErrorMessage(""+response.getMessage());
                       // Toast.makeText(getContext(),response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                pbTotalItem.setVisibility(GONE);
                tvTotalItem.setVisibility(VISIBLE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    public static void displayTotalCart(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, context.MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        editor.apply();

    }

    public void performActionOnApiResponse(ByobSummary response , String action){
        pbTotalItem.setVisibility(GONE);
        tvTotalItem.setVisibility(VISIBLE);
        if (ItemSelectFinder.equals("AddingFirstItem")) {

            rlAddtoCart.setVisibility(GONE);
            rlAddItems.setVisibility(VISIBLE);
            tvTotalItem.setText("" + (Integer.parseInt(tvTotalItem.getText().toString())));
            //DashboardActivity.totalCartItem(tvTotalItem.getText().toString());

            if (onAddToCartSelection != null)
                onAddToCartSelection.onAddToCart(response, action);
        }

        else if (ItemSelectFinder.equals("RemoveItem")) {
            if (Integer.parseInt(tvTotalItem.getText().toString()) == 1) {
                rlAddtoCart.setVisibility(VISIBLE);
                rlAddItems.setVisibility(GONE);
                //DashboardActivity.totalCartItem("makeItInvisable");
                //DashboardActivity.totalCartItem("subtract");
            } else {
                tvTotalItem.setVisibility(VISIBLE);
                tvTotalItem.setText("" + (Integer.parseInt(tvTotalItem.getText().toString()) - 1));
                //DashboardActivity.totalCartItem("subtract");
            }
            if (onAddToCartSelection != null)
                onAddToCartSelection.onAddToCart(response, action);
        }

        if (ItemSelectFinder.equals("AddingSecondOrMoreThenThis")) {
            //if (Integer.parseInt(tvTotalItem.getText().toString()) < limitOfItem) {

            tvTotalItem.setVisibility(VISIBLE);
            tvTotalItem.setText("" + (Integer.parseInt(tvTotalItem.getText().toString()) + 1));
            //DashboardActivity.totalCartItem("add");
            if (onAddToCartSelection != null)
                onAddToCartSelection.onAddToCart(response, action);
            /*} else {
                if (limitOfItem == 1)
                    Toast.makeText(getContext(), "You can only add " + limitOfItem + " same item in Offer", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "You can only add " + limitOfItem + "same item", Toast.LENGTH_LONG).show();
            }*/

        }
    }

    public void sendAnalyticsData(Context context,String action, String item_id, String item_type) {

        try {
        ValuCartApp application = (ValuCartApp) context;
        mTracker = application.getDefaultTracker();
        //Log.i(TAG, "Setting screen name: " + name);
        //mTracker.setScreenName("Image~" + "aewwae");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Add to Cart")
                .setAction("Click")
                .build());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "dirham");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item_type);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item_id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putDouble(FirebaseAnalytics.Param.PRICE, Float.parseFloat(priceItem));
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY, 1);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, 1);
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        //mFirebaseAnalytics.logEvent("share_image", bundle);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        mFirebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        mFirebaseAnalytics.setSessionTimeoutDuration(500);

        //Sets the user ID property.
        mFirebaseAnalytics.setUserId("");

        //Sets a user property to a given value.
        mFirebaseAnalytics.setUserProperty("property", action);


            //Facebook Login

            setAutoLogAppEventsEnabled(true);
            FacebookSdk.setAutoInitEnabled(true);
            FacebookSdk.fullyInitialize();
            setAdvertiserIDCollectionEnabled(true);

            logger = AppEventsLogger.newLogger(context);
            logger.logEvent(AppEventsConstants.EVENT_NAME_AD_CLICK);

            JSONObject jsonObject;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", item_id);
                jsonObject.put("quantity", "1");
                jsonObject.put("price", priceItem);
                jsonObject.put("name", name);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            Bundle params = new Bundle();
            params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "Durham");
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT,jsonArray.toString());
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "HDFU-8452");
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, item_type);
            logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART,
                    0,
                    params);

        }catch (Exception e){}

    }
    AppEventsLogger logger;
}

