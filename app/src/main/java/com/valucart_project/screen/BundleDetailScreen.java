package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvBundleDetail;
import com.valucart_project.adapter.AdapterRvBundleDetailBottomSheet;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.BundleDetail;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BundleDetailScreen extends FragmentActivity implements OnItemSelection {

    private RecyclerView rvBunderList;
    private AdapterRvBundleDetail adapterRvBundleDetail;
    Library library;
    BottomSheetDialog bottomSheetDialog;
    BundleDetail bundleDetailListing;
    Bundle extras;
    String CallingFrom = "", bundleid = "" , inventory = "";
    TextView tvTitleBundle, tvTotalSubTotal, tvTotalAmount, tvDiscount,tvTitle;
    int positionSelected;
    AdapterRvBundleDetailBottomSheet adapterRvBundleDetailBottomSheet;
    RelativeLayout rlProceedCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bundle_detail_screen);
        library = new Library(this);

        ImageView ivMenuSearch = findViewById(R.id.ivMenuSearch);
        ivMenuSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SearchDialogFragment searchDialogFragment = new SearchDialogFragment( getApplicationContext());
                //searchDialogFragment.show(getSupportFragmentManager(),"");
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlProceedCart = findViewById(R.id.rlProceedCart);
        rlProceedCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadCart("add", bundleid, "bundle");
            }
        });

        tvTitle = findViewById(R.id.tvTitle);
        tvTitleBundle = findViewById(R.id.tvTitleBundle);
        tvTotalSubTotal = findViewById(R.id.tvTotalSubTotal);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvDiscount = findViewById(R.id.tvDiscount);

        extraBundle();
        GetBundleApi();
    }

    public void extraBundle() {
        extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            bundleid = extras.getString("id");
            inventory = extras.getString("inventory");
            if(inventory.equals("0")) {
                rlProceedCart.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemSelected(String value, int position) {

        if (value.equals("bottomsheet")) {
            BundleDetail.Data.Products.ProductsAlternatives productAlternativeDetail = new BundleDetail.Data.Products.ProductsAlternatives();
            productAlternativeDetail.setQuantity(bundleDetailListing.getData().getProducts().get(positionSelected).getQuantity());
            productAlternativeDetail.getData().setName(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getName());
            productAlternativeDetail.getData().setDescription(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getDescription());
            productAlternativeDetail.getData().setPackaging_quantity(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getPackaging_quantity());
            productAlternativeDetail.getData().setMaximum_selling_price(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getMaximum_selling_price());
            productAlternativeDetail.getData().setValucart_price(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getValucart_price());
            productAlternativeDetail.getData().setPackaging_quantity_unit(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getPackaging_quantity_unit());
            productAlternativeDetail.getData().setThumbnail(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getThumbnail());
            productAlternativeDetail.getData().setId(bundleDetailListing.getData().getProducts().get(positionSelected).getData().getId());


            BundleDetail.Data.Products.ProductData productDetail = new BundleDetail.Data.Products.ProductData();
            productDetail.setName(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getName());
            productDetail.setDescription(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getDescription());
            productDetail.setPackaging_quantity(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getPackaging_quantity());
            productDetail.setMaximum_selling_price(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getMaximum_selling_price());
            productDetail.setValucart_price(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getValucart_price());
            productDetail.setPackaging_quantity_unit(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getPackaging_quantity_unit());
            productDetail.setThumbnail(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getThumbnail());
            productDetail.setId(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(position).getData().getId());


            bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().set(position,productAlternativeDetail);
            bundleDetailListing.getData().getProducts().get(positionSelected).setData(productDetail);

            bottomSheetDialog.dismiss();
            adapterRvBundleDetail.notifyDataSetChanged();

        } else {
            positionSelected = position;

            if (bundleDetailListing.getData().getProducts().get(position).getAlternatives().size() > 0) {

                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_bundle_detail, null);

                RecyclerView rvSelectedProduct = view.findViewById(R.id.rvSelectedProduct);
                // add a divider after each item for more clarity
                adapterRvBundleDetailBottomSheet = new AdapterRvBundleDetailBottomSheet(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives(), this, this);

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                if (library.IsTablet())
                    rvSelectedProduct.setLayoutManager(horizontalLayoutManager);
                else
                    rvSelectedProduct.setLayoutManager(horizontalLayoutManager);

                rvSelectedProduct.setAdapter(adapterRvBundleDetailBottomSheet);
                adapterRvBundleDetailBottomSheet.notifyDataSetChanged();

                ImageView ivCancel = view.findViewById(R.id.ivCancel);
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                    }
                });

                bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
                bottomSheetDialog.setContentView(view);

                bottomSheetDialog.show();

            }
        }
    }

    private void GetBundleApi() {
        library.showLoading();
        APIManager.getInstance().getBundle(bundleid, new APIResponseCallback<BundleDetail>() {

            @Override
            public void onResponseLoaded(@NonNull BundleDetail response) {
                library.hideLoading();
                bundleDetailListing = response;
                /*try {
                    bundleDetailExchanger = (BundleDetail) bundleDetailListing.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }*/
              /*  bundleDetailExchanger = new BundleDetail.Data();
                for(int counter = 0;counter<bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().size();counter++) {
                    bundleDetailExchanger.getProducts().add(bundleDetailListing.getData().getProducts().get(positionSelected).getAlternatives().get(counter));
                }
                bundleDetailExchanger.getProducts().add(bundleDetailListing.getData().getProducts().get(positionSelected));*/
                itemList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void itemList() {
        try {
            ImageView ivBundle = findViewById(R.id.ivBundle);

            tvTitle.setText("" + bundleDetailListing.getData().getName());
            tvTitleBundle.setText("" + bundleDetailListing.getData().getDescription());

            tvTotalSubTotal.setText("" + bundleDetailListing.getData().getMaximum_selling_price() + " AED");
            tvTotalAmount.setText("" + bundleDetailListing.getData().getValucart_price() + " AED");
            tvDiscount.setText("" + bundleDetailListing.getData().getPercentage_discount() + " %");

            rvBunderList = findViewById(R.id.rvBunderList);
            // add a divider after each item for more clarity
            //rvBunderList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            adapterRvBundleDetail = new AdapterRvBundleDetail(bundleDetailListing, this, this);


            if (library.IsTablet())
                rvBunderList.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1));
            else
                rvBunderList.setLayoutManager(new GridLayoutManager(this, 1));

            rvBunderList.setAdapter(adapterRvBundleDetail);

            library.displayImage(bundleDetailListing.getData().getImages().get(0) + "?w=275", ivBundle);

            LinearLayout llOnOffer = findViewById(R.id.llOnOffer);
            TextView  tvBundleOff = findViewById(R.id.tvBundleOff);

            if (bundleDetailListing.getData().getMaximum_selling_price().equals((bundleDetailListing.getData().getValucart_price()))) {
                llOnOffer.setVisibility(View.INVISIBLE);
                tvBundleOff.setText("");
            } else {
                llOnOffer.setVisibility(View.VISIBLE);
                tvBundleOff.setText(""+bundleDetailListing.getData().getPercentage_discount()+" % Off");
            }


        }catch (Exception e){}
    }

    private void loadCart(String action, String item_id, String item_type) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addBundleCart(action, item_id, item_type,bundleItems(), new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    if(response.getAsJsonObject().get("status").toString().equals("1")) {

                        if (Constants.cart_id != null) {
                        //if (Constants.cart_id.equals(""))
                            Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();
                    }
                    Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                    displayTotalCart(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));

                    startActivity(new Intent(getApplicationContext(), AddToCartProductScreen.class));
                    }else {
                        library.alertErrorMessage(""+response.getAsJsonObject().get("message").toString());
//                        Toast.makeText(getApplicationContext(),response.getAsJsonObject().get("message").toString(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(),jsonObject.get("message").toString(),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){}
            }
        });
    }


    public static void displayTotalCart(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        editor.apply();

    }

    public   ArrayList<String>  bundleItems(){
        //Map<String,String> params = new HashMap<>();

        //JsonObject finalobject = new JsonObject();
        ArrayList<String> bundleIdList = new ArrayList<>();
        for(int count=0;count<bundleDetailListing.getData().getProducts().size();count++){
            bundleIdList.add(""+bundleDetailListing.getData().getProducts().get(count).getData().getId());
        }
        //Gson gson = new Gson();
        //params.put("product_list", bundleIdList.toString());
        //finalobject.add("products", gson.fromJson(bundleIdList.toString(), JsonElement.class));
        return bundleIdList;
    }

}

