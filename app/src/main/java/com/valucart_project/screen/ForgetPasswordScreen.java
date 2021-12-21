package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordScreen extends Activity {

    Library library;
    LinearLayout llEmailVerification,llRecoverPassword;
    EditText  etEmailRecover,etCodeRecover,etEmail;
    ShowHidePasswordEditText etNewPassword ,etNewPasswordConfirmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        library = new Library(this);

        setContentView(R.layout.forget_password_screen);

        initField();
    }

    public void initField(){
        etEmail = findViewById(R.id.etEmail);

        RelativeLayout rlNext = findViewById(R.id.rlNext);
        llEmailVerification = findViewById(R.id.llEmailVerification);
        llRecoverPassword = findViewById(R.id.llRecoverPassword);

        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecover(etEmail.getText().toString());
            }
        });

        etEmailRecover = findViewById(R.id.etEmailRecover);
        //etEmailRecover.setText("afridi@valucart.com");
        etCodeRecover = findViewById(R.id.etCodeRecover);
        etCodeRecover.setText("");
        etNewPassword = findViewById(R.id.etNewPassword);
        //etNewPassword.setText("12345678");
        etNewPasswordConfirmation = findViewById(R.id.etNewPasswordConfirmation);
        //etNewPasswordConfirmation.setText("12345678");

        RelativeLayout rlSave = findViewById(R.id.rlSave);
        rlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(etEmailRecover.getText().toString(),etCodeRecover.getText().toString(),etNewPassword.getText().toString(),etNewPasswordConfirmation.getText().toString());
            }
        });
    }

    private void loadRecover(String email) {
        library.showLoading();

        APIManager.getInstance().recoverEmail(email, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                if(response.getAsJsonObject().get("status").getAsString().equals("1")){
                    library.hideKeyBoard();
                    llEmailVerification.setVisibility(View.GONE);
                    llRecoverPassword.setVisibility(View.VISIBLE);
                    etEmailRecover.setText(etEmail.getText().toString());
                    library.hideKeyBoard();
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("username")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("username").toString());
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

    private void changePassword(String email,String code,String newPassword,String newPasswordConf) {

        library.showLoading();
        APIManager.getInstance().changePassword(email,code,newPassword,newPasswordConf, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                if(response.getAsJsonObject().get("status").getAsString().equals("1")){
                    Toast.makeText(getApplicationContext(),response.getAsJsonObject().get("message").getAsString(),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                    finish();
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("code")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("code").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                    }else if (jsonObject1.toString().contains("ref")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("ref").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                    }else if (jsonObject1.toString().contains("new_password")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("new_password").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
                    }else if (jsonObject1.toString().contains("new_password_confirmation")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("new_password_confirmation").toString());
                        library.alertErrorMessage(jsonArray.get(0).toString());
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

}

