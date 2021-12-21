package com.valucart_project.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.models.Addresses;
import com.valucart_project.models.Areas;
import com.valucart_project.models.Banners;
import com.valucart_project.models.BundleDetail;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Categories;
import com.valucart_project.models.CheckoutTime;
import com.valucart_project.models.CustomerBundles;
import com.valucart_project.models.Generic;
import com.valucart_project.models.Intervals;
import com.valucart_project.models.Login;
import com.valucart_project.models.MyOrder;
import com.valucart_project.models.OrderSummary;
import com.valucart_project.models.Products;
import com.valucart_project.models.ProductsAddToCart;
import com.valucart_project.models.ProductsBundle;
import com.valucart_project.models.ProductsDetails;
import com.valucart_project.models.ProductsWishList;
import com.valucart_project.models.ProfileDetail;
import com.valucart_project.models.ProfileWallet;
import com.valucart_project.models.Scheduled;
import com.valucart_project.models.Search;
import com.valucart_project.models.SubCategory;
import com.valucart_project.models.Vendors;
import com.valucart_project.models.WalletRedeme;
import com.valucart_project.models.WalletTransactions;
import com.valucart_project.models.PromoOffers;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    //Login
    @FormUrlEncoded
    @POST("oauth/token")
    Call<Login> getLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type, @Field("client_id") String client_id, @Field("scope") String scope, @Field("fcm_token") String fcm_token , @Field("device_type") String device_type);

    //Registration
    @FormUrlEncoded
    @POST("customers")
    Call<Login> getSignUp(@Field("name") String loginName, @Field("password") String loginPassword, @Field("email") String loginemail, @Field("phone_number") String phone_number, @Field("gender") String gender , @Field("agreed_to_terms") String agreed_to_terms, @Field("fcm_token") String fcm_token, @Field("device_type") String device_type);

    /*
    @POST("Login")
    Call<Login> getResidentAlbum(@Query("loginName") String loginName, @Query("loginName") int loginPassword);
    */


    @FormUrlEncoded
    @POST("customers/addresses")
    Call<JsonElement> addAddressApartment(@Header("Authorization") String Authorization ,@Field("tag") String tag, @Field("area") String area, @Field("building") String building_name, @Field("apartment_number") String apartment_number, @Field("landmark") String landmark, @Field("notes") String notes , @Field("phone_number") String phone_number, @Field("location_type") String location_type);

    @FormUrlEncoded
    @POST("customers/addresses")
    Call<JsonElement> addAddressVilla(@Header("Authorization") String Authorization ,@Field("tag") String tag, @Field("area") String area, @Field("street") String street, @Field("villa_number") String villa_number , @Field("landmark") String landmark, @Field("notes") String notes , @Field("phone_number") String phone_number, @Field("location_type") String location_type);


    @FormUrlEncoded
    @PUT("customers/addresses/{address_id}")
    Call<JsonElement> UpdateAddressApartment(@Header("Authorization") String Authorization , @Path("address_id") String address_id ,@Field("tag") String tag, @Field("area") String area, @Field("building") String building , @Field("apartment_number") String apartment_number, @Field("landmark") String landmark, @Field("notes") String notes, @Field("phone_number") String phone_number, @Field("location_type") String location_type);


    @FormUrlEncoded
    @PUT("customers/addresses/{address_id}")
    Call<JsonElement> UpdateAddressVilla(@Header("Authorization") String Authorization , @Path("address_id") String address_id ,@Field("tag") String tag, @Field("area") String area, @Field("street") String street, @Field("villa_number") String villa_number , @Field("landmark") String landmark, @Field("notes") String notes, @Field("phone_number") String phone_number, @Field("location_type") String location_type);


    @FormUrlEncoded
    @POST("customers/addresses")
    Call<JsonElement> addAddressTest(@Header("Authorization") String Authorization);


    @FormUrlEncoded
    @POST("oauth/token")
    Call<Login> getoauth(@Field("provider") String provider, @Field("token") String token, @Field("fcm_token") String fcm_token , @Field("device_type") String device_type);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @GET("logout")
    Call<Generic> getLogout(@Header("Authorization") String Authorization);

    @POST("customers/verifyEmail")
    @FormUrlEncoded
    Call<Generic> getEmailVerification(@Header("Authorization") String Authorization, @Field("code") String current_password);

    @PUT("customers/password")
    @FormUrlEncoded
    Call<Generic> getChangePassword(@Header("Authorization") String Authorization, @Field("current_password") String current_password, @Field("new_password") String new_password , @Field("new_password_confirmation") String new_password_confirmation);

    @GET("departments")
    Call<Categories> getCategory();

    @GET("subcategories")
    Call<SubCategory> getSubCategoryMain(@Query("category") String department);

    @GET("categories")
    Call<Categories> getDepartmentCategory(@Query("department") String department);


    @GET("products")
    Call<Products> getProducts(@Query("category") String categories, @Query("sorting") String sorting, @Query("price") String price , @Query("community") String community , @Query("brand") String brand,@Query("subcategory") String subcategory,@Query("department") String department, @Query("page") int page, @Query("per_page") int per_page);

    @GET("bundles")
    Call<ProductsBundle> getProductsBundle(@Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("category") String category, @Query("brand") String brand, @Query("page") int page, @Query("per_page") int per_page);

    @GET("products")
    Call<Products> getProductsByob(@Query("bundlable") String bundlable, @Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("department") String department, @Query("brand") String brand , @Query("page") int page, @Query("per_page") int per_page);

    @GET("products?meatmonday")
    Call<Products> getProductsmeatmonday(@Query("bundlable") String bundlable, @Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("department") String department, @Query("brand") String brand , @Query("page") int page, @Query("per_page") int per_page);


    @GET("products")
    Call<Products> getProductsbulk(@Query("bulk") String bulk, @Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("department") String department, @Query("brand") String brand, @Query("page") int page, @Query("q") String q, @Query("per_page") int per_page);

    @GET("products?featured")
    Call<Products> getFeatures(@Query("subcategories") String subcategories, @Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("department") String department, @Query("brand") String brand, @Query("page") int page, @Query("per_page") int per_page);

    @GET("products")
    Call<Products> getCategory(@Query("category") String category);

    @GET("banners")
    Call<Banners> getBanners();

    @GET("banners/Popular_Department_1")
    Call<Banners> getBannersPopulatDepartment1();

    @GET("banners/Popular_Department_2")
    Call<Banners> getBannersPopulatDepartment2();

    @GET("products/{productId}")
    Call<Products> getFeatures(@Path("productId") String productId);

    @GET("brands")
    Call<SubCategory> getProductsBrands();

    @GET("communities")
    Call<SubCategory> getProductsCommunities();

    //offers
    @GET("products")
    Call<Products> getOffers(@Query("offers") String offers ,@Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("category") String category, @Query("brand") String brand, @Query("page") int page, @Query("per_page") int per_page);

    @GET("products")
    Call<Products> getProductsCommunity(@Query("sorting") String sorting, @Query("price") String price , @Query("community") String community, @Query("category") String category, @Query("brand") String brand, @Query("page") int page, @Query("per_page") int per_page);

