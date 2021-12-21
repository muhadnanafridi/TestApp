package com.valucart_project.webservices;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valucart_project.interfaces.APIResponseCallback;
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
import com.valucart_project.rest.ApiClient;
import com.valucart_project.rest.ApiInterface;
import com.valucart_project.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;

public class APIManager {

    private static APIManager apiManager;
    private ApiInterface apiService;

    private APIManager() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public static APIManager getInstance() {
        if (apiManager == null) {
            apiManager = new APIManager();
        }

        return apiManager;
    }

    public void loginUser(String userName, String userPassword, String grant_type, String client_id, String scope,String fcm_token, APIResponseCallback<Login> callback) {
        Call<Login> call = apiService.getLogin(userName, userPassword, grant_type, client_id, scope,fcm_token,"android");
        call.enqueue(callback);
    }


    public void getSignUp(String userName, String userPassword, String userEmail, String userPhoneNumber, String userGender,String fcm_token, APIResponseCallback<Login> callback) {
        Call<Login> call = apiService.getSignUp(userName, userPassword, userEmail, userPhoneNumber, userGender.toLowerCase(),"yes",fcm_token,"android");
        call.enqueue(callback);
    }

    public void getFacebookGoogle(String name, String token,String fcm_token ,APIResponseCallback<Login> callback) {
        //Call<Login> call = apiService.getoauth(name, URLEncoder.encode(token));
        Call<Login> call = apiService.getoauth(name, token,fcm_token,"android");
        call.enqueue(callback);
    }

    public void getLogout(String token, APIResponseCallback<Generic> callback) {
        Call<Generic> call = apiService.getLogout("Bearer "+token);
        call.enqueue(callback);
    }

    public void getEmailVerification(String token ,String code, APIResponseCallback<Generic> callback) {
        Call<Generic> call = apiService.getEmailVerification("Bearer "+token,code  );
        call.enqueue(callback);
    }

    public void getChangePassword(String token, String currentPasword,String newPassword, APIResponseCallback<Generic> callback) {
        Call<Generic> call = apiService.getChangePassword("Bearer "+token,currentPasword ,newPassword,newPassword );
        call.enqueue(callback);
    }


    public void getCategories(APIResponseCallback<Categories> callback) {
        Call<Categories> call = apiService.getCategory();
        call.enqueue(callback);
    }

    public void getMainCategories(String id, APIResponseCallback<SubCategory> callback) {
        Call<SubCategory> call = apiService.getSubCategoryMain(id);
        call.enqueue(callback);
    }


    public void getCategories(String id, APIResponseCallback<Categories> callback) {
        Call<Categories> call = apiService.getDepartmentCategory(id);
        call.enqueue(callback);
    }

    public void getProducts(String categoryId, String sort, String price, String communityId,String categoryID , String brandId,String department,int page, APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getProducts(categoryId, sort, price, communityId,brandId,categoryID,department,page,14);
        call.enqueue(callback);
    }

    public void getProductsSearch(String subCategoryId, String sort, String price, APIResponseCallback<Products> callback) {
        //Call<Products> call = apiService.getProducts(subCategoryId, sort, price, communityId,categoryID,brandId);
        //call.enqueue(callback);
    }


