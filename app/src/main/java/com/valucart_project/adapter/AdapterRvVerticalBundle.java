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
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnAddToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.ProductsBundle;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddCartView;

import java.util.ArrayList;

public class AdapterRvVerticalBundle extends RecyclerView.Adapter<AdapterRvVerticalBundle.GroceryViewHolder> implements OnAddToCartSelection {

    private ArrayList<ProductsBundle.Data> prodDataArrayList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem;
    WishItemSelection wishItemSelection;
    Library library;

    public AdapterRvVerticalBundle(ArrayList<ProductsBundle.Data> data1, Context context, String nameItem1 , OnItemSelection onItemSelection1, WishItemSelection wishItemSelection1){
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
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bundle_list_grocery_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {
/*
            String urlImage = Uri.parse(url)
                    .buildUpon()
                    .build()
                    .toString();
*/

            holder.tvProductName.setText(prodDataArrayList.get(position).getName().toUpperCase());
            holder.tvProductPrice.setText(prodDataArrayList.get(position).getValucart_price() + "AED");

            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelection.onItemSelected(nameItem, position);
                }
            });

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
                        Toast.makeText(context, "Login First before adding any item to wishlist", Toast.LENGTH_LONG).show();
                    }

                }
            });

            holder.addCartView.initInterface(this, position,prodDataArrayList.get(position).getId(),"bundle",""+prodDataArrayList.get(position).getValucart_price(),""+prodDataArrayList.get(position).getName());
            //if (prodDataArrayList.get(position).getPackaging_quantity_unit() == null)
            holder.tvDescription.setText(""+prodDataArrayList.get(position).getDescription());
            //else
            //   holder.tvWeight.setText(new java.text.DecimalFormat("#").format(prodDataArrayList.get(position).getPackaging_quantity()));

            String url="";
            if(!prodDataArrayList.get(position).getThumbnail().equals("")) {
                url = prodDataArrayList.get(position).getThumbnail() + "?w=200";
            }else {
                if(prodDataArrayList.get(position).getImages().size()>0)
                url = prodDataArrayList.get(position).getImages().get(0)+ "?w=200";
            }
            holder.tvTotalItems.setText("Total Items : "+prodDataArrayList.get(position).getItem_count());

            //Picasso.with(context).load(url.replaceAll(" ", "%20")).into(holder.ivProductImage);
            library.displayImage(url,holder.ivProductImage);

            if (prodDataArrayList.get(position).getMaximum_selling_price().equals((prodDataArrayList.get(position).getValucart_price()))) {
                holder.tvOldPrice.setText("");
                //holder.tvOldPrice.setVisibility(View.GONE);
                holder.llOnOffer.setVisibility(View.INVISIBLE);
                holder.tvBundleOff.setText("");
            } else {
                holder.tvOldPrice.setText(prodDataArrayList.get(position).getMaximum_selling_price() + "AED");
                holder.llOnOffer.setVisibility(View.VISIBLE);
                holder.tvBundleOff.setText(""+prodDataArrayList.get(position).getPercentage_discount()+" % Off");
            }

            if(prodDataArrayList.get(position).getInventory()>0){
                holder.addCartView.showAddToCard();
            }else {
                holder.addCartView.outOfStock();
            }
            
        }catch (Exception e){
            holder.tvOldPrice.setText("");
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
        TextView tvProductName,tvProductPrice,tvDescription,tvOldPrice,tvBundleOff,tvTotalItems;
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

            tvDescription=view.findViewById(R.id.tvDescription);
            tvOldPrice=view.findViewById(R.id.tvOldPrice);
            tvBundleOff=view.findViewById(R.id.tvBundleOff);
            tvTotalItems=view.findViewById(R.id.tvTotalItems);

            llOnOffer=view.findViewById(R.id.llOnOffer);
        }

    }

}

