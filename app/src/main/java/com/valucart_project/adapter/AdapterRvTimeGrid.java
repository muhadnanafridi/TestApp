package com.valucart_project.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.CheckoutTime;

public class AdapterRvTimeGrid extends RecyclerView.Adapter<AdapterRvTimeGrid.GroceryViewHolder>{

    private CheckoutTime horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;

    public AdapterRvTimeGrid(CheckoutTime horizontalGrocderyList1, Context context, OnItemSelection itemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList1;
        this.context = context;
        onItemSelection= itemSelection1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_time_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        holder.tvTime.setText(horizontalGrocderyList.getData().get(position).getFrom()+"-"+horizontalGrocderyList.getData().get(position).getTo());

        if(!horizontalGrocderyList.getData().get(position).getAvailable()) {
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
            holder.llItemHeader.setBackgroundResource(R.drawable.bg_rounded_gray);
        }else if(horizontalGrocderyList.getData().get(position).getSelectedItem().equals("true")) {
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
            holder.llItemHeader.setBackgroundResource(R.drawable.bg_rounded_curve_purple);
        }else {
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.llItemHeader.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
        }

        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!horizontalGrocderyList.getData().get(position).getAvailable()) {
                    Toast.makeText(context,"Currently this time slot is not available",Toast.LENGTH_LONG).show();
                }else onItemSelection.onItemSelected("",position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItemHeader;
        TextView tvTime;
        public GroceryViewHolder(View view) {
            super(view);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            tvTime=view.findViewById(R.id.tvTime);
        }
    }

}

