package com.valucart_project.utils;

import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Categories;
import com.valucart_project.models.CustomersRegistration;
import com.valucart_project.models.Login;
import com.valucart_project.models.Products;
import com.valucart_project.models.ProfileDetail;

public class Constants {

    //public static final String BASE_URL = BuildConfig.BASE_URL;
    //public static final String BASE_URL_ICON = BuildConfig.BASE_URL_ICON;
    public static final String DateFormat = "dd-MM-yyyy";
    public static Boolean userLogin = false;
    public static Boolean emailVerified = false;
    public static String access_token = "";
    public static CustomersRegistration customersRegistration = new CustomersRegistration();
    public static Login login = new Login();
    public static String client_id = "4d71cf0f7eec0c8b0a5b31aebb2a0a06";
    public static String grant_type = "password";
    public static String scope = "*";
    public static Categories categories;
    public static Boolean BYOBFirstTimeViewScreen = false;
    public static String cart_id = "";
    public static int totalCart = 0;
    public static String valucartPreferences = "valucart";
    public static String orderReference = "";
    public static String customer_id = "";
    public static String bundleId = "";
    public static ProfileDetail profileDetail = new ProfileDetail();
    public static Products productsListByob = new Products();
    public static ByobSummary byobSummaryList;
    public static Boolean showPromo = false;
    public static String fireBaseToken = "";
    public static Boolean analyticEnable = false;

    public static Boolean facebookAds = false;
    public static String linkingType = "";
    public static String linkingId = "";
    public static String linkingName = "";
    public static String linkingCategoryId = "";

}

