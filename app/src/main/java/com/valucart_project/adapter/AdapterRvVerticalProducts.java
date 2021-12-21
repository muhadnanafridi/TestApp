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
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddCartView;

import java.util.ArrayList;

public class AdapterRvVerticalProducts extends RecyclerView.Adapter<AdapterRvVerticalProducts.GroceryViewHolder> implements OnAddToCartSelection {

    private ArrayList<Products.Data> prodDataArrayList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem;
    WishItemSelection wishItemSelection;
    Library library;

    public AdapterRvVerticalProducts(ArrayList<Products.Data> data1, Context context, String nameItem1 , OnItemSelection onItemSelection1, WishItemSelection wishItemSelection1){
        this.prodDataArrayList= data1;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem = nameItem1;
        wishItemSelection = wishItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_grocery_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {
            //String url = prodDataArrayList.get(position).getThumbnail() + "?w=151";
            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected(nameItem, position);
                }
            });
            library.displayImage(prodDataArrayList.get(position).getThumbnail() + "?w=151",holder.ivProductImage);
            //Picasso.with(context).load(url.replaceAll(" ", "%20")).into(holder.ivProductImage);
            holder.tvProductName.setText(prodDataArrayList.get(position).getName());
            holder.tvProductPrice.setText(prodDataArrayList.get(position).getValucart_price() + " AED");

            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.ivWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Constants.access_token.equals("")) {
                        if (!prodDataArrayList.get(position).getIs_wishList()) {
                            holder.ivWish.setImageResource(R.drawable.ic_selected_heart);
                            prodDataArrayList.get(position).setIs_wishList(true);
                            wishItemSelection.onWishValueSelected(nameItem, position, "add");
                        } else {
                            holder.ivWish.setImageResource(R.drawable.ic_heart);
                            prodDataArrayList.get(position).setIs_wishList(false);
                            wishItemSelection.onWishValueSelected(nameItem, position, "remove");
                        }
                    } else {
                        library.alertErrorMessage("Login First before adding any item to wishlist");
                        //Toast.makeText(context, "Login First before adding any item to wishlist", Toast.LENGTH_LONG).show();
                    }
                }
            });
            holder.addCartView.initInterface(this, position,prodDataArrayList.get(position).getId(),"product",""+prodDataArrayList.get(position).getValucart_price(),""+prodDataArrayList.get(position).getName());

            //if (prodDataArrayList.get(position).getPackaging_quantity_unit() == null)
            //else
            //   holder.tvWeight.setText(new java.text.DecimalFormat("#").format(prodDataArrayList.get(position).getPackaging_quantity()));

            if (prodDataArrayList.get(position).getMaximum_selling_price().equals((prodDataArrayList.get(position).getValucart_price()))) {
                holder.tvOldPrice.setText("");
                //holder.tvOldPrice.setVisibility(View.GONE);
                holder.llOnOffer.setVisibility(View.INVISIBLE);
                holder.tvBundleOff.setText("");
            } else {
                holder.tvOldPrice.setText(prodDataArrayList.get(position).getMaximum_selling_price() + " AED");
                holder.llOnOffer.setVisibility(View.VISIBLE);
                holder.tvBundleOff.setText(""+prodDataArrayList.get(position).getPercentage_discount()+" % Off");
            }

            //holder.tvWeight.setText(new java.text.DecimalFormat("#").format(prodDataArrayList.get(position).getPackaging_quantity()) + prodDataArrayList.get(position).getPackaging_quantity_unit().getSymbol());
            if(prodDataArrayList.get(position).getIs_bulk() && prodDataArrayList.get(position).getBulk_quantity()>0)
                holder.tvWeight.setText(""+prodDataArrayList.get(position).getPackaging_quantity() + prodDataArrayList.get(position).getPackaging_quantity_unit().getSymbol()+" x "+prodDataArrayList.get(position).getBulk_quantity());
            else
                holder.tvWeight.setText(""+prodDataArrayList.get(position).getPackaging_quantity() + prodDataArrayList.get(position).getPackaging_quantity_unit().getSymbol());

            if(prodDataArrayList.get(position).getIs_offer()){
                holder.addCartView.changeLimit(1);
            }

            if(prodDataArrayList.get(position).getInventory()>0){
                holder.addCartView.showAddToCard();
            }else {
                holder.addCartView.outOfStock();
            }

        }catch (Exception e){
            holder.tvOldPrice.setText("");
            holder.tvWeight.setText("");

            //holder.tvOldPrice.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return prodDataArrayList.size();
    }

    @Override
    public void onAddToCart(String value, int position) {
        onItemSelection.onItemSelected(value,position);
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage,ivWish;
        TextView tvProductName,tvProductPrice,tvWeight,tvOldPrice,tvBundleOff;
        LinearLayout llItemHeader,llOnOffer;
        AddCartView addCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            tvProductPrice=view.findViewById(R.id.tvProductPrice);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            ivWish=view.findViewById(R.id.ivWish);
            addCartView=view.findViewById(R.id.addCartView);
            tvWeight=view.findViewById(R.id.tvWeight);
            tvOldPrice=view.findViewById(R.id.tvOldPrice);
            llOnOffer=view.findViewById(R.id.llOnOffer);
            tvBundleOff=view.findViewById(R.id.tvBundleOff);
        }

    }

}

