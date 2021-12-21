package com.valucart_project.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import ss.com.bannerslider.ImageLoadingService;

public class PicassoImageLoadingService implements ImageLoadingService {

    public Context context;
    Library library;

    public PicassoImageLoadingService(Context context) {
        this.context = context;
        library = new Library(context);
    }

    int radius = 15; // corner radius, higher value = more rounded
    int margin = 0; // crop margin, set to 0 for corners with no crop
    ImageView imageView1;

    @Override
    public void loadImage(final String url, @NonNull final ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //int rounded_value = 120;
        GlideApp.with(context)
                .load(url.replace("https", "http"))
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(imageView);

        //Picasso.with(context).load(url).transform(transformation).into(imageView);
/*
        Picasso.with(context).load(url).fit().centerCrop()
                .transform(transformation)
                .into(imageView);
*/
/*        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context)
                        .load(url)
                        .transform(new RoundedTransformation(60, 0))
                        .into(imageView);
            }
        }, 100);*/
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        //final int radius = 55;
        //final int margin = 5;
        //final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideApp.with(context)
                .load(url.replace("https", "http"))
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(imageView);

    }

}

