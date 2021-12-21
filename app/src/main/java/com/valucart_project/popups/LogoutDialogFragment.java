package com.valucart_project.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.Generic;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.utils.Constants;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class LogoutDialogFragment extends DialogFragment {

    Context context;
    boolean isViewOnly;

    public static void showPopup( FragmentActivity activity) {
        LogoutDialogFragment residentNotesDialogFragment = new LogoutDialogFragment( activity);
        residentNotesDialogFragment.isViewOnly = true;

        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");
    }

    @SuppressLint("ValidFragment")
    public LogoutDialogFragment(Context contextrecord) {
        context = contextrecord;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.logout_popup_fragment, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        view.findViewById(R.id.rlNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        view.findViewById(R.id.rlYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Constants.userLogin = false;
                startActivity(new Intent(context, DashboardActivity.class));
                emptyTotalCart(context);
                loadLogout();
            }
        });

        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        hideKeyBoard();

    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    private void loadLogout() {

        APIManager.getInstance().getLogout(Constants.access_token  , new APIResponseCallback<Generic>() {

            @Override
            public void onResponseLoaded(@NonNull Generic response) {

                Toast.makeText(context,"Logout Successfully",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {

                //Toast.makeText(context,"error.",Toast.LENGTH_LONG).show();
                //Constants.userLogin = true;
                //startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                try {
                    //library.alertErrorMessage(""+jsonObject.get("message".toString()));

                    Toast.makeText(getContext(), ""+jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

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

