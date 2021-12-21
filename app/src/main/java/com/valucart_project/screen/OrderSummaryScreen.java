package com.valucart_project.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
/*import com.telr.mobile.sdk.entity.request.payment.Address;
import com.telr.mobile.sdk.entity.request.payment.App;
import com.telr.mobile.sdk.entity.request.payment.Billing;
import com.telr.mobile.sdk.entity.request.payment.MobileRequest;
import com.telr.mobile.sdk.entity.request.payment.Name;
import com.telr.mobile.sdk.entity.request.payment.Tran;*/
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterOrderSummary;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.OrderSummary;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryScreen extends FragmentActivity {

    private List<Grocery> groceryList = new ArrayList<>();
    private RecyclerView rvBunderList;
    private AdapterOrderSummary adapterOrderSummary;
    Library library;
    public static final boolean isSecurityEnabled = false;      // Mark false to test on simulator, True to test on actual device and Production
    private String amount = "350"; // Just for testing

    public static final String KEY = "9Th3#nF3TJ~z9wBn";        // TODO: Insert your Key here
    public static final String STORE_ID = "21779 ";    // TODO: Insert your Store ID here  21779
    public static final String EMAIL = "afridi@valucart.com";     // TODO: Insert the customer email here
    OrderSummary orderSummaryList = new OrderSummary();
    Products allorderSummaryList = new Products();
    TextView tvAllDeliveryDetail, tvTotalAmount;
    String paymentType = "cod";
    CheckBox radioPayCash, radioPayCard,radioWallet;
    String url, poll_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_screen);
        library = new Library(this);

        ImageView ivMenuSearch = findViewById(R.id.ivMenuSearch);
        ivMenuSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SearchDialogFragment searchDialogFragment = new SearchDialogFragment( getApplicationContext());
                //searchDialogFragment.show(getSupportFragmentManager(),"");
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout rlConfirm = findViewById(R.id.rlConfirm);
        rlConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //radioPayCash = (CheckBox) findViewById(R.id.radioPayCash);
                //radioPayCard = (CheckBox) findViewById(R.id.radioPayCard);
                //radioWallet = (CheckBox) findViewById(R.id.radioWallet);
                paymentType = "";

                if(radioWallet.isChecked()){
                    paymentType = "wallet";
                }

                if(radioPayCash.isChecked()){
                    //paymentType = "cod";
                    if(paymentType.equals("")){
                        paymentType = "cod";
                    }else {
                        paymentType =paymentType+",cod";
                    }
                }
                if(radioPayCard.isChecked()){

                    if(paymentType.equals("")){
                        paymentType = "card";
                    }else if(radioPayCash.isChecked()){
                        Toast.makeText(getApplicationContext(),"Please select either Pay by cash or Pay by card .",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        paymentType =paymentType+",card";
                    }
                }


                if(paymentType.equals("")){
                    Toast.makeText(getApplicationContext(),"Select Payment type",Toast.LENGTH_LONG).show();
                }else
                        GetOrder();
                /*Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //EditText editText =(EditText) findViewById(R.id.text_amount);
                //amount = editText.getText().toString();

                intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest());
                intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.valucart_project.screen.DeliverySuccessScreen");
                intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.valucart_project.screen.DeliverySuccessScreen");
                intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);
                startActivity(intent);*/
            }
        });
        SharedPreferences prefs = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE);

        if (prefs.getString("TotalCart", null) != null) {
            if (!prefs.getString("TotalCart", null).equals(""))
                Constants.totalCart = Integer.parseInt(prefs.getString("TotalCart", null));
            Constants.cart_id = prefs.getString("cart_id", null);
            if (prefs.getString("token", null) != null) {
                if (!prefs.getString("token", null).equals("")) {
                    Constants.access_token = prefs.getString("token", null);
                    Constants.userLogin = true;
                }
            }
        }
        GetOrderApi();
        initFields();
    }

    public void initFields() {

        tvAllDeliveryDetail = findViewById(R.id.tvAllDeliveryDetail);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        //RadioGroup radioPayBy = (RadioGroup) findViewById(R.id.radioPayBy);
        radioPayCash = (CheckBox) findViewById(R.id.radioPayCash);
        radioPayCard = (CheckBox) findViewById(R.id.radioPayCard);
        radioWallet = (CheckBox) findViewById(R.id.radioWallet);

        paymentType = "cod";

        radioPayCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radioPayCard.setChecked(false);
                    radioPayCash.setChecked(true);
                }
            }
        });

        radioPayCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radioPayCash.setChecked(false);
                    radioPayCard.setChecked(true);
                }
            }
        });

