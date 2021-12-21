package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvSearchFilterCategories;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Categories;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.SubCategory;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import me.tom.range.slider.RangeSliderView;

public class SearchFilterScreen extends Activity implements OnItemSelection {

    private List<Grocery> categoriesItem = new ArrayList<>();
    private List<Grocery> brandItem = new ArrayList<>();
    private List<Grocery> communityItem = new ArrayList<>();
    private RecyclerView rvCategories, rvBrands, rvCommunity;
    private AdapterRvSearchFilterCategories categoriesAdapter, departmentAdapter, communiutyAdapter;
    Library library;
    RelativeLayout rlDone;
    TextView tvMin, tvMax, tvProduct, tvBundle, tvBulk, tvType, tvBrands, tvCategories;
    LinearLayout llBulk, llMyBundle, llProduct, llBundles, llType;
    String CallingFrom = "",SubCategoryId="",Category = "";
    String typeSelected = "";
    SubCategory brandsList, communityList,subCategory;
    public static Categories categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter_screen);
        library = new Library(this);

        rlDone = findViewById(R.id.rlDone);

        initFields();
        priceHandler();

        categoriesItem();

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),ProductListingScreen.class));
                //finish();

                if(typeSelected.equals("Bundle")){
                    CallingFrom = "Bundle";
                }else if(typeSelected.equals("Bulk")){
                    CallingFrom = "Bulk";
                }

                if (CallingFrom.equals("Bundle")) {
                    Intent intent = new Intent(getApplicationContext(), SuperSavorZoneScreen.class);
                    intent.putExtra("CallingFrom", "BundleSearch");
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                } else if (CallingFrom.equals("Bulk")) {
                    Intent intent = new Intent(getApplicationContext(), SuperSavorZoneScreen.class);
                    intent.putExtra("CallingFrom", "BulkSearch");
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Categories", itemSelected(categoriesItem));
                    intent.putExtra("Brand", itemSelected(brandItem));
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                } else if (CallingFrom.equals("Byob")) {
                    Intent intent = new Intent(getApplicationContext(), SuperSavorZoneScreen.class);
                    intent.putExtra("CallingFrom", "ByobSearch");
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Type", typeSelected);
                    intent.putExtra("Categories", itemSelected(categoriesItem));
                    intent.putExtra("Brand", itemSelected(brandItem));
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                } else if (CallingFrom.equals("ProductsCommunity")){
                    Intent intent = new Intent(getApplicationContext(), ShopByCommunityScreen.class);
                    intent.putExtra("CallingFrom", CallingFrom);
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Type", typeSelected);
                    intent.putExtra("Categories", itemSelected(categoriesItem));
                    intent.putExtra("Brand", itemSelected(brandItem));
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                } else if (CallingFrom.equals("BulkCommunity")) {
                    Intent intent = new Intent(getApplicationContext(), ShopByCommunityScreen.class);
                    intent.putExtra("CallingFrom", CallingFrom);
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Categories", itemSelected(categoriesItem));
                    intent.putExtra("Brand", itemSelected(brandItem));
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                }else  if (CallingFrom.equals("BundleCommunity")) {
                    Intent intent = new Intent(getApplicationContext(), ShopByCommunityScreen.class);
                    intent.putExtra("CallingFrom", CallingFrom);
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ProductListingScreen.class);
                    intent.putExtra("CallingFrom", CallingFrom);
                    intent.putExtra("MinimumPrice", tvMin.getText().toString().split("\\s+")[1]);
                    intent.putExtra("MaximumPrice", tvMax.getText().toString().split("\\s+")[1]);
                    intent.putExtra("Type", typeSelected);
                    intent.putExtra("filterRecord", "yes");

                    //if(!Category.equals(""))

                    if(SubCategoryId.equals("")) {
                        //intent.putExtra("Categories", Category);
                        intent.putExtra("Categories", itemSelected(categoriesItem).equals("")?Category:itemSelected(categoriesItem));
                        intent.putExtra("SubCategories","");
                    } else {
                        intent.putExtra("Categories",Category);
                        intent.putExtra("SubCategories",itemSelected(categoriesItem));
                        intent.putExtra("SubCategoryId",SubCategoryId);
                    }

                    intent.putExtra("Brand", itemSelected(brandItem));
                    intent.putExtra("Community", itemSelected(communityItem));
                    startActivity(intent);
                    finish();
                }
            }
        });

        loadCommunity();
    }

    public void fetchingData() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            if (CallingFrom.equals("Byob")) {
                llBundles.setVisibility(View.GONE);
                loadProducts();
            } else if (CallingFrom.equals("Bulk")) {
                llType.setVisibility(View.GONE);
                tvType.setVisibility(View.GONE);
                loadProducts();
            } else if (CallingFrom.equals("Bundle")) {
                llType.setVisibility(View.GONE);
                tvType.setVisibility(View.GONE);

                tvBrands.setVisibility(View.GONE);
                rvBrands.setVisibility(View.GONE);

                tvCategories.setVisibility(View.GONE);
                rvCategories.setVisibility(View.GONE);
            }else if (CallingFrom.equals("ProductsCommunity")) {
                llProduct.setVisibility(View.GONE);
                loadProducts();
            } else if (CallingFrom.equals("BulkCommunity")) {
                llType.setVisibility(View.GONE);
                tvType.setVisibility(View.GONE);
                loadProducts();
            } else if (CallingFrom.equals("BundleCommunity")) {
                llType.setVisibility(View.GONE);
                tvType.setVisibility(View.GONE);

                tvBrands.setVisibility(View.GONE);
                rvBrands.setVisibility(View.GONE);

                tvCategories.setVisibility(View.GONE);
                rvCategories.setVisibility(View.GONE);
            }else if (CallingFrom.equals("Departments")){
                SubCategoryId = extras.getString("SubCategoryId");
                Category = extras.getString("Category");
                loadProducts();
            }
            else {
                loadProducts();
            }
        }

    }

    public void initFields() {
        typeSelected = "";
/*        CardView card_view_top = findViewById(R.id.card_view_top);
        card_view_top.setRadius(20);

        CardView cvBundle = findViewById(R.id.cvBundle);
        cvBundle.setRadius(20);

        CardView cvBulk = findViewById(R.id.cvBulk);
        cvBulk.setRadius(20);*/

        llBulk = findViewById(R.id.llBulk);
        llMyBundle = findViewById(R.id.llMyBundle);
        llProduct = findViewById(R.id.llProduct);

        tvProduct = findViewById(R.id.tvProduct);
        tvBundle = findViewById(R.id.tvBundle);
        tvBulk = findViewById(R.id.tvBulk);

        llBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBulk.setBackgroundResource(R.drawable.bg_rounded_purple);
                llMyBundle.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                llProduct.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

                tvProduct.setTextColor(getResources().getColor(R.color.colorBlack));
                tvBulk.setTextColor(getResources().getColor(R.color.colorClearWhite));
                tvBundle.setTextColor(getResources().getColor(R.color.colorBlack));
                typeSelected = "Bulk";
            }
        });

        llMyBundle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBulk.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                llMyBundle.setBackgroundResource(R.drawable.bg_rounded_purple);
                llProduct.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

                tvProduct.setTextColor(getResources().getColor(R.color.colorBlack));
                tvBundle.setTextColor(getResources().getColor(R.color.colorClearWhite));
                tvBulk.setTextColor(getResources().getColor(R.color.colorBlack));
                typeSelected = "Bundle";
            }
        });

        llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBulk.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                llMyBundle.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                llProduct.setBackgroundResource(R.drawable.bg_rounded_purple);

                tvBundle.setTextColor(getResources().getColor(R.color.colorBlack));
                tvBulk.setTextColor(getResources().getColor(R.color.colorBlack));
                tvProduct.setTextColor(getResources().getColor(R.color.colorClearWhite));

                typeSelected = "Product";
            }
        });

        llBundles = findViewById(R.id.llBundles);
        llType = findViewById(R.id.llType);
        tvType = findViewById(R.id.tvType);
        tvBrands = findViewById(R.id.tvBrands);
        tvCategories = findViewById(R.id.tvCategories);
        rvBrands = findViewById(R.id.rvBrands);
        rvCategories = findViewById(R.id.rvCategories);
        rvCommunity = findViewById(R.id.rvCommunity);
    }

    public void priceHandler() {

        tvMin = findViewById(R.id.tvMin);
        tvMax = findViewById(R.id.tvMax);

        RangeSliderView rangeSliderView = (RangeSliderView) findViewById(R.id.rangeSliderView);
        //rangeSliderView.setMinAndMaxValue(20,1000);
        ArrayList<Integer> values = new ArrayList<>();
        for (int counter = 1; counter < 1001; counter++)
            values.add(counter);
        //values.add(1000);
        rangeSliderView.setRangeValues(values);

        rangeSliderView.setOnValueChangedListener(new RangeSliderView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue) {
                Log.d("min value:", String.valueOf(minValue));
                Log.d("max value:", String.valueOf(maxValue));
                tvMin.setText("AED " + minValue);
                tvMax.setText("AED " + maxValue);
            }

            @Override
            public String parseMinValueDisplayText(int minValue) {
                return super.parseMinValueDisplayText(minValue);
            }

            @Override
            public String parseMaxValueDisplayText(int maxValue) {
                return super.parseMinValueDisplayText(maxValue);
            }
        });

    }

    private void categoriesItem() {

        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        categoriesAdapter = new AdapterRvSearchFilterCategories(categoriesItem, this, this, "category");

        if (library.IsTablet())
            rvCategories.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 7 : 4));
        else
            rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
        rvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void onItemSelected(String value, int position) {

        if (value.equals("brand")) {

            if (brandItem.get(position).getSelectedItem()) {
                brandItem.get(position).setSelectedItem(false);
            } else {
                brandItem.get(position).setSelectedItem(true);
            }

            departmentAdapter.notifyDataSetChanged();
        } else if (value.equals("community")) {

            if (communityItem.get(position).getSelectedItem()) {
                communityItem.get(position).setSelectedItem(false);
            } else {
                communityItem.get(position).setSelectedItem(true);
            }

            communiutyAdapter.notifyDataSetChanged();
        } else {

            if (categoriesItem.get(position).getSelectedItem()) {
                categoriesItem.get(position).setSelectedItem(false);
            } else {
                categoriesItem.get(position).setSelectedItem(true);
            }

            categoriesAdapter.notifyDataSetChanged();
        }

    }

    private void loadProducts() {

        library.showLoading();
        APIManager.getInstance().getProductsBrands(new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                //library.hideLoading();
                brandsList = response;
                brandsItem();
                if(SubCategoryId==null){
                    loadDepartment();
                } else if(SubCategoryId.equals(""))
                    loadDepartment();
                else
                    loadDepartmentSubCategories(SubCategoryId);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                //loadDepartment();
                try {
                    library.alertErrorMessage(jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    private void brandsItem() {
        for (int counter = 0; counter < brandsList.getData().size(); counter++) {
            brandItem.add(new Grocery(brandsList.getData().get(counter).getName(),brandsList.getData().get(counter).getId(), R.drawable.ic_product , false));
        }

        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        departmentAdapter = new AdapterRvSearchFilterCategories(brandItem, this, this, "brand");

        if (library.IsTablet())
            rvBrands.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 7 : 4));
        else
            rvBrands.setLayoutManager(new GridLayoutManager(this, 3));
        rvBrands.setAdapter(departmentAdapter);
        //populategroceryList();
    }

    private void loadDepartment() {

        APIManager.getInstance().getCategories(new APIResponseCallback<Categories>() {

            @Override
            public void onResponseLoaded(@NonNull Categories response) {
                library.hideLoading();
                categories = response;
                categoriesList();
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

    private void loadDepartmentSubCategories( String id) {

        //library.showLoading();
        APIManager.getInstance().getMainCategories(id ,new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                library.hideLoading();
                subCategory = response;
                if(subCategory.getData().size()==0)
                    tvCategories.setVisibility(View.GONE);
                subCategoriesList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    private void subCategoriesList() {
        for (int counter = 0; counter < subCategory.getData().size(); counter++) {
            categoriesItem.add(new Grocery(subCategory.getData().get(counter).getName(),subCategory.getData().get(counter).getId(), R.drawable.ic_product, false));
        }
        categoriesAdapter.notifyDataSetChanged();
    }


    private void categoriesList() {
        for (int counter = 0; counter < categories.getData().size(); counter++) {
            categoriesItem.add(new Grocery(categories.getData().get(counter).getName(),categories.getData().get(counter).getId(), R.drawable.ic_product, false));
        }
        categoriesAdapter.notifyDataSetChanged();
    }

    private void loadCommunity() {

        library.showLoading();
        APIManager.getInstance().getProductsCommunities(new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                library.hideLoading();
                communityList = response;
                communityItem();
                fetchingData();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }


    private void communityItem() {

        // add a divider after each item for more clarity
        //groceryRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        communiutyAdapter = new AdapterRvSearchFilterCategories(communityItem, this, this, "community");

        if (library.IsTablet())
            rvCommunity.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 7 : 4));
        else
            rvCommunity.setLayoutManager(new GridLayoutManager(this, 3));
        rvCommunity.setAdapter(communiutyAdapter);
        communityList();
    }


    private void communityList() {

        for (int counter = 0; counter < communityList.getData().size(); counter++) {
            communityItem.add(new Grocery(communityList.getData().get(counter).getName().substring(0, 1).toUpperCase() + communityList.getData().get(counter).getName().substring(1),communityList.getData().get(counter).getId(), R.drawable.ic_product, false));
        }
        communiutyAdapter.notifyDataSetChanged();

    }


    public String itemSelected(List<Grocery> item){
        String values="";
        for(int counter = 0 ;counter<item.size();counter++){
            if(item.get(counter).getSelectedItem()) {
                if(values.equals(""))
                    values = item.get(counter).getId();
                else
                    values = values+","+item.get(counter).getId();
            }
        }
        return values;
    }

}
