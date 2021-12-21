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
import com.valucart_project.interfaces.OnAddToCartSelection;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddCartView;

public class AdapterRvMyWishlist extends RecyclerView.Adapter<AdapterRvMyWishlist.GroceryViewHolder> implements OnAddToCartSelection {

    private Products horizontalGrocderyList;
    Context context;
    Library library;

    public AdapterRvMyWishlist(Products horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_wishlist_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    GroceryViewHolder holderItem;
    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holderItem = holder;
        //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getThumbnail()+"?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);
        library.displayImage(horizontalGrocderyList.getData().get(position).getThumbnail() + "?w=151",holder.ivProductImage);


        if(horizontalGrocderyList.getData().get(position).getName()==null)
            holder.tvProductName.setText("Bundle 1");
        else
            holder.tvProductName.setText(horizontalGrocderyList.getData().get(position).getName());

        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
            }
        });

        if(horizontalGrocderyList.getData().get(position).getProductType().equals("bundle"))
            holder.addCartView.initInterface(this, position,horizontalGrocderyList.getData().get(position).getId(),"bundle",""+horizontalGrocderyList.getData().get(position).getValucart_price(),""+horizontalGrocderyList.getData().get(position).getName());
        else
            holder.addCartView.initInterface(this, position,horizontalGrocderyList.getData().get(position).getId(),"product",""+horizontalGrocderyList.getData().get(position).getValucart_price(),""+horizontalGrocderyList.getData().get(position).getName());
        //holder.addCartView.addItemListener(horizontalGrocderyList.getData().get(position).getQuantity());

        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.tvProductTotalPrice.setText("AED  "+horizontalGrocderyList.getData().get(position).getValucart_price()+"  ");

        try {

            if (horizontalGrocderyList.getData().get(position).getMaximum_selling_price().equals(horizontalGrocderyList.getData().get(position).getValucart_price()))
                holder.tvProductOldPrice.setText("");
            else
                holder.tvProductOldPrice.setText("AED  " + horizontalGrocderyList.getData().get(position).getMaximum_selling_price());

            holder.tvProductWeight.setText("Weight: "+horizontalGrocderyList.getData().get(position).getPackaging_quantity() + horizontalGrocderyList.getData().get(position).getPackaging_quantity_weight());
            if(horizontalGrocderyList.getData().get(position).getInventory()>0){
                holder.addCartView.showAddToCard();
            }else {
                holder.addCartView.outOfStock();
            }

        }catch (Exception e){
            holder.tvProductOldPrice.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    @Override
    public void onAddToCart(String value, int position) {
        if(value.equals("AddToCartAdd")){
            //loadCart("add",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
        }else {
            //loadCart("subtract",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
        }
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName,tvProductOldPrice,tvProductWeight,tvProductTotalPrice;
        LinearLayout llItemHeader;
        AddCartView addCartView;
        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            tvProductOldPrice=view.findViewById(R.id.tvProductOldPrice);
            addCartView=view.findViewById(R.id.addCartView);
            tvProductWeight=view.findViewById(R.id.tvProductWeight);
            tvProductTotalPrice=view.findViewById(R.id.tvProductTotalPrice);
        }
    }


}

