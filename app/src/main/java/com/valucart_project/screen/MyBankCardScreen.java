package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvMyBankCard;
import com.valucart_project.models.Grocery;
import com.valucart_project.utils.Library;

import java.util.ArrayList;
import java.util.List;

public class MyBankCardScreen extends Activity {

    private List<Grocery> groceryList = new ArrayList<>();
    private RecyclerView rvAddItemForCheckout;
    private AdapterRvMyBankCard adapterRvMyBankCard;
    Library library;
    RelativeLayout rlAddNewCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bank_card_screen);
        library = new Library(this);
        itemList();

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlAddNewCard = findViewById(R.id.rlAddNewCard);
        rlAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckoutScreenPayment.class));
            }
        });

    }

    private void itemList(){
        rvAddItemForCheckout = findViewById(R.id.rvAddItemForCheckout);
        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterRvMyBankCard = new AdapterRvMyBankCard(groceryList, this);

        if(library.IsTablet())
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1 ));
        else
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, 1));
        rvAddItemForCheckout.setAdapter(adapterRvMyBankCard);

        populategroceryList();
    }

    private void populategroceryList(){
        Grocery potato = new Grocery("Potato", R.drawable.ic_product);
        Grocery onion = new Grocery("Onion", R.drawable.ic_product);
        Grocery cabbage = new Grocery("Cabbage", R.drawable.ic_product);
        Grocery cauliflower = new Grocery("Cauliflower", R.drawable.ic_product);

        groceryList.add(potato);
        groceryList.add(onion);
        groceryList.add(cabbage);
        //groceryList.add(cauliflower);
        adapterRvMyBankCard.notifyDataSetChanged();
    }

}

