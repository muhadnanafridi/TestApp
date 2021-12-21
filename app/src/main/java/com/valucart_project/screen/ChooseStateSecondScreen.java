package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.valucart_project.R;

public class ChooseStateSecondScreen extends Activity implements View.OnClickListener {

    RelativeLayout rlNext,rlOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_state_second_screen);

        rlNext = findViewById(R.id.rlNext);
        rlNext.setOnClickListener(this);

        rlOk = findViewById(R.id.rlOk);
        rlOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(ChooseStateSecondScreen.this, DashboardActivity.class));
    }
}