/*
    @Headers("Connection:close")
*/
/*@Headers({
        "Accept: application/json",
        "Content-Type: application/json"
})*/
    //@Headers( "Content-Type: application/json; charset=utf-8")
    @Headers({"Content-type: application/json",
        "Accept: */*"})
    @POST("customerbundles")
    Call<JsonElement> getCustomerBundles(@Header("Authorization") String Authorization ,@Body JsonObject body );

    @Headers({ "Connection:close"
            ,"Accept: application/json"})
    @POST("cart")
    @FormUrlEncoded
    Call<JsonElement> getcart(@Header("Authorization") String Authorization ,@Field("action") String action ,@Field("item_id") String item_id ,@Field("item_type") String item_type,@Field("cart_id") String cart_id);

    @GET("cart")
    Call<JsonElement> getcartAfterLogin(@Header("Authorization") String Authorization );


    @Headers({ "Connection:close"
            ,"Accept: application/json"})
    @POST("cart")
    @FormUrlEncoded
    Call<JsonElement> addBundleCart(@Header("Authorization") String Authorization ,@Field("action") String action ,@Field("item_id") String item_id ,@Field("item_type") String item_type,@Field("cart_id") String cart_id ,@Field("product_list[]") ArrayList<String> body );//,@Field("product_list") ArrayList<String> product_list);


    @Headers({ "Connection:close"
            ,"Accept: application/json"})
    @POST("wishlist")
    @FormUrlEncoded
    Call<JsonElement> addWishlist(@Header("Authorization") String Authorization , @Field("action") String action ,@Field("item_id") String item_id ,@Field("item_type") String item_type);

    @Headers("Connection:close")
    @POST("delete_customerbundle")
    @FormUrlEncoded
    Call<CustomerBundles> getBundleDeleteItem(@Header("Authorization") String Authorization ,@Field("bundle_id") String bundle_id );

