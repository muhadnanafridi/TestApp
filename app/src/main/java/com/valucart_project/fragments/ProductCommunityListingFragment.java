package com.valucart_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.adapter.AdapterRvPopolarDepartment;
import com.valucart_project.adapter.AdapterRvVerticalProducts;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
import com.valucart_project.models.SubCategory;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.screen.ProductDetailScreen;
import com.valucart_project.screen.SearchFilterScreen;
import com.valucart_project.screen.SearchScreen;
import com.valucart_project.screen.ShopByCommunityScreen;
import com.valucart_project.screen.SuperSavorZoneScreen;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductCommunityListingFragment extends Fragment implements OnItemSelection, View.OnClickListener, AdapterView.OnItemSelectedListener , WishItemSelection {

    private RecyclerView groceryRecyclerView,rvShopByCommunityCategories;
    private AdapterRvVerticalProducts groceryAdapter;
    Library library;
    private AdapterRvPopolarDepartment categoriesAdapter;
    View view;
    LinearLayout llSearch, llFilter;
    RelativeLayout rlSortBy;
    ImageView ivAdvanceSearch;
    Spinner spinnerSortBy;
    private String[] sortBy = {"Select", "price:low-high", "price:high-low", "Latest Products"};
    TextView tvSortBy;
    Products productsList;
    String MinimumPrice = "1";
    String MaximumPrice = "1000";
    String Type = "";
    String Category = "";
    String Brand = "";
    public static String Community = "";
    String sortOrderSelected = "price:latest";
    ProgressBar pbBYOB;
    int firstTimeAvoid = 0;
    int currentPageNumber =0;
    private List<Grocery> communitiesList = new ArrayList<>();
    String shopBycommunitySelected = "Emirati", shopBycommunityId = "";
    static SubCategory communityList = new SubCategory();

    public static ProductCommunityListingFragment newInstance(String title) {
        ProductCommunityListingFragment fragment = new ProductCommunityListingFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_community_listing_fragment, container, false);
        library = new Library(getContext());

        initItem();
        filterHandler();

        loadCommunity(ShopByCommunityScreen.CommunityId);

        return view;
    }

    public void filterHandler() {
        if (ShopByCommunityScreen.anyDataAvailableToShow && ShopByCommunityScreen.CallingFrom.equals("ProductsCommunity")) {
            MinimumPrice = ShopByCommunityScreen.MinimumPrice;
            MaximumPrice = ShopByCommunityScreen.MaximumPrice;
            Type = ShopByCommunityScreen.Type;
            Category = ShopByCommunityScreen.Category;
            Brand = ShopByCommunityScreen.Brand;
            Community = SuperSavorZoneScreen.Community;
            //loadCommunity(Community);
        }
    }

    public void initItem() {
        currentPageNumber =0;
        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        llFilter = view.findViewById(R.id.llFilter);
        llFilter.setOnClickListener(this);

        rlSortBy = view.findViewById(R.id.rlSortBy);
        rlSortBy.setOnClickListener(this);

        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);

        ivAdvanceSearch = view.findViewById(R.id.ivAdvanceSearch);
        ivAdvanceSearch.setOnClickListener(this);

        sortByItem();
    }


    @Override
    public void onItemSelected(String value, int position) {

        if (value.equals("Productlisting")) {
            Intent intent = new Intent(getContext(), ProductDetailScreen.class);
            intent.putExtra("id", "" + productsList.getData().get(position).getId());
            intent.putExtra("name", "" + productsList.getData().get(position).getName());
            intent.putExtra("subcategory_id", "" + productsList.getData().get(position).getCategory().getId());
            intent.putExtra("description", "" + productsList.getData().get(position).getDescription());
            intent.putExtra("packaging_quantity", "" + productsList.getData().get(position).getPackaging_quantity());
            intent.putExtra("valucart_price", "" + productsList.getData().get(position).getValucart_price());
            intent.putExtra("maximum_selling_price", "" + productsList.getData().get(position).getMaximum_selling_price());
            intent.putStringArrayListExtra("ImagesList", productsList.getData().get(position).getImages());
            intent.putExtra("packaging_quantity_unit_Symbal",""+productsList.getData().get(position).getPackaging_quantity_unit().getSymbol()+"");
            intent.putExtra("thumbnail", "" + productsList.getData().get(position).getThumbnail());
            startActivity(intent);
        } else if (value.equals("AddToCartAdd")) {
            ShopByCommunityScreen.totalCartValue("add", productsList.getData().get(position).getId(), "product");
        } else if (value.equals("AddToCartSubtract")) {
            ShopByCommunityScreen.totalCartValue("subtract", productsList.getData().get(position).getId(), "product");
        } else if (value.equals("Communities")) {
            if (communitiesList.get(position).getSelectedItem()) {
                communitiesList.get(position).setSelectedItem(false);
            } else {
                if (communitiesList.size() > 0)
                    communitiesList.clear();

                for (int counter = 0; counter < communityList.getData().size(); counter++) {
                    communitiesList.add(new Grocery(communityList.getData().get(counter).getName().toUpperCase(), communityList.getData().get(counter).getId(), communityList.getData().get(counter).getIcon(), false));
                }

                for (int counter = 0; counter < communitiesList.size(); counter++) {
                    if (counter == position) {
                        communitiesList.get(position).setSelectedItem(true);
                        shopBycommunitySelected = communitiesList.get(position).getProductName();
                        shopBycommunityId = communityList.getData().get(position).getId();
                        ShopByCommunityScreen.CommunityId = shopBycommunityId;
                    }
                }
            }

            if (categoriesAdapter == null) {
                categoriesAdapter = new AdapterRvPopolarDepartment(communitiesList, getContext(), this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvShopByCommunityCategories.setLayoutManager(horizontalLayoutManager);
                rvShopByCommunityCategories.setAdapter(categoriesAdapter);
            } else
                categoriesAdapter.notifyDataSetChanged();
            ShopByCommunityScreen.CommunityName = ""+communityList.getData().get(position).getName();
            ShopByCommunityScreen.tvTitle.setText(""+communityList.getData().get(position).getName());
            loadProducts(1,communityList.getData().get(position).getId());
        }

    }


    @Override
    public void onClick(View v) {
        if (llSearch == v) {
            //SearchDialogFragment searchDialogFragment = new SearchDialogFragment(getContext());
            //searchDialogFragment.show(getFragmentManager(), "");
            Intent intent = new Intent(getContext(), SearchScreen.class);
            startActivity(intent);
        }
        if (ivAdvanceSearch == v) {
            startActivity(new Intent(getContext(), SearchFilterScreen.class));
        }
        if (llFilter == v) {
            Intent intent = new Intent(getContext(), SearchFilterScreen.class);
            intent.putExtra("CallingFrom", "ProductsCommunity");
            startActivity(intent);
            getActivity().finish();
        }
        if (rlSortBy == v) {
            spinnerSortBy.performClick();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            //sortOrderSelected
            if (position == 1) {
                sortOrderSelected = "price:low-high";
                loadProducts(1,communityId);
            } else if (position == 2) {
                sortOrderSelected = "price:high-low";
                loadProducts(1,communityId);
            } else {
                sortOrderSelected = "price:latest";
                loadProducts(1,communityId);
            }
            tvSortBy.setText("Sort By " + sortBy[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    int currentPage;
    String communityId;

    private void loadProducts(final int page,final String Id) {
        communityId = Id;
        currentPage = page;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        APIManager.getInstance().getProductsCommunity(sortOrderSelected, MinimumPrice + "-" + MaximumPrice, Id , Category, Brand, page, new APIResponseCallback<Products>() {
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
                if (response.getData().size() != 0)
                    itemList(response,Id,page);

                    pbBYOB.setVisibility(View.GONE);
            }
            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    pbBYOB.setVisibility(View.GONE);
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        });
    }

    Products productsItem;

    private void itemList(Products response, final String id,int pagenumber) {

        productsItem = response;
        groceryRecyclerView = view.findViewById(R.id.rvProducts);
        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (response.getMeta().getCurrent_page() == 1) {
            groceryAdapter = new AdapterRvVerticalProducts(response.getData(), getContext(), "Productlisting", this,this);

            if (library.IsTablet())
                groceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), library.getScreenOrientation(getContext())));
            else
                groceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
                            if(productsItem.getMeta().getCurrent_page()>currentPageNumber) {
                                loadProducts(productsItem.getMeta().getCurrent_page() + 1,id);
                                currentPageNumber = productsItem.getMeta().getCurrent_page();
                            }
                            firstTimeAvoid=0;
                        } else {
                            firstTimeAvoid = 1;
                        }
                    }

                }
            }
        });

    }

    @Override
    public void onWishValueSelected(String value, int position, String action) {
        library.apiLoadItemToWishlist(action, productsList.getData().get(position).getId(), "product");
    }


    private void loadCommunity(final String communityId) {
        library.showLoading();
        APIManager.getInstance().getProductsCommunities(new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                library.hideLoading();
                communityList = response;
                if (communityList.getData().size() > 0) {
                    communicatesNames(communityId);
                    loadProducts(1,communityId);
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


    private void communicatesNames(String communityId) {

        communitiesList.clear();

        //shopBycommunitySelected = communityList.getData().get(0).getName();
        //shopBycommunityId = communityList.getData().get(0).getId();
        for (int counter = 0; counter < communityList.getData().size(); counter++) {
            communitiesList.add(new Grocery(communityList.getData().get(counter).getName().toUpperCase(), communityList.getData().get(counter).getId(), communityList.getData().get(counter).getIcon(), false));
        }

        for (int counter = 0; counter < communityList.getData().size(); counter++) {
            if(communityList.getData().get(counter).getId().equals(communityId)){
                shopBycommunitySelected = communityList.getData().get(counter).getName();
                shopBycommunityId = communityList.getData().get(counter).getId();
                communitiesList.get(counter).setSelectedItem(true);
            }
        }

        if (categoriesAdapter == null) {
            //shopBycommunitySelected = "Emirati";
            rvShopByCommunityCategories = view.findViewById(R.id.rvShopByCommunityCategories);
            categoriesAdapter = new AdapterRvPopolarDepartment(communitiesList, getContext(), this,"Communities");
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvShopByCommunityCategories.setLayoutManager(horizontalLayoutManager);
            rvShopByCommunityCategories.setAdapter(categoriesAdapter);
        } else
            categoriesAdapter.notifyDataSetChanged();

    }

}

