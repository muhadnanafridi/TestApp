package com.valucart_project.screen;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvBYOBApproval;
import com.valucart_project.adapter.AdapterRvHorizantalSearchRelatedCategories;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.utils.Library;

import java.util.ArrayList;
import java.util.List;

public class BuildYourOwnBundleApprovalScreen extends FragmentActivity implements OnItemSelection {

    private List<Grocery> groceryList = new ArrayList<>();
    private List<Grocery> categoriesList = new ArrayList<>();
    private RecyclerView rvBYOB,rvItemsCategories;
    private AdapterRvBYOBApproval groceryAdapter;
    Library library;
    private AdapterRvHorizantalSearchRelatedCategories categoriesAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_your_own_bundle_approval_screen);
        library = new Library(this);
        itemList();
        categoriesItem();

        ImageView ivMenuSearch = findViewById(R.id.ivMenuSearch);
        ivMenuSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SearchDialogFragment searchDialogFragment = new SearchDialogFragment( getApplicationContext());
                //searchDialogFragment.show(getSupportFragmentManager(),"");
                Intent intent = new Intent(getApplicationContext(), SearchScreen.class);
                startActivity(intent);
            }
        });


        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RelativeLayout rlAddMore= findViewById(R.id.rlAddMore);
        rlAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout rlNext= findViewById(R.id.rlNext);
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), BundleSummaryScreen.class));
                startActivity(new Intent(getApplicationContext(), AddToCartProductScreen.class));
            }
        });

    }
    
    private void itemList(){
        /*rvBYOB = findViewById(R.id.rvBYOB);
        // add a divider after each item for more clarity
        //rvBYOB.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        groceryAdapter = new AdapterRvBYOBApproval(groceryList, this );

        if(library.IsTablet())
            rvBYOB.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 5 : 3 ));
        else
            rvBYOB.setLayoutManager(new GridLayoutManager(this, 3));
        rvBYOB.setAdapter(groceryAdapter);


        populategroceryList();*/
    }

    private void populategroceryList(){
        Grocery potato = new Grocery("Potato", R.drawable.ic_product);
        Grocery onion = new Grocery("Onion", R.drawable.ic_product);
        Grocery cabbage = new Grocery("Cabbage", R.drawable.ic_product);
        Grocery cauliflower = new Grocery("Cauliflower", R.drawable.ic_product);

        groceryList.add(potato);
        groceryList.add(onion);
        groceryList.add(cabbage);
        groceryList.add(cauliflower);
        groceryList.add(cauliflower);
        groceryList.add(cauliflower);
        groceryList.add(cauliflower);

        groceryAdapter.notifyDataSetChanged();
    }
    
    private void categoriesItem() {
        rvItemsCategories = findViewById(R.id.rvItemsCategories);
        //rvFeaturedItems.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        categoriesAdapter = new AdapterRvHorizantalSearchRelatedCategories(categoriesList, this, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(categoriesAdapter);
        populateCategoriesList();
    }

    private void populateCategoriesList(){
        categoriesList.add(new Grocery("Nuts", R.drawable.ic_product));
        categoriesList.add(new Grocery("Pasta", R.drawable.ic_product));
        categoriesList.add(new Grocery("Breakfast Items", R.drawable.ic_product));
        categoriesList.add(new Grocery("Beveragea", R.drawable.ic_product));
        categoriesList.add( new Grocery("Biscuits and Cakes", R.drawable.ic_product));
        categoriesList.add( new Grocery("Olives and Pickles", R.drawable.ic_product));
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(String value, int position) {

        if (categoriesList.get(position).getSelectedItem()) {
            categoriesList.get(position).setSelectedItem(false);
        } else {
            for (int counter = 0; counter < categoriesList.size(); counter++) {
                if (counter == position)
                    categoriesList.get(position).setSelectedItem(true);
                else
                    categoriesList.get(position).setSelectedItem(false);
            }
        }
        categoriesAdapter.notifyDataSetChanged();
        rvItemsCategories.notify();

    }
}

