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
import com.valucart_project.models.Intervals;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class AdapterRvInterval extends RecyclerView.Adapter<AdapterRvInterval.GroceryViewHolder> {
    private Intervals intervalsList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem = "";

    public AdapterRvInterval(Intervals intervalsList1, Context context, OnItemSelection onItemSelection1) {
        this.intervalsList = intervalsList1;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem= "";
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

        holder.tvItemName.setText(intervalsList.getData().get(position).getName().toUpperCase());
        if (!intervalsList.getData().get(position).getSelectedItem()) {
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
                    onItemSelection.onItemSelected("interval", position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(intervalsList.getData()==null){
            return 0;
        }else
            return intervalsList.getData().size();
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
