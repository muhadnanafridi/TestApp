package com.valucart_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.valucart_project.adapter.AdapterRvHorizantalSearchRelatedCategories;
import com.valucart_project.adapter.AdapterRvVerticalProducts;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
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

public class BulkCommunityListingFragment extends Fragment implements OnItemSelection , View.OnClickListener , AdapterView.OnItemSelectedListener , WishItemSelection , SpinnerViewSelection {

    private List<Grocery> groceryList = new ArrayList<>();
    private List<Grocery> categoriesList = new ArrayList<>();
    private RecyclerView groceryRecyclerView;
    private AdapterRvVerticalProducts groceryAdapter;
    Library library;
    private AdapterRvHorizantalSearchRelatedCategories categoriesAdapter;
    View view;
    LinearLayout llSearch,llFilter;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bulk_listing_fragment, container, false);
        library = new Library(getContext());

        initItem();
        filterHandler();
        loadProducts(1);
        return  view;
    }

    public void initItem(){
        currentPageNumber =0;
        llFilter = view.findViewById(R.id.llFilter);
        llFilter.setOnClickListener(this);

        rlSortBy= view.findViewById(R.id.rlSortBy);
        rlSortBy.setOnClickListener(this);

        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);

        ivAdvanceSearch = view.findViewById(R.id.ivAdvanceSearch);
        ivAdvanceSearch.setOnClickListener(this);

        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        sortByItem();
    }

    public void filterHandler(){
        if(ShopByCommunityScreen.anyDataAvailableToShow && ShopByCommunityScreen.CallingFrom.equals("BulkCommunity")){
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
        if(value.equals("Bulklisting")){
            Intent intent = new Intent(getContext(), ProductDetailScreen.class);
            intent.putExtra("id",""+productsList.getData().get(position).getId());
            intent.putExtra("name",""+productsList.getData().get(position).getName());
            intent.putExtra("subcategory_id",""+productsList.getData().get(position).getCategory().getId());
            intent.putExtra("description",""+productsList.getData().get(position).getDescription());
            intent.putExtra("packaging_quantity",""+productsList.getData().get(position).getPackaging_quantity());
            intent.putExtra("valucart_price",""+productsList.getData().get(position).getValucart_price());
            intent.putExtra("maximum_selling_price",""+productsList.getData().get(position).getMaximum_selling_price());
            if(productsList.getData().get(position).getIs_bulk() && productsList.getData().get(position).getBulk_quantity()>0)
                intent.putExtra("packaging_quantity_unit_Symbal",""+productsList.getData().get(position).getPackaging_quantity_unit().getSymbol()+" * "+productsList.getData().get(position).getBulk_quantity());
            else
                intent.putExtra("packaging_quantity_unit_Symbal",""+productsList.getData().get(position).getPackaging_quantity_unit().getSymbol()+"");
            intent.putStringArrayListExtra("ImagesList",productsList.getData().get(position).getImages());
            intent.putExtra("thumbnail",""+productsList.getData().get(position).getThumbnail());
            intent.putExtra("inventory",""+productsList.getData().get(position).getInventory());
            startActivity(intent);
        }else if(value.equals("AddToCartAdd")){
                ShopByCommunityScreen.totalCartValue("add",productsList.getData().get(position).getId(),"bulk");
        }else if(value.equals("AddToCartSubtract")){
                ShopByCommunityScreen.totalCartValue("subtract",productsList.getData().get(position).getId(),"bulk");
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
            startActivity(new Intent(getContext(), SearchFilterScreen.class));
        }if(llFilter==v){
/*            Intent intent = new Intent(getContext(), SearchFilterScreen.class);
            intent.putExtra("CallingFrom","BulkCommunity");
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

    private void loadProducts(int page) {

        currentPage = page;
        if (currentPage >1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        APIManager.getInstance().getProductsBulk( "", sortOrderSelected, MinimumPrice+"-"+MaximumPrice,ShopByCommunityScreen.CommunityId ,Category,Brand,page,"", new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
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

                if (response.getData().size() != 0)
                    itemList(response);

                pbBYOB.setVisibility(View.GONE);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    Products productsItem;
    private void itemList(Products response){

        productsItem = response;
        groceryRecyclerView = view.findViewById(R.id.rvBunderList);
        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (response.getMeta().getCurrent_page() == 1) {
            groceryAdapter = new AdapterRvVerticalProducts(response.getData(), getContext(),"Bulklisting",this,this);

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
    int currentPageNumber =0;

    @Override
    public void onWishValueSelected(String value, int position, String action) {
        library.apiLoadItemToWishlist(action, productsList.getData().get(position).getId(), "product");
    }

    @Override
    public void onValueSelected(String value, int position) {
        sortOrderSelected = value;
        loadProducts(1);
        tvSortBy.setText("Sort By " + sortBy[position]);
    }
}

