package com.valucart_project.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterShopByCommunityPage;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.NonSwipeableViewPager;

public class ShopByCommunityScreen extends FragmentActivity {

    RelativeLayout rlCart;
    public static String CommunityName;
    public static String CommunityId;
    public static TextView tvTitle;
    NonSwipeableViewPager viewPager;
    static TextView tvTotalCarts;
    TabLayout tabLayout;
    AdapterShopByCommunityPage adapter;
    public static String MinimumPrice = "1";
    public static String MaximumPrice = "1000";
    public static String Type = "";
    public static String Category = "";
    public static String Brand = "";
    public static String CategoryId = "";
    public static String ProductId = "";
    public static Boolean anyDataAvailableToShow = false;
    static Context activity;
    public static String CallingFrom = "";
    public static String Community = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_by_community_screen);
        activity = getApplicationContext();

        ExtraBundleOnStart();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Product"));
        tabLayout.addTab(tabLayout.newTab().setText("Bulk"));
        tabLayout.addTab(tabLayout.newTab().setText("Bundles"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#666666"));

        viewPager = (NonSwipeableViewPager) findViewById(R.id.nonSwipeablePager);
        adapter = new AdapterShopByCommunityPage
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //adapter.getItem(2);
        //adapter.notifyDataSetChanged();
        //viewPager.setCurrentItem(2);

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlCart = findViewById(R.id.rlCart);
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddToCartProductScreen.class));
            }
        });


        tvTotalCarts = findViewById(R.id.tvTotalCarts);
        displayTotalCart(getApplicationContext(), "" + Constants.totalCart);

        feachingData();
        ExtraBundleOnEnd();
    }

    public void ExtraBundleOnStart() {

        tvTitle = findViewById(R.id.tvTitle);

        makeAllEmgty();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            if (extras.getString("CallingFrom").equals("BundleCommunity")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                tvTitle.setText(CommunityName.substring(0, 1).toUpperCase() + CommunityName.substring(1));
                Community = extras.getString("Community");
            } else if (extras.getString("CallingFrom").equals("BulkCommunity")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                tvTitle.setText(CommunityName.substring(0, 1).toUpperCase() + CommunityName.substring(1));
                Community = extras.getString("Community");
            } else if (extras.getString("CallingFrom").equals("ProductsCommunity")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                Community = extras.getString("Community");
                tvTitle.setText(CommunityName.substring(0, 1).toUpperCase() + CommunityName.substring(1));
            }
        }

    }

    public void makeAllEmgty() {
        anyDataAvailableToShow = false;
        MinimumPrice = "1";
        MaximumPrice = "1000";
        Type = "";
        Category = "";
        Brand = "";
    }


    public void feachingData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString("CommunityName") != null) {
            CommunityName = extras.getString("CommunityName");
            CommunityId = extras.getString("CommunityId");
            tvTitle.setText(CommunityName.toUpperCase());
        }
    }

    public void ExtraBundleOnEnd() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if (extras.getString("CallingFrom").equals("ShopByCommunitySeeAll")) {

            }
            if (extras.getString("CallingFrom").equals("ProductsCommunity")) {
               /* adapter.getItem(0);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
                tabLayout.getTabAt(0).select();*/
            } else if (extras.getString("CallingFrom").equals("BulkCommunity")) {
                adapter.getItem(1);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1);
                tabLayout.getTabAt(1).select();
            } else if (extras.getString("CallingFrom").equals("BundleCommunity")) {
                adapter.getItem(2);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(2);
                tabLayout.getTabAt(2).select();
            }

        }

/*
        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        //viewPager.setCurrentItem(2);
                        tabLayout.getTabAt(2).select();
                    }
                }, 1000);
*/
    }

    public static void totalCartValue(String action, String id, String item_Type) {
        //subtract  add
        //loadCart(action, id, item_Type);
        displayTotalCart(activity, "" + Constants.totalCart);
    }



    public static void displayTotalCart(Context context, String value) {
        if (value.equals("0"))
            tvTotalCarts.setVisibility(View.GONE);
        else
            tvTotalCarts.setVisibility(View.VISIBLE);

        tvTotalCarts.setText("" + value);
    }


}
