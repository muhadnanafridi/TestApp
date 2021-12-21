package com.valucart_project.screen;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.valucart_project.R;
import com.valucart_project.popups.ChooseStateDialogFragment;

public class ChooseStateFirstScreen extends FragmentActivity implements View.OnClickListener {

    RelativeLayout rlNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_state_first_screen);

        rlNext = findViewById(R.id.rlNext);
        rlNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
            //startActivity(new Intent(ChooseStateFirstScreen.this, ChooseStateSecondScreen.class));
        ChooseStateDialogFragment chooseStateDialogFragment = new ChooseStateDialogFragment( this);
        chooseStateDialogFragment.show(getSupportFragmentManager(),"");
    }
}
