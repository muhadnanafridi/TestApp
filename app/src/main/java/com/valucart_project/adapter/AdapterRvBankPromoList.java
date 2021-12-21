package com.valucart_project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnAddBYOBToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Products;
import com.valucart_project.models.PromoOffers;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddBundleCartView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterRvBankPromoList extends RecyclerView.Adapter<AdapterRvBankPromoList.GroceryViewHolder>  {

    private ArrayList<PromoOffers.Data> horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    Library library;

    public AdapterRvBankPromoList(ArrayList<PromoOffers.Data> horizontalGrocderyList, Context context, OnItemSelection onItemSelection1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_promo_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }


    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {

            library.displayImage(horizontalGrocderyList.get(position).getImage() + "?w=151",holder.ivBrand);
            holder.llBankPromo.setBackgroundResource(R.drawable.bg_rounded);
            holder.llBankPromo.setBackgroundColor(Color.parseColor(horizontalGrocderyList.get(position).getColor_code()));

            holder.tvDiscount.setText(horizontalGrocderyList.get(position).getTitle());
            holder.tvDescription.setText("" + horizontalGrocderyList.get(position).getDescription());
            holder.llOffers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected("promoCode", position);
                }
            });

        } catch (Exception e) {}
    }


    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }


    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBrand;
        TextView tvDiscount, tvDescription;
        LinearLayout llBankPromo,llOffers;

        public GroceryViewHolder(View view) {
            super(view);
            ivBrand = view.findViewById(R.id.ivBrand);

            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvDescription = view.findViewById(R.id.tvDescription);

            llBankPromo = view.findViewById(R.id.llBankPromo);
            llOffers = view.findViewById(R.id.llOffers);
        }

    }

}

