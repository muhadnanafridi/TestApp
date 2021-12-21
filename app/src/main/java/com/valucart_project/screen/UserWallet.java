package com.valucart_project.screen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvMyWallet;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.WalletRedeme;
import com.valucart_project.models.WalletTransactions;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserWallet extends Activity {

    private RecyclerView rvTransactions;
    private AdapterRvMyWallet adapterRvMyWallet;
    Library library;
    WalletTransactions walletTransactions;
    TextView tvNoRecordAvailable,tvPrice,tvredeme;
    EditText etCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_screen);

        init();
        loadTransactions();

    }

    public  void init(){

        rvTransactions = findViewById(R.id.rvTransactions);

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);

        library = new Library(this);

        tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setText(""+getIntent().getExtras().getString("WalletValue")+"");

        etCode = findViewById(R.id.etCode);
        tvredeme = findViewById(R.id.tvredeme);
        tvredeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                library.hideKeyBoard();
                //loadRedeme("5230476927975");
                 loadRedeme(""+etCode.getText().toString());
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadRedeme(String code) {
        library.showLoading();
        APIManager.getInstance().getRedeme(code, new APIResponseCallback<WalletRedeme>() {

            @Override
            public void onResponseLoaded(@NonNull WalletRedeme response) {
            try {
                library.hideLoading();
                if (response.getStatus() == 1) {
                    etCode.setText("");
                    tvPrice.setText("" + response.getData().getWallet() + "");
                    loadTransactions();
                } else {
                    library.alertErrorMessage(""+response.getMessage());
                    //Toast.makeText(getApplicationContext(), "" + response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.hideLoading();
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Please try again , You entered wrong code" , Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void loadTransactions() {

        APIManager.getInstance().getTransactions(new APIResponseCallback<WalletTransactions>() {

            @Override
            public void onResponseLoaded(@NonNull WalletTransactions response) {
                walletTransactions = response;
                itemList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){}
            }

        });

    }

    private void itemList(){

        if(walletTransactions.getData().size()==0)
        {
            tvNoRecordAvailable.setVisibility(View.VISIBLE);
        }else
            tvNoRecordAvailable.setVisibility(View.GONE);

        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterRvMyWallet = new AdapterRvMyWallet(walletTransactions, this);

        if(library.IsTablet())
            rvTransactions.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1 ));
        else
            rvTransactions.setLayoutManager(new GridLayoutManager(this, 1));
        rvTransactions.setAdapter(adapterRvMyWallet);

    }

}