//    @POST("customerbundles/bundle")
    @Headers("Connection:close")
    @POST("customerbundles")
    @FormUrlEncoded
    Call<JsonElement> getBundleByob(@Header("Authorization") String Authorization ,@Field("action") String actions ,@Field("bundle_id") String bundle_id ,@Field("product_id") String product_id);

/*
    @Headers("Connection:close")
    @POST("customerbundles")
    @FormUrlEncoded
    Call<ByobSummary> getBundleByobSummary(@Header("Authorization") String Authorization ,@Field("action") String actions ,@Field("bundle_id") String bundle_id ,@Field("product_id") String product_id);
*/

    @Headers("Connection:close")
    @POST("meatmonday")
    @FormUrlEncoded
        Call<ByobSummary> getBundleByobSummary(@Header("Authorization") String Authorization ,@Field("action") String actions ,@Field("cart_id") String cart_id ,@Field("product_id") String product_id);


    //offers
    @GET("search")
    Call<Search> getSearch(@Query("q") String q );

    @GET
    public Call<Products> productList(@Url String url);

    @GET
    public Call<ProductsDetails> productDetails(@Url String url);

    @GET("cart/{cartId}")
    Call<ProductsAddToCart> getCart(@Header("Authorization") String Authorization ,@Path("cartId") String cartId);

    @POST("cart/coupon")
    @FormUrlEncoded
    Call<ProductsAddToCart> loadCoupon(@Header("Authorization") String Authorization ,@Field("cart_id") String cart_id ,@Field("coupon") String coupon);

