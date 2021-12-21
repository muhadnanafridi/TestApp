package com.valucart_project.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvBankPromoList;
import com.valucart_project.adapter.AdapterRvDepartments;
import com.valucart_project.adapter.AdapterRvFeaturesHorizantalList;
import com.valucart_project.adapter.AdapterRvHorizantalPopularBundleList;
import com.valucart_project.adapter.AdapterRvHorizantalVendors;
import com.valucart_project.adapter.AdapterRvPopolarDepartment;
import com.valucart_project.adapter.SliderAdapterBanner;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Banners;
import com.valucart_project.models.Categories;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Products;
import com.valucart_project.models.ProductsBundle;
import com.valucart_project.models.ProductsDetails;
import com.valucart_project.models.PromoOffers;
import com.valucart_project.models.SliderItem;
import com.valucart_project.models.SubCategory;
import com.valucart_project.models.Vendors;
import com.valucart_project.popups.BankPromoDialogFragment;
import com.valucart_project.popups.PromoCodeDialog;
import com.valucart_project.screen.BundleDetailScreen;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.screen.ProductDetailScreen;
import com.valucart_project.screen.ProductListingScreen;
import com.valucart_project.screen.SearchFilterScreen;
import com.valucart_project.screen.SearchScreen;
import com.valucart_project.screen.ShopByCommunityScreen;
import com.valucart_project.screen.SuperSavorZoneScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ss.com.bannerslider.Slider;

public class DashboardFragment extends Fragment implements View.OnClickListener, WishItemSelection, OnItemSelection {

    Slider slider, slider2;
    View view;
    private List<Grocery> communitiesList = new ArrayList<>();
    private List<Grocery> departmentList = new ArrayList<>();
    private RecyclerView rvFeaturedItems, rvPopularBundles, rvShopByCommunityCategories, rvPopularDepartments, rvShopByCommunity, rvVendors;
    private AdapterRvFeaturesHorizantalList adapterFeaturesHorizantalList;
    private AdapterRvPopolarDepartment categoriesAdapter;
    AdapterRvDepartments adapterRvDepartments;
    LinearLayout llSuperSavorZone, llSearch;
    TextView tvSeeAllFeaturedItems, tvSeeAllPopularBundles, tvSeeAllPopularDepartments, tvSeeAllShopByCommunity, tvNoRecordAvailableFeatured;
    ImageView ivAdvanceSearch, ivBundles, ivBulk, ivByob;
    ScrollView svProductDetail;
    String shopBycommunitySelected = "Emirati", shopBycommunityId = "";
    Library library;
    public static Products featureProductsList = new Products();
    static Products productsCommunityList = new Products();
    static SubCategory communityList = new SubCategory();
    static Categories popularDepartment = new Categories();
    static ProductsBundle productPopularBundle = new ProductsBundle();
    AdapterRvHorizantalPopularBundleList adapterRvHorizantalPopularBundleList;
    static Banners bannersList = new Banners();
    static Banners bannersPopularDepartment1 = new Banners();
    static Banners bannersPopularDepartment2 = new Banners();
    static Vendors vendorsList = new Vendors();
    static PromoOffers promoOffers = new PromoOffers();
    AdapterRvHorizantalVendors adapterRvHorizantalVendors;
    static Date nextDate = new Date();
    Date oldDate = new Date();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String toyBornTime = dateFormat.format(nextDate);
    Boolean CallFroomlinkDeep = false;
    String popupCoupon = "show";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        library = new Library(getContext());

        initFields();
        popupCoupon = "show";
        CallFroomlinkDeep = false;
        if (Constants.facebookAds) {
            CallFroomlinkDeep = true;
            popupCoupon = "notshow";
            if (Constants.linkingType.equals("product")) {
                loadProductDetail("https://v2api.valucart.com/products/" + Constants.linkingId);
                Constants.facebookAds = false;
            }
        }

        displayRecord();

