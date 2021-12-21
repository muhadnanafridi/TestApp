package com.valucart_project.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.valucart_project.R;
import com.valucart_project.utils.Library;

import java.util.List;

public class AdapterProductDetailSlider extends PagerAdapter {

    private Context context;
    private List<String> imagesList;
    String url;
    Library library;

    public AdapterProductDetailSlider( Context context , List<String> imagesList1) {
        this.context = context;
        this.imagesList = imagesList1;
        library = new Library(context);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_slider_item, null);

        ImageView ivProduct = view.findViewById(R.id.ivProduct);

        //Picasso.with(context).load(imagesList.get(position).toString()+"?w=300".replaceAll(" ", "%20")).into(ivProduct);
        library.displayImage(imagesList.get(position).toString() + "?w=300",ivProduct);

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
