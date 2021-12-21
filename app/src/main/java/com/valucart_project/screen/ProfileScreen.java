package com.valucart_project.screen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Generic;
import com.valucart_project.popups.LogoutDialogFragment;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileScreen extends FragmentActivity implements View.OnClickListener , AdapterView.OnItemSelectedListener {

    RelativeLayout rlChangePassword, rlUpdateProfile,rlGender;
    ImageView ivLogout;
    EditText etUserName,etGender,etEmail,etPhone;
    private String[] genderBy = {"Select", "Male", "Female"};
    Spinner spinnerGender;
    Library library;
    String oauthProvider="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        library = new Library(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlChangePassword = findViewById(R.id.rlChangePassword);
        rlChangePassword.setOnClickListener(this);

        rlUpdateProfile = findViewById(R.id.rlUpdateProfile);
        rlUpdateProfile.setOnClickListener(this);

        ivLogout = findViewById(R.id.ivLogout);
        ivLogout.setOnClickListener(this);

        rlGender= findViewById(R.id.rlGender);
        rlGender.setOnClickListener(this);

        etUserName = findViewById(R.id.etUserName);
        etGender = findViewById(R.id.etGender);
        etGender.setOnClickListener(this);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        etUserName.setText(""+Constants.profileDetail.getData().getName());
        etGender.setText(""+Constants.profileDetail.getData().getGender());
        etEmail.setText(""+Constants.profileDetail.getData().getEmail());
        etPhone.setText(""+Constants.profileDetail.getData().getPhone_number());

        sortByItem();

        oauthProvider = getIntent().getExtras().getString("oauth_provider");
        if(!oauthProvider.equals(""))
            rlChangePassword.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        if (rlUpdateProfile == v) {
            //LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment(getApplicationContext());
            //logoutDialogFragment.show(getSupportFragmentManager(),"");
            //EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext());
            //phoneVerificationDialogFragment.show(getSupportFragmentManager(),"");
            //PromoCodeDialog forgetPasswordDialogFragment = new PromoCodeDialog(getApplicationContext());
            //forgetPasswordDialogFragment.show(getSupportFragmentManager(),"");
            //Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
            loadUpdateProfile(etUserName.getText().toString(),etEmail.getText().toString(),etPhone.getText().toString(),etGender.getText().toString());
        }

        if (rlChangePassword == v) {
            startActivity(new Intent(this, ChangePasswordScreen.class));
        }

        if (ivLogout == v) {
           // Constants.userLogin = false;
            LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment(getApplicationContext());
            logoutDialogFragment.show(getSupportFragmentManager(), "");
            //EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getApplicationContext());
            //phoneVerificationDialogFragment.show(getSupportFragmentManager(),"");
        }
        if (rlGender == v) {
            spinnerGender.performClick();
        }
        if (etGender == v) {
            spinnerGender.performClick();
        }

    }


    public void sortByItem() {

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        AdapterForSpinner aa = new AdapterForSpinner(this, R.layout.simple_spinner_item, genderBy);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerGender.setAdapter(aa);
        spinnerGender.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            etGender.setText(""+genderBy[position].toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadUpdateProfile(String name,String email,String phone_number , String gender) {
        library.showLoading();
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU5NWUwYTg3MTBmOWVkZGU0MWNhNDQyMmE4ZjE2N2UxNTljZDQ2NzcyMGM2MTI2NDVhYzc1OWQyMGYzNWI5YzdkZTQ2ZGRkMzJjYTM1YmU1In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6ImU5NWUwYTg3MTBmOWVkZGU0MWNhNDQyMmE4ZjE2N2UxNTljZDQ2NzcyMGM2MTI2NDVhYzc1OWQyMGYzNWI5YzdkZTQ2ZGRkMzJjYTM1YmU1IiwiaWF0IjoxNTU1OTMwNjI3LCJuYmYiOjE1NTU5MzA2MjcsImV4cCI6MTU1NzIyNjYyNywic3ViIjoiMTkiLCJzY29wZXMiOlsiKiJdfQ.VgHwK64KSBvw08VNrlhkkC4ZKzgoKSPLW4lithAbNPX_TyBiwvPdM7_4ihZetnfiftm8qMdEUHIShTP0jO88-wFEkQKGrIqxF2K5LpUACiNdUHh0tPK-HPWPkAneLB6CJ7QrELPhxFcgx-nGdMS1i2FSiB3M5T_GuNNZ3zUPbK4xZVANJ02Ww_1I0EexnOAzCRNMb6XpbLyDY12Y2btAgxI36Xl5NZ7TxPKKqeI0TPS5gt7hLq3XzIE9zgXCLvcgjc7e3aj525prVgDpVZ5VJ4bAUY9ED3AB-8Ov4IPRN7DjRlcceS9XPgEmYoqBjWwditauMQyCYyMHJxach3VfaKs3EVUxQa-WCeeqIGHRrmI97D0koRMPM4CFxzUlz5D-B9piKEaPJYVspUCCc8NZDOlxScjJihSU2SIgqe_jSz0FGbkpZN6EqS7C-gF74q1fg2oSC5Z8KHMAM5RPYatBvC7fna1m-PX-8y5jeNCxf0ANbEZ4f9Wk-imxiHSK50rkV3zPUShuwrR-ucKCkBF0YuQFioMaT-bws0v2MHMPtbsetXhfCKyG6r0mJHv-eggsP41nG7lxuGvg2qy_GV7nnt7qa4bZg186I4gKJvjCDitdJTi0f5h8IIKNu9cZTahDO0T3jQjQWd1UtghxUVKbBkjoleQ4NLiYbxbFyKXuS48";
        APIManager.getInstance().updateProfile(Constants.access_token , name, email ,phone_number,gender.toLowerCase(), new APIResponseCallback<Generic>() {

            @Override
            public void onResponseLoaded(@NonNull Generic response) {
                library.hideLoading();
                Toast.makeText(getApplicationContext(),response.getMessage(),Toast.LENGTH_LONG).show();
                //Constants.userLogin = false;
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("email")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("email").toString());
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
                    } else if (jsonObject1.toString().contains("name")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("name").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                        //Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        library.alertErrorMessage("" + jsonObject.getString("message").toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        library.alertErrorMessage("The user credentials were incorrect.");
                    } catch (Exception ex) {
                        library.alertErrorMessage("The user credentials were incorrect.");
                    }
                } catch (Exception e){
                    library.alertErrorMessage("The user credentials were incorrect.");
                }
            }

        });

    }


}

