package com.valucart_project.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterForSpinner;
import com.valucart_project.interfaces.SpinnerViewSelection;
import org.jetbrains.annotations.NotNull;

public class SpinnerView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String[] textArray;
    private LayoutInflater inflater;
    private EditText edtSpinner;
    private Spinner spinner;
    private SpinnerViewSelection spinnerViewSelection;
    private TextInputLayout textInputLayout;
    int position = 0;

    public SpinnerView(Context context) {
        super(context);
        init(null);
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.spinner_edit_text_view, null);
        edtSpinner = view.findViewById(R.id.edtSpinnerView);
        spinner = view.findViewById(R.id.spnrSpinnerView);
        textInputLayout = view.findViewById(R.id.tilSpinnerView);
        spinner.setOnItemSelectedListener(null);
        edtSpinner.setOnClickListener(this);

        if (attributeSet != null) {
            TypedArray t = getContext().obtainStyledAttributes(attributeSet, R.styleable.SpinnerView);

            Drawable drawable = t.getDrawable(R.styleable.SpinnerView_imgSrc);
            int color = t.getColor(R.styleable.SpinnerView_imgTint, ContextCompat.getColor(getContext(), R.color.colorGray));
            String hint = t.getString(R.styleable.SpinnerView_hint);

            if (drawable != null) {
                drawable.setTint(color);
                edtSpinner.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }

            if (hint != null)
                textInputLayout.setHint(hint);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setOnItemSelectedListener(SpinnerView.this);
            }
        }, 1000);

        addView(view);
    }

    public void setTextArray(String[] textArray) {
        this.textArray = textArray;
        spinner.setAdapter(new AdapterForSpinner(getContext(), R.layout.spinner_item, textArray));
    }

    public void setAdapter(@NotNull ArrayAdapter adapter) {
        spinner.setAdapter(adapter);
    }

    public String getSelectedText() {
        return edtSpinner.getText().toString();
    }

    public void disableView() {
        edtSpinner.setEnabled(false);
    }

    public void enableView() {
        edtSpinner.setEnabled(true);
    }

    public void setValue(String value) {
        edtSpinner.setText(value);
    }

    public void setHint(String hint) {
        textInputLayout.setHint(hint);
    }

    public void setOnValueSelected(SpinnerViewSelection spinnerViewSelection ){
        this.spinnerViewSelection=spinnerViewSelection;
    }
    public void setSelected(int position){
        if(textArray!=null){
            if(textArray.length>0){
                spinner.setSelection(position);
            }
        }
    }

    @Override
    public void onClick(View v) {
        spinner.performClick();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        if (position == 0) {
            if (textArray[position].equals("Select")) {
                edtSpinner.setText("");
            }
        } else if(textArray != null) {
            edtSpinner.setText(textArray[position]);

            if(spinnerViewSelection!=null){
                spinnerViewSelection.onValueSelected(textArray[position],position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getPosition() {
        return position;
    }
}
