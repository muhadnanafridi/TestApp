package com.valucart_project.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnAddBYOBToCartSelection;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.models.Products;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.AddBundleCartView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class AdapterRvBYOBVerticalList extends RecyclerView.Adapter<AdapterRvBYOBVerticalList.GroceryViewHolder> implements OnAddBYOBToCartSelection {

    private ArrayList<Products.Data> horizontalGrocderyList;
    Context context;
    OnAddBYOBToCartSelection onItemSelection;
    WishItemSelection wishItemSelection;
    Library library;

    public AdapterRvBYOBVerticalList(ArrayList<Products.Data> horizontalGrocderyList, Context context, OnAddBYOBToCartSelection onItemSelection1, WishItemSelection wishItemSelection1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        wishItemSelection = wishItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.build_your_own_bundle_vertical_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    //ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {

            //holder.ivProductImage.setImageResource(horizontalGrocderyList.get(position).getProductImage());
            //String imageUrl = ""+horizontalGrocderyList.get(position).getThumbnail()+"?w=30";
            //String imageUrl = "http://testing.v2.api.valucart.com/img/products/26169074195/3D0A2260copy.jpg?w=30";
            //String imageUrl = "https://i.imgur.com/H981AN7.jpg";
            //Picasso.with(context).load(horizontalGrocderyList.get(position).getThumbnail() + "?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);
            library.displayImage(horizontalGrocderyList.get(position).getThumbnail() + "?w=151",holder.ivProductImage);

            //imageLoader.displayImage(imageUrl, holder.ivProductImage);

            holder.tvProductName.setText(horizontalGrocderyList.get(position).getName());
            holder.tvProductPrice.setText("" + horizontalGrocderyList.get(position).getValucart_price() + " AED");
            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String productName = horizontalGrocderyList.get(position).getName().toString();
                    /*Intent intent = new Intent(context, ProductDetailScreen.class);
                    intent.putExtra("id",""+horizontalGrocderyList.get(position).getId());
                    intent.putExtra("name",""+horizontalGrocderyList.get(position).getName());
                    intent.putExtra("subcategory_id",""+horizontalGrocderyList.get(position).getSubcategory_id());
                    intent.putExtra("description",""+horizontalGrocderyList.get(position).getDescription());
                    intent.putExtra("packaging_quantity",""+horizontalGrocderyList.get(position).getPackaging_quantity());
                    intent.putExtra("valucart_price",""+horizontalGrocderyList.get(position).getValucart_price());
                    intent.putExtra("maximum_selling_price",""+horizontalGrocderyList.get(position).getMaximum_selling_price());
                    intent.putExtra("thumbnail",""+horizontalGrocderyList.get(position).getThumbnail());
                    intent.putStringArrayListExtra("ImagesList",horizontalGrocderyList.get(position).getImages());
                    intent.putExtra("packaging_quantity_unit_Symbal",""+horizontalGrocderyList.get(position).getPackaging_quantity_unit().getSymbol());
                    context.startActivity(intent);*/
                }
            });
            holder.ivWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Constants.access_token.equals("")) {
                        if (!horizontalGrocderyList.get(position).getIs_wishList()) {
                            holder.ivWish.setImageResource(R.drawable.ic_selected_heart);
                            horizontalGrocderyList.get(position).setIs_wishList(true);
                            wishItemSelection.onWishValueSelected("", position, "add");
                        } else {
                            holder.ivWish.setImageResource(R.drawable.ic_heart);
                            horizontalGrocderyList.get(position).setIs_wishList(false);
                            wishItemSelection.onWishValueSelected("", position, "remove");
                        }
                    } else {
                        Toast.makeText(context, "Login First before adding any item to wishlist", Toast.LENGTH_LONG).show();
                    }
                }
            });

            holder.addCartView.initInterface(this, position, horizontalGrocderyList.get(position).getId(), "product",""+horizontalGrocderyList.get(position).getValucart_price(),""+horizontalGrocderyList.get(position).getName());
            if (horizontalGrocderyList.get(position).getAddToByob() != 0) {
                holder.addCartView.addItemListener(horizontalGrocderyList.get(position).getAddToByob());
            }
            holder.tvWeight.setText(horizontalGrocderyList.get(position).getPackaging_quantity() + horizontalGrocderyList.get(position).getPackaging_quantity_unit().getSymbol());

            if (horizontalGrocderyList.get(position).getIs_offer()) {
                holder.addCartView.changeLimit(1);
            }

            if (horizontalGrocderyList.get(position).getInventory() > 0) {
                holder.addCartView.showAddToCard();
            } else {
                holder.addCartView.outOfStock();
            }

            if (horizontalGrocderyList.get(position).getMaximum_selling_price().equals((horizontalGrocderyList.get(position).getValucart_price()))) {
                holder.tvOldPrice.setText("");
                //holder.tvOldPrice.setVisibility(View.GONE);
                holder.llOnOffer.setVisibility(View.INVISIBLE);
                holder.tvBundleOff.setText("");
            } else {
                holder.tvOldPrice.setText(horizontalGrocderyList.get(position).getMaximum_selling_price() + " AED");
                holder.llOnOffer.setVisibility(View.VISIBLE);
                holder.tvBundleOff.setText(""+horizontalGrocderyList.get(position).getPercentage_discount()+" % Off");
            }

            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } catch (Exception e) {
            holder.tvOldPrice.setText("");
        }
    }


    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    @Override
    public void onAddToCart(ByobSummary byobSummarylist, String action) {
        onItemSelection.onAddToCart(byobSummarylist, action);
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage, ivWish;
        TextView tvProductName, tvProductPrice, tvWeight,tvOldPrice,tvBundleOff;
        LinearLayout llItemHeader,llOnOffer;
        AddBundleCartView addCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            llItemHeader = view.findViewById(R.id.llItemHeader);
            ivWish = view.findViewById(R.id.ivWish);
            addCartView = view.findViewById(R.id.addCartView);
            tvProductPrice = view.findViewById(R.id.tvProductPrice);
            tvWeight = view.findViewById(R.id.tvWeight);

            tvOldPrice=view.findViewById(R.id.tvOldPrice);
            llOnOffer=view.findViewById(R.id.llOnOffer);
            tvBundleOff=view.findViewById(R.id.tvBundleOff);
        }

    }

}

