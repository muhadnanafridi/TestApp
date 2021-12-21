package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterProductDetailSlider;
import com.valucart_project.adapter.AdapterRvFeaturesHorizantalList;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailScreen extends FragmentActivity implements View.OnClickListener, WishItemSelection, OnItemSelection {

    private RecyclerView rvRelevantItem;
    ViewPager viewPager;
    TabLayout indicator;
    ImageView ivCancel, ivRemove, ivAdd, ivWish;
    TextView tvTotalItem, tvOldPrice, tvSeeMore, tvPrice, tvProductName, tvWeight, tvDescription, tvTotalProductItem;
    ScrollView svProductDetail;
    RelativeLayout rlAddToCart, rlAddCart, rlProceedCart, rlCart,rlOutOfStock;
    String CallingFrom, id, name, subcategory_id, description, packaging_quantity, valucart_price, maximum_selling_price, packaging_quantity_unit_Symbal;
    Library library;
    Products featureProductsList;
    ArrayList<String> imagesList, getImagesIntent;
    String thumbnail,inventory;
    AdapterRvFeaturesHorizantalList adapterFeaturesHorizantalList;
    Boolean isWishlistSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_screen);
        library = new Library(this);

        tvSeeMore = findViewById(R.id.tvSeeMore);
        tvSeeMore.setOnClickListener(this);

        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(this);

        ivRemove = findViewById(R.id.ivRemove);
        ivRemove.setOnClickListener(this);

        tvTotalItem = findViewById(R.id.tvTotalCarts);
        tvTotalItem.setOnClickListener(this);

        tvTotalProductItem = findViewById(R.id.tvTotalProductItem);

        ivAdd = findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(this);

        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvPrice = findViewById(R.id.tvPrice);

        tvProductName = findViewById(R.id.tvProductName);

        tvWeight = findViewById(R.id.tvWeight);

        tvDescription = findViewById(R.id.tvDescription);

        rlAddToCart = findViewById(R.id.rlAddToCart);
        rlAddToCart.setOnClickListener(this);

        rlAddCart = findViewById(R.id.rlAddCart);
        rlAddCart.setOnClickListener(this);

        rlProceedCart = findViewById(R.id.rlProceedCart);
        rlProceedCart.setOnClickListener(this);

        rlCart = findViewById(R.id.rlCart);
        rlCart.setOnClickListener(this);

        ivWish = findViewById(R.id.ivWish);
        ivWish.setOnClickListener(this);

        svProductDetail = findViewById(R.id.svProductDetail);
        svProductDetail.fullScroll(View.FOCUS_UP);
        svProductDetail.smoothScrollTo(0, 0);

        rlOutOfStock = findViewById(R.id.rlOutOfStock);

        svProductDetail.postDelayed(new Runnable() {
            @Override
            public void run() {
                svProductDetail.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 100);
        getIntentRecord();
        viewPagerController();

        if (Constants.totalCart > 0)
            tvTotalItem.setVisibility(View.VISIBLE);

        tvTotalItem.setText("" + Constants.totalCart);
        loadFeatures();
    }

    @Override
    public void onClick(View view) {

        if (view == tvSeeMore) {
            Intent intent = new Intent(this, ProductListingScreen.class);
            intent.putExtra("CallingFrom", "FeatureProducts");
            intent.putExtra("subcategory_id", "" + featureProductsList.getData().get(0).getSubcategory_id());
            startActivity(intent);
        }

        if (view == ivCancel) {
            finish();
        }
        if (ivRemove == view) {
            if (Integer.parseInt(tvTotalItem.getText().toString()) >= 1) {
                //rlAddtoCart.setVisibility(VISIBLE);
                //rlAddItems.setVisibility(GONE);
                //rlAddCart.setVisibility(View.VISIBLE);
                //rlProceedCart.setVisibility(View.GONE);
                loadCart("subtract", id, "product");

            } else {
                loadCart("subtract", id, "product");
            }
        }
        if (ivAdd == view) {
            loadCart("add", id, "product");
        }


        if (rlAddToCart == view) {
            startActivity(new Intent(this, AddToCartProductScreen.class));

        }

        if (rlAddCart == view) {

            loadCart("add", id, "product");
        }

        if (rlProceedCart == view) {
            //rlAddCart.setVisibility(View.VISIBLE);
            //rlProceedCart.setVisibility(View.GONE);
        }

        if (tvTotalItem == view) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
        }

        if (rlCart == view) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
        }

        if (ivWish == view) {
            if (!Constants.access_token.equals("")) {
                if (isWishlistSelected) {
                    ivWish.setImageResource(R.drawable.ic_heart);
                    library.apiLoadItemToWishlist("remove", id, "product");
                    isWishlistSelected = false;
                } else {
                    ivWish.setImageResource(R.drawable.ic_selected_heart);
                    library.apiLoadItemToWishlist("add", id, "product");
                    isWishlistSelected = true;
                }
            } else {
                library.alertErrorMessage("Login First before adding any item to wishlist");
//                Toast.makeText(this, "Login First before adding any item to wishlist", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void loadCart(final String action, String item_id, String item_type) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addCart(action, item_id, item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    if (response.getAsJsonObject().get("status").toString().equals("1")) {

                        //if (Constants.cart_id.equals(""))
                            Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();
                        //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                        DashboardActivity.totalCartValue(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                        if (Constants.totalCart > 0)
                            tvTotalItem.setVisibility(View.VISIBLE);
                        tvTotalItem.setText("" + Constants.totalCart);
                        displayTotalCart(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));

                        if(action.equals("add")){
                            rlAddCart.setVisibility(View.GONE);
                            rlProceedCart.setVisibility(View.VISIBLE);
                            tvTotalProductItem.setText("" + (Integer.parseInt(tvTotalProductItem.getText().toString()) + 1));
                        }else{
                            tvTotalProductItem.setText("" + (Integer.parseInt(tvTotalProductItem.getText().toString()) - 1));
                            if(tvTotalProductItem.getText().toString().equals("0")) {
                                rlAddCart.setVisibility(View.VISIBLE);
                                rlProceedCart.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        library.alertErrorMessage(""+response.getAsJsonObject().get("message").toString());
                        Toast.makeText(getApplicationContext(), response.getAsJsonObject().get("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }


    @Override
    public void onWishValueSelected(String value, int position, String action) {

    }

    public static void displayTotalCart(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        editor.apply();
    }

    @Override
    public void onItemSelected(String value, int position) {
        Intent intent = new Intent(this, ProductDetailScreen.class);
        intent.putExtra("id", "" + featureProductsList.getData().get(position).getId());
        intent.putExtra("name", "" + featureProductsList.getData().get(position).getName());
        intent.putExtra("subcategory_id", "" + featureProductsList.getData().get(position).getCategory().getId());
        intent.putExtra("description", "" + featureProductsList.getData().get(position).getDescription());
        intent.putExtra("packaging_quantity", "" + featureProductsList.getData().get(position).getPackaging_quantity());
        intent.putExtra("valucart_price", "" + featureProductsList.getData().get(position).getValucart_price());
        intent.putExtra("maximum_selling_price", "" + featureProductsList.getData().get(position).getMaximum_selling_price());
        intent.putExtra("packaging_quantity_unit_Symbal", "" + featureProductsList.getData().get(position).getPackaging_quantity_unit().getSymbol());
        intent.putStringArrayListExtra("ImagesList", featureProductsList.getData().get(position).getImages());
        intent.putExtra("thumbnail", "" + featureProductsList.getData().get(position).getThumbnail());
        intent.putExtra("inventory",""+inventory);
        startActivity(intent);
        finish();
    }

    public void viewPagerController() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        if (imagesList != null)
            viewPager.setAdapter(new AdapterProductDetailSlider(this, imagesList));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            ProductDetailScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < imagesList.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void getIntentRecord() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            CallingFrom = extras.getString("CallingFrom");
            id = extras.getString("id");
            name = extras.getString("name");
            subcategory_id = extras.getString("subcategory_id");
            description = extras.getString("description");
            packaging_quantity = extras.getString("packaging_quantity");
            valucart_price = extras.getString("valucart_price");
            maximum_selling_price = extras.getString("maximum_selling_price");
            getImagesIntent = extras.getStringArrayList("ImagesList");
            thumbnail = extras.getString("thumbnail");
            inventory = extras.getString("inventory");

            if(inventory.equals("0")) {
                rlOutOfStock.setVisibility(View.VISIBLE);
                rlAddCart.setVisibility(View.GONE);
            }

            packaging_quantity_unit_Symbal = extras.getString("packaging_quantity_unit_Symbal");

            if (maximum_selling_price.equals(valucart_price)) {
                tvOldPrice.setText("");
                //tvOldPrice.setVisibility(View.GONE);
            }
            else
                tvOldPrice.setText(maximum_selling_price + " AED");

            tvPrice.setText(valucart_price + " AED");
            tvProductName.setText("" + name);

            if (packaging_quantity_unit_Symbal != null)
                tvWeight.setText("" + packaging_quantity + " " + packaging_quantity_unit_Symbal);
            else
                tvWeight.setVisibility(View.GONE);

            tvDescription.setText("" + description + "");

            imagesList = new ArrayList<>();
            imagesList.add(thumbnail);
            for (int counter = 0; counter < getImagesIntent.size(); counter++) {
                imagesList.add(getImagesIntent.get(counter).toString());
            }

        }

    }

    private void loadFeatures() {

        library.showLoading();
        APIManager.getInstance().getCategory(subcategory_id, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                library.hideLoading();
                featureProductsList = response;
                featureItem(response);
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

    private void featureItem(Products featureProducts) {
        rvRelevantItem = findViewById(R.id.rvRelevantItem);
        adapterFeaturesHorizantalList = new AdapterRvFeaturesHorizantalList(featureProducts.getData(), this, "features", this, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRelevantItem.setLayoutManager(horizontalLayoutManager);
        rvRelevantItem.setAdapter(adapterFeaturesHorizantalList);
        adapterFeaturesHorizantalList.notifyDataSetChanged();
    }

}

