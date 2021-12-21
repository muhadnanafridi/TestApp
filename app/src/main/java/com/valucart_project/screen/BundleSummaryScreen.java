package com.valucart_project.screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvBundleSummary;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Grocery;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BundleSummaryScreen extends FragmentActivity implements OnItemSelection {

    private List<Grocery> groceryList = new ArrayList<>();
    private RecyclerView rvBunderList;
    private AdapterRvBundleSummary adapterRvBundleDetail;
    Library library;
    EditText etBundleName;
    ImageView ivEdit;
    String byobId;
    ByobSummary productsList = new ByobSummary();
    TextView tvTotalAmount, tvTotalCartsAmmount, tvDiscount, tvAddMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bundle_summary_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        library = new Library(this);
        ExtraBundleOnStart();
        //itemList();

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

        RelativeLayout rlAddToCart = findViewById(R.id.rlAddToCart);
        rlAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etBundleName.getText().toString().equals("Bundle1"))
                    loadChangeBundleName(etBundleName.getText().toString());

                Intent intent = new Intent(getApplicationContext(), DeliveryDateTimeScreen.class);
                intent.putExtra("CallFrom", "meetmonday");
                startActivity(intent);
                //loadCart("add", byobId, "customer_bundle");

            }
        });

        loadBYOB();
    }

    public void ExtraBundleOnStart() {

        /*Bundle extras = getIntent().getExtras();

        if (extras != null) {
            byobId = extras.getString("byobId");
        }
*/
        byobId =  Constants.bundleId;
    }


    private void itemList() {
        tvAddMore = findViewById(R.id.tvAddMore);
        tvAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.bundleId = byobId;
                Constants.BYOBFirstTimeViewScreen = false;
                Intent intent = new Intent(BundleSummaryScreen.this, SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardByob");
                startActivity(intent);
            }
        });
        tvTotalCartsAmmount = findViewById(R.id.tvTotalCartsAmmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvDiscount = findViewById(R.id.tvDiscount);

        ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBundleName.setEnabled(true);
                etBundleName.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        etBundleName = findViewById(R.id.etBundleName);
        etBundleName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loadChangeBundleName(etBundleName.getText().toString());
                    return true;
                }
                return false;
            }
        });


        etBundleName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadChangeBundleName(etBundleName.getText().toString());
                }
                return false;
            }
        });
        rvBunderList = findViewById(R.id.rvBunderList);
        // add a divider after each item for more clarity
        if (!library.IsTablet())
            rvBunderList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapterRvBundleDetail = new AdapterRvBundleSummary(productsList, this, this, byobId);

        if (library.IsTablet())
            rvBunderList.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1));
        else
            rvBunderList.setLayoutManager(new GridLayoutManager(this, 1));

        rvBunderList.setAdapter(adapterRvBundleDetail);

        tvTotalCartsAmmount.setText("AED  " + productsList.getData().getValucart_price());
        tvTotalAmount.setText("AED  " + productsList.getData().getMaximum_selling_price());
        tvDiscount.setText("AED  " + productsList.getData().getDiscount());
        etBundleName.setText("" + productsList.getData().getName());
    }


    public void removeItem(int position) {
        groceryList.remove(position);
        adapterRvBundleDetail = new AdapterRvBundleSummary(productsList, this, this, byobId);
        rvBunderList.setAdapter(adapterRvBundleDetail);
    }

    @Override
    public void onItemSelected(String value, int position) {
        if (value.equals("UpDateCart")) {
            loadBYOB();
        } else {
            alertRemoveItem(position);
        }
    }

    public void alertRemoveItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BundleSummaryScreen.this);
        builder.setTitle("Product Alert")
                .setMessage("Are you sure, you want to Delete this Item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadBYOB() {
        //{"created_at":"2019-04-23 06:58:31","id":3,"price":2.05,"discount":0,"discounted_price":2.05,"products":[{"quantity":1,"product":{"id":"wgQXJxmon3Rq","sku":"6291101131265","name":"alokozay detergent premium 110g","description":"detergent premium 110 grm","packaging_quantity":110,"maximum_selling_price":2.05,"valucart_price":2.05,"images":["http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7189 copy.jpg","http://testing.v2.api.valucart.com/img/products/6291101131265/3D0A7191 copy.jpg"],"packaging_quantity_unit":{"id":"wWx50jA6Y7gA","name":"G","symbol":"G"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/6291101131265/thumb_3D0A7190 copy.jpg"}}]}
        library.showLoading();
        APIManager.getInstance().getBYOB(byobId, new APIResponseCallback<ByobSummary>() {

            @Override
            public void onResponseLoaded(@NonNull ByobSummary response) {
                library.hideLoading();
                productsList = response;

                itemList();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }

            }

        });

    }


    private void loadChangeBundleName(String bundleName) {

        APIManager.getInstance().getChangeBundleName(byobId, bundleName, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //library.hideLoading();
                //loadCart("add", byobId, "customer_bundle");
                //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message").toString());

//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }

            }

        });

    }

    private void loadCart(String action, String item_id, String item_type) {
        library.showLoading();
        //{"status":1,"data":{"id":"5158aa71f08320f12ad58d80d4f21f5c","customer":"","price":22,"discount":0,"discounted_price":22,"item_count":1,"products":[],"bundles":[],"customer_bundles":[{"quantity":1,"item":{"id":394,"customer_id":null,"name":"Bundle","description":null,"created_at":"2019-05-18 07:12:48","maximum_selling_price":22,"discount":0,"valucart_price":22,"products":[{"quantity":1,"product":{"id":"Wx50jAmO6Y7g","sku":"8850124037350","name":"nescafe coffee instant my cup  in  sticks ns  24x20grm","description":"coffee instant my cup  in  sticks ns ","packaging_quantity":20,"maximum_selling_price":1,"valucart_price":1,"is_bulk":false,"bulk_quantity":0,"is_offer":false,"percentage_discount":0,"inventory":1204,"images":["http://testing.v2.api.valucart.com/img/products/8850124037350/Aa10219 (1859 of 2903).jpg"],"packaging_quantity_unit":{"id":"e2g90gLBJYOP","name":"GRM","symbol":"GRM"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/8850124037350/thumb_Aa10219 (1858 of 2903).jpg"}},{"quantity":1,"product":{"id":"D7v04DdL05lP","sku":"6294003571788","name":"nescafe cofee in hazelnut  xa\n 20x17gm","description":"cofee in hazelnut  xa\n","packaging_quantity":17,"maximum_selling_price":1,"valucart_price":1,"is_bulk":false,"bulk_quantity":0,"is_offer":false,"percentage_discount":0,"inventory":749,"images":["http://testing.v2.api.valucart.com/img/products/6294003571788/Aa10219 (2689 of 2903).jpg"],"packaging_quantity_unit":{"id":"RD7v04yLJ5lP","name":"GM","symbol":"GM"},"thumbnail":"http://testing.v2.api.valucart.com/img/products/6294003571788/thumb_Aa10219 (2688 of 2903).jpg"}}],"inventory":749,"thumbnail":"https://s3.eu-central-1.amazonaws.com/assets.valucart.com/customerbundle/ic_add_byob.png"}}]}}
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addCart(action, item_id, item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    if (response.getAsJsonObject().get("status").toString().equals("1")) {

                        //if (Constants.cart_id.equals(""))
                        Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();
                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                        DashboardActivity.totalCartValue(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));

                        if (Constants.userLogin)
                            loadMyBundle();

                        startActivity(new Intent(getApplicationContext(), AddToCartProductScreen.class));
                        displayTotalCart(getApplicationContext(), "");
                    } else {
                        Toast.makeText(getApplicationContext(), response.getAsJsonObject().get("message").toString(), Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                library.alertErrorMessage("Add Item more then 5 ");

//                Toast.makeText(getApplicationContext(), "Add Item more then 5 ", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void displayTotalCart(Context context, String value) {
        //Constants.bundleId = "";
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        //editor.putString("bundle_id", "");
        editor.apply();

    }

    private void loadMyBundle() {

        APIManager.getInstance().connectByobWithLogin(byobId, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage("" + jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
            }

        });

    }

}

