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

public class AdapterRvHorizantalPopularBundleList extends RecyclerView.Adapter<AdapterRvHorizantalPopularBundleList.GroceryViewHolder> implements OnAddToCartSelection {

    private ProductsBundle horizontalGrocderyList;
    Context context;
    WishItemSelection wishItemSelection;
    String itemValue;
    OnItemSelection onItemSelection;
    Library library ;

    public AdapterRvHorizantalPopularBundleList(ProductsBundle horizontalGrocderyList, Context context, String itemValue1 , WishItemSelection wishItemSelection1 , OnItemSelection onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        wishItemSelection = wishItemSelection1;
        itemValue = itemValue1;
        onItemSelection = onItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_popular_bundle_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);

        return gvh;
    }

    @Override
    public void onBindViewHolder(final  GroceryViewHolder holder, final int position) {

        //holder.ivProductImage.setImageResource(horizontalGrocderyList.getData().get(position).getProductImage());
        if(horizontalGrocderyList.getData().get(position).getThumbnail() .equals("")) {
            if(horizontalGrocderyList.getData().get(position).getImages().size()>0)
                library.displayImage(horizontalGrocderyList.getData().get(position).getImages().get(0)  + "?w=250",holder.ivProductImage);
        //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getImages().get(0) + "?w=250".replace("https", "http")).into(holder.ivProductImage);
        }
        else
            library.displayImage(horizontalGrocderyList.getData().get(position).getThumbnail()  + "?w=250",holder.ivProductImage);

        //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getThumbnail() + "?w=250".replace("https", "http")).into(holder.ivProductImage);

        holder.tvProductName.setText(horizontalGrocderyList.getData().get(position).getName().toUpperCase());
        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ivWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.access_token.equals("")) {
                    if (!horizontalGrocderyList.getData().get(position).getIs_wishList()) {
                        holder.ivWish.setImageResource(R.drawable.ic_selected_heart);
                        horizontalGrocderyList.getData().get(position).setIs_wishList(true);
                        wishItemSelection.onWishValueSelected(itemValue, position, "add");
                    } else {
                        holder.ivWish.setImageResource(R.drawable.ic_heart);
                        horizontalGrocderyList.getData().get(position).setIs_wishList(false);
                        wishItemSelection.onWishValueSelected(itemValue, position, "remove");
                    }
                } else {
                    Toast.makeText(context, "Login First before adding any item to wishlist", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.llProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = horizontalGrocderyList.getData().get(position).getName().toString();
                //Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context, BundleListingFragment.class));
                onItemSelection.onItemSelected(itemValue,position );
            }
        });
        holder.addCartView.initInterface(this, position,horizontalGrocderyList.getData().get(position).getId(),"bundle",""+horizontalGrocderyList.getData().get(position).getValucart_price(),""+horizontalGrocderyList.getData().get(position).getName());
        //holder.tvWeigh.setText(new java.text.DecimalFormat("#").format(horizontalGrocderyList.getData().get(position).getPackaging_quantity()) + horizontalGrocderyList.getData().get(position).getPackaging_quantity_unit().getSymbol());
        holder.tvWeigh.setVisibility(View.GONE);
        holder.tvProductPrice.setText( horizontalGrocderyList.getData().get(position).getValucart_price().toString()+" AED");
        holder.tvDesc.setText( horizontalGrocderyList.getData().get(position).getDescription().toString()+"  ");
        if (horizontalGrocderyList.getData().get(position).getMaximum_selling_price().equals((horizontalGrocderyList.getData().get(position).getValucart_price()))) {
            holder.tvOldPrice.setText("");
            //holder.tvOldPrice.setVisibility(View.GONE);
            holder.llOnOffer.setVisibility(View.INVISIBLE);
            holder.tvBundleOff.setText("");
        } else {
            holder.tvOldPrice.setText(horizontalGrocderyList.getData().get(position).getMaximum_selling_price() + " AED");
            holder.llOnOffer.setVisibility(View.VISIBLE);
            holder.tvBundleOff.setText(""+horizontalGrocderyList.getData().get(position).getPercentage_discount()+" % Off");
        }
        holder.tvTotalItems.setText("Total Items : "+horizontalGrocderyList.getData().get(position).getItem_count());
        if(horizontalGrocderyList.getData().get(position).getInventory()>0){
            holder.addCartView.showAddToCard();
        }else {
            holder.addCartView.outOfStock();
        }

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    @Override
    public void onAddToCart(String value, int position) {
        if(value.equals("AddToCartAdd")){
            //loadCart("add",horizontalGrocderyList.getData().get(position).getId(),"bundle");
        }else {
            //loadCart("subtract",horizontalGrocderyList.getData().get(position).getId(),"bundle");
        }
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage,ivWish;
        TextView tvProductName,tvWeigh,tvProductPrice,tvDesc,tvOldPrice,tvBundleOff,tvTotalItems;
        LinearLayout llProductItem,llOnOffer;
        AddCartView addCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            tvWeigh=view.findViewById(R.id.tvWeight);
            tvDesc=view.findViewById(R.id.tvDesc);
            tvProductPrice=view.findViewById(R.id.tvProductPrice);

            llProductItem=view.findViewById(R.id.llProductItem);
            ivWish=view.findViewById(R.id.ivWish);
            addCartView=view.findViewById(R.id.addCartView);

            tvOldPrice=view.findViewById(R.id.tvOldPrice);
            tvBundleOff=view.findViewById(R.id.tvBundleOff);
            tvTotalItems=view.findViewById(R.id.tvTotalItems);
            llOnOffer=view.findViewById(R.id.llOnOffer);
        }
    }

}

