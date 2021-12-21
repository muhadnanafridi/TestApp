package com.valucart_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.valucart_project.R;
import com.valucart_project.models.WalletTransactions;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRvMyWallet extends RecyclerView.Adapter<AdapterRvMyWallet.GroceryViewHolder>{
    private WalletTransactions walletTransactions;
    Context context;

    public AdapterRvMyWallet(WalletTransactions walletTransactions1, Context context){
        this.walletTransactions= walletTransactions1;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_wallet_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(view);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.tvName.setText("Balance  "+walletTransactions.getData().get(position).getBalance());

        if(walletTransactions.getData().get(position).getType().equals("credit")){
            holder.tvType.setText("+");
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tvType.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }else {
            holder.tvType.setText(" -");
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tvType.setTextColor(context.getResources().getColor(R.color.colorRed));
            //holder.tvDate.setText("Purchase Item");
        }

        holder.tvDate.setText(""+walletTransactions.getData().get(position).getDescription());
        holder.tvTime.setText(""+walletTransactions.getData().get(position).getCreated_at());
        holder.tvPrice.setText(""+walletTransactions.getData().get(position).getTransaction_amount()+" ");

        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return walletTransactions.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvType,tvName,tvPrice,tvDate,tvTime;
        LinearLayout llItemHeader;

        public GroceryViewHolder(View view) {
            super(view);

            tvName=view.findViewById(R.id.tvName);
            tvType=view.findViewById(R.id.tvType);
            tvPrice=view.findViewById(R.id.tvPrice);
            tvDate=view.findViewById(R.id.tvDate);
            tvTime=view.findViewById(R.id.tvTime);

            llItemHeader=view.findViewById(R.id.llItemHeader);
        }
    }
}

