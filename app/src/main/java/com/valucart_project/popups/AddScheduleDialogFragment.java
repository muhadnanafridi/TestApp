package com.valucart_project.popups;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.DateTimeChangeListener;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class AddScheduleDialogFragment extends DialogFragment {

    Context context;
    private boolean isViewOnly;
    TextView tvDate;
    SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DateFormat);
    LinearLayout llDate;
    ImageView ivCancel;

    public static void showPopup( FragmentActivity activity) {
        LogoutDialogFragment residentNotesDialogFragment = new LogoutDialogFragment( activity);
        residentNotesDialogFragment.isViewOnly = true;

        residentNotesDialogFragment.show(activity.getSupportFragmentManager(),"");
    }

    @SuppressLint("ValidFragment")
    public AddScheduleDialogFragment(Context contextrecord) {
        context = contextrecord;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.add_schedule_screen, container, false);

        //riskAssessmentData.setQuestion(carePlanData.getQuestion());

        initView(view);

        return view;
    }

    private void initView(View view) {

        ivCancel =  view.findViewById(R.id.ivCancel);
        tvDate =  view.findViewById(R.id.tvDate);

        RelativeLayout rlOk =  view.findViewById(R.id.rlOk);
        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(context, DashboardActivity.class));
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(context, DashboardActivity.class));
            }
        });

        llDate = view.findViewById(R.id.llDate);
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateTimeDialogFragment = new DateDialogFragment();
                dateTimeDialogFragment.show(getFragmentManager(), "");
                dateTimeDialogFragment.setMinDate(Calendar.getInstance().getTimeInMillis());
                dateTimeDialogFragment.setDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void onDateTimeChange(String dateTime) {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date newDate = format.parse(dateTime);
                            tvDate.setText(dateFormatter.format(newDate));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDate.performClick();
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

