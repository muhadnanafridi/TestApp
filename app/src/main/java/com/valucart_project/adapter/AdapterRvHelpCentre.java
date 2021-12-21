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
import com.valucart_project.models.Grocery;

import java.util.List;

public class AdapterRvHelpCentre extends RecyclerView.Adapter<AdapterRvHelpCentre.GroceryViewHolder>{
    private List<Grocery> horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String nameItem;

    public AdapterRvHelpCentre(List<Grocery> horizontalGrocderyList, Context context, String nameItem1 , OnItemSelection onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem = nameItem1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_popular_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        holder.tvProduct.setText(horizontalGrocderyList.get(position).getProductName());
        holder.rlPopularList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                onItemSelection.onItemSelected(nameItem,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduct;
        RelativeLayout rlPopularList;
        public GroceryViewHolder(View view) {
            super(view);
            tvProduct=view.findViewById(R.id.tvProduct);
            rlPopularList=view.findViewById(R.id.rlPopularList);
        }
    }
}

