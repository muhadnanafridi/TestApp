package com.valucart_project.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvMyBundles;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.CustomerBundles;
import com.valucart_project.screen.BundleSummaryScreen;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.screen.LoginScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class MyBundleFragment extends Fragment implements OnItemSelection {

    private RecyclerView rvMyBundle;
    private AdapterRvMyBundles adapterRvMyBundles;
    View view;
    Library library;
    CustomerBundles customerBundlesList;
    TextView tvNoRecordAvailable;
    LinearLayout llLoginFirst;
    ProgressBar pbBYOB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_bundles_fragment, null);

        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        rvMyBundle = view.findViewById(R.id.rvMyBundle);

        library = new Library(getContext());
        tvNoRecordAvailable = view.findViewById(R.id.tvNoRecordAvailable);
        tvNoRecordAvailable.setVisibility(View.GONE);

        llLoginFirst = view.findViewById(R.id.llLoginFirst);

        if(Constants.userLogin) {
            llLoginFirst.setVisibility(View.GONE);
            GetBundlesApi(1);
        } else {
            rvMyBundle.setVisibility(View.GONE);
            llLoginFirst.setVisibility(View.VISIBLE);
        }

        RelativeLayout rlLogin = view.findViewById(R.id.rlLogin);
        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginScreen.class));
            }
        });

        return view;
    }



    public void initInterface() {
        itemList(1);
    }
    int currentPageNumber = 0;
    int firstTimeAvoid = 0;

    private void itemList(final int pageNumber) {
        if (customerBundlesList.getData().size() == 0) {
            tvNoRecordAvailable.setVisibility(View.VISIBLE);
        } else
            tvNoRecordAvailable.setVisibility(View.GONE);

        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        if (pageNumber == 1) {
            adapterRvMyBundles = new AdapterRvMyBundles(customerBundlesList, getContext(), this);

            if (library.IsTablet())
                rvMyBundle.setLayoutManager(new GridLayoutManager(getContext(), library.getOrientationLandScape(getContext()) ? 2 : 1));
            else
                rvMyBundle.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvMyBundle.setAdapter(adapterRvMyBundles);
        }else {
            adapterRvMyBundles.notifyDataSetChanged();
        }

        firstTimeAvoid = 0;
        rvMyBundle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rvMyBundle.canScrollVertically(1)) {

                    if (pageNumber < customerBundlesList.getMeta().getLast_page()) {
                        if (firstTimeAvoid == 1) {
                            if (pageNumber > currentPageNumber) {
                                currentPageNumber = pageNumber;
                                GetBundlesApi(pageNumber + 1);
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


    @Override
    public void onItemSelected(String value, int position) {
        if (value.equals("EditBundle")) {
            Intent intent = new Intent(getContext(), BundleSummaryScreen.class);
            intent.putExtra("byobId", "" + customerBundlesList.getData().get(position).getId());
            startActivity(intent);
            /*Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom","MyBundle");
            intent.putExtra("CategoryId",groceryList.get(position).getProductName());
            intent.putExtra("ProductId",groceryList.get(position).getProductName());
            startActivity(intent);*/
        }else if (value.equals("DeleteBundle")) {
            loadRemoveItem(""+customerBundlesList.getData().get(position).getId());
        } else {
            loadCart("add", "" + customerBundlesList.getData().get(position).getId(), "customer_bundle");
            //Toast.makeText(getContext(), "Added Successfully to Add to Cart", Toast.LENGTH_LONG).show();
        }
    }

    int currentPage;
    private void GetBundlesApi(int pageNumber) {
        currentPage = pageNumber;
        if (currentPage > 1)
            pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        APIManager.getInstance().getBundles(pageNumber,new APIResponseCallback<CustomerBundles>() {

            @Override
            public void onResponseLoaded(@NonNull CustomerBundles response) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                //myOrderList=response;
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        customerBundlesList.getData().add(response.getData().get(counter));
                    }
                    itemList(currentPage);
                } else {
                    customerBundlesList = response;
                    if(customerBundlesList.getData().size()>0) {
                        rvMyBundle.setVisibility(View.VISIBLE);
                        itemList(currentPage);
                    }
                    else {
                        rvMyBundle.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                tvNoRecordAvailable.setVisibility(View.VISIBLE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    //loadCart("add",byobId,"customer_bundle");
    private void loadCart(String action, String item_id, String item_type) {

        library.showLoading();
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addCart(action, item_id, item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    if (response.getAsJsonObject().get("status").toString().equals("1")) {
                        Toast.makeText(getContext(), "Added Successfully to Add to Cart", Toast.LENGTH_LONG).show();
                        if (Constants.cart_id != null) {
                            //if (Constants.cart_id.equals(""))
                                Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();
                        }
                        //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                        DashboardActivity.totalCartValue(getContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                        displayTotalCart(getContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                    } else {
                        Toast.makeText(getContext(), response.getAsJsonObject().get("message").toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
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
    public static void emptyTotalCart(Context context ){

        Constants.bundleId = "";
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("bundle_id", "");
        editor.apply();

    }

    private void loadRemoveItem(final String id) {
        library.showLoading();
        APIManager.getInstance().addRemoveItem(id, new APIResponseCallback<CustomerBundles>() {

            @Override
            public void onResponseLoaded(@NonNull CustomerBundles response) {
                try {

                    library.hideLoading();
                    if(Constants.bundleId.equals(id))
                        emptyTotalCart(getContext());

                    customerBundlesList = response;
                    //Constants.bundleId = "" + customerBundlesList.getData().get(0).getId();
                    itemList(1);

                    Constants.totalCart = response.getItem_count();

                    DashboardActivity.totalCartValue(getContext(), "" +response.getItem_count());
                    displayTotalCart(getContext(), "" + response.getItem_count());

                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.hideLoading();
                    //Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){}
            }

        });

    }

}

