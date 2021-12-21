package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.adapter.AdapterRvBundleCategories;
import com.valucart_project.adapter.AdapterRvVerticalProducts;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Categories;
import com.valucart_project.models.Products;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;

public class ProductListingScreen extends FragmentActivity implements OnItemSelection, View.OnClickListener, AdapterView.OnItemSelectedListener , WishItemSelection , SpinnerViewSelection {

    private RecyclerView groceryRecyclerView,rvItemsCategories;
    private AdapterRvVerticalProducts groceryAdapter;
    Library library;
    RelativeLayout rlSortBy;
    private String[] sortBy = {"Select", "price:low-high", "price:high-low", "Latest Products"};
    TextView tvSortBy, tvTitle;
    Spinner spinnerSortBy;
    LinearLayout llFilter, llCart;
    static TextView tvTotalCarts;
    Products productsList = new Products();
    String CallingFrom, categoryId = "";
    static Context activity;
    String sortOrderSelected = "price:latest";
    public static String MinimumPrice = "1";
    public static String MaximumPrice = "1000";
    public static String Type = "";
    public static String departmentId = "";
    public static String SubCategories = "";
    public static String Brand = "";
    public static String Community = "";
    Bundle extras;
    ProgressBar pbBYOB;
    TextView tvNoRecordAvailable;
    public static String categoryName = "";
    private AdapterRvBundleCategories categoriesAdapter;
    Categories categoriesList;
    int currentPageNumber = 0;
    int firstTimeAvoid = 0;
    ImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_screen);
        library = new Library(this);

        currentPageNumber = 0;

        rvItemsCategories = findViewById(R.id.rvItemsCategories);

        tvTitle = findViewById(R.id.tvTitle);

        groceryRecyclerView = findViewById(R.id.rvBunderList);

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);
        tvNoRecordAvailable.setVisibility(View.GONE);

        pbBYOB = findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        ExtraBundleOnStart();
        sortByItem();

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTotalCarts = findViewById(R.id.tvTotalCarts);
        totalCartValue(getApplicationContext(), "" + Constants.totalCart);
        getIntentRecord();
        initField();
    }

    public void ExtraBundleOnStart() {
        makeAllEmgty();
        extras = getIntent().getExtras();
        SubCategories = "";
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            if (extras.getString("MinimumPrice") != null) {
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                departmentId = extras.getString("Categories");
                SubCategories = extras.getString("SubCategories");
                Brand = extras.getString("Brand");
                Community = extras.getString("Community");
            }
        }
    }

    public void makeAllEmgty() {
        MinimumPrice = "1";
        MaximumPrice = "1000";
        Type = "";
        departmentId = "";
        Brand = "";
    }

    public void initField() {
        itemList();
    }

    private void itemList() {

        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);

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
            //totalCartItem("Add");
            //loadCart("add", productsList.getData().get(position).getId(), "product");
            totalCartValue(getApplicationContext(),""+Constants.totalCart);
        } else if (value.equals("AddToCartSubtract")) {
            //totalCartItem("subtract");
            //loadCart("subtract", productsList.getData().get(position).getId(), "product");
            totalCartValue(getApplicationContext(),""+Constants.totalCart);
        } else if (value.equals("categories")) {

            if (categoriesList.getData().get(position).getSelectedItem()) {
                categoriesList.getData().get(position).setSelectedItem(false);
            } else {
                for (int counter = 0; counter < categoriesList.getData().size(); counter++) {
                    if (counter == position)
                        categoriesList.getData().get(counter).setSelectedItem(true);
                    else
                        categoriesList.getData().get(counter).setSelectedItem(false);
                }
            }

            if (categoriesAdapter == null) {
                categoriesAdapter = new AdapterRvBundleCategories(categoriesList, this, this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvItemsCategories.setLayoutManager(horizontalLayoutManager);
                rvItemsCategories.setAdapter(categoriesAdapter);
            } else
                categoriesAdapter.notifyDataSetChanged();

            categoryId = ""+ categoriesList.getData().get(position).getId();
            currentPageNumber = 0;
            loadProducts(""+ categoriesList.getData().get(position).getId(), sortOrderSelected, "", 1);
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
            if (productsList.getData().get(position).getIs_bulk() && productsList.getData().get(position).getBulk_quantity() > 0)
                intent.putExtra("packaging_quantity_unit_Symbal", "" + productsList.getData().get(position).getPackaging_quantity_unit().getSymbol() + "");
            intent.putExtra("thumbnail", "" + productsList.getData().get(position).getThumbnail());
            intent.putExtra("inventory",""+productsList.getData().get(position).getInventory());
            startActivity(intent);

        }

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
            library.bottomsheetSort(this,this);
        } else if (llFilter == v) {
/*            Intent intent = new Intent(this, SearchFilterScreen.class);
            intent.putExtra("CallingFrom", CallingFrom); //Departments
            intent.putExtra("SubCategoryId", SubCategoryId);
            intent.putExtra("Category", Category);

            startActivity(intent);
            finish();*/
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(this);
            //residentNotesDialogFragment.show(getSupportFragmentManager(), "");
            Intent intent = new Intent(this, SearchScreen.class);
            startActivity(intent);
        } else if (llCart == v) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
        }else if (ivSearch == v) {
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(ProductListingScreen.this);
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
            if (position == 1) {
                sortOrderSelected = "price:low-high";
                getIntentRecord();
            } else if (position == 2) {
                sortOrderSelected = "price:high-low";
                getIntentRecord();
            } else {
                sortOrderSelected = "price:latest";
                getIntentRecord();
            }
            tvSortBy.setText("Sort By " + sortBy[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    Products productsItem;

    public void displayApiData(Products response) {
        productsItem = response;

        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (response.getMeta().getCurrent_page() == 1) {

            groceryAdapter = new AdapterRvVerticalProducts(response.getData(), this, "Productlist", this,this);

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
                                if (CallingFrom.equals("FeatureProducts")) {
                                    loadFeatures(extras.getString("subcategory_id"), productsItem.getMeta().getCurrent_page() + 1);
                                    //loadFeatures(extras.getString("subcategory_id"), productsItem.getMeta().getCurrent_page() + 1);
                                } else if (CallingFrom.equals("Departments")) {
                                    if(categoryId.equals(""))
                                        categoryId = extras.getString("SubCategoryId");
                                    loadProducts(categoryId, "", "", productsItem.getMeta().getCurrent_page() + 1);
                                } else if (CallingFrom.equals("OffersProducts")) {
                                    loadOffers( productsItem.getMeta().getCurrent_page() + 1);
                                } else if (CallingFrom.equals("SearchProductslist")) {
                                    loadProducts("", "", "", productsItem.getMeta().getCurrent_page() + 1);
                                }
                                firstTimeAvoid = 0;
                            }
                        } else {
                            firstTimeAvoid = 1;
                        }
                    }

                }
            }
        });

    }


    public void getIntentRecord() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            if (CallingFrom.equals("FeatureProducts")) {
                loadFeatures(extras.getString("subcategory_id"), 1);
                tvTitle.setText("Feature Products");
            } else if (CallingFrom.equals("Departments")) {

                if (!extras.getString("SubCategoryId").equals(""))
                    categoryId = extras.getString("SubCategoryId");

                if (extras.getString("Categories")!=null) {
                    rvItemsCategories.setVisibility(View.VISIBLE);
                    departmentId = extras.getString("Categories");
                    loadDepartmentCategories(departmentId);
                }else
                    loadProducts(extras.getString("SubCategoryId"), sortOrderSelected, "", 1);

                if (extras.getString("DepartmentCategoryName") == null) {
                    tvTitle.setText("" + categoryName.toUpperCase());
                } else {
                    categoryName = extras.getString("DepartmentCategoryName");
                    tvTitle.setText("" + extras.getString("DepartmentCategoryName").toUpperCase());
                }

            } else if (CallingFrom.equals("OffersProducts")) {

                tvTitle.setText("Offers");
                loadOffers(1);

                LinearLayout llSortFilter = findViewById(R.id.llSortFilter);
                llSortFilter.setVisibility(View.VISIBLE);

            } else if (CallingFrom.equals("SearchProductslist")) {
                loadProducts("", sortOrderSelected, "", 1);
            }

        } else {
            loadProducts("", sortOrderSelected, "", 1);
        }

    }

    int currentPage;

    private void loadFeatures(String id, int page) {
        currentPage = page;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        APIManager.getInstance().getFeatures(id, sortOrderSelected, MinimumPrice + "-" + MaximumPrice, Community, departmentId, Brand, page, new APIResponseCallback<Products>() {

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

                if (response.getData().size() != 0) {
                    tvNoRecordAvailable.setVisibility(View.GONE);
                    groceryRecyclerView.setVisibility(View.VISIBLE);
                    displayApiData(response);
                }else {
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                    groceryRecyclerView.setVisibility(View.GONE);
                }
                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                tvNoRecordAvailable.setVisibility(View.VISIBLE);
                groceryRecyclerView.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    private void loadProducts(String categoryId, String sort, String price, int page) {
        //dNAQJe36bKRa
        currentPage = page;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        APIManager.getInstance().getProducts(categoryId, sortOrderSelected, MinimumPrice + "-" + MaximumPrice, Community, SubCategories, Brand, departmentId, page, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                library.hideLoading();
                //productsList = response;
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        productsList.getData().add(response.getData().get(counter));
                    }
                } else {
                    productsList = response;
                }
                if (response.getData().size() != 0) {
                    tvNoRecordAvailable.setVisibility(View.GONE);
                    groceryRecyclerView.setVisibility(View.VISIBLE);
                    displayApiData(response);
                }else {
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                    groceryRecyclerView.setVisibility(View.GONE);
                }
                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                tvNoRecordAvailable.setVisibility(View.VISIBLE);
                groceryRecyclerView.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void loadOffers( int page) {
        currentPage = page;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        APIManager.getInstance().getOffers("", sortOrderSelected, MinimumPrice + "-" + MaximumPrice, Community, departmentId, Brand, page, new APIResponseCallback<Products>() {

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
                if (response.getData().size() != 0) {
                    tvNoRecordAvailable.setVisibility(View.GONE);
                    groceryRecyclerView.setVisibility(View.VISIBLE);
                    displayApiData(response);
                }else {
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                    groceryRecyclerView.setVisibility(View.GONE);
                }
                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                tvNoRecordAvailable.setVisibility(View.VISIBLE);
                groceryRecyclerView.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                }catch (Exception e){}

            }

        });

    }

    public static void totalCartValue(Context context, String value) {
        if (value.equals("0"))
            tvTotalCarts.setVisibility(View.GONE);
        else
            tvTotalCarts.setVisibility(View.VISIBLE);

        tvTotalCarts.setText("" + value);
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

    private void categoriesItem() {
        for (int counter = 0; counter < categoriesList.getData().size(); counter++)
        {
            if(categoryId.equals(categoriesList.getData().get(counter).getId()))
                categoriesList.getData().get(counter).setSelectedItem(true);
        }
        currentPageNumber = 0;
        if(categoryId.equals(""))
            categoriesList.getData().get(0).setSelectedItem(true);

        categoriesAdapter = new AdapterRvBundleCategories(categoriesList, this, this,"categories");
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(categoriesAdapter);
    }

    private void loadDepartmentCategories(String departmentId) {

        APIManager.getInstance().getCategories(departmentId ,new APIResponseCallback<Categories>() {

            @Override
            public void onResponseLoaded(@NonNull Categories response) {
                categoriesList = response;
                if(response.getData().size()>0) {
                    if(categoryId.equals(""))
                        categoryId = response.getData().get(0).getId();
                    categoriesItem();

                    loadProducts(categoryId, sortOrderSelected, "", 1);
                }else
                    Toast.makeText(getApplicationContext(),"No Category available",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
            }

        });

    }

}