        return view;
    }

    public void initFields() {
        rvShopByCommunity = view.findViewById(R.id.rvShopByCommunity);

        tvNoRecordAvailableFeatured = view.findViewById(R.id.tvNoRecordAvailableFeatured);

        tvSeeAllShopByCommunity = view.findViewById(R.id.tvSeeAllShopByCommunity);
        tvSeeAllShopByCommunity.setOnClickListener(this);

        llSuperSavorZone = view.findViewById(R.id.llSuperSavorZone);

        ivBundles = view.findViewById(R.id.ivBundles);
        ivBundles.setOnClickListener(this);

        ivBulk = view.findViewById(R.id.ivBulk);
        ivBulk.setOnClickListener(this);

        ivByob = view.findViewById(R.id.ivByob);
        ivByob.setOnClickListener(this);

        tvSeeAllFeaturedItems = view.findViewById(R.id.tvSeeAllFeaturedItems);
        tvSeeAllFeaturedItems.setOnClickListener(this);

        tvSeeAllPopularBundles = view.findViewById(R.id.tvSeeAllPopularBundles);
        tvSeeAllPopularBundles.setOnClickListener(this);

        tvSeeAllPopularDepartments = view.findViewById(R.id.tvSeeAllPopularDepartments);
        tvSeeAllPopularDepartments.setOnClickListener(this);

        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);

        ivAdvanceSearch = view.findViewById(R.id.ivAdvanceSearch);
        ivAdvanceSearch.setOnClickListener(this);

        svProductDetail = view.findViewById(R.id.svProductDetail);
        svProductDetail.fullScroll(View.FOCUS_UP);
        svProductDetail.smoothScrollTo(0, 0);
        svProductDetail.postDelayed(new Runnable() {
            @Override
            public void run() {
                svProductDetail.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 1000);

        ImageView ivWhatsSpp = view.findViewById(R.id.ivWhatsSpp);
        ivWhatsSpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
    }

    public void displayRecord() {
        updateApi();
        if (bannersList.getData().size() == 0) {
            loadBanners();
            loadBannersPopulatDepartment1();
            loadBannersPopulatDepartment2();
        } else {
            setupSlideViews();
            setpopularDepartmentBanner1();
            setpopularDepartmentBanner2();
        }

        if (featureProductsList.getData().size() != 0) {
            if (!CallFroomlinkDeep)
                library.showLoading();
            featureItem(featureProductsList);
            communicatesNames();
            shopByCommunityItem();
            popularDepartments(popularDepartment);
            popularBundlesItem();
            loadVendor();
            displayBankPromoOffers();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    library.hideLoading();
                }
            }, 500);
        } else {
            loadFeatures();
        }
    }

    private void loadProductDetail(String url) {

        library.showLoading();
        APIManager.getInstance().getProductDetail(url, new APIResponseCallback<ProductsDetails>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsDetails response) {
                library.hideLoading();

                Intent intent = new Intent(getContext(), ProductDetailScreen.class);
                intent.putExtra("id", "" + response.getData().getId());
                intent.putExtra("name", "" + response.getData().getName());
                intent.putExtra("subcategory_id", "" + response.getData().getCategory().getId());
                intent.putExtra("description", "" + response.getData().getDescription());
                intent.putExtra("packaging_quantity", "" + response.getData().getPackaging_quantity());
                intent.putExtra("valucart_price", "" + response.getData().getValucart_price());
                intent.putExtra("maximum_selling_price", "" + response.getData().getMaximum_selling_price());
                intent.putStringArrayListExtra("ImagesList", response.getData().getImages());
                intent.putExtra("thumbnail", "" + response.getData().getThumbnail());
                if (response.getData().getPackaging_quantity_unit() != null) {
                    if (response.getData().getIs_bulk() && response.getData().getBulk_quantity() > 0)
                        intent.putExtra("packaging_quantity_unit_Symbal", "" + response.getData().getPackaging_quantity_unit().getSymbol() + " X " + response.getData().getBulk_quantity());
                    else
                        intent.putExtra("packaging_quantity_unit_Symbal", "" + response.getData().getPackaging_quantity_unit().getSymbol() + "");
                }
                intent.putExtra("inventory", "" + response.getData().getInventory());
                startActivity(intent);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }

    SliderView sliderView;
    private SliderAdapterBanner adapter;

    private void setupSlideViews() {

        sliderView = view.findViewById(R.id.imageSlider);

        adapter = new SliderAdapterBanner(getContext(),this);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        if(bannersList.getData().size()>0) {
            for(int counter=0;counter<bannersList.getData().size();counter++) {
                SliderItem sliderItem = new SliderItem();
                sliderItem.setDescription("");
                sliderItem.setImageUrl(bannersList.getData().get(counter).getPortrait() + "?w=500");
                adapter.addItem(sliderItem);
            }
        }

       /* Slider.init(new PicassoImageLoadingService(getContext()));

        slider = view.findViewById(R.id.banner_slider1);
        slider.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider.setAdapter(new AdapterMainBannerSlider(bannersList));
                slider.setSelectedSlide(0);
                slider.setInterval(5000);
                slider.setOnSlideClickListener(new OnSlideClickListener() {
                    @Override
                    public void onSlideClick(int position) {

                        if (bannersList.getData().get(position).getResource_type().equals("product_department")) {
                            Intent intent = new Intent(getContext(), ProductListingScreen.class);
                            intent.putExtra("CallingFrom", "Departments");
                            intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                            intent.putExtra("SubCategoryId", "");
                            intent.putExtra("Categories", bannersList.getData().get(position).getResource_identifier());
                            startActivity(intent);
                        } else if (bannersList.getData().get(position).getResource_type().equals("product_category")) {
                            Intent intent = new Intent(getContext(), ProductListingScreen.class);
                            intent.putExtra("CallingFrom", "Departments");
                            intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                            intent.putExtra("SubCategoryId", bannersList.getData().get(position).getResource_identifier());
                            intent.putExtra("Categories", "");
                            startActivity(intent);
                        } else if (bannersList.getData().get(position).getResource_type().equals("product")) {
                            loadProductDetail(bannersList.getData().get(position).getHref());
                        } else if (bannersList.getData().get(position).getResource_type().equals("bundle")) {
                            Intent intent = new Intent(getContext(), BundleDetailScreen.class);
                            intent.putExtra("id", "" + bannersList.getData().get(position).getResource_identifier());
                            intent.putExtra("inventory", "1");
                            startActivity(intent);
                        }

                    }
                });

            }
        }, 100);

        slider2 = view.findViewById(R.id.banner_slider2);
        slider2.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider2.setAdapter(new AdapterMainBannerSlider(bannersList));
                slider2.setSelectedSlide(0);
                slider2.setInterval(5000);
                slider2.setOnSlideClickListener(new OnSlideClickListener() {
                    @Override
                    public void onSlideClick(int position) {

                        if (bannersList.getData().get(position).getResource_type().equals("product_department")) {
                            Intent intent = new Intent(getContext(), ProductListingScreen.class);
                            intent.putExtra("CallingFrom", "Departments");
                            intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                            intent.putExtra("SubCategoryId", "");
                            intent.putExtra("Categories", bannersList.getData().get(position).getResource_identifier());
                            startActivity(intent);
                        } else if (bannersList.getData().get(position).getResource_type().equals("product_category")) {
                            Intent intent = new Intent(getContext(), ProductListingScreen.class);
                            intent.putExtra("CallingFrom", "Departments");
                            intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                            intent.putExtra("SubCategoryId", bannersList.getData().get(position).getResource_identifier());
                            intent.putExtra("Categories", "");
                            startActivity(intent);
                        } else if (bannersList.getData().get(position).getResource_type().equals("product")) {
                            loadProductDetail(bannersList.getData().get(position).getHref());
                        } else if (bannersList.getData().get(position).getResource_type().equals("bundle")) {
                            Intent intent = new Intent(getContext(), BundleDetailScreen.class);
                            intent.putExtra("id", "" + bannersList.getData().get(position).getResource_identifier());
                            intent.putExtra("inventory", "1");
                            startActivity(intent);
                        }


                    }
                });


            }
        }, 100);*/

    }


    @Override
    public void onClick(View v) {

        if (ivBundles == v) {
            Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom", "DashBoardMyBundle");
            startActivity(intent);
        }
        if (ivBulk == v) {
            Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom", "DashBoardBulk");
            intent.putExtra("search", "");
            startActivity(intent);
        }
        if (ivByob == v) {
            Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom", "DashBoardByob");
            startActivity(intent);
        }
        if (tvSeeAllFeaturedItems == v) {

            Intent intent = new Intent(getContext(), ProductListingScreen.class);
            //intent.putExtra("CallingFrom", "FeatureProducts");
            intent.putExtra("CallingFrom", "OffersProducts");
            //intent.putExtra("subcategory_id", "");
            startActivity(intent);
        }
        if (tvSeeAllPopularBundles == v) {
            Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
            intent.putExtra("CallingFrom", "DashBoardMyBundle");
            startActivity(intent);
        }
        if (llSearch == v) {
            //SearchDialogFragment residentNotesDialogFragment = new SearchDialogFragment(getContext());
            //residentNotesDialogFragment.show(getFragmentManager(), "");
            Intent intent = new Intent(getContext(), SearchScreen.class);
            startActivity(intent);
        }
        if (ivAdvanceSearch == v) {
            startActivity(new Intent(getContext(), SearchFilterScreen.class));
        }
        if (tvSeeAllPopularDepartments == v) {
            DashboardActivity.llDepartment.performClick();
        }
        if (tvSeeAllShopByCommunity == v) {
            Intent intent = new Intent(getContext(), ShopByCommunityScreen.class);
            intent.putExtra("CommunityName", shopBycommunitySelected);
            intent.putExtra("CommunityId", shopBycommunityId);
            intent.putExtra("CallingFrom", "ShopByCommunitySeeAll");
            startActivity(intent);
        }

    }

    @Override
    public void onWishValueSelected(String name, int position, String action) {
        if (name.equals("popularBundles")) {
            library.apiLoadItemToWishlist(action, productPopularBundle.getData().get(position).getId(), "bundle");
        } else if (name.equals("ShopByCommunity")) {
            library.apiLoadItemToWishlist(action, productsCommunityList.getData().get(position).getId(), "product");
        } else if (name.equals("features")) {
            library.apiLoadItemToWishlist(action, featureProductsList.getData().get(position).getId(), "product");
        }
    }

    @Override
    public void onItemSelected(String name, int position) {

        if (name.equals("ShopByCommunityCategory")) {

            if (communitiesList.get(position).getSelectedItem()) {
                communitiesList.get(position).setSelectedItem(false);
            } else {
                if (communitiesList.size() > 0)
                    communitiesList.clear();

                for (int counter = 0; counter < communityList.getData().size(); counter++) {
                    communitiesList.add(new Grocery(communityList.getData().get(counter).getName().toUpperCase(), communityList.getData().get(counter).getId(), communityList.getData().get(counter).getIcon(), false));
                }

                for (int counter = 0; counter < communitiesList.size(); counter++) {
                    if (counter == position) {
                        communitiesList.get(position).setSelectedItem(true);
                        shopBycommunitySelected = communitiesList.get(position).getProductName();
                        shopBycommunityId = communityList.getData().get(position).getId();
                    }
                }
            }

            if (categoriesAdapter == null) {
                categoriesAdapter = new AdapterRvPopolarDepartment(communitiesList, getContext(), this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvShopByCommunityCategories.setLayoutManager(horizontalLayoutManager);
                rvShopByCommunityCategories.setAdapter(categoriesAdapter);
            } else
                categoriesAdapter.notifyDataSetChanged();

            loadProductsCommunity(communityList.getData().get(position).getId());

        } else if (name.equals("popularBundles")) {
            Intent intent = new Intent(getContext(), BundleDetailScreen.class);
            intent.putExtra("id", "" + productPopularBundle.getData().get(position).getId());
            intent.putExtra("inventory", "" + productPopularBundle.getData().get(position).getInventory());
            startActivity(intent);
        } else if (name.equals("ShopByCommunity")) {
            Intent intent = new Intent(getContext(), ProductDetailScreen.class);
            intent.putExtra("id", "" + productsCommunityList.getData().get(position).getId());
            intent.putExtra("name", "" + productsCommunityList.getData().get(position).getName());
            intent.putExtra("subcategory_id", "" + productsCommunityList.getData().get(position).getCategory().getId());
            intent.putExtra("description", "" + productsCommunityList.getData().get(position).getDescription());
            intent.putExtra("packaging_quantity", "" + productsCommunityList.getData().get(position).getPackaging_quantity());
            intent.putExtra("valucart_price", "" + productsCommunityList.getData().get(position).getValucart_price());
            intent.putExtra("maximum_selling_price", "" + productsCommunityList.getData().get(position).getMaximum_selling_price());
            intent.putStringArrayListExtra("ImagesList", productsCommunityList.getData().get(position).getImages());
            intent.putExtra("packaging_quantity_unit_Symbal", "" + productsCommunityList.getData().get(position).getPackaging_quantity_unit().getSymbol() + "");
            intent.putExtra("thumbnail", "" + productsCommunityList.getData().get(position).getThumbnail());
            intent.putExtra("inventory", "" + productsCommunityList.getData().get(position).getInventory());
            startActivity(intent);
        } else if (name.equals("features")) {
            Intent intent = new Intent(getContext(), ProductDetailScreen.class);
            intent.putExtra("id", "" + featureProductsList.getData().get(position).getId());
            intent.putExtra("name", "" + featureProductsList.getData().get(position).getName());
            intent.putExtra("subcategory_id", "" + featureProductsList.getData().get(position).getCategory().getId());
            intent.putExtra("description", "" + featureProductsList.getData().get(position).getDescription());
            intent.putExtra("packaging_quantity", "" + featureProductsList.getData().get(position).getPackaging_quantity());
            intent.putExtra("valucart_price", "" + featureProductsList.getData().get(position).getValucart_price());
            intent.putExtra("maximum_selling_price", "" + featureProductsList.getData().get(position).getMaximum_selling_price());
            intent.putStringArrayListExtra("ImagesList", featureProductsList.getData().get(position).getImages());
            if (featureProductsList.getData().get(position).getIs_bulk() && featureProductsList.getData().get(position).getBulk_quantity() > 0)
                intent.putExtra("packaging_quantity_unit_Symbal", "" + featureProductsList.getData().get(position).getPackaging_quantity_unit().getSymbol() + " * " + featureProductsList.getData().get(position).getBulk_quantity());
            else
                intent.putExtra("packaging_quantity_unit_Symbal", "" + featureProductsList.getData().get(position).getPackaging_quantity_unit().getSymbol() + "");
            intent.putExtra("thumbnail", "" + featureProductsList.getData().get(position).getThumbnail());
            intent.putExtra("inventory", "" + featureProductsList.getData().get(position).getInventory());
            startActivity(intent);
        } else if (name.equals("Department")) {
            Intent intent = new Intent(getContext(), ProductListingScreen.class);
            intent.putExtra("CallingFrom", "Departments");
            intent.putExtra("DepartmentCategoryName", popularDepartment.getData().get(position).getName());
            intent.putExtra("Categories", popularDepartment.getData().get(position).getId());
            intent.putExtra("SubCategoryId", "");
            startActivity(intent);
        } else if (name.equals("promoCode")) {
            bankPromoPopup(position);
        } else if (name.equals("banner")) {
            if (bannersList.getData().get(position).getResource_type().equals("product_department")) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                intent.putExtra("SubCategoryId", "");
                intent.putExtra("Categories", bannersList.getData().get(position).getResource_identifier());
                startActivity(intent);
            } else if (bannersList.getData().get(position).getResource_type().equals("product_category")) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", bannersList.getData().get(position).getName());
                intent.putExtra("SubCategoryId", bannersList.getData().get(position).getResource_identifier());
                intent.putExtra("Categories", "");
                startActivity(intent);
            } else if (bannersList.getData().get(position).getResource_type().equals("product")) {
                loadProductDetail(bannersList.getData().get(position).getHref());
            } else if (bannersList.getData().get(position).getResource_type().equals("bundle")) {
                Intent intent = new Intent(getContext(), BundleDetailScreen.class);
                intent.putExtra("id", "" + bannersList.getData().get(position).getResource_identifier());
                intent.putExtra("inventory", "1");
                startActivity(intent);
            }
        } else {
            startActivity(new Intent(getContext(), ProductDetailScreen.class));
        }
    }

    public void popularDepartments(Categories response) {
        departmentsNames(response);
        rvPopularDepartments = view.findViewById(R.id.rvPopularDepartments);
        adapterRvDepartments = new AdapterRvDepartments(departmentList, getContext(), this, "Department");
        //LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //rvPopularDepartments.setLayoutManager(horizontalLayoutManager);
        rvPopularDepartments.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvPopularDepartments.setAdapter(adapterRvDepartments);
        adapterRvDepartments.notifyDataSetChanged();


        LinearLayout llBaby = view.findViewById(R.id.llBaby);
        llBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Baby");
                intent.putExtra("Categories", "oPy86LPZ6kVe");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });
        LinearLayout llPantry = view.findViewById(R.id.llPantry);
        llPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Pantry");
                intent.putExtra("Categories", "b8Mr6olw0lR5");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        LinearLayout llstationery = view.findViewById(R.id.llstationery);
        llstationery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Stationery");
                intent.putExtra("Categories", "mxXOJmBknWv3");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        LinearLayout llFruitVeg = view.findViewById(R.id.llFruitVeg);
        llFruitVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Vegetables & Fruits");
                intent.putExtra("Categories", "Gbg56GmrJmlR");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        LinearLayout llOrganic = view.findViewById(R.id.llOrganic);
        llOrganic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Organic");
                intent.putExtra("Categories", "l7aW6pWon48o");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        LinearLayout llDairyMilk = view.findViewById(R.id.llDairyMilk);
        llDairyMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Dairy and Milk");
                intent.putExtra("Categories", "exBrJlM5n28a");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        ImageView ivMeat = view.findViewById(R.id.ivMeat);
        ivMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Meat & Poultry");
                intent.putExtra("Categories", "x3ePJ89En854");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);*/
                Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardByob");
                startActivity(intent);
            }
        });

        /*LinearLayout llPetCare = view.findViewById(R.id.llPetCare);
        llPetCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Pets");
                intent.putExtra("Categories", "r3xB0Z9z6K8a");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });

        LinearLayout llHouseHold = view.findViewById(R.id.llHouseHold);
        llHouseHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", "Household Essentials");
                intent.putExtra("Categories", "xa2m0Rm86XW8");
                intent.putExtra("SubCategoryId", "");
                startActivity(intent);
            }
        });*/


        ImageView ivOffer = view.findViewById(R.id.ivOffer);
        ivOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "OffersProducts");
                startActivity(intent);
            }
        });

    }

    private void departmentsNames(Categories response) {
        for (int counter = 0; counter < response.getData().size(); counter++) {
            departmentList.add(new Grocery(response.getData().get(counter).getName(), response.getData().get(counter).getId(), response.getData().get(counter).getIcon(), response.getData().get(counter).getImage(), false));
        }
    }

    private void loadBanners() {
        APIManager.getInstance().getBanners(new APIResponseCallback<Banners>() {

            @Override
            public void onResponseLoaded(@NonNull Banners response) {
                bannersList = response;
                setupSlideViews();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });
    }

    private void loadBannersPopulatDepartment1() {
        APIManager.getInstance().getBannerPopulatDepartment1(new APIResponseCallback<Banners>() {

            @Override
            public void onResponseLoaded(@NonNull Banners response) {
                bannersPopularDepartment1 = response;
                setpopularDepartmentBanner1();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {

                } catch (Exception e) {}
            }

        });
    }

    ImageView ivPopularDepartment2;
    private void loadBannersPopulatDepartment2() {
        APIManager.getInstance().getBannerPopulatDepartment2(new APIResponseCallback<Banners>() {

            @Override
            public void onResponseLoaded(@NonNull Banners response) {
                bannersPopularDepartment2 = response;
                 setpopularDepartmentBanner2();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {

                } catch (Exception e) {
                }
            }

        });
    }

    public void setpopularDepartmentBanner2(){
        ivPopularDepartment2 = view.findViewById(R.id.ivPopularDepartment2);
        library.displayImage(bannersPopularDepartment2.getData().get(0).getPortrait() + "?w=500",ivPopularDepartment2);
        ivPopularDepartment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", bannersPopularDepartment2.getData().get(0).getName());
                intent.putExtra("SubCategoryId", "");
                intent.putExtra("Categories", bannersPopularDepartment2.getData().get(0).getResource_identifier());
                startActivity(intent);
            }
        });

    }

    public void setpopularDepartmentBanner1(){

        ImageView ivPopularDepartment1 = view.findViewById(R.id.ivPopularDepartment1);
        library.displayImage(bannersPopularDepartment1.getData().get(0).getPortrait() + "?w=500",ivPopularDepartment1);
        ivPopularDepartment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductListingScreen.class);
                intent.putExtra("CallingFrom", "Departments");
                intent.putExtra("DepartmentCategoryName", bannersPopularDepartment1.getData().get(0).getName());
                intent.putExtra("SubCategoryId", "");
                intent.putExtra("Categories", bannersPopularDepartment1.getData().get(0).getResource_identifier());
                startActivity(intent);
            }
        });
    }


    private void loadFeatures() {
        tvNoRecordAvailableFeatured.setVisibility(View.VISIBLE);
        if (!CallFroomlinkDeep)
            library.showLoading(); //getFeatures getOffers
        APIManager.getInstance().getFeatures("", "", "", "", "", "", 1, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                //library.hideLoading();
                featureProductsList = response;
                tvNoRecordAvailableFeatured.setVisibility(View.GONE);
                featureItem(response);

                loadCommunity();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }

    private void featureItem(Products featureProducts) {
        rvFeaturedItems = view.findViewById(R.id.rvFeaturedItems);
        adapterFeaturesHorizantalList = new AdapterRvFeaturesHorizantalList(featureProducts.getData(), getContext(), "features", this, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedItems.setLayoutManager(horizontalLayoutManager);
        rvFeaturedItems.setAdapter(adapterFeaturesHorizantalList);
        adapterFeaturesHorizantalList.notifyDataSetChanged();
    }


    private void loadCommunity() {

        APIManager.getInstance().getProductsCommunities(new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                library.hideLoading();
                communityList = response;
                if (communityList.getData().size() > 0) {
                    communicatesNames();
                    loadProductsCommunity(communityList.getData().get(0).getId());
                }

                loadPopularDepartment();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }
            }

        });

    }

    private void loadProductsCommunity(String id) {

        //library.showLoading();
        APIManager.getInstance().getProductsCommunity("", "", id, "", "", 1, new APIResponseCallback<Products>() {

            @Override
            public void onResponseLoaded(@NonNull Products response) {
                productsCommunityList = response;
                if (productsCommunityList.getData().size() > 0) {
                    rvShopByCommunity.setVisibility(View.VISIBLE);
                    shopByCommunityItem();
                } else {
                    rvShopByCommunity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }

    private void loadPopularDepartment() {

        //library.showLoading();
        APIManager.getInstance().getPopularDepartment(new APIResponseCallback<Categories>() {

            @Override
            public void onResponseLoaded(@NonNull Categories response) {
                library.hideLoading();
                popularDepartment = response;
                popularDepartments(popularDepartment);
                loadPopularBundle();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }
            }

        });

    }


    private void communicatesNames() {
        communitiesList.clear();
        shopBycommunitySelected = communityList.getData().get(0).getName();
        shopBycommunityId = communityList.getData().get(0).getId();
        for (int counter = 0; counter < communityList.getData().size(); counter++) {
            communitiesList.add(new Grocery(communityList.getData().get(counter).getName().toUpperCase(), communityList.getData().get(counter).getId(), communityList.getData().get(counter).getIcon(), counter == 0 ? true : false));
        }

        if (categoriesAdapter == null) {
            //shopBycommunitySelected = "Emirati";
            rvShopByCommunityCategories = view.findViewById(R.id.rvShopByCommunityCategories);
            categoriesAdapter = new AdapterRvPopolarDepartment(communitiesList, getContext(), this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvShopByCommunityCategories.setLayoutManager(horizontalLayoutManager);
            rvShopByCommunityCategories.setAdapter(categoriesAdapter);
        } else
            categoriesAdapter.notifyDataSetChanged();
    }


    private void shopByCommunityItem() {

        //rvShopByCommunity.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterFeaturesHorizantalList = new AdapterRvFeaturesHorizantalList(productsCommunityList.getData(), getContext(), "ShopByCommunity", this, this);
        rvShopByCommunity.setLayoutManager(horizontalLayoutManager1);
        rvShopByCommunity.setAdapter(adapterFeaturesHorizantalList);

    }


    private void loadPopularBundle() {

        APIManager.getInstance().getPopularBundle(new APIResponseCallback<ProductsBundle>() {

            @Override
            public void onResponseLoaded(@NonNull ProductsBundle response) {
                library.hideLoading();
                productPopularBundle = response;
                popularBundlesItem();
                loadVendor();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }


    private void popularBundlesItem() {
        rvPopularBundles = view.findViewById(R.id.rvPopularBundles);
        //rvPopularBundles.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        adapterRvHorizantalPopularBundleList = new AdapterRvHorizantalPopularBundleList(productPopularBundle, getContext(), "popularBundles", this, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPopularBundles.setLayoutManager(horizontalLayoutManager);
        rvPopularBundles.setAdapter(adapterRvHorizantalPopularBundleList);


        ImageView ivBanner = view.findViewById(R.id.ivBanner);
        ivBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SuperSavorZoneScreen.class);
                intent.putExtra("CallingFrom", "DashBoardMyBundle");
                startActivity(intent);
            }
        });
    }

    private void loadVendor() {

        APIManager.getInstance().getVendors(new APIResponseCallback<Vendors>() {

            @Override
            public void onResponseLoaded(@NonNull Vendors response) {
                vendorsList = response;
                VendorsItem();
                loadBankPromooffers();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }

    private void VendorsItem() {

        rvVendors = view.findViewById(R.id.rvVendors);
        adapterRvHorizantalVendors = new AdapterRvHorizantalVendors(vendorsList, getContext(), "Vendors");
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvVendors.setLayoutManager(horizontalLayoutManager);
        rvVendors.setAdapter(adapterRvHorizantalVendors);

        if (popupCoupon.equals("show")) {
            //promoCode();
        } else {
            popupCoupon = "show";
        }

    }

    public void promoCode() {
        try {
            if (!Constants.userLogin && Constants.showPromo) {
                PromoCodeDialog promoCodeDialog = new PromoCodeDialog(getContext());
                promoCodeDialog.show(getFragmentManager(), "");
                Constants.showPromo = false;
            }
        } catch (Exception e) {
        }
    }


    public void openWhatsApp() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+971506393959&text=hi"));
        startActivity(browserIntent);
    }

    public void updateApi() {
        long next = nextDate.getTime();
        long difference = next - oldDate.getTime();
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        if (hours >= 1) {
            toyBornTime = dateFormat.format(nextDate);
            DashboardFragment.featureProductsList = new Products();
            //Toast.makeText(getContext(),""+hours,Toast.LENGTH_LONG).show();
        }//else
        //   Toast.makeText(getContext(),""+hours,Toast.LENGTH_LONG).show();
    }

    private void loadBankPromooffers() {

        APIManager.getInstance().getBankPromooffers(new APIResponseCallback<PromoOffers>() {

            @Override
            public void onResponseLoaded(@NonNull PromoOffers response) {
                promoOffers = response;
                displayBankPromoOffers();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage("" + jsonObject.get("message".toString()));
                } catch (Exception e) {
                }

            }

        });

    }

    AdapterRvBankPromoList adapterRvBankPromoList;

    public void displayBankPromoOffers() {
        RecyclerView rvBankPromoCode = view.findViewById(R.id.rvBankPromoCode);

        GridLayoutManager gridLayoutManager = null;
        adapterRvBankPromoList = new AdapterRvBankPromoList(promoOffers.getData(), getContext(), this);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvBankPromoCode.setLayoutManager(gridLayoutManager);

        rvBankPromoCode.setAdapter(adapterRvBankPromoList);
    }

    BottomSheetDialog bottomSheetDialog;

    public void bankPromoPopup(final int position) {

        BankPromoDialogFragment phoneVerificationDialogFragment = new BankPromoDialogFragment(getContext(), "registration", promoOffers.getData().get(position));
        phoneVerificationDialogFragment.show(getFragmentManager(), "promotion");

           /* View view = getLayoutInflater().inflate(R.layout.bottom_sheet_bank_promo, null);
            ImageView ivPromo = view.findViewById(R.id.ivPromo);

            TextView tvCode = view.findViewById(R.id.tvCode);
            tvCode.setText("Promo Code : " + promoOffers.getData().get(position).getCode());

            TextView tvDesc = view.findViewById(R.id.tvDesc);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDesc.setText(Html.fromHtml(promoOffers.getData().get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvDesc.setText(Html.fromHtml(promoOffers.getData().get(position).getDescription()));
            }

            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText("" + promoOffers.getData().get(position).getTitle());

            library.displayImage(promoOffers.getData().get(position).getImage() + "?w=151", ivPromo);

            RelativeLayout rlDone = view.findViewById(R.id.rlDone);
            rlDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", promoOffers.getData().get(position).getCode());
                    clipboard.setPrimaryClip(clip);
                }
            });

            ImageView ivCancel = view.findViewById(R.id.ivCancel);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();*/

    }


}

