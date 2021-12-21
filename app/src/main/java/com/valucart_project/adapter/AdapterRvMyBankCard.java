package com.valucart_project.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.models.Grocery;
import com.valucart_project.screen.CheckoutScreenPayment;

import java.util.List;

public class AdapterRvMyBankCard extends RecyclerView.Adapter<AdapterRvMyBankCard.GroceryViewHolder>{
    private List<Grocery> horizontalGrocderyList;
    Context context;

    public AdapterRvMyBankCard(List<Grocery> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bank_card_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
/*        holder.tvProductName.setText(horizontalGrocderyList.get(position).getProductName());
        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = horizontalGrocderyList.get(position).getProductName().toString();
                context.startActivity(new Intent(context, BundleSummaryScreen.class));
            }
        });*/

        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalGrocderyList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.lleditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CheckoutScreenPayment.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        LinearLayout llItemHeader;
        LinearLayout llDelete,lleditCard;
        public GroceryViewHolder(View view) {
            super(view);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            llDelete=view.findViewById(R.id.llDelete);
            lleditCard=view.findViewById(R.id.lleditCard);
        }
    }
}
