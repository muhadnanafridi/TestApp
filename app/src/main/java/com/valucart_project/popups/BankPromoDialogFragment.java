package com.valucart_project.popups;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.fragments.DashboardFragment;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Generic;
import com.valucart_project.models.PromoOffers;
import com.valucart_project.screen.BundleSummaryScreen;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.screen.DeliveryDateTimeScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class BankPromoDialogFragment extends DialogFragment {

    Context context;
    boolean isViewOnly;
    static String callingFrom="";
    RelativeLayout rlTitleBar;
    OnItemSelection onItemSelection;
    Library library;
    PromoOffers.Data data;

    public static void showPopup( FragmentActivity activity) {
        BankPromoDialogFragment residentNotesDialogFragment = new BankPromoDialogFragment( activity,callingFrom);
        residentNotesDialogFragment.isViewOnly = true;
        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");
    }

    @SuppressLint("ValidFragment")
    public BankPromoDialogFragment(Context contextrecord, String callingFrom1) {
        context = contextrecord;
        callingFrom = callingFrom1;
        library = new Library(context);
    }

    @SuppressLint("ValidFragment")
    public BankPromoDialogFragment(Context contextrecord, String callingFrom1,PromoOffers.Data data1) {
        context = contextrecord;
        callingFrom = callingFrom1;
        data = data1;
        library = new Library(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.bottom_sheet_bank_promo, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        rlTitleBar = view.findViewById(R.id.rlTitleBar);
        rlTitleBar.setBackgroundColor(Color.parseColor(data.getColor_code()+ ""));
        try {
            TextView tvCopyPromo = view.findViewById(R.id.tvCopyPromo);
            tvCopyPromo.setTextColor(Color.parseColor("" + data.getColor_code()));
        }catch (Exception e){}

        ImageView ivPromo = view.findViewById(R.id.ivPromo);

        TextView tvCode = view.findViewById(R.id.tvCode);
        //tvCode.setText("Promo Code : " + data.getCode());
        String valueHtml = "Promo Code : " + data.getCode()+"";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvCode.setText(Html.fromHtml(valueHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvCode.setText(Html.fromHtml(valueHtml));
        }

        String valueHtmlDesc = data.getDescription();

        WebView wvData = view.findViewById(R.id.wvData);
        wvData.loadDataWithBaseURL(null, valueHtmlDesc, "text/html", "utf-8", null);


        TextView tvDesc = view.findViewById(R.id.tvDesc);

       // valueHtmlDesc = "<h4>An Unordered List:</h4><p> Test akjsfha ajsjhdkjh asjnbdhk</p><ul>  <li>Coffee sdfa sdf f</li> <li>Tea sdfs edfwef</li> <li>Milk sdfwsf</li></ul>";

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //    tvDesc.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
       // } else {
       //     tvDesc.setText(Html.fromHtml(content));
       // }

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("" + data.getTitle());

        library.displayImage(data.getImage() + "?w=151", ivPromo);

        RelativeLayout rlDone = view.findViewById(R.id.rlDone);
        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", data.getCode());
                clipboard.setPrimaryClip(clip);
            }
        });


        view.findViewById(R.id.ivCancel).setOnClickListener(new View.OnClickListener() {
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
        } catch (Exception e) {}
    }


}