/*
        radioPayBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioPayCash) {
                    paymentType = "cod";
                } else if (checkedId == R.id.radioPayCard) {
                    paymentType = "card";
                }
            }
        });
*/

    }

/*
    private MobileRequest getMobileRequest() {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("123456789");                          // Application installation ID
        app.setName("Telr SDK DEMO");                    // Application name
        app.setUser("123456");                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("1");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("auth");

*/
/* Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        *//*



        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API");         // Transaction description
        tran.setCurrency("AED");                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount);                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //tran.setRef(???);                           // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        tran.setLangauge("en");                        // (Optinal) default is en -> English
        mobile.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();
        address.setCity("Dubai");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("AE");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Dubai");                     // Region
        address.setLine1("SIT G=Towe");                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst("Hany");                          // Forename : the minimum required details for a transaction to be processed
        name.setLast("Sakr");                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mr");                           // Title
        billing.setName(name);
        billing.setEmail(EMAIL);                 // TODO: Insert your email here : the minimum required details for a transaction to be processed.
        billing.setPhone("588519952");                // Phone number, required if enabled in your merchant dashboard.
        mobile.setBilling(billing);
        return mobile;

    }
*/

    private void GetOrderApi() {
        //String order_id="ValuCart41116";
        String order_id = Constants.orderReference;
        library.showLoading();
        APIManager.getInstance().getOrder(order_id, new APIResponseCallback<OrderSummary>() {

            @Override
            public void onResponseLoaded(@NonNull OrderSummary response) {
                library.hideLoading();
                orderSummaryList = response;
                combineProducts();

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    public void combineProducts() {
        allorderSummaryList.getData().clear();
        for (int counter = 0; counter < orderSummaryList.getData().getProducts().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(orderSummaryList.getData().getProducts().get(counter).getItem().getName());
            data.setDescription(orderSummaryList.getData().getProducts().get(counter).getItem().getDescription());
            data.setId("" + orderSummaryList.getData().getProducts().get(counter).getItem().getId());
            data.setImages(orderSummaryList.getData().getProducts().get(counter).getItem().getImages());
            data.setAddToByob(orderSummaryList.getData().getProducts().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(orderSummaryList.getData().getProducts().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(orderSummaryList.getData().getProducts().get(counter).getItem().getValucart_price());
            data.setQuantity(orderSummaryList.getData().getProducts().get(counter).getQuantity());
            data.setThumbnail(orderSummaryList.getData().getProducts().get(counter).getItem().getThumbnail());
            data.setPackaging_quantity(orderSummaryList.getData().getProducts().get(counter).getItem().getPackaging_quantity());
            data.setPackaging_quantity_weight(orderSummaryList.getData().getProducts().get(counter).getItem().getPackaging_quantity() + " " + orderSummaryList.getData().getProducts().get(counter).getItem().getPackaging_quantity_unit().getSymbol());
            //data.setPackaging_quantity_unit(orderSummaryList.getData().getProducts().get(counter).getData().getPackaging_quantity_unit());
            data.setProductType("product");
            data.setDelivery_date(orderSummaryList.getData().getDelivery_date());
            allorderSummaryList.getData().add(data);
        }

        for (int counter = 0; counter < orderSummaryList.getData().getCustomer_bundles().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getName());
            data.setDescription(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getDescription());
            data.setId("" + orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getId());
            data.setImages(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getImages());
            data.setAddToByob(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getPrice());
            data.setQuantity(orderSummaryList.getData().getCustomer_bundles().get(counter).getQuantity());
            if (orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getThumbnail() != null) {
                if (!orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getThumbnail().equals("")) {
                    data.setThumbnail(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getThumbnail());
                } else {
                    if (orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getImages().size() > 0)
                        data.setThumbnail(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getImages().get(0));
                    else
                        data.setThumbnail("");
                }
            } else {
                if (orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getImages().size() > 0)
                    data.setThumbnail(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getImages().get(0));
                else
                    data.setThumbnail("");
            }
            //data.setThumbnail(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getThumbnail());
            data.setPackaging_quantity(orderSummaryList.getData().getCustomer_bundles().get(counter).getItem().getPackaging_quantity());
            //data.setPackaging_quantity_weight(orderSummaryList.getData().getCustomer_bundles().get(counter).getData().getPackaging_quantity()+" " +orderSummaryList.getData().getCustomer_bundles().get(counter).getData().getPackaging_quantity_unit().getSymbol() );
            data.setPackaging_quantity_weight("");

            data.setProductType("customer_bundle");
            data.setDelivery_date(orderSummaryList.getData().getDelivery_date());
            //data.setPackaging_quantity_unit(orderSummaryList.getData().getCustomer_bundles().get(counter).getData().getPackaging_quantity_unit());
            allorderSummaryList.getData().add(data);
        }
        for (int counter = 0; counter < orderSummaryList.getData().getBundles().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(orderSummaryList.getData().getBundles().get(counter).getItem().getName().replaceAll("%20", " "));
            data.setDescription(orderSummaryList.getData().getBundles().get(counter).getItem().getDescription());
            data.setId("" + orderSummaryList.getData().getBundles().get(counter).getItem().getId());
            data.setImages(orderSummaryList.getData().getBundles().get(counter).getItem().getImages());
            data.setAddToByob(orderSummaryList.getData().getBundles().get(counter).getItem().getAddToByob());
            data.setMaximum_selling_price(orderSummaryList.getData().getBundles().get(counter).getItem().getMaximum_selling_price());
            data.setValucart_price(orderSummaryList.getData().getBundles().get(counter).getItem().getValucart_price());
            data.setQuantity(orderSummaryList.getData().getBundles().get(counter).getQuantity());
            // data.setThumbnail(orderSummaryList.getData().getBundles().get(counter).getItem().getThumbnail());
            if (orderSummaryList.getData().getBundles().get(counter).getItem().getThumbnail() != null) {
                if (!orderSummaryList.getData().getBundles().get(counter).getItem().getThumbnail().equals("")) {
                    data.setThumbnail(orderSummaryList.getData().getBundles().get(counter).getItem().getThumbnail());
                } else {
                    if (orderSummaryList.getData().getBundles().get(counter).getItem().getImages().size() > 0)
                        data.setThumbnail(orderSummaryList.getData().getBundles().get(counter).getItem().getImages().get(0));
                    else
                        data.setThumbnail("");
                }
            } else {
                if (orderSummaryList.getData().getBundles().get(counter).getItem().getImages().size() > 0)
                    data.setThumbnail(orderSummaryList.getData().getBundles().get(counter).getItem().getImages().get(0));
                else
                    data.setThumbnail("");
            }
            data.setPackaging_quantity(orderSummaryList.getData().getBundles().get(counter).getItem().getPackaging_quantity());
            //data.setPackaging_quantity_weight(orderSummaryList.getData().getBundles().get(counter).getData().getPackaging_quantity()+" " +orderSummaryList.getData().getBundles().get(counter).getData().getPackaging_quantity_unit().getSymbol() );
            data.setPackaging_quantity_weight("");

            data.setProductType("bundle");
            data.setDelivery_date(orderSummaryList.getData().getDelivery_date());
            //data.setPackaging_quantity_unit(orderSummaryList.getData().getBundles().get(counter).getData().getPackaging_quantity_unit());
            allorderSummaryList.getData().add(data);
        }


        itemList();
    }

    private void itemList() {
        try {
            rvBunderList = findViewById(R.id.rvBunderList);
            // add a divider after each item for more clarity
            //rvBunderList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            adapterOrderSummary = new AdapterOrderSummary(allorderSummaryList, this);

            if (library.IsTablet())
                rvBunderList.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1));
            else
                rvBunderList.setLayoutManager(new GridLayoutManager(this, 1));

            rvBunderList.setAdapter(adapterOrderSummary);

            tvTotalAmount.setText("AED " + orderSummaryList.getData().getTotal_price());
            if (orderSummaryList.getData().getDelivery_address() != null) {
/*
                String building = orderSummaryList.getData().getDelivery_address().getBuilding() == null ? "" : orderSummaryList.getData().getDelivery_address().getBuilding();
                String floor = orderSummaryList.getData().getDelivery_address().getFloor() == null ? "" : " "+orderSummaryList.getData().getDelivery_address().getFloor();
                String apartment = orderSummaryList.getData().getDelivery_address().getApartment() == null ? "" : " " +orderSummaryList.getData().getDelivery_address().getApartment();
                String landmark = orderSummaryList.getData().getDelivery_address().getLandmark() == null ? "" : "," +orderSummaryList.getData().getDelivery_address().getLandmark();
                String area = orderSummaryList.getData().getDelivery_address().getArea().getName() == null ? "" :  " " + orderSummaryList.getData().getDelivery_address().getArea().getName();
*/

                tvAllDeliveryDetail.setText(orderSummaryList.getData().getDelivery_address()+"\n"+orderSummaryList.getData().getDelivery_date()+" "+orderSummaryList.getData().getDelivery_time());
            }

        } catch (Exception e) {
        }
    }

    private void GetOrder() {
        String order_id = Constants.customer_id;
        library.showLoading();
        //paymentType = "wallet,cod";

        APIManager.getInstance().getOrderPayment(order_id, paymentType, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                try {
                    if (response.getAsJsonObject().get("status").getAsString().equals("1")) {
                        if (paymentType.equals("cod") || paymentType.equals("wallet,cod")) {
                            Intent intent = new Intent(getApplicationContext(), DeliverySuccessScreen.class);
                            intent.putExtra("Price", "AED " + orderSummaryList.getData().getTotal_price());
                            intent.putExtra("Status", "true");
                            intent.putExtra("delivery_charge", "" + orderSummaryList.getData().getDelivery_charge());
                            startActivity(intent);
                        } else if (paymentType.equals("card") || paymentType.equals("wallet,card")) {
                            url = response.getAsJsonObject().get("data").getAsJsonObject().get("payment_url").getAsString();
                            poll_url = response.getAsJsonObject().get("data").getAsJsonObject().get("poll_url").getAsString();
                            Intent intent = new Intent(getApplicationContext(), PaymentByCardScreen.class);
                            intent.putExtra("url", "" + url);
                            intent.putExtra("poll_url", "" + poll_url);
                            intent.putExtra("Price", "AED " + orderSummaryList.getData().getTotal_price());
                            intent.putExtra("delivery_charge", "" + orderSummaryList.getData().getDelivery_charge());
                            startActivity(intent);
                        } else if (paymentType.equals("wallet")) {
                            Intent intent = new Intent(getApplicationContext(), DeliverySuccessScreen.class);
                            intent.putExtra("Price", "AED " + orderSummaryList.getData().getTotal_price());
                            intent.putExtra("Status", "true");
                            intent.putExtra("delivery_charge", "" + orderSummaryList.getData().getDelivery_charge());
                            startActivity(intent);
                        }
                    }else {
                        library.alertErrorMessage(""+response.getAsJsonObject().get("message").getAsString());
                    }
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

}
