package com.valucart_project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Scheduled;

public class AdapterRvMySchedule extends RecyclerView.Adapter<AdapterRvMySchedule.GroceryViewHolder>{
    private Scheduled scheduledList;
    Context context;
    OnItemSelection onItemSelection;

    public AdapterRvMySchedule(Scheduled scheduledList1, Context context , OnItemSelection onItemSelection1){
        this.scheduledList= scheduledList1;
        this.context = context;
        onItemSelection = onItemSelection1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_schedule_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRemoveItem(position);
            }
        });
        holder.tvProductReference.setText(scheduledList.getData().get(position).getOrder_reference());
        holder.tvScheduleDate.setText(scheduledList.getData().get(position).getSchedule_next_date());
        holder.tvScheduleInterval.setText(scheduledList.getData().get(position).getSchedule_interval());
/*
        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
            }
        });
*/
        holder.llEditSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelection.onItemSelected("EditSchedule",position);
            }
        });
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRemoveItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return scheduledList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDelete;
        TextView tvProductReference,tvScheduleDate,tvScheduleInterval;
        LinearLayout llItemHeader,llDelete,llEditSchedule;

        public GroceryViewHolder(View view) {
            super(view);
            ivDelete=view.findViewById(R.id.ivDelete);
            tvProductReference=view.findViewById(R.id.tvProductReference);
            tvScheduleDate=view.findViewById(R.id.tvScheduleDate);
            tvScheduleInterval=view.findViewById(R.id.tvScheduleInterval);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            llEditSchedule=view.findViewById(R.id.llEditSchedule);
            llDelete=view.findViewById(R.id.llDelete);
        }
    }

    public void alertRemoveItem(final int position ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Schedule Alert")
                .setMessage("Are you sure, you want to remove Schedule?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onItemSelection.onItemSelected(""+scheduledList.getData().get(position).getOrder_id(),position);
                        //loadCartToRemoveItem("delete",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
