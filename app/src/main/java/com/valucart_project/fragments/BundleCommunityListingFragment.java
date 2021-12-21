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
import com.valucart_project.adapter.AdapterRvBundleCategories;
import com.valucart_project.adapter.AdapterRvVerticalBundle;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Categories;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.ProductsBundle;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.screen.BundleDetailScreen;
import com.valucart_project.screen.SearchFilterScreen;
import com.valucart_project.screen.SearchScreen;
import com.valucart_project.screen.ShopByCommunityScreen;
import com.valucart_project.screen.SuperSavorZoneScreen;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BundleCommunityListingFragment extends Fragment implements OnItemSelection, View.OnClickListener  , AdapterView.OnItemSelectedListener , WishItemSelection , SpinnerViewSelection {

    private List<Grocery> groceryList = new ArrayList<>();
    private List<Grocery> categoriesList = new ArrayList<>();
    private List<Grocery> categoriesListUpdate = new ArrayList<>();
    private RecyclerView groceryRecyclerView,rvItemsCategories;
    private AdapterRvVerticalBundle groceryAdapter;
    Library library;
    private AdapterRvBundleCategories categoriesAdapter;
    View view;
    LinearLayout llSearch,llFilter;
    RelativeLayout rlSortBy;
    ImageView ivAdvanceSearch;
    Spinner spinnerSortBy;
    private String[] sortBy = {"Select", "price:low-high", "price:high-low", "Latest Products"};
    TextView tvSortBy;
    ProductsBundle productsList;
    String MinimumPrice = "1";
    String MaximumPrice = "1000";
     String Type = "";
     String Category = "";
     String Brand = "";
    public static String Community = "";
    String sortOrderSelected = "price:latest";
    ProgressBar pbBYOB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bundle_listing_fragment, container, false);

        library = new Library(getContext());
        initItem();
        filterHandler();
        GetCategoryApi();

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initItem(){
        currentPageNumber =0;

        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);

        ivAdvanceSearch = view.findViewById(R.id.ivAdvanceSearch);
        ivAdvanceSearch.setOnClickListener(this);

        llFilter = view.findViewById(R.id.llFilter);
        llFilter.setOnClickListener(this);

        rlSortBy= view.findViewById(R.id.rlSortBy);
        rlSortBy.setOnClickListener(this);

        //categoriesItem();
        sortByItem();
    }

    public void filterHandler(){
        if(ShopByCommunityScreen.anyDataAvailableToShow && ShopByCommunityScreen.CallingFrom.equals("BundleCommunity")){
            MinimumPrice = ShopByCommunityScreen.MinimumPrice;
            MaximumPrice = ShopByCommunityScreen.MaximumPrice;
            Type = ShopByCommunityScreen.Type;
            Category = ShopByCommunityScreen.Category;
            Brand = ShopByCommunityScreen.Brand;
            Community= SuperSavorZoneScreen.Community;
        }
    }




    @Override
    public void onItemSelected(String value, int position) {

        if(value.equals("bundleList")){
            Intent intent = new Intent(getContext(), BundleDetailScreen.class);
            intent.putExtra("id",""+productsList.getData().get(position).getId());
            intent.putExtra("inventory",""+productsList.getData().get(position).getInventory());
            startActivity(intent);
        }else if(value.equals("AddToCartAdd")){
                ShopByCommunityScreen.totalCartValue("add",productsList.getData().get(position).getId(),"bundle");
        }else if(value.equals("AddToCartSubtract")){
                ShopByCommunityScreen.totalCartValue("subtract",productsList.getData().get(position).getId(),"bundle");
        }else {
            Category =categoriesListItem.getData().get(position).getId();
            if (categoriesListItem.getData().get(position).getSelectedItem()) {
                categoriesListItem.getData().get(position).setSelectedItem(false);
            } else {
               /* categoriesList.clear();
                categoriesList.add(new Grocery("Nuts", R.drawable.ic_product));
                categoriesList.add(new Grocery("Pasta", R.drawable.ic_product));
                categoriesList.add(new Grocery("Breakfast Items", R.drawable.ic_product));
                categoriesList.add(new Grocery("Beveragea", R.drawable.ic_product));
                categoriesList.add( new Grocery("Biscuits and Cakes", R.drawable.ic_product));
                categoriesList.add( new Grocery("Olives and Pickles", R.drawable.ic_product));*/

                for (int counter = 0; counter < categoriesListItem.getData().size(); counter++) {
                    if (counter == position)
                        categoriesListItem.getData().get(counter).setSelectedItem(true);
                    else
                        categoriesListItem.getData().get(counter).setSelectedItem(false);
                }
            }

            if(categoriesAdapter==null) {
                categoriesAdapter = new AdapterRvBundleCategories(categoriesListItem, getContext(), this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvItemsCategories.setLayoutManager(horizontalLayoutManager);
                rvItemsCategories.setAdapter(categoriesAdapter);
            }else
                categoriesAdapter.notifyDataSetChanged();

            loadProducts(1);
        }

    }

    @Override
    public void onClick(View v) {
        if(llSearch==v){
            //SearchDialogFragment searchDialogFragment = new SearchDialogFragment( getContext());
            //searchDialogFragment.show(getFragmentManager(),"");
            Intent intent = new Intent(getContext(), SearchScreen.class);
            startActivity(intent);
        }if(ivAdvanceSearch==v){
            Intent intent = new Intent(getContext(), SearchFilterScreen.class);
            intent.putExtra("CallingFrom","Bundle");
            startActivity(intent);
            getActivity().finish();
        }if(llFilter==v){
/*            Intent intent = new Intent(getContext(), SearchFilterScreen.class);
            intent.putExtra("CallingFrom","BundleCommunity");
            startActivity(intent);
            getActivity().finish();*/
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(getContext());
            //residentNotesDialogFragment.show(getFragmentManager(), "");
            Intent intent = new Intent(getContext(), SearchScreen.class);
            startActivity(intent);
        }if(rlSortBy==v){
            //spinnerSortBy.performClick();
            library.bottomsheetSort(getActivity(),this);
        }
    }

    public void sortByItem(){
        tvSortBy = (TextView) view.findViewById(R.id.tvSortBy);

        spinnerSortBy = (Spinner) view.findViewById(R.id.spinnerSortBy);
        AdapterForSpinner aa = new AdapterForSpinner(getContext(),R.layout.simple_spinner_item,sortBy);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    int currentPage;
    private void loadProducts( int page ) {

        currentPage = page;
        if (currentPage >1)
            pbBYOB.setVisibility(View.VISIBLE);
        APIManager.getInstance().getProductsBundles( sortOrderSelected, MinimumPrice+"-"+MaximumPrice,ShopByCommunityScreen.CommunityId,Category,Brand,page, new APIResponseCallback<ProductsBundle>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsBundle response) {
                library.hideLoading();
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        productsList.getData().add(response.getData().get(counter));
                    }

                    //for (int counter = 0; counter < response.getData().size(); counter++) {
                    //    productsList.getData().get(counter).setPackaging_quantity_unit(response.getData().get(counter).getPackaging_quantity_unit());
                    //}
                } else {
                    productsList = response;
                }
                itemList(response);

                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    ProductsBundle productsItem;
    private void itemList(ProductsBundle response){

        productsItem = response;
        groceryRecyclerView = view.findViewById(R.id.rvBunderList);
        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (response.getMeta().getCurrent_page() == 1) {
            groceryAdapter = new AdapterRvVerticalBundle(response.getData(), getContext(),"bundleList",this,this);

            if(library.IsTablet())
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
                                loadProducts(productsItem.getMeta().getCurrent_page() + 1);
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
    int firstTimeAvoid = 0;
    Categories categoriesListItem;
    int currentPageNumber =0;
    private void GetCategoryApi() {
        //library.showLoading();
        APIManager.getInstance().getBundleCategory(new APIResponseCallback<Categories>() {

            @Override
            public void onResponseLoaded(@NonNull Categories response) {
                //library.hideLoading();
                categoriesListItem = response;
                categoriesItem();

                loadProducts(1);

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void categoriesItem() {
        rvItemsCategories = view.findViewById(R.id.rvItemsCategories);
        //rvFeaturedItems.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        categoriesAdapter = new AdapterRvBundleCategories(categoriesListItem, getContext(),this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(categoriesAdapter);
        //populateCategoriesList();
    }

    private void populateCategoriesList(){
        for(int counter=0;counter<categoriesListItem.getData().size();counter++) {
            categoriesList.add(new Grocery(categoriesListItem.getData().get(counter).getName(),categoriesListItem.getData().get(counter).getId(),categoriesListItem.getData().get(counter).getIcon(), false));
        }
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWishValueSelected(String value, int position, String action) {
        library.apiLoadItemToWishlist(action, productsItem.getData().get(position).getId(), "bundle");
    }

    @Override
    public void onValueSelected(String value, int position) {
        sortOrderSelected = value;
        loadProducts(1);
        tvSortBy.setText("Sort By " + sortBy[position]);
    }
}

