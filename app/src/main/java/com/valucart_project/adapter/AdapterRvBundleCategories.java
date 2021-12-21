package com.valucart_project.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Categories;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class AdapterRvBundleCategories extends RecyclerView.Adapter<AdapterRvBundleCategories.GroceryViewHolder> {
    private Categories horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem = "";

    public AdapterRvBundleCategories(Categories horizontalGrocderyList, Context context, OnItemSelection onItemSelection1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem= "";
    }

    public AdapterRvBundleCategories(Categories horizontalGrocderyList, Context context, OnItemSelection onItemSelection1, String name1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem= name1;
    }


    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizental_categories_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {
            holder.tvItemName.setText(horizontalGrocderyList.getData().get(position).getName().substring(0,1).toUpperCase() + horizontalGrocderyList.getData().get(position).getName().substring(1));
            if (!horizontalGrocderyList.getData().get(position).getSelectedItem()) {
                holder.rlItemName.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                holder.tvItemName.setTextColor(context.getResources().getColor(R.color.editFieldLabel));
            } else {
                holder.rlItemName.setBackgroundResource(R.drawable.bg_rounded_purple_border);
                holder.tvItemName.setTextColor(context.getResources().getColor(R.color.colorAppLogo));
            }
            //holder.cvItem.setRadius(20);
            holder.rlItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nameItem.equals(""))
                        onItemSelection.onItemSelected("ShopByCommunityCategory", position);
                    else
                        onItemSelection.onItemSelected(nameItem, position);
                }
            });
        }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        RelativeLayout rlItemName;

        public GroceryViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvItemName);
            rlItemName = view.findViewById(R.id.rlItemName);
        }
    }
}
