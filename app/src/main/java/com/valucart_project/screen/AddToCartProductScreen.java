package com.valucart_project.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvCheckout;
import com.valucart_project.application.ValuCartApp;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Products;
import com.valucart_project.models.ProductsAddToCart;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class AddToCartProductScreen extends Activity implements OnItemSelection {

    private RecyclerView rvAddItemForCheckout;
    private AdapterRvCheckout adapterRvBundleSummary; // Replace checkout adopter currently of no use
    Library library;
    RelativeLayout rlApply;
    EditText etPromoCode;
    ProductsAddToCart productsList;
    TextView tvTotalAmount, tvTotalCartsAmmount,tvPromoDiscount,tvPromoSuccessMessage,tvDeliveryCharges;
    Products allProductsList = new Products();
    RelativeLayout rlLoginToCheckOut,rlApplied,rlMainLayout;
    LinearLayout llLoginFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart_product_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        library = new Library(this);

        rlMainLayout = findViewById(R.id.rlMainLayout);
        llLoginFirst = findViewById(R.id.llLoginFirst);

        rlApplied = findViewById(R.id.rlApplied);
        tvPromoSuccessMessage = findViewById(R.id.tvPromoSuccessMessage);

        tvPromoDiscount = findViewById(R.id.tvPromoDiscount);

        tvDeliveryCharges = findViewById(R.id.tvDeliveryCharges);

        tvTotalCartsAmmount = findViewById(R.id.tvTotalCartsAmmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        rlLoginToCheckOut = findViewById(R.id.rlLoginToCheckOut);

        rlLoginToCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        if (!tvTotalAmount.getText().toString().equals("AED 0")) {
                            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                            intent.putExtra("CallFrom", "AddToCart");
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Add products To Cart", Toast.LENGTH_LONG).show();
                        }
                } catch (Exception e) {
                    if (tvTotalAmount.getText().toString().equals("AED 0")) {
                        Toast.makeText(getApplicationContext(), "Add products To Cart", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        RelativeLayout rlAddToCart = findViewById(R.id.rlAddToCart);

        if (Constants.userLogin) {
            rlAddToCart.setVisibility(View.VISIBLE);
            rlLoginToCheckOut.setVisibility(View.GONE);
        } else {
            rlAddToCart.setVisibility(View.GONE);
            rlLoginToCheckOut.setVisibility(View.VISIBLE);
        }

        rlAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String  cart = Constants.cart_id;
                    if (!tvTotalAmount.getText().toString().equals("AED 0")) {
                        if(Constants.analyticEnable)
                            sendAnalyticsData(getApplicationContext());

                            Intent intent = new Intent(getApplicationContext(), DeliveryDateTimeScreen.class);
                            intent.putExtra("CallFrom", "AddToCart");
                            startActivity(intent);

                    } else {
                            Toast.makeText(getApplicationContext(), "Add products To Cart", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (tvTotalAmount.getText().toString().equals("AED 0")) {
                        Toast.makeText(getApplicationContext(), "Add products To Cart", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etPromoCode = findViewById(R.id.etPromoCode);
        //etPromoCode.setText("2761A115");

        rlApply = findViewById(R.id.rlApply);
        rlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                library.hideKeyBoard();
                if (!etPromoCode.getText().toString().equals("")) {
                    loadCoupon(etPromoCode.getText().toString());
                }
            }
        });

        loadMyCart(false);
    }

    private FirebaseAnalytics mFirebaseAnalytics;
    private static Tracker mTracker;

    public void sendAnalyticsData(Context context){

        //productsList.getData()
        ValuCartApp application = (ValuCartApp) getApplication();
        mTracker = application.getDefaultTracker();
        //Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + "CheckOut");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("CheckOut")
                .setAction("Click")
                .build());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "dirham");
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "dirham");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Checkout");
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.bundleId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Checkout");
        bundle.putDouble(FirebaseAnalytics.Param.PRICE, productsList.getData().getPrice());
        int quantity =  productsList.getData().getProducts().size()+ productsList.getData().getBundles().size()+ productsList.getData().getCustomer_bundles().size();
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY,quantity);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, 1);
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        //mFirebaseAnalytics.logEvent("share_image", bundle);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        mFirebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        mFirebaseAnalytics.setSessionTimeoutDuration(500);

        //Sets the user ID property.
        mFirebaseAnalytics.setUserId(Constants.bundleId);

        //Sets a user property to a given value.
        mFirebaseAnalytics.setUserProperty("property", "Checkout");


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
            jsonObject.put("id",  Constants.bundleId);
            jsonObject.put("quantity", quantity);
            jsonObject.put("price", productsList.getData().getPrice());
            jsonObject.put("name", "Checkout");
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
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "");
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Checkout");
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,
                0,
                params);

    }

    AppEventsLogger logger;

    @Override
    public void onItemSelected(String value, int position) {

        if(value.equals("refreshCart"))
            loadMyCart(true);
        else
            loadMyCart(false);

    }


    private void loadMyCart(final boolean refreshAdopter) {
        //6b3b358777b58c3b5128ea1a7eb6accf  2329dd0e0fd029d2baf0620c5336dcb5
        //library.showLoading();
        //Constants.cart_id = "0f552ac7098962a73eb486606d7a1878";
        APIManager.getInstance().getMyCart(Constants.cart_id, new APIResponseCallback<ProductsAddToCart>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsAddToCart response) {
                //library.hideLoading();
                productsList = new ProductsAddToCart();
                productsList = response;
                combineProducts();
                if(Constants.cart_id.equals(""))
                    Constants.cart_id = productsList.getData().getId();
                itemList(refreshAdopter);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    private void itemList(boolean refreshAdopter) {

        rvAddItemForCheckout = findViewById(R.id.rvAddItemForCheckout);
        // add a divider after each item for more clarity
        if (adapterRvBundleSummary == null || refreshAdopter) {

            if (!library.IsTablet()) {
                rvAddItemForCheckout.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                rvAddItemForCheckout.addItemDecoration(new DividerItemDecoration(this, 0));
            }

            adapterRvBundleSummary = new AdapterRvCheckout(allProductsList, this, this);

            if (library.IsTablet())
                rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1));
            else
                rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, 1));
            rvAddItemForCheckout.setAdapter(adapterRvBundleSummary);
        } else {
            adapterRvBundleSummary.notifyDataSetChanged();
        }

        NumberFormat nf = new DecimalFormat("#.####");
        tvTotalCartsAmmount.setText(  "AED " + nf.format(productsList.getData().getTotal_price()));
        tvTotalAmount.setText("AED " + nf.format(productsList.getData().getPrice()));
        tvPromoDiscount.setText("" + productsList.getData().getDiscount()+" ");
        tvDeliveryCharges.setText("" +productsList.getData().getDelivery_charge() +"");

        //rlMainLayout = findViewById(R.id.rlMainLayout);
        //llLoginFirst = findViewById(R.id.llLoginFirst);
        //AED 0

        if(tvTotalCartsAmmount.getText().toString().equals("AED 0")){
            rlMainLayout.setVisibility(View.GONE);
            llLoginFirst.setVisibility(View.VISIBLE);
        }else {
            rlMainLayout.setVisibility(View.VISIBLE);
            llLoginFirst.setVisibility(View.GONE);
        }

        if(!productsList.getData().getCoupon().equals("")){
            rlApplied.setVisibility(View.VISIBLE);
            rlApply.setVisibility(View.GONE);
            etPromoCode.setEnabled(false);
            tvPromoSuccessMessage.setVisibility(View.VISIBLE);
            etPromoCode.setText(""+productsList.getData().getCoupon());
            tvPromoSuccessMessage.setText("Promo code Discount Available");
        }

        RelativeLayout rlShop =  findViewById(R.id.rlShop);
        rlShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToCartProductScreen.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


    public void combineProducts() {
        allProductsList.getData().clear();
        for (int counter = 0; counter < productsList.getData().getProducts().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(productsList.getData().getProducts().get(counter).getItem().getName());
            data.setDescription(productsList.getData().getProducts().get(counter).getItem().getDescription());
            data.setId("" + productsList.getData().getProducts().get(counter).getItem().getId());
            data.setImages(productsList.getData().getProducts().get(counter).getItem().getImages());
            data.setAddToByob(productsList.getData().getProducts().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(productsList.getData().getProducts().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(productsList.getData().getProducts().get(counter).getItem().getValucart_price());
            data.setQuantity(productsList.getData().getProducts().get(counter).getQuantity());
            data.setThumbnail(productsList.getData().getProducts().get(counter).getItem().getThumbnail());
            data.setPackaging_quantity(productsList.getData().getProducts().get(counter).getItem().getPackaging_quantity());
            if(productsList.getData().getProducts().get(counter).getItem().getPackaging_quantity_unit()!=null)
            data.setPackaging_quantity_weight(productsList.getData().getProducts().get(counter).getItem().getPackaging_quantity_unit().getSymbol());
            //data.setPackaging_quantity_unit(productsList.getData().getProducts().get(counter).getItem().getPackaging_quantity_unit());
            data.setProductType("product");
            allProductsList.getData().add(data);
        }

        for (int counter = 0; counter < productsList.getData().getCustomer_bundles().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(productsList.getData().getCustomer_bundles().get(counter).getItem().getName().replaceAll("%20", " "));
            data.setDescription(productsList.getData().getCustomer_bundles().get(counter).getItem().getDescription());
            data.setId("" + productsList.getData().getCustomer_bundles().get(counter).getItem().getId());
            data.setImages(productsList.getData().getCustomer_bundles().get(counter).getItem().getImages());
            data.setAddToByob(productsList.getData().getCustomer_bundles().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(productsList.getData().getCustomer_bundles().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(productsList.getData().getCustomer_bundles().get(counter).getItem().getValucart_price());
            data.setQuantity(productsList.getData().getCustomer_bundles().get(counter).getQuantity());
            data.setThumbnail(productsList.getData().getCustomer_bundles().get(counter).getItem().getThumbnail());
            //data.setPackaging_quantity(""+productsList.getData().getCustomer_bundles().get(counter).getItem().getPackaging_quantity());
            //data.setPackaging_quantity_weight(productsList.getData().getCustomer_bundles().get(counter).getItem().getPackaging_quantity_unit().getSymbol());
            data.setPackaging_quantity("");
            data.setPackaging_quantity_weight("");
            data.setProductType("customer_bundle");
            //data.setPackaging_quantity_unit(productsList.getData().getCustomer_bundles().get(counter).getData().getPackaging_quantity_unit());
            allProductsList.getData().add(data);
        }

        for (int counter = 0; counter < productsList.getData().getBundles().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(productsList.getData().getBundles().get(counter).getItem().getName().replaceAll("%20", " "));
            data.setDescription(productsList.getData().getBundles().get(counter).getItem().getDescription());
            data.setId("" + productsList.getData().getBundles().get(counter).getItem().getId());
            data.setImages(productsList.getData().getBundles().get(counter).getItem().getImages());
            data.setAddToByob(productsList.getData().getBundles().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(productsList.getData().getBundles().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(productsList.getData().getBundles().get(counter).getItem().getValucart_price());
            data.setQuantity(productsList.getData().getBundles().get(counter).getQuantity());
            if (!productsList.getData().getBundles().get(counter).getItem().getThumbnail().equals("")) {
                data.setThumbnail(productsList.getData().getBundles().get(counter).getItem().getThumbnail());
            } else {
                if (productsList.getData().getBundles().get(counter).getItem().getImages().size() > 0)
                    data.setThumbnail(productsList.getData().getBundles().get(counter).getItem().getImages().get(0));
                else
                    data.setThumbnail("");
            }
            //data.setPackaging_quantity(""+productsList.getData().getBundles().get(counter).getItem().getPackaging_quantity());
            //data.setPackaging_quantity_weight(productsList.getData().getBundles().get(counter).getItem().getPackaging_quantity_unit().getSymbol());
            data.setPackaging_quantity("");
            data.setPackaging_quantity_weight("");
            data.setProductType("bundle");
            //data.setPackaging_quantity_unit(productsList.getData().getBundles().get(counter).getData().getPackaging_quantity_unit());
            allProductsList.getData().add(data);
        }
    }

    private void loadCoupon(String coupon) {
        //6b3b358777b58c3b5128ea1a7eb6accf  2329dd0e0fd029d2baf0620c5336dcb5
        library.showLoading();
        APIManager.getInstance().loadCoupon(Constants.cart_id,coupon, new APIResponseCallback<ProductsAddToCart>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsAddToCart response) {
                library.hideLoading();
                productsList = new ProductsAddToCart();
                productsList = response;
                combineProducts();
                itemList(false);
                Toast.makeText(getApplicationContext(), "Thanks For using ValuCart Coupon code to get discount ", Toast.LENGTH_LONG).show();
                rlApplied.setVisibility(View.VISIBLE);
                rlApply.setVisibility(View.GONE);
                etPromoCode.setEnabled(false);
                tvPromoSuccessMessage.setVisibility(View.VISIBLE);
                tvPromoSuccessMessage.setText("Promo code Successfully Applied");
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    tvPromoSuccessMessage.setVisibility(View.VISIBLE);
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("cart_id")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("cart_id").toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Add Some Products in a cart then add promo", Toast.LENGTH_LONG).show();
                        tvPromoSuccessMessage.setText("Add Some Products in a cart then add promo");
                    } else if (jsonObject1.toString().contains("coupon")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("coupon").toString());
                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        tvPromoSuccessMessage.setText(""+jsonArray.get(0).toString());
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Coupon Code is not valid",Toast.LENGTH_LONG).show();
                    tvPromoSuccessMessage.setText("Coupon Code is not valid");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Coupon Code is not valid",Toast.LENGTH_LONG).show();

                }

            }

        });

    }

}

