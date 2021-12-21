package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvHorizantalDeliveryDate;
import com.valucart_project.adapter.AdapterRvTimeGrid;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Addresses;
import com.valucart_project.models.CheckoutTime;
import com.valucart_project.models.DeliveryDate;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DeliveryDateTimeScreen extends Activity implements WishItemSelection, OnItemSelection, View.OnClickListener {

    private List<DeliveryDate> deliveryDates = new ArrayList<>();
    private RecyclerView rvHorizontalListDate, rvTime;
    private AdapterRvHorizantalDeliveryDate adapterRvHorizantalDeliveryDate;
    private AdapterRvTimeGrid timeAdapter;
    Library library;
    TextView tvHome, tvOffice, tvOther, tvAddNewAddress, tvMyAddress;
    LinearLayout llHome, llOffice, llOther, llDeliveryAddress;
    Addresses addressesList = new Addresses();
    CheckoutTime checkoutTimeList = new CheckoutTime();
    String selectedDate = "", addressId = "", timeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delivery_date_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        library = new Library(this);
        initFields();
        categoriesItem();
        getIntentRecord();
        DeliveryDate();

        RelativeLayout llCheckout = findViewById(R.id.llCheckout);
        llCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressId.equals(""))
                    Toast.makeText(getApplicationContext(),"Add new Address",Toast.LENGTH_LONG).show();
                else if(timeId.equals(""))
                    Toast.makeText(getApplicationContext(),"Select Available Time",Toast.LENGTH_LONG).show();
                else
                    getcheckoutApi();
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadAddress();
        GetTimeApi();

    }

    public static String CallingFrom="";
    TextView tvMonday;
    public void getIntentRecord() {
    //meetmonday
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallFrom");
             tvMonday = findViewById(R.id.tvMonday);
            if(CallingFrom.equals("meetmonday")) {
                tvMonday.setVisibility(View.VISIBLE);
                rvHorizontalListDate.setVisibility(View.GONE);
            }
        }else {
            if(CallingFrom.equals("meetmonday")){
                 tvMonday = findViewById(R.id.tvMonday);
                tvMonday.setVisibility(View.VISIBLE);
                rvHorizontalListDate.setVisibility(View.GONE);
            }
        }
    }

    public void initFields() {
        llDeliveryAddress = findViewById(R.id.llDeliveryAddress);
        tvMyAddress = findViewById(R.id.tvMyAddress);
        tvMyAddress.setVisibility(View.GONE);

        tvAddNewAddress = findViewById(R.id.tvAddNewAddress);
        tvAddNewAddress.setOnClickListener(this);

        tvHome = findViewById(R.id.tvHome);
        tvHome.setOnClickListener(this);

        tvOffice = findViewById(R.id.tvOffice);
        tvOffice.setOnClickListener(this);

        tvOther = findViewById(R.id.tvOther);
        tvOther.setOnClickListener(this);

        llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(this);

        llOffice = findViewById(R.id.llOffice);
        llOffice.setOnClickListener(this);

        llOther = findViewById(R.id.llOther);
        llOther.setOnClickListener(this);

    }

    private void categoriesItem() {
        rvHorizontalListDate = findViewById(R.id.rvHorizontalListDate);
        //rvHorizontalListDate.addItemDecoration(new DividerItemDecoration(this, 0));
        // add a divider after each item for more clarity
        adapterRvHorizantalDeliveryDate = new AdapterRvHorizantalDeliveryDate(deliveryDates, this, "categories", this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHorizontalListDate.setLayoutManager(horizontalLayoutManager);
        rvHorizontalListDate.setAdapter(adapterRvHorizantalDeliveryDate);


    }

    private void DeliveryDate() {
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        SimpleDateFormat simpleMonth = new SimpleDateFormat("MMMM");
        int total_count=7;
        for (int counter = 0; counter < total_count; counter++) {
            //Date date = calendar.getInstance().getTime();
            if(CallingFrom.equals("meetmonday")&& simpleDateformat.format(calendar.getTime()).equals("Mon")){

                if(counter !=0) {
                    deliveryDates.add(new DeliveryDate(calendar.get(Calendar.DAY_OF_MONTH), simpleDateformat.format(calendar.getTime()), true, formatDate.format(calendar.getTime())));
                    selectedDate = formatDate.format(calendar.getTime());
                    tvMonday.setText("Monday, " + selectedDate.split("-")[0] + "th " + simpleMonth.format(calendar.getTime()));
                }else {
                    total_count=9;
                }
            }else if (!CallingFrom.equals("meetmonday")&& counter == 0) {
                deliveryDates.add(new DeliveryDate(calendar.get(Calendar.DAY_OF_MONTH), simpleDateformat.format(calendar.getTime()), true, formatDate.format(calendar.getTime())));
                selectedDate = formatDate.format(calendar.getTime());
            }else {
                deliveryDates.add(new DeliveryDate(calendar.get(Calendar.DAY_OF_MONTH), simpleDateformat.format(calendar.getTime()), false, formatDate.format(calendar.getTime())));
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapterRvHorizantalDeliveryDate.notifyDataSetChanged();
    }


    @Override
    public void onWishValueSelected(String value, int position,String action) {
        selectedDate = deliveryDates.get(position).getCompleteDate();
        GetTimeApi();
    }

    @Override
    public void onItemSelected(String value, int position) {
        for (int count = 0; count < checkoutTimeList.getData().size(); count++) {
            checkoutTimeList.getData().get(count).setSelectedItem("false");
            if (count == position) {
                timeId = "" + checkoutTimeList.getData().get(count).getId();
                checkoutTimeList.getData().get(count).setSelectedItem("true");
            }
        }
        timeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (tvHome == v) {
            llHome.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOffice.setTextColor(getResources().getColor(R.color.colorBlack));
            tvOther.setTextColor(getResources().getColor(R.color.colorBlack));

            tvMyAddress.setText("" + addressesList.getData().get(0).getBuilding() + addressesList.getData().get(0).getVilla_number()+ " , " + addressesList.getData().get(0).getStreet() +addressesList.getData().get(0).getApartment_number() + " , " + addressesList.getData().get(0).getStreet() +addressesList.getData().get(0).getApartment_number() +addressesList.getData().get(0).getLandmark()+"  "  + " , " + addressesList.getData().get(0).getArea().getName() + " , "+addressesList.getData().get(0).getState().getName()+ " " +addressesList.getData().get(0).getPhone_number());
            addressId = "" + addressesList.getData().get(0).getId();
        }
        if (tvOffice == v) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.colorBlack));
            tvOffice.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOther.setTextColor(getResources().getColor(R.color.colorBlack));
            tvMyAddress.setText("" + addressesList.getData().get(1).getBuilding() + addressesList.getData().get(1).getVilla_number()+ " , " + addressesList.getData().get(1).getStreet() +addressesList.getData().get(1).getApartment_number() + "  "  + "  "  +addressesList.getData().get(1).getLandmark() +  " , " + addressesList.getData().get(1).getArea().getName() + " , "+addressesList.getData().get(1).getState().getName()+ " " +addressesList.getData().get(1).getPhone_number());
            addressId = "" + addressesList.getData().get(1).getId();
        }
        if (tvOther == v) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            tvHome.setTextColor(getResources().getColor(R.color.colorBlack));
            tvOffice.setTextColor(getResources().getColor(R.color.colorBlack));
            tvOther.setTextColor(getResources().getColor(R.color.colorClearWhite));

            tvMyAddress.setText("" + addressesList.getData().get(2).getBuilding() + addressesList.getData().get(2).getVilla_number()+ " , " + addressesList.getData().get(2).getStreet() +addressesList.getData().get(2).getApartment_number() + "  "  +addressesList.getData().get(2).getLandmark() + " , " + addressesList.getData().get(2).getArea().getName() + " , "+addressesList.getData().get(2).getState().getName()+ " " +addressesList.getData().get(2).getPhone_number());
            addressId = "" + addressesList.getData().get(2).getId();
        }
        if (tvAddNewAddress == v) {
            Intent intent = new Intent(getApplicationContext(), DeliveryAddressScreen.class);
            intent.putExtra("CallingFrom","DeliveryDateTime");
            startActivity(intent);
        }
    }

    private void loadAddress() {
        library.showLoading();
        APIManager.getInstance().getAddress(new APIResponseCallback<Addresses>() {

            @Override
            public void onResponseLoaded(@NonNull Addresses response) {
                library.hideLoading();
                addressesList = response;
                if (addressesList.getData().size() == 0) {
                    llDeliveryAddress.setVisibility(View.GONE);
                    tvMyAddress.setVisibility(View.GONE);
                } else {

                    llDeliveryAddress.setVisibility(View.VISIBLE);
                    tvMyAddress.setVisibility(View.VISIBLE);
                    addressId = "" + addressesList.getData().get(0).getId();
                    if (addressesList.getData().size() == 1) {
                        tvHome.setText("" + addressesList.getData().get(0).getName());
                        tvMyAddress.setText("" + addressesList.getData().get(0).getBuilding() + addressesList.getData().get(0).getVilla_number()+ " , " + addressesList.getData().get(0).getStreet() +addressesList.getData().get(0).getApartment_number() + "  "+ "  "  +addressesList.getData().get(0).getLandmark()   + " , " + addressesList.getData().get(0).getArea().getName() + " , "+addressesList.getData().get(0).getState().getName()+ " " +addressesList.getData().get(0).getPhone_number());

                        llHome.setVisibility(View.VISIBLE);
                        llOffice.setVisibility(View.GONE);
                        llOther.setVisibility(View.GONE);
                    } else if (addressesList.getData().size() == 2) {
                        tvMyAddress.setText("" + addressesList.getData().get(0).getBuilding() + addressesList.getData().get(0).getVilla_number()+ " , " + addressesList.getData().get(0).getStreet() +addressesList.getData().get(0).getApartment_number() + "  " + "  "  +addressesList.getData().get(0).getLandmark()  + " , " + addressesList.getData().get(0).getArea().getName() + " , "+addressesList.getData().get(0).getState().getName()+ " " +addressesList.getData().get(0).getPhone_number());

                        tvHome.setText("" + addressesList.getData().get(0).getName());
                        tvOffice.setText("" + addressesList.getData().get(1).getName());

                        llHome.setVisibility(View.VISIBLE);
                        llOffice.setVisibility(View.VISIBLE);
                        llOther.setVisibility(View.GONE);
                    } else if (addressesList.getData().size() > 2) {
                        tvMyAddress.setText("" + addressesList.getData().get(0).getBuilding() + addressesList.getData().get(0).getVilla_number()+ " , " + addressesList.getData().get(0).getStreet() +addressesList.getData().get(0).getApartment_number() + "  " + "  "  +addressesList.getData().get(0).getLandmark()  + " , " + addressesList.getData().get(0).getArea().getName() + " , "+addressesList.getData().get(0).getState().getName()+ " " +addressesList.getData().get(0).getPhone_number());

                        tvHome.setText("" + addressesList.getData().get(0).getName());
                        tvOffice.setText("" + addressesList.getData().get(1).getName());
                        tvOther.setText("" + addressesList.getData().get(2).getName());

                        llHome.setVisibility(View.VISIBLE);
                        llOffice.setVisibility(View.VISIBLE);
                        llOther.setVisibility(View.VISIBLE);
                    }

                    llHome.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
                    llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                    llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

                    tvHome.setTextColor(getResources().getColor(R.color.colorClearWhite));
                    tvOffice.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvOther.setTextColor(getResources().getColor(R.color.colorBlack));

                }
                //startActivity(new Intent(getApplicationContext(), DeliveryDateTimeScreen.class));
                //finish();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                llDeliveryAddress.setVisibility(View.GONE);
                tvMyAddress.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }


    private void GetTimeApi() {
        //library.showLoading();
        APIManager.getInstance().getTime(selectedDate,new APIResponseCallback<CheckoutTime>() {

            @Override
            public void onResponseLoaded(@NonNull CheckoutTime response) {
                //library.hideLoading();
                checkoutTimeList = response;
                itemList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //library.hideLoading();
                llDeliveryAddress.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void itemList() {
        int checkAvailablePosition = 0;
        timeId = "";
        while (checkoutTimeList.getData().size()>checkAvailablePosition){
            if(checkoutTimeList.getData().get(checkAvailablePosition).getAvailable()) {
                checkoutTimeList.getData().get(checkAvailablePosition).setSelectedItem("true");
                timeId = "" + checkoutTimeList.getData().get(checkAvailablePosition).getId();
                checkAvailablePosition=checkoutTimeList.getData().size();
            }else
                checkAvailablePosition++;
        }

        rvTime = findViewById(R.id.rvTime);
        //rvTime.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //if(timeAdapter==null) {
            timeAdapter = new AdapterRvTimeGrid(checkoutTimeList, this, this);
            if (library.IsTablet())
                rvTime.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 5 : 3));
            else
                rvTime.setLayoutManager(new GridLayoutManager(this, 2));
            rvTime.setAdapter(timeAdapter);
        //}else
        //     timeAdapter.notifyDataSetChanged();
        // rvTime.invalidate();
    }

//{"status":1,"data":{"id":55,"customer_id":2,"payment_type":"cod","sub_total_price":146,"first_order_discount":0,"price":146,"order_reference":"ValuCart34075","time_slot_id":1,"delivery_date":"2019-04-28","created_at":"2019-04-28 17:51:46","updated_at":"2019-04-28 17:51:54","cart_id":42,"address_id":2}}
    String idCheckout;
    private void getcheckoutApi() {
        library.showLoading();

        if(CallingFrom.equals("meetmonday")){
            idCheckout = Constants.bundleId;
        }else {
            idCheckout = Constants.cart_id;
        }

        APIManager.getInstance().getcheckout(CallingFrom,"" + idCheckout, selectedDate, addressId, timeId, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                //checkoutTimeList =response;
                //itemList();
                Constants.orderReference = response.getAsJsonObject().get("data").getAsJsonObject().get("order_reference").getAsString();
                Constants.customer_id =response.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
                startActivity(new Intent(getApplicationContext(), OrderSummaryScreen.class));
                //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                   // library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                        if (jsonObject1.toString().contains("time_slot_id")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("time_slot_id").toString());
                            library.alertErrorMessage(jsonArray.get(0).toString());
                            //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        } else if (jsonObject1.toString().contains("address_id")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("address_id").toString());
                            library.alertErrorMessage(jsonArray.get(0).toString());
                            //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        } else if (jsonObject1.toString().contains("delivery_date")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("delivery_date").toString());
                            library.alertErrorMessage(jsonArray.get(0).toString());
                            //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        } else if (jsonObject1.toString().contains("cart_id")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("cart_id").toString());
                            library.alertErrorMessage(jsonArray.get(0).toString());
                            //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        }else {
                            library.alertErrorMessage(""+jsonObject.getString("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        library.alertErrorMessage("The user credentials were incorrect.");
                        //Toast.makeText(getApplicationContext(),"The user credentials were incorrect.",Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        library.alertErrorMessage("The user credentials were incorrect.");
                    }

                }catch (Exception e){}
            }

        });
        //{"customer_id":2,"order_reference":"ValuCart41116","delivery_date":"2019-04-25","address_id":"2","cart_id":"1","time_slot_id":"2","price":100.7,"updated_at":"2019-04-25 11:36:39","created_at":"2019-04-25 11:36:39","id":1}
    }

}

