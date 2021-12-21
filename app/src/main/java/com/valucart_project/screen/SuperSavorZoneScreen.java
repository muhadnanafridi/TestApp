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
import com.valucart_project.adapter.AdapterSuperSavorZonePage;
import com.valucart_project.popups.SearchDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.NonSwipeableViewPager;

public class SuperSavorZoneScreen extends FragmentActivity implements View.OnClickListener {

    static AdapterSuperSavorZonePage adapter;
    static TabLayout tabLayout;
    static TextView tvTotalCarts;
    static NonSwipeableViewPager viewPager;
    public static ImageView ivSearch;
    RelativeLayout rlCart;
    public static Boolean anyDataAvailableToShow = false;
    public static String MinimumPrice = "1";
    public static String MaximumPrice = "1000";
    public static String Type = "";
    public static String Category = "";
    public static String Brand = "";
    public static String CategoryId = "";
    public static String ProductId = "";
    public static String Community = "";
    public static String Search = "";
    static Context activity;
    public static String CallingFrom = "";
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_savor_zone_screen);
        activity = getApplicationContext();

        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);

        ExtraBundleOnStart();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Bundles"));
        tabLayout.addTab(tabLayout.newTab().setText("Bulk"));
        tabLayout.addTab(tabLayout.newTab().setText("Meat Monday"));
        //tabLayout.addTab(tabLayout.newTab().setText("BYOB"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#666666"));

        viewPager = (NonSwipeableViewPager) findViewById(R.id.nonSwipeablePager);
        adapter = new AdapterSuperSavorZonePage
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //title = (TextView) viewPager.getChildAt(0);
        //title.setTextSize(20);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlCart = findViewById(R.id.rlCart);
        rlCart.setOnClickListener(this);

        tvTotalCarts = findViewById(R.id.tvTotalCarts);
        displayTotalCart(getApplicationContext(), "" + Constants.totalCart);
        //totalCartItem("makeItInvisable");

        ExtraBundleOnEnd();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ExtraBundleOnStart() {
        makeAllEmgty();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
            if (extras.getString("CallingFrom").equals("BundleSearch")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                Community = extras.getString("Community");
                Search = "";
            } else if (extras.getString("CallingFrom").equals("BulkSearch")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                Community = extras.getString("Community");
                Search = "";
            } else if (extras.getString("CallingFrom").equals("ByobSearch")) {
                anyDataAvailableToShow = true;
                MinimumPrice = extras.getString("MinimumPrice");
                MaximumPrice = extras.getString("MaximumPrice");
                Type = extras.getString("Type");
                Category = extras.getString("Categories");
                Brand = extras.getString("Brand");
                Community = extras.getString("Community");
                Search = "";
            } else if (extras.getString("CallingFrom").equals("MyBundle")) {
                anyDataAvailableToShow = true;
                CategoryId = extras.getString("CategoryId");
                ProductId = extras.getString("ProductId");
                Community = extras.getString("Community");
                Search = "";
            } else if (extras.getString("CallingFrom").equals("DashBoardMyBundle")) {
                Search = "";
                ivSearch.setVisibility(View.VISIBLE);
            } else if (extras.getString("CallingFrom").equals("DashBoardBulk")) {
                Search = extras.getString("search");
                ivSearch.setVisibility(View.GONE);
            } else if (extras.getString("CallingFrom").equals("DashBoardByob")) {
                Search = "";
                ivSearch.setVisibility(View.GONE);
            }
        }
    }

    public void ExtraBundleOnEnd() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if (extras.getString("CallingFrom").equals("BundleSearch")) {
               /* adapter.getItem(0);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
                tabLayout.getTabAt(0).select();*/
            } else if (extras.getString("CallingFrom").equals("BulkSearch")) {
                adapter.getItem(1);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1);
                tabLayout.getTabAt(1).select();
            } else if (extras.getString("CallingFrom").equals("ByobSearch")) {
                adapter.getItem(2);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(2);
                tabLayout.getTabAt(2).select();
            } else if (extras.getString("CallingFrom").equals("MyBundle")) {
                adapter.getItem(2);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(2);
                tabLayout.getTabAt(2).select();
            } else if (extras.getString("CallingFrom").equals("DashBoardMyBundle")) {

            } else if (extras.getString("CallingFrom").equals("DashBoardBulk")) {
                adapter.getItem(1);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1);
                tabLayout.getTabAt(1).select();
            } else if (extras.getString("CallingFrom").equals("DashBoardByob")) {
                adapter.getItem(2);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(2);
                tabLayout.getTabAt(2).select();
                ivSearch.setVisibility(View.GONE);
                tvTitle.setText("Meat Monday");
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


    @Override
    public void onClick(View v) {
        if (v == ivSearch) {
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(this);
            //residentNotesDialogFragment.show(getSupportFragmentManager(), "");
            Intent intent = new Intent(this, SearchScreen.class);
            startActivity(intent);
        }
        if (v == rlCart) {
            startActivity(new Intent(this, AddToCartProductScreen.class));
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(SuperSavorZoneScreen.this, DashboardActivity.class));
        finish();
    }

}

