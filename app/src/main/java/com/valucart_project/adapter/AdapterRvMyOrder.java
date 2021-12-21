package com.valucart_project.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.MyOrder;

public class AdapterRvMyOrder extends RecyclerView.Adapter<AdapterRvMyOrder.GroceryViewHolder>{

    private MyOrder orderList;
    Context context;
    OnItemSelection onItemSelection;

    public AdapterRvMyOrder(MyOrder orderList1, Context context , OnItemSelection onItemSelection1){
        this.orderList= orderList1;
        this.context = context;
        onItemSelection = onItemSelection1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {

        holder.ivProductImage.setImageResource(R.mipmap.ic_order);
        holder.tvProductName.setText(orderList.getData().get(position).getPayment_type());
        holder.tvProductOrderId.setText(orderList.getData().get(position).getOrder_reference());
        holder.tvProductPrice.setText(""+orderList.getData().get(position).getPrice());
        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = orderList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                onItemSelection.onItemSelected("OrderDetail",position);
            }
        });

        holder.llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelection.onItemSelected("feedback",position);
            }
        });

        holder.llEditSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!orderList.getData().get(position).getIs_scheduled())
                    onItemSelection.onItemSelected("Schedule",position);
                else
                    Toast.makeText(context,"Already Scheduled",Toast.LENGTH_LONG).show();
            }
        });

        holder.llReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onItemSelection.onItemSelected("ReOrder",position);

            }
        });


        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvDate.setText(""+orderList.getData().get(position).getCreated_at());
        holder.tvStatus.setText(""+orderList.getData().get(position).getStatus());
        if(orderList.getData().get(position).getIs_scheduled())
        {
            holder.tvSchedule.setText("Scheduled");//colorWhiteWithLightGray
            holder.tvSchedule.setTextColor(context.getResources().getColor(R.color.colorWhiteWithLightGray));
        }else {
            holder.tvSchedule.setText("Schedule");
            holder.tvSchedule.setTextColor(context.getResources().getColor(R.color.colorAppLogo));
        }

    }

    @Override
    public int getItemCount() {
        return orderList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName,tvProductOldPrice,tvProductOrderId,tvProductPrice,tvStatus,tvDate,tvSchedule;
        LinearLayout llItemHeader,llFeedback,llEditSchedule,llReOrder;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            tvProductOldPrice=view.findViewById(R.id.tvProductOldPrice);
            tvProductOrderId=view.findViewById(R.id.tvProductOrderId);
            llFeedback=view.findViewById(R.id.llFeedback);
            llEditSchedule=view.findViewById(R.id.llEditSchedule);
            llReOrder=view.findViewById(R.id.llReOrder);

            tvProductPrice=view.findViewById(R.id.tvProductPrice);
            tvStatus=view.findViewById(R.id.tvStatus);
            tvDate=view.findViewById(R.id.tvDate);
            tvSchedule=view.findViewById(R.id.tvSchedule);
        }

    }

}

