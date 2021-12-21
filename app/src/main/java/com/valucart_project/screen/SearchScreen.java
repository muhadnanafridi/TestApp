package com.valucart_project.screen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvPopularSearch;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.ProductsDetails;
import com.valucart_project.models.Search;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchScreen extends Activity implements OnItemSelection {

    private boolean isViewOnly;
    private RecyclerView  rvPopularSearch;
    AdapterRvPopularSearch adapterRvPopularSearch;
    public static Library library;
    ProgressBar progressBar;
    EditText etSearch;
    TextView tvPopularSearch;
    Search searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog_fragment);
        library = new Library(this);
        initView();
    }

    private void initView() {

        //((TextView) view.findViewById(R.id.tvTypeofPad)).setText(bowelData.getTypes_of_pad_used());
        //((TextView) view.findViewById(R.id.tvStoolType)).setText(bowelData.getStool_type());
        rvPopularSearch = findViewById(R.id.rvPopularSearch);
        findViewById(R.id.ivCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        etSearch = (EditText) findViewById(R.id.etSearch);

        etSearch.requestFocus();
        //hideKeyBoard();

        ImageView ivAdvanceDetail = findViewById(R.id.ivAdvanceDetail);
        ivAdvanceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(getApplicationContext(), SearchFilterScreen.class));
            }
        });

        tvPopularSearch = findViewById(R.id.tvPopularSearch);

        searchHandler();
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {}
    }

    public void searchHandler() {
        etSearch.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(final Editable s) {
                        //int search =0;
                        if (s.length() > 2) {
                            progressBar.setVisibility(View.VISIBLE);
                            tvPopularSearch.setVisibility(View.GONE);
                            rvPopularSearch.setVisibility(View.GONE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (s.length() > 2)
                                        loadSearch(s.toString());
                                    else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        tvPopularSearch.setVisibility(View.VISIBLE);
                                        tvPopularSearch.setText("No Record Available");
                                        rvPopularSearch.setVisibility(View.GONE);
                                    }
                                }
                            }, 2000);
                            //search =3;
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvPopularSearch.setVisibility(View.VISIBLE);
                            tvPopularSearch.setText("No Record Available");
                            rvPopularSearch.setVisibility(View.GONE);
                        }

                    }
                }
        );
    }

    @Override
    public void onItemSelected(String value, int position) {
        if (value.equals("products")) {
            Intent intent = new Intent(getApplicationContext(), ProductListingSearchScreen.class);
            intent.putExtra("CallingFrom","SearchProductslist");
            intent.putExtra("href",searchList.getData().get(position).getHref());
            startActivity(intent);
        } else if(value.equals("product")){
            loadProductDetail(searchList.getData().get(position).getHref());
        }else {
            Intent intent = new Intent(this, SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom", "DashBoardBulk");
            intent.putExtra("search", ""+etSearch.getText().toString());
            startActivity(intent);
        }
    }

    private void loadSearch(final String search ) {

        //library.showLoading();
        APIManager.getInstance().getSearch(search , new APIResponseCallback<Search>() {

            @Override
            public void onResponseLoaded(@NonNull Search response) {
                searchList = response;
                progressBar.setVisibility(View.GONE);
                rvPopularSearch.setVisibility(View.VISIBLE);
                popularSearchItem();
                if(searchList.getData().size()==1) {
                    tvPopularSearch.setVisibility(View.VISIBLE);
                    tvPopularSearch.setText(Html.fromHtml("<b>"+search+"</b>" + " have No Record Available "  ));
                }else if(searchList.getData().size()>0) {
                    tvPopularSearch.setVisibility(View.GONE);
                }else {
                    tvPopularSearch.setVisibility(View.VISIBLE);
                    tvPopularSearch.setText("No Record Available");
                    rvPopularSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                progressBar.setVisibility(View.GONE);
                tvPopularSearch.setVisibility(View.VISIBLE);
                tvPopularSearch.setText("No Record Available");
                rvPopularSearch.setVisibility(View.GONE);
            }

        });

    }

    private void popularSearchItem() {
       // if(adapterRvPopularSearch==null) {

            /*try {
                rvPopularSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            } catch (Exception e) {}*/

            adapterRvPopularSearch = new AdapterRvPopularSearch(searchList, this, "PopularSearch", this);
            if (library.IsTablet())
                rvPopularSearch.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(this) ? 2 : 1));
            else
                rvPopularSearch.setLayoutManager(new GridLayoutManager(this, 1));

            rvPopularSearch.setAdapter(adapterRvPopularSearch);
        //}else
        //    adapterRvPopularSearch.notifyDataSetChanged();
    }

    private void loadProductDetail(String url ) {

        library.showLoading();
        APIManager.getInstance().getProductDetail(url , new APIResponseCallback<ProductsDetails>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsDetails response) {
                library.hideLoading();
                Intent intent = new Intent(getApplicationContext(), ProductDetailScreen.class);
                intent.putExtra("id",""+response.getData().getId());
                intent.putExtra("name",""+response.getData().getName());
                intent.putExtra("subcategory_id",""+response.getData().getCategory().getId());
                intent.putExtra("description",""+response.getData().getDescription());
                intent.putExtra("packaging_quantity",""+response.getData().getPackaging_quantity());
                intent.putExtra("valucart_price",""+response.getData().getValucart_price());
                intent.putExtra("maximum_selling_price",""+response.getData().getMaximum_selling_price());
                intent.putStringArrayListExtra("ImagesList",response.getData().getImages());
                intent.putExtra("thumbnail",""+response.getData().getThumbnail());
                if(response.getData().getPackaging_quantity_unit()!=null) {
                    if(response.getData().getIs_bulk() && response.getData().getBulk_quantity()>0)
                        intent.putExtra("packaging_quantity_unit_Symbal",""+response.getData().getPackaging_quantity_unit().getSymbol()+" X "+response.getData().getBulk_quantity());
                    else
                        intent.putExtra("packaging_quantity_unit_Symbal",""+response.getData().getPackaging_quantity_unit().getSymbol()+"");
                }
                intent.putExtra("inventory",""+response.getData().getInventory());
                startActivity(intent);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    //library.alertErrorMessage(""+jsonObject.get("message".toString()));

                   Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
        //https://v2api.valucart.com/products/xBNJMADdJEjO
    }

}

