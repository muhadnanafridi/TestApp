package com.valucart_project.popups;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import com.valucart_project.interfaces.DateTimeChangeListener;
import java.text.DecimalFormat;
import java.util.Calendar;

public class DateDialogFragment extends DialogFragment implements OnDateChangedListener {

    private int year, monthOfYear, dayOfMonth, hour, minute, minDayOffset;
    private DateTimeChangeListener dateTimeChangeListener;
    DateDialog myDialog;
    long minDateMilisec = 0, maxDateMiliSec = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        monthOfYear = c.get(Calendar.MONTH) + 1;
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        myDialog = new DateDialog(getActivity(), this);
        if (maxDateMiliSec != 0) {
            myDialog.setMaxDate(maxDateMiliSec);
        }
        myDialog.setTitle("");
        myDialog.setDateListener(year, monthOfYear - 1, dayOfMonth, this);
        if (minDateMilisec != 0) {
            myDialog.setMinDate(minDateMilisec);
        }
        return myDialog;
    }

    public void setMinDate(long minDateMilisec) {
        this.minDateMilisec = minDateMilisec;

    }

    public void setMaxDate(long maxDateMilisec) {
        this.maxDateMiliSec = maxDateMilisec;
    }

    public void setDateTimeChangeListener(DateTimeChangeListener dateTimeChangeListener) {
        this.dateTimeChangeListener = dateTimeChangeListener;
    }

    public void setDateTime() {
        dateTimeChangeListener.onDateTimeChange(new DecimalFormat("00").format(dayOfMonth)
                + "-" + new DecimalFormat("00").format(monthOfYear) + "-" + year);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear + 1;
        this.dayOfMonth = dayOfMonth;
        Log.d("onDateChanged", "" + monthOfYear);
    }

}
