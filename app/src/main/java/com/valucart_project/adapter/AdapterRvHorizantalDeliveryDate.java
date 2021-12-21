package com.valucart_project.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.valucart_project.R;
import com.valucart_project.interfaces.WishItemSelection;
import com.valucart_project.models.DeliveryDate;
import com.valucart_project.utils.Library;

import java.util.List;

public class AdapterRvHorizantalDeliveryDate extends RecyclerView.Adapter<AdapterRvHorizantalDeliveryDate.GroceryViewHolder>{
    private List<DeliveryDate> deliveryDateList;
    Context context;
    WishItemSelection wishItemSelection;
    String itemValue;
    Library library;

    public AdapterRvHorizantalDeliveryDate(List<DeliveryDate> deliveryDateList1, Context context, String itemValue1 , WishItemSelection wishItemSelection1){
        this.deliveryDateList= deliveryDateList1;
        this.context = context;
        wishItemSelection = wishItemSelection1;
        itemValue = itemValue1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_delivery_time_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        if(deliveryDateList.get(position).getSelectedItem()) {
            if(library.IsTablet()) {
                holder.tvDay.setTextSize(24);
                holder.tvDate.setTextSize(24);
            }else {
                holder.tvDay.setTextSize(20);
                holder.tvDate.setTextSize(20);
            }
            holder.rlProductItem.setBackgroundResource(R.color.colorAppLogo);
            holder.tvDay.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
        }else {
            if(library.IsTablet()) {
                holder.tvDay.setTextSize(21);
                holder.tvDate.setTextSize(21);
            }else {
                holder.tvDay.setTextSize(17);
                holder.tvDate.setTextSize(17);
            }
            holder.rlProductItem.setBackgroundResource(R.color.colorClearWhite);
            holder.tvDay.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.colorAppLogo));
        }

        holder.tvDay.setText(""+deliveryDateList.get(position).getDay());
        holder.tvDate.setText(""+deliveryDateList.get(position).getDate());

        holder.rlProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = deliveryDateList.get(position).getProductName().toString();
                //Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context, BundleListingFragment.class));
                for(int counter = 0 ;counter<7;counter++){
                    if(counter==position)
                        deliveryDateList.get(counter).setSelectedItem(true);
                    else
                        deliveryDateList.get(counter).setSelectedItem(false);
                }
                notifyDataSetChanged();
                wishItemSelection.onWishValueSelected(itemValue,position ,"action");
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryDateList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay,tvDate;
        RelativeLayout rlProductItem;
        public GroceryViewHolder(View view) {
            super(view);
            tvDay=view.findViewById(R.id.tvDay);
            tvDate=view.findViewById(R.id.tvDate);
            rlProductItem=view.findViewById(R.id.rlProductItem);
        }
    }
}

