package com.valucart_project.screen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.valucart_project.R;

public class PhoneLoginScreen extends Activity implements OnClickListener {

    TextView txtFortgotPassword;
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login_verification_popup);

        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==ivCancel){
            finish();
        }
    }

}

