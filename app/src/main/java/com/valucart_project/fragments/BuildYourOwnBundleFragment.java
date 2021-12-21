package com.valucart_project.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.adapter.AdapterRvBYOBApproval;
import com.valucart_project.adapter.AdapterRvBYOBVerticalList;
import com.valucart_project.adapter.AdapterRvHorizantalSearchRelatedCategories;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnAddBYOBToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.screen.BundleSummaryScreen;
import com.valucart_project.screen.LoginScreen;
import com.valucart_project.screen.SearchFilterScreen;
import com.valucart_project.screen.SearchScreen;
import com.valucart_project.screen.SuperSavorZoneScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class BuildYourOwnBundleFragment extends Fragment implements OnItemSelection, AdapterView.OnItemSelectedListener, WishItemSelection, OnAddBYOBToCartSelection, SpinnerViewSelection {

    private List<Grocery> categoriesList = new ArrayList<>();
    static Products productsList = new Products();
    private RecyclerView rvBYOB, rvItemsCategories, rvSelectedProduct;
    private AdapterRvBYOBVerticalList groceryAdapter;
    Library library;
    private AdapterRvHorizantalSearchRelatedCategories categoriesAdapter;
    static View view;
    View viewbottomSheetDialog;
    LinearLayout llSearch, llFilter;
    RelativeLayout rlSortBy;
    ImageView ivAdvanceSearch;
    Spinner spinnerSortBy;
    private String[] sortBy = {"Select", "price:low-high", "price:high-low", "Latest Products"};
    TextView tvSortBy;
    static TextView tvtotalByobPrice;
    BottomSheetDialog bottomSheetDialog;
    static TextView tvTotalCarts;
    private static TextView tvPrice;
    private static ProgressBar pbPrice;
    private static int progressStatus = 0;
    private static Handler handler = new Handler();
    static double d = 0.0;
    static float aFloat = (float) d;
    static Float pricePurchase = (float) d;
    String CategoryId = "";
    String ProductId = "";
    RelativeLayout rlNextDescription, rlDescription, rlItemListing;
    AdapterRvBYOBApproval adapterRvBYOBApproval;
    String MinimumPrice = "1";
    String MaximumPrice = "1000";
    String Type = "";
    String Category = "";
    String Brand = "";
    static String Community = "";
    String sortOrderSelected = "price:latest";
    ProgressBar pbBYOB;
    int currentPage;
    ByobSummary productsListSummary = new ByobSummary();
    Products productsItem;
    int firstTimeAvoid = 0;
    int currentPageNumber = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.build_your_own_bundle_listing_fragment, container, false);

        library = new Library(getContext());
        initItem();
        sortByItem();
        extraBundle();

        loadProducts(1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filterHandler();
    }

    public void initItem() {
        currentPageNumber = 0;
        productsList = new Products();
        tvPrice = view.findViewById(R.id.tvPrice);
        SuperSavorZoneScreen.ivSearch.setVisibility(View.GONE);

        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        tvtotalByobPrice = view.findViewById(R.id.tvtotalByobPrice);

        pricePurchase = (float) d;
        progressStatus = 0;

        pbPrice = view.findViewById(R.id.pbPrice);
        pbPrice.setProgress(0);

        llFilter = view.findViewById(R.id.llFilter);
        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.productsListByob = productsList;
                Intent intent = new Intent(getContext(), SearchFilterScreen.class);
                intent.putExtra("CallingFrom", "Byob");
                startActivity(intent);
                getActivity().finish();
            }
        });

        rlSortBy = view.findViewById(R.id.rlSortBy);
        rlSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBottomSheetSortBy();
            }
        });

        rlDescription = view.findViewById(R.id.rlDescription);
        rlItemListing = view.findViewById(R.id.rlItemListing);
        rlNextDescription = view.findViewById(R.id.rlNextDescription);
        rlNextDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDescription.setVisibility(View.GONE);
                rlItemListing.setVisibility(View.VISIBLE);
                Constants.BYOBFirstTimeViewScreen = false;
            }
        });

        tvTotalCarts = view.findViewById(R.id.tvTotalCarts);

        categoriesItem();

        RelativeLayout rlNext = view.findViewById(R.id.rlNext);
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productsList.getData().size() > 0) {
                    if (!tvtotalByobPrice.getText().toString().equals("AED 0")) {
                        if (Constants.userLogin) {
                            Intent intent = new Intent(getContext(), BundleSummaryScreen.class);
                            intent.putExtra("byobId", Constants.bundleId);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), LoginScreen.class);
                            intent.putExtra("CallFrom", "BuildYourOwnBundle");
                            startActivity(intent);
                        }
                    }
                } else
                    Toast.makeText(getContext(), "Add Product to BYOB", Toast.LENGTH_LONG).show();
            }
        });

        RelativeLayout rlShow = view.findViewById(R.id.rlShow);
        rlShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvtotalByobPrice.getText().toString().equals("AED 0"))
                    showBottomSheetSelectedItem();
            }
        });

        if (!Constants.BYOBFirstTimeViewScreen) {
            rlDescription.setVisibility(View.GONE);
            rlItemListing.setVisibility(View.VISIBLE);
        }

    }

    public void callBottomSheetSortBy() {
        library.bottomsheetSort(getActivity(), this);
    }

    public void filterHandler() {
        if (SuperSavorZoneScreen.anyDataAvailableToShow && SuperSavorZoneScreen.CallingFrom.equals("ByobSearch")) {

            MinimumPrice = SuperSavorZoneScreen.MinimumPrice;
            MaximumPrice = SuperSavorZoneScreen.MaximumPrice;
            Type = SuperSavorZoneScreen.Type;
            Category = SuperSavorZoneScreen.Category;
            Brand = SuperSavorZoneScreen.Brand;
            Community = SuperSavorZoneScreen.Community;
            productsListSummary = Constants.byobSummaryList;
            if (productsListSummary.getData().getProducts().size() > 0) {
                tvTotalCarts.setVisibility(View.VISIBLE);

                tvTotalCarts.setText("" + productsListSummary.getData().getProducts().size());
                tvPrice.setText("AED  " + new DecimalFormat("##.##").format(productsListSummary.getData().getMaximum_selling_price()));
                tvtotalByobPrice.setText("AED  " + new DecimalFormat("##.##").format(productsListSummary.getData().getMaximum_selling_price()));
                pbPrice.setProgress((int) Math.round(productsListSummary.getData().getMaximum_selling_price()));
            } else {
                setTvTotalCarts("makeItInvisable", 0);
            }

        } else if (!Constants.bundleId.equals("")) {
            loadBYOB();
        } else {
            productsListSummary = new ByobSummary();
            Constants.byobSummaryList = new ByobSummary();
            Constants.bundleId = "";
            setTvTotalCarts("makeItInvisable", 0);
        }
    }


    private void loadBYOB() {
        //{"created_at":"2019-04-23 06:58:31","id":3,"price":2.05,"discount":0,"discounted_price":2.05,"products":[{"quantity":1,"product":{"id":"wgQXJxmon3Rq","sku":"6291101131265","name":"alokozay detergent premium 110g","description":"detergent premium 110 grm","packaging_quantity":110,"maximum_selling_price":2.05,"valucart_price":2.05,"images":["http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7189 copy.jpg","http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7191 copy.jpg"],"packaging_quantity_unit":{"id":"wWx50jA6Y7gA","name":"G","symbol":"G"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/6291101131265/thumb_3D0A7190 copy.jpg"}}]}
        APIManager.getInstance().getBYOB(Constants.bundleId, new APIResponseCallback<ByobSummary>() {

            @Override
            public void onResponseLoaded(@NonNull ByobSummary response) {
                try {
                    productsListSummary = new ByobSummary();
                    productsListSummary = response;
                    Constants.bundleId = "" + response.getData().getId();
                    Constants.byobSummaryList = response;

                    if (adapterRvBYOBApproval != null) {
                        updateBottomSheetAdopter("");
                    }
                    if (productsListSummary.getData().getProducts().size() > 0) {
                        tvTotalCarts.setVisibility(View.VISIBLE);
                        tvTotalCarts.setText("" + productsListSummary.getData().getProducts().size());
                    } else {
                        tvTotalCarts.setVisibility(View.GONE);
                    }
                    progressPrice("add", productsListSummary.getData().getMaximum_selling_price());
                    displayTotalCart(getContext());
                    // pbPrice.setProgress((int)  Math.round(productsListSummary.getData().getDiscounted_price()));
                } catch (Exception e) {}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                }catch (Exception e){}

            }

        });

    }


    private void categoriesItem() {
        rvItemsCategories = view.findViewById(R.id.rvItemsCategories);
        //rvFeaturedItems.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        categoriesAdapter = new AdapterRvHorizantalSearchRelatedCategories(categoriesList, getContext(), this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(categoriesAdapter);
        populateCategoriesList();
    }

    private void populateCategoriesList() {
        categoriesList.add(new Grocery("Nuts"));
        categoriesList.add(new Grocery("Pasta"));
        categoriesList.add(new Grocery("Breakfast Items"));
        categoriesList.add(new Grocery("Beveragea"));
        categoriesList.add(new Grocery("Biscuits and Cakes"));
        categoriesList.add(new Grocery("Olives and Pickles"));
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(String value, int position) {
        //selectedGroceryList.add( new Grocery("Breakfast Bundle", R.drawable.ic_product));
        if (value.equals("AddToCartAdd")) {
            productsList.getData().get(position).setAddToByob(productsList.getData().get(position).getAddToByob() + 1);
            loadByob("add", "" + productsList.getData().get(position).getId(), Constants.bundleId);
        } else if (value.equals("AddToCartSubtract")) {
            productsList.getData().get(position).setAddToByob(productsList.getData().get(position).getAddToByob() - 1);
            loadByob("subtract", "" + productsList.getData().get(position).getId(), Constants.bundleId);
        } else if (value.equals("Delete")) {
            // tvTotalCarts.setText("" + (Integer.valueOf(tvTotalCarts.getText().toString()) - 1));
            loadByob("remove", "" + productsList.getData().get(position).getId(), Constants.bundleId);
        } else if (value.equals("RemoveOne")) {
            // tvTotalCarts.setText("" + (Integer.valueOf(tvTotalCarts.getText().toString()) - 1));
            if (productsListSummary.getData().getProducts().get(position).getQuantity() > 1)
                loadByob("subtract", "" + productsListSummary.getData().getProducts().get(position).getProduct().getId(), Constants.bundleId);
            else
                loadByob("remove", "" + productsListSummary.getData().getProducts().get(position).getProduct().getId(), Constants.bundleId);
        } else if (value.equals("AddOne")) {
            //  tvTotalCarts.setText("" + (Integer.valueOf(tvTotalCarts.getText().toString()) + 1));
            loadByob("add", "" + productsListSummary.getData().getProducts().get(position).getProduct().getId(), Constants.bundleId);
        }
    }


    public static void setTvTotalCarts(String value1, final int position) {
        try {
            int value;
            if (value1.equals("makeItInvisable")) {
                tvTotalCarts.setVisibility(View.GONE);
                progressStatus = 0;
                pbPrice.setProgress(0);
                tvPrice.setText("AED  " + 0);

            } else if (value1.equals("subtract")) {
                tvTotalCarts.setVisibility(View.VISIBLE);
            } else {

            }

        } catch (Exception e) {
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            //sortOrderSelected
            if (position == 1) {
                sortOrderSelected = "price:low-high";
                loadProducts(1);
            } else if (position == 2) {
                sortOrderSelected = "price:high-low";
                loadProducts(1);
            } else {
                sortOrderSelected = "price:latest";
                loadProducts(1);
            }
            tvSortBy.setText("Sort By " + sortBy[position]);
        }
    }

    public void sortByItem() {
        tvSortBy = (TextView) view.findViewById(R.id.tvSortBy);

        spinnerSortBy = (Spinner) view.findViewById(R.id.spinnerSortBy);
        AdapterForSpinner aa = new AdapterForSpinner(getContext(), R.layout.simple_spinner_item, sortBy);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSortBy.setAdapter(aa);
        spinnerSortBy.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static void progressPrice(final String checkAddORSub, Float pricePurchase1) {

        //if (checkAddORSub.equals("add")) {
        //    pricePurchase = pricePurchase1;
        //} else {
            pricePurchase = pricePurchase1;
        //}

        //Add  subtract
        if (progressStatus > pricePurchase) {
            progressStatus = 0;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < pricePurchase) {
                    // Update the progress status
                    progressStatus += 1;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        if (checkAddORSub.equals("Add"))
                            Thread.sleep(30);  //3 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pbPrice.setProgress(progressStatus);
                            // Show the progress on TextView
                            tvPrice.setText("AED  " + new DecimalFormat("##.##").format(pricePurchase));
                            tvtotalByobPrice.setText("AED  " + new DecimalFormat("##.##").format(pricePurchase));
                        }
                    });
                }
            }
        }).start(); // Start the operation

        if (pricePurchase == 0) {
            pbPrice.setProgress(0);
            tvPrice.setText("AED  " + 0);
            tvtotalByobPrice.setText("AED  " + 0);
        }

    }


    public void extraBundle() {

        if (SuperSavorZoneScreen.anyDataAvailableToShow) {
            CategoryId = SuperSavorZoneScreen.CategoryId;
            ProductId = SuperSavorZoneScreen.ProductId;
            rlDescription.setVisibility(View.GONE);
            rlItemListing.setVisibility(View.VISIBLE);
        }

    }

    public static void removeItem() {
        progressPrice("subtract", aFloat);
    }

    private void loadProducts(int page) {
        currentPage = page;
        if (currentPage > 1) {
            pbBYOB.setVisibility(View.VISIBLE);
            // if(footerView!=null)
            //  footerView.setVisibility(View.VISIBLE);
        } else {
            library.showLoading();
        }
        //library.showLoading();
        String minmaxPrice;
        if (MinimumPrice.equals("1") && MaximumPrice.equals("1000"))
            minmaxPrice = "";
        else
            minmaxPrice = MinimumPrice + "-" + MaximumPrice;
//getProductsByob
        APIManager.getInstance().getProductsMondayMeat("", sortOrderSelected, minmaxPrice, Community, Category, Brand, page, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                library.hideLoading();
                // if(footerView!=null)
                // footerView.setVisibility(View.GONE);
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        productsList.getData().add(response.getData().get(counter));
                    }
                } else {
                    productsList = response;
                }
                if (response.getData().size() != 0)
                    itemList(response);
                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                // if(footerView!=null)
                // footerView.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }


    private void itemList(Products products) {
        productsItem = products;
        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SearchDialogFragment searchDialogFragment = new SearchDialogFragment(getContext());
                //searchDialogFragment.show(getFragmentManager(), "");
                Intent intent = new Intent(getContext(), SearchScreen.class);
                startActivity(intent);
            }
        });

        ivAdvanceSearch = view.findViewById(R.id.ivAdvanceSearch);
        ivAdvanceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchFilterScreen.class));
            }
        });

        rvBYOB = view.findViewById(R.id.rvBYOB);
        // add a divider after each item for more clarity
        //rvBYOB.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        GridLayoutManager gridLayoutManager = null;
        if (products.getMeta().getCurrent_page() == 1) {
            groceryAdapter = new AdapterRvBYOBVerticalList(products.getData(), getContext(), this, this);

            if (library.IsTablet()) {
                gridLayoutManager = new GridLayoutManager(getContext(), library.getScreenOrientation(getContext()));
                rvBYOB.setLayoutManager(gridLayoutManager);
            } else {
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                rvBYOB.setLayoutManager(gridLayoutManager);
            }

            rvBYOB.setAdapter(groceryAdapter);
        } else {
            groceryAdapter.notifyDataSetChanged();
        }

        firstTimeAvoid = 0;

        rvBYOB.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rvBYOB.canScrollVertically(1)) {
                    if (productsItem.getMeta().getCurrent_page() < productsItem.getMeta().getLast_page()) {
                        if (firstTimeAvoid == 1) {
                            if (productsItem.getMeta().getCurrent_page() > currentPageNumber) {
                                loadProducts(productsItem.getMeta().getCurrent_page() + 1);
                                currentPageNumber = productsItem.getMeta().getCurrent_page();
                            }
                            firstTimeAvoid = 0;
                        } else {
                            firstTimeAvoid = 1;
                        }
                    }

                }
            }
        });

    }


    public JsonObject arrayItemSeleteded() {
        //[{"product_id":"wgQXJxmon3Rq","quantity":1},{"product_id":"dNAQJeW36bKR","quantity":1},{"product_id":"axBNJM1JEjOb","quantity":1}]
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        JsonObject finalobject = new JsonObject();
        try {
            //productsList
            for (int counter = 0; counter < productsList.getData().size(); counter++) {
                if (productsList.getData().get(counter).getAddToByob() > 0) {
                    obj = new JSONObject();
                    obj.put("product_id", productsList.getData().get(counter).getId()).put("quantity", productsList.getData().get(counter).getAddToByob());
                    jsonArray.put(obj);
                }
            }
            Gson gson = new Gson();

            finalobject.add("product_list", gson.fromJson(jsonArray.toString(), JsonElement.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return finalobject;
    }

    private void addBYOBToCard() {

        //{"created_at":"2019-04-23 06:58:31","id":3,"price":2.05,"discount":0,"discounted_price":2.05,"products":[{"quantity":1,"product":{"id":"wgQXJxmon3Rq","sku":"6291101131265","name":"alokozay detergent premium 110g","description":"detergent premium 110 grm","packaging_quantity":110,"maximum_selling_price":2.05,"valucart_price":2.05,"images":["http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7189 copy.jpg","http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7191 copy.jpg"],"packaging_quantity_unit":{"id":"wWx50jA6Y7gA","name":"G","symbol":"G"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/6291101131265/thumb_3D0A7190 copy.jpg"}}]}
        if (arrayItemSeleteded().toString().equals("{\"products\":[]}")) {
            Toast.makeText(getContext(), "Add Product to BYOB", Toast.LENGTH_LONG).show();
            return;
        }
        library.showLoading();
        APIManager.getInstance().addBYOBToCard(arrayItemSeleteded(), new APIResponseCallback<JsonElement>() {
            //{"status":1,"data":{"name":"Bundle","created_at":"2019-04-29 00:42:25","id":61,"price":2,"discount":0,"discounted_price":2,"products":[{"quantity":1,"product":{"id":"Wx50jAmO6Y7g","sku":"8850124037350","name":"nescafe coffee instant my cup  in  sticks ns  24x20grm","description":"coffee instant my cup  in  sticks ns ","packaging_quantity":20,"maximum_selling_price":1,"valucart_price":1,"images":["http://testing.v2.api.valucart.com/img/products/8850124037350/Aa10219 (1859 of 2903).jpg"],"packaging_quantity_unit":{"id":"e2g90gLBJYOP","name":"GRM","symbol":"GRM"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/8850124037350/thumb_Aa10219 (1858 of 2903).jpg"}},{"quantity":1,"product":{"id":"D7v04DdL05lP","sku":"6294003571788","name":"nescafe cofee in hazelnut  xa\n 20x17gm","description":"cofee in hazelnut  xa\n","packaging_quantity":17,"maximum_selling_price":1,"valucart_price":1,"images":["http://testing.v2.api.valucart.com/img/products/6294003571788/Aa10219 (2689 of 2903).jpg"],"packaging_quantity_unit":{"id":"RD7v04yLJ5lP","name":"GM","symbol":"GM"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/6294003571788/thumb_Aa10219 (2688 of 2903).jpg"}}]}}
            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                if (response != null) {
                    if (((JsonObject) response).get("data").getAsJsonObject().get("id").toString() != null) {
                        String id = ((JsonObject) response).get("data").getAsJsonObject().get("id").toString();
                        Constants.bundleId = id;
                        Intent intent = new Intent(getContext(), BundleSummaryScreen.class);
                        intent.putExtra("byobId", id);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                }catch (Exception e){}

            }

        });

    }

    private void loadByob(final String action, String product_id, String bundle_Id) {
        library.showLoading();
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addByobSummary(action, product_id, bundle_Id, new APIResponseCallback<ByobSummary>() {
            @Override
            public void onResponseLoaded(@NonNull ByobSummary response) {
                try {
                    library.hideLoading();
                    productsListSummary = new ByobSummary();
                    productsListSummary = response;
                    Constants.bundleId = "" + response.getData().getId();
                    Constants.byobSummaryList = response;

                    if (adapterRvBYOBApproval != null) {
                        updateBottomSheetAdopter(action);
                    }
                    if (productsListSummary.getData().getProducts().size() > 0) {
                        tvTotalCarts.setVisibility(View.VISIBLE);
                        tvTotalCarts.setText("" + productsListSummary.getData().getProducts().size());
                    } else {
                        tvTotalCarts.setVisibility(View.GONE);
                    }
                    progressPrice(action, productsListSummary.getData().getMaximum_selling_price());
                    displayTotalCart(getContext());
                    // pbPrice.setProgress((int)  Math.round(productsListSummary.getData().getDiscounted_price()));
                } catch (Exception e) {}
            }
            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                }catch (Exception e){}

            }

        });
    }

    public void updateBottomSheetAdopter(String action) {
            adapterRvBYOBApproval = new AdapterRvBYOBApproval(productsListSummary.getData(), getContext(), this);
            rvSelectedProduct.setAdapter(adapterRvBYOBApproval);
    }

    public void showBottomSheetSelectedItem() {

        viewbottomSheetDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_build_your_own_bundle, null);

        rvSelectedProduct = viewbottomSheetDialog.findViewById(R.id.rvSelectedProduct);

        adapterRvBYOBApproval = new AdapterRvBYOBApproval(productsListSummary.getData(), getContext(), this);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        if (library.IsTablet())
            rvSelectedProduct.setLayoutManager(horizontalLayoutManager);
        else
            rvSelectedProduct.setLayoutManager(horizontalLayoutManager);

        rvSelectedProduct.setAdapter(adapterRvBYOBApproval);
        adapterRvBYOBApproval.notifyDataSetChanged();

        RelativeLayout rlAddMore = viewbottomSheetDialog.findViewById(R.id.rlAddMore);
        rlAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        RelativeLayout rlNext = viewbottomSheetDialog.findViewById(R.id.rlNext);
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (productsList.getData().size() > 0) {
                    Intent intent = new Intent(getContext(), BundleSummaryScreen.class);
                    intent.putExtra("byobId", Constants.bundleId);
                    startActivity(intent);
                } else
                    Toast.makeText(getContext(), "Add Product to BYOB", Toast.LENGTH_LONG).show();
            }
        });

        ImageView ivCancel = viewbottomSheetDialog.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //progressPrice("Add",1);
                //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
            }
        });
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);

        bottomSheetDialog.setContentView(viewbottomSheetDialog);
        bottomSheetDialog.show();


        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

    }


    public static void displayTotalCart(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("bundle_id", "" + Constants.bundleId);
        editor.apply();
    }

    @Override
    public void onWishValueSelected(String value, int position, String action) {
        library.apiLoadItemToWishlist(action, productsList.getData().get(position).getId(), "product");
    }

    @Override
    public void onAddToCart(ByobSummary response, String action) {
        try {
            productsListSummary = new ByobSummary();
            productsListSummary = response;
            Constants.bundleId = "" + response.getData().getId();
            Constants.byobSummaryList = response;

            if (adapterRvBYOBApproval != null) {
                updateBottomSheetAdopter(action);
            }
            if (productsListSummary.getData().getProducts().size() > 0) {
                tvTotalCarts.setVisibility(View.VISIBLE);
                tvTotalCarts.setText("" + productsListSummary.getData().getProducts().size());
            } else {
                tvTotalCarts.setVisibility(View.GONE);
            }
            progressPrice(action, productsListSummary.getData().getMaximum_selling_price());
            displayTotalCart(getContext());
        } catch (Exception e) {}
    }

    @Override
    public void onValueSelected(String value, int position) {
        sortOrderSelected = value;
        currentPageNumber = 0;
        loadProducts(1);
        tvSortBy.setText("Sort By " + sortBy[position]);
    }

}

