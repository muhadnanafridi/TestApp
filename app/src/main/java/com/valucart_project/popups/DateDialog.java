package com.valucart_project.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import com.valucart_project.R;
import java.util.Calendar;

public class DateDialog extends Dialog implements View.OnClickListener {

    private DatePicker mDate;
    DateDialogFragment dateTimeDialogFragment;
    private OnDateChangedListener onDateChangedListener;

    public DateDialog(Context context, DateDialogFragment dateTimeDialogFragmentL) {
        super(context);
        dateTimeDialogFragment = dateTimeDialogFragmentL;
        setContentView(R.layout.date_dialog);
        mDate = (DatePicker) findViewById(R.id.datePicker);
        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);
        setTitle("");
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                dateTimeDialogFragment.setDateTime();
                dismiss();
            case R.id.cancel:
                dismiss();
        }
    }

    public void setDateListener(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener date) {
        onDateChangedListener=date;
        mDate.init(year, monthOfYear, dayOfMonth, date);
    }

    public void setMinDate(long minDate) {
        mDate.setMinDate(minDate);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(minDate);
        mDate.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),onDateChangedListener);
    }

    public void setMaxDate(long maxDate) {
        mDate.setMaxDate(maxDate);
    }
}
