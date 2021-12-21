package com.valucart_project.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Generic;
import com.valucart_project.screen.BundleSummaryScreen;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.screen.DeliveryDateTimeScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class EmailVerificationDialogFragment extends DialogFragment {

    Context context;
    boolean isViewOnly;
    EditText etVerifyCharactor1,etVerifyCharactor2,etVerifyCharactor3,etVerifyCharactor4;
    static String callingFrom="";
    RelativeLayout rlEmailReSend;
    OnItemSelection onItemSelection;


    public static void showPopup( FragmentActivity activity) {
        EmailVerificationDialogFragment residentNotesDialogFragment = new EmailVerificationDialogFragment( activity,callingFrom);
        residentNotesDialogFragment.isViewOnly = true;
        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");
    }

    @SuppressLint("ValidFragment")
    public EmailVerificationDialogFragment(Context contextrecord,String callingFrom1) {
        context = contextrecord;
        callingFrom = callingFrom1;
    }

    @SuppressLint("ValidFragment")
    public EmailVerificationDialogFragment(Context contextrecord, String callingFrom1, OnItemSelection onItemSelection1) {
        context = contextrecord;
        callingFrom = callingFrom1;
        onItemSelection=onItemSelection1;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.email_login_verification_popup, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        rlEmailReSend = view.findViewById(R.id.rlEmailReSend);

        etVerifyCharactor1 = view.findViewById(R.id.etVerifyCharactor1);
        etVerifyCharactor2 = view.findViewById(R.id.etVerifyCharactor2);
        etVerifyCharactor3 = view.findViewById(R.id.etVerifyCharactor3);
        etVerifyCharactor4 = view.findViewById(R.id.etVerifyCharactor4);

        view.findViewById(R.id.rlPhoneVerification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etVerifyCharactor1.getText().toString().equals("")&&!etVerifyCharactor2.getText().toString().equals("")&&!etVerifyCharactor3.getText().toString().equals("")&&!etVerifyCharactor4.getText().toString().equals(""))
                    loadEmailVerification(""+etVerifyCharactor1.getText().toString()+etVerifyCharactor2.getText().toString()+etVerifyCharactor3.getText().toString()+etVerifyCharactor4.getText().toString());
                else
                    Toast.makeText(context,"Please enter your complete verification number ",Toast.LENGTH_LONG).show();
            }
        });

        rlEmailReSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResendEmail();
            }
        });

        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        textChangeListener();
        hideKeyBoard();
    }

    public void textChangeListener(){

        etVerifyCharactor1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etVerifyCharactor1.getText().toString().length()==1)     //size as per your requirement
                {
                    etVerifyCharactor2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etVerifyCharactor2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etVerifyCharactor2.getText().toString().length()==1)     //size as per your requirement
                {
                    etVerifyCharactor3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etVerifyCharactor3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etVerifyCharactor3.getText().toString().length()==1)     //size as per your requirement
                {
                    etVerifyCharactor4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

    }
    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {}
    }

    private void loadEmailVerification(String code) {
        //code = "036741";
        APIManager.getInstance().getEmailVerification(Constants.access_token  , code , new APIResponseCallback<Generic>() {

            @Override
            public void onResponseLoaded(@NonNull Generic response) {
                Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_LONG).show();
                Constants.emailVerified = true;
                dismiss();
                if(callingFrom.equals("MoreMenu")) {
                    startActivity(new Intent(context, DashboardActivity.class));
                } else if(callingFrom.equals("AddToCart")){
                    startActivity(new Intent(context, DeliveryDateTimeScreen.class));
                    getActivity().finish();
                } else if(callingFrom.equals("BuildYourOwnBundle")){
                    Intent intent = new Intent(context, BundleSummaryScreen.class);
                    intent.putExtra("byobId", Constants.bundleId);
                    startActivity(intent);
                    getActivity().finish();
                } else if(callingFrom.equals("registration")){
                    startActivity(new Intent(context, DashboardActivity.class));
                }else if(callingFrom.equals("DeliveryAddress")){
                    onItemSelection.onItemSelected("",0);
                }

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {

                try {
                    Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Invalid Code.",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(context,"Invalid Code.",Toast.LENGTH_LONG).show();
                }
                //Constants.userLogin = true;
                //startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }

        });
    }

    private void loadResendEmail() {
        Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();
        APIManager.getInstance().resendEmail(new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                //Toast.makeText(context, "Email Verification Code sended to your Email Succesfully", Toast.LENGTH_SHORT).show();
                try {
                    Toast.makeText(context, response.getAsJsonObject().get("message").toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
               // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                try {
                    Toast.makeText(context, ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });

    }

}