/*    @GET("customerbundles/{bundleId}") meatmonday
    Call<ByobSummary> getBYOB(@Path("bundleId") String bundleId);*/

    @GET("customerbundles/{bundleId}")
    Call<ByobSummary> getBYOB(@Path("bundleId") String bundleId);

    @Headers({"Content-type: application/json",
            "Accept: */*"})
    @PUT("customerbundles/{bundle_id}")
    Call<JsonElement> getBundleName(@Path("bundle_id") String bundle_id,@Body JsonObject body );

    @GET("departments")
    Call<Categories> getPopularDepartment(@Query("popular") String popular);

    @GET("areas")
    Call<Areas> getArea(@Query("state") String state);

    @GET("states")
    Call<Areas> getstates();


    @GET("customers/addresses")
    Call<Addresses> getAddresses(@Header("Authorization") String Authorization );

    @GET("wishlist")
    Call<ProductsWishList> getWishlist(@Header("Authorization") String Authorization );

    @GET("orders/schedule")
    Call<Scheduled> getScheduled(@Header("Authorization") String Authorization);

    @DELETE("orders/schedule")
    Call<JsonElement> deleteScheduled(@Header("Authorization") String Authorization, @Query("order_id") String order_id );

    @POST("wishlist/delete")
    Call<ProductsWishList> clearWishlist(@Header("Authorization") String Authorization );

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("deliverytime")
    Call<CheckoutTime> getTime(@Header("Authorization") String Authorization , @Query("for") String currentDate );

    @POST("checkout")
    @FormUrlEncoded
    Call<JsonElement> saveCheckout(@Header("Authorization") String Authorization ,@Field("cart_id") String cart_id ,@Field("delivery_date") String delivery_date,@Field("address_id") String address_id,@Field("time_slot_id") String time_slot_id);

    @POST("meatmonday/checkout")
    @FormUrlEncoded
    Call<JsonElement> saveMeetMondayCheckout(@Header("Authorization") String Authorization ,@Field("cart_id") String cart_id ,@Field("delivery_date") String delivery_date,@Field("address_id") String address_id,@Field("time_slot_id") String time_slot_id);


    @GET("checkout/{order_id}")
    Call<OrderSummary> getOrder(@Header("Authorization") String Authorization , @Path("order_id") String order_id);

    @GET("customerbundles")
    Call<CustomerBundles> getBundles(@Header("Authorization") String Authorization , @Query("page") int page, @Query("per_page") int per_page);

    @PUT("customerbundles/update/{bundle_id}")
    Call<JsonElement> connectByobWithLogin(@Header("Authorization") String Authorization , @Path("bundle_id") String bundle_id);

    @GET("customers/resendEmailCode")
    Call<JsonElement> resendEmailLogin(@Header("Authorization") String Authorization );


    @POST("checkout/payment")
    @FormUrlEncoded
    Call<JsonElement> getOrderConformation(@Header("Authorization") String Authorization,@Field("order") String order ,@Field("payment_method") String payment_method );

    @GET
    Call<JsonElement> getPaymentConformation(@Url String url );

    @GET("customers")
    Call<ProfileDetail> getProfile(@Header("Authorization") String Authorization );

    @GET("wallet ")
    Call<ProfileWallet> getWallet(@Header("Authorization") String Authorization );

    @GET("orders")
    Call<MyOrder> getorders(@Header("Authorization") String Authorization, @Query("page") int page, @Query("per_page") int per_page );

    @Headers("Connection:close")
    @POST("feedback")
    @FormUrlEncoded
    Call<JsonElement> sendFeedbackAgainstOrder(@Header("Authorization") String Authorization , @Field("message") String message ,@Field("order_id") String order_id);

    @Headers("Connection:close")
    @POST("orders/schedule")
    @FormUrlEncoded
    Call<JsonElement> sendScheduleApi(@Header("Authorization") String Authorization , @Field("interval_id") String interval_id ,@Field("order_id") String order_id,@Field("start_date") String start_date);

    @Headers("Connection:close")
    @POST("orders/reorder")
    @FormUrlEncoded
    Call<JsonElement> reOrder(@Header("Authorization") String Authorization ,@Field("order") String order_id);


    @GET("orders/schedule/intervals")
    Call<Intervals> getInterval();

    @PUT("customers")
    @FormUrlEncoded
    Call<Generic> updateProfile(@Header("Authorization") String Authorization, @Field("name") String name, @Field("email") String email , @Field("phone_number") String phone_number, @Field("gender") String gender);


    @POST("customers/account_recovery")
    @FormUrlEncoded
    Call<JsonElement> recoverEmail( @Field("username") String username );

    @POST("customers/account_recovery/recover")
    @FormUrlEncoded
    Call<JsonElement> changePassword( @Field("ref") String ref ,@Field("code") String code  ,@Field("new_password") String new_password ,@Field("new_password_confirmation") String new_password_confirmation );


    @GET("bundles/{bundleId}")
    Call<BundleDetail> getBundlesDetail(@Path("bundleId") String bundleId,@Query("with_atl_products") String with_atl_products);


    @GET("bundlecategories")
    Call<Categories> getBundleCategory();


    @GET("bundles")
    Call<ProductsBundle> getPopularBundle(@Query("popular") String popular);


    @GET("vendors")
    Call<Vendors> getVendors();

    @GET("availableoffers ")
    Call<PromoOffers> getBankPromooffers();

    @GET("versions")
    Call<JsonElement> getVersion();


    @FormUrlEncoded
    @POST("redeme")
    Call<WalletRedeme> walletRedeme(@Header("Authorization") String Authorization , @Field("code") String code);


    @GET("wallettransactions ")
    Call<WalletTransactions> getTransactions(@Header("Authorization") String Authorization );

}

