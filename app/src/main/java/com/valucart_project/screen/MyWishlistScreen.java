package com.valucart_project.screen;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvMyWishlist;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
import com.valucart_project.models.ProductsWishList;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyWishlistScreen extends Activity implements View.OnClickListener {

    private List<Grocery> groceryList = new ArrayList<>();
    private RecyclerView rvAddItemForCheckout;
    private AdapterRvMyWishlist adapterRvMyWishlist;
    Library library;
    RelativeLayout rlCleanAll;
    ProductsWishList productWishList;
    Products allProductsList = new Products();
    TextView tvNoRecordAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wishlist_screen);
        library = new Library(this);

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);
        rlCleanAll = findViewById(R.id.rlCleanAll);
        rlCleanAll.setOnClickListener(this);

        loadWishList();
    }

    private void itemList(){

        combineProducts();

        rvAddItemForCheckout = findViewById(R.id.rvAddItemForCheckout);
        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterRvMyWishlist = new AdapterRvMyWishlist(allProductsList, this);

        if(library.IsTablet())
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1 ));
        else
            rvAddItemForCheckout.setLayoutManager(new GridLayoutManager(this, 1));
        rvAddItemForCheckout.setAdapter(adapterRvMyWishlist);
    }


    @Override
    public void onClick(View v) {
        apiClearWishlist();
    }

    private void loadWishList() {
        library.showLoading();
        APIManager.getInstance().getWishList(new APIResponseCallback<ProductsWishList>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsWishList response) {
                library.hideLoading();
                productWishList = response;
                if(response.getData().getBundles().size()==0 && response.getData().getProducts().size()==0 ) {
                    rlCleanAll.setVisibility(View.GONE);
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                } else {
                    itemList();
                }
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

    public void combineProducts() {
        allProductsList.getData().clear();
        for (int counter = 0; counter < productWishList.getData().getProducts().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(productWishList.getData().getProducts().get(counter).getName());
            data.setDescription(productWishList.getData().getProducts().get(counter).getDescription());
            data.setId("" + productWishList.getData().getProducts().get(counter).getId());
            data.setImages(productWishList.getData().getProducts().get(counter).getImages());
            data.setAddToByob(productWishList.getData().getProducts().get(counter).getAddToByob());
            data.setMaximum_selling_price(productWishList.getData().getProducts().get(counter).getMaximum_selling_price());
            data.setValucart_price(productWishList.getData().getProducts().get(counter).getValucart_price());
            data.setThumbnail(productWishList.getData().getProducts().get(counter).getThumbnail());
            data.setPackaging_quantity(productWishList.getData().getProducts().get(counter).getPackaging_quantity());
            data.setPackaging_quantity_weight( productWishList.getData().getProducts().get(counter).getPackaging_quantity_unit().getSymbol());
            //data.setPackaging_quantity_unit(productWishList.getData().getProducts().get(counter).getPackaging_quantity_unit());
            data.setInventory(productWishList.getData().getProducts().get(counter).getInventory());
            data.setProductType("product");
            allProductsList.getData().add(data);
        }

        for (int counter = 0; counter < productWishList.getData().getBundles().size(); counter++) {
            Products.Data data = new Products.Data();
            data.setName(productWishList.getData().getBundles().get(counter).getName().replaceAll("%20", " "));
            data.setDescription(productWishList.getData().getBundles().get(counter).getDescription());
            data.setId("" + productWishList.getData().getBundles().get(counter).getId());
            data.setImages(productWishList.getData().getBundles().get(counter).getImages());
            data.setAddToByob(productWishList.getData().getBundles().get(counter).getAddToByob());
            data.setMaximum_selling_price(productWishList.getData().getBundles().get(counter).getMaximum_selling_price());
            data.setValucart_price(productWishList.getData().getBundles().get(counter).getValucart_price());
            if (!productWishList.getData().getBundles().get(counter).getThumbnail().equals("")) {
                data.setThumbnail(productWishList.getData().getBundles().get(counter).getThumbnail());
            } else {
                if (productWishList.getData().getBundles().get(counter).getImages().size() > 0)
                    data.setThumbnail(productWishList.getData().getBundles().get(counter).getImages().get(0));
                else
                    data.setThumbnail("");
            }
            //data.setThumbnail(productWishList.getData().getBundles().get(counter).getThumbnail());
            data.setPackaging_quantity(productWishList.getData().getBundles().get(counter).getPackaging_quantity());
            data.setPackaging_quantity_weight(productWishList.getData().getBundles().get(counter).getPackaging_quantity_unit().getSymbol() );
            //data.setPackaging_quantity_weight("");

            data.setProductType("bundle");
            //data.setPackaging_quantity_unit(productWishList.getData().getBundles().get(counter).getPackaging_quantity_unit());
            allProductsList.getData().add(data);
        }
    }

    private void apiClearWishlist() {
        library.showLoading();
        APIManager.getInstance().clearWishlist(new APIResponseCallback<ProductsWishList>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsWishList response) {
                library.hideLoading();
                productWishList = response;
                allProductsList.getData().clear();
                adapterRvMyWishlist.notifyDataSetChanged();
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


}