    public void getProductsBundles( String sort, String price, String communityId,String categoryID , String brandId ,int page, APIResponseCallback<ProductsBundle> callback) {
        Call<ProductsBundle> call = apiService.getProductsBundle( sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }

    public void getProductsBulk(String bundle, String sort, String price, String communityId,String categoryID , String brandId ,int page ,String search , APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getProductsbulk(bundle, sort, price, communityId,categoryID,brandId,page,search,14);
        call.enqueue(callback);
    }

    public void getProductsByob(String bundle, String sort, String price, String communityId,String categoryID , String brandId ,int page , APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getProductsByob(bundle, sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }

    public void getProductsMondayMeat(String bundle, String sort, String price, String communityId,String categoryID , String brandId ,int page , APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getProductsmeatmonday(bundle, sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }

    public void getFeatures(String id, String sort, String price, String communityId,String categoryID , String brandId ,int page, APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getFeatures(id, sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }


    public void getBanners(APIResponseCallback<Banners> callback) {
        Call<Banners> call = apiService.getBanners();
        call.enqueue(callback);
    }

    public void getBannerPopulatDepartment1(APIResponseCallback<Banners> callback) {
        Call<Banners> call = apiService.getBannersPopulatDepartment1();
        call.enqueue(callback);
    }

    public void getBannerPopulatDepartment2(APIResponseCallback<Banners> callback) {
        Call<Banners> call = apiService.getBannersPopulatDepartment2();
        call.enqueue(callback);
    }


    public void getCategory(String id, APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getCategory(id);
        call.enqueue(callback);
    }

    public void getProductsBrands(APIResponseCallback<SubCategory> callback) {
        Call<SubCategory> call = apiService.getProductsBrands();
        call.enqueue(callback);
    }

    public void getProductsCommunities(APIResponseCallback<SubCategory> callback) {
        Call<SubCategory> call = apiService.getProductsCommunities();
        call.enqueue(callback);
    }

    public void getOffers(String offerID , String sort, String price, String communityId,String categoryID , String brandId,int page , APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getOffers(offerID,sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }

    public void getProductsCommunity(String sort, String price, String communityId,String categoryID , String brandId ,int page ,  APIResponseCallback<Products> callback) {
        Call<Products> call = apiService.getProductsCommunity(sort, price, communityId,categoryID,brandId,page,14);
        call.enqueue(callback);
    }

    public void addBYOBToCard(JsonObject productIdList, APIResponseCallback<JsonElement> callback) {
        //WRequest request = new WRequest();
        //request.data.put("products", productIdList.toString());

        //Call<Generic> call = apiService.getCustomerBundles( new WRequest("products", productIdList));
        //URLEncoder.encode(micView.getText().toString().trim(), "UTF-8")
        String token="";
        if(!Constants.access_token.equals(""))
             token ="Bearer "+Constants.access_token;

        Call<JsonElement> call = null;
        call = apiService.getCustomerBundles(token,productIdList);
        call.enqueue(callback);
    }

    public void addCart(String action, String item_id, String item_type, APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.getcart("Bearer "+Constants.access_token,action, item_id, item_type, Constants.cart_id);
        call.enqueue(callback);
    }

    public void getCartDetailAfterLogin( APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.getcartAfterLogin("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void addBundleCart(String action, String item_id, String item_type, ArrayList<String>  bundleArray, APIResponseCallback<JsonElement> callback) {
        //ArrayListBundle arrayListBundle = new ArrayListBundle(bundleArray);

        Call<JsonElement> call = apiService.addBundleCart("Bearer "+Constants.access_token,action, item_id, item_type, Constants.cart_id,bundleArray);
        call.enqueue(callback);
    }

    public class ArrayListBundle{
        @SerializedName("product_list")
        @Expose
        private ArrayList<String> product_list;
        public ArrayListBundle(ArrayList<String> product_list) {
            this.product_list=product_list;
        }
    }

    public void addWishlist(String action, String item_id, String item_type, APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.addWishlist("Bearer "+Constants.access_token,action, item_id, item_type);
        call.enqueue(callback);
    }


    public void addRemoveItem(String item_id, APIResponseCallback<CustomerBundles> callback) {
        Call<CustomerBundles> call = apiService.getBundleDeleteItem("Bearer "+Constants.access_token, item_id);
        call.enqueue(callback);
    }

    public void addByob(String action, String product_id, String byob_id, APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.getBundleByob("Bearer "+Constants.access_token,action, byob_id, product_id);
        call.enqueue(callback);
    }

    public void addByobSummary(String action, String product_id, String byob_id, APIResponseCallback<ByobSummary> callback) {
        Call<ByobSummary> call = apiService.getBundleByobSummary("Bearer "+Constants.access_token,action, byob_id, product_id);
        call.enqueue(callback);
    }

    public void getSearch(String searchKeyword, APIResponseCallback<Search> callback) {
        Call<Search> call = apiService.getSearch(searchKeyword);
        call.enqueue(callback);
    }

    public void getProductList(String url, String sort, String price, APIResponseCallback<Products> callback) {

        Call<Products> call = apiService.productList(url);
        call.enqueue(callback);

    }

    public void getProductDetail(String url, APIResponseCallback<ProductsDetails> callback) {
        Call<ProductsDetails> call = apiService.productDetails(url);
        call.enqueue(callback);
    }

    public void getMyCart(String cartId, APIResponseCallback<ProductsAddToCart> callback) {
        Call<ProductsAddToCart> call = apiService.getCart("Bearer "+Constants.access_token,cartId);
        call.enqueue(callback);
    }

    public void loadCoupon(String cartId,String coupon, APIResponseCallback<ProductsAddToCart> callback) {
        Call<ProductsAddToCart> call = apiService.loadCoupon("Bearer "+Constants.access_token,cartId,coupon);
        call.enqueue(callback);
    }

    public void getBYOB(String bundleId, APIResponseCallback<ByobSummary> callback) {
        Call<ByobSummary> call = apiService.getBYOB(bundleId);
        call.enqueue(callback);
    }

    public void getChangeBundleName(String id , String nameItems, APIResponseCallback<JsonElement> callback) {
        try {
            JsonObject object = new JsonObject();
            Gson gson = new Gson();
            //object.add("name", gson.fromJson( URLEncoder.encode(nameItems.replaceAll(" ", "%20"), "UTF-8"), JsonElement.class));
            object.add("name", gson.fromJson( android.net.Uri.encode(nameItems, "UTF-8"), JsonElement.class));
            //object.add("name", gson.fromJson(URLEncoder.encode(nameItems, "UTF-8"), JsonElement.class));
            Call<JsonElement> call = apiService.getBundleName(id, object);
            call.enqueue(callback);
        }catch (Exception e){}
    }

    public void getPopularDepartment(APIResponseCallback<Categories> callback) {
        Call<Categories> call = apiService.getPopularDepartment("");
        call.enqueue(callback);
    }


    public void getArea(String id , APIResponseCallback<Areas> callback) {
        Call<Areas> call = apiService.getArea(id);
        call.enqueue(callback);
    }

    public void getStates(APIResponseCallback<Areas> callback) {
        Call<Areas> call = apiService.getstates();
        call.enqueue(callback);
    }

    public void saveAddress(String userName, String area, String street, String building, String floor,String apartment,String landmark ,String notes ,String phone , String ApartmentVilla , APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call;
        if(ApartmentVilla.equals("apartment"))
            call = apiService.addAddressApartment("Bearer "+Constants.access_token,userName, area, building,apartment,landmark,notes,phone,ApartmentVilla);
        else
            call = apiService.addAddressVilla("Bearer "+Constants.access_token,userName, area, street, floor.toLowerCase(),landmark,notes,phone,ApartmentVilla);
        //Call<JsonElement> call = apiService.addAddressTest("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void updateAddress(String id ,String userName, String area, String street, String building, String floor,String apartment,String landmark ,String notes ,String phone, String ApartmentVilla , APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call;
        if(ApartmentVilla.equals("apartment"))
            call = apiService.UpdateAddressApartment("Bearer "+Constants.access_token,id,userName, area, building.toLowerCase(),apartment,landmark,notes,phone,ApartmentVilla);
        else
            call = apiService.UpdateAddressVilla("Bearer "+Constants.access_token,id,userName, area, street, floor.toLowerCase(),landmark,notes,phone,ApartmentVilla);

        call.enqueue(callback);
    }

    public void getAddress( APIResponseCallback<Addresses> callback ) {
        Call<Addresses> call = apiService.getAddresses("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void getWishList( APIResponseCallback<ProductsWishList> callback ) {
        Call<ProductsWishList> call = apiService.getWishlist("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void getMySchedule( APIResponseCallback<Scheduled> callback ) {
        Call<Scheduled> call = apiService.getScheduled("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void deleteMySchedule(String orderId, APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.deleteScheduled("Bearer "+Constants.access_token,orderId);
        call.enqueue(callback);
    }

    public void clearWishlist( APIResponseCallback<ProductsWishList> callback ) {
        Call<ProductsWishList> call = apiService.clearWishlist("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }


    public void getTime( String selectedDate , APIResponseCallback<CheckoutTime> callback ) {
        Call<CheckoutTime> call = apiService.getTime("Bearer "+Constants.access_token,selectedDate);
        call.enqueue(callback);
    }

    public void getcheckout( String callFrom ,String cart_id , String delivery_date , String address_id , String time_slot_id , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call;
        if(callFrom.equals("meetmonday")){
             call = apiService.saveMeetMondayCheckout("Bearer "+Constants.access_token,cart_id,delivery_date,address_id,time_slot_id);
        }else {
            call = apiService.saveCheckout("Bearer "+Constants.access_token,cart_id,delivery_date,address_id,time_slot_id);
        }
        call.enqueue(callback);
    }


    public void getOrder( String orderId , APIResponseCallback<OrderSummary> callback ) {
        Call<OrderSummary> call = apiService.getOrder("Bearer "+Constants.access_token,orderId);
        call.enqueue(callback);
    }

    public void getBundles( int pageNumber ,APIResponseCallback<CustomerBundles> callback ) {
        Call<CustomerBundles> call = apiService.getBundles("Bearer "+Constants.access_token,pageNumber,14);
        call.enqueue(callback);
    }


    public void connectByobWithLogin(String bundleId, APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.connectByobWithLogin("Bearer "+Constants.access_token,bundleId);
        call.enqueue(callback);
    }

    public void resendEmail(APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.resendEmailLogin("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void  getOrderPayment( String orderId ,String payment_method , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.getOrderConformation("Bearer "+Constants.access_token,orderId,payment_method);
        call.enqueue(callback);
    }


    public void getPayment( String url , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.getPaymentConformation(url);
        call.enqueue(callback);
    }

    public void getProfile( APIResponseCallback<ProfileDetail> callback ) {
        Call<ProfileDetail> call = apiService.getProfile("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }

    public void getProfileWallet( APIResponseCallback<ProfileWallet> callback ) {
        //String value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIn0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIiwiaWF0IjoxNTc4MjE1MTU4LCJuYmYiOjE1NzgyMTUxNTgsImV4cCI6MTU4MDgwNzE1OCwic3ViIjoiMTUiLCJzY29wZXMiOlsiKiJdfQ.nv9xU5e3p32ezxU8OcFKT2qWqLTNDDNwL91p-G08nWaplRMOLUKf4Fih0eGsz1eQDwdNny1XFlfFnhLLaoEnEW6C3aKo3kxyBbY2z1ZZ5CpQTuVe5TzZUQKRizXCN6wjGPfgzkyItNFHTJr0Yt5h1MOHmqMkjCPniniGh4CIhul8sQ55r0ZFlK4kESGUNOLstZ3B1D9CK1RJsmIY4KOQV8eQ2NEBEF5mOGViSlsIuYCgRdSG6Rme0dNCD2usnlY6wbpsKGXeR_HczdELSUVlf_4qTuwuppJW6808TcH3oFf8eHjurNF0Yr_4IimZEFeaoVCxqj1n9hAqdiCcQ_4RjdSRk15MUadkXEjQU_c68uZGWBCws1IS9x01SEy_O2a15_xY11lCMp96tJuKkBn83xS0RxMH1EF7fxz6tmzX5QAVjpGsN8gkpJkhKtVNlGe_h1GbEZZWST-6x_GjFq4X3JPrtk5srNdRf-0e6di3tky8vISAKj3wMZLe7AYnvaQZz7nXwpLzEVmAfZqhukJLY4krlAeBS_jXcQ9075md6QY1C1mf4m123ptymzDKwDvOyUnM5SF_U7oMBILYOrFvWSmgnuknbdPL8mKZeqp6_Vv6NW_44-3uPkd0t2rMxR8S51W6NHUDKlEtOa1Mdboab9YaMhadrhfnR3c4b-bOHw8";
        //Call<ProfileWallet> call = apiService.getWallet("Bearer "+value);
        Call<ProfileWallet> call = apiService.getWallet("Bearer "+Constants.access_token);
        call.enqueue(callback);
    }


    public void getMyOrder(int pageNumber,int totalPages , APIResponseCallback<MyOrder> callback ) {
        Call<MyOrder> call = apiService.getorders("Bearer "+Constants.access_token,pageNumber,totalPages);
        call.enqueue(callback);
    }

    public void sendFeedbackApi( String orderId , String message , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.sendFeedbackAgainstOrder("Bearer "+Constants.access_token ,message ,orderId);
        call.enqueue(callback);
    }
    public void sendScheduleApi( String orderId , String intervalId , String intervalDate , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.sendScheduleApi("Bearer "+Constants.access_token ,intervalId ,orderId,intervalDate);
        call.enqueue(callback);
    }

    public void reOrder( String orderId , APIResponseCallback<JsonElement> callback ) {
        Call<JsonElement> call = apiService.reOrder("Bearer "+Constants.access_token ,orderId);
        call.enqueue(callback);
    }

    public void getInterval(  APIResponseCallback<Intervals> callback ) {
        Call<Intervals> call = apiService.getInterval();
        call.enqueue(callback);
    }

    public void updateProfile(String token, String name,String email, String phone_number,String gender , APIResponseCallback<Generic> callback) {
        Call<Generic> call = apiService.updateProfile("Bearer "+token,name ,email,phone_number,gender);
        call.enqueue(callback);
    }

    public void recoverEmail(String email , APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.recoverEmail(email);
        call.enqueue(callback);
    }

    public void changePassword(String email ,String code,String changePass,String changePassConf , APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.changePassword(email,code,changePass,changePassConf);
        call.enqueue(callback);
    }

    public void getBundle( String id ,APIResponseCallback<BundleDetail> callback ) {
        Call<BundleDetail> call = apiService.getBundlesDetail(id,"");
        call.enqueue(callback);
    }


    public void getBundleCategory( APIResponseCallback<Categories> callback ) {
        Call<Categories> call = apiService.getBundleCategory();
        call.enqueue(callback);
    }

    public void getPopularBundle(APIResponseCallback<ProductsBundle> callback) {
        Call<ProductsBundle> call = apiService.getPopularBundle("");
        call.enqueue(callback);
    }
    public void getVendors(APIResponseCallback<Vendors> callback) {
        Call<Vendors> call = apiService.getVendors();
        call.enqueue(callback);
    }

    public void getRedeme(String code,APIResponseCallback<WalletRedeme> callback) {
        //String value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIn0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIiwiaWF0IjoxNTc4MjE1MTU4LCJuYmYiOjE1NzgyMTUxNTgsImV4cCI6MTU4MDgwNzE1OCwic3ViIjoiMTUiLCJzY29wZXMiOlsiKiJdfQ.nv9xU5e3p32ezxU8OcFKT2qWqLTNDDNwL91p-G08nWaplRMOLUKf4Fih0eGsz1eQDwdNny1XFlfFnhLLaoEnEW6C3aKo3kxyBbY2z1ZZ5CpQTuVe5TzZUQKRizXCN6wjGPfgzkyItNFHTJr0Yt5h1MOHmqMkjCPniniGh4CIhul8sQ55r0ZFlK4kESGUNOLstZ3B1D9CK1RJsmIY4KOQV8eQ2NEBEF5mOGViSlsIuYCgRdSG6Rme0dNCD2usnlY6wbpsKGXeR_HczdELSUVlf_4qTuwuppJW6808TcH3oFf8eHjurNF0Yr_4IimZEFeaoVCxqj1n9hAqdiCcQ_4RjdSRk15MUadkXEjQU_c68uZGWBCws1IS9x01SEy_O2a15_xY11lCMp96tJuKkBn83xS0RxMH1EF7fxz6tmzX5QAVjpGsN8gkpJkhKtVNlGe_h1GbEZZWST-6x_GjFq4X3JPrtk5srNdRf-0e6di3tky8vISAKj3wMZLe7AYnvaQZz7nXwpLzEVmAfZqhukJLY4krlAeBS_jXcQ9075md6QY1C1mf4m123ptymzDKwDvOyUnM5SF_U7oMBILYOrFvWSmgnuknbdPL8mKZeqp6_Vv6NW_44-3uPkd0t2rMxR8S51W6NHUDKlEtOa1Mdboab9YaMhadrhfnR3c4b-bOHw8";
        Call<WalletRedeme> call = apiService.walletRedeme("Bearer "+Constants.access_token,code);
        call.enqueue(callback);
    }

    public void getTransactions(APIResponseCallback<WalletTransactions> callback) {
        //String value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIn0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjgwZDI2NzEzNWU0MGZkODIxZmUyZDY4ODRhY2IyZjMwZWI5NTMzOGQ3YTJlMDI5YzFmYThmOGUyNzU5N2M0NTJhZWZhMDdmMThiMWRiNGNmIiwiaWF0IjoxNTc4MjE1MTU4LCJuYmYiOjE1NzgyMTUxNTgsImV4cCI6MTU4MDgwNzE1OCwic3ViIjoiMTUiLCJzY29wZXMiOlsiKiJdfQ.nv9xU5e3p32ezxU8OcFKT2qWqLTNDDNwL91p-G08nWaplRMOLUKf4Fih0eGsz1eQDwdNny1XFlfFnhLLaoEnEW6C3aKo3kxyBbY2z1ZZ5CpQTuVe5TzZUQKRizXCN6wjGPfgzkyItNFHTJr0Yt5h1MOHmqMkjCPniniGh4CIhul8sQ55r0ZFlK4kESGUNOLstZ3B1D9CK1RJsmIY4KOQV8eQ2NEBEF5mOGViSlsIuYCgRdSG6Rme0dNCD2usnlY6wbpsKGXeR_HczdELSUVlf_4qTuwuppJW6808TcH3oFf8eHjurNF0Yr_4IimZEFeaoVCxqj1n9hAqdiCcQ_4RjdSRk15MUadkXEjQU_c68uZGWBCws1IS9x01SEy_O2a15_xY11lCMp96tJuKkBn83xS0RxMH1EF7fxz6tmzX5QAVjpGsN8gkpJkhKtVNlGe_h1GbEZZWST-6x_GjFq4X3JPrtk5srNdRf-0e6di3tky8vISAKj3wMZLe7AYnvaQZz7nXwpLzEVmAfZqhukJLY4krlAeBS_jXcQ9075md6QY1C1mf4m123ptymzDKwDvOyUnM5SF_U7oMBILYOrFvWSmgnuknbdPL8mKZeqp6_Vv6NW_44-3uPkd0t2rMxR8S51W6NHUDKlEtOa1Mdboab9YaMhadrhfnR3c4b-bOHw8";
        Call<WalletTransactions> call = apiService.getTransactions("Bearer "+Constants.access_token);
        call.enqueue(callback);
        //Constants.access_token
    }

    public void getBankPromooffers(APIResponseCallback<PromoOffers> callback) {
        Call<PromoOffers> call = apiService.getBankPromooffers();
        call.enqueue(callback);
    }

    public void getVersion(APIResponseCallback<JsonElement> callback) {
        Call<JsonElement> call = apiService.getVersion();
        call.enqueue(callback);
    }

}

