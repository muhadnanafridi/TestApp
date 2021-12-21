package com.valucart_project.popups;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.valucart_project.R;
import com.valucart_project.screen.LoginScreen;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class PromoCodeDialog extends DialogFragment {

    Context context;
    boolean isViewOnly;

    public static void showPopup( FragmentActivity activity) {
        PromoCodeDialog residentNotesDialogFragment = new PromoCodeDialog( activity);
        residentNotesDialogFragment.isViewOnly = true;

        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");
    }

    @SuppressLint("ValidFragment")
    public PromoCodeDialog(Context contextrecord) {
        context = contextrecord;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.promo_code_popup, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        view.findViewById(R.id.llPromo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", "First10");
                clipboard.setPrimaryClip(clip);
                dismiss();
                startActivity(new Intent(context, LoginScreen.class));
            }
        });
/*
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
*/

        //hideKeyBoard();
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

}

