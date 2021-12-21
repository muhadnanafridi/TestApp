package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;

public class OnBoardingSecondScreen extends Activity implements View.OnClickListener {

    LinearLayout llDubai,llSharjah,llOther;
    TextView tvOther,tvDubai,tvSharjah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_second_screen);

        llDubai = findViewById(R.id.llDubai);
        llDubai.setOnClickListener(this);

        llSharjah = findViewById(R.id.llSharjah);
        llSharjah.setOnClickListener(this);

        llOther = findViewById(R.id.llOther);
        llOther.setOnClickListener(this);

        tvSharjah= findViewById(R.id.tvSharjah);
        tvDubai= findViewById(R.id.tvDubai);
        tvOther= findViewById(R.id.tvOther);
    }

    @Override
    public void onClick(View view) {

        if(llDubai==view){

            llDubai.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llSharjah.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvDubai.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvSharjah.setTextColor(getResources().getColor(R.color.editFieldLabel));
            tvOther.setTextColor(getResources().getColor(R.color.editFieldLabel));

            //startActivity(new Intent(OnBoardingSecondScreen.this, DashboardActivity.class));
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Intent intent = new Intent(OnBoardingSecondScreen.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        if(llSharjah==view){
            llDubai.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llSharjah.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
            llOther.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);

            tvDubai.setTextColor(getResources().getColor(R.color.editFieldLabel));
            tvSharjah.setTextColor(getResources().getColor(R.color.colorClearWhite));
            tvOther.setTextColor(getResources().getColor(R.color.editFieldLabel));

            Intent intent = new Intent(OnBoardingSecondScreen.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(llOther==view){
            llDubai.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llSharjah.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            llOther.setBackgroundResource(R.drawable.bg_rounded_curve_purple);

            tvDubai.setTextColor(getResources().getColor(R.color.editFieldLabel));
            tvSharjah.setTextColor(getResources().getColor(R.color.editFieldLabel));
            tvOther.setTextColor(getResources().getColor(R.color.colorClearWhite));

            Intent intent = new Intent(OnBoardingSecondScreen.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

}

