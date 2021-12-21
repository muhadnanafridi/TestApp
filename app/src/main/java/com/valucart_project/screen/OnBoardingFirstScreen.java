package com.valucart_project.screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterOnboardingSlider;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class OnBoardingFirstScreen extends Activity {

    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> color;
    List<String> colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_first_screen);

        TextView tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingFirstScreen.this, OnBoardingSecondScreen.class));
            }
        });

        TextView tvNext = findViewById(R.id.tvNext);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(OnBoardingFirstScreen.this, OnBoardingSecondScreen.class));
                if (viewPager.getCurrentItem() < color.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    // viewPager.setCurrentItem(0);
                    startActivity(new Intent(OnBoardingFirstScreen.this, OnBoardingSecondScreen.class));
                }
            }
        });

        viewPagerController();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void viewPagerController(){

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        indicator=(TabLayout)findViewById(R.id.indicator);
        color = new ArrayList<>();
        color.add(Color.RED);
        color.add(Color.GREEN);
        //color.add(Color.BLUE);

        colorName = new ArrayList<>();
        colorName.add("RED");
        colorName.add("GREEN");
        //colorName.add("BLUE");

        viewPager.setAdapter(new AdapterOnboardingSlider(this, color, colorName));
        indicator.setupWithViewPager(viewPager, true);

        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            OnBoardingFirstScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < color.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}

