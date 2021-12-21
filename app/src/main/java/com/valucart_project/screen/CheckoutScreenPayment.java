package com.valucart_project.screen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.valucart_project.R;

public class CheckoutScreenPayment extends FragmentActivity {

    RadioGroup rgCard;
    EditText etName,etCard,etExpiredDate,etCvv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_payment_screen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initField();
    }

    public void initField(){
        rgCard = findViewById(R.id.rgCard);

        etName = findViewById(R.id.etName);
        etCard = findViewById(R.id.etCard);
        etExpiredDate = findViewById(R.id.etExpiredDate);
        etCvv = findViewById(R.id.etCvv);

        rgCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                if(checkedRadioButton.getText().equals("Cash on delivery")){
                    etName.setEnabled(false);
                    etCard.setEnabled(false);
                    etExpiredDate.setEnabled(false);
                    etCvv.setEnabled(false);
                }else {
                    etName.setEnabled(true);
                    etCard.setEnabled(true);
                    etExpiredDate.setEnabled(true);
                    etCvv.setEnabled(true);
                }
            }
        });

        RelativeLayout rlDone = findViewById(R.id.rlDone);
        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderSummaryScreen.class));
            }
        });

        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

