package com.valucart_project.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.valucart_project.screen.DashboardActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class ChooseStateDialogFragment extends DialogFragment {

    Context context;
    private boolean isViewOnly;

    public static void showPopup( FragmentActivity activity) {
        ChooseStateDialogFragment residentNotesDialogFragment = new ChooseStateDialogFragment( activity);
        residentNotesDialogFragment.isViewOnly = true;

        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");

    }

    @SuppressLint("ValidFragment")
    public ChooseStateDialogFragment(Context contextrecord) {
        context = contextrecord;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        View view = inflater.inflate(R.layout.choose_state_dialog_fragment, container, false);

        //riskAssessmentData.setQuestion(carePlanData.getQuestion());

        initView(view);

        return view;
    }

    private void initView(View view) {

        //((TextView) view.findViewById(R.id.tvTypeofPad)).setText(bowelData.getTypes_of_pad_used());
        //((TextView) view.findViewById(R.id.tvStoolType)).setText(bowelData.getStool_type());

        view.findViewById(R.id.rlOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(context, DashboardActivity.class));
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

}

