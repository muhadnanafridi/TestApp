package com.valucart_project.screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.valucart_project.R;
import com.valucart_project.fragments.DashboardFragment;
import com.valucart_project.fragments.DepartmentFragment;
import com.valucart_project.fragments.MoreMenuFragment;
import com.valucart_project.fragments.MyBundleFragment;
import com.valucart_project.fragments.MyOrderFragment;
import com.valucart_project.utils.Constants;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DashboardActivity extends FragmentActivity implements View.OnClickListener {

    protected RelativeLayout btnOpenMenu;
    TextView txtTabTitle, tvHome, tvDepartment, tvMyOrder, tvBundle, tvMyProfile;
    static TextView tvTotalCarts;
    protected FragmentManager fragmentManager;
    RelativeLayout rlHome, llMyOrders, llMyBundles, llMore, rlHeaderContainer;
    public static RelativeLayout llDepartment;
    ImageView ivHome, ivDepartment, ivMyOrders, ivBundles, ivMyProfile, ivNotifications, ivTitle;
    String tag = "";
    protected Fragment fragment = null;
    View viewHome, viewDepartment, viewMyOrder, viewMyBundles, viewMore;
    LinearLayout llOffers, llCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_dashboard_header);

        initFields();

        llCart = findViewById(R.id.llCart);
        llCart.setOnClickListener(this);

        String tag = "";

        fragment = null;
        fragment = new DashboardFragment();
        setTabName("Dashboard");
        tag = "Dashboard";
        if (fragment != null)
            inflate(1, fragment, tag);

        initField();

        //String value = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences prefs = getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE);

        if (!prefs.getString("TotalCart", null).equals("")) {
            Constants.totalCart = Integer.parseInt(prefs.getString("TotalCart", null));
            Constants.cart_id = prefs.getString("cart_id", null).equals("") ? "" : prefs.getString("cart_id", null);
            tvTotalCarts.setVisibility(View.VISIBLE);
            tvTotalCarts.setText("" + Constants.totalCart);
        }
        Constants.bundleId = prefs.getString("bundle_id", null).equals("") ? "" : prefs.getString("bundle_id", null);
        if (!prefs.getString("token", null).equals("")) {
            Constants.access_token = prefs.getString("token", null);
            Constants.userLogin = true;
        }

        totalCartValue(getApplicationContext(), "" + Constants.totalCart);

        deepLinkingHandler();
    }

    public void deepLinkingHandler() {

        if (Constants.facebookAds) {
            if (Constants.linkingType.equals("offer")) {
                Intent intent = new Intent(this, ProductListingScreen.class);
                intent.putExtra("CallingFrom", "OffersProducts");
                startActivity(intent);
                Constants.facebookAds = false;
            } else if (Constants.linkingType.equals("product_department")) {
                Intent intent = new Intent(this, ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", Constants.linkingName);
                intent.putExtra("SubCategoryId", "");
                intent.putExtra("Categories", Constants.linkingId);
                startActivity(intent);
                Constants.facebookAds = false;
            } else if (Constants.linkingType.equals("byob")) {
                Intent intent = new Intent(this, SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardByob");
                startActivity(intent);
                Constants.facebookAds = false;
            } else if (Constants.linkingType.equals("bundleDetail")) {
                Intent intent = new Intent(this, BundleDetailScreen.class);
                intent.putExtra("id", "" + Constants.linkingId);
                intent.putExtra("inventory", "1");
                startActivity(intent);
                Constants.facebookAds = false;
            } else if (Constants.linkingType.equals("bundleCategory")) {
                Intent intent = new Intent(this, SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardMyBundle");
                startActivity(intent);
                Constants.facebookAds = false;
            } else if (Constants.linkingType.equals("bulk")) {
                Intent intent = new Intent(this, SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardBulk");
                startActivity(intent);
                Constants.facebookAds = false;
            }
        }

    }

    public void initFields() {

        llOffers = findViewById(R.id.llOffers);
        llOffers.setOnClickListener(this);

        rlHeaderContainer = findViewById(R.id.rlHeaderContainer);

        fragmentManager = getSupportFragmentManager();


        txtTabTitle = findViewById(R.id.txtTabTitle);

        tvTotalCarts = findViewById(R.id.tvTotalCarts);

        btnOpenMenu = findViewById(R.id.rlMenuButton);
        btnOpenMenu.setOnClickListener(this);

        ivNotifications = findViewById(R.id.ivNotifications);
        ivNotifications.setOnClickListener(this);

        ivTitle = findViewById(R.id.ivTitle);

        viewHome = findViewById(R.id.viewHome);
        viewDepartment = findViewById(R.id.viewDepartment);
        viewMyOrder = findViewById(R.id.viewMyOrder);
        viewMyBundles = findViewById(R.id.viewMyBundles);
        viewMore = findViewById(R.id.viewMore);


        tvHome = findViewById(R.id.tvHome);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvMyOrder = findViewById(R.id.tvMyOrder);
        tvBundle = findViewById(R.id.tvBundle);
        tvMyProfile = findViewById(R.id.tvMyProfile);

    }


    protected void inflate(int fragmentHolderLayoutId, Fragment fragment, String tag) {

        FragmentTransaction fragTrans = fragmentManager.beginTransaction();
        fragTrans.replace(R.id.FragmentContainer, fragment, tag);
        fragTrans.commit();
        fragTrans.addToBackStack(tag);

    }


    public void setTabName(String tabName) {

        rlHeaderContainer.setVisibility(View.VISIBLE);

        if (tabName.equals("Dashboard")) {
            ivTitle.setVisibility(View.VISIBLE);
            txtTabTitle.setVisibility(View.GONE);
        } else {
            ivTitle.setVisibility(View.GONE);
            txtTabTitle.setVisibility(View.VISIBLE);
            txtTabTitle.setText(tabName);

            if (tabName.equals("More")) {
                rlHeaderContainer.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (llOffers == v) {
            Intent intent = new Intent(this, ProductListingScreen.class);
            intent.putExtra("CallingFrom", "OffersProducts");
            startActivity(intent);
        }

        if (btnOpenMenu == v) {

        }

        if (llCart == v) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
        }

        if (ivNotifications == v) {
            Toast.makeText(this, "No notification available", Toast.LENGTH_LONG).show();
        }


        if (rlHome == v) {
            fragment = null;
            fragment = new DashboardFragment();
            setTabName("Dashboard");
            tag = "Dashboard";
            if (fragment != null)
                inflate(1, fragment, tag);
            ivHome.setColorFilter(Color.parseColor("#8506ff"));
            ivDepartment.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyOrders.setColorFilter(Color.parseColor("#a8a8a8"));
            ivBundles.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyProfile.setColorFilter(Color.parseColor("#a8a8a8"));
            viewHome.setBackgroundColor(Color.parseColor("#8506ff"));
            viewDepartment.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyOrder.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyBundles.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMore.setBackgroundColor(Color.parseColor("#ffffff"));

            tvHome.setTextColor(Color.parseColor("#8506ff"));
            tvDepartment.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyOrder.setTextColor(Color.parseColor("#a8a8a8"));
            tvBundle.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyProfile.setTextColor(Color.parseColor("#a8a8a8"));
        }

        if (llDepartment == v) {
            fragment = null;
            fragment = new DepartmentFragment();
            setTabName("Departments");
            tag = "Departments";
            if (fragment != null)
                inflate(2, fragment, tag);
            ivHome.setColorFilter(Color.parseColor("#a8a8a8"));
            ivDepartment.setColorFilter(Color.parseColor("#8506ff"));
            ivMyOrders.setColorFilter(Color.parseColor("#a8a8a8"));
            ivBundles.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyProfile.setColorFilter(Color.parseColor("#a8a8a8"));

            viewHome.setBackgroundColor(Color.parseColor("#ffffff"));
            viewDepartment.setBackgroundColor(Color.parseColor("#8506ff"));
            viewMyOrder.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyBundles.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMore.setBackgroundColor(Color.parseColor("#ffffff"));

            tvHome.setTextColor(Color.parseColor("#a8a8a8"));
            tvDepartment.setTextColor(Color.parseColor("#8506ff"));
            tvMyOrder.setTextColor(Color.parseColor("#a8a8a8"));
            tvBundle.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyProfile.setTextColor(Color.parseColor("#a8a8a8"));

        }

        if (llMyOrders == v) {
            fragment = null;
            fragment = new MyOrderFragment();
            setTabName("My Order");
            tag = "My Order";
            if (fragment != null)
                inflate(3, fragment, tag);
            ivHome.setColorFilter(Color.parseColor("#a8a8a8"));
            ivDepartment.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyOrders.setColorFilter(Color.parseColor("#8506ff"));
            ivBundles.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyProfile.setColorFilter(Color.parseColor("#a8a8a8"));
            //startActivity(new Intent(getApplicationContext(), MyOrderFragment.class));

            viewHome.setBackgroundColor(Color.parseColor("#ffffff"));
            viewDepartment.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyOrder.setBackgroundColor(Color.parseColor("#8506ff"));
            viewMyBundles.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMore.setBackgroundColor(Color.parseColor("#ffffff"));

            tvHome.setTextColor(Color.parseColor("#a8a8a8"));
            tvDepartment.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyOrder.setTextColor(Color.parseColor("#8506ff"));
            tvBundle.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyProfile.setTextColor(Color.parseColor("#a8a8a8"));
        }

        if (llMyBundles == v) {
            fragment = null;
            fragment = new MyBundleFragment();
            setTabName("My Bundle");
            tag = "My Bundle";
            if (fragment != null)
                inflate(4, fragment, tag);
            ivHome.setColorFilter(Color.parseColor("#a8a8a8"));
            ivDepartment.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyOrders.setColorFilter(Color.parseColor("#a8a8a8"));
            ivBundles.setColorFilter(Color.parseColor("#8506ff"));
            ivMyProfile.setColorFilter(Color.parseColor("#a8a8a8"));

            viewHome.setBackgroundColor(Color.parseColor("#ffffff"));
            viewDepartment.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyOrder.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyBundles.setBackgroundColor(Color.parseColor("#8506ff"));
            viewMore.setBackgroundColor(Color.parseColor("#ffffff"));

            tvHome.setTextColor(Color.parseColor("#a8a8a8"));
            tvDepartment.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyOrder.setTextColor(Color.parseColor("#a8a8a8"));
            tvBundle.setTextColor(Color.parseColor("#8506ff"));
            tvMyProfile.setTextColor(Color.parseColor("#a8a8a8"));
        }

        if (llMore == v) {
            fragment = null;
            fragment = new MoreMenuFragment();
            setTabName("More");
            tag = "More";
            if (fragment != null)
                inflate(5, fragment, tag);
            ivHome.setColorFilter(Color.parseColor("#a8a8a8"));
            ivDepartment.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyOrders.setColorFilter(Color.parseColor("#a8a8a8"));
            ivBundles.setColorFilter(Color.parseColor("#a8a8a8"));
            ivMyProfile.setColorFilter(Color.parseColor("#8506ff"));

            viewHome.setBackgroundColor(Color.parseColor("#ffffff"));
            viewDepartment.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyOrder.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMyBundles.setBackgroundColor(Color.parseColor("#ffffff"));
            viewMore.setBackgroundColor(Color.parseColor("#8506ff"));

            tvHome.setTextColor(Color.parseColor("#a8a8a8"));
            tvDepartment.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyOrder.setTextColor(Color.parseColor("#a8a8a8"));
            tvBundle.setTextColor(Color.parseColor("#a8a8a8"));
            tvMyProfile.setTextColor(Color.parseColor("#8506ff"));
        }

    }


    public void initField() {
        rlHome = findViewById(R.id.rlHome);
        rlHome.setOnClickListener(this);
        llDepartment = findViewById(R.id.llDepartment);
        llDepartment.setOnClickListener(this);
        llMyOrders = findViewById(R.id.llMyOrders);
        llMyOrders.setOnClickListener(this);
        llMyBundles = findViewById(R.id.llMyBundles);
        llMyBundles.setOnClickListener(this);
        llMore = findViewById(R.id.llMore);
        llMore.setOnClickListener(this);

        ivHome = findViewById(R.id.ivHome);
        ivDepartment = findViewById(R.id.ivDepartment);
        ivMyOrders = findViewById(R.id.ivMyOrders);
        ivBundles = findViewById(R.id.ivBundles);
        ivMyProfile = findViewById(R.id.ivMyProfile);
    }

    int backPressCheck = 1;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (backPressCheck % 3 == 0)
            alertRemoveItem();

        backPressCheck = backPressCheck + 1;
    }

    public static void totalCartValue(Context context, String value) {
        if (value.equals("0"))
            tvTotalCarts.setVisibility(View.GONE);
        else
            tvTotalCarts.setVisibility(View.VISIBLE);

        tvTotalCarts.setText("" + value);

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.apply();
    }

    public void alertRemoveItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ValuCart Alert")
                .setMessage("Are you sure, you want to close ValuCart App")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
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

}

