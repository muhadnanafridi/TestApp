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
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.CustomerBundles;

public class AdapterRvMyBundles extends RecyclerView.Adapter<AdapterRvMyBundles.GroceryViewHolder>{

    private CustomerBundles horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;

    public AdapterRvMyBundles(CustomerBundles horizontalGrocderyList, Context context, OnItemSelection onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bundles_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        try {
            holder.ivProductImage.setImageResource(R.mipmap.ic_add_byob);
            holder.tvProductName.setText(horizontalGrocderyList.getData().get(position).getName().replaceAll("%20", " "));
            holder.tvDate.setText(horizontalGrocderyList.getData().get(position).getCreated_at());
            holder.tvQuantity.setText("" + horizontalGrocderyList.getData().get(position).getProducts().size());
            holder.tvProductPrice.setText(""+horizontalGrocderyList.getData().get(position).getMaximum_selling_price()+ " AED");
/*
        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
            }
        });
*/
            holder.llEditBundle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected("EditBundle", position);
                }
            });

            holder.llAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected("AddToCart", position);
                }
            });

            holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //holder.tvProductOldPrice.setText(""+horizontalGrocderyList.getData().get(position).get);

            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected("DeleteBundle", position);
                }
            });


        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName,tvProductOldPrice,tvDate,tvQuantity,tvProductPrice,tvRemove;
        LinearLayout llItemHeader,llEditBundle,llAddToCart,llDelete;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            tvProductOldPrice=view.findViewById(R.id.tvProductOldPrice);
            tvDate=view.findViewById(R.id.tvDate);
            tvQuantity=view.findViewById(R.id.tvQuantity);
            tvProductPrice=view.findViewById(R.id.tvProductPrice);
            tvRemove=view.findViewById(R.id.tvRemove);

            llEditBundle=view.findViewById(R.id.llEditBundle);
            llAddToCart=view.findViewById(R.id.llAddToCart);
            llDelete=view.findViewById(R.id.llDelete);
        }

    }

}

