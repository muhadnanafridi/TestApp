package com.valucart_project.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Login;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginScreen extends FragmentActivity implements OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    CallbackManager callbackManager;
    LoginButton fbLogin;
    EditText  etEmailAddress;
    ShowHidePasswordEditText etPassword;
    RelativeLayout rlLogin;
    TextView tvSigup,tvForgetPassword;
    private Library library;
    String CallingFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        library = new Library(this);
        initFields();

        loginFbGmail();
        //printHashKey(getApplicationContext());
        getIntentRecord();
        //printKeyHash(this);
    }

    public void getIntentRecord() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CallingFrom = extras.getString("CallFrom");
        }
    }


    public void initFields() {

        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(this);

        rlLogin = findViewById(R.id.rlLogin);
        rlLogin.setOnClickListener(this);

        RelativeLayout rlFaceBook = findViewById(R.id.rlFaceBook);
        rlFaceBook.setOnClickListener(this);

        RelativeLayout rlGmail = findViewById(R.id.rlGmail);
        rlGmail.setOnClickListener(this);

        RelativeLayout rlSkip = findViewById(R.id.rlSkip);

        tvSigup = findViewById(R.id.tvSigup);
        tvSigup.setOnClickListener(this);

        etEmailAddress = findViewById(R.id.etEmailAddress);

        etPassword = findViewById(R.id.etPassword);

        //etEmailAddress.setText("adnan1222@gmail.com");
/*        etEmailAddress.setText("afridi@valucart.com");
        //etEmailAddress.setText("aridi@valucart.com");//adnankhan
        etPassword.setText("adnanafridi");*/
/*        etEmailAddress.setText("doanantony@valucart.com");
        etPassword.setText("123456789");*/

    }

    public void loginFbGmail() {
        //.requestIdToken(getString(R.string.google_client_id))

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
        //fbLogin.performClick();
        fbLogin.setReadPermissions(Arrays.asList("email", "public_profile"));
        //fbLogin.setReadPermissions("email", "public_profile", "user_friends");
        LoginManager.getInstance().logOut();
        //}

        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rlGmail:
                signIn();
                break;
            case R.id.googleLogin:
                signIn();
                break;
            case R.id.rlFaceBook:
                fbLogin.performClick();
                break;
            case R.id.ivCancel:
                finish();
                break;
            case R.id.rlSkip:
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.rlNext:
                break;
            case R.id.tvForgetPassword:
                startActivity(new Intent(this, ForgetPasswordScreen.class));
                break;
            case R.id.rlLogin:
                if (etEmailAddress.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_LONG).show();
                } else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
                } else {
                    loadloginUser();
                }
                break;
            case R.id.tvSigup:
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                intent.putExtra("CallingFrom", CallingFrom);
                startActivity(intent);
                break;

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

            //Log.w("xf", "signInResult:failed code=");  account.getIdToken()  getServerAuthCode();
            String token = account.getIdToken();
            //String ServerAuthCode = account.getServerAuthCode();
            Log.w("xf", "signInResult:failed code=" + token);
            Constants.userLogin = true;
            loadRegisterFbGoogle("google", token);
            //loadRegisterFbGoogle("google", ServerAuthCode);

        } catch (ApiException e) {
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("xf", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(),"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }


    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("testing item", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("testing item", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("testing item", "printHashKey()", e);
        }
    }

    public void printKeyHash(Activity context) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

    private void getUserProfile(final AccessToken currentAccessToken, final String accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());

                        try {
                            String id = object.getString("id");

                            Constants.userLogin = true;
                            loadRegisterFbGoogle("facebook", accessToken);

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

    private void loadloginUser() {
        library.showLoading();
        APIManager.getInstance().loginUser(etEmailAddress.getText().toString(), etPassword.getText().toString(), Constants.grant_type, Constants.client_id, Constants.scope,Constants.fireBaseToken, new APIResponseCallback<Login>() {

            @Override
            public void onResponseLoaded(@NonNull Login response) {
                library.hideLoading();
                Constants.userLogin = true;
                Login login = response;
                Constants.access_token = login.getAccess_token();
                saveToken(getApplicationContext(), Constants.access_token);
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                if (!Constants.bundleId.equals("")) {
                    loadMyBundle();
                }
                if (CallingFrom.equals("AddToCart")) {
                    //startActivity(new Intent(getApplicationContext(), DeliveryDateTimeScreen.class));
                    Intent intent = new Intent(getApplicationContext(), DeliveryDateTimeScreen.class);
                    intent.putExtra("CallFrom", "AddToCart");
                    startActivity(intent);
                    finish();
                }else if (CallingFrom.equals("BuildYourOwnBundle")) {
                    Intent intent = new Intent(getApplicationContext(), BundleSummaryScreen.class);
                    intent.putExtra("byobId", Constants.bundleId);
                    startActivity(intent);
                    finish();
                } else {
                    loadCart();

                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                library.alertErrorMessage("The user credentials were incorrect.");
                //Toast.makeText(getApplicationContext(), "The user credentials were incorrect.", Toast.LENGTH_LONG).show();
            }

        });

    }

    private void loadRegisterFbGoogle(String name, String token) {

        APIManager.getInstance().getFacebookGoogle(name, token,Constants.fireBaseToken, new APIResponseCallback<Login>() {

            @Override
            public void onResponseLoaded(@NonNull Login response) {
                library.hideLoading();
                Constants.access_token = response.getAccess_token();
                saveToken(getApplicationContext(), Constants.access_token);
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                if (!Constants.bundleId.equals("")) {
                    loadMyBundle();
                }
                if (CallingFrom.equals("AddToCart")) {
                    startActivity(new Intent(getApplicationContext(), DeliveryDateTimeScreen.class));
                    finish();
                } else if (CallingFrom.equals("BuildYourOwnBundle")) {
                    Intent intent = new Intent(getApplicationContext(), BundleSummaryScreen.class);
                    intent.putExtra("byobId", Constants.bundleId);
                    startActivity(intent);
                    finish();
                }else {
                    loadCart();
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                library.alertErrorMessage("The user credentials were incorrect.");
                //Toast.makeText(getApplicationContext(), "The user credentials were incorrect.", Toast.LENGTH_LONG).show();
            }

        });

    }

    public static void saveToken(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("token", "" + value);
        editor.apply();

    }

    private void loadMyBundle() {

        APIManager.getInstance().connectByobWithLogin(Constants.bundleId, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                library.hideLoading();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    //Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                }catch (Exception e){}

                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void loadCart() {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        library.showLoading();
        APIManager.getInstance().getCartDetailAfterLogin( new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    if(response.getAsJsonObject().get("status").toString().equals("1")) {
                        //if (Constants.cart_id.equals(""))
                        if (!((JsonObject) ((JsonObject) response).get("data")).get("id").toString().equals("null"))
                            Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();

                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());

                        displayTotalCart(getApplicationContext());
                        DashboardActivity.totalCartValue(getApplicationContext(), "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),response.getAsJsonObject().get("message").toString(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //{"status":0,"message":"Product on offer can not exceed quantity 1 in the cart."}
                library.hideLoading();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }

        });
    }
    public static void displayTotalCart(Context context ){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", ""+Constants.totalCart);
        editor.putString("cart_id", ""+Constants.cart_id);
        editor.apply();
    }

}

//client id
//129100931757-0807mj33fao6pbcnajubepbnutuckpa3.apps.googleusercontent.com
//client security
//LEPotvYTV8iDvhD51M9rOIbY
//DD:78:6C:E7:37:37:AC:88:70:78:8A:6C:45:6E:C9:9B:85:0B:70:4D     Debug
//655292895947-c67nvhp2aibm1dalhgq09ubn84pi0evb.apps.googleusercontent.com
//3Xhs5zc3rIhweIpsRW7Jm4ULcE0=
//              1XJab6GlbxNUNS2FUnQkNJIhQ54=
//D5:72:5A:6F:A1:A5:6F:13:54:35:2D:85:52:74:24:34:92:21:43:9E     Release
//SkzKfXdpcarnuOI7+rS1IvXR4so=
//SKzKfXdpcarnuOl7+rS1lvXR4so=


//Hash Key

//1XJab6GlbxNUNS2FUnQkNJIhQ54=
//3Xhs5zc3rIhweIpsRW7Jm4ULcE0=
//SKzKfXdpcarnuOl7+rS1lvXR4so=
