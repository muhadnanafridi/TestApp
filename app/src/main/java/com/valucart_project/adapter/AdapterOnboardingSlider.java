package com.valucart_project.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.valucart_project.R;
import java.util.List;

public class AdapterOnboardingSlider extends PagerAdapter {

    private Context context;
    private List<Integer> color;
    private List<String> colorName;

    public AdapterOnboardingSlider(Context context, List<Integer> color, List<String> colorName) {
        this.context = context;
        this.color = color;
        this.colorName = colorName;
    }

    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.onboarding_slider_item, null);

        LinearLayout llPartner = view.findViewById(R.id.llPartner);
        LinearLayout llBulk = view.findViewById(R.id.llBulk);
        LinearLayout llbyob = view.findViewById(R.id.llbyob);

        if(position==10){
            llbyob.setVisibility(View.VISIBLE);
            llBulk.setVisibility(View.GONE);
            llPartner.setVisibility(View.GONE);
        }else if(position==0){
            llbyob.setVisibility(View.GONE);
            llBulk.setVisibility(View.VISIBLE);
            llPartner.setVisibility(View.GONE);
        }else {
            llbyob.setVisibility(View.GONE);
            llBulk.setVisibility(View.GONE);
            llPartner.setVisibility(View.VISIBLE);
        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}

