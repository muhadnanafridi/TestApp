package com.valucart_project.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.valucart_project.models.Products;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddCartView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRvFeaturesHorizantalList extends RecyclerView.Adapter<AdapterRvFeaturesHorizantalList.GroceryViewHolder> implements OnAddToCartSelection {

    private ArrayList<Products.Data> horizontal1List;
    Context context;
    WishItemSelection wishItemSelection;
    String itemValue;
    OnItemSelection onItemSelection;
    Library library;

    public AdapterRvFeaturesHorizantalList(ArrayList<Products.Data> horizontalList1, Context context, String itemValue1, WishItemSelection wishItemSelection1, OnItemSelection onItemSelection1) {
        this.horizontal1List = horizontalList1;
        this.context = context;
        wishItemSelection = wishItemSelection1;
        itemValue = itemValue1;
        onItemSelection = onItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_feature_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {
            /*Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener() {

                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("Picasso Error", "Errored out, hiding view");
                }

            });*/
            library.displayImage(horizontal1List.get(position).getThumbnail() + "?w=151",holder.ivProductImage);
            //Picasso.with(context).load(horizontal1List.get(position).getThumbnail() + "?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);

            holder.tvProductName.setText(horizontal1List.get(position).getName().substring(0, 1).toUpperCase() + horizontal1List.get(position).getName().substring(1));
            holder.tvProductPrice.setText("" + horizontal1List.get(position).getValucart_price() + "  AED");
            holder.ivWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Constants.access_token.equals("")) {
                        if (!horizontal1List.get(position).getIs_wishList()) {
                            holder.ivWish.setImageResource(R.drawable.ic_selected_heart);
                            horizontal1List.get(position).setIs_wishList(true);
                            wishItemSelection.onWishValueSelected(itemValue, position, "add");
                        } else {
                            holder.ivWish.setImageResource(R.drawable.ic_heart);
                            horizontal1List.get(position).setIs_wishList(false);
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
                    String productName = horizontal1List.get(position).getName().toString();
                    //Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, BundleListingFragment.class));
                    onItemSelection.onItemSelected(itemValue, position);
                }
            });
            holder.addCartView.initInterface(this, position,horizontal1List.get(position).getId(),"product",""+horizontal1List.get(position).getValucart_price(),""+horizontal1List.get(position).getName());

            holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (horizontal1List.get(position).getMaximum_selling_price().equals((horizontal1List.get(position).getValucart_price()))) {
                holder.llOnOffer.setVisibility(View.INVISIBLE);
                holder.tvProductOldPrice.setText("");
                holder.tvBundleOff.setText("");
            } else {
                holder.llOnOffer.setVisibility(View.VISIBLE);
                holder.tvProductOldPrice.setText(horizontal1List.get(position).getMaximum_selling_price() + " AED");
                holder.tvBundleOff.setText(""+horizontal1List.get(position).getPercentage_discount()+" % Off");
            }


            holder.tvWeight.setText(""+horizontal1List.get(position).getPackaging_quantity() + horizontal1List.get(position).getPackaging_quantity_unit().getSymbol());

            if (horizontal1List.get(position).getIs_offer()) {
                holder.addCartView.changeLimit(1);
            }

            if(horizontal1List.get(position).getInventory()>0){
                holder.addCartView.showAddToCard();
            }else {
                holder.addCartView.outOfStock();
            }

        } catch (Exception e) {}
    }

    @Override
    public int getItemCount() {
        return horizontal1List.size();
    }

    @Override
    public void onAddToCart(String value, int position) {
        if (value.equals("AddToCartAdd")) {
            //loadCart("add", horizontal1List.get(position).getId(), "product");
        } else {
            //loadCart("subtract", horizontal1List.get(position).getId(), "product");
        }
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage, ivWish;
        TextView tvProductName, tvProductOldPrice, tvProductPrice, tvWeight,tvBundleOff;
        LinearLayout llProductItem, llOnOffer;
        AddCartView addCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvProductOldPrice = view.findViewById(R.id.tvProductOldPrice);
            tvProductPrice = view.findViewById(R.id.tvProductPrice);
            tvWeight = view.findViewById(R.id.tvWeight);
            llProductItem = view.findViewById(R.id.llProductItem);
            ivWish = view.findViewById(R.id.ivWish);
            addCartView = view.findViewById(R.id.addCartView);
            llOnOffer = view.findViewById(R.id.llOnOffer);
            tvBundleOff = view.findViewById(R.id.tvBundleOff);
        }

    }

    public static void displayTotalCart(Context context, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "" + Constants.totalCart);
        editor.putString("cart_id", "" + Constants.cart_id);
        editor.apply();

    }

}

