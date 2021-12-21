package com.valucart_project.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import java.util.List;

public class AdapterRvSearchFilterCategories extends RecyclerView.Adapter<AdapterRvSearchFilterCategories.GroceryViewHolder>{

    private List<Grocery> horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String categoryName;

    public AdapterRvSearchFilterCategories(List<Grocery> horizontalGrocderyList, Context context , OnItemSelection onItemSelection1 , String categoryName1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        categoryName = categoryName1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_filter_grid_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        holder.tvProduct.setText(horizontalGrocderyList.get(position).getProductName().substring(0, 1).toUpperCase() + horizontalGrocderyList.get(position).getProductName().substring(1));
        //holder.cvItem.setRadius(20);
        if(!horizontalGrocderyList.get(position).getSelectedItem()){
            holder.llItem.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
            holder.tvProduct.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }else {
            holder.llItem.setBackgroundResource(R.drawable.bg_rounded_purple);
            holder.tvProduct.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String productName = horizontalGrocderyList.get(position).getProductName().toString();
               // Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
                onItemSelection.onItemSelected(categoryName,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduct;
        LinearLayout llItem;
        public GroceryViewHolder(View view) {
            super(view);
            tvProduct=view.findViewById(R.id.tvProduct);
            llItem=view.findViewById(R.id.llItem);

        }
    }
}

