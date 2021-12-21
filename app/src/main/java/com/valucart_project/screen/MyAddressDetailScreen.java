package com.valucart_project.screen;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Areas;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAddressDetailScreen extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    RelativeLayout rlUpdate, rlArea, rlState;
    ImageView ivCancel;
    TextView tvHome, tvOffice, tvOther, tvArea, tvState;
    LinearLayout llHome, llOffice, llOther;
    Areas areasList, stateList;
    Spinner spinnerArea, spinnerState;
    private String[] stateBy, areaBy;//=  {"Select", "Minimum Price", "Maximum Price", "Latest Products"};
    public String areaId = "";
    private Library library;
    EditText etBuildingVilla, etFloor, etApartmentNo, etNearByLandmark, etStreet,etPhone,etTag;
    String ItemName = "";
    Bundle extras;
    String CallingFrom = "";
    Boolean stateSelected = false;
    String id, Apartment, stateName ,Area, Building, Floor, Landmark, Street, NameTag,apartmentvillaApi,phone_number;
    String selectedApartmentVilla = "apartment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_address_detail_screen);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        library = new Library(this);

        initFields();
        loadStates();
        radioButtonGroupHandler();
        getIntentRecord();
    }

     TextView tvBuildingVilla;
      LinearLayout llVillaNo;
      LinearLayout llApartmentNo ;
      TextView tvStreet;
    public void radioButtonGroupHandler(){
         tvBuildingVilla = findViewById(R.id.tvBuildingVilla);
         llVillaNo = findViewById(R.id.llVillaNo);
         llApartmentNo = findViewById(R.id.llApartmentNo);
         tvStreet = findViewById(R.id.tvStreet);

        tvBuildingVilla.setVisibility(View.VISIBLE);
        etBuildingVilla.setVisibility(View.VISIBLE);
        llApartmentNo.setVisibility(View.VISIBLE);


        llVillaNo.setVisibility(View.GONE);
        tvStreet.setVisibility(View.GONE);
        etStreet.setVisibility(View.GONE);
        selectedApartmentVilla ="apartment";
        RadioGroup rgApartmentVilla = (RadioGroup) findViewById(R.id.rgApartmentVilla);
        rgApartmentVilla.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rbApartment:
                        // do operations specific to this selection
                        tvBuildingVilla.setVisibility(View.VISIBLE);
                        etBuildingVilla.setVisibility(View.VISIBLE);
                        llApartmentNo.setVisibility(View.VISIBLE);


                        llVillaNo.setVisibility(View.GONE);
                        tvStreet.setVisibility(View.GONE);
                        etStreet.setVisibility(View.GONE);
                        selectedApartmentVilla ="apartment";
                        break;
                    case R.id.rbVilla:
                        // do operations specific to this selection
                        tvBuildingVilla.setVisibility(View.GONE);
                        etBuildingVilla.setVisibility(View.GONE);
                        llApartmentNo.setVisibility(View.GONE);

                        llVillaNo.setVisibility(View.VISIBLE);
                        tvStreet.setVisibility(View.VISIBLE);
                        etStreet.setVisibility(View.VISIBLE);
                        selectedApartmentVilla ="villa";
                        break;
                }
            }
        });



    }

    public void extraBundleOnStart() {
        extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
        }
    }

    public void initFields() {
        stateSelected = false;

        etTag = findViewById(R.id.etTag);
        etStreet = findViewById(R.id.etStreet);

        tvArea = findViewById(R.id.tvArea);
        tvState = findViewById(R.id.tvState);

        rlUpdate = findViewById(R.id.rlUpdate);
        rlUpdate.setOnClickListener(this);

        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(this);

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

        rlArea = findViewById(R.id.rlArea);
        rlArea.setOnClickListener(this);

        rlState = findViewById(R.id.rlState);
        rlState.setOnClickListener(this);

        etBuildingVilla = findViewById(R.id.etBuildingVilla);
        etFloor = findViewById(R.id.etFloor);
        etApartmentNo = findViewById(R.id.etApartmentNo);
        etNearByLandmark = findViewById(R.id.etNearByLandmark);
        etPhone = findViewById(R.id.etPhone);
    }

    @Override
    public void onClick(View v) {

        if (rlUpdate == v) {
            if (areaId.equals(""))
                Toast.makeText(getApplicationContext(), "Please Select Your Area", Toast.LENGTH_LONG).show();

            else if(etPhone.getText().toString().equals("")) {
                library.alertErrorMessage("Please Select Phone");
                //Toast.makeText(getApplicationContext(), "Please Select Tag", Toast.LENGTH_LONG).show();
            }else
                loadAddress();
        }

        if (ivCancel == v) {
            finish();
        }
        if (tvHome == v) {
            llHome.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOffice.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOther.setTextColor(getResources().getColor(R.color.menuItemIconColor));

            ItemName = "Home";
/*            tvHome.setTextSize(18);
            tvHome.setTypeface(Typeface.DEFAULT_BOLD);
            tvOffice.setTextSize(15);
            tvOffice.setTypeface(Typeface.DEFAULT);
            tvOther.setTextSize(15);
            tvOther.setTypeface(Typeface.DEFAULT);*/
        }
        if (tvOffice == v) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOffice.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOther.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            ItemName = "Office";
/*            tvHome.setTextSize(15);
            tvHome.setTypeface(Typeface.DEFAULT);
            tvOffice.setTextSize(18);
            tvOffice.setTypeface(Typeface.DEFAULT_BOLD);
            tvOther.setTextSize(15);
            tvOther.setTypeface(Typeface.DEFAULT);*/
        }
        if (tvOther == v) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            tvHome.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOffice.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOther.setTextColor(getResources().getColor(R.color.colorClearWhite));
            ItemName = "Other";
        }
        if (rlArea == v) {
            spinnerArea.performClick();
        }

        if (rlState == v) {
            spinnerState.performClick();
        }

    }

    private void loadAreas(String statesId) {

        APIManager.getInstance().getArea(statesId, new APIResponseCallback<Areas>() {

            @Override
            public void onResponseLoaded(@NonNull Areas response) {

                areasList = response;
                if (areasList.getData().size() > 0) {
                    areaBy = new String[areasList.getData().size() + 1];
                    areaBy[0] = "Select";
                }
                for (int counter = 1; counter < areasList.getData().size() + 1; counter++) {
                    areaBy[counter] = areasList.getData().get(counter - 1).getName();
                }
                sortByArea();
                rlArea.setVisibility(View.VISIBLE);

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                   // Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    private void loadStates() {

        APIManager.getInstance().getStates(new APIResponseCallback<Areas>() {

            @Override
            public void onResponseLoaded(@NonNull Areas response) {
                stateList = response;
                if (stateList.getData().size() > 0) {
                    stateBy = new String[stateList.getData().size() + 1];
                    stateBy[0] = "Select";
                }
                for (int counter = 1; counter < stateList.getData().size() + 1; counter++) {
                    stateBy[counter] = stateList.getData().get(counter - 1).getName();
                }

                sortByState();
                nameSelected();
                loadAreas("" + 4);
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


    public void sortByState() {

        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        AdapterForSpinner aa = new AdapterForSpinner(this, R.layout.simple_spinner_item, stateBy);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerState.setAdapter(aa);
        spinnerState.setOnItemSelectedListener(this);

    }

    public void sortByArea() {

        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        AdapterForSpinner aa = new AdapterForSpinner(this, R.layout.simple_spinner_item, areaBy);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerArea.setAdapter(aa);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvArea.setText("" + areaBy[position].toString());
                    areaId = "" + areasList.getData().get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            tvState.setText("" + stateBy[position].toString());
            loadAreas("" + stateList.getData().get(position - 1).getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadAddress() {
        library.showLoading();
        APIManager.getInstance().updateAddress(id, etTag.getText().toString(), areaId, etStreet.getText().toString(), etBuildingVilla.getText().toString(), etFloor.getText().toString()
                , etApartmentNo.getText().toString(), etNearByLandmark.getText().toString(), "",etPhone.getText().toString() , selectedApartmentVilla, new APIResponseCallback<JsonElement>() {

                    @Override
                    public void onResponseLoaded(@NonNull JsonElement response) {
                        library.hideLoading();
                        finish();
                    }

                    @Override
                    public void onResponseError(JSONObject jsonObject, Throwable error) {
                        library.hideLoading();
                        try {
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                            if (jsonObject1.toString().contains("area")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("area").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            } else if (jsonObject1.toString().contains("building")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("building").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("street")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("street").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("floor")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("floor").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("apartment")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("apartment").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("tag")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("tag").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("landmark")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("landmark").toString());
                                library.alertErrorMessage(jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("location_type")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("location_type").toString());
                                //library.alertErrorMessage(""+jsonArray.get(0).toString());
                                Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("villa_number")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("villa_number").toString());
                                Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("phone_number")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("phone_number").toString());
                                library.alertErrorMessage(""+jsonArray.get(0).toString());
                                //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.toString().contains("contact_information")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("contact_information").toString());
                                Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                                //library.alertErrorMessage(""+jsonObject.getString("message").toString());
                            }else {
                                library.alertErrorMessage(""+jsonObject.getString("message").toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            library.alertErrorMessage("Given Data is invalid");
                            //Toast.makeText(getApplicationContext(), "Given Data is invalid" .toString(), Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            library.alertErrorMessage("Given Data is invalid");
                        }
                    }

                });
    }

    public void getIntentRecord() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            Apartment = extras.getString("Apartment");
            Area = extras.getString("Area");
            areaId = extras.getString("AreaId");

            Building = extras.getString("Building");
            Floor = extras.getString("villa");
            Landmark = extras.getString("Landmark");
            Street = extras.getString("Street");
            NameTag = extras.getString("Name");
            apartmentvillaApi = extras.getString("apartmentvilla");
            phone_number =  extras.getString("phone");

            tvArea.setText("" + Area);
            tvState.setText(extras.getString("State"));
            etBuildingVilla.setText("" + Building);
            etFloor.setText("" + Floor);
            etApartmentNo.setText("" + Apartment);
            etNearByLandmark.setText("" + Landmark);
            etStreet.setText("" + Street);
            etPhone.setText(""+phone_number);

            RadioButton rbApartment = findViewById(R.id.rbApartment);
            RadioButton rbVilla = findViewById(R.id.rbVilla);

            if(apartmentvillaApi.equals("apartment")){
                rbApartment.setVisibility(View.VISIBLE);
                rbApartment.setChecked(true);
                rbVilla.setVisibility(View.VISIBLE);


                tvBuildingVilla.setVisibility(View.VISIBLE);
                etBuildingVilla.setVisibility(View.VISIBLE);
                llApartmentNo.setVisibility(View.VISIBLE);


                llVillaNo.setVisibility(View.GONE);
                tvStreet.setVisibility(View.GONE);
                etStreet.setVisibility(View.GONE);
                selectedApartmentVilla ="apartment";
            }else {
                rbApartment.setVisibility(View.VISIBLE);
                rbVilla.setVisibility(View.VISIBLE);
                rbVilla.setChecked(true);

                tvBuildingVilla.setVisibility(View.GONE);
                etBuildingVilla.setVisibility(View.GONE);
                llApartmentNo.setVisibility(View.GONE);

                llVillaNo.setVisibility(View.VISIBLE);
                tvStreet.setVisibility(View.VISIBLE);
                etStreet.setVisibility(View.VISIBLE);
                selectedApartmentVilla ="villa";
            }

        }

    }

    public void nameSelected(){

        etTag.setText(""+NameTag);
        if (NameTag.toLowerCase().equals("home")) {
            llHome.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOffice.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOther.setTextColor(getResources().getColor(R.color.menuItemIconColor));

            ItemName = "Home";
/*            tvHome.setTextSize(18);
            tvHome.setTypeface(Typeface.DEFAULT_BOLD);
            tvOffice.setTextSize(15);
            tvOffice.setTypeface(Typeface.DEFAULT);
            tvOther.setTextSize(15);
            tvOther.setTypeface(Typeface.DEFAULT);*/
        }
        if (NameTag.toLowerCase().equals("office")) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvHome.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOffice.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOther.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            ItemName = "Office";
/*            tvHome.setTextSize(15);
            tvHome.setTypeface(Typeface.DEFAULT);
            tvOffice.setTextSize(18);
            tvOffice.setTypeface(Typeface.DEFAULT_BOLD);
            tvOther.setTextSize(15);
            tvOther.setTypeface(Typeface.DEFAULT);*/
        }
        if (NameTag.toLowerCase().equals("other")) {
            llHome.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOffice.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            tvHome.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOffice.setTextColor(getResources().getColor(R.color.menuItemIconColor));
            tvOther.setTextColor(getResources().getColor(R.color.colorClearWhite));
            ItemName = "Other";
        }

    }

}

