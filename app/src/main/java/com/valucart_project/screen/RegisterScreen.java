package com.valucart_project.screen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonElement;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.adapter.AdapterRvInterval;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.DateTimeChangeListener;
import com.valucart_project.models.Login;
import com.valucart_project.popups.DateDialogFragment;
import com.valucart_project.popups.EmailVerificationDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class RegisterScreen extends FragmentActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    CallbackManager callbackManager;
    LinearLayout llFbGoogle, llSignin;
    RelativeLayout rlRegister, rlNext;
    LoginButton fbLogin;
    private Library library;
    EditText etUserName, etGender, etEmail,etPhone;
    ShowHidePasswordEditText etPassword;
    private String[] genderArray = {"Select", "Male", "Female"};
    Spinner spinnerGender;
    String CallingFrom="";
    RadioButton rbTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_screen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        library = new Library(this);

        initItem();
        initGoogleFb();
        selectGender();
        getIntentRecord();
    }

    public void getIntentRecord(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallingFrom");
        }
    }

    public void initGoogleFb() {
        SignInButton googleLogin = findViewById(R.id.googleLogin);
        googleLogin.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.googleLogin).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        fbLogin = findViewById(R.id.fbLogin);

        fbLogin.setReadPermissions(Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().logOut();

        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                String accessToken = AccessToken.getCurrentAccessToken().getToken();
                getUserProfile(AccessToken.getCurrentAccessToken(), accessToken);
                Log.d("API123", loggedIn + " ??");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("API123", " ??");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("API123", " ??");
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
    }


    public void initItem() {

        rbTerm = findViewById(R.id.rbTerm);

        etUserName = findViewById(R.id.etUserName);
        etGender = findViewById(R.id.etGender);
        etGender.setOnClickListener(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);

        TextView tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(this);
        rlNext = findViewById(R.id.rlNext);
        rlNext.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
        TextView tvTermCondition = findViewById(R.id.tvTermCondition);
        tvTermCondition.setOnClickListener(this);

        LinearLayout llFacebook = findViewById(R.id.llFacebook);
        llFacebook.setOnClickListener(this);
        LinearLayout llGoogle = findViewById(R.id.llGoogle);
        llGoogle.setOnClickListener(this);

        llFbGoogle = findViewById(R.id.llFbGoogle);
        llSignin = findViewById(R.id.llSignin);

        rlRegister = findViewById(R.id.rlRegister);
        rlRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLogin:
                //signIn();
                break;
            case R.id.tvSignIn:
                startActivity(new Intent(this, LoginScreen.class));
                break;
            case R.id.rlNext:
                if (!rbTerm.isChecked()) {
                    library.alertErrorMessage("Please select Terms and Condition");
                    // Toast.makeText(getApplicationContext(), "Please enter User Name", Toast.LENGTH_LONG).show();
                }
                else if (etUserName.getText().toString().equals("")) {
                    library.alertErrorMessage("Please enter User Name");
                    // Toast.makeText(getApplicationContext(), "Please enter User Name", Toast.LENGTH_LONG).show();
                } else if (etPassword.getText().toString().equals("")) {
                    library.alertErrorMessage("Please enter password");
                    //Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
                } else if (etEmail.getText().toString().equals("")) {
                    library.alertErrorMessage("Please enter Email");
                    //Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_LONG).show();
                }  else {
                    loadRegisterUser();
                }
                break;
            case R.id.tvTermCondition:
                //startActivity(new Intent(this, TermConditionScreen.class));
                //browseUrl("http://v2api.valucart.com/termsofuse/");
                reSchedulePopup();
                break;
            case R.id.llFacebook:
                fbLogin.performClick();
                break;
            case R.id.llGoogle:
                signIn();

                break;
            case R.id.rlRegister:
                Constants.userLogin = true;
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.etGender:
                spinnerGender.performClick();
                break;

        }
    }
    public void browseUrl(String url){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            String token = account.getIdToken();

            Constants.userLogin = true;
            loadRegisterFbGoogle("google", token);
            Log.w("xf", "signInResult:failed code=");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("xf", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }


    private void getUserProfile(AccessToken currentAccessToken, final String accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            //String first_name = object.getString("first_name");
                            //String last_name = object.getString("last_name");
                            //String email = object.getString("email");
                            String id = object.getString("id");
                            //String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            //String image_url1 = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Constants.userLogin = true;
                            loadRegisterFbGoogle("facebook", accessToken);

                            LoginManager.getInstance().logOut();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void loadRegisterUser() {
        library.showLoading();

        APIManager.getInstance().getSignUp(etUserName.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(), etGender.getText().toString(),Constants.fireBaseToken, new APIResponseCallback<Login>() {

            @Override
            public void onResponseLoaded(@NonNull Login response) {

                library.hideLoading();
                Constants.userLogin = true;
                Login login = response;
                Constants.access_token= login.getAccess_token();
                saveToken(getApplicationContext(), Constants.access_token);
                Toast.makeText(getApplicationContext(), "Your Account is registered successfully", Toast.LENGTH_LONG).show();
                if(!Constants.bundleId.equals("")){
                    loadMyBundle();
                }

                if (CallingFrom.equals("AddToCart")) {
                    EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"AddToCart");
                    phoneVerificationDialogFragment.show(getSupportFragmentManager(),"AddToCart");
                } else if (CallingFrom.equals("BuildYourOwnBundle")) {
                    EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"AddToCart");
                    phoneVerificationDialogFragment.show(getSupportFragmentManager(),"BuildYourOwnBundle");
                } else {
                    EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"registration");
                    phoneVerificationDialogFragment.show(getSupportFragmentManager(),"registration");
                }

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                //{"status":0,"message":"The given data was invalid.","errors":{"email":["The email address adnankhan895@gmail.com is already associated with another customer."]}}
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("email")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("email").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("name")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("name").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("phone_number")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("phone_number").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("gender")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("gender").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("password")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("password").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The user credentials were incorrect.");
                    //Toast.makeText(getApplicationContext(),"The user credentials were incorrect.",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    library.alertErrorMessage("The user credentials were incorrect.");
                }

            }

        });

    }

    private void loadMyBundle() {

        APIManager.getInstance().connectByobWithLogin(Constants.bundleId, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                library.hideLoading();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    public static void saveToken(Context context , String value){

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("token", ""+value);
        editor.apply();

    }


    private void loadRegisterFbGoogle(String name, String token) {

        library.showLoading();
        APIManager.getInstance().getFacebookGoogle(name, token,Constants.fireBaseToken, new APIResponseCallback<Login>() {

            @Override
            public void onResponseLoaded(@NonNull Login response) {
                library.hideLoading();
                Constants.userLogin = true;
                Login login = response;
                Constants.access_token= response.getAccess_token();
                saveToken(getApplicationContext(), Constants.access_token);
                Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                if(!Constants.bundleId.equals("")){
                    loadMyBundle();
                }
                if (CallingFrom.equals("AddToCart")) {
                    //EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"AddToCart");
                    //phoneVerificationDialogFragment.show(getSupportFragmentManager(),"AddToCart");
                    Intent intent = new Intent(getApplicationContext(), DeliveryDateTimeScreen.class);
                    intent.putExtra("CallFrom", "AddToCart");
                    startActivity(intent);
                    finish();
                } else if (CallingFrom.equals("BuildYourOwnBundle")) {
                    //EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"AddToCart");
                    //phoneVerificationDialogFragment.show(getSupportFragmentManager(),"BuildYourOwnBundle");
                    Intent intent = new Intent(RegisterScreen.this, BundleSummaryScreen.class);
                    intent.putExtra("byobId", Constants.bundleId);
                    startActivity(intent);
                    finish();
                }else {
                    //EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext(),"registration");
                    //phoneVerificationDialogFragment.show(getSupportFragmentManager(),"registration");
                    startActivity(new Intent(RegisterScreen.this, DashboardActivity.class));
                }

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

    public void selectGender() {

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        AdapterForSpinner aa = new AdapterForSpinner(this, R.layout.simple_spinner_item, genderArray);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerGender.setAdapter(aa);
        spinnerGender.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0)
            etGender.setText("" + genderArray[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    View view;
    BottomSheetDialog bottomSheetDialog;
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("v2api.valucart.com".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    public void reSchedulePopup(){

        View viewData = getLayoutInflater().inflate(R.layout.bottom_sheeet_term_condition, null);

        WebView wv = (WebView) viewData.findViewById(R.id.wbTermCondition);
        library.showLoading();
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                library.hideLoading();
                bottomSheetDialog.show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                library.hideLoading();

            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        wv.clearCache(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadUrl("http://v2api.valucart.com/termsofuse");


        //TextView wbTermCondition = (TextView) viewData.findViewById(R.id.wbTermCondition);
        //wbTermCondition.setMovementMethod(new ScrollingMovementMethod());
        RelativeLayout rlNext = viewData.findViewById(R.id.rlNext);
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rbTerm.setChecked(true);

                bottomSheetDialog.dismiss();

            }
        });

        ImageView ivCancel = viewData.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(RegisterScreen.this, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(viewData);

    }


}

