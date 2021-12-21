package com.valucart_project.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Library;

public class AdapterOrderSummary extends RecyclerView.Adapter<AdapterOrderSummary.GroceryViewHolder>{

    private Products horizontalGrocderyList;
    Context context;
    Library library;

    public AdapterOrderSummary(Products horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        try {
            //holder.ivProductImage.setImageResource(horizontalGrocderyList.getData().get(position).getThumbnail());
            if(horizontalGrocderyList.getData().get(position).getProductType().equals("customer_bundle"))
                holder.ivProductImage.setImageResource(R.mipmap.ic_add_byob);
            else
                library.displayImage(horizontalGrocderyList.getData().get(position).getThumbnail() + "?w=151",holder.ivProductImage);
                //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getThumbnail() + "?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);

            holder.tvProductName.setText(horizontalGrocderyList.getData().get(position).getName());
            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                    //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                }
            });

            holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setText("" + horizontalGrocderyList.getData().get(position).getValucart_price()+"AED  ");

            //holder.tvProductOldPrice.setText("" + horizontalGrocderyList.getData().get(position).getMaximum_selling_price()+"AED");
            holder.tvQuantity.setText("" + horizontalGrocderyList.getData().get(position).getQuantity());
            holder.tvDate.setText("" + horizontalGrocderyList.getData().get(position).getDelivery_date());
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName,tvProductOldPrice,tvProductPrice,tvQuantity,tvDate;
        LinearLayout llItemHeader;
        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            tvProductOldPrice=view.findViewById(R.id.tvProductOldPrice);
            tvProductPrice=view.findViewById(R.id.tvProductPrice);
            tvQuantity=view.findViewById(R.id.tvQuantity);
            tvDate=view.findViewById(R.id.tvDate);
        }
    }

}

