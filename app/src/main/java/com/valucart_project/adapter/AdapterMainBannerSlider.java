package com.valucart_project.adapter;

import com.valucart_project.models.Banners;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class AdapterMainBannerSlider extends SliderAdapter {

    Banners banners;

    @Override
    public int getItemCount() {
        return banners.getData().size();
    }

    public AdapterMainBannerSlider (Banners banners1){
        banners = banners1;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        viewHolder.bindImageSlide(banners.getData().get(position).getPortrait()+"?w=500");
           /*  case 0:

                break;
           case 1:
                viewHolder.bindImageSlide("https://s3.eu-central-1.amazonaws.com/assets.valucart.com/product_images/1550575284267_Taj%20Web%20Banner%20Design%20final%20-%20Mo.jpg");
                break;
            case 2:
                viewHolder.bindImageSlide("https://s3.eu-central-1.amazonaws.com/assets.valucart.com/product_images/1549439945255_Paddlers%20Mo%20banner_06_02_2919.jpg");
                break;
            case 3:
                viewHolder.bindImageSlide("https://s3.eu-central-1.amazonaws.com/assets.valucart.com/product_images/1548932111367_2%20%282%29.jpg");
                break;
            case 4:
                viewHolder.bindImageSlide("https://s3.eu-central-1.amazonaws.com/assets.valucart.com/product_images/1552130638042_Persil_360%20Web%20Banner%20Design_07_03_2019.jpg");
                break;*/
    }

}

