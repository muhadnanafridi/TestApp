package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvMyAddress;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Addresses;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

public class MyAddressScreen extends Activity implements View.OnClickListener {

    private RecyclerView rvAddItemForCheckout;
    private AdapterRvMyAddress adapterRvMyAddress;
    Library library;
    RelativeLayout rlAddNewAddress;
    Addresses addressesList;
    TextView tvNoRecordAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        library = new Library(this);
        setContentView(R.layout.my_address_screen);

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);
        tvNoRecordAvailable.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddress();
    }

    @Override
    public void onClick(View v) {
        if(rlAddNewAddress ==v){
            Intent intent = new Intent(getApplicationContext(), DeliveryAddressScreen.class);
            intent.putExtra("CallingFrom","MyAddress");
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
                itemList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void itemList(){
        if(addressesList.getData().size()==0)
        {
            tvNoRecordAvailable.setVisibility(View.VISIBLE);
        }else
            tvNoRecordAvailable.setVisibility(View.GONE);

        rlAddNewAddress = findViewById(R.id.rlAddNewAddress);
        rlAddNewAddress.setOnClickListener(this);

        rvAddItemForCheckout = findViewById(R.id.rvAddItemForCheckout);
        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterRvMyAddress = new AdapterRvMyAddress(addressesList, this);

        if(library.IsTablet())
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1 ));
        else
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, 1));
        rvAddItemForCheckout.setAdapter(adapterRvMyAddress);

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

