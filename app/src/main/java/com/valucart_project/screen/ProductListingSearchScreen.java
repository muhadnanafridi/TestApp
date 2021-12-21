package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.adapter.AdapterRvVerticalProducts;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListingSearchScreen extends FragmentActivity implements OnItemSelection, View.OnClickListener, AdapterView.OnItemSelectedListener, WishItemSelection, SpinnerViewSelection {

    private RecyclerView groceryRecyclerView;
    private AdapterRvVerticalProducts groceryAdapter;
    Library library;
    RelativeLayout rlSortBy;
    private String[] sortBy = {"Select", "price:low-high", "price:high-low", "Latest Products"};
    TextView tvSortBy;
    Spinner spinnerSortBy;
    LinearLayout llFilter, llCart;
    static TextView tvTotalCarts;
    Products productsList;
    String CallingFrom;
    String sortOrderSelected = "price:latest";
    ProgressBar pbBYOB;
    TextView tvNoRecordAvailable;
    ImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_screen);
        library = new Library(this);
        currentPageNumber = 0;

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);
        tvNoRecordAvailable.setVisibility(View.GONE);

        sortByItem();

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTotalCarts = findViewById(R.id.tvTotalCarts);
        totalCartItem("makeItInvisable");
        getIntentRecord();
        initField();
    }


    public void initField() {
        itemList();
    }

    private void itemList() {

        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);

        pbBYOB = findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        llCart = findViewById(R.id.llCart);
        llCart.setOnClickListener(this);

        llFilter = findViewById(R.id.llFilter);
        llFilter.setOnClickListener(this);


        rlSortBy = findViewById(R.id.rlSortBy);
        rlSortBy.setOnClickListener(this);

    }


    @Override
    public void onItemSelected(String value, int position) {

        if (value.equals("AddToCartAdd")) {
            totalCartItem("Add");
            //loadCart("add", productsList.getData().get(position).getId(), "product");
        } else if (value.equals("AddToCartSubtract")) {
            totalCartItem("subtract");
            //loadCart("subtract", productsList.getData().get(position).getId(), "product");
        } else {
            Intent intent = new Intent(getApplicationContext(), ProductDetailScreen.class);
            intent.putExtra("CallingFrom", "Products");
            intent.putExtra("id", "" + productsList.getData().get(position).getId());
            intent.putExtra("name", "" + productsList.getData().get(position).getName());
            intent.putExtra("subcategory_id", "" + productsList.getData().get(position).getCategory().getId());
            intent.putExtra("description", "" + productsList.getData().get(position).getDescription());
            intent.putExtra("packaging_quantity", "" + productsList.getData().get(position).getPackaging_quantity());
            intent.putExtra("valucart_price", "" + productsList.getData().get(position).getValucart_price());
            intent.putExtra("maximum_selling_price", "" + productsList.getData().get(position).getMaximum_selling_price());
            intent.putStringArrayListExtra("ImagesList", productsList.getData().get(position).getImages());
            intent.putExtra("packaging_quantity_unit_Symbal", "" + productsList.getData().get(position).getPackaging_quantity_unit().getSymbol() + "");
            intent.putExtra("thumbnail", "" + productsList.getData().get(position).getThumbnail());
            intent.putExtra("inventory", "" + productsList.getData().get(position).getInventory());
            startActivity(intent);
        }

    }

    private void loadCart(String action, String item_id, String item_type) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addCart(action, item_id, item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    if (response.getAsJsonObject().get("status").toString().equals("1")) {

                        if (Constants.cart_id.equals(""))
                            Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();
                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                        displayTotalCart(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                    } else {
                        Toast.makeText(getApplicationContext(), response.getAsJsonObject().get("message").toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }

            }

        });

    }

    public static void displayTotalCart(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        editor.apply();
    }

    @Override
    public void onClick(View v) {

        if (rlSortBy == v) {
            //spinnerSortBy.performClick();
            library.bottomsheetSort(this, this);
        }
        else if (llFilter == v) {
/*          Intent intent = new Intent(this, SearchFilterScreen.class);
            intent.putExtra("CallingFrom", CallingFrom); //Departments
            intent.putExtra("SubCategoryId", SubCategoryId);
            startActivity(intent);
            finish();*/
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(this);
            //residentNotesDialogFragment.show(getSupportFragmentManager(), "");
            Intent intent = new Intent(this, SearchScreen.class);
            startActivity(intent);
        }
        else if (llCart == v) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
        }
        else if (ivSearch == v) {
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(ProductListingSearchScreen.this);
            //residentNotesDialogFragment.show(getSupportFragmentManager(), "");
            Intent intent = new Intent(this, SearchScreen.class);
            startActivity(intent);
        }

    }

    public void sortByItem() {
        tvSortBy = (TextView) findViewById(R.id.tvSortBy);

        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
        AdapterForSpinner aa = new AdapterForSpinner(getApplicationContext(), R.layout.simple_spinner_item, sortBy);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSortBy.setAdapter(aa);
        spinnerSortBy.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            //sortOrderSelected
            if (position == 1)
                sortOrderSelected = "price:low-high";
            else if (position == 2)
                sortOrderSelected = "price:high-low";
            else sortOrderSelected = "price:latest";

            tvSortBy.setText("Sort By " + sortBy[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void totalCartItem(String value1) {
        try {

            int value;
            if (value1.equals("makeItInvisable")) {
                tvTotalCarts.setVisibility(View.GONE);
                tvTotalCarts.setText("0");
            } else if (value1.equals("subtract")) {
                value = Integer.parseInt(tvTotalCarts.getText().toString()) - 1;
                if (value == 0) {
                    tvTotalCarts.setVisibility(View.GONE);
                    tvTotalCarts.setText("0");
                } else {
                    tvTotalCarts.setVisibility(View.VISIBLE);
                    tvTotalCarts.setText("" + value);
                }
            } else {
                value = Integer.parseInt(tvTotalCarts.getText().toString()) + 1;
                tvTotalCarts.setVisibility(View.VISIBLE);
                tvTotalCarts.setText("" + value);
            }

        } catch (Exception e) {
        }
    }

    int firstTimeAvoid = 0;
    Products productsItem;

    public void displayApiData(Products response) {
        productsItem = response;
        groceryRecyclerView = findViewById(R.id.rvBunderList);
        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (response.getMeta().getCurrent_page() == 1) {
            groceryAdapter = new AdapterRvVerticalProducts(response.getData(), this, "Productlist", this, this);

            if (library.IsTablet())
                groceryRecyclerView.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 4 : 2));
            else
                groceryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            groceryRecyclerView.setAdapter(groceryAdapter);
        } else {
            groceryAdapter.notifyDataSetChanged();
        }
        firstTimeAvoid = 0;
        groceryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!groceryRecyclerView.canScrollVertically(1)) {

                    if (productsItem.getMeta().getCurrent_page() < productsItem.getMeta().getLast_page()) {
                        if (firstTimeAvoid == 1) {
                            if (productsItem.getMeta().getCurrent_page() > currentPageNumber) {
                                currentPageNumber = productsItem.getMeta().getCurrent_page();
                                loadProducts(href, sortOrderSelected, "", productsItem.getMeta().getCurrent_page() + 1);
                            }
                            firstTimeAvoid = 0;
                        }else {
                            firstTimeAvoid = 1;
                        }
                    }

                }
            }
        });

    }

    int currentPageNumber = 0;
    String href;

    public void getIntentRecord() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            href = extras.getString("href");
            loadProducts(extras.getString("href"), sortOrderSelected, "", 1);
        }
    }

    int currentPage;

    private void loadProducts(String href, String sort, String price, int page) {
        currentPage = page;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        //sorting price
        href = href + "&sorting=" + sort + "&price=" + price + "&page=" + currentPage;
        APIManager.getInstance().getProductList(href, sort, price, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                library.hideLoading();
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        productsList.getData().add(response.getData().get(counter));
                    }
                } else {
                    productsList = response;
                }
                //productsList.getData().add() = response;
                if (response.getData().size() != 0)
                    displayApiData(response);
                else
                   tvNoRecordAvailable.setVisibility(View.VISIBLE);
                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                    library.alertErrorMessage("" + jsonObject.get("message").toString());

//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                }

            }

        });
    }

    @Override
    public void onWishValueSelected(String value, int position, String action) {
        library.apiLoadItemToWishlist(action, productsList.getData().get(position).getId(), "product");
    }

    @Override
    public void onValueSelected(String value, int position) {
        sortOrderSelected = value;
        getIntentRecord();
        tvSortBy.setText("Sort By " + sortBy[position]);
    }
}

