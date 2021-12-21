package com.valucart_project.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.utils.Library;

import java.util.List;

public class AdapterRvPopolarDepartment extends RecyclerView.Adapter<AdapterRvPopolarDepartment.GroceryViewHolder> {
    private List<Grocery> horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem = "";
    String  firstTimeCall = "";
    Library library;

    public AdapterRvPopolarDepartment(List<Grocery> horizontalGrocderyList, Context context, OnItemSelection onItemSelection1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem= "";
        firstTimeCall= "yes";
        library = new Library(context);
    }

    public AdapterRvPopolarDepartment(List<Grocery> horizontalGrocderyList, Context context, OnItemSelection onItemSelection1, String name1) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem= name1;
        library = new Library(context);
    }


    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_popular_departments_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        holder.tvItemName.setText(horizontalGrocderyList.get(position).getProductName());
        //Picasso.with(context).load(horizontalGrocderyList.get(position).getIcon() + "?w=100".replaceAll(" ", "%20")).into(holder.ivPopular);
        library.displayImage(horizontalGrocderyList.get(position).getIcon() + "?w=100",holder.ivPopular);
        //if (horizontalGrocderyList.get(0).getSelectedItem()) {
        //    if(firstTimeCall.equals("yes"))
       // }else {
       //         firstTimeCall= "no";
       // }

        if (!horizontalGrocderyList.get(position).getSelectedItem()) {
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
                if(nameItem.equals(""))
                    onItemSelection.onItemSelected("ShopByCommunityCategory", position);
                else
                    onItemSelection.onItemSelected(nameItem, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        RelativeLayout rlItemName;
        ImageView ivPopular;

        public GroceryViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvItemName);
            rlItemName = view.findViewById(R.id.rlItemName);
            ivPopular = view.findViewById(R.id.ivPopular);
        }
    }
}
