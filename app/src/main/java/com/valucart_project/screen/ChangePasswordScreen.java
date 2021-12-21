package com.valucart_project.screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Generic;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordScreen extends Activity {

    ShowHidePasswordEditText etCurrentPassword,etNewPassword,etConfirmPassword;
    Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        library = new Library(this);
        setContentView(R.layout.change_password_screen);

        etCurrentPassword=findViewById(R.id.etCurrentPassword);
        etNewPassword=findViewById(R.id.etNewPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //etCurrentPassword.setText("adnanafridi");
        //etNewPassword.setText("adnankhan");
        //etConfirmPassword.setText("adnankhan");
        RelativeLayout rlChangePassword = findViewById(R.id.rlChangePassword);
        rlChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constants.userLogin = false;
                //startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                //finish();
                loadChangePassword(etCurrentPassword.getText().toString(),etNewPassword.getText().toString());
            }
        });

    }
//{"status":0,"message":"The given data was invalid","errors":{"new_password":["New password must be at least 8 characters long"],"new_password_confirmation":["The new password confirmation must be at least 8 characters."]}}
    private void loadChangePassword(String currentPasword,String newPassword) {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU5NWUwYTg3MTBmOWVkZGU0MWNhNDQyMmE4ZjE2N2UxNTljZDQ2NzcyMGM2MTI2NDVhYzc1OWQyMGYzNWI5YzdkZTQ2ZGRkMzJjYTM1YmU1In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6ImU5NWUwYTg3MTBmOWVkZGU0MWNhNDQyMmE4ZjE2N2UxNTljZDQ2NzcyMGM2MTI2NDVhYzc1OWQyMGYzNWI5YzdkZTQ2ZGRkMzJjYTM1YmU1IiwiaWF0IjoxNTU1OTMwNjI3LCJuYmYiOjE1NTU5MzA2MjcsImV4cCI6MTU1NzIyNjYyNywic3ViIjoiMTkiLCJzY29wZXMiOlsiKiJdfQ.VgHwK64KSBvw08VNrlhkkC4ZKzgoKSPLW4lithAbNPX_TyBiwvPdM7_4ihZetnfiftm8qMdEUHIShTP0jO88-wFEkQKGrIqxF2K5LpUACiNdUHh0tPK-HPWPkAneLB6CJ7QrELPhxFcgx-nGdMS1i2FSiB3M5T_GuNNZ3zUPbK4xZVANJ02Ww_1I0EexnOAzCRNMb6XpbLyDY12Y2btAgxI36Xl5NZ7TxPKKqeI0TPS5gt7hLq3XzIE9zgXCLvcgjc7e3aj525prVgDpVZ5VJ4bAUY9ED3AB-8Ov4IPRN7DjRlcceS9XPgEmYoqBjWwditauMQyCYyMHJxach3VfaKs3EVUxQa-WCeeqIGHRrmI97D0koRMPM4CFxzUlz5D-B9piKEaPJYVspUCCc8NZDOlxScjJihSU2SIgqe_jSz0FGbkpZN6EqS7C-gF74q1fg2oSC5Z8KHMAM5RPYatBvC7fna1m-PX-8y5jeNCxf0ANbEZ4f9Wk-imxiHSK50rkV3zPUShuwrR-ucKCkBF0YuQFioMaT-bws0v2MHMPtbsetXhfCKyG6r0mJHv-eggsP41nG7lxuGvg2qy_GV7nnt7qa4bZg186I4gKJvjCDitdJTi0f5h8IIKNu9cZTahDO0T3jQjQWd1UtghxUVKbBkjoleQ4NLiYbxbFyKXuS48";
        APIManager.getInstance().getChangePassword(Constants.access_token , currentPasword, newPassword , new APIResponseCallback<Generic>() {

            @Override
            public void onResponseLoaded(@NonNull Generic response) {
                Toast.makeText(getApplicationContext(),response.getMessage(),Toast.LENGTH_LONG).show();
                Constants.userLogin = false;
                emptyTotalCart(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                finish();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("new_password")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("new_password").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString());
//                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("new_password_confirmation")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("new_password_confirmation").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString());
//                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else if (jsonObject1.toString().contains("current_password")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("current_password").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString());
//                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The user credentials were incorrect.");
//                    Toast.makeText(getApplicationContext(),"The user credentials were incorrect.",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    library.alertErrorMessage("The user credentials were incorrect.");
                }

                //Constants.userLogin = true;
                //startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }

        });

    }
    public static void emptyTotalCart(Context context ){

        Constants.cart_id ="";
        Constants.totalCart = 0;

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "");
        editor.putString("cart_id", "");
        editor.putString("token", "");
        editor.putString("bundle_id", "");
        editor.apply();

    }

}

